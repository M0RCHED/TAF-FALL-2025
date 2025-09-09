package ca.etsmtl.taf.entity;

import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "t_test_user")
@Getter
@Setter
public class TestUser {

    @Id
    private Long id;

    private User systemUser;
    private Set<Project> projects;

}
