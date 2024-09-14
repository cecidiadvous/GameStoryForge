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
        <ul>
          <li v-for="chapter in chapters" :key="chapter.chapterId" @click="selectChapter(chapter)"
              :class="{ selected: chapter.chapterId === selectedChapter.chapterId }">
            {{ chapter.name }}
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
<!--                  There is no character selected.-->
                </div>
                <div class="character-box" v-else v-for="character in selectedCharacters" :key="character.characterId">
                  <div class="character-select-card">
                    <span>{{ character.name }}</span>
                    <img @click.stop="unselectCharacter(character.characterId)" src="@/assets/close.svg" class="unselect-button" alt="Unselect Character"/>
                  </div>
                </div>
              </section>
            </div>
              <div class="story-box-wrapper">
              <section class="story-box">
                <textarea v-model="storyDescription" placeholder="Describe the style and story of the game"></textarea>
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
                <div v-else v-for="character in availableCharacters" :key="character.characterId" class="character-card"
                     @click="selectCharacter(character)">
                  <p><strong>Name:</strong> {{ character.name }}</p>
                  <p><strong>Role:</strong> {{ character.role }}</p>
                  <p><strong>Ability:</strong> {{ character.ability }}</p>
                  <div class="character-actions">
                    <button @click.stop="openEditModal(character)">✏️</button>
                    <button @click.stop="removeCharacter(character.characterId)">🗑️</button>
                  </div>
                </div>
              </section>
              <img src="@/assets/character_add.svg" @click="openAddCharacterModal" class="add-character-image" alt="Add Character"/>
            </div>
          </div>

          <section class="storyline-box">
            <textarea v-model="storylineDescription" ></textarea>
          </section>
        </main>
      </div>
    </div>

    <!-- Modal for Editing Character -->
    <div v-if="showInstructions" class="modal-overlay">
      <div class="modal-content">
        <button @click="toggleInstructions" class="close-modal">Close</button>
        <section class="instructions">
          <h3>Instruction</h3>
          <p>1. Select a Chapter: Use the left sidebar to choose or add a chapter.</p>
          <p>2. Select Characters: Drag characters from "Character Select" to the "Character List" to include them in
            the story.</p>
          <p>3. Customize Characters: Click on a character in the "Character List".</p>
          <p>4. Write the Draft: Use the "Describe the style and story of the game" box to draft.</p>
          <p>5. Generate Story Dialogue.</p>
          <p>6. Download: download the final content.</p>
        </section>
      </div>
    </div>

    <Modal :visible="isEditModalVisible" @close="closeEditModal">
      <template #header>
        <h2>Edit Character</h2>
      </template>
      <template #body>
        <form @submit.prevent="updateCharacter">
          <div>
            <label for="character-name">Name:</label>
            <input id="character-name" v-model="editableCharacter.name" required/>
          </div>
          <div>
            <label for="character-role">Role:</label>
            <input id="character-role" v-model="editableCharacter.role" required/>
          </div>
          <div>
            <label for="character-ability">Ability:</label>
            <input id="character-ability" v-model="editableCharacter.ability" required/>
          </div>
          <div>
            <label for="character-background">Background:</label>
            <textarea id="character-background" v-model="editableCharacter.background"></textarea>
          </div>
          <button type="submit">Save</button>
        </form>
      </template>
      <template #footer>
        <button @click="closeEditModal">Close</button>
      </template>
    </Modal>
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
      editableCharacter: null, // The character being edited
    };
  },
  async mounted() {
    const gameId = this.$route.params.gameId;
    await this.fetchGameName(gameId);
    await this.fetchChapters(gameId);
    await this.fetchCharactersForGame(gameId);
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
      if (this.newChapterName.trim() !== '') {
        const gameId = this.$route.params.gameId; // Retrieve gameId from route parameters
        const newChapter = {
          name: this.newChapterName,
          description: '',
          gameId: parseInt(gameId, 10), // Ensure gameId is an integer
          userText: '',
          systemText: '',
        };

        try {
          const response = await axios.post('/api/chapters', newChapter);
          this.chapters.push(response.data);
          this.newChapterName = '';
        } catch (error) {
          console.error('Error adding chapter:', error);
        }
      }
    },
    selectChapter(chapter) {
      this.selectedChapter = chapter;
    },
    async fetchCharactersForGame(gameId) {
      try {
        const response = await axios.get(`/api/characters?gameId=${gameId}`);
        this.availableCharacters = response.data;
      } catch (error) {
        console.error('Error fetching characters:', error);
      }
    },
    openAddCharacterModal() {
      this.editableCharacter = {name: '', role: '', ability: '', background: ''};
      this.isEditModalVisible = true;
    },
    selectCharacter(character) {
      if (!this.selectedCharacters.find(c => c.characterId === character.characterId)) {
        this.selectedCharacters.push(character);
      }
    },
    async unselectCharacter(id) {
      this.selectedCharacters = this.selectedCharacters.filter(character => character.characterId !== id);

    },
    openEditModal(character) {
      this.editableCharacter = {...character};
      this.isEditModalVisible = true;
    },
    closeEditModal() {
      this.isEditModalVisible = false;
      this.editableCharacter = null;
    },
    async updateCharacter() {
      const index = this.availableCharacters.findIndex(c => c.characterId === this.editableCharacter.characterId);
      if (index !== -1) {
        try {
          const response = await axios.put(`/api/characters/${this.editableCharacter.characterId}`, this.editableCharacter);
          this.availableCharacters[index] = response.data;
          this.closeEditModal();
        } catch (error) {
          console.error('Error updating character:', error);
        }
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
  padding: 6px 13px;
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
  //background: url('http://localhost:8080/uploads/images/51c43978-71e3-4b5f-a2d4-21f772de4f02_Screenshot 2024-08-24 095635.png');
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
  grid-auto-rows: 70px; /* 设置每行卡片的高度 */
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
  padding: 10px;
  margin-bottom: 10px;
  cursor: pointer;
}

.character-select-card {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #25272f;
  border-radius: 7px;
  padding: 10px;
  cursor: pointer;
  width: 120px;
}

.unselect-button {
  width: 20px; /* Set the desired width */
  height: 20px; /* Set the desired height */
  cursor: pointer;
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

.story-box textarea:focus {
  outline: none; /* Remove the default focus outline */
  /* Optionally, add custom focus styles */
  border: none; /* Remove any border */
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

.storyline-box {
  display: flex;
  gap: 30px; /* Space between the two sections */
  width: 900px;
  margin: 10px auto;
  height: 300px;
  margin-top: 10px;
}


.storyline-box textarea {
  width: 860px;
  height: 300px;
  background-color: rgba(62, 64, 74, 0.95);
  color: #fff;
  border: none;
  border-radius: 12px;
  padding: 20px;
  resize: none;
  font-weight: normal;
  font-family: Lora, serif;
  font-size: 16px;
}

.storyline-box textarea::placeholder {
  color: #bbb;
}

.scrollable-container {
  scrollbar-color: #989898 #2c2c2c; /* 滑块颜色 滚动条轨道颜色 */
  scrollbar-width: thin; /* 可以为 auto, thin 或 none */
  -webkit-border-radius: 12px;

}

::-webkit-scrollbar {
  width: 20px;
}

/* Track */

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.7); /* 半透明背景 */
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1001; /* 保证在最顶层 */
}

.modal-content {
  background-color: #000000;
  padding: 20px;
  border-radius: 12px;
  width: 50%; /* 可根据需要调整宽度 */
  max-width: 600px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.3);
  position: relative;
}

.close-modal {
  position: absolute;
  top: 10px;
  right: 10px;
  background: none;
  border: none;
  font-size: 18px;
  cursor: pointer;
  color: #ffffff;
}

.instructions {
  color: #ffffff;
  text-align: left;

}

.instructions h3 {
  margin-top: 10px;
  text-align: center;

}


/* width */


</style>
  