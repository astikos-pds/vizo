<script lang="ts" setup>
import type { DropdownMenuItem } from "@nuxt/ui";
import * as locales from "@nuxt/ui/locale";

const { t, locale, setLocale } = useI18n();
const isDark = useDark();

function selectLocale(e: Event, newLocale: typeof locale.value) {
  setLocale(newLocale);
}

function createLocaleItem(
  localeItem: typeof locale.value,
  flag: string
): DropdownMenuItem {
  return {
    id: localeItem.code,
    label: localeItem.name,
    icon: `i-flagpack-${flag}`,
    type: "checkbox",
    onSelect: (e: Event) => selectLocale(e, localeItem.code),
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
    onSelect: (e: Event) => (isDark.value = theme.id === "dark" ? true : false),
    checked: isDark.value === (theme.id === "dark" ? true : false),
  };
}

const items = computed<DropdownMenuItem[][]>(() => [
  [
    {
      label: "Language",
      icon: "i-lucide-languages",
      children: [
        createLocaleItem(locales.en, "us"),
        createLocaleItem(locales.pt_br, "br"),
      ],
    },
    {
      label: "Theme",
      icon: "i-lucide-palette",
      children: [
        createThemeItem({
          id: "light",
          label: "Light",
          icon: "sun",
        }),
        createThemeItem({
          id: "dark",
          label: "Dark",
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
