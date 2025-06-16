package ca.etsmtl.taf.selenium.payload;

import ca.etsmtl.taf.entity.GatlingRequest;
import ca.etsmtl.taf.entity.SeleniumActionRequest;
import ca.etsmtl.taf.entity.SeleniumTestCase;
import ca.etsmtl.taf.selenium.payload.requests.SeleniumAction;
import ca.etsmtl.taf.selenium.payload.requests.SeleniumCase;
import ca.etsmtl.taf.selenium.payload.requests.SeleniumResponse;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Component;
import java.sql.Timestamp;
import java.time.Duration;
import java.util.List;
import ca.etsmtl.taf.repository.SeleniumCaseRepository;
import org.openqa.selenium.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.ArrayList;

@Component
public class SeleniumTestService {

    @Autowired
    private SeleniumCaseRepository seleniumCaseRepository;

    public SeleniumResponse executeTestCase(SeleniumCase seleniumCase) {
        List<SeleniumAction> seleniumActions = seleniumCase.getActions();

        // Convertir les SeleniumActions en SeleniumActionRequest
        List<SeleniumActionRequest> seleniumActionRequests = convertToSeleniumActionRequests(seleniumActions);

        SeleniumResponse seleniumResponse = new SeleniumResponse();
        seleniumResponse.setCase_id(seleniumCase.getCase_id());
        seleniumResponse.setCaseName(seleniumCase.getCaseName());
        seleniumResponse.setSeleniumActions(seleniumActions);
        long currentTimestamp = (new Timestamp(System.currentTimeMillis())).getTime();
        seleniumResponse.setTimestamp(currentTimestamp / 1000);

        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        long startTime = System.currentTimeMillis();

        // Créer l'objet SeleniumTestCase avec les informations nécessaires
        SeleniumTestCase seleniumTestCase = new SeleniumTestCase();
        seleniumTestCase.setCase_id(seleniumCase.getCase_id());
        seleniumTestCase.setCaseName(seleniumCase.getCaseName());
        seleniumTestCase.setActions(seleniumCase.getActions());
        seleniumTestCase.setTimestamp(currentTimestamp / 1000);

        // Liste pour stocker les résultats des actions
        List<String> actionResults = new ArrayList<>();

        try {
            for (SeleniumActionRequest seleniumActionRequest : seleniumActionRequests) {
                String actionResult = null;
                try {
                    if (seleniumActionRequest.getAction_type_id() == 1) {
                        driver.get(seleniumActionRequest.getInput());
                        actionResult = "Success";
                    }
                } catch (Exception e) {
                    actionResult = "Failure: " + e.getMessage();
                }

                // Si actionResult est null, le remplacer par "Failed"
                if (actionResult == null) {
                    actionResult = "Failed";
                }

                actionResults.add(actionResult);
            }

            driver.quit();
            seleniumResponse.setDuration(System.currentTimeMillis() - startTime);
            seleniumResponse.setSuccess(true);

            // Ajouter les résultats des actions à l'objet SeleniumTestCase
            seleniumTestCase.setActionResults(actionResults);

            // Sauvegarde des résultats dans MongoDB après exécution
            seleniumCaseRepository.save(seleniumTestCase);

        } catch (Exception e) {
            setFailureResponse(seleniumResponse, driver, startTime, e.getMessage());
            seleniumTestCase.setErrorMessage(e.getMessage());
            seleniumCaseRepository.save(seleniumTestCase);
        }

        return seleniumResponse;
    }

    private List<SeleniumActionRequest> convertToSeleniumActionRequests(List<SeleniumAction> seleniumActions) {
        List<SeleniumActionRequest> seleniumActionRequests = new ArrayList<>();

        for (SeleniumAction seleniumAction : seleniumActions) {
            SeleniumActionRequest actionRequest = new SeleniumActionRequest();
            actionRequest.setAction_type_id(seleniumAction.getAction_type_id());
            actionRequest.setInput(seleniumAction.getInput());
            seleniumActionRequests.add(actionRequest);
        }

        return seleniumActionRequests;
    }

    private void setFailureResponse(SeleniumResponse seleniumResponse, WebDriver driver, long startTime, String message) {
        driver.quit();
        seleniumResponse.setSuccess(false);
        seleniumResponse.setOutput(message);
        seleniumResponse.setDuration(System.currentTimeMillis() - startTime);
    }

    public List<SeleniumTestCase> getAllRequests() {
        return seleniumCaseRepository.findAll();
    }

}
