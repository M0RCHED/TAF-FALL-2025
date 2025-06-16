package ca.etsmtl.taf.controller;

import ca.etsmtl.taf.dto.TestCaseDTO;
import ca.etsmtl.taf.dto.TestPlanDTO;
import ca.etsmtl.taf.dto.TestSuiteDTO;
import ca.etsmtl.taf.entity.TestCase;
import ca.etsmtl.taf.entity.TestPlan;
import ca.etsmtl.taf.entity.TestSuite;
import ca.etsmtl.taf.service.TestPlanService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ca.etsmtl.taf.payload.request.TestApiRequest;
import jakarta.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;



@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/testapi")
public class TestApiController {
    private final TestPlanService testPlanService;

    public TestApiController(TestPlanService testPlanService) {
        this.testPlanService = testPlanService;
    }

    @Value("${taf.app.testAPI_url}")
    String Test_API_microservice_url;

    @Value("${taf.app.testAPI_port}")
    String Test_API_microservice_port;

    @PostMapping("/checkApi")
    public ResponseEntity<String> testApi(@Valid @RequestBody TestApiRequest testApiRequest) throws URISyntaxException, IOException, InterruptedException {
        var uri = new URI(Test_API_microservice_url+":"+Test_API_microservice_port+"/microservice/testapi/checkApi");
        uri.toString().trim();
        ObjectMapper objectMapper = new ObjectMapper();

        String requestBody = objectMapper
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(testApiRequest);

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder(uri)
                .header("Content-Type", "application/json")
                .POST(BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response =
                client.send(request, BodyHandlers.ofString());
        return ResponseEntity.ok(response.body());
    }

    @GetMapping("/testPlans")
    public ResponseEntity<List<TestPlan>> getTestPlans() {
        List<TestPlan> testPlans = testPlanService.getTestPlansWithDetails();
        return ResponseEntity.ok(testPlans);
    }

    @PostMapping("/{type}")
    public ResponseEntity<?> addElement(@PathVariable String type, @RequestBody Object data) {
        Object savedElement = testPlanService.createElement(type, data);
        return ResponseEntity.ok(savedElement);
    }

    @GetMapping("/{type}/{id}")
    public ResponseEntity<?> getElementById(@PathVariable String type, @PathVariable String id) {
        Optional<?> result = testPlanService.getElementById(type, id);
        return result.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{type}/{id}")
    public ResponseEntity<?> updateElement(@PathVariable String type, @PathVariable String id, @RequestBody Object updatedData) {
        Object updatedElement = testPlanService.updateElement(type, id, updatedData);
        return updatedElement != null ? ResponseEntity.ok(updatedElement) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{type}/{id}")
    public ResponseEntity<Void> deleteElement(@PathVariable String type, @PathVariable String id) {
        testPlanService.deleteElement(type, id);
        return ResponseEntity.noContent().build();
    }
}
