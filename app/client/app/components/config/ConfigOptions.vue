<script lang="ts" setup>
import type { DropdownMenuItem } from "@nuxt/ui";
import * as locales from "@nuxt/ui/locale";

const { t, locale, setLocale } = useI18n();
const colorMode = useColorMode();

function selectLocale(newLocale: typeof locale.value) {
  setLocale(newLocale);
}

function createLocaleItem(
  localeItem: typeof locales.en,
  flag: string
): DropdownMenuItem {
  return {
    id: localeItem.code,
    label: localeItem.name,
    icon: `i-flagpack-${flag}`,
    type: "checkbox",
    onSelect: (e: Event) =>
      selectLocale(localeItem.code as typeof locale.value),
    checked: locale.value === localeItem.code,
  };
}

function createThemeItem(theme: {
  id: "light" | "dark" | "system";
  label: string;
  icon: string;
}): DropdownMenuItem {
  return {
    id: theme.id,
    label: theme.label,
    icon: `i-lucide-${theme.icon}`,
    type: "checkbox",
    onSelect: (e: Event) => (colorMode.preference = theme.id),
    checked: colorMode.preference === theme.id,
  };
}

const items = computed<DropdownMenuItem[][]>(() => [
  [
    {
      label: t("configHeader.language.label"),
      icon: "i-lucide-languages",
      children: [
        createLocaleItem(locales.en, "us"),
        createLocaleItem(locales.pt_br, "br"),
      ],
    },
    {
      label: t("configHeader.theme.label"),
      icon: "i-lucide-palette",
      children: [
        createThemeItem({
          id: "system",
          label: t("configHeader.theme.system"),
          icon: "monitor",
        }),
        createThemeItem({
          id: "light",
          label: t("configHeader.theme.light"),
          icon: "sun",
        }),
        createThemeItem({
          id: "dark",
          label: t("configHeader.theme.dark"),
          icon: "moon",
        }),
      ],
    },
  ],
]);
</script>

<template>
  <UDropdownMenu
    :items="items"
    :content="{
      align: 'start',
      side: 'bottom',
      sideOffset: 8,
    }"
  >
    <UButton
      icon="i-lucide-settings"
      color="neutral"
      variant="outline"
      size="xl"
      :ui="{
        leadingIcon: 'text-lg xl:text-xl',
      }"
    ></UButton>
  </UDropdownMenu>
</template>
