package com.limitcross.facility.repository;

import com.limitcross.facility.domain.SmtpMailConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SmtpMailConfigurationRepository extends JpaRepository<SmtpMailConfiguration, Long> {}