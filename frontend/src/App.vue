<template>
  <div id="app">
    <h1>Authentication</h1>
    <button @click="showLogin = !showLogin">
      Switch to {{ showLogin ? 'Register' : 'Login' }}
    </button>
    <Login v-if="showLogin" />
    <Register v-else />
  </div>
</template>

<script>
import Login from './components/Login.vue';
import Register from './components/Register.vue';

export default {
  components: {
    Login,
    Register,
  },
  data() {
    return {
      showLogin: true, // 控制显示登录还是注册
    };
  },
};
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}

button {
  margin-bottom: 20px;
  padding: 10px 20px;
  background-color: #42b983;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

button:hover {
  background-color: #369971;
}
</style>


