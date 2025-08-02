package ca.etsmtl.taf.entity;

import java.util.Date;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import lombok.Getter;
import lombok.Setter;

@Document(collection = "t_test_case")
@Getter
@Setter
public class TestCase {

    @Id
    private String id;

    @DBRef
    private TestSuite testSuite;

    @DBRef
    private TestPlan testPlan;

    private String name;

    private String description;

    @CreatedDate
    private Date createdDate;

    @CreatedBy
    private String createdBy;

    // Priorite ?
    private PriorityLevel priority;
    // Statut : En cours, Succes, Echec, Pas commence.
    private StepStatus status;
}