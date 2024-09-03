<template>
  <div class="background-container">
    <h1>GameStoryForge</h1>
    <div class="auth-page">
      <h2>{{ showLogin ? 'Login' : 'Register' }}</h2>
      <form @submit.prevent="handleSubmit">
        <div class="form-group">
          <label for="username">Username:</label>
          <input type="text" v-model="username" id="username" required />
        </div>
        <div class="form-group">
          <label for="password">Password:</label>
          <input type="password" v-model="password" id="password" required />
        </div>
        <button type="submit">{{ showLogin ? 'Log in' : 'Register' }}</button>
      </form>
      <div class="toggle-link">
        <p v-if="showLogin">
          Don't have an account?<br>
          <a href="#" @click.prevent="toggleForm">Register here</a>
        </p>
        <p v-else>
          Already have an account?<br>
          <a href="#" @click.prevent="toggleForm">Login here</a>
        </p>
      </div>
      <div v-if="message" :class="{'error-message': isError, 'success-message': !isError}">
        {{ message }}
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      showLogin: true, // 控制显示登录还是注册组件
      username: '',
      password: '',
      message: '',
      isError: false,
    };
  },
  methods: {
    toggleForm() {
      this.showLogin = !this.showLogin;
      this.message = '';
      this.isError = false;
    },
    async handleSubmit() {
      this.message = '';
      if (this.showLogin) {
        await this.login();
      } else {
        await this.register();
      }
    },
    async login() {
      try {
        const response = await axios.post('/api/auth/login', {
          username: this.username,
          password: this.password,
        });
        // alert('Login successful! Welcome, ' + response.data.username);
        localStorage.setItem('user', JSON.stringify(response.data));
        this.$router.push('/home'); // 登录成功后跳转到主页
      } catch (err) {
        this.handleError(err, 'Invalid username or password');
      }
    },
    async register() {
      try {
        const response = await axios.post('/api/auth/register', {
          username: this.username,
          password: this.password,
        });
        alert('Registration successful!');
        this.message = 'Registration successful!';
        this.isError = false;
        this.toggleForm(); // 注册成功后自动切换到登录页面
      } catch (err) {
        this.handleError(err, 'Registration failed');
      }
    },
    handleError(err, defaultMsg) {
      this.message = err.response && err.response.data.message
          ? err.response.data.message
          : defaultMsg;
      this.isError = true;
    },
  },
};
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Lobster&display=swap');
@import url('https://fonts.googleapis.com/css2?family=Lora:wght@400;700&display=swap');

.background-container {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background-image: url('@/assets/background.png'); /* 替换为你的背景图片路径 */
  background-size: cover;
  background-position: center;
  flex-direction: column
}

.background-container h1 {

  margin-bottom: 7%;
  font-size: 68px;
  font-weight: normal;
  color: white;
  font-family: Lobster, cursive;

}

.auth-page {
  max-width: 300px;
  width: 100%;
  aspect-ratio: 3.5 / 4;
  margin-bottom: 12%;
  padding: 1rem;
  border: 1px solid #444444;
  border-radius: 12px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  background: rgba(33, 34, 36,0.88);
  color: #E9E9E9;
  font-family: Lora, serif;
}

.auth-page h2 {
  text-align: center;
  margin-top: 3%;
  font-size: 33px;
  font-weight: normal;
  line-height: 0.7;
  margin-bottom: 1rem;
}

.form-group {
  width: 100%;
  margin-bottom: 1rem;

}

.form-group label {
  width:70%;
  text-align: left;
  font-size: 19px;
  display: block;
  margin-bottom: 0.5rem;
  padding-left: 15px;
}

.form-group input {
  width:90%;
  padding: 0.7rem;
  box-sizing: border-box;
  border-radius: 12px;
  border: 3px solid rgba(131, 129, 129,0.88);
  background-color: rgba(33, 34, 36,0);
  color: white;
  font-size: 16px;
  font-family: Lora, serif;
  margin: 0 auto;
}

button {
  width: 90%;
  padding: 0.75rem;
  background-color: rgba(33, 34, 36,0);
  color: white;
  border-radius: 12px;
  border: 3px solid rgba(131, 129, 129,0.88);
  cursor: pointer;
  font-family: Lora, serif;
  font-size: 22px;
  line-height: 0.9;
}

button:hover {
  background-color: rgba(173, 170, 170, 0.4);
}

.toggle-link {
  margin-bottom: 10%;
  font-size: 17px;
  line-height: 1;
  padding-bottom: 10px;
}

.toggle-link a {
  color: #009dff;
  text-decoration: none;
  cursor: pointer;

}

.toggle-link a:hover {
  text-decoration: underline;
}

.error-message {
  color: #ea2626;
  margin-top: 1rem;
  font-size: 19px;
  font-weight: normal;
}

.success-message {
  color: #20e120;
  margin-top: 1rem;
  font-size: 19px;
}
</style>
