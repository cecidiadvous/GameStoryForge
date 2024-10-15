<template>
  <div class="workshop-container">
    <!-- Top Navigation Bar -->
    <nav class="top-nav">

      <h2>
        <span class="gamestory">GameStory</span>
        <span class="forge">Forge</span>
      </h2>

      <div class="nav-right">
        <ul class="nav-list">
          <li>
            <button @click="handleDownload">Download</button>
          </li>
          <li>
            <router-link to="/home">Home</router-link>
          </li>
          <li>
            <router-link to="/dashboard">Dashboard</router-link>
          </li>
          <li>
            <button @click="logout">Log Out</button>
          </li>
        </ul>
      </div>
    </nav>

    <aside class="sidebar">
      <div class="input-button-wrapper">
        <div class="input-button-container">
          <input v-model="newChapterName" placeholder="Add chapter name" @keyup.enter="addChapter"/>
          <img src="@/assets/chapter_add.svg" @click="addChapter" class="add-button"/>
        </div>
      </div>
      <div class="chapter-list-container">
        <h3>{{ projectTitle }}</h3>
        <h3>Chapter List:</h3>
        <ul>
          <li
              v-for="chapter in chapters"
              :key="chapter.chapterId"
              :class="{ selected: chapter.chapterId === selectedChapter?.chapterId }"
              class="chapter-item"
              @click="selectChapter(chapter)" >

          <span>
          {{ chapter.name }}
        </span>
          <div class="menu-wrapper">
            <!-- 三点图标，选中或悬停时显示 -->
            <button
                class="menu-button"
                v-if="chapter.chapterId === selectedChapter?.chapterId || chapter.showMenu"
                @click.stop="toggleMenu(chapter.chapterId)">
              &#8942; <!-- 三点符号 -->
            </button>
            <!-- 弹出菜单 -->
            <div v-if="chapter.showMenu" class="menu-dropdown">
              <button @click="openRenameModal(chapter)">Rename</button>
              <button class="delete" @click="openDeleteModal(chapter)">Delete</button>
            </div>
          </div>
          </li>
        </ul>
      </div>
    </aside>

    <!-- Scrollable container for main content -->
    <div class="main-content-container">
      <div :style="{ backgroundImage: `url(${backgroundImage})` }" class="scrollable-container">
