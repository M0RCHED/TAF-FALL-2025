package ca.etsmtl.taf.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Map;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "test_cases") // Nom de la collection MongoDB
public class TestCase {

    @Id
    private String id; // Mappé à partir de "_id"

    private String runId; // La "clé étrangère" vers le TestRun
    private String project;
    private String suite;
    private String type;
    private String tool;
    private String name;
    private String status;
    private String executedBy;
    private Instant executedAt;
    private long durationMs;

    @JsonProperty("env")
    private CaseEnvironment environment;

    private List<TestStep> steps;

    @JsonProperty("metrics")
    private CaseMetrics caseMetrics;

    private List<Requirement> requirements;
    private List<Defect> defects;

    @JsonProperty("links")
    private CaseLinks caseLinks;

    @JsonProperty("error")
    private ErrorDetails errorDetails;
}

// --- Classes imbriquées pour TestCase ---

@Data
@NoArgsConstructor
@AllArgsConstructor
class CaseEnvironment {
    private String os;
    private String browser;
    private String resolution;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // N'inclut pas les champs nuls (comme "error")
class TestStep {
    private String name;
    private String status;
    private long durationMs;
    private String error; // Sera null si "status" est "passed"
}

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // Les champs api, ui, perf seront souvent nuls
class CaseMetrics {
    private ApiMetrics api;
    private UiMetrics ui;
    private PerformanceMetrics performance; // Réutilisation de la classe de TestRun
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class ApiMetrics {
    private String method;
    private String url;
    private int statusCode;
    private int latencyMs;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class UiMetrics {
    private int actionsCount;
    private List<String> screenshots;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class Requirement {
    private String id;
    private String status;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class Defect {
    private String id;
    private String status;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class CaseLinks {
    private String reportUrl;
    private String artifactUrl;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class ErrorDetails {
    private String message;
    private String stack;
}
