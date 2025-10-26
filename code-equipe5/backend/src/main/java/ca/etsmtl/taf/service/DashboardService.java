package ca.etsmtl.taf.service;

import ca.etsmtl.taf.dto.*;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;
import java.util.*;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Service
public class DashboardService {

    private final MongoTemplate mongo;

    public DashboardService(MongoTemplate mongo) {
        this.mongo = mongo;
    }

    // ---- helpers ----
    private static Instant parseDayStart(String d) {
        if (d == null || d.isBlank()) return null;
        try { return Instant.parse(d + "T00:00:00Z"); } catch (DateTimeParseException e) { return null; }
    }
    private static Instant parseDayEnd(String d) {
        if (d == null || d.isBlank()) return null;
        try { return Instant.parse(d + "T23:59:59Z"); } catch (DateTimeParseException e) { return null; }
    }
    /** Convertit Date/Instant/String(ISO-8601) en Instant, sinon null. */
    private static Instant toInstant(Object v) {
        if (v == null) return null;
        if (v instanceof Instant) return (Instant) v;
        if (v instanceof Date) return ((Date) v).toInstant();
        if (v instanceof CharSequence) {
            try { return Instant.parse(v.toString()); } catch (Exception ignore) { return null; }
        }
        return null;
    }

    // ---- A) derniers runs (cartes) ----
    public List<RunCardDto> latestRuns(String project, String status, int limit) {
        limit = Math.max(1, Math.min(limit, 50));

        // 1) tenter via test_runs
        Query q = new Query();
        q.addCriteria(Criteria.where("project.key").is(project));
        if (status != null && !status.isBlank()) {
            q.addCriteria(Criteria.where("run.status").is(status));
        }
        q.with(Sort.by(Sort.Direction.DESC, "createdAt"));
        q.limit(limit);
        q.fields().include("project.key")
                .include("pipeline.runId")
                .include("run.status")
                .include("run.stats")
                .include("createdAt");

        List<Map> rows = mongo.find(q, Map.class, "test_runs");

        // 2) fallback depuis test_cases si rien trouvé
        if (rows == null || rows.isEmpty()) {
            Aggregation agg = newAggregation(
                    match(Criteria.where("project").is(project)),
                    sort(Sort.by(Sort.Direction.DESC, "executedAt")),
                    group("runId")
                            .sum(ConditionalOperators.when(Criteria.where("status").is("failed")).then(1).otherwise(0)).as("failed")
                            .first("executedAt").as("anyTime"),
                    project()
                            .and("_id").as("runId")
                            .and("anyTime").as("anyTime")
                            .and(ConditionalOperators.when(
                                            ComparisonOperators.Gt.valueOf("$failed").greaterThanValue(0))
                                    .then("failed").otherwise("passed")).as("status"),
                    sort(Sort.by(Sort.Direction.DESC, "anyTime")),
                    limit(limit)
            );
            List<Map> rs = mongo.aggregate(agg, "test_cases", Map.class).getMappedResults();
            List<RunCardDto> out = new ArrayList<>();
            for (Map r : rs) {
                RunCardDto d = new RunCardDto();
                d.projectKey = project;
                d.runId = String.valueOf(r.get("runId"));
                d.status = String.valueOf(r.get("status"));
                d.createdAt = toInstant(r.get("anyTime")); // approx via 1er executedAt
                out.add(d);
            }
            return out;
        }

        // mapping test_runs -> DTO
        List<RunCardDto> out = new ArrayList<>();
        for (Map r : rows) {
            RunCardDto d = new RunCardDto();
            Map proj = (Map) r.get("project");
            Map run  = (Map) r.get("run");
            Map stats = run == null ? null : (Map) run.get("stats");
            Map pipe = (Map) r.get("pipeline");

            d.projectKey = proj == null ? null : String.valueOf(proj.get("key"));
            d.runId      = pipe == null ? null : String.valueOf(pipe.get("runId"));
            d.status     = run == null ? null : String.valueOf(run.get("status"));
            d.createdAt  = toInstant(r.get("createdAt"));
            if (stats != null) {
                d.total  = ((Number) stats.getOrDefault("total", 0)).longValue();
                d.passed = ((Number) stats.getOrDefault("passed", 0)).longValue();
                d.failed = ((Number) stats.getOrDefault("failed", 0)).longValue();
            }
            out.add(d);
        }
        return out;
    }

