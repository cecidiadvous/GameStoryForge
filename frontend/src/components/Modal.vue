<template>
    <div class="modal-overlay" v-if="visible">
      <div class="modal-content">
        <header class="modal-header">
          <slot name="header">
            <!-- Default header content -->
            <h2>Edit Character</h2>
          </slot>
          <button @click="closeModal" class="close-button">×</button>
        </header>
        <section class="modal-body">
          <slot name="body">
            <!-- Default body content -->
          </slot>
        </section>
        <footer class="modal-footer">
          <slot name="footer">
            <!-- Default footer content -->
            <button @click="closeModal">Close</button>
          </slot>
        </footer>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    props: {
      visible: {
        type: Boolean,
        default: false,
      },
    },
    methods: {
      closeModal() {
        this.$emit('close');
      },
    },
  };
  </script>
  
  <style scoped>
  .modal-overlay {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.5);
    display: flex;
    align-items: center;
    justify-content: center;
    z-index: 1000;
  }
  
  .modal-content {
    background: rgb(2, 2, 2);
    padding: 20px;
    border-radius: 8px;
    max-width: 500px;
    width: 100%;
  }
  
  .modal-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    border-bottom: 1px solid #ddd;
    padding-bottom: 10px;
  }
  
  .modal-body {
    padding: 20px 0;
  }
  
  .modal-footer {
    display: flex;
    justify-content: flex-end;
    padding-top: 10px;
    border-top: 1px solid #ddd;
  }
  
  .close-button {
    background: transparent;
    border: none;
    font-size: 20px;
    cursor: pointer;
  }
  </style>
  