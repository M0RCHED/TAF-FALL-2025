package ca.etsmtl.taf.entity;
import java.util.Date;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.annotation.Id;

import lombok.Getter;
import lombok.Setter;

@Document(collection = "t_test_plan")
@Getter
@Setter
public class TestPlan {

    @Id
    private String id;

    @DBRef
    private Project project; // plusieurs projets possibles

    private String name;

    private String description;

    @DBRef
    private TestUser responsable;

    @CreatedDate
    private Date createdDate;

    @CreatedBy
    private String createdBy;

    //Statut : Pourcentage Succes, Echec...
    private Status status;
    //Priorite ?
    private Priority priority;
}
