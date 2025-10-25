package ca.etsmtl.taf.controller;

import ca.etsmtl.taf.entity.TestRun;
import ca.etsmtl.taf.service.TestRunService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", maxAge = 3600)
public class TestRunController {

    @Autowired
    private TestRunService testRunService;

    /**
     * GET /api/projects/{projectKey}/runs
     * Récupère la liste de tous les TestRuns pour un projet donné.
     * C'est pour la page d'accueil du tableau de bord.
     */
    @GetMapping("/projects/{projectKey}/runs")
    public ResponseEntity<List<TestRun>> getProjectRuns(
            @PathVariable String projectKey) {
        List<TestRun> runs = testRunService.getRunsForProject(projectKey);
        return ResponseEntity.ok(runs);
    }

    /**
     * GET /api/runs/{runId}
     * Récupère les détails d'une seule exécution.
     */
    @GetMapping("/runs/{runId}")
    public ResponseEntity<TestRun> getRunDetails(@PathVariable String runId) {
        try {
            TestRun run = testRunService.getRunDetails(runId);
            return ResponseEntity.ok(run);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
