<script setup lang="ts">
import { useI18n } from "vue-i18n";
import type { Pageable } from "~/types/http";

const { t } = useI18n();

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
