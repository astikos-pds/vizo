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
      label: "Geral",
      icon: "i-lucide-layout-dashboard",
      to: "/municipality/dashboard"
    },
    {
      label: "Perfil",
      icon: "i-lucide-user",
      to: "/municipality/profile"
    },
    {
      label: "Problemas",
      icon: "i-lucide-badge-alert",
      to: "/municipality/problems",
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
      to: "/login",
      onSelect: (_) => logout(),
      class: "cursor-pointer",
    },
  ],
]);

const route = useRoute();
</script>

<template>
  <div class="h-screen flex flex-row">
    <section
      class="fixed top-0 left-0 h-screen p-4 border-x border-default z-30"
      :class="{
        'min-w-60': !collapsed,
        hidden: isMobile && collapsed,
        'bg-elevated/25': !isMobile,
      }"
    >
      <header class="min-h-[10%] flex justify-between items-center">
        <div class="w-full flex flex-row gap-1 justify-center items-center">
          <img
            src="/public/favicon-mun.svg"
            alt="Logo"
            :class="collapsed ? 'size-8' : 'size-12'"
          />
          <h1
            v-if="!collapsed"
            class="hidden xl:block text-3xl font-semibold uppercase text-red-400"
          >
            Vizo <H5 CLASS="text-base text-red-400">GESTÃO URBANA</H5>
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
    <div class="size-full border-r border-default" :style="{ marginLeft: !collapsed ? '15rem' : '0' }">
      <header
        class="h-[7%] lg:h-[8%] p-3 lg:p-5 border-b border-default flex flex-row items-center gap-1.5"
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
          {{ t(`navBar.${route.name?.toString()}`) }}
        </h1>
      </header>
      <main class="h-[93%] lg:h-[92%] w-full">
        <slot />
      </main>
    </div>
  </div>
</template>
