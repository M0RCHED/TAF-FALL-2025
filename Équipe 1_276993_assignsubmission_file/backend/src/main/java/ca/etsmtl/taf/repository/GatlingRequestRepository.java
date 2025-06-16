package ca.etsmtl.taf.repository;

import ca.etsmtl.taf.entity.GatlingRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GatlingRequestRepository extends MongoRepository<GatlingRequest, String> {
}