<!--        <img :src="backgroundImage" alt="Background Image" />-->

        <main class="main-content">
          <div class="top-bar">
            <header class="chapter-header">

              <span>{{ selectedChapter.subtitle }}</span>
            </header>
            <button @click="toggleInstructions" class="instructions-button">
              {{ showInstructions ? 'Hide Instructions' : 'Show Instructions' }}
            </button>
          </div>

          <div class="character-sections">
            <div class="left-column">
              <div class="character-select-wrapper">
              <h3>Character Select</h3>
                <section class="character-select">
                  <div v-if="selectedCharacters.length === 0" class="no-characters">

                  </div>
                  <div v-else v-for="character in selectedCharacters" :key="character.characterId" class="character-box">
                    <div class="character-select-card">
                      <span>{{ character.name }}</span>
                      <img @click.stop="unselectCharacter(character.characterId)" src="@/assets/close.svg" class="unselect-button" alt="Unselect Character"/>
                    </div>
                  </div>
                </section>
            </div>
              <div class="story-box-wrapper">
              <section class="story-box">
                <textarea v-model="storyDescription" class="story-textarea" placeholder="Describe the style and story of the game"></textarea>
                <img src="@/assets/create_button3.svg" @click="createStory" class="create-button" alt="Create Story"/>
              </section>
              </div>
            </div>

            <div class="character-list-wrapper">
              <h3>Character List</h3>
              <section class="character-list">

                <div v-if="availableCharacters.length === 0" class="no-characters">
                  There is no character.
                </div>
                <div v-else v-for="character in availableCharacters" :key="character.characterId" class="character-card" @click="addCharacterToChapter(character)">
                  <div class="character-header">
                    <img @click.stop="openEditModal(character)" src="@/assets/edit_button.svg" class="edit-icon" alt="Edit Character"/>
                    <img :src=character.image alt="Character Image" class="character-image"/>
                    <img @click.stop="removeCharacter(character.characterId)" src="@/assets/delete_icon.svg" class="delete-icon" alt="Delete Character"/>
                  </div>
                  <p><strong>Name:</strong> {{ character.name }}</p>
                  <p><strong>Role:</strong> {{ character.role }}</p>
                  <p><strong>Ability:</strong> {{ character.ability }}</p>
                </div>
              </section>
              <img src="@/assets/character_add.svg" @click="openAddCharacterModal" class="add-character-image" alt="Add Character"/>
            </div>
          </div>

          <section class="storyline-box">
            <div class="storyline-box-wrapper">
              <h3>Storyline</h3>
            <textarea v-model="storylineDescription" ></textarea>
            <div class="dropdown">
              <button class="dropdown-button" @click="toggleDropdown">
                {{ selectedLanguageName }} ▼
              </button>
              <ul v-if="dropdownOpen" class="dropdown-menu">
                <li @click="selectLanguage('en', 'English')">English</li>
                <li @click="selectLanguage('es', 'Spanish')">Spanish</li>
                <li @click="selectLanguage('fr', 'French')">French</li>
                <li @click="selectLanguage('de', 'German')">German</li>
                <li @click="selectLanguage('zh', 'Chinese (Simplified)')">Chinese (Simplified)</li>
                <li @click="selectLanguage('zh-TW', 'Chinese (Traditional)')">Chinese (Traditional)</li>
                <li @click="selectLanguage('ja', 'Japanese')">Japanese</li>
                <li @click="selectLanguage('ko', 'Korean')">Korean</li>
                <li @click="selectLanguage('ru', 'Russian')">Russian</li>
                <li @click="selectLanguage('pt', 'Portuguese')">Portuguese</li>
                <li @click="selectLanguage('it', 'Italian')">Italian</li>
                <li @click="selectLanguage('ar', 'Arabic')">Arabic</li>
                <li @click="selectLanguage('hi', 'Hindi')">Hindi</li>
                <!-- More languages can be added here -->
              </ul>
            </div>
            </div>
          </section>
          <div class="dialogue-box-wrapper">
            <h3>Character Dialogue</h3>
          <section class="dialogue-box">
            <textarea v-model="dialogueDescription" placeholder=""></textarea>
          </section>
          </div>
        </main>
      </div>
    </div>


    <div v-if="showInstructions" class="modal-overlay">
      <div class="modal-content">
        <button @click="toggleInstructions" class="close-modal">X</button>
        <section class="instructions">
          <h3>Instruction</h3>
          <p>1. Select a Chapter: Use the left sidebar to choose or add a chapter.</p>
          <p>2. Select Characters: Drag characters from "Character Select" to the "Character List" to include them in the story.</p>
          <p>3. Customize Characters: Click on a character in the "Character List".</p>
          <p>4. Write the Draft: Use the "Describe the style and story of the game" box to draft.</p>
          <p>5. Generate Story Dialogue.</p>
          <p>6. Download: download the final content.</p>
        </section>
      </div>
    </div>

    <div v-if="isEditModalVisible" class="modal-overlay">
      <div class="modal-content">
        <header class="modal-header">
          <h2>Edit Character</h2>
        </header>
        <section class="modal-body">
          <form @submit.prevent="updateCharacter">
            <!-- Flexbox 布局 -->
            <div class="form-container">
              <!-- 左边显示图片的正方形框以及图片上传 -->
              <div class="image-section">
                <label for="character-image">Character Image:</label>
                <div class="image-preview-box">
                  <img v-if="imageUrl" :src="imageUrl" alt="Character Image" class="image-preview" />
                  <div v-else class="placeholder">No Image</div>
                </div>
                <!-- Character Image 上传框在图片展示框的下面 -->
                <div class="image-upload">

                  <input id="character-image" type="file" @change="handleImageUpload" />
                </div>
              </div>
              <!-- 右边表单内容 -->
              <div class="form-fields">
                <div>
                  <label for="character-name">Name:</label>
                  <input id="character-name" v-model="editableCharacter.name" autocomplete="off" required />
                </div>
                <div>
                  <label for="character-role">Role:</label>
                  <input id="character-role" v-model="editableCharacter.role" autocomplete="off" required />
                </div>
                <div>
                  <label for="character-ability">Ability:</label>
                  <input id="character-ability" v-model="editableCharacter.ability" autocomplete="off" required />
                </div>
              </div>
            </div>
            <!-- Background 区域单独放在下面 -->
            <div class="background-section">
              <label for="character-background">Background:</label>
              <textarea id="character-background" v-model="editableCharacter.background"></textarea>
            </div>

            <div class="modal-footer">
              <button type="submit">Save</button>
              <button @click="closeEditModal" type="button">Cancel</button>
            </div>
          </form>
        </section>
      </div>
    </div>

    <!-- Rename Chapter Modal -->
    <div v-if="isRenameModalVisible" class="modal-overlay">
      <div class="modal-content">
        <header class="modal-header">
          <h2>Rename Chapter</h2>
        </header>
        <section class="modal-body">
          <form @submit.prevent="confirmRename">
            <label for="new-chapter-name">New Chapter Name:</label>
            <input id="new-chapter-name" v-model="newChapterName" required/>
            <div class="modal-footer">
              <button type="submit">Rename</button>
              <button @click="closeRenameModal" type="button">Cancel</button>
            </div>
          </form>
        </section>
      </div>
    </div>

    <!-- Delete Chapter Confirmation Modal -->
    <div v-if="isDeleteModalVisible" class="modal-overlay">
      <div class="modal-content">
        <header class="modal-header">
          <h2>Confirm Delete</h2>
        </header>
        <section class="modal-body">
          <p>Are you sure you want to delete this chapter?</p>
          <div class="modal-footer">
            <button @click="confirmDelete" type="button">Delete</button>
            <button @click="closeDeleteModal" type="button">Cancel</button>
          </div>
        </section>
      </div>
    </div>
  </div>
</template>


<script>
import Modal from './Modal.vue';
import axios from 'axios';
import backgroundImage from '@/assets/background.png';

