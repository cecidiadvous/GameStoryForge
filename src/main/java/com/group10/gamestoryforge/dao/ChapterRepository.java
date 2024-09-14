package com.group10.gamestoryforge.dao;

import com.group10.gamestoryforge.model.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, Integer> {
    List<Chapter> findByGameId(Integer gameId);
}

