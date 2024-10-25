<template>
  <div class="home-container">
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
    <div class="content">
      <h1>The powerful AI platform for building</h1>
      <h2>Game storyline and dialogue</h2>
      <p>Build and scale AI experiences powered by GPT-4.0.</p>
      <button @click="startBuilding">Start building</button>

      <div class="section">
        <div class="section-content">
          <h2>Why important?</h2>
          <p>
            Game storylines and dialogues draw players deeply into the game,
            making them feel connected and invested in the characters and events,
            which enhances the overall gaming experience.
          </p>
        </div>
        <div class="section-image">
          <img src="@/assets/home.jpg" alt="Gaming experience">
        </div>
      </div>
    </div>

  </div>
</template>

<script>
export default {
  name: 'Home',
  methods: {
    logout() {
      localStorage.removeItem('user');
      this.$router.push('/auth');
    },
    startBuilding() {
      this.$router.push('/dashboard');
    }
  }
};
</script>


<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Lora:wght@400;700&display=swap');
@import url('https://fonts.googleapis.com/css2?family=Lobster&display=swap');

.home-container {
  position: relative;
  width: 100%;
  height: 100vh;
  background-color: #000000;
  background-size: cover;
  background-position: center;
  margin: 0;
  padding: 0;
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
  color: #ffffff;
}

.navbar-left h2 {
  margin: 0;
  font-size: 24px;
  white-space: nowrap;
}

.content {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  text-align: center;
  color: white;
  max-width: 2000px;
  min-width: 1000px;
  padding: 0px;

}

.content h1 {
  width: 100%;
  font-size: 300%;
  margin-bottom: 10px;
  font-family: 'Lora', serif;
  font-weight: normal;
}

.content h2 {
  font-size: 300%;
  margin-bottom: 20px;
  margin-top: 0px;
  font-family: 'Lora', serif;
  font-weight: normal;
}

.content p {
  font-size: 18px;
  margin-bottom: 30px;
  font-family: 'Lora', serif;
}

.content button {
  padding: 10px 20px;
  background-color: #ffffff;
  color: #000000;
  border: none;
  border-radius: 30px;
  font-size: 16px;
  cursor: pointer;
  font-family: 'Lora', serif;
}

.content button:hover {
  background-color: #dddddd;
}

.section {
  display: flex;
  width: 900px;
  justify-content: space-between;
  align-items: center;
  background-color: #171717;
  padding: 0px;
  margin-top: 4%;
  border-radius: 12px;
  margin-left: 5%;

}

.section-content {
  flex: 1;
  color: white;
  padding: 0px;
  margin: 0px;
  font-family: 'Lora', serif;
}

.section-content h2 {
  font-size: 24px;
  margin-bottom: 20px;
}

.section-content p {
  font-size: 18px;
  line-height: 1.6;
}

.section-image {
  flex: 1;
  margin-bottom: -5px;
  text-align: right;
  padding: 0px;
}

.section-image img {
  max-width: 100%;
  height: auto;
  border-radius: 12px;
}


@media (max-width: 768px) {
  .navbar {
    flex-direction: column;
    height: auto;
    padding: 10px;
  }

  .navbar-left,
  .navbar-right {
    width: 100%;
    text-align: center;
    margin-bottom: 10px;
  }

  .navbar-right button {
    width: 100%;
    padding: 10px;
  }
}
</style>
