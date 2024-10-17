package com.group10.gamestoryforge.dao;
import com.group10.gamestoryforge.model.Character;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CharacterRepository extends JpaRepository<Character, Long> {
    List<Character> findByGameId(Long gameId);
    List<Character> findByChapters_ChapterId(Long chapterId);
}

