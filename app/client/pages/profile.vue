<script lang="ts" setup>
import { ref } from "vue";
import { useI18n } from "vue-i18n";
import type { TabsItem } from "@nuxt/ui";

const { t } = useI18n();

useHead({
  meta: [
    { name: "viewport", content: "width=device-width, initial-scale=1.0" },
  ],
});

/* definePageMeta({
  layout: "official",
}); */

const items = [
  {
    label: t("profile.tab.account.label"),
    description: t("profile.tab.account.description"),
    icon: "i-lucide-user",
    slot: "account" as const,
  },
  {
    label: t("profile.tab.admin.label"),
    description: t("profile.tab.admin.description"),
    icon: "i-lucide-lock",
    slot: "password" as const,
  },
] satisfies TabsItem[];

const user = reactive({
  name: "Joaquim Souza",
  username: "joaquimsouza",
  avatar: "/avatar/avatar1.png",
  email: "example@email.com",
  phone: "+55 11 91234-5678",
  address: "Rua Exemplo, 123",
  city: "São Paulo"
});

const badges = ref([
  { icon: "i-lucide-headset", label: t("profile.badges.solver") },
  { icon: "i-lucide-heart-handshake", label: t("profile.badges.helper") },
]);
const avatarOptions = Array.from({ length: 12 }, (_, i) => `/avatar/avatar${i + 1}.png`)
</script>

<template>
  <div class="grid grid-cols-1 gap-6 p-4 sm:p-6 md:p-8 xl:grid-cols-1 m-10">
    <div class="overflow-hidden rounded-2xl sm:px-6 flex flex-col items-left">
      <div class="flex items-center gap-6 mb-4">
        <img
          :src="user.avatar || `https://avatar.iran.liara.run/username?username=${user.name}`"
          height="120"
          width="120"
          class="rounded-full"
        />
        <div class="flex flex-col">
          <span class="text-lg font-semibold">@{{ user.username }}</span>
          <!-- Modal de editar -->
          <UModal title="Escolha um Avatar">
            <UButton
              icon="i-lucide-pencil"
              size="sm"
              class="mt-2 w-fit"
              color="primary"
              variant="soft"
            >
              Editar
            </UButton>
            <template #body>
              <div class="flex flex-wrap gap-4 justify-center items-center py-4">
                <div
                  v-for="(avatar, idx) in avatarOptions"
                  :key="avatar"
                  class="cursor-pointer border-2 rounded-full p-1 transition-all duration-200"
                  :class="user.avatar === avatar ? 'border-blue-500' : 'border-transparent'"
                  @click="user.avatar = avatar"
                >
                  <img :src="avatar" width="80" height="80" class="rounded-full" />
                </div>
              </div>
            </template>
          </UModal>
        </div>
      </div>
    </div>
  </div>
</template>
