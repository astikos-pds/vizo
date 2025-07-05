<script lang="ts" setup>
import { useBreakpoints } from "@vueuse/core";
import NavigationBody from "~/components/NavigationBody.vue";
import NavigationHeader from "~/components/NavigationHeader.vue";

const { t } = useI18n();

const collapsed = ref<boolean>(false);
const open = ref<boolean>(false);

const breakpoints = useBreakpoints({
  md: 768,
});

const isMobile = breakpoints.smallerOrEqual("md");

const route = useRoute();
</script>

<template>
  <div class="h-screen flex flex-row">
    <USlideover
      v-if="isMobile"
      side="left"
      v-model:open="open"
      :ui="{
        header: 'flex flex-row justify-between px-3 sm:px-3 sm:py-0',
        body: 'p-0 sm:p-0',
      }"
    >
      <template #header>
        <NavigationHeader :collapsed="collapsed" />
        <UButton
          icon="i-lucide-x"
          color="neutral"
          variant="ghost"
          class="text-xl"
          @click="open = !open"
        />
      </template>
      <template #body>
        <NavigationBody :collapsed="collapsed" />
      </template>
    </USlideover>
    <section
      v-else
      class="h-full border-x border-default"
      :class="{
        'w-60': !collapsed,
        'bg-elevated/25': !isMobile,
      }"
    >
      <NavigationHeader :collapsed="collapsed" />
      <NavigationBody :collapsed="collapsed" />
    </section>
    <div class="size-full border-r border-default">
      <header
        class="h-18 p-3 lg:p-5 border-b border-default flex flex-row items-center gap-1.5"
      >
        <UButton
          v-if="isMobile"
          icon="i-lucide-menu"
          color="neutral"
          variant="ghost"
          class="text-xl"
          @click="open = !open"
        />

        <UButton
          v-else
          :icon="
            collapsed
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
      <main class="h-[calc(100vh-4.5rem)] w-full">
        <slot />
      </main>
    </div>
  </div>
</template>
