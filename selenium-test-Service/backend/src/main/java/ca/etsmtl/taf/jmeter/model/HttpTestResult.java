package ca.etsmtl.taf.jmeter.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection = "jmeter_http_test_results")
public class HttpTestResult {

    @Id
    private String id;
    private String testPlanId;

    @JsonProperty("allThreads") // ✅ Correspondance exacte avec le JSON
    private String allThreads;

    @JsonProperty("grpThreads")
    private String grpThreads;

    @JsonProperty("label")
    private String label;

    @JsonProperty("threadName")
    private String threadName;

    @JsonProperty("URL") // ⚠️ JSON utilise "URL", Java doit le mapper correctement
    private String url;

    @JsonProperty("responseCode")
    private String responseCode;

    @JsonProperty("responseMessage")
    private String responseMessage;

    @JsonProperty("success")
    private String success;

    @JsonProperty("failureMessage")
    private String failureMessage;

    @JsonProperty("elapsed")
    private String elapsed;

    @JsonProperty("bytes")
    private String bytes;

    @JsonProperty("sentBytes")
    private String sentBytes;

    @JsonProperty("Latency") // ⚠️ JSON a "Latency" avec majuscule
    private String latency;

    @JsonProperty("Connect") // ⚠️ JSON a "Connect" avec majuscule
    private String connect;

    @JsonProperty("dataType")
    private String dataType;

    @JsonProperty("timeStamp")
    private String timeStamp;

    @JsonProperty("IdleTime")
    private String idleTime;

    // Constructeurs
    public HttpTestResult() {}

    public HttpTestResult(String testPlanId, String allThreads, String grpThreads,  String label, String threadName, String url, String responseCode,
                          String responseMessage, String success, String failureMessage, String elapsed,
                          String bytes, String sentBytes, String latency, String connect, String dataType, String timeStamp, String idleTime) {
        this.testPlanId = testPlanId;
        this.allThreads = allThreads;
        this.grpThreads = grpThreads;
        this.label = label;
        this.threadName = threadName;
        this.url = url;
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
        this.success = success;
        this.failureMessage = failureMessage;
        this.elapsed = elapsed;
        this.bytes = bytes;
        this.sentBytes = sentBytes;
        this.latency = latency;
        this.connect = connect;
        this.dataType = dataType;
        this.timeStamp = timeStamp;
        this.idleTime = idleTime;
    }

    // Getters et Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTestPlanId() { return testPlanId; }
    public void setTestPlanId(String testPlanId) { this.testPlanId = testPlanId; }

    public String getAllThreads() { return allThreads; }
    public void setAllThreads(String allThreads) { this.allThreads = allThreads; }

    public String getGrpThreads() { return grpThreads; }
    public void setGrpThreads(String grpThreads) {
        this.grpThreads = grpThreads;
    }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    public String getThreadName() { return threadName; }
    public void setThreadName(String threadName) { this.threadName = threadName; }

    public String getURL() { return url; }
    public void setURL(String URL) { this.url = URL; }

    public String getResponseCode() { return responseCode; }
    public void setResponseCode(String responseCode) { this.responseCode = responseCode; }

    public String getResponseMessage() { return responseMessage; }
    public void setResponseMessage(String responseMessage) { this.responseMessage = responseMessage; }

    public String getSuccess() { return success; }
    public void setSuccess(String success) { this.success = success; }

    public String getFailureMessage() { return failureMessage; }
    public void setFailureMessage(String failureMessage) { this.failureMessage = failureMessage; }

    public String getElapsed() { return elapsed; }
    public void setElapsed(String elapsed) { this.elapsed = elapsed; }

    public String getBytes() { return bytes; }
    public void setBytes(String bytes) { this.bytes = bytes; }

    public String getSentBytes() { return sentBytes; }
    public void setSentBytes(String sentBytes) { this.sentBytes = sentBytes; }

    public String getLatency() { return latency; }
    public void setLatency(String latency) { this.latency = latency; }

    public String getConnect() { return connect; }
    public void setConnect(String connect) { this.connect = connect; }

    public String getDataType() { return dataType; }
    public void setDataType(String dataType) { this.dataType = dataType; }

    public String getTimeStamp() { return timeStamp; }
    public void setTimeStamp(String timeStamp) { this.timeStamp = timeStamp; }

    public String getIdleTime() { return idleTime; }
    public void setIdleTime(String idleTime) { this.idleTime = idleTime; }

}
