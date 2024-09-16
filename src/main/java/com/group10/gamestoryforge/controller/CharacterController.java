package com.group10.gamestoryforge.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group10.gamestoryforge.model.Character;
import com.group10.gamestoryforge.service.CharacterService;
import com.group10.gamestoryforge.service.GameService;
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
            @RequestPart("character") String characterJson,
            @RequestPart(value = "image", required = false) MultipartFile image) throws Exception {

        // 调用 service 处理业务逻辑
        Character newCharacter = characterService.addCharacter(characterJson, image);
        return ResponseEntity.status(HttpStatus.CREATED).body(newCharacter);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Character> updateCharacter(
            @PathVariable Integer id,
            @RequestPart("character") String characterJson,
            @RequestPart(value = "image", required = false) MultipartFile image) throws Exception {

        // 调用 service 处理业务逻辑
        Character updatedCharacter = characterService.updateCharacter(id, characterJson, image);
        return ResponseEntity.ok(updatedCharacter);
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCharacter(@PathVariable Integer id) {
        characterService.deleteCharacter(id);
        return ResponseEntity.noContent().build();
    }
}
