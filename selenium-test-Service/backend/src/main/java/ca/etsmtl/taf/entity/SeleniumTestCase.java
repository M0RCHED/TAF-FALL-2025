package ca.etsmtl.taf.entity;

import java.util.Date;
import java.util.List;

import ca.etsmtl.taf.selenium.payload.requests.SeleniumAction;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

//SeleniumTestCase

@Document(collection = "selenium_test_cases")
@Getter
@Setter
public class SeleniumTestCase {

    @Id
    private String id;
    private int case_id;
    private String caseName;
    private List<SeleniumAction> actions;
    private List<String> actionResults;  // Liste des résultats des actions (succès ou échec)

    // Ajout d'un champ timestamp
    private long timestamp;

    // Ajout du champ pour le message d'erreur
    private String errorMessage;  // Champ pour stocker les erreurs

}