package ca.etsmtl.taf.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertCallback;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@Document(collection = "t_test_case")
@Getter
@Setter
public class TestCase {
    @Transient
    public static final String API_NAME = "testCase";

    @Id
    private String id;

    @CreatedDate
    private Date createdDate;

    public String testSuiteId;
    public String name;
    public String description;
    public String httpMethod; // GET, POST, PUT, DELETE
    public String url;
    public Map<String, String> headers;
    public String requestBody;
    public int expectedStatusCode;
    public String expectedResponseBody;

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
class TestCaseCallback implements BeforeConvertCallback<TestCase> {
    @Override
    public TestCase onBeforeConvert(TestCase testCase, String collection) {
        testCase.setCreatedDate(new Date());
        return testCase;
    }

}