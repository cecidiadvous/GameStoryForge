package com.group10.gamestoryforge.service;

import com.group10.gamestoryforge.dao.GameRepository;
import com.group10.gamestoryforge.dao.UserDataRepository;
import com.group10.gamestoryforge.model.Game;
import com.group10.gamestoryforge.model.UserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class GameService {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private UserDataRepository userDataRepository;

    // 通过配置文件定义上传路径，增加灵活性
    private final String uploadDir = "uploads/images/";

    // 创建新游戏
    @Transactional
    public Game createGame(String username, Game game) {
        UserData user = userDataRepository.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found: " + username); // 更明确的异常处理
        }
        game.setUser(user); // 将游戏与用户绑定
        return gameRepository.save(game);
    }

    // 保存游戏图片
    public String saveGameImage(MultipartFile image) {
        String fileName = UUID.randomUUID().toString() + "_" + image.getOriginalFilename();
        Path filePath = Paths.get(uploadDir, fileName);
        try {
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, image.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Failed to save image", e); // 使用异常替代返回 null
        }
        return "/uploads/images/" + fileName; // 返回文件的相对路径
    }

    // 删除旧图片
    private void deleteGameImage(String imagePath) {
        if (imagePath != null) {
            Path pathToDelete = Paths.get(System.getProperty("user.dir") + imagePath);
            try {
                Files.deleteIfExists(pathToDelete);
            } catch (IOException e) {
                throw new RuntimeException("Failed to delete image: " + imagePath, e);
            }
        }
    }

    // 获取用户的游戏列表
    public List<Game> getGamesByUsername(String username) {
        UserData user = userDataRepository.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found: " + username); // 更明确的异常处理
        }
        return gameRepository.findByUser(user);
    }

    // 更新游戏，并可能更新图片
    @Transactional
    public Game updateGame(Long gameId, Game updatedGame, MultipartFile image) {
        return gameRepository.findById(gameId).map(game -> {
            // 更新名称和描述
            game.setName(updatedGame.getName());
            game.setDescription(updatedGame.getDescription());

            // 如果有新的图片，更新图片并删除旧图片
            if (image != null && !image.isEmpty()) {
                // 删除旧图片
                deleteGameImage(game.getImage());
                // 保存新图片
                String imagePath = saveGameImage(image);
                game.setImage(imagePath);
            }

            return gameRepository.save(game);
        }).orElseThrow(() -> new IllegalArgumentException("Game not found: " + gameId));
    }

    // 删除游戏并删除相关图片
    @Transactional
    public void deleteGame(Long gameId) {
        gameRepository.findById(gameId).ifPresent(game -> {
            // 删除游戏关联的图片
            deleteGameImage(game.getImage());
            gameRepository.delete(game);
        });
    }
}

