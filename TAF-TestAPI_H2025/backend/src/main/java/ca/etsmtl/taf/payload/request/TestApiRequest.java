package ca.etsmtl.taf.payload.request;
import javax.validation.constraints.NotBlank;
import java.util.Map;

public class TestApiRequest {
    @NotBlank
    private String method;

    @NotBlank
    private String apiUrl;

    private int statusCode;
    
    private String input;
    
    private String expectedOutput;

    private Map<String, String> headers; // Ajout du champ manquant

    // Getters et setters
    public String getMethod() { return this.method; }
    public void setMethod(String method) { this.method = method; }

    public String getApiUrl() { return this.apiUrl; }
    public void setApiUrl(String apiUrl) { this.apiUrl = apiUrl; }

    public void setStatusCode(int statusCode) { this.statusCode = statusCode; }
    public int getStatusCode() { return this.statusCode; }

    public String getInput() { return this.input; }
    public void setInput(String input) { this.input = input; }

    public String getExpectedOutput() { return this.expectedOutput; }
    public void setExpectedOutput(String expectedOutput) { this.expectedOutput = expectedOutput; }

    public Map<String, String> getHeaders() { return headers; }
    public void setHeaders(Map<String, String> headers) { this.headers = headers; }
}
