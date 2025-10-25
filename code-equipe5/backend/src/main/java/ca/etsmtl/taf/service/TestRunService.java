package ca.etsmtl.taf.service;

import ca.etsmtl.taf.entity.TestRun;
import ca.etsmtl.taf.repository.TestRunRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TestRunService {

    @Autowired
    private TestRunRepository testRunRepository;

    public List<TestRun> getRunsForProject(String projectKey) {
        return testRunRepository.findAll(); // Simple pour l'instant
    }

    public TestRun getRunDetails(String runId) {
        return testRunRepository.findByRunId(runId)
                .orElseThrow(() -> new RuntimeException("Run not found: " + runId));
    }
}
