<template>
  <div class="dashboard-container">
    <nav class="navbar">
      <div class="navbar-left">
        <h2>
          <span class="gamestory">GameStory</span>
          <span class="forge">Forge</span>
        </h2>
      </div>
      <div class="navbar-right">
        <span @click="logout" class="logout-text">Log Out</span>
      </div>
    </nav>
    <h1 class="dashboard-title">Dashboard</h1>

    <div class="games-list">
      <div class="game-card" v-for="(game, index) in games" :key="game.gameId">
        <img :src="game.image" alt="Game image" @click="goToWarkshop(game.gameId)" />

        <div class="game-info">
          <h3>{{ game.name }}</h3>
          <button class="edit-button" @click="editGame(index)">
            <img src="@/assets/edit_button.svg" alt="Edit" class="edit-icon">
          </button>

          <button class="delete-button" @click="deleteGame(index)">
            <img src="@/assets/delete_icon.svg" alt="Delete" class="delete-icon">
          </button>
        </div>
      </div>
      <div class="add-card" @click="showForm = true">
        <span>+</span>
      </div>
    </div>


    <div v-if="showForm" class="popup-form">
      <div class="form-container">
        <h2 v-if="editingIndex === -1">New Game</h2>
        <h2 v-else>Edit Game</h2>
        <input v-model="newGame.name" placeholder="Game Name" />
        <textarea v-model="newGame.description" placeholder="Game Description"></textarea>
        <input type="file" @change="handleFileUpload" />
        <button v-if="editingIndex === -1" @click="addGame">+ Create</button>
        <button v-else @click="updateGame">Update</button>
        <button @click="cancelEdit">Cancel</button>
      </div>
    </div>
  </div>
</template>


<script>

import axios from 'axios';
import backgroundImage from '@/assets/background.png';

export default {
  name: 'Dashboard',
  data() {
    return {
      showForm: false,
      editingIndex: -1,
      defaultImage: backgroundImage,
      newGame: {
        name: '',
        gameId: '',
        description: '',
        image: '',
      },
      games: [],
    };
  },
  async mounted() {
    await this.fetchGames();
  },
  methods: {
    goToWarkshop(gameId) {
      this.$router.push(`/workshop/${gameId}`);
    },
    // 获取游戏列表
    async fetchGames() {
      const user = JSON.parse(localStorage.getItem('user'));
      if (user) {
        try {
          const response = await axios.get(`/api/dashboard/games?username=${user.username}`);
          this.games = response.data.map(game => {
            // 如果游戏有图片，确保完整的图片URL
            game.image = game.image
                ? encodeURI(`http://localhost:8080${game.image}`)
                : this.defaultImage;
            return game;
          });
          console.log('myGames:', this.games);
        } catch (error) {
          console.error('Error fetching games:', error);
        }
      } else {
        this.$router.push('/auth');
      }
    },

    // 添加游戏
    async addGame() {
      const user = JSON.parse(localStorage.getItem('user'));
      const formData = new FormData();

      formData.append('name', this.newGame.name);
      formData.append('description', this.newGame.description);
      formData.append('image', this.newGame.image);
      formData.append('username', user.username);

      if (this.newGame.name && this.newGame.description) {
        try {
          // 发送 POST 请求到后端，包括 FormData 数据
          const response = await axios.post('/api/dashboard/games', formData, {
            headers: {
              'Content-Type': 'multipart/form-data'
            }
          });


          response.data.image = response.data.image
              ? `http://localhost:8080${response.data.image}`
              : this.defaultImage;

          this.games.push(response.data);
          this.showForm = false;
          this.resetForm();
        } catch (error) {
          console.error('Error adding game:', error);
        }
      }
    },
    editGame(index) {
      this.editingIndex = index;
      this.newGame = { ...this.games[index] };
      this.showForm = true;
    },
    async updateGame() {
      const user = JSON.parse(localStorage.getItem('user'));
      if (this.editingIndex !== -1) {
        try {
          const formData = new FormData();
          formData.append('name', this.newGame.name);
          formData.append('description', this.newGame.description);
          if (this.newGame.image) {
            formData.append('image', this.newGame.image);
          }
          formData.append('username', user.username);

          const response = await axios.put(`/api/dashboard/games/${this.games[this.editingIndex].gameId}`, formData, {
            headers: {
              'Content-Type': 'multipart/form-data'
            }
          });

          this.games[this.editingIndex] = response.data;

          this.games[this.editingIndex].image = response.data.image
              ? `http://localhost:8080${response.data.image}?t=${new Date().getTime()}`
              : this.defaultImage;

          this.showForm = false;
          this.resetForm();
        } catch (error) {
          console.error('Error updating game:', error);
        }
      }
    },

    async deleteGame(index) {
      try {
        const gameId = this.games[index].gameId;
        await axios.delete(`/api/dashboard/games/${gameId}`);
        this.games.splice(index, 1);
      } catch (error) {
        console.error('Error deleting game:', error);
      }
    },
    cancelEdit() {
      this.showForm = false;
      this.resetForm();
    },
    resetForm() {
      this.newGame = {
        name: '',
        description: '',
        image: '',
      };
      this.editingIndex = -1;
    },
    logout() {
      localStorage.removeItem('user');
      this.$router.push('/auth');
    },
    handleFileUpload(event) {
      const file = event.target.files[0];
      if (file) {
        this.newGame.image = file;
      }
    },
  },
};

