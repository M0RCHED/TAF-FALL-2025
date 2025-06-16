package ca.etsmtl.taf.entity;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "gatling_results")
public class GatlingResult {

    // Attributs de la requête Gatling
    private String testBaseUrl;
    private String testScenarioName;
    private String testRequestName;
    private String testMethodType;

    // Attributs liés aux performances des tests Gatling
    private int requestCount;
    private long minResponseTime;
    private long maxResponseTime;
    private long meanResponseTime;
    private double standardDeviation;
    private long response50thPercentile;
    private long response75thPercentile;
    private long response95thPercentile;
    private long response99thPercentile;
    private double meanRequestsPerSec;

    // Constructeur par défaut
    public GatlingResult() {
    }

    // Getters et Setters
    public String getTestBaseUrl() {
        return testBaseUrl;
    }

    public void setTestBaseUrl(String testBaseUrl) {
        this.testBaseUrl = testBaseUrl;
    }

    public String getTestScenarioName() {
        return testScenarioName;
    }

    public void setTestScenarioName(String testScenarioName) {
        this.testScenarioName = testScenarioName;
    }

    public String getTestRequestName() {
        return testRequestName;
    }

    public void setTestRequestName(String testRequestName) {
        this.testRequestName = testRequestName;
    }

    public String getTestMethodType() {
        return testMethodType;
    }

    public void setTestMethodType(String testMethodType) {
        this.testMethodType = testMethodType;
    }

    public int getRequestCount() {
        return requestCount;
    }

    public void setRequestCount(int requestCount) {
        this.requestCount = requestCount;
    }

    public long getMinResponseTime() {
        return minResponseTime;
    }

    public void setMinResponseTime(long minResponseTime) {
        this.minResponseTime = minResponseTime;
    }

    public long getMaxResponseTime() {
        return maxResponseTime;
    }

    public void setMaxResponseTime(long maxResponseTime) {
        this.maxResponseTime = maxResponseTime;
    }

    public long getMeanResponseTime() {
        return meanResponseTime;
    }

    public void setMeanResponseTime(long meanResponseTime) {
        this.meanResponseTime = meanResponseTime;
    }

    public double getStandardDeviation() {
        return standardDeviation;
    }

    public void setStandardDeviation(double standardDeviation) {
        this.standardDeviation = standardDeviation;
    }

    public long getResponse50thPercentile() {
        return response50thPercentile;
    }

    public void setResponse50thPercentile(long response50thPercentile) {
        this.response50thPercentile = response50thPercentile;
    }

    public long getResponse75thPercentile() {
        return response75thPercentile;
    }

    public void setResponse75thPercentile(long response75thPercentile) {
        this.response75thPercentile = response75thPercentile;
    }

    public long getResponse95thPercentile() {
        return response95thPercentile;
    }

    public void setResponse95thPercentile(long response95thPercentile) {
        this.response95thPercentile = response95thPercentile;
    }

    public long getResponse99thPercentile() {
        return response99thPercentile;
    }

    public void setResponse99thPercentile(long response99thPercentile) {
        this.response99thPercentile = response99thPercentile;
    }

    public double getMeanRequestsPerSec() {
        return meanRequestsPerSec;
    }

    public void setMeanRequestsPerSec(double meanRequestsPerSec) {
        this.meanRequestsPerSec = meanRequestsPerSec;
    }

    @Override
    public String toString() {
        return "GatlingResult{" +
                ", testBaseUrl='" + testBaseUrl + '\'' +
                ", testScenarioName='" + testScenarioName + '\'' +
                ", testRequestName='" + testRequestName + '\'' +
                ", testMethodType='" + testMethodType + '\'' +
                ", requestCount=" + requestCount +
                ", minResponseTime=" + minResponseTime +
                ", maxResponseTime=" + maxResponseTime +
                ", meanResponseTime=" + meanResponseTime +
                ", standardDeviation=" + standardDeviation +
                ", response50thPercentile=" + response50thPercentile +
                ", response75thPercentile=" + response75thPercentile +
                ", response95thPercentile=" + response95thPercentile +
                ", response99thPercentile=" + response99thPercentile +
                ", meanRequestsPerSec=" + meanRequestsPerSec +
                '}';
    }
}
