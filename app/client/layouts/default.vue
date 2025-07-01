<script lang="ts" setup>
import type { NavigationMenuItem } from "@nuxt/ui";
import { useBreakpoints } from "@vueuse/core";

const { t } = useI18n();
const { logout } = useAuth();

const collapsed = ref<boolean>(false);

const breakpoints = useBreakpoints({
  md: 768,
});

const isMobile = breakpoints.smallerOrEqual("md");

const items = computed<NavigationMenuItem[][]>(() => [
  [
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
  ],
  [
    {
      label: t("navBar.exit"),
      icon: "i-lucide-log-out",
      onSelect: async (e) => {
        logout();
        await navigateTo("/login");
      },
      class: "cursor-pointer",
    },
  ],
]);

const route = useRoute();
</script>

<template>
  <div class="h-screen flex flex-row">
    <section
      class="h-full p-4 border-x border-neutral-200 dark:border-neutral-800"
      :class="{
        'min-w-60': !collapsed,
        hidden: isMobile && collapsed,
        'bg-elevated/25': !isMobile,
      }"
    >
      <header class="min-h-[10%] flex justify-between items-center">
        <div class="w-full flex flex-row gap-1 justify-center items-center">
          <img
            src="/public/favicon.svg"
            alt="Logo"
            :class="collapsed ? 'size-8' : 'size-12'"
          />
          <h1
            v-if="!collapsed"
            class="hidden xl:block text-3xl font-semibold uppercase"
          >
            Vizo
          </h1>
        </div>
      </header>
      <div class="h-[90%] w-full flex flex-col justify-between">
        <UNavigationMenu
          :collapsed="collapsed"
          :items="items[0]"
          orientation="vertical"
          tooltip
        />

        <UNavigationMenu
          :collapsed="collapsed"
          :items="items[1]"
          orientation="vertical"
          tooltip
        />
      </div>
    </section>
    <div class="size-full border-r border-neutral-200 dark:border-neutral-800">
      <header
        class="h-[7%] lg:h-[8%] p-3 lg:p-5 border-b border-neutral-200 dark:border-neutral-800 flex flex-row items-center gap-1.5"
      >
        <UButton
          :icon="
            isMobile
              ? 'i-lucide-menu'
              : collapsed
              ? 'i-lucide-square-chevron-right'
              : 'i-lucide-square-chevron-left'
          "
          color="neutral"
          variant="ghost"
          class="text-xl"
          @click="collapsed = !collapsed"
        />
        <h1 class="font-semibold text-base capitalize">
          {{ route.name === "index" ? "home" : route.name }}
        </h1>
      </header>
      <main class="h-[93%] lg:h-[92%] w-full">
        <slot />
      </main>
    </div>
  </div>
</template>
