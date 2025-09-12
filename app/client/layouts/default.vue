<script lang="ts" setup>
import { useBreakpoints } from "@vueuse/core";
import NavigationBody from "~/components/navigation/NavigationBody.vue";
import NavigationHeader from "~/components/navigation/NavigationHeader.vue";
import UserProfile from "~/components/navigation/UserProfile.vue";

const { t } = useI18n();

const collapsed = ref<boolean>(false);
const open = ref<boolean>(false);

const route = useRoute();
const name = computed(() => route.meta.name);

const breakpoints = useBreakpoints({
  md: 768,
});

const isMobile = breakpoints.smallerOrEqual("md");

const { user } = useLoggedInUserStore();
</script>

<template>
  <div class="h-screen flex flex-row">
    <USlideover
      v-if="isMobile"
      side="left"
      v-model:open="open"
      :ui="{
        header: 'flex flex-row justify-between px-3 sm:px-3 sm:py-0',
        body: 'p-3 sm:p-3',
        footer: 'px-3 sm:px-3 sm:py-0',
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
      <template #footer>
        <UserProfile v-if="user" :user="user" :collapsed="collapsed" />
      </template>
    </USlideover>
    <section
      v-else
      class="h-full border-x border-default flex flex-col"
      :class="{
        'w-60': !collapsed,
        'bg-elevated/25': !isMobile,
      }"
    >
      <NavigationHeader
        :collapsed="collapsed"
        class="my-3 flex items-center justify-center"
      />
      <div class="flex-1 flex flex-col justify-between items-center">
        <NavigationBody :collapsed="collapsed" class="px-2" />
        <UserProfile
          v-if="user"
          :user="user"
          :collapsed="collapsed"
          class="border-t border-default w-full"
        />
      </div>
    </section>
    <div class="h-screen w-full flex flex-col flex-1 border-r border-default">
      <header
        class="h-18 p-3 lg:p-5 border-b border-default flex flex-row items-center justify-between gap-1.5"
      >
        <div class="flex items-center">
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
            {{ name ?? t(`navBar.${route.name?.toString()}`) }}
          </h1>
        </div>

        <div class="flex items-center gap-3">
          <UButton
            size="xl"
            color="neutral"
            variant="outline"
            icon="i-lucide-bell"
            to="/notifications"
            class="rounded-full size-10 flex items-center justify-center text-xl"
          />

          <UButton
            v-if="user"
            size="xl"
            color="neutral"
            variant="link"
            :avatar="{
              src: user.avatarUrl?.toString(),
              alt: user.name,
              size: 'lg',
            }"
            :to="`/users/${user.id}`"
          />
        </div>
      </header>
      <main class="flex-1 w-full overflow-y-auto flex flex-col items-center">
        <slot />
      </main>
    </div>
  </div>
</template>