export default {
  components: {
    Modal,
  },
  data() {
    return {
      defaultImage: backgroundImage,
      projectTitle: '',
      backgroundImage: '',
      chapters: [],
      newChapterName: '',
      selectedChapter: {},
      availableCharacters: [],
      selectedCharacters: [],
      storyDescription: '',
      showInstructions: false,
      isEditModalVisible: false,
      editableCharacter: null,
      imageUrl: null,
      isRenameModalVisible: false,
      isDeleteModalVisible: false,
      chapterToEdit: null,
      chapterToDelete: null,
      storylineDescription: '',
      dialogueDescription: '',
      originalStorylineDescription: '', // 新增变量保存原始故事描述
      originalDialogueDescription: '', // 新增变量保存原始对话描述
      selectedLanguage: 'zh',
      selectedLanguageName: 'Chinese (Simplified)',
      dropdownOpen: false
    };
  },
  async mounted() {
    const gameId = this.$route.params.gameId;
    await this.fetchGameName(gameId);
    await this.fetchChapters(gameId);
    await this.fetchCharactersForGame(gameId);
    document.addEventListener('click', this.handleClickOutside);
    if (this.chapters.length > 0) {
      // 如果获取到的章节列表不为空，默认选择第一个章节
      this.selectChapter(this.chapters[0]);
    }
  },
  methods: {
    async logout() {
      localStorage.removeItem('user');
      this.$router.push('/auth');
    },

    async fetchGameName(gameId) {
      try {
        const response = await axios.get(`/api/dashboard/games/${gameId}`);
        this.projectTitle = response.data.name;
        this.backgroundImage = response.data.image
            ? encodeURI(`http://localhost:8080${response.data.image}`)
            : this.defaultImage;
      } catch (error) {
        console.error('Error fetching game name:', error);
      }
    },
    async fetchChapters(gameId) {
      try {
        const response = await axios.get(`/api/chapters?gameId=${gameId}`);
        this.chapters = response.data;
      } catch (error) {
        console.error('Error fetching chapters:', error);
      }
    },

    async addChapter() {
      const newChapter = {
        name: this.newChapterName,
        description: '',
        userText: '',
        systemText: ''
      };

      try {
        // 使用 gameId 构建请求 URL
        const gameId = parseInt(this.$route.params.gameId, 10);

        // 发送请求时，将 gameId 作为路径参数传递
        const response = await axios.post(`/api/chapters/game/${gameId}`, newChapter, {
          headers: {
            'Content-Type': 'application/json'
          }
        });

        console.log('Chapter added:', response.data);
        this.chapters.push(response.data); // 更新章节列表
        this.selectChapter(response.data); // 自动选择新添加的章节
        this.newChapterName = '';
      } catch (error) {
        console.error('Error adding chapter:', error);
      }
    },

    openRenameModal(chapter) {
      this.chapterToEdit = chapter;
      this.newChapterName = chapter.name;
      this.isRenameModalVisible = true;
    },
    closeRenameModal() {
      this.isRenameModalVisible = false;
      this.chapterToEdit = null;
      this.newChapterName = '';
    },
    async confirmRename() {
      if (this.newChapterName.trim() !== '') {
        try {
          const response = await axios.put(`/api/chapters/${this.chapterToEdit.chapterId}`, { name: this.newChapterName.trim() });
          this.chapterToEdit.name = response.data.name;
          this.closeRenameModal();
        } catch (error) {
          console.error('Error renaming chapter:', error);
        }
      }
    },

    openDeleteModal(chapter) {
      this.chapterToDelete = chapter;
      this.isDeleteModalVisible = true;
    },
    closeDeleteModal() {
      this.isDeleteModalVisible = false;
      this.chapterToDelete = null;
    },
    async confirmDelete() {
      try {
        const chapterIdToDelete = this.chapterToDelete.chapterId;
        await axios.delete(`/api/chapters/${chapterIdToDelete}`);
        const indexToDelete = this.chapters.findIndex(chapter => chapter.chapterId === chapterIdToDelete);
        this.chapters = this.chapters.filter(chapter => chapter.chapterId !== chapterIdToDelete);
        this.closeDeleteModal();

        if (this.chapters.length > 0) {
          if (indexToDelete > 0) {
            this.selectChapter(this.chapters[indexToDelete - 1]);
          } else {
            this.selectChapter(this.chapters[0]);
          }
        }
      } catch (error) {
        console.error('Error deleting chapter:', error);
      }
    },

    async selectChapter(chapter) {
      this.selectedChapter = chapter; // 设置当前选中的章节

      try {
        // 发送请求到后端获取与该章节关联的角色
        const response = await axios.get(`/api/chapters/${chapter.chapterId}/characters`);

        // 更新 selectedCharacters 列表
        this.selectedCharacters = response.data;

        // Fetch the previously created story for the selected chapter
        await this.fetchPreviousStory(chapter.chapterId);

      } catch (error) {
        console.error('Error fetching characters or previous story for chapter:', error);
      }
    },

    async fetchPreviousStory(chapterId) {
      try {
        const response = await axios.get(`/api/stories/${chapterId}`);
        console.log('Previous story fetched:', response.data);

        // Check if the response data is empty or null
        if (!response.data || !response.data.storyline || !response.data.dialogue) {
          this.originalStorylineDescription = '';
          this.originalDialogueDescription = '';
          this.storylineDescription = '';
          this.dialogueDescription = '';
          return;
        }

        // Parse the JSON response to extract the storyline and dialogue
        const storyline = response.data.storyline;
        const dialogueArray = response.data.dialogue;

        const dialogue = dialogueArray.map(item => `${item.character}: ${item.text}`).join('\n');

        // 保存原始语言版本
        this.originalStorylineDescription = storyline;
        this.originalDialogueDescription = dialogue;

        // 更新当前显示的描述
        this.storylineDescription = storyline;
        this.dialogueDescription = dialogue;

      } catch (error) {
        console.error('Error fetching previous story:', error);
        // Set to empty values in case of error
        this.originalStorylineDescription = '';
        this.originalDialogueDescription = '';
        this.storylineDescription = '';
        this.dialogueDescription = '';
      }
    },

    toggleMenu(chapterId) {
      this.chapters = this.chapters.map(chapter => {
        if (chapter.chapterId === chapterId) {
          chapter.showMenu = !chapter.showMenu;
        } else {
          chapter.showMenu = false; // 隐藏其他菜单
        }
        return chapter;
      });
    },
    closeAllMenus() {
      this.chapters.forEach(chapter => {
        chapter.showMenu = false;
      });
    },
    handleClickOutside(event) {
      const menuWrapper = document.querySelector('.menu-dropdown');
      if (menuWrapper && !menuWrapper.contains(event.target)) {
        this.closeAllMenus();
      }
    },


    async fetchCharactersForGame(gameId) {
      try {
        const response = await axios.get(`/api/characters?gameId=${gameId}`);
        this.availableCharacters = response.data.map(character => {
          // 如果角色有图片，确保完整的图片URL
          character.image = character.image
              ? encodeURI(`http://localhost:8080${character.image}`)
              : this.defaultImage;
          return character;
        });
      } catch (error) {
        console.error('Error fetching characters:', error);
      }
    },
    openAddCharacterModal() {
      this.editableCharacter = { name: '', role: '', ability: '', background: '', image: null }; // 初始化空白角色
      this.imageUrl = null; // 确保图片为空
      this.isEditModalVisible = true; // 显示添加角色的模态框
    },
    selectCharacter(character) {
      if (!this.selectedCharacters.find(c => c.characterId === character.characterId)) {
        this.selectedCharacters.push(character);
      }
    },
    openEditModal(character) {
      this.editableCharacter = { ...character }; // 将当前角色的信息深拷贝到 editableCharacter
      this.imageUrl = character.image || null;   // 如果有角色图片，则设置为 imageUrl
      this.isEditModalVisible = true;            // 显示编辑模态框
    },
    closeEditModal() {
      this.isEditModalVisible = false;           // 隐藏模态框
      this.editableCharacter = null;             // 清空编辑的角色信息
      this.imageUrl = null;                      // 重置图片预览
    },
    handleImageUpload(event) {
      const file = event.target.files[0];
      this.imageUrl = URL.createObjectURL(file);  // 预览图片
      this.editableCharacter.image = file;
    },
    async updateCharacter() {
      try {
        const formData = new FormData();
        const gameId = this.$route.params.gameId;

        formData.append('character', JSON.stringify({
          name: this.editableCharacter.name,
          role: this.editableCharacter.role,
          ability: this.editableCharacter.ability,
          background: this.editableCharacter.background,
          gameId: gameId,
        }));

        if (this.editableCharacter.image) {
          formData.append('image', this.editableCharacter.image);
        }

        if (this.editableCharacter.characterId) {
          // Update existing character
          const response = await axios.put(`/api/characters/${this.editableCharacter.characterId}`, formData, {
            headers: {
              'Content-Type': 'multipart/form-data',
            },
          });
          const index = this.availableCharacters.findIndex(c => c.characterId === this.editableCharacter.characterId);
          if (index !== -1) {
            this.availableCharacters[index] = response.data;
            // Ensure the image URL is properly encoded
            this.availableCharacters[index].image = this.availableCharacters[index].image
                ? encodeURI(`http://localhost:8080${this.availableCharacters[index].image}`)
                : this.defaultImage;
          }
        } else {
          // Add new character
          const response = await axios.post('/api/characters', formData, {
            headers: {
              'Content-Type': 'multipart/form-data',
            },
          });
          response.data.image = response.data.image
              ? encodeURI(`http://localhost:8080${response.data.image}`)
              : this.defaultImage;
          this.availableCharacters.push(response.data);
        }

        this.closeEditModal();
      } catch (error) {
        console.error('Error updating character:', error);
      }
    },
    async removeCharacter(id) {
      try {
        await axios.delete(`/api/characters/${id}`);
        this.availableCharacters = this.availableCharacters.filter(character => character.characterId !== id);
        this.selectedCharacters = this.selectedCharacters.filter(character => character.characterId !== id);
      } catch (error) {
        console.error('Error deleting character:', error);
      }
    },
    toggleInstructions() {
      this.showInstructions = !this.showInstructions;
    },

    async addCharacterToChapter(character) {
      if (!this.selectedCharacters.find(c => c.characterId === character.characterId)) {
        this.selectedCharacters.push(character); // 前端更新已选角色

        try {
          // 向后端发送请求，添加角色到当前章节
          await axios.post(`/api/chapters/${this.selectedChapter.chapterId}/addCharacter`, {
            characterId: character.characterId,
          });
        } catch (error) {
          console.error('Error adding character to chapter:', error);
          // 如果请求失败，可以从 selectedCharacters 中移除
          this.selectedCharacters.pop();
        }
      }
    },

    async unselectCharacter(characterId) {
      this.selectedCharacters = this.selectedCharacters.filter(character => character.characterId !== characterId);

      try {
        // 向后端发送请求，移除角色与当前章节的关联
        await axios.post(`/api/chapters/${this.selectedChapter.chapterId}/removeCharacter`, {
          characterId,
        });
      } catch (error) {
        console.error('Error removing character from chapter:', error);
      }
    },

    async createStory() {
      if (!this.selectedChapter || !this.selectedChapter.chapterId) {
        console.error('No chapter selected');
        return;
      }

      const payload = {
        chapterId: this.selectedChapter.chapterId,
        storyDescription: this.storyDescription
      };

      try {
        const response = await axios.post('/api/stories/create', payload);
        console.log('Story created:', response.data);

        // Parse the JSON response to extract the storyline
        const storyline = response.data.storyline;
        const dialogueArray = response.data.dialogue;

        const dialogue = dialogueArray.map(item => `${item.character}: ${item.text}`).join('\n');

        // 保存原始语言版本
        this.originalStorylineDescription = storyline;
        this.originalDialogueDescription = dialogue;

        // 更新当前显示的描述
        this.storylineDescription = storyline;
        this.dialogueDescription = dialogue;

      } catch (error) {
        console.error('Error creating story:', error);
      }
    },

    toggleDropdown() {
      this.dropdownOpen = !this.dropdownOpen;
    },
    
    // 选择语言时更新选中的语言
    async selectLanguage(code, name) {
      this.selectedLanguage = code;
      this.selectedLanguageName = name;
      this.dropdownOpen = false;

      await this.translateContent();
    },

    async translateContent() {
      try {
        // 翻译并替换故事描述
        this.storylineDescription = await this.translateText(this.originalStorylineDescription, this.selectedLanguage);

        // 翻译并替换对话描述
        this.dialogueDescription = await this.translateText(this.originalDialogueDescription, this.selectedLanguage);

      } catch (error) {
        console.error('Translation failed:', error);
      }
    },
    
    // 调用Google Translate API翻译文本
    async translateText(text, targetLanguage) {
      const API_KEY = 'AIzaSyCPa0d4nRmeZsI7vpFHANj_5SU7wfteEu8';
      const URL = `https://translation.googleapis.com/language/translate/v2?key=${API_KEY}`;

      try {
        const response = await axios.post(URL, {
          q: text,
          target: targetLanguage
        });
        return response.data.data.translations[0].translatedText;
      } catch (error) {
        console.error('Error translating text:', error);
        throw error;
      }
    },

    async handleDownload() {
      if (!this.$route.params.gameId) {
        console.error('No gameId available for download');
        return;
      }

      try {
        // 发送 GET 请求，传递 gameId 到后端
        const response = await axios({
          url: `/api/pdf/generate?gameID=${this.$route.params.gameId}`, // 使用 GET 请求
          method: 'GET',
          responseType: 'blob' // 将响应类型设置为 blob，便于处理文件下载
        });

        // 创建一个 URL 供下载使用
        const blob = new Blob([response.data], { type: 'application/pdf' });
        const link = document.createElement('a');
        link.href = window.URL.createObjectURL(blob);
        link.download = `game_${this.$route.params.gameId}.pdf`; // 设置下载的文件名
        link.click(); // 触发下载
      } catch (error) {
        console.error('Error downloading PDF:', error);
      }
    }
  },



};
</script>


