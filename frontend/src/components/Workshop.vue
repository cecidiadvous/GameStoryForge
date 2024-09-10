<template>
    <div class="workshop-container">
      <!-- Top Navigation Bar -->
      <nav class="top-nav">
        <div class="nav-left">
          <span class="logo">GameStoryForge</span>
        </div>
        <div class="nav-right">
          <ul class="nav-list">
            <li><button @click="handleDownload">Download</button></li>
            <li><router-link to="/home">Home</router-link></li>
            <li><router-link to="/dashboard">Dashboard</router-link></li>
            <li><button @click="logout">Sign Out</button></li>
          </ul>
        </div>
      </nav>
      <aside class="sidebar">
        <h3>{{ projectTitle }}</h3>
        <ul>
          <li v-for="chapter in chapters" :key="chapter.chapterId" @click="selectChapter(chapter)">
            {{ chapter.name }}
          </li>
        </ul>
        <input v-model="newChapterName" placeholder="Add chapter name" />
        <button @click="addChapter">Add Chapter</button>
      </aside>
  
      <main class="main-content">
        <div class="top-bar">
          <header class="chapter-header">
            <h2>{{ selectedChapter.name }}</h2>
            <span>{{ selectedChapter.subtitle }}</span>
          </header>
          <button @click="toggleInstructions" class="instructions-button">
            {{ showInstructions ? 'Hide Instructions' : 'Show Instructions' }}
          </button>
        </div>
  
        <section v-if="showInstructions" class="instructions">
          <h3>Instruction</h3>
          <p>1. Select a Chapter: Use the left sidebar to choose or add a chapter.</p>
          <p>2. Select Characters: Drag characters from "Character Select" to the "Character List" to include them in the story.</p>
          <p>3. Customize Characters: Click on a character in the "Character List".</p>
          <p>4. Write the Draft: Use the "Describe the style and story of the game" box to draft.</p>
          <p>5. Generate Story Dialogue.</p>
          <p>6. Download: download the final content.</p>
        </section>
  
        <section class="character-select">
          <h3>Character Select</h3>
          <div class="character-box" v-for="character in availableCharacters" :key="character.characterId" @click="selectCharacter(character)">
            <p>{{ character.name }}</p>
          </div>
        </section>
  
        <section class="character-list">
          <h3>Character List</h3>
          <div v-if="selectedCharacters.length === 0" class="no-characters">
            There is no character selected.
          </div>
          <div v-else v-for="character in selectedCharacters" :key="character.characterId" class="character-card">
            <p><strong>Name:</strong> {{ character.name }}</p>
            <p><strong>Role:</strong> {{ character.role }}</p>
            <p><strong>Ability:</strong> {{ character.ability }}</p>
            <div class="character-actions">
              <button @click="openEditModal(character)">✏️ Edit</button>
              <button @click="removeCharacter(character.characterId)">🗑️ Delete</button>
            </div>
          </div>
        </section>
  
        <section class="story-box">
          <h3>Describe the style and story of the game</h3>
          <textarea v-model="storyDescription" placeholder="Describe the style and story of the game"></textarea>
        </section>
  
        <!-- Modal for Editing Character -->
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
      </main>
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
    methods: {
      async logout() {
        localStorage.removeItem('user');
        this.$router.push('/login');
      },
      async fetchChapters() {
        try {
          const response = await axios.get('/api/chapters');
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
            gameId: 1, // Replace with actual game ID
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
        this.fetchCharactersForChapter(chapter.chapterId);
      },
      async fetchCharactersForChapter(chapterId) {
        try {
          const response = await axios.get(`/api/chapters/${chapterId}/characters`);
          this.selectedCharacters = response.data;
        } catch (error) {
          console.error('Error fetching characters:', error);
        }
      },
      selectCharacter(character) {
        if (!this.selectedCharacters.find(c => c.characterId === character.characterId)) {
          this.selectedCharacters.push(character);
        }
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
        const index = this.selectedCharacters.findIndex(c => c.characterId === this.editableCharacter.characterId);
        if (index !== -1) {
          try {
            const response = await axios.put(`/api/characters/${this.editableCharacter.characterId}`, this.editableCharacter);
            this.selectedCharacters[index] = response.data;
            this.closeEditModal();
          } catch (error) {
            console.error('Error updating character:', error);
          }
        }
      },
      async removeCharacter(id) {
        try {
          await axios.delete(`/api/characters/${id}`);
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
      await this.fetchChapters();

    }
  };
  </script>
  
  
  <style scoped>
  .workshop-container {
    display: flex;
    height: 100vh;
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
}

.nav-left .logo {
  font-size: 20px;
  font-weight: bold;
  color: white;
  text-transform: uppercase;
}

.nav-right .nav-list {
  list-style: none;
  margin: 0;
  padding: 0;
  display: flex;
}

.nav-right .nav-list li {
  margin-left: 20px;
}

.nav-right .nav-list li a,
.nav-right .nav-list li button {
  color: white;
  background: none;
  border: none;
  cursor: pointer;
  text-decoration: none;
  font-size: 16px;
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
    background-color: #444;
    padding: 20px;
    padding-top: 70px;
    display: flex;
    flex-direction: column;
    gap: 10px;
  }
  
  .sidebar h3 {
    margin-bottom: 10px;
  }
  
  .sidebar ul {
    list-style: none;
    padding: 0;
  }
  
  .sidebar ul li {
    margin: 10px 0;
    cursor: pointer;
  }
  
  .sidebar input {
    margin-top: 10px;
    padding: 5px;
    background-color: #555;
    color: #fff;
    border: none;
    border-radius: 5px;
  }
  
  .main-content {
    flex-grow: 1;
    padding: 20px;
    padding-top: 70px;
    background: url('@/assets/workshop_initial_background.png') no-repeat center center;
    background-size: cover;
    position: relative;
  }
  
  .top-bar {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
  
  .instructions-button {
    padding: 10px 20px;
    background-color: #1f67b4;
    color: white;
    border: none;
    border-radius: 5px;
    cursor: pointer;
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
  
  .character-select .character-box {
    background-color: #555;
    border-radius: 5px;
    padding: 10px;
    margin-bottom: 10px;
    cursor: pointer;
  }
  
  .character-box:hover {
    background-color: #666;
  }
  
  .character-list .character-card {
    background-color: #555;
    border-radius: 5px;
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
    width: 100%;
    height: 100px;
    background-color: #444;
    color: #fff;
    border: none;
    border-radius: 5px;
    padding: 10px;
    resize: none;
  }
  
  .story-box textarea::placeholder {
    color: #bbb;
  }
  </style>
  