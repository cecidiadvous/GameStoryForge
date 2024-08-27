<template>
  <div class="register-container">
    <h2>Register</h2>
    <form @submit.prevent="register">
      <div class="form-group">
        <label for="username">Username:</label>
        <input type="text" v-model="username" id="username" required />
      </div>
      <div class="form-group">
        <label for="password">Password:</label>
        <input type="password" v-model="password" id="password" required />
      </div>
      <div class="form-group">
        <label for="role">Role:</label>
        <input type="text" v-model="role" id="role" placeholder="Enter role (optional)" />
      </div>
      <button type="submit">Register</button>
    </form>
    <div v-if="message" :class="{'error-message': isError, 'success-message': !isError}">
      {{ message }}
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
      role: '',
      message: '',
      isError: false,
    };
  },
  methods: {
    async register() {
      this.message = '';
      try {
        const response = await axios.post('/api/auth/register', {
          username: this.username,
          password: this.password,
          role: this.role || 'USER', // 默认角色是 USER
        });
        this.message = response.data.message;
        this.isError = false;

        // 注册成功后跳转到登录页面
        this.$router.push('/login');
      } catch (err) {
        this.message = 'Registration failed: ' + (err.response && err.response.data.message ? err.response.data.message : err.message);
        this.isError = true;
      }
    },
  },
};
</script>

<style scoped>
.register-container {
  max-width: 400px;
  margin: 0 auto;
  padding: 1rem;
  border: 1px solid #ccc;
  border-radius: 5px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
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
}

button {
  width: 100%;
  padding: 0.75rem;
  background-color: #28a745;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

button:hover {
  background-color: #218838;
}

.error-message {
  color: red;
  margin-top: 1rem;
}

.success-message {
  color: green;
  margin-top: 1rem;
}
</style>
