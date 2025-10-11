package ca.etsmtl.taf.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ca.etsmtl.taf.entity.Test;
import ca.etsmtl.taf.repository.TestRepository;
import ca.etsmtl.taf.dto.ReportDTO;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/test") // préfixe clair
public class TestController {

    private final TestRepository testRepository;

    public TestController(TestRepository testRepository) {
        this.testRepository = testRepository;
    }

    @GetMapping("/tests")
    public Iterable<Test> findAllTests() {
        return testRepository.findAll();
    }

    // ping simple pour valider le mapping
    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("OK");
    }

    @PostMapping("/tests")
    public Test create(@RequestBody Test payload) {
        return testRepository.save(payload);
    }


    @GetMapping("/report")
    public ReportDTO getReport() {
        var items = new ArrayList<ReportDTO.TestItem>();
        testRepository.findAll().forEach(t -> {
            var ti = new ReportDTO.TestItem();
            ti.id = t.getId();
            ti.tool = "jmeter";              // provisoire: valeur par défaut
            ti.feature = "General";          // provisoire
            ti.scenario = t.getName();       // mappe name -> scenario
            ti.status = normalizeStatus(t.getStatus());
            ti.durationMs = 0;               // si pas de durée en DB
            items.add(ti);
        });

        var r = new ReportDTO();
        r.run = new ReportDTO.Run();
        r.run.runId = UUID.randomUUID().toString();
        r.run.generatedAt = Instant.now().toString();

        r.stats = new ReportDTO.Stats();
        r.stats.total = items.size();
        r.stats.passed = (int) items.stream().filter(i -> "passed".equals(i.status)).count();
        r.stats.failed = (int) items.stream().filter(i -> "failed".equals(i.status)).count();
        r.stats.skipped = (int) items.stream().filter(i -> "skipped".equals(i.status)).count();
        r.stats.durationMs = 0;

        r.tests = items;
        return r;
    }

    private String normalizeStatus(String s) {
        if (s == null) return "skipped";
        s = s.trim().toLowerCase();
        if (s.startsWith("pass") || s.equals("ok") || s.equals("succes") || s.equals("success")) return "passed";
        if (s.startsWith("fail") || s.equals("ko")) return "failed";
        if (s.startsWith("skip")) return "skipped";
        return "failed"; // par défaut si inconnu
    }


}
