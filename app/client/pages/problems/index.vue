<script setup lang="ts">
import type { Pageable } from "~/types/http";

definePageMeta({
  middleware: ["auth", "official", "department"],
});

const pagination = ref<Pageable>({
  page: 0,
  size: 20,
});
</script>

<template>
  <div class="lg:p-5">
    <ProblemTable v-model:pagination="pagination" />
  </div>
</template>
