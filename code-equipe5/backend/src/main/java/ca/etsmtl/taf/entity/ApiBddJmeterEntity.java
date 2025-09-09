package ca.etsmtl.taf.entity;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "JMETER_RESULTS", schema = "tf")
public class ApiBddJmeterEntity implements Serializable {

     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("timeStamp")
    @Column(name = "time_stamp", nullable = false)
    private Long timeStamp;

    @JsonProperty("date")
    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @JsonProperty("elapsed")
    @Column(name = "elapsed", nullable = false)
    private Integer elapsed;

    @JsonProperty("label")
    @Column(name = "label", length = 255)
    private String label;

    @JsonProperty("responseCode")
    @Column(name = "response_code", length = 50)
    private String responseCode;

    @JsonProperty("responseMessage")
    @Column(name = "response_message", length = 255)
    private String responseMessage;

    @JsonProperty("threadName")
    @Column(name = "thread_name", length = 255)
    private String threadName;

    @JsonProperty("dataType")
    @Column(name = "data_type", length = 50)
    private String dataType;

    @JsonProperty("success")
    @Column(name = "success")
    private Boolean success;

    @JsonProperty("failureMessage")
    @Column(name = "failure_message")
    private String failureMessage;

    @JsonProperty("bytes")
    @Column(name = "bytes")
    private Integer bytes;

    @JsonProperty("sentBytes")
    @Column(name = "sent_bytes")
    private Integer sentBytes;

    @JsonProperty("grpThreads")
    @Column(name = "grp_threads")
    private Integer grpThreads;

    @JsonProperty("allThreads")
    @Column(name = "all_threads")
    private Integer allThreads;

    @JsonProperty("URL")
    @Column(name = "url", length = 500)
    private String url;

    @JsonProperty("Latency")
    @Column(name = "latency")
    private Integer latency;

    @JsonProperty("IdleTime")
    @Column(name = "idle_time")
    private Integer idleTime;

    @JsonProperty("Connect")
    @Column(name = "connect")
    private Integer connect;


    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Integer getElapsed() {
        return elapsed;
    }

    public void setElapsed(Integer elapsed) {
        this.elapsed = elapsed;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getFailureMessage() {
        return failureMessage;
    }

    public void setFailureMessage(String failureMessage) {
        this.failureMessage = failureMessage;
    }

    public Integer getBytes() {
        return bytes;
    }

    public void setBytes(Integer bytes) {
        this.bytes = bytes;
    }

    public Integer getSentBytes() {
        return sentBytes;
    }

    public void setSentBytes(Integer sentBytes) {
        this.sentBytes = sentBytes;
    }

    public Integer getGrpThreads() {
        return grpThreads;
    }

    public void setGrpThreads(Integer grpThreads) {
        this.grpThreads = grpThreads;
    }

    public Integer getAllThreads() {
        return allThreads;
    }

    public void setAllThreads(Integer allThreads) {
        this.allThreads = allThreads;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getLatency() {
        return latency;
    }

    public void setLatency(Integer latency) {
        this.latency = latency;
    }

    public Integer getIdleTime() {
        return idleTime;
    }

    public void setIdleTime(Integer idleTime) {
        this.idleTime = idleTime;
    }

    public Integer getConnect() {
        return connect;
    }

    public void setConnect(Integer connect) {
        this.connect = connect;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
