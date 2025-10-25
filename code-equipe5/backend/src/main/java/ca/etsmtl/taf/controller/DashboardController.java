package ca.etsmtl.taf.controller;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.ZoneOffset;
import java.util.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

/**
 * Contrôleur Dashboard – lecture MongoDB seulement.
 * Collections attendues : test_runs (1 doc/run) et/ou test_cases (1 doc/test).
 *
 * Endpoints:
 *  - GET  /dashboard/report?project=TAF&status=&limit=5
 *  - GET  /dashboard/report/run/{runId}
 *  - GET  /dashboard/cases?project=TAF&type=&tool=&status=&from=2025-10-01&to=2025-10-19&page=0&size=20
 *  - GET  /dashboard/summary/passrate?project=TAF&days=14
 */
@RestController
@RequestMapping("/dashboard")
@CrossOrigin(origins = { "http://localhost:4200", "http://localhost:5173" }) // Front Angular/React en dev
public class DashboardController {

    private final MongoTemplate mongo;

    public DashboardController(MongoTemplate mongoTemplate) {
        this.mongo = mongoTemplate;
    }

    // ---------- A) Derniers runs (cards haut de page) ----------
    // ex: /dashboard/report?project=TAF&status=failed&limit=5
    @GetMapping("/report")
    public List<Map<String,Object>> latestRuns(
            @RequestParam String project,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "5") int limit) {

        Query q = new Query();
        q.addCriteria(Criteria.where("project.key").is(project));
        if (status != null && !status.isBlank()) {
            q.addCriteria(Criteria.where("run.status").is(status));
        }
        q.with(Sort.by(Sort.Direction.DESC, "createdAt"));
        q.limit(Math.max(1, Math.min(limit, 50)));
        // projection légère -> idéal pour “cards”
        q.fields().include("project.key").include("pipeline.runId")
                .include("run.status").include("run.stats").include("createdAt");

//        return mongo.find(q, Map.class, "test_runs");
        return cast(mongo.find(q, Map.class, "test_runs"));
    }

    // ---------- B) Détail d’un run ----------
    // ex: /dashboard/report/run/gha-2025-10-19-0012
    @GetMapping("/report/run/{runId}")
    public ResponseEntity<?> runById(@PathVariable String runId) {
        Map<?,?> run = mongo.findOne(Query.query(Criteria.where("pipeline.runId").is(runId)), Map.class, "test_runs");
        if (run != null) return ResponseEntity.ok(run);

        // Fallback: recompose à partir des test_cases si test_runs absent
        List<Map> cases = mongo.find(
                Query.query(Criteria.where("runId").is(runId))
                        .with(Sort.by(Sort.Direction.ASC, "executedAt")),
                Map.class, "test_cases");

        if (cases == null || cases.isEmpty()) return ResponseEntity.notFound().build();

        long total = cases.size();
        long failed = cases.stream().filter(c -> "failed".equals(String.valueOf(c.get("status")))).count();
        long passed = total - failed;

        Map<String,Object> composed = new LinkedHashMap<>();
        composed.put("pipeline", Map.of("runId", runId));
        composed.put("run", Map.of(
                "status", failed > 0 ? "failed" : "passed",
                "stats", Map.of("total", total, "passed", passed, "failed", failed)
        ));
        composed.put("cases", cases);
        return ResponseEntity.ok(composed);
    }

    // ---------- C) Table des tests (pagination + filtres) ----------
    // ex: /dashboard/cases?project=TAF&type=ui&tool=selenium&status=failed&from=2025-10-01&to=2025-10-19&page=0&size=20
    @GetMapping("/cases")
    public Map<String,Object> searchCases(
            @RequestParam String project,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String tool,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String from,   // YYYY-MM-DD
            @RequestParam(required = false) String to,     // YYYY-MM-DD
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        List<Criteria> cs = new ArrayList<>();
        cs.add(Criteria.where("project").is(project));
        if (type   != null && !type.isBlank())   cs.add(Criteria.where("type").is(type));
        if (tool   != null && !tool.isBlank())   cs.add(Criteria.where("tool").is(tool));
        if (status != null && !status.isBlank()) cs.add(Criteria.where("status").is(status));
        Instant f = parseDayStart(from);
        Instant t = parseDayEnd(to);
        if (f != null || t != null) {
            Criteria c = Criteria.where("executedAt");
            if (f != null) c = c.gte(f);
            if (t != null) c = c.lte(t);
            cs.add(c);
        }

        Criteria matchCrit = new Criteria().andOperator(cs.toArray(new Criteria[0]));
        int skipN = Math.max(0, page) * Math.max(1, size);

        Aggregation agg = newAggregation(
                match(matchCrit),
                sort(Sort.by(Sort.Direction.DESC, "executedAt")),
                skip(skipN),
                limit(Math.min(size, 100))
        );

        List<Map> items = mongo.aggregate(agg, "test_cases", Map.class).getMappedResults();
        long total = mongo.count(Query.query(matchCrit), "test_cases");

        Map<String,Object> out = new LinkedHashMap<>();
        out.put("page", page);
        out.put("size", size);
        out.put("total", total);
        out.put("items", items);
        return out;
    }

    // ---------- D) Passrate (graph ligne) ----------
    // ex: /dashboard/summary/passrate?project=TAF&days=14
    @GetMapping("/summary/passrate")
    public List<Map<String,Object>> passrate(
            @RequestParam String project,
            @RequestParam(defaultValue = "14") int days) {

        Instant from = LocalDate.now(ZoneOffset.UTC).minusDays(days).atStartOfDay().toInstant(ZoneOffset.UTC);

        // 1) préférer test_runs si existant
        try {
            Aggregation agg = newAggregation(
                    match(Criteria.where("project.key").is(project).and("createdAt").gte(from)),
                    project("status")
                            .andExpression("dateToString('%Y-%m-%d', $createdAt)").as("day")
                            .and("$run.status").as("status"),
                    group("day")
                            .sum(ConditionalOperators.when(Criteria.where("status").is("passed")).then(1).otherwise(0)).as("passed")
                            .count().as("total"),
                    sort(Sort.by("day").ascending())
            );
            List<Map> res = mongo.aggregate(agg, "test_runs", Map.class).getMappedResults();
            if (!res.isEmpty()) return cast(res);
        } catch (Exception ignore) {}

        // 2) fallback : regrouper les test_cases par run puis par jour (passed/failed)
        Aggregation agg2 = newAggregation(
                match(Criteria.where("project").is(project).and("executedAt").gte(from)),
                group("runId")
                        .sum(ConditionalOperators.when(Criteria.where("status").is("failed")).then(1).otherwise(0)).as("failed")
                        .first("executedAt").as("anyTime"),
                project("day", "status")
                        .andExpression("dateToString('%Y-%m-%d', $anyTime)").as("day")
                        .and(ConditionalOperators.when(
                                        ComparisonOperators.Gt.valueOf("$failed").greaterThanValue(0))
                                .then("failed").otherwise("passed")).as("status"),
                group("day")
                        .sum(ConditionalOperators.when(Criteria.where("status").is("passed")).then(1).otherwise(0)).as("passed")
                        .count().as("total"),
                sort(Sort.by("day").ascending())
        );

        List<Map> res2 = mongo.aggregate(agg2, "test_cases", Map.class).getMappedResults();
        return cast(res2);
    }

    // ---------- helpers ----------
    private static Instant parseDayStart(String d) {
        if (d == null || d.isBlank()) return null;
        try { return Instant.parse(d + "T00:00:00Z"); } catch (DateTimeParseException e) { return null; }
    }
    private static Instant parseDayEnd(String d) {
        if (d == null || d.isBlank()) return null;
        try { return Instant.parse(d + "T23:59:59Z"); } catch (DateTimeParseException e) { return null; }
    }
    @SuppressWarnings("unchecked")
    private static List<Map<String,Object>> cast(List<Map> in){ return (List<Map<String,Object>>)(List<?>)in; }
}
