<script lang="ts" setup>
import { useI18n } from "vue-i18n";
import * as locales from "@nuxt/ui/locale";

const { locale } = useI18n();

const localesMapping = {
  en: locales.en,
  "pt-BR": locales.pt_br,
};
</script>

<template>
  <NuxtLoadingIndicator color="primary" />
  <UApp :locale="localesMapping[locale]">
    <div class="max-w-[2000px] mx-auto min-h-screen">
      <NuxtLayout>
        <NuxtPage />
      </NuxtLayout>
    </div>
  </UApp>
</template>
