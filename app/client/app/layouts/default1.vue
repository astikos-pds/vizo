<script lang="ts" setup>
const open = ref<boolean>(false);

const { user } = useLoggedInUserStore();
</script>

<template>
  <UDashboardGroup unit="rem">
    <UDashboardSidebar
      id="default"
      v-model:open="open"
      collapsible
      resizable
      class="bg-elevated/25"
      :ui="{ footer: 'lg:border-t lg:border-default' }"
    >
      <template #header="{ collapsed }">
        <NavigationHeader :collapsed="collapsed" />
      </template>

      <template #default="{ collapsed }">
        <UDashboardSearchButton
          :collapsed="collapsed"
          class="bg-transparent ring-default"
        />

        <NavigationBody :collapsed="collapsed" v-model:open="open" />
      </template>

      <template #footer="{ collapsed }">
        <NavigationUserProfile
          v-if="user"
          :user="user"
          :collapsed="collapsed"
        />
      </template>
    </UDashboardSidebar>

    <slot />
  </UDashboardGroup>
</template>
