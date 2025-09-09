package ca.etsmtl.taf.repository;

import ca.etsmtl.taf.entity.ApiBddJmeterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiBddJmeterRepository extends JpaRepository<ApiBddJmeterEntity, Long> {
}