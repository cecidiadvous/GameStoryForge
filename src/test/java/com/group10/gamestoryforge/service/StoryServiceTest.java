package com.group10.gamestoryforge.service;

import com.group10.gamestoryforge.dao.ChapterRepository;
import com.group10.gamestoryforge.dao.CharacterRepository;
import com.group10.gamestoryforge.model.Chapter;
import com.group10.gamestoryforge.model.Character;
import com.group10.gamestoryforge.model.ChatResponse;
import com.group10.gamestoryforge.model.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class StoryServiceTest {

    @Mock
    private CharacterRepository characterRepository;

    @Mock
    private ChatService chatService;

    @Mock
    private ChapterRepository chapterRepository;

    @InjectMocks
    private StoryService storyService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateStory() throws Exception {
        Long chapterId = 1L;
        String storyDescription = "A brief story description.";

        Chapter chapter = new Chapter();
        chapter.setChapterId(chapterId);
        chapter.setCreatedAt(LocalDateTime.now());
        chapter.setSystemText("Previous storyline");

        List<Chapter> chapters = new ArrayList<>();
        chapters.add(chapter);

        List<Character> characters = new ArrayList<>();
        Character character = new Character();
        character.setName("Hero");
        characters.add(character);

        ChatResponse chatResponse = new ChatResponse();
        ChatResponse.Choice choice = new ChatResponse.Choice();
        ChatResponse.Choice.Message message = new ChatResponse.Choice.Message();
        message.setContent("Generated storyline");
        choice.setMessage(message);
        chatResponse.setChoices(List.of(choice));

        when(chapterRepository.findById(chapterId)).thenReturn(Optional.of(chapter));
        when(chapterRepository.findByGame(any())).thenReturn(chapters);
        when(characterRepository.findByChapters_ChapterId(chapterId)).thenReturn(characters);
        when(chatService.getChatResponse(any())).thenReturn(CompletableFuture.completedFuture(chatResponse));

        CompletableFuture<String> result = storyService.createStory(chapterId, storyDescription);

        assertEquals("Generated storyline", result.get());
        verify(chapterRepository, times(1)).save(any(Chapter.class));
    }

    @Test
    public void testGetStoryByChapterId() throws Exception {
        Long chapterId = 1L;
        Chapter chapter = new Chapter();
        chapter.setChapterId(chapterId);
        chapter.setSystemText("Sample storyline");

        when(chapterRepository.findById(chapterId)).thenReturn(Optional.of(chapter));

        CompletableFuture<String> result = storyService.getStoryByChapterId(chapterId);

        assertEquals("Sample storyline", result.get());
    }

    @Test
    public void testGetChaptersCreatedBefore() {
        Long chapterId = 1L;
        Chapter currentChapter = new Chapter();
        currentChapter.setChapterId(chapterId);
        currentChapter.setCreatedAt(LocalDateTime.now());

        Chapter previousChapter = new Chapter();
        previousChapter.setChapterId(2L);
        previousChapter.setCreatedAt(LocalDateTime.now().minusDays(1));

        List<Chapter> allChapters = List.of(currentChapter, previousChapter);

        when(chapterRepository.findById(chapterId)).thenReturn(Optional.of(currentChapter));
        when(chapterRepository.findByGame(any())).thenReturn(allChapters);

        List<Chapter> result = storyService.getChaptersCreatedBefore(chapterId);

        assertEquals(1, result.size());
        assertEquals(previousChapter.getChapterId(), result.get(0).getChapterId());
    }
}
