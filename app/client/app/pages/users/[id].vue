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

definePageMeta({
  name: "Profile",
  middleware: ["auth"],
});

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
  avatar: "https://avatar.iran.liara.run/public/46",
  email: "example@email.com",
  phone: "+55 11 91234-5678",
  address: "Rua Exemplo, 123",
  city: "São Paulo",
  team: "Asfalto e Cimento",
  helpedPeople: 723,
  problemsSolved: 423,
  permission: "Administrador",
});

const reports = ref([
  {
    id: "1001",
    date: "2025-07-10 09:30:00",
    title: "Buraco na rua",
    description:
      "Encontrei esse buraco no canto da rua, esta juntando mosquitos da dengue até.",
    image:
      "https://s2-g1.glbimg.com/BXoCVbSSUMqwk8SrldbMK3pYYbg=/0x0:1280x960/984x0/smart/filters:strip_icc()/i.s3.glbimg.com/v1/AUTH_59edd422c0c84a879bd37670ae4f538a/internal_photos/bs/2018/1/p/JbO1BoTCu5FmmTAWCQvA/cratera-joao-pessoa-bayeux.jpg",
    status: "pending",
  },
  {
    id: "1002",
    date: "2025-07-09 14:15:00",
    title: "Luz queimada em poste",
    description:
      'A luz do poste esta queimada quase fui atropelado por um cara que gritava " chora garotinho".',
    image:
      "https://s2-g1.glbimg.com/BXoCVbSSUMqwk8SrldbMK3pYYbg=/0x0:1280x960/984x0/smart/filters:strip_icc()/i.s3.glbimg.com/v1/AUTH_59edd422c0c84a879bd37670ae4f538a/internal_photos/bs/2018/1/p/JbO1BoTCu5FmmTAWCQvA/cratera-joao-pessoa-bayeux.jpg",
    status: "in_progress",
  },
  {
    id: "1003",
    date: "2025-07-08 17:45:00",
    title: "Vazamento de água",
    description: "SDhjnkdfjdsbnkfjsdbfjsdl,.fhusdlfhjdsf,ljsdfghbu",
    image:
      "https://s2-g1.glbimg.com/BXoCVbSSUMqwk8SrldbMK3pYYbg=/0x0:1280x960/984x0/smart/filters:strip_icc()/i.s3.glbimg.com/v1/AUTH_59edd422c0c84a879bd37670ae4f538a/internal_photos/bs/2018/1/p/JbO1BoTCu5FmmTAWCQvA/cratera-joao-pessoa-bayeux.jpg",
    status: "completed",
  },
]);

const badges = ref([
  { icon: "i-lucide-headset", label: t("profile.badges.solver") },
  { icon: "i-lucide-heart-handshake", label: t("profile.badges.helper") },
]);

/* const avatarOptions = [
  "https://avatar.iran.liara.run/public/4",
  "https://avatar.iran.liara.run/public/74",
  "https://avatar.iran.liara.run/public/57",
  "https://avatar.iran.liara.run/public/10",
  "https://avatar.iran.liara.run/public/86",
  "https://avatar.iran.liara.run/public/43",
  "https://avatar.iran.liara.run/public/30",
  "https://avatar.iran.liara.run/public/64",
  "https://avatar.iran.liara.run/public/90",
  "https://avatar.iran.liara.run/public/14",
  "https://avatar.iran.liara.run/public/19",
  "https://avatar.iran.liara.run/public/96"
] */
</script>

