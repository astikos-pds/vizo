<script lang="ts" setup>
import type { FormSubmitEvent } from "@nuxt/ui";
import * as locales from "@nuxt/ui/locale";
import { z } from "zod";

const { t, locale, setLocale } = useI18n();

useHead({
  title: t("head.settings.title"),
  meta: [{ name: "description", content: t("head.settings.description") }],
});

definePageMeta({
  middleware: ["auth"],
});

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
const icon = computed(
  () => languageItems.value.find((item) => item.id === form.language)?.icon
);

const toast = useToast();
const onSubmit = (event: FormSubmitEvent<SettingsSchema>) => {
  setLocale(event.data.language);

  toast.add({
    title: t("toast.success.title"),
    description: t("toast.success.description.settingsSaved"),
    color: "success",
  });
};
</script>

<template>
  <section
    class="w-full flex items-center flex-col gap-5 py-20 overflow-y-scroll border-r border-neutral-200 dark:border-neutral-800"
  >
    <h1 class="text-3xl font-semibold">
      {{ t("settings.title") }}
    </h1>

    <UForm
      :state="form"
      :schema="settingsSchema"
      @submit="onSubmit"
      class="min-w-[65%] md:min-w-[55%] lg:min-w-[35%] flex flex-col items-center gap-5"
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
          :icon="icon"
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
