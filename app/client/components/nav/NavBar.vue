<script lang="ts" setup>
import type { NavigationMenuItem } from "@nuxt/ui";
import { useBreakpoints } from "@vueuse/core";

const { t } = useI18n();
const { logout } = useAuth();

const collapsed = ref<boolean>(false);

const breakpoints = useBreakpoints({
  lg: 1024,
  md: 768,
});

const isMobile = breakpoints.smallerOrEqual("md");

watchEffect(() => {
  collapsed.value = isMobile.value;
});

const items = computed<NavigationMenuItem[]>(() => [
  {
    label: t("navBar.home"),
    icon: "i-lucide-house",
    to: "/",
  },
  {
    label: t("navBar.report"),
    icon: "i-lucide-message-square-warning",
    to: "/report",
  },
  {
    label: t("navBar.settings"),
    icon: "i-lucide-settings",
    to: "/settings",
  },
]);

const textClasses = ref<string>("text-lg xl:text-xl");
</script>

<template>
  <section
    class="min-h-full p-2 lg:p-3 xl:p-4 border-x border-neutral-200 dark:border-neutral-800"
    :class="collapsed ? '' : 'lg:min-w-[20%]'"
  >
    <header class="h-[10%] flex justify-between items-center">
      <div
        v-if="!collapsed"
        class="flex flex-row gap-3 justify-center items-center"
      >
        <img src="/public/favicon.svg" alt="Logo" class="size-16" />
        <h1 class="hidden xl:block text-4xl font-semibold uppercase">Vizo</h1>
      </div>
      <UButton
        icon="i-lucide-menu"
        color="neutral"
        variant="outline"
        size="xl"
        class="hover:cursor-pointer size-[2.5rem] flex justify-center items-center text-[1.5rem]"
        @click="collapsed = !collapsed"
      />
    </header>
    <div class="h-[90%] flex flex-col justify-between">
      <main class="h-full flex flex-col">
        <UNavigationMenu
          :collapsed="collapsed"
          :items="items"
          orientation="vertical"
          :ui="{
            link: `px-3 min-h-[3rem] ${textClasses} gap-4`,
          }"
        >
          <template #item-label="{ item }">{{ item.label }}</template>
        </UNavigationMenu>
      </main>
      <div class="h-[10%] flex items-center">
        <NuxtLink to="/login" class="w-full">
          <UButton
            icon="i-lucide-log-out"
            color="neutral"
            variant="ghost"
            @click="logout"
            :class="`w-full cursor-pointer ${textClasses}`"
            ><span v-if="!collapsed">{{ t("navBar.exit") }}</span></UButton
          >
        </NuxtLink>
      </div>
    </div>
  </section>
</template>
