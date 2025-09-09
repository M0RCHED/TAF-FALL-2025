package ca.etsmtl.taf.service;

import ca.etsmtl.taf.entity.GatlingResult;
import ca.etsmtl.taf.repository.GatlingResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GatlingResultService {

    @Autowired
    private GatlingResultRepository gatlingResultRepository;

    public void saveGatlingResult(GatlingResult result) {
        gatlingResultRepository.save(result); // ✅ Ensure this repository is for GatlingResult, NOT GatlingRequest
        System.out.println("✅ Gatling test result saved to MongoDB: " + result);
    }

    public List<GatlingResult> getResultsForTest(String requestName) {
        return gatlingResultRepository.findByTestRequestName(requestName);
    }

}
