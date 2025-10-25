package ca.etsmtl.taf.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Map;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "test_runs")
public class TestRun {

    @Id
    private String id; // ID MongoDB
    private String runId; // Clé unique de l'exécution

    private Project project;
    private PipelineInfo pipeline;
    private SuiteInfo suite;

    @JsonProperty("environment") // "environment" mot-clé
    private EnvironmentInfo envDetails;

    @JsonProperty("run") // "run" mot-clé
    private RunDetails runDetails;

    private List<TestCaseSummary> cases; // La liste des cas résumés
    private Integrations integrations;
    private Metrics metrics;
    private Instant createdAt;
    private List<String> tags;
}

// --- Classes imbriquées (POJOs) pour TestRun ---

@Data
@NoArgsConstructor
@AllArgsConstructor
class PipelineInfo {
    private String runId;
    private String workflow;
    private GitInfo git;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class GitInfo {
    private String branch;
    private String commit;
    private String author;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class SuiteInfo {
    private String name;
    private String type;
    private String version;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class EnvironmentInfo {
    private String name;
    private String os;
    private String browser;
    private String baseUrl;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class RunDetails {
    private String status;
    private Instant startedAt;
    private Instant endedAt;
    private RunStats stats;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class RunStats {
    private int total;
    private int passed;
    private int failed;
    private int skipped;
    private long durationMs;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class TestCaseSummary { // Résumé d'un cas de test (dans la liste de TestRun)
    private String id;
    private String name;
    private String type;
    private String tool;
    private String status;
    private long durationMs;
    private List<String> tags;
    private Map<String, Object> details; // Pour les objets dynamiques comme "selenium"
    private List<Artifact> artifacts;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class Artifact {
    private String type;
    private String path;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class Integrations {
    private JiraIntegration jira;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class JiraIntegration {
    private String projectKey;
    private List<String> issueKeys;
    private DefectPolicy defectPolicy;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class DefectPolicy {
    private String onFailed;
    private List<String> labels;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class Metrics {
    private PerformanceMetrics performance;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class PerformanceMetrics { // Cette classe peut être réutilisée
    private int vus;
    private int throughputRps;
    private int avgLatencyMs;
    private int p95;
    private double errorRatePct;
    private String tool;
}