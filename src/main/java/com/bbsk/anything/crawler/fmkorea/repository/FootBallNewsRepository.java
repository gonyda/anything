package com.bbsk.anything.crawler.fmkorea.repository;

import com.bbsk.anything.crawler.fmkorea.entity.FootballNews;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FootBallNewsRepository extends JpaRepository<FootballNews, Long> {
    List<FootballNews> findTop20ByOrderByRegDtDesc();

}