</script>


<style scoped>
.dashboard-container {
  padding: 0px;
  text-align: center;
  color: #ffffff;
  background-color: #202123;
  min-height: 100vh;
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

.navbar {
  width: 100%;
  height: 40px;
  background-color: rgba(0, 0, 0, 0.7);
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
  box-sizing: border-box;
  position: absolute;
  top: 0;
  left: 0;
  color: white;
}

.navbar-right .logout-text {
  color: #E1E1E1;
  cursor: pointer;
  font-size: 16px;
  font-weight: normal;
  font-family: Lora, serif;
}

.navbar-right .logout-text:hover {
  text-decoration: underline;
}



.navbar-left h2 {
  margin: 0;
  font-size: 24px;
  white-space: nowrap;
}

.dashboard-title {
  font-size: 26px;
  font-weight: normal;
  color: #ffffff;
  text-align: left;
  margin: 0px ;
  margin-left: 50px;
  font-family: 'Lora', serif;
  padding-top: 60px;
}


.games-list {
  display: flex;

  gap: 35px;
  padding: 20px;
  padding-top: 20px;
  flex-wrap: wrap;
  padding-left: 50px;
}

.game-card {
  width: 250px;
  height: 250px;
  background-color: #3f3f48;
  padding: 0px;
  border-radius: 12px;
  text-align: center;
  box-sizing: border-box;
  color: #D2D2D2;

}

.game-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.3);
}

.game-card img {
  width: 100%;
  height: 80%;
  border-radius: 12px 12px 0px 0px;
  object-fit: cover;
  cursor: pointer;
}

.game-info {
  position: relative;
  padding: 10px;
  font-size: 16px;
  font-weight: normal;
  font-family: Lora, serif;
}

.game-info h3 {
  font-size: 18px;
  font-weight: normal;
  font-family: Lora, serif;
  margin-top: -1px;
  display: inline-block;
}

.edit-button {
  position: absolute;
  width: 38px;
  bottom: 15px;
  right: 205px;
  padding: 5px;
  background-color: rgba(63, 125, 101, 0);
  border: none;
  cursor: pointer;
}

.edit-button:hover {
  transform: scale(1.1);
}

.delete-button {
  position: absolute;
  width: 38px;
  bottom: 15px;
  right: 5px;
  padding: 5px;
  background-color: rgba(63, 125, 101, 0);
  border: none;
  cursor: pointer;
}

.delete-button:hover {
  transform: scale(1.1);
}

.edit-icon, .delete-icon {
  width: 100%;
  height: auto;
}


.add-card {
  width: 250px;
  height: 250px;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #3f3f48;
  border-radius: 12px;
  font-size: 36px;
  color: #ffffff;
  cursor: pointer;
}

.add-card span {
  font-size: 68px;
  color: #D2D2D2;
}

.add-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.3);
}

.popup-form {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.7);
  display: flex;
  justify-content: center;
  align-items: center;
  font-family: 'Lora', serif;
  color: #D2D2D2;
  font-weight: normal;
}

.form-container {
  background-color: #3E404A;
  padding: 20px;
  border-radius: 8px;
  width: 400px;
  height: 350px;
  text-align: center;
}

.form-container h2 {
  margin-top: 0px;
  font-weight: normal;
}

.form-container input,
.form-container textarea {
  width: 90%;
  padding: 10px;
  margin-bottom: 10px;
  border: 2px solid #A4A4A4;
  border-radius: 8px;
  background-color: #3E404A;
  font-family: 'Lora', serif;
  color: #D2D2D2;
}

.form-container textarea {
  height: 100px;
}

.form-container button {
  padding: 10px 20px;
  margin: 5px;
  background-color: #3f7d65;
  color: #f2f4f4;
  border: none;
  border-radius: 8px;
  cursor: pointer;
}

.form-container button:hover {
  background-color: #4da081;
}


</style>