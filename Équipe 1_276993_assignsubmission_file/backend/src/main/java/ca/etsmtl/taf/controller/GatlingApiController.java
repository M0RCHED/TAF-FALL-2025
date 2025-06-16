package ca.etsmtl.taf.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ca.etsmtl.taf.entity.GatlingResult;
import ca.etsmtl.taf.service.GatlingRequestService;
import ca.etsmtl.taf.service.GatlingResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ca.etsmtl.taf.entity.GatlingRequest;
import ca.etsmtl.taf.payload.response.MessageResponse;
import ca.etsmtl.taf.provider.GatlingJarPathProvider;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/gatling")
public class GatlingApiController {

    @Autowired
    private GatlingRequestService gatlingRequestService;

    @Autowired
    private GatlingResultService gatlingResultService;

    static boolean isWindows = System.getProperty("os.name").toLowerCase().contains("win");

    /**
     * @param gatlingRequest
     * @return
     */
    @PostMapping(value = "/runSimulation")
    public ResponseEntity<MessageResponse> runSimulation(@RequestBody GatlingRequest gatlingRequest) {
        try {

            // Valider et enregistrer la requête dans MongoDB
            gatlingRequestService.saveGatlingRequest(gatlingRequest);

            String gatlingJarPath = new GatlingJarPathProvider().getGatlingJarPath();
            String testRequest = isWindows
                    ? "{\\\"baseUrl\\\":\\\"" + gatlingRequest.getTestBaseUrl() + "\\\",\\\"scenarioName\\\":\\\""
                            + gatlingRequest.getTestScenarioName() + "\\\",\\\"requestName\\\":\\\""
                            + gatlingRequest.getTestRequestName() + "\\\",\\\"uri\\\":\\\""
                            + gatlingRequest.getTestUri() + "\\\",\\\"requestBody\\\":\\\""
                            + gatlingRequest.getTestRequestBody() + "\\\",\\\"methodType\\\":\\\""
                            + gatlingRequest.getTestMethodType() + "\\\",\\\"usersNumber\\\":\\\""
                            + gatlingRequest.getTestUsersNumber() + "\\\"}"
                    :

                    "{\"baseUrl\":\"" + gatlingRequest.getTestBaseUrl()
                            + "\",\"scenarioName\":\"" + gatlingRequest.getTestScenarioName()
                            + "\",\"requestName\":\"" + gatlingRequest.getTestRequestName() + "\",\"uri\":\""
                            + gatlingRequest.getTestUri() + "\",\"requestBody\":\""
                            + gatlingRequest.getTestRequestBody() + "\",\"methodType\":\""
                            + gatlingRequest.getTestMethodType() + "\",\"usersNumber\":\""
                            + gatlingRequest.getTestUsersNumber() + "\"}";

            StringBuilder gatlingCommand = new StringBuilder();
            gatlingCommand.append("java -jar ");
            gatlingCommand.append(gatlingJarPath);
            gatlingCommand.append(" -DrequestJson=");
            gatlingCommand.append(testRequest);

            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec(gatlingCommand.toString());

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            int exitCode = process.waitFor();

            if (exitCode == 0) {

                // Parser le résultat de Gatling
                String sampleOutput = parseOutput(output.toString());
                GatlingResult result = parseGatlingOutput(sampleOutput);

                // Ajouter les informations de la requête Gatling au résultat
                result.setTestBaseUrl(gatlingRequest.getTestBaseUrl());
                result.setTestScenarioName(gatlingRequest.getTestScenarioName());
                result.setTestRequestName(gatlingRequest.getTestRequestName());
                result.setTestMethodType(gatlingRequest.getTestMethodType());

                // Enregistrer le résultat dans MongoDB
                gatlingResultService.saveGatlingResult(result);

                return new ResponseEntity<>(new MessageResponse(parseOutput(output.toString())),
                        HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new MessageResponse(output.toString()),
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (IOException e) {
            return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }

    private String parseOutput(String output) {
        String returnString = "---- Global Information --------------------------------------------------------\n";

        String startPattern = "---- Global Information --------------------------------------------------------";
        String endPattern = "---- Response Time Distribution ------------------------------------------------";
        Pattern pattern = Pattern.compile(startPattern + "(.*?)" + endPattern, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(output);
        if (matcher.find()) {
            returnString += matcher.group(1).trim() + "\n";
        } else {
            returnString += "Not found in Gatling output.";
        }

        returnString += "---- Generated Report ------------------------------------------------------\n";

        String regex = "Please open the following file: (file:///[^\s]+|https?://[^\s]+)";

        pattern = Pattern.compile(regex, Pattern.MULTILINE);
        matcher = pattern.matcher(output);

        if (matcher.find()) {
            for (int i = 1; i <= matcher.groupCount(); i++) {
                returnString += matcher.group(i).trim();
            }
        } else {
            returnString += "Not found in Gatling output.";
        }

        return returnString;
    }

    public GatlingResult parseGatlingOutput(String output) {
        GatlingResult result = new GatlingResult();

        Pattern pattern = Pattern.compile(
                "request count\\s+(\\d+) .*?" +
                        "min response time\\s+(\\d+) .*?" +
                        "max response time\\s+(\\d+) .*?" +
                        "mean response time\\s+(\\d+) .*?" +
                        "std deviation\\s+(\\d+) .*?" +
                        "response time 50th percentile\\s+(\\d+) .*?" +
                        "response time 75th percentile\\s+(\\d+) .*?" +
                        "response time 95th percentile\\s+(\\d+) .*?" +
                        "response time 99th percentile\\s+(\\d+) .*?" +
                        "mean requests/sec\\s+(\\d+)",
                Pattern.DOTALL
        );

        Matcher matcher = pattern.matcher(output);
        if (matcher.find()) {
            result.setRequestCount(Integer.parseInt(matcher.group(1)));
            result.setMinResponseTime(Long.parseLong(matcher.group(2)));
            result.setMaxResponseTime(Long.parseLong(matcher.group(3)));
            result.setMeanResponseTime(Long.parseLong(matcher.group(4)));
            result.setStandardDeviation(Double.parseDouble(matcher.group(5)));
            result.setResponse50thPercentile(Long.parseLong(matcher.group(6)));
            result.setResponse75thPercentile(Long.parseLong(matcher.group(7)));
            result.setResponse95thPercentile(Long.parseLong(matcher.group(8)));
            result.setResponse99thPercentile(Long.parseLong(matcher.group(9)));
            result.setMeanRequestsPerSec(Double.parseDouble(matcher.group(10)));
        } else {
            throw new RuntimeException("Gatling output parsing failed!");
        }

        return result;
    }

    @GetMapping("/requests")
    public ResponseEntity<List<GatlingRequest>> getAllRequests() {
        return ResponseEntity.ok(gatlingRequestService.getAllRequests());
    }

    @GetMapping("/results")
    public ResponseEntity<List<GatlingResult>> getResultsForTest(
            @RequestParam String requestName) {
        return ResponseEntity.ok(gatlingResultService.getResultsForTest(requestName));
    }
}
