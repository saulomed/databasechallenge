package com.logmein.saulo.databaseChallenge.server.repository;

import com.logmein.saulo.databaseChallenge.server.entity.KeyValueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface KeyValueRepository extends JpaRepository<KeyValueEntity, String> {

    @Query(value = "DELETE FROM KeyValueEntity WHERE key = ?1")
    public void delete(String key);

}
