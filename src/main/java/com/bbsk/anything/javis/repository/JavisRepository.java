package com.bbsk.anything.javis.repository;

import com.bbsk.anything.javis.entity.Javis;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JavisRepository extends JpaRepository<Javis, Long> {
    List<Javis> findAllByUserUserIdOrderByChatIdAsc(String userId);
}
