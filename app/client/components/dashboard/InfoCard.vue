<script setup lang="ts">
defineProps<{
  value: string | number
  label: string
  desc: string
  color?: string
}>()
</script>

<template>
  <div
    class="m-8 overflow-hidden rounded-3xl px-5 pt-5 dark:border-gray-800 min-h-50"
    :class="color"
  >
    <div class="flex flex-col justify-center items-center h-full py-8">
      <h1 class="text-white font-bold text-4xl mb-2">
        {{ value }}
      </h1>
      <h1 class="text-white font-bold text-lg">
        {{ label }}
      </h1>
      <h1 class="text-white font-bold text-xs">
        {{ desc }}
      </h1>
    </div>
  </div>
</template>