package ca.etsmtl.taf.controller;

import ca.etsmtl.taf.dto.*;
import ca.etsmtl.taf.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dashboard")
@CrossOrigin(origins = { "http://localhost:4200", "http://localhost:5173" })
public class DashboardController {

    private final DashboardService service;

    public DashboardController(DashboardService service) {
        this.service = service;
    }

    @GetMapping("/report")
    public List<RunCardDto> latestRuns(
            @RequestParam String project,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "5") int limit) {
        return service.latestRuns(project, status, limit);
    }

    @GetMapping("/report/run/{runId}")
    public ResponseEntity<RunDetailDto> runById(@PathVariable String runId) {
        RunDetailDto d = service.runById(runId);
        return d == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(d);
    }

    @GetMapping("/cases")
    public Map<String,Object> searchCases(
            @RequestParam String project,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String tool,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return service.searchCases(project, type, tool, status, from, to, page, size);
    }

    @GetMapping("/summary/passrate")
    public List<PassratePointDto> passrate(
            @RequestParam String project,
            @RequestParam(defaultValue = "14") int days) {
        return service.passrate(project, days);
    }
}
