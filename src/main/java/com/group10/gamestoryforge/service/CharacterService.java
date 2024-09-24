package com.group10.gamestoryforge.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group10.gamestoryforge.model.Character;
import com.group10.gamestoryforge.dao.CharacterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class CharacterService {

    @Autowired
    private CharacterRepository characterRepository;

    @Autowired
    private GameService gameService;

    public List<Character> getCharactersByGameId(Integer gameId) {
        return characterRepository.findByGameId(gameId);
    }

    public List<Character> getAllCharacters() {
        return characterRepository.findAll();
    }

    public Optional<Character> getCharacterById(Integer id) {
        return characterRepository.findById(id);
    }

    // 添加角色逻辑
    public Character addCharacter(String characterJson, MultipartFile image) throws Exception {
        // 将 JSON 转换为 Character 对象
        ObjectMapper objectMapper = new ObjectMapper();
        Character character = objectMapper.readValue(characterJson, Character.class);

        // 处理图片文件上传逻辑
        if (image != null && !image.isEmpty()) {
            String imagePath = gameService.saveGameImage(image);  // 调用 GameService 来保存图片
            character.setImage(imagePath);  // 设置图片路径
        }

        // 保存角色
        return characterRepository.save(character);
    }

    // 更新角色逻辑
    public Character updateCharacter(Integer id, String characterJson, MultipartFile image) throws Exception {
        // 将 JSON 转换为 Character 对象

        ObjectMapper objectMapper = new ObjectMapper();
        Character characterDetails = objectMapper.readValue(characterJson, Character.class);

        // 查找现有的角色
        Character existingCharacter = characterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Character not found"));

        // 处理图片文件上传逻辑
        if (image != null && !image.isEmpty()) {
            gameService.deleteGameImage(existingCharacter.getImage());
            String imagePath = gameService.saveGameImage(image);  // 调用 GameService 来保存图片
            existingCharacter.setImage(imagePath);  // 更新图片路径
        }

        // 更新角色信息
        existingCharacter.setName(characterDetails.getName());
        existingCharacter.setRole(characterDetails.getRole());
        existingCharacter.setBackground(characterDetails.getBackground());
        existingCharacter.setAbility(characterDetails.getAbility());
        existingCharacter.setGameId(characterDetails.getGameId());

        return characterRepository.save(existingCharacter);  // 保存更新后的角色
    }

    public void deleteCharacter(Integer id) {
        characterRepository.findById(id).ifPresent(character -> {
            // 删除游戏关联的图片
            gameService.deleteGameImage(character.getImage());
            characterRepository.deleteById(id);
        });
    }

}


