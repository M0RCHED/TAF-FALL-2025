package ca.etsmtl.taf.repository.testapi;

import ca.etsmtl.taf.entity.TestSuite;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestSuiteRepository extends MongoRepository<TestSuite, String> {
}
