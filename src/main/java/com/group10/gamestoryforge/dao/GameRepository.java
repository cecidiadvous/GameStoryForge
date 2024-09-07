package com.group10.gamestoryforge.dao;

import com.group10.gamestoryforge.model.Game;
import com.group10.gamestoryforge.model.UserData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
    // 查找用户的游戏列表
    List<Game> findByUser(UserData user);
}

