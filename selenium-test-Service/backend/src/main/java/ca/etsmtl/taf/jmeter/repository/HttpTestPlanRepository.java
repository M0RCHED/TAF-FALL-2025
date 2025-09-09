package ca.etsmtl.taf.jmeter.repository;

import ca.etsmtl.taf.jmeter.model.HttpTestPlan;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HttpTestPlanRepository extends MongoRepository<HttpTestPlan, String> {
}
