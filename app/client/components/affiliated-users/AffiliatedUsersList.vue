<script lang="ts" setup>
const { t } = useI18n();

import type { AffiliatedUser } from "~/types/domain/affiliated-user";
import AffiliatedUsersProfile from "./AffiliatedUsersProfile.vue";

defineProps<{
  items: AffiliatedUser[];
}>();
</script>

<template>
  <div class="pl-4 mb-6 flex flex-col gap-2">
    <div
      v-if="items.length === 0"
      class="text-sm flex justify-center items-center h-24 border border-default rounded-md"
    >
<<<<<<< HEAD
      {{ t("components.affiliatedUsers.noPublicAgentsFound") }}
=======
      {{ t('components.affiliatedUsers.noPublicAgentsFound') }}
>>>>>>> 4492851ec4479446cd92d78277630bfd4d8f8a01
    </div>
    <div v-else class="w-full" v-for="(affiliatedUser, i) in items">
      <AffiliatedUsersProfile
        :key="affiliatedUser.id"
        v-bind="affiliatedUser"
        class="w-full mb-2"
      />
      <USeparator v-if="i < items.length - 1" />
    </div>
  </div>
</template>
