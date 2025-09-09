package ca.etsmtl.taf.repository.testapi;

import ca.etsmtl.taf.entity.TestPlan;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestPlanRepository extends MongoRepository<TestPlan, String> {
}
