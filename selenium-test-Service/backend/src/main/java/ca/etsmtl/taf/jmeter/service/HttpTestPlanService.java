package ca.etsmtl.taf.jmeter.service;

import ca.etsmtl.taf.entity.GatlingRequest;
import ca.etsmtl.taf.jmeter.model.HttpTestPlan;
import ca.etsmtl.taf.jmeter.repository.HttpTestPlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HttpTestPlanService {

    @Autowired
    private HttpTestPlanRepository repository;

    public HttpTestPlan saveTestPlan(HttpTestPlan testPlan) {
        return repository.save(testPlan);
    }

    public List<HttpTestPlan> getAllRequests() {
        return repository.findAll();
    }

}
