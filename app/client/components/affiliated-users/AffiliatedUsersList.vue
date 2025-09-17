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
      {{ t('components.affiliatedUsers.noPublicAgentsFound') }}
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
