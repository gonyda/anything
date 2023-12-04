package com.bbsk.anything.test.repository;

import com.bbsk.anything.test.entity.Tests;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestRepository extends JpaRepository<Tests, Long> {
}