    // ---- B) détail run ----
    public RunDetailDto runById(String runId) {
        Map<?,?> run = mongo.findOne(Query.query(Criteria.where("pipeline.runId").is(runId)), Map.class, "test_runs");
        if (run != null) {
            RunDetailDto d = new RunDetailDto();
            Map proj = (Map) run.get("project");
            Map r    = (Map) run.get("run");
            Map stats = r == null ? null : (Map) r.get("stats");
            Map pipe  = (Map) run.get("pipeline");

            d.projectKey = proj == null ? null : String.valueOf(proj.get("key"));
            d.runId      = pipe == null ? null : String.valueOf(pipe.get("runId"));
            d.status     = r == null ? null : String.valueOf(r.get("status"));
            d.createdAt  = toInstant(run.get("createdAt"));
            d.stats      = new RunDetailDto.Stats();
            if (stats != null) {
                d.stats.total      = ((Number) stats.getOrDefault("total", 0)).longValue();
                d.stats.passed     = ((Number) stats.getOrDefault("passed", 0)).longValue();
                d.stats.failed     = ((Number) stats.getOrDefault("failed", 0)).longValue();
                d.stats.durationMs = ((Number) stats.getOrDefault("durationMs", 0)).longValue();
            }
            // <-- retourner aussi les cases si stockées dans test_runs
            Object casesObj = run.get("cases");
            if (casesObj instanceof List) {
                d.cases = (List) casesObj; // DTO accepte List brute dans ton implémentation
            }
            return d;
        }

        // fallback : reconstruire depuis test_cases
        List<Map> cases = mongo.find(
                Query.query(Criteria.where("runId").is(runId))
                        .with(Sort.by(Sort.Direction.ASC, "executedAt")),
                Map.class, "test_cases");

        if (cases == null || cases.isEmpty()) return null;

        long total = cases.size();
        long failed = cases.stream().filter(c -> "failed".equals(String.valueOf(c.get("status")))).count();

        RunDetailDto d = new RunDetailDto();
        d.runId = runId;
        d.projectKey = String.valueOf(cases.get(0).get("project"));
        d.status = failed > 0 ? "failed" : "passed";
        d.stats = new RunDetailDto.Stats();
        d.stats.total  = total;
        d.stats.passed = total - failed;
        d.stats.failed = failed;
        d.cases = (List) cases;
        // optionnel : createdAt approximé par le max(executedAt)
        Object lastExec = cases.get(cases.size()-1).get("executedAt");
        d.createdAt = toInstant(lastExec);
        return d;
    }

    // ---- C) recherche/pagination des tests ----
    public Map<String,Object> searchCases(
            String project, String type, String tool, String status,
            String from, String to, int page, int size) {

        page = Math.max(page, 0);
        size = Math.min(Math.max(size, 1), 100);

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
        int skipN = page * size;

        Aggregation agg = newAggregation(
                match(matchCrit),
                sort(Sort.by(Sort.Direction.DESC, "executedAt")),
                skip(skipN),
                limit(size)
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

    // ---- D) passrate (par jour) ----
    public List<PassratePointDto> passrate(String project, int days) {
        int win = (days <= 0 || days > 365) ? 14 : days;
        Instant from = LocalDate.now(ZoneOffset.UTC)
                .minusDays(win - 1L)
                .atStartOfDay()
                .toInstant(ZoneOffset.UTC);

        // 1) si on a test_runs : simple et fiable
        try {
            Aggregation aggRuns = newAggregation(
                    match(Criteria.where("project.key").is(project)
                            .and("createdAt").gte(Date.from(from))),
                    project()
                            .and(DateOperators.DateToString
                                    .dateOf("createdAt")
                                    .toString("%Y-%m-%d")).as("day")
                            .and("$run.status").as("status"),
                    group("day")
                            .sum(ConditionalOperators.when(Criteria.where("status").is("passed"))
                                    .then(1).otherwise(0)).as("passed")
                            .count().as("total"),
                    sort(Sort.by("day").ascending())
            );
            List<Map> res = mongo.aggregate(aggRuns, "test_runs", Map.class).getMappedResults();
            if (!res.isEmpty()) return mapPassrate(res);
        } catch (Exception ignore) {}

        // 2) Fallback : reconstituer par run depuis test_cases (executedAt est String)
        Aggregation aggCases = newAggregation(
                match(Criteria.where("project").is(project)),
                addFields().addField("execDate")
                        .withValue(DateOperators.dateFromString("$executedAt"))
                        .build(),
                match(Criteria.where("execDate").gte(Date.from(from))),
                group("runId")
                        .sum(ConditionalOperators.when(Criteria.where("status").is("failed"))
                                .then(1).otherwise(0)).as("failed")
                        .first("execDate").as("anyTime"),
                project()
                        .and(DateOperators.DateToString
                                .dateOf("anyTime")
                                .toString("%Y-%m-%d")).as("day")
                        .and(ConditionalOperators.when(
                                        ComparisonOperators.Gt.valueOf("$failed").greaterThanValue(0))
                                .then("failed").otherwise("passed"))
                        .as("status"),
                group("day")
                        .sum(ConditionalOperators.when(Criteria.where("status").is("passed"))
                                .then(1).otherwise(0)).as("passed")
                        .count().as("total"),
                sort(Sort.by("day").ascending())
        );

        List<Map> res2 = mongo.aggregate(aggCases, "test_cases", Map.class).getMappedResults();
        return mapPassrate(res2);
    }

    // --------------- helpers ---------------
    private List<PassratePointDto> mapPassrate(List<Map> rows) {
        List<PassratePointDto> out = new ArrayList<>();
        for (Map r : rows) {
            PassratePointDto p = new PassratePointDto();
            p.day = r.containsKey("_id") ? String.valueOf(r.get("_id")) : String.valueOf(r.get("day"));
            Object passed = r.get("passed");
            Object total  = r.get("total");
            p.passed = passed == null ? 0 : ((Number) passed).longValue();
            p.total  = total  == null ? 0 : ((Number) total ).longValue();
            out.add(p);
        }
        return out;
    }
}