<template>
  <div class="grid grid-cols-1 gap-6 p-4 sm:p-6 md:p-8 xl:grid-cols-1">
    <div class="overflow-hidden rounded-2xl sm:px-6 flex flex-col items-center">
      <!-- <img :src="user.avatar || 'https://avatar.iran.liara.run/public'" height="120" width="120" class="rounded-full mb-4" /> 
      <UModal title="Escolha um Avatar">
        <UButton class="mb-4 px-4 py-2">Alterar avatar</UButton>
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
      </UModal> -->
      <UAvatar :alt="user.name" size="3xl" />
      <UTabs
        :items="items"
        variant="link"
        :ui="{ trigger: 'grow' }"
        class="gap-4 w-full"
      >
        <template #account="{ item }">
          <p class="text-muted mb-4">
            {{ item.description }}
          </p>

          <UForm :state="user" class="flex flex-col gap-4">
            <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div class="flex flex-col gap-4">
                <UFormField :label="t('profile.form.name')" name="name">
                  <UInput v-model="user.name" class="w-80" required />
                </UFormField>
                <UFormField :label="t('profile.form.username')" name="username">
                  <UInput
                    v-model="user.username"
                    class="w-80"
                    required
                    :disabled="true"
                  />
                </UFormField>
                <UFormField :label="t('profile.form.email')" name="email">
                  <UInput
                    v-model="user.email"
                    class="w-80"
                    required
                    :disabled="true"
                  />
                </UFormField>
              </div>
              <div class="flex flex-col gap-4">
                <UFormField
                  :label="t('profile.form.cellphone')"
                  name="cellphone"
                >
                  <UInput v-model="user.phone" class="w-40" required />
                </UFormField>
                <UFormField :label="t('profile.form.address')" name="address">
                  <UInput v-model="user.address" class="w-80" required />
                </UFormField>
              </div>
            </div>
            <UButton
              :label="t('profile.form.save')"
              type="submit"
              variant="soft"
              class="self-end mt-4"
            />
          </UForm>
          <div
            class="border border-gray-200 bg-white px-4 pb-3 pt-4 dark:border-gray-800 dark:bg-white/[0.03] p-6 mt-8"
          >
            <h2 class="text-xl font-semibold mb-6">
              {{ t("profile.reports.title") }}
            </h2>
            <div class="flex flex-col gap-8">
              <div
                v-for="(report, idx) in reports"
                :key="report.id"
                class="flex flex-col gap-4"
              >
                <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
                  <div class="flex flex-col gap-4">
                    <UFormField
                      :label="t('profile.reports.type')"
                      :name="`reportType-${idx}`"
                    >
                      <UInput v-model="report.title" :disabled="true" />
                    </UFormField>
                    <UFormField
                      :label="t('profile.reports.date')"
                      :name="`reportDate-${idx}`"
                    >
                      <UInput v-model="report.date" :disabled="true" />
                    </UFormField>
                  </div>
                  <div class="flex flex-col gap-4">
                    <UFormField
                      :label="t('profile.reports.description')"
                      :name="`reportDescription-${idx}`"
                    >
                      <UTextarea
                        v-model="report.description"
                        :rows="5"
                        :maxrows="5"
                        autoresize
                        class="w-full"
                        :disabled="true"
                      />
                    </UFormField>
                    <div class="flex justify-end">
                      <UButton
                        :label="t('profile.reports.delete')"
                        icon="i-lucide-trash-2"
                        size="md"
                        color="error"
                        variant="solid"
                        class="mt-2"
                      />
                    </div>
                    <div class="w-[200px] h-[200px]">
                      <NuxtImg
                        :src="report.image"
                        class="rounded-lg w-full h-full object-cover aspect-square"
                      />
                    </div>
                  </div>
                </div>
                <hr
                  v-if="idx < reports.length - 1"
                  class="border-t border-gray-300 dark:border-gray-700 my-4"
                />
              </div>
            </div>
          </div>
        </template>

        <template #password="{ item }">
          <div class="flex flex-col items-center justify-center gap-8 py-12">
            <div class="grid grid-cols-1 md:grid-cols-2 gap-8 w-full">
              <div class="flex flex-col items-center justify-center">
                <div class="text-4xl font-bold mb-6 text-center">
                  <h2 class="text-gray-600 dark:text-gray-300">
                    {{ t("profile.stats.solved") }}
                  </h2>
                  <h2 class="text-green-600 text-5xl font-extrabold">
                    {{ user.problemsSolved }}
                  </h2>
                </div>
              </div>
              <div class="flex flex-col items-center justify-center">
                <div class="text-4xl font-bold mb-6 text-center">
                  <h2 class="text-gray-600 dark:text-gray-300">
                    {{ t("profile.stats.helped") }}
                  </h2>
                  <h2 class="text-green-600 text-5xl font-extrabold">
                    {{ user.helpedPeople }}
                  </h2>
                </div>
              </div>
            </div>
            <div class="flex flex-row items-center justify-center gap-4 mt-6">
              <template v-if="user.problemsSolved > 0">
                <UBadge
                  :icon="badges[0].icon"
                  size="md"
                  :color="
                    user.problemsSolved > 300
                      ? 'info'
                      : user.problemsSolved > 100
                      ? 'success'
                      : 'primary'
                  "
                  variant="solid"
                  >{{ badges[0].label }}</UBadge
                >
              </template>
              <template v-if="user.helpedPeople > 0">
                <UBadge
                  :icon="badges[1].icon"
                  size="md"
                  :color="
                    user.helpedPeople > 300
                      ? 'info'
                      : user.helpedPeople > 100
                      ? 'success'
                      : 'primary'
                  "
                  variant="solid"
                  >{{ badges[1].label }}</UBadge
                >
              </template>
            </div>
          </div>
          <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
            <div class="flex flex-col gap-4">
              <UFormField
                :label="t('profile.fields.permission')"
                :name="'permission'"
              >
                <UInput v-model="user.permission" :disabled="true" />
              </UFormField>
              <UFormField :label="t('profile.fields.city')" :name="'city'">
                <UInput v-model="user.city" :disabled="true" />
              </UFormField>
              <UFormField :label="t('profile.fields.team')" :name="'team'">
                <UInput v-model="user.team" :disabled="true" />
              </UFormField>
            </div>
          </div>
        </template>
      </UTabs>
    </div>
  </div>
</template>
