package ca.etsmtl.taf.entity;

import java.util.Date;


import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "t_test_step")
@Getter
@Setter
public class TestStep {

    @Id
    private Long id;

    private TestCase testCase;
    private String name;
    private String description;

    @CreatedDate
    private Date createdDate;

    @CreatedBy
    private String createdBy;
    
	// Statut : En cours, Succès, Échec, Pas commencé.

}
