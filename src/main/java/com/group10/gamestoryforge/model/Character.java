package com.group10.gamestoryforge.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "characters")
public class Character {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "character_id")
    private Long characterId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String role;

    @Column(nullable = true)
    private String background;

    @Column(nullable = false)
    private String ability;

    @Column(name = "game_id", nullable = false)
    private Long gameId;

    @Column(name = "image")
    private String image;

    @ManyToMany(mappedBy = "characters")
    @JsonIgnore  // 防止递归序列化
    private Set<Chapter> chapters = new HashSet<>();

    public Character() {
    }

    // Getters and Setters
    public Long getCharacterId() {
        return characterId;
    }

    public void setCharacterId(Long characterId) {
        this.characterId = characterId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getAbility() {
        return ability;
    }

    public void setAbility(String ability) {
        this.ability = ability;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Set<Chapter> getChapters() {
        return chapters;
    }

    public void setChapters(Set<Chapter> chapters) {
        this.chapters = chapters;
    }

    // 写出 toString 方法
    @Override
    public String toString() {
        return "Character{" +
                ", name='" + name + '\'' +
                ", role='" + role + '\'' +
                ", background='" + background + '\'' +
                ", ability='" + ability + '\'' +
                '}';
    }

    @PreRemove
    public void deleteImage() {
        if (image != null) {

            Path pathToDelete = Paths.get(System.getProperty("user.dir") + image);
            System.out.println("Deleting file at: " + pathToDelete.toString());
            try {
                Files.deleteIfExists(pathToDelete);
            } catch (IOException e) {
                throw new RuntimeException("Failed to delete image: " + image, e);
            }
        }
    }
}
