<template>
  <div class="flex flex-col min-h-screen">
    <div class="flex-grow flex flex-col items-center justify-center bg-white p-6">
      <h1 class="text-6xl md:text-7xl lg:text-8xl font-black text-blue-700 leading-none">
        VIZO
      </h1>
      <p class="text-sm md:text-base lg:text-lg font-medium text-blue-600 mt-2 tracking-widest">
        ASTIKOS
      </p>
    </div>

    <div class="flex-none bg-gray-800 p-8 flex flex-col items-center justify-center rounded-t-3xl md:rounded-t-none">
      <div class="w-full max-w-sm flex flex-col space-y-4">
        <button
          @click="handleAccessClick"
          class="w-full py-4 px-6 bg-blue-700 text-white font-semibold rounded-lg shadow-md
                 hover:bg-blue-800 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-opacity-75
                 transition duration-300 ease-in-out text-lg"
        >
          Acessar
        </button>

        <button
          @click="handleCreateAccountClick"
          class="w-full py-4 px-6 border-2 border-blue-700 text-white font-semibold rounded-lg shadow-md
                 hover:bg-blue-700 hover:text-white focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-opacity-75
                 transition duration-300 ease-in-out text-lg"
        >
          Criar conta
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { navigateTo } from '#app';

definePageMeta({
  layout: "guest",
});

const handleAccessClick = () => {
  navigateTo('/login');
};

const handleCreateAccountClick = () => {
  navigateTo('/register');
};

</script>

<style scoped>
</style>