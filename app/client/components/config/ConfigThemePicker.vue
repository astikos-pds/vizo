<script lang="ts" setup>
import { useDark } from "@vueuse/core";

const isDark = useDark();

function toggleTheme() {
  isDark.value = !isDark.value;
}
</script>

<template>
  <UButton
    :icon="isDark ? 'i-lucide-sun' : 'i-lucide-moon'"
    size="xl"
    color="neutral"
    variant="outline"
    class="cursor-pointer"
    @click="toggleTheme"
  />
</template>
