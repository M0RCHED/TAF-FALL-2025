package ca.etsmtl.taf.jmeter.service;

import ca.etsmtl.taf.jmeter.model.HttpTestResult;
import ca.etsmtl.taf.jmeter.repository.HttpTestResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HttpTestResultService {

    @Autowired
    private HttpTestResultRepository repository;

    public List<HttpTestResult> saveTestResults(List<HttpTestResult> results) {
        return repository.saveAll(results);
    }

    public List<HttpTestResult> getResultsByTestPlanId(String testPlanId) {
        return repository.findAllByTestPlanId(testPlanId);
    }

}
