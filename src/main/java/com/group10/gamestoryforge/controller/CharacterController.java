package com.group10.gamestoryforge.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group10.gamestoryforge.model.Character;
import com.group10.gamestoryforge.service.CharacterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/characters")
public class CharacterController {

    @Autowired
    private CharacterService characterService;

    @GetMapping
    public ResponseEntity<List<Character>> getCharactersByGameId(@RequestParam Integer gameId) {
        List<Character> characters = characterService.getCharactersByGameId(gameId);
        return ResponseEntity.ok(characters);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Character> addCharacter(
            @RequestPart("character") String characterJson,   // 接收 JSON 字符串
            @RequestPart(value = "image", required = false) MultipartFile image) throws Exception {

        // 将字符串形式的 JSON 转换为 Character 对象
        ObjectMapper objectMapper = new ObjectMapper();
        Character character = objectMapper.readValue(characterJson, Character.class);

        // 处理图片文件上传逻辑（如果需要）
//        if (image != null && !image.isEmpty()) {
//            String imagePath = characterService.saveCharacterImage(image);
//            character.setImage(imagePath);
//        }

        // 处理角色的保存逻辑
        Character newCharacter = characterService.addCharacter(character);

        return ResponseEntity.status(HttpStatus.CREATED).body(newCharacter);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Character> updateCharacter(@PathVariable Integer id, @RequestBody Character characterDetails) {
        Character updatedCharacter = characterService.updateCharacter(id, characterDetails);
        return ResponseEntity.ok(updatedCharacter);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCharacter(@PathVariable Integer id) {
        characterService.deleteCharacter(id);
        return ResponseEntity.noContent().build();
    }
}
