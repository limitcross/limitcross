package com.limitcross.facility.repository;

import com.limitcross.facility.domain.SavedAppCredential;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SavedAppCredentialRepository extends JpaRepository<SavedAppCredential, String> {
    Optional<SavedAppCredential> findOneByOwnerUidAndAppName(String ownerUid, String appName);
    void deleteByOwnerUidAndAppName(String ownerUid, String appName);
}