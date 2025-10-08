<script lang="ts" setup>
const { isPermissionGranted: isPermissionForGeolocationGranted } =
  useMapGeolocation();

const { user } = useLoggedInUserStore();

const {
  isPermissionGranted: isPermissionForNotificationsGranted,
  isRequestingPermission,
} = usePush();
</script>

<template>
  <ClientOnly>
    <div class="flex items-center gap-1">
      <UTooltip text="Geolocation tracking state">
        <UButton
          size="xl"
          color="neutral"
          variant="ghost"
          :icon="
            isPermissionForGeolocationGranted
              ? 'i-lucide-navigation'
              : 'i-lucide-navigation-off'
          "
          :to="isPermissionForGeolocationGranted ? undefined : '/settings'"
          class="flex items-center justify-center text-xl pointer-auto"
        />
      </UTooltip>

      <UTooltip
        :text="
          isPermissionForNotificationsGranted
            ? 'Go to notifcations'
            : 'Enable push notifications'
        "
      >
        <UButton
          size="xl"
          color="neutral"
          variant="ghost"
          :icon="
            isPermissionForNotificationsGranted
              ? 'i-lucide-bell'
              : 'i-lucide-bell-off'
          "
          :to="
            isPermissionForNotificationsGranted ? '/notifications' : '/settings'
          "
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
