package ca.etsmtl.taf.controller;

import ca.etsmtl.taf.entity.TestCase;
import ca.etsmtl.taf.service.TestCaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", maxAge = 3600)
public class TestCaseController {

    @Autowired
    private TestCaseService testCaseService;

    /**
     * GET /api/runs/{runId}/cases
     * Récupère tous les cas de test détaillés pour un runId spécifique.
     * * Supporte le filtrage par statut :
     * GET /api/runs/{runId}/cases?status=failed
     * GET /api/runs/{runId}/cases?status=passed
     */
    @GetMapping("/runs/{runId}/cases")
    public ResponseEntity<List<TestCase>> getCasesForRun(
            @PathVariable String runId,
            @RequestParam(required = false) String status) {

        List<TestCase> cases = testCaseService.getCasesForRun(runId, status);

        if (cases.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(cases);
    }
}