<script lang="ts" setup>
const { error } = useMapGeolocation();
const isPermissionForGeolocationDenied = computed(
  () => error.value?.code === 1
);

const { user } = useLoggedInUserStore();
</script>

<template>
  <div class="flex items-center gap-2">
    <UTooltip text="Geolocation tracking state">
      <UButton
        size="xl"
        :color="isPermissionForGeolocationDenied ? 'neutral' : 'primary'"
        variant="ghost"
        :icon="
          isPermissionForGeolocationDenied
            ? 'i-lucide-navigation-off'
            : 'i-lucide-navigation'
        "
        class="rounded-full flex items-center justify-center text-xl pointer-auto"
      />
    </UTooltip>

    <UButton
      size="xl"
      color="neutral"
      variant="outline"
      icon="i-lucide-bell"
      to="/notifications"
      class="rounded-full flex items-center justify-center text-xl"
    />

    <UButton
      v-if="user"
      size="xl"
      color="neutral"
      variant="link"
      square
      :avatar="{
        src: user.avatarUrl?.toString(),
        alt: user.name,
        size: 'lg',
      }"
      :to="`/users/${user.id}`"
      class="p-0"
    />
  </div>
</template>