<style scoped>
.workshop-container {
  display: flex;
  height: 200vh;
  background-color: #333;
  color: #fff;
}

.top-nav {
  background-color: #000;
  padding: 10px 20px;
  position: fixed; /* Fix the nav bar at the top */
  width: 100%; /* Make it span the full width */
  top: 0;
  left: 0;
  z-index: 1000; /* Keep it above other content */
  display: flex;
  justify-content: space-between; /* Space between logo and nav items */
  align-items: center;
  box-sizing: border-box;
  height: 40px; /* Ensure a fixed height for the nav bar */
}


.gamestory {
  color: #A0A0A0;
  font-weight: normal;
  font-size: 26px;
  font-family: Lobster, cursive;
}

.forge {
  color: #727272;
  font-weight: normal;
  font-size: 26px;
  font-family: Lobster, cursive;
}

.nav-right .nav-list {
  list-style: none;
  display: flex; /* 改成 flex 布局 */
  align-items: center; /* 垂直对齐到中心 */
  color: #E1E1E1;
  cursor: pointer;
  font-size: 16px;
  font-weight: normal;
  font-family: Lora, serif;
}

.nav-right .nav-list li {
  margin-left: 15px;
}

.nav-right .nav-list li a,
.nav-right .nav-list li button {
  background: none;
  border: none;
  cursor: pointer;
  text-decoration: none;
  font-size: 16px;
  color: #E1E1E1;
  font-weight: normal;
  font-family: Lora, serif;
  display: flex; /* 确保按钮内的文本居中对齐 */
  align-items: center; /* 垂直居中对齐 */
  padding: 0px 0px 0 5px; /* 添加一些内边距确保点击区域足够 */
}

