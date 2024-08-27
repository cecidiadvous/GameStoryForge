<template>
  <div class="home-container">
    <h1>Hello World</h1>
    <button @click="logout">Log Out</button>
  </div>
</template>

<script>
export default {
  name: 'Home',
  methods: {
    logout() {
      // 清除本地存储的用户信息
      localStorage.removeItem('user');
      // 重定向到登录页面
      this.$router.push('/login');
    }
  }
};
</script>

<style scoped>
/* 添加一些样式 */
.home-container {
  text-align: center;
  margin-top: 20px;
}

button {
  margin-top: 20px;
  padding: 10px 20px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

button:hover {
  background-color: #0056b3;
}
</style>

