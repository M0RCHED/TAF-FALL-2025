package ca.etsmtl.taf.jmeter.controllers;


import ca.etsmtl.taf.entity.GatlingRequest;
import ca.etsmtl.taf.entity.GatlingResult;
import ca.etsmtl.taf.jmeter.JMeterRunner;
import ca.etsmtl.taf.jmeter.model.FTPTestPlan;
import ca.etsmtl.taf.jmeter.model.HttpTestPlan;
import ca.etsmtl.taf.jmeter.model.HttpTestResult;
import ca.etsmtl.taf.jmeter.model.TestPlanBase;
import ca.etsmtl.taf.jmeter.service.HttpTestPlanService;
import ca.etsmtl.taf.jmeter.service.HttpTestResultService;
import ca.etsmtl.taf.provider.GatlingJarPathProvider;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.exceptions.CsvException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/jmeter")
public class JmeterController {

  @Autowired
  private HttpTestPlanService testPlanService;

  @Autowired
  private HttpTestResultService testResultService;

  private ResponseEntity<?> executeTestPlan(TestPlanBase testPlan, String type, String testPlanId) {
    testPlan.generateTestPlan();
    ExecutorService executorService = Executors.newSingleThreadExecutor();

    //Submit the task to the ExecutorService
    AtomicReference<String> resultPathRef = new AtomicReference<>();
    Future<?> future = executorService.submit(() -> {
      String result = null;
      try {
        result = JMeterRunner.runJMeter(type);
      } catch (URISyntaxException e) {
        throw new RuntimeException(e);
      }
      resultPathRef.set(result);
    });


    try {
      //Wait for the task to finish
      future.get();
    } catch (ExecutionException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred during task execution.");
    } catch (InterruptedException e){
      Thread.currentThread().interrupt();
      return null;
    }
    finally {
      //Shutdown the ExecutorService to release resources
      executorService.shutdown();
    }

    //Convertir les résultats en JSON
    List<Map<String, String>> result = null;
    String resultPath = resultPathRef.get();
    if (resultPath != null && !resultPath.isEmpty()) {
      try {
        result = JMeterRunner.convertCSVtoJSON(resultPath);
      } catch (IOException | CsvException e) {
        throw new RuntimeException(e);
      }
    }

    //Enregistrer les résultats dans MongoDB
    if (result != null && !result.isEmpty()) {

      System.out.println(result);

      ObjectMapper objectMapper = new ObjectMapper();
      List<HttpTestResult> testResults = objectMapper.convertValue(result, new TypeReference<List<HttpTestResult>>() {});

      //Associer chaque résultat à l'ID du test
      testResults.forEach(r -> r.setTestPlanId(testPlanId));

      //Enregistrer dans MongoDB
      List<HttpTestResult> savedResults = testResultService.saveTestResults(testResults);
      System.out.println("Résultats enregistrés avec succès !");
      return ResponseEntity.ok(savedResults);
    } else {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Aucun résultat généré par JMeter.");
    }

  }

  @PostMapping("/http")
  public ResponseEntity<?> getJmeterTestPlan(@RequestBody HttpTestPlan jmeterTestPlan) throws IOException, CsvException {

    //Enregistrer dans MongoDB
    HttpTestPlan savedTestPlan = testPlanService.saveTestPlan(jmeterTestPlan);

    //Exécuter le test JMeter
    return executeTestPlan(jmeterTestPlan, "http", savedTestPlan.getId());
  }

  @GetMapping("/requests")
  public ResponseEntity<List<HttpTestPlan>> getAllRequests() {
    return ResponseEntity.ok(testPlanService.getAllRequests());
  }

  @GetMapping("/results")
  public ResponseEntity<List<HttpTestResult>> getResultsForTest(
          @RequestParam String testPlanId) {
    return ResponseEntity.ok(testResultService.getResultsByTestPlanId(testPlanId));
  }
}