.nav-right .nav-list li button:hover,
.nav-right .nav-list li a:hover {
  text-decoration: underline;
}

.nav-right .nav-list li button:focus {
  outline: none;
}


.sidebar {
  width: 200px;
  background-color: #000000;
  padding: 20px;
  padding-top: 45px;
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.sidebar h3 {
  margin-bottom: 10px;
  margin-top: 10px;
  font-weight: normal;
  font-size: 20px;
}


.sidebar ul {
  list-style: none;
  padding: 0;
  margin: 5px 5px;
}

.sidebar ul li {
  margin: 3px 0;
  cursor: pointer;
  font-size: 18px;
  padding: 6px 5px 6px 13px;
  height: 24px;
}

.sidebar ul li:hover {
  background-color: #36373f; /* Change background color on hover */
  border-radius: 8px;
}

.sidebar ul li.selected {
  background-color: #36373f; /* Highlight color */
  border-radius: 8px;
}

.sidebar input {
  margin-top: 10px;
  padding: 5px;
  background-color: #555;
  color: #fff;
  border: none;
  border-radius: 5px;
}

.input-button-container {
  position: relative;
  display: inline-block;

}

.input-button-container input {
  padding-right: 60px; /* Adjust padding to make space for the button */
  width: 130px;
  background-color: #000; /* Set background color to black */
  border: 1.5px solid #a1a1a1; /* Set border color to white */
  color: #E1E1E1;
  font-weight: normal;
  font-family: Lora, serif;
}

.input-button-container .add-button {
  margin-top: 12px;
  position: absolute;
  right: 3px;
  top: 0;
  height: 100%;
  cursor: pointer;
  border-radius: 0 5px 5px 0;
  width: 24px; /* Set the desired width */
  height: 24px; /* Set the desired height */
}



.chapter-list-container {
  text-align: left; /* Align content to the left */
  color: #E1E1E1;
  font-weight: normal;
  font-family: Lora, serif;
}

.chapter-item.selected {
  background-color: #36373f; /* Highlight color */
  border-radius: 8px; /* 圆角边框 */
  color: white; /* 设置选中后字体颜色 */
}

/* 普通章节样式 */
.chapter-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px;
  position: relative;
  cursor: pointer; /* 添加手型光标以指示整个区域可点击 */
}

