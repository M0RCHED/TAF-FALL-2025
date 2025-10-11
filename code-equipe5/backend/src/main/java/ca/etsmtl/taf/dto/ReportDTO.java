package ca.etsmtl.taf.dto;

import java.util.List;

public class ReportDTO {
    public Run run;
    public Stats stats;
    public List<TestItem> tests;

    public static class Run { public String runId; public String generatedAt; }
    public static class Stats { public int total, passed, failed, skipped; public long durationMs; }
    public static class TestItem {
        public Integer id; public String tool; public String feature; public String scenario;
        public String status; public long durationMs;
    }
}
