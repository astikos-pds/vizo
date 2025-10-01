<script lang="ts" setup>
import { useI18n } from "vue-i18n";
import * as locales from "@nuxt/ui/locale";

const { locale } = useI18n();

const localesMapping = {
  en: locales.en,
  "pt-BR": locales.pt_br,
};

watchEffect(() => {
  useHead({
    htmlAttrs: {
      lang: locale.value,
    },
  });
});
</script>

<template>
  <ClientOnly>
    <NuxtPwaManifest />
  </ClientOnly>

  <UApp :locale="localesMapping[locale]">
    <NuxtLoadingIndicator color="primary" :height="5" :throttle="0" />

    <NuxtLayout>
      <NuxtPage />
    </NuxtLayout>
  </UApp>
</template>
