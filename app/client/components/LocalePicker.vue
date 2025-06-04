<script lang="ts" setup>
import type { SelectMenuItem } from "@nuxt/ui";
import * as locales from "@nuxt/ui/locale";

const { t, locale, setLocale } = useI18n();

const languageItems = ref([
  { id: locales.en.code, label: locales.en.name, icon: "i-flagpack-us" },
  { id: locales.pt_br.code, label: locales.pt_br.name, icon: "i-flagpack-br" },
] satisfies SelectMenuItem[]);

const language = ref<typeof locale.value>(locale.value);
const icon = computed(
  () => languageItems.value.find((item) => item.id === locale.value)?.icon
);

watch(language, (newLanguage) => {
  setLocale(newLanguage);
});

console.log(icon);
</script>

<template>
  <div class="w-48">
    <USelectMenu
      v-model="language"
      value-key="id"
      :items="languageItems"
      :icon="icon"
      :search-input="{
        placeholder: t('settings.searchPlaceholder'),
        icon: 'i-lucide-search',
      }"
      class="w-full"
    />
  </div>
</template>
