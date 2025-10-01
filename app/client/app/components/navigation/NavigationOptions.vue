<script lang="ts" setup>
const { error } = useMapGeolocation();
const isPermissionForGeolocationDenied = computed(
  () => error.value?.code === 1
);
</script>

<template>
  <div class="flex items-center gap-1">
    <UButton
      size="xl"
      :color="isPermissionForGeolocationDenied ? 'neutral' : 'primary'"
      variant="ghost"
      :icon="
        isPermissionForGeolocationDenied
          ? 'i-lucide-navigation-off'
          : 'i-lucide-navigation'
      "
      class="rounded-full size-10 flex items-center justify-center text-xl pointer-auto"
    />

    <!-- <UButton
            size="xl"
            color="neutral"
            variant="outline"
            icon="i-lucide-bell"
            to="/notifications"
            class="rounded-full size-10 flex items-center justify-center text-xl"
          /> -->

    <!-- <UButton
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
          /> -->
  </div>
</template>
