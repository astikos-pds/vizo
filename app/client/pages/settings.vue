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

const themeItems = computed(() => [
  {
    label: t("configHeader.theme.system"),
    value: "system",
    icon: "i-lucide-monitor",
  },
  {
    label: t("configHeader.theme.light"),
    value: "light",
    icon: "i-lucide-sun",
  },
  {
    label: t("configHeader.theme.dark"),
    value: "dark",
    icon: "i-lucide-moon",
  },
]);

const colorMode = useColorMode();

const settingsSchema = z.object({
  language: z.custom<typeof locale.value>(),
  theme: z.custom<typeof colorMode.preference>(),
});

type SettingsSchema = z.output<typeof settingsSchema>;

const form = reactive<SettingsSchema>({
  language: locale.value,
  theme: colorMode.preference,
});

const languageIcon = computed(
  () => languageItems.value.find((item) => item.id === form.language)?.icon
);

const toast = useToast();
const onSubmit = async (event: FormSubmitEvent<SettingsSchema>) => {
  await setLocale(event.data.language);

  colorMode.preference = event.data.theme;

  toast.add({
    title: t("toast.success.title"),
    description: t("toast.success.description.settingsSaved"),
    color: "success",
  });
};
</script>

<template>
  <section
    class="size-full flex items-center flex-col gap-5 py-20 overflow-y-auto"
  >
    <h1 class="text-3xl font-semibold">
      {{ t("settings.title") }}
    </h1>

    <UForm
      :state="form"
      :schema="settingsSchema"
      @submit="onSubmit"
      class="w-[65%] md:w-110 lg:w-130 flex flex-col items-center gap-5"
    >
      <UFormField
        :label="t('settings.languageLabel')"
        name="language"
        class="w-full"
        ><USelectMenu
          v-model="form.language"
          value-key="id"
          :items="languageItems"
          :icon="languageIcon"
          :search-input="{
            placeholder: t('settings.searchPlaceholder'),
            icon: 'i-lucide-search',
          }"
          class="w-full"
      /></UFormField>

      <UFormField
        :label="t('settings.themeLabel')"
        name="theme"
        class="w-full rounded-1"
      >
        <div class="grid grid-cols-3 gap-1.5">
          <UButton
            v-for="item in themeItems"
            :icon="item.icon"
            :label="item.label"
            @click="form.theme = item.value"
            color="neutral"
            variant="outline"
            :class="[
              form.theme === item.value
                ? 'bg-elevated'
                : 'hover:bg-elevated/50',
            ]"
          />
        </div>
      </UFormField>

      <UButton type="submit">{{ t("settings.saveButton") }}</UButton>
    </UForm>
  </section>
</template>
