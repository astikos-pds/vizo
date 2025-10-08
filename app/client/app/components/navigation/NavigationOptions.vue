<script lang="ts" setup>
const { error } = useMapGeolocation();
const isPermissionForGeolocationDenied = computed(
  () => error.value?.code === 1
);

const { user } = useLoggedInUserStore();

const { isPermissionGranted, isRequestingPermission } = usePush();
</script>

<template>
  <ClientOnly>
    <div class="flex items-center gap-1">
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
          class="flex items-center justify-center text-xl pointer-auto"
        />
      </UTooltip>

      <UTooltip
        :text="
          isPermissionGranted
            ? 'Go to notifcations'
            : 'Enable push notifications'
        "
      >
        <UButton
          size="xl"
          color="neutral"
          variant="ghost"
          :icon="isPermissionGranted ? 'i-lucide-bell' : 'i-lucide-bell-off'"
          :to="isPermissionGranted ? '/notifications' : '/settings'"
          :loading="isRequestingPermission"
          class="flex items-center justify-center text-xl"
        />
      </UTooltip>

      <UButton
        v-if="user"
        size="xs"
        color="neutral"
        variant="ghost"
        :avatar="{
          src: user.avatarUrl?.toString(),
          alt: user.name,
          size: 'sm',
        }"
        :to="`/users/${user.id}`"
      />
    </div>
  </ClientOnly>
</template>
