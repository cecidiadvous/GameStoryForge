<template>
  <div class="login-container">
    <h2>Login</h2>
    <form @submit.prevent="login">
      <div class="form-group">
        <label for="username">Username:</label>
        <input type="text" v-model="username" id="username" required />
      </div>
      <div class="form-group">
        <label for="password">Password:</label>
        <input type="password" v-model="password" id="password" required />
      </div>
      <button type="submit">Log in</button>
    </form>
    <div class="register-link">
      <!-- 注册链接 -->
      Don't have an account? <router-link to="/register">Register here</router-link>
    </div>
    <div v-if="error" class="error-message">
      {{ error }}
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      username: '',
      password: '',
      error: null,
    };
  },
  methods: {
    async login() {
      this.error = null;
      try {
        const response = await axios.post('/api/auth/login', {
          username: this.username,
          password: this.password,
        });
        alert('Login successful! Welcome, ' + response.data.username);

        // 登录成功后，将用户信息存储在 localStorage 中
        localStorage.setItem('user', JSON.stringify(response.data));

        this.$router.push('/home'); // 登录成功后跳转到主页
      } catch (err) {
        if (err.response && err.response.status === 401) {
          this.error = 'Invalid username or password';
        } else {
          this.error = 'An error occurred. Please try again later.';
        }
      }
    },
  },
};
</script>

<style scoped>
.login-container {
  max-width: 500px;
  aspect-ratio: 3.5 / 4;
  margin: 0 auto;
  padding: 1rem;
  border: 1px solid #ccc;
  border-radius: 5px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  background: rgba(255, 255, 255, 0.9); /* 使登录表单有轻微的背景 */
  color: #333;
}


.form-group {
  margin-bottom: 1rem;
}

.form-group label {
  display: block;
  margin-bottom: 0.5rem;
}

.form-group input {
  width: 100%;
  padding: 0.5rem;
  box-sizing: border-box;
  border-radius: 5px;
  border: 1px solid #ccc;
}

button {
  width: 100%;
  padding: 0.75rem;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

button:hover {
  background-color: #0056b3;
}

.register-link {
  margin-top: 1rem;
  text-align: center;
}

.register-link a {
  color: #007bff;
  text-decoration: none;
}

.register-link a:hover {
  text-decoration: underline;
}

.error-message {
  color: red;
  margin-top: 1rem;
}
</style>