.menu-wrapper {
  position: relative;
}

/* 隐藏默认情况下的三点按钮 */
.menu-button {
  background: none;
  border: none;
  cursor: pointer;
  font-size: 18px;
  color: #ddd;
}

/* 鼠标悬停时显示三点按钮 */
.chapter-item:hover .menu-button {
  display: block;
}

.menu-button:hover {
  color: #ffffff; /* 鼠标悬停时按钮颜色 */
}

/* 菜单下拉样式 */
.menu-dropdown {
  position: absolute;
  right: 0;
  top: 100%;
  background-color: #3f3f46;
  border: 1px solid #515157;
  border-radius: 4px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  z-index: 10;
}

.menu-dropdown button {
  display: block;
  width: 100px;
  padding: 8px;
  background: none;
  border: none;
  text-align: left;
  cursor: pointer;
  color: rgba(215, 215, 215, 0.88);
}

.menu-dropdown button.delete {
  color: #e10808;
}

.menu-dropdown button:hover {
  background-color: #505056;
}


html, body {
  height: 100%;
  margin: 0;
  padding: 0;
  overflow: hidden; /* 阻止整个页面的滚动条 */

}

.main-content-container {
  flex-grow: 1;
  margin-top: 40px; /* Add a top margin equal to the nav bar's height */
  display: flex;
  flex-direction: column;

  background-color: #000000;
}

.workshop-container {
  display: flex;
  height: 100vh; /* 让容器充满视口高度 */
  background-color: #333;
  color: #fff;
}

.scrollable-container {
  flex-grow: 1;
  padding: 20px;
  padding-left: 0;
  padding-right: 15px;
  padding-top: 0;
  overflow-y: auto; /* 使内容可滚动 */
  background-color: #f4f4f4;
  border-radius: 12px;
  margin: 0 8px 8px 0;
  background: url('http://localhost:8080/uploads/images/51c43978-71e3-4b5f-a2d4-21f772de4f02_Screenshot 2024-08-24 095635.png');
  background-size: cover;
}


.main-content {
  flex-grow: 1;
  display: flex;
  flex-direction: column;

}

.top-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 10px;
  margin-bottom: 10px;
}

.instructions-button {
  font-size: 14px;
  padding: 8px 15px;
  background-color: #1f67b4;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-weight: normal;
  font-family: Lora, serif;
}

.close-modal {
  padding-left: 395px;
  background: none;
  border: none;
  color: #fff;
  font-size: 20px;
  cursor: pointer;
}

.close-modal:hover {
  color: #ccc;
}

.instructions-button:hover {
  background-color: #0056b3;
}

.chapter-header {
  margin-bottom: 20px;
}

.instructions,
.character-select,
.character-list,
.story-box {
  margin-bottom: 20px;
  font-weight: normal;
  font-family: Lora, serif;

}



.character-list-wrapper {
  background-color: rgba(62, 64, 74, 0.95); /* Adjust the border color and width as needed */
  border-radius: 12px; /* Optional: Add border radius */
  width: 400px;
  padding: 0;
  height: 748px;
}

.character-select-wrapper {
  background-color: rgba(62, 64, 74, 0.95); /* Adjust the border color and width as needed */
  border-radius: 12px; /* Optional: Add border radius */
  width: 512px;
  padding: 0;
  margin-bottom: 30px;
  height: 355px;
  border: 4px dashed #adadad;

}



.character-select-wrapper h3 {
  margin-bottom: 12px;
  margin-top: 12px;
  font-weight: normal;
  font-family: Lora, serif;
  }

.character-list-wrapper h3 {
  margin-bottom: 12px;
  margin-top: 16px;
  font-weight: normal;
  font-family: Lora, serif;
}

.character-sections {
  display: flex;
  gap: 30px; /* Space between the two sections */
  width: 900px;
  margin: 0 auto;
  margin-bottom: -5px;
}

.left-column {
  display: flex;
  flex-direction: column;
  flex: 1;
  margin-right: 0px;

}

.character-select, .character-list {
  flex: 1;
  background-color: rgba(62, 64, 74, 0);
  padding: 20px;
  border-radius: 12px;
  overflow-y: auto; /* 启用垂直滚动 */
  height: 626px; /* 设置固定的最大高度 */
  scrollbar-color: #989898 #2c2c2c; /* 滑块颜色 滚动条轨道颜色 */
  scrollbar-width: thin; /* 可以为 auto, thin 或 none */
  margin-bottom: 0px;
  padding-top: 0px;
}

.character-select {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(140px, 1fr)); /* 自动填充列 */
  grid-auto-rows: 90px; /* 设置每行卡片的高度 */
  gap: 10px; /* 卡片之间的间距 */
  max-height: 280px; /* 设置最大高度，限制行数 */
  overflow-y: auto; /* 超出行数时滚动 */
}

