package com.group10.gamestoryforge.controller;

import com.group10.gamestoryforge.model.Game;
import com.group10.gamestoryforge.model.UserData;
import com.group10.gamestoryforge.service.GameService;
import com.group10.gamestoryforge.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/dashboard/games")
public class GameController {

    @Autowired
    private GameService gameService;

    @Autowired
    private UserService userService;

    // 创建新游戏
    @PostMapping
    public ResponseEntity<Game> createGame(
            @RequestParam String username,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam(value = "image", required = false) MultipartFile image) {

        String imagePath = null;
        if (image != null && !image.isEmpty()) {
            imagePath = gameService.saveGameImage(image); // 保存图片并返回路径
        }

        // 创建并保存游戏
        Game game = new Game();
        game.setName(name);
        game.setDescription(description);
        game.setImage(imagePath); // 将图片路径保存到游戏中

        Game createdGame = gameService.createGame(username, game);
        if (createdGame != null) {
            return ResponseEntity.ok(createdGame);
        } else {
            return ResponseEntity.badRequest().build(); // 如果用户不存在或创建失败
        }
    }

    // 获取某个用户的游戏列表
    @GetMapping
    public ResponseEntity<List<Game>> getGamesByUsername(@RequestParam String username) {
        List<Game> games = gameService.getGamesByUsername(username);
        return ResponseEntity.ok(games);

    }

    @GetMapping("/{gameId}")
    public ResponseEntity<Game> getGameById(@PathVariable Long gameId) {
        Game game = gameService.getGameById(gameId);
        if (game != null) {

            return ResponseEntity.ok(game);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 更新游戏
    @PutMapping("/{gameId}")
    public ResponseEntity<Game> updateGame(
            @PathVariable Long gameId,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestParam String username) {

        Game updatedGame = new Game();
        updatedGame.setName(name);
        updatedGame.setDescription(description);

        Game game = gameService.updateGame(gameId, updatedGame, image);
        if (game != null) {
            return ResponseEntity.ok(game);
        } else {
            return ResponseEntity.notFound().build(); // 如果游戏不存在
        }
    }

    // 删除游戏
    @DeleteMapping("/{gameId}")
    public ResponseEntity<Void> deleteGame(@PathVariable Long gameId) {
        gameService.deleteGame(gameId);
        return ResponseEntity.noContent().build(); // 成功删除返回 204 状态码
    }
}

