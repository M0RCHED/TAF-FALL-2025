package ca.etsmtl.taf.repository;

import ca.etsmtl.taf.entity.TestRun;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TestRunRepository extends MongoRepository<TestRun, String> {

    List<TestRun> findByProjectKey(String projectKey);

    Optional<TestRun> findByRunId(String runId);
}
