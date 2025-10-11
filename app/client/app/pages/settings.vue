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
  areNotificationsEnabled: z.boolean(),
  isGeolocationEnabled: z.boolean(),
});

type SettingsSchema = z.output<typeof settingsSchema>;

const form = reactive<SettingsSchema>({
  language: locale.value,
  theme: colorMode.preference,
  areNotificationsEnabled: false,
  isGeolocationEnabled: false,
});

const languageIcon = computed(
  () => languageItems.value.find((item) => item.id === form.language)?.icon
);

const themeIcon = computed(
  () => themeItems.value.find((item) => item.value === form.theme)?.icon
);

const {
  isPermissionGranted: isPermissionForNotificationGranted,
  requestPermissionForNotifications,
} = usePush();

const {
  isPermissionGranted: isGeolocationEnabled,
  requestGeolocationPermission,
  getGeolocationPermission,
} = useMapGeolocation();

const toast = useToast();

const onSubmit = async (event: FormSubmitEvent<SettingsSchema>) => {
  await setLocale(event.data.language);

  colorMode.preference = event.data.theme;

  const isGeolocationEnabled = event.data.isGeolocationEnabled;
  const areNotificationsEnabled = event.data.areNotificationsEnabled;

  if (isGeolocationEnabled) {
    requestGeolocationPermission();
  }

  if (areNotificationsEnabled) {
    await requestPermissionForNotifications();
  }

  toast.add({
    title: t("toast.success.title"),
    description: t("toast.success.description.settingsSaved"),
    color: "success",
  });
};

onMounted(async () => {
  form.areNotificationsEnabled =
    "Notification" in window && Notification.permission === "granted";

  const geolocationStatus = await getGeolocationPermission();
  form.isGeolocationEnabled = geolocationStatus.state === "granted";
});
</script>

<template>
  <CommonPage title="Settings" with-padding>
    <section class="size-full flex items-center flex-col mt-10">
      <div
        class="w-[80%] md:w-[75%] xl:w-[65%] 2xl:w-[55%] flex flex-col items-center gap-5"
      >
        <h1 class="text-3xl font-semibold">
          {{ t("pages.settings.title") }}
        </h1>

        <UForm
          :state="form"
          :schema="settingsSchema"
          @submit="onSubmit"
          class="w-full flex flex-col items-center gap-4"
        >
          <UFormField
            :label="t('pages.settings.languageLabel')"
            name="language"
            class="w-full"
            ><USelectMenu
              v-model="form.language"
              value-key="id"
              :items="languageItems"
              :icon="languageIcon"
              :search-input="{
                placeholder: t('pages.settings.searchPlaceholder'),
                icon: 'i-lucide-search',
              }"
              class="w-full"
          /></UFormField>

          <UFormField
            :label="t('pages.settings.themeLabel')"
            name="theme"
            class="w-full"
          >
            <USelect
              v-model="form.theme"
              value-key="value"
              :items="themeItems"
              :icon="themeIcon"
              class="w-full"
            />
          </UFormField>

          <UFormField
            label="Geolocation"
            name="geolocation"
            description="Geolocation tracking permits to make reports base in your current position."
            class="w-full flex justify-between items-center gap-4"
          >
            <USwitch
              v-model="form.isGeolocationEnabled"
              :disabled="isGeolocationEnabled"
            />
          </UFormField>

          <UFormField
            label="Notifications"
            name="notifications"
            description="Push notifications permit to notify in background, even if you are not using the app."
            class="w-full flex justify-between items-center gap-4"
          >
            <USwitch
              v-model="form.areNotificationsEnabled"
              :disabled="isPermissionForNotificationGranted"
            />
          </UFormField>

          <UButton type="submit">{{ t("pages.settings.saveButton") }}</UButton>
        </UForm>
      </div>
    </section>
  </CommonPage>
</template>
