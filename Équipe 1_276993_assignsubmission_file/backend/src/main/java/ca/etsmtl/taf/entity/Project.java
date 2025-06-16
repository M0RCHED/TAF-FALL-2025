package ca.etsmtl.taf.entity;

import java.util.Date;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "t_project")
@Getter
@Setter
public class Project {

    @Id
    private Long id;
	
	private String name;
	private String description;
	private Date startDate;
	private Date endDate;
	

	private TestUser owner;

    @CreatedDate
    private Date createdDate;

    @CreatedBy
    private String createdBy;	

}