.character-box {
  height: 60px; /* Set the desired height */
  display: flex;
  flex-wrap: wrap;
  gap: 20px; /* 设置卡片之间的间距 */
  justify-content: space-between;

}


.character-box p {
  margin-top: 0;
  margin-bottom: 10px;
  cursor: pointer;
}

.character-list .character-card {
  background-color: #25272f;
  border-radius: 7px;
  padding: 5px;
  margin-bottom: 10px;
  cursor: pointer;
  text-align: center;
}

.character-card p {
  margin-top: 0;
  margin-bottom: 5px;
  gap: 10px;
}

.character-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  position: relative;
  margin-top: 5px;
  margin-left: 2px;
  margin-right: 2px;
}

.edit-icon, .delete-icon {
  width: 30px; /* Set the desired width */
  height: 30px; /* Set the desired height */
  cursor: pointer;
  margin-top: -35px;
}

.edit-icon:hover, .delete-icon:hover {
  transform: scale(1.1); /* Add a hover effect */
}

.character-image {
  width: 50px; /* Set the desired width */
  height: 50px; /* Set the desired height */
  object-fit: cover; /* Ensure the image covers the area */
  border-radius: 50%; /* Make the image circular */
  margin-bottom: 10px; /* Add some space below the image */
}

.character-select-card {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #25272f;
  border-radius: 7px;
  padding: 10px;
  cursor: pointer;
  width: 150px;
  height: 70px;
}

.unselect-button {
  width: 20px; /* Set the desired width */
  height: 20px; /* Set the desired height */
  cursor: pointer;
  margin-right: -5px;
}

.unselect-button:hover {
  filter:  brightness(1.2); /* Add a hover effect */
}

.character-list .no-characters {
  color: grey;
  font-style: italic;
}

.character-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

.add-character-image {
  margin-top: 10px;
  margin-bottom: 5px;
  cursor: pointer;
  width: 30px; /* Adjust the size as needed */
  height: 30px; /* Adjust the size as needed */
}

.add-character-image:hover {
  filter: brightness(1.2); /* Add a hover effect */
}

.story-box {
  width: 520px;
  flex: 1;
}

.story-box-wrapper {
  background-color: rgba(62, 64, 74, 0.95); /* Adjust the border color and width as needed */
  border-radius: 12px; /* Optional: Add border radius */
  width: 520px;
  padding: 0;
  margin-bottom: 30px;
  height: 355px;
}

.story-box textarea {
  margin-top: 15px;
  padding: 0 10px 15px 20px;
  width: 490px;
  height: 275px;
  background-color: rgba(62, 64, 74, 0);
  color: #fff;
  border: none;
  border-radius: 12px;

  resize: none;
  font-weight: normal;
  font-family: Lora, serif;
  font-size: 16px;
  scrollbar-color: #989898 #2c2c2c; /* 滑块颜色 滚动条轨道颜色 */
  scrollbar-width: thin; /* 可以为 auto, thin 或 none */
}

.story-box textarea,.storyline-box textarea, .dialogue-box textarea:focus {
  outline: none; /* Remove the default focus outline */
  /* Optionally, add custom focus styles */
  border: none; /* Remove any border */
}

.story-textarea::placeholder {
  color: #aaa; /* 设置占位符为浅灰色 */
  opacity: 1; /* 可选：设置占位符的不透明度，默认为0.5 */
}


.create-button {
  margin-top: 1px;
  margin-bottom: 10px;
  cursor: pointer;
  width: 37px; /* Adjust the size as needed */
  height: 37px; /* Adjust the size as needed */
  padding: 0;
}

.create-button:hover {
  filter: brightness(1.2); /* Add a hover effect */
}

.storyline-box-wrapper {
  background-color: rgba(62, 64, 74, 0.95); /* Adjust the border color and width as needed */
  border-radius: 12px; /* Optional: Add border radius */
  width: 900px;
  padding: 0;
  height: 370px;
  margin: 0px auto;
  margin-bottom: 60px;
}

.storyline-box-wrapper h3 {
  margin-bottom: 15px;
  margin-top: 10px;
  font-weight: normal;
  font-family: Lora, serif;
}

.storyline-box {
  display: flex;
  gap: 30px; /* Space between the two sections */
  width: 900px;
  margin: 10px auto;
  height: 300px;
  margin-top: 10px;
  margin-bottom: 70px;
  position: relative;
}


.storyline-box textarea {
  width: 860px;
  height: 300px;
  background-color: rgba(62, 64, 74, 0);
  color: #fff;
  border: none;
  border-radius: 12px;
  padding: 0px 20px 0px 20px;
  resize: none;
  font-weight: normal;
  font-family: Lora, serif;
  font-size: 16px;
  scrollbar-color: #989898 #2c2c2c; /* 滑块颜色 滚动条轨道颜色 */
  scrollbar-width: thin; /* 可以为 auto, thin 或 none */
}

.storyline-box textarea::placeholder {
  color: #bbb;
}

.dropdown {
  position: absolute;
  top: 10px;
  right: 10px; /* Adjust right position to ensure button fits inside box */
}

.dropdown-button {
  padding: 10px 15px;
  background-color: #4c5caf;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  outline: none;
  font-size: 14px;
  transition: background-color 0.3s ease;
  font-weight: normal;
  font-family: Lora, serif;
}

.dropdown-button:hover {
  background-color: #4557a0;
}

