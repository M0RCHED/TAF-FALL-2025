package ca.etsmtl.taf.service;

import ca.etsmtl.taf.entity.TestCase;
import ca.etsmtl.taf.repository.TestCaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TestCaseService {

    @Autowired
    private TestCaseRepository testCaseRepository;

    public List<TestCase> getCasesForRun(String runId, String status) {
        if (status != null && !status.isEmpty()) {
            return testCaseRepository.findByRunIdAndStatus(runId, status);
        } else {
            return testCaseRepository.findByRunId(runId);
        }
    }

    public List<TestCase> saveAllCases(List<TestCase> testCases) {
        return testCaseRepository.saveAll(testCases);
    }
}