<script lang="ts" setup>
const { collapsed } = defineProps<{
  collapsed: boolean;
}>();
</script>

<template>
  <header class="h-20 flex justify-between items-center">
    <div class="w-full flex flex-row gap-1 justify-center items-center">
      <img
        src="/public/favicon.svg"
        alt="Logo"
        :class="collapsed ? 'size-8' : 'size-12'"
      />
      <h1 v-if="!collapsed" class="xl:block text-3xl font-semibold uppercase">
        Vizo
      </h1>
    </div>
  </header>
</template>
