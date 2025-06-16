package ca.etsmtl.taf.jmeter.repository;

import ca.etsmtl.taf.jmeter.model.HttpTestResult;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HttpTestResultRepository extends MongoRepository<HttpTestResult, String> {
    List<HttpTestResult> findAllByTestPlanId(String testPlanId);
}
