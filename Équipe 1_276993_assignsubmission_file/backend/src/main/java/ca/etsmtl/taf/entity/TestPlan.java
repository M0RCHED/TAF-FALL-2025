package ca.etsmtl.taf.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertCallback;
import org.springframework.stereotype.Component;

import java.util.*;

@Document(collection = "t_test_plan")
@Getter
@Setter
public class TestPlan {
    @Transient
    public static final String API_NAME = "testPlan";

    @Id
    private String id;

    @CreatedDate
    private Date createdDate;

    public String name;
    public String description;
    public List<String> testSuiteIds = new ArrayList<>();

    @Transient
    public List<TestSuite> testSuites;

    public void setCreatedDate(Date date) {
        this.createdDate = date;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

@Component
class TestPlanCallback implements BeforeConvertCallback<TestPlan> {
    @Override
    public TestPlan onBeforeConvert(TestPlan testPlan, String collection) {
        testPlan.setCreatedDate(new Date());
        return testPlan;
    }

}
