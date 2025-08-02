package ca.etsmtl.taf.entity;
import java.util.Date;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.annotation.Id;

import lombok.Getter;
import lombok.Setter;

@Document(collection = "t_test_step")
@Getter
@Setter
public class TestStep {

    @Id
    private String id;

    @DBRef
    private TestCase testCase;

    private String name;
    private String description;

    @CreatedDate
    private Date createdDate;

    @CreatedBy
    private String createdBy;

    // Statut a venir : En cours, Succes, Echec, Pas commence.
    private StepStatus status;

}