.dropdown-menu {
  position: absolute;
  top: 40px; /* Adjust to display below the button */
  right: 0;
  background-color: #fff;
  border: 1px solid #ccc;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  list-style-type: none;
  padding: 10px 0;
  margin: 0;
  width: 180px; /* Define a width for better alignment */
  z-index: 1;
  transition: opacity 0.3s ease;
}

.dropdown-menu li {
  padding: 10px 20px;
  font-size: 14px;
  cursor: pointer;
  color: #333;
  transition: background-color 0.3s ease, color 0.3s ease;
}

.dropdown-menu li:hover {
  background-color: #f0f0f0;
  color: #4CAF50;
}

.dropdown-menu li:not(:last-child) {
  border-bottom: 1px solid #ddd;
}

.scrollable-container {
  scrollbar-color: #989898 #2c2c2c; /* 滑块颜色 滚动条轨道颜色 */
  scrollbar-width: thin; /* 可以为 auto, thin 或 none */
  -webkit-border-radius: 12px;

}

::-webkit-scrollbar {
  width: 20px;
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.7);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1001;

}

.modal-content {
  background: #3f3f48;
  padding: 10px 20px 20px 20px;
  border-radius: 8px;
  width: 400px;
  font-family: 'Lora', serif;
  color: #D2D2D2;
}

.modal-content label {
  display: block;
  margin-bottom: 5px;
}

.form-container {
  display: flex;
  gap: 35px; /* 图片和表单字段之间的间距 */
}

.image-section {
  display: flex;
  flex-direction: column;
  margin-left: 10px;
  justify-content: center;
  align-items: center;
}
.image-preview-box {
  width: 146px;
  height: 146px;
  display: flex;
  justify-content: center;
  align-items: center;
  border-radius: 8px;
  border: 2px solid #A4A4A4;
}


.image-preview {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 8px;
  border: 2px solid #A4A4A4;
}

.form-fields {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.form-fields label {
  text-align: left; /* 确保标签文字左对齐 */
  display: block;
  margin-bottom: 5px; /* 给标签和输入框之间增加一些间距 */
  font-size: 16px;
}

.image-upload input[type="file"],
.form-fields input {
  width: 194px;
  height: 41px;
  padding: 10px;
  margin-bottom: 15px;
  border: 2px solid #A4A4A4;
  border-radius: 8px;
  background: #3f3f46;
  color: #D2D2D2;
  font-family: 'Lora', serif;
  box-sizing: border-box;

}

.image-upload input[type="file"] {
  margin-top: 15px;
  width: 150px;
  height: 41px;
}

.modal-header {
  position: relative; /* 使关闭按钮绝对定位 */
  text-align: center; /* 使标题居中 */
  padding-bottom: 10px;

}
.modal-header h2 {
  font-size: 24px;
  font-weight: normal;
  margin: 0;
}


.background-section label {
  display: block;
  margin-bottom: 5px;
}

.background-section textarea {
  width: 95%;
  padding: 10px;
  border: 2px solid #A4A4A4;
  border-radius: 8px;
  background: #3f3f46;
  color: #D2D2D2;
  font-family: 'Lora', serif;
  box-sizing: border-box;
  resize: none;
  height: 100px;
}



.modal-footer {
  display: flex;
  justify-content: center; /* 将按钮水平居中 */
  padding-top: 10px;
  width: fit-content; /* 确保只根据内容的宽度 */
  margin: 0 auto;
  gap: 30px;
}

.modal-footer button {
  padding: 10px 20px;
  background-color: #0776ee;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-family: 'Lora', serif;
}

.modal-footer button:hover {
  background-color: #0056b3;
}


.instructions {
  color: #ffffff;
  text-align: left;

}

.instructions h3 {
  margin-top: 0px;
  text-align: center;
}

#new-chapter-name {
  width: 50%; /* Set the width to 100% of the parent container */
  padding: 10px; /* Add padding for better spacing */
  margin-bottom: 10px; /* Add margin at the bottom for spacing */
  border: 2px solid #A4A4A4; /* Set the border color and width */
  border-radius: 8px; /* Add rounded corners */
  background-color: #3E404A; /* Set the background color */
  color: #D2D2D2; /* Set the text color */
  font-family: 'Lora', serif; /* Set the font family */
  box-sizing: border-box; /* Ensure padding and border are included in the element's total width and height */
}

.dialogue-box-wrapper {
  background-color: rgba(62, 64, 74, 0.95); /* Adjust the border color and width as needed */
  border-radius: 12px; /* Optional: Add border radius */
  width: 900px;
  padding: 0;
  height: 340px;
  margin: 10px auto;
  margin-bottom: 20px;
  margin-top: 35px;
}

.dialogue-box-wrapper h3 {
  margin-bottom: 10px;
  margin-top: 10px;
  font-weight: normal;
  font-family: Lora, serif;
}

.dialogue-box {
  display: flex;
  width: 900px;
  margin: 10px auto;
  height: 300px;
  margin-top: 10px;
}

.dialogue-box textarea {
  width: 860px;
  height: 260px;
  background-color: rgba(62, 64, 74, 0);
  color: #fff;
  border: none;
  border-radius: 12px;
  padding: 0px 20px 20px 20px;
  resize: none;
  font-weight: normal;
  font-family: Lora, serif;
  font-size: 16px;
  scrollbar-color: #989898 #2c2c2c; /* 滑块颜色 滚动条轨道颜色 */
  scrollbar-width: thin; /* 可以为 auto, thin 或 none */
}

.dialogue-box textarea::placeholder {
  color: #bbb;
}
</style>
  