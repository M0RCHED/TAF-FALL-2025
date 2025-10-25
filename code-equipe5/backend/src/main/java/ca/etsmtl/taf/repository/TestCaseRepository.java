package ca.etsmtl.taf.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import ca.etsmtl.taf.entity.TestCase;

@Repository
public interface TestCaseRepository extends MongoRepository<TestCase, String> {

    List<TestCase> findByRunId(String runId);

    List<TestCase> findByRunIdAndStatus(String runId, String status);
}
