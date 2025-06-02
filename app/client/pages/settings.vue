<script lang="ts" setup>
import type { FormSubmitEvent } from "@nuxt/ui";
import * as locales from "@nuxt/ui/locale";
import { z } from "zod";

const { t, locale, setLocale } = useI18n();

const languageItems = ref([
  { id: locales.en.code, label: locales.en.name, icon: "i-flagpack-us" },
  { id: locales.pt_br.code, label: locales.pt_br.name, icon: "i-flagpack-br" },
]);

const settingsSchema = z.object({
  language: z.custom<typeof locale.value>(),
});

type SettingsSchema = z.output<typeof settingsSchema>;

const form = reactive<SettingsSchema>({
  language: locale.value,
});

const onSubmit = (event: FormSubmitEvent<SettingsSchema>) => {
  setLocale(event.data.language);
};
</script>

<template>
  <section
    class="w-full flex items-center flex-col gap-5 py-20 overflow-y-scroll border-r border-neutral-200"
  >
    <h1 class="text-3xl font-semibold text-neutral-900">
      {{ t("settings.title") }}
    </h1>

    <UForm
      :state="form"
      :schema="settingsSchema"
      @submit="onSubmit"
      class="min-w-[35rem] flex flex-col items-center gap-5"
    >
      <UFormField
        :label="t('settings.languageLabel')"
        name="language"
        size="xl"
        class="w-full"
        ><USelectMenu
          v-model="form.language"
          value-key="id"
          :items="languageItems"
          :search-input="{
            placeholder: t('settings.searchPlaceholder'),
            icon: 'i-lucide-search',
          }"
          class="w-full"
      /></UFormField>

      <UButton type="submit" size="xl" class="cursor-pointer">{{
        t("settings.saveButton")
      }}</UButton>
    </UForm>
  </section>
</template>
