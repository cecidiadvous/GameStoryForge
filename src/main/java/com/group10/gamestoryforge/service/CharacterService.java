package com.group10.gamestoryforge.service;

import com.group10.gamestoryforge.model.Character;
import com.group10.gamestoryforge.dao.CharacterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CharacterService {

    @Autowired
    private CharacterRepository characterRepository;

    public List<Character> getCharactersByGameId(Integer gameId) {
        return characterRepository.findByGameId(gameId);
    }
    public List<Character> getAllCharacters() {
        return characterRepository.findAll();
    }

    public Optional<Character> getCharacterById(Integer id) {
        return characterRepository.findById(id);
    }

    public Character createCharacter(Character character) {
        return characterRepository.save(character);
    }

    public Character updateCharacter(Integer id, Character characterDetails) {
        Character character = characterRepository.findById(id).orElseThrow();
        character.setName(characterDetails.getName());
        character.setRole(characterDetails.getRole());
        character.setBackground(characterDetails.getBackground());
        character.setAbility(characterDetails.getAbility());
        character.setGameId(characterDetails.getGameId());
        return characterRepository.save(character);
    }

    public void deleteCharacter(Integer id) {
        characterRepository.deleteById(id);
    }
}

