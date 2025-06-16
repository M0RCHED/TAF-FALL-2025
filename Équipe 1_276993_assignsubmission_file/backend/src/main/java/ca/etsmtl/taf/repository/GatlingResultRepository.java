package ca.etsmtl.taf.repository;

import ca.etsmtl.taf.entity.GatlingResult;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GatlingResultRepository extends MongoRepository<GatlingResult, String> {
    List<GatlingResult> findByTestRequestName(String testRequestName);
}
