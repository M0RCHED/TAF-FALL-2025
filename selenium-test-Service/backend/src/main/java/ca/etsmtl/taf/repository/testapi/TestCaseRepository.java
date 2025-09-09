package ca.etsmtl.taf.repository.testapi;

import ca.etsmtl.taf.entity.TestCase;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestCaseRepository extends MongoRepository<TestCase, String> {
}
