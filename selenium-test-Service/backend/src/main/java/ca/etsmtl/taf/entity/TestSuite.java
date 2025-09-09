package ca.etsmtl.taf.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertCallback;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document(collection = "t_test_suite")
@Getter
@Setter
public class TestSuite {
    @Transient
    public static final String API_NAME = "testSuite";

    @Id
    private String id;

    @CreatedDate
    private Date createdDate;

    public String testPlanId;
    public String name;
    public String description;
    public List<String> testCaseIds = new ArrayList<>();

    @Transient
    public List<TestCase> testCases;

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
class TestSuiteCallback implements BeforeConvertCallback<TestSuite> {
    @Override
    public TestSuite onBeforeConvert(TestSuite testSuite, String collection) {
        testSuite.setCreatedDate(new Date());
        return testSuite;
    }

}