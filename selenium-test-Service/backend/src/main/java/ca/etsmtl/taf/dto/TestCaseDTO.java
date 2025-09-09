package ca.etsmtl.taf.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TestCaseDTO {
    public String name;
    public String description;
    public String testSuiteId;
    public String httpMethod;
    public String url;
    public String requestBody;
    public String expectedResponseBody;
    public int expectedStatusCode;
}
