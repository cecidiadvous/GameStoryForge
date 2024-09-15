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
          <li><button @click="handleDownload">Download</button></li>
          <li><router-link to="/home">Home</router-link></li>
          <li><router-link to="/dashboard">Dashboard</router-link></li>
          <li><button @click="logout">Log Out</button></li>
        </ul>
      </div>
    </nav>

    <aside class="sidebar">
      <div class="input-button-wrapper">
        <div class="input-button-container">
          <input v-model="newChapterName" placeholder="Add chapter name" @keyup.enter="addChapter" />
          <img src="@/assets/chapter_add.svg" @click="addChapter" class="add-button" />
        </div>
      </div>
      <div class="chapter-list-container">
        <h3>{{ projectTitle }}</h3>
        <ul>
          <li v-for="chapter in chapters" :key="chapter.chapterId" @click="selectChapter(chapter)" :class="{ selected: chapter.chapterId === selectedChapter.chapterId }">
            {{ chapter.name }}
          </li>
        </ul>
      </div>
    </aside>

    <!-- Scrollable container for main content -->
    <div class="main-content-container">
      <div class="scrollable-container">
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
            <section class="character-select">
              <h3>Selected Characters</h3>
              <div v-if="selectedCharacters.length === 0" class="no-characters">
                There is no character selected.
              </div>
              <div class="character-box" v-else v-for="character in selectedCharacters" :key="character.characterId" @click="unselectCharacter(character.characterId)">
                <p>{{ character.name }}</p>
              </div>
            </section>

            <section class="character-list">
              <h3>Character List</h3>
              <div v-if="availableCharacters.length === 0" class="no-characters">
                There is no character.
              </div>
              <div v-else v-for="character in availableCharacters" :key="character.characterId" class="character-card" @click="selectCharacter(character)">
                <p><strong>Name:</strong> {{ character.name }}</p>
                <p><strong>Role:</strong> {{ character.role }}</p>
                <p><strong>Ability:</strong> {{ character.ability }}</p>
                <div class="character-actions">
                  <button @click.stop="openEditModal(character)">✏️</button>
                  <button @click.stop="removeCharacter(character.characterId)">🗑️</button>
                </div>
              </div>
              <!-- Add New Character-->
              <div class="add-new-character">
                <!-- <h3>Add a New Character</h3> -->
                <div>
                  <label for="new-character-name">Name: </label>
                  <input id="new-character-name" v-model="newCharacter.name" required />
                </div>
                <div>
                  <label for="new-character-role">Role: </label>
                  <input id="new-character-role" v-model="newCharacter.role" required />
                </div>
                <div>
                  <label for="new-character-ability">Ability: </label>
                  <input id="new-character-ability" v-model="newCharacter.ability" required />
                </div>
                <button type="button" class="add-character-btn" @click="addCharacter">Add Character</button>
              </div>
            </section>
          </div>

          <section class="story-box">
            <h3>Describe the style and story of the game</h3>
            <textarea v-model="storyDescription" placeholder="Describe the style and story of the game"></textarea>
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
          <p>2. Select Characters: Drag characters from "Character Select" to the "Character List" to include them in the story.</p>
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
            <input id="character-name" v-model="editableCharacter.name" required />
          </div>
          <div>
            <label for="character-role">Role:</label>
            <input id="character-role" v-model="editableCharacter.role" required />
          </div>
          <div>
            <label for="character-ability">Ability:</label>
            <input id="character-ability" v-model="editableCharacter.ability" required />
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
  
  export default {
    components: {
      Modal,
    },
    data() {
      return {
        projectTitle: 'Elden Ring',
        gameId: null,
        chapters: [],
        newChapterName: '',
        selectedChapter: {},
        availableCharacters: [],
        selectedCharacters: [],
        storyDescription: '',
        showInstructions: false,
        isEditModalVisible: false,
        editableCharacter: null, // The character being edited
        // add new character data
        newCharacter: {
          name: '',
          role: '',
          ability: ''
        }
      };
    },
    methods: {
      async logout() {
        localStorage.removeItem('user');
        this.$router.push('/login');
      },

      async addCharacter() {
        console.log('Add Character button clicked!'); 
        if (this.newCharacter.name && this.newCharacter.role && this.newCharacter.ability) {
          try {
            // Add the gameId to the character before making the request
            this.newCharacter.gameId = this.gameId;

            const response = await axios.post('/api/characters', this.newCharacter);
            console.log('Character added successfully:', response.data); 
            this.availableCharacters.push(response.data);

            // Reset the newCharacter object after the character is added
            this.newCharacter = { 
              name: '', 
              role: '', 
              ability: '' 
            };
          } catch (error) {
            console.error('Error adding character:', error);
          }
        }
      },

      async fetchChapters() {
        try {
          const response = await axios.get(`/api/chapters`, {
            params: {
              gameId: this.gameId // Include gameId as a query parameter
            }
          });
          this.chapters = response.data;
        } catch (error) {
          console.error('Error fetching chapters:', error);
        }
      },
      async addChapter() {
        if (this.newChapterName.trim() !== '') {
          const newChapter = {
            name: this.newChapterName,
            description: '',
            gameId: this.gameId, // Replace with actual game ID
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
      async fetchCharactersForGame() { 
        try {
          const response = await axios.get(`/api/characters`, {
            params: {
              gameId: this.gameId // Include gameId as a query parameter
            }
          });
          this.availableCharacters = response.data
        } catch (error) {
          console.error('Error fetching characters:', error);
        }
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
        this.editableCharacter = { ...character };
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
    async mounted() {
      this.gameId = this.$route.params.gameId;
      console.log("Game ID from route:", this.gameId);
      await this.fetchChapters();
      await this.fetchCharactersForGame();

    }
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
    background: url('@/assets/background.png') no-repeat center center;
    background-size: cover;
  }


  .main-content {
    flex-grow: 1;

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

  }

  .character-sections {
    display: flex;
    gap: 20px; /* Space between the two sections */
    width: 870px;
    margin: 0 auto;
  }



  .character-select, .character-list {
    flex: 1;
    background-color: rgba(62, 64, 74, 0.9);
    padding: 20px;
    border-radius: 8px;
    overflow-y: auto; /* 启用垂直滚动 */
    max-height: 400px; /* 设置固定的最大高度 */
    scrollbar-color: #989898 #2c2c2c; /* 滑块颜色 滚动条轨道颜色 */
    scrollbar-width: thin; /* 可以为 auto, thin 或 none */

  }



  .character-box:hover {
    background-color: #666;
  }

  .character-list .character-card {
    background-color: #25272f;
    border-radius: 7px;
    padding: 10px;
    margin-bottom: 10px;
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

  .story-box textarea {
    width: 850px;
    height: 300px;
    background-color: rgba(62, 64, 74, 0.9);
    color: #fff;
    border: none;
    border-radius: 5px;
    padding: 10px;
    resize: none;
  }

  .story-box textarea::placeholder {
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
    background-color: white;
    padding: 20px;
    border-radius: 8px;
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
  }

  .instructions {
    color: #333;
  }
  .add-new-character {
    background-color: rgba(62, 64, 74, 0.9); /* Same background as the rest */
    padding: 30px; /* Add padding for spacing */
    border-radius: 12px; /* Rounded corners */
    box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2); /* Subtle shadow for depth */
    max-width: 400px; /* Limit the width */
    margin: 40px auto; /* Center the form */
  }

  .form-group {
    margin-bottom: 20px; /* Space between form elements */
  }

  label {
    display: block; /* Block label for alignment */
    font-weight: bold; /* Emphasize the label */
    color: #E1E1E1; /* Light color for contrast */
    margin-bottom: 8px; /* Add space below label */
  }

  input {
    width: 90%; /* Full-width inputs */
    padding: 12px; /* Comfortable padding */
    border: 2px solid #555; /* Solid border */
    border-radius: 8px; /* Rounded corners for inputs */
    background-color: #25272f; /* Dark background for inputs */
    color: #e1e1e1; /* Light text color */
    font-size: 16px; /* Comfortable text size */
    box-shadow: inset 0 2px 4px rgba(0, 0, 0, 0.2); /* Slight inner shadow */
  }

  input:focus {
    outline: none; /* Remove default focus */
    border-color: #1f67b4; /* Blue border on focus */
    box-shadow: 0 0 6px rgba(31, 103, 180, 0.5); /* Focus glow */
  }

  .add-character-btn {
    background-color: #1f67b4; /* Button color */
    color: #fff; /* White text */
    padding: 10px 20px; /* Padding for the button */
    border: none; /* Remove border */
    border-radius: 5px; /* Rounded button corners */
    cursor: pointer; /* Pointer cursor */
    font-size: 16px; /* Font size */
    font-weight: bold; /* Bold text */
    width: 100%; /* Full width button */
    transition: background-color 0.3s ease; /* Smooth background transition */
  }

  .add-character-btn:hover {
    background-color: #0056b3; /* Darker blue on hover */
  }

  .add-character-btn:focus {
    outline: none;
    box-shadow: 0 0 5px rgba(0, 123, 255, 0.5); /* Focus glow */
  }

  .character-select, .character-list {
    /* Keep the same styles as the existing ones for consistency */
    flex: 1;
    background-color: rgba(62, 64, 74, 0.9);
    padding: 20px;
    border-radius: 8px;
    overflow-y: auto;
    max-height: 400px;
    scrollbar-color: #989898 #2c2c2c;
    scrollbar-width: thin;
  }

  .character-select .no-characters, .character-list .no-characters {
    color: grey;
    font-style: italic;
  }

  .character-box:hover {
    background-color: #666;
  }

  .character-list .character-card {
    background-color: #25272f;
    border-radius: 7px;
    padding: 10px;
    margin-bottom: 10px;
  }

  .character-actions {
    display: flex;
    justify-content: flex-end;
    gap: 10px;
  }

  .story-box textarea {
    width: 850px;
    height: 300px;
    background-color: rgba(62, 64, 74, 0.9);
    color: #fff;
    border: none;
    border-radius: 5px;
    padding: 10px;
    resize: none;
  }

  .story-box textarea::placeholder {
    color: #bbb;
  }

  .add-character-btn {
    background-color: #1f67b4; /* Primary blue */
    color: #fff; /* White text */
    padding: 12px 20px; /* Padding for better click area */
    margin-top: 20px;
    border: none; /* No border */
    border-radius: 8px; /* Rounded corners */
    cursor: pointer; /* Pointer cursor */
    font-size: 18px; /* Larger font */
    font-weight: bold; /* Bold text */
    text-align: center; /* Center the text */
    transition: background-color 0.3s ease, transform 0.3s ease; /* Smooth transitions */
    width: 100%; /* Full-width button */
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2); /* Subtle shadow */
  }

  .add-character-btn:hover {
    background-color: #0056b3; /* Darker blue on hover */
    transform: translateY(-2px); /* Lift the button on hover */
  }

  .add-character-btn:focus {
    outline: none; /* Remove the default focus outline */
    box-shadow: 0 0 8px rgba(31, 103, 180, 0.8); /* Focus glow */
  }

  .add-character-btn:active {
    background-color: #004080; /* Darker on click */
    transform: translateY(0); /* Press down */
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2); /* Decrease shadow */
  }

  

  /* width */




  </style>
  
  