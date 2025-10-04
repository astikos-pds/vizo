<script lang="ts" setup>
const { t } = useI18n();

import type { AffiliatedUser } from "~/types/domain/affiliated-user";

defineProps<{
  items: AffiliatedUser[];
}>();
</script>

<template>
  <ul role="list" class="divide-y divide-default">
    <AffiliatedUsersProfile
      v-for="affiliatedUser in items"
      :key="affiliatedUser.id"
      v-bind="affiliatedUser"
      class="w-full"
    />
  </ul>
</template>
