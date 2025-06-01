<template>
  <div class="flex flex-col min-h-screen bg-white">
    <div class="flex-none p-6 pb-0">
      <NuxtLink to="/welcome">
        <button aria-label="Voltar" class="text-gray-900 text-3xl">
          &larr;
        </button>
      </NuxtLink>
    </div>

    <div class="bg-gray-800 w-full rounded-t-3xl shadow-lg
                h-[70vh] absolute bottom-0 flex flex-col justify-between p-8 pt-20">
      <div class="w-full max-w-sm mx-auto">
        <h1 class="text-3xl md:text-4xl lg:text-5xl font-bold text-white mb-8 text-center leading-tight">
          Criar uma conta
        </h1>

        <form class="space-y-6">
          <div>
            <label for="name" class="block text-sm font-medium text-gray-300 mb-1">NOME</label>
            <input
              type="text"
              id="name"
              v-model="formData.name"
              placeholder="Jira Martins"
              class="w-full px-4 py-3 bg-gray-700 text-white rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 border border-transparent placeholder-gray-400"
              required
            />
          </div>

          <div>
            <label for="email" class="block text-sm font-medium text-gray-300 mb-1">EMAIL</label>
            <input
              type="email"
              id="email"
              v-model="formData.email"
              placeholder="someone@example.com"
              class="w-full px-4 py-3 bg-gray-700 text-white rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 border border-transparent placeholder-gray-400"
              required
            />
          </div>

          <div>
            <label for="password" class="block text-sm font-medium text-gray-300 mb-1">SENHA</label>
            <input
              type="password"
              id="password"
              v-model="formData.password"
              placeholder="********"
              class="w-full px-4 py-3 bg-gray-700 text-white rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 border border-transparent placeholder-gray-400"
              required
            />
          </div>

          <p class="text-sm text-center text-gray-400 mt-4">
            Já tem cadastro?
            <NuxtLink to="/login" class="text-blue-500 hover:underline">
              Acesse aqui
            </NuxtLink>
          </p>

          <button
            type="submit"
            class="w-full py-4 px-6 bg-blue-700 text-white font-semibold rounded-lg shadow-md
                   hover:bg-blue-800 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-opacity-75
                   transition duration-300 ease-in-out text-lg"
          >
            Criar
          </button>
        </form>
      </div>

      <p class="text-center text-gray-500 text-xs mt-8">
        ASTIKOS <br> 2025
      </p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';

definePageMeta({
  layout: "guest",
});

const formData = ref({
  name: '',
  email: '',
  password: ''
});

</script>

<style scoped>
</style>