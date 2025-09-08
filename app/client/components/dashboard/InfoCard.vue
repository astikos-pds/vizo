<script setup lang="ts">
defineProps<{
  value: string | number
  label: string
}>()
</script>

<template>
  <div
    class="m-8 overflow-hidden rounded-md border border-gray-200 px-5 pt-5 dark:border-gray-800 min-h-50 bg-gradient-to-tr from-green-600 to-green-300"
  >
    <div class="flex flex-col justify-center items-center h-full py-8">
      <h1 class="text-white font-bold text-4xl mb-2">
        {{ value }}
      </h1>
      <h1 class="text-white font-bold text-lg">
        {{ label }}
      </h1>
    </div>
  </div>
</template>