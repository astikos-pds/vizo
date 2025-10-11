<script lang="ts" setup>
const {
  isPermissionGranted: isGeolocationEnabled,
  isRequestingPermission: isRequestingGeolocationPermission,
  coords,
  isLocationPrecise,
} = useMapGeolocation();

const { user } = useLoggedInUserStore();

const {
  isPermissionGranted: areNotificationsEnabled,
  isRequestingPermission: isRequestingNotificationPermission,
} = usePush();
</script>

<template>
  <ClientOnly>
    <div class="flex items-center gap-1">
      <UTooltip
        :text="
          isGeolocationEnabled
            ? isLocationPrecise
              ? `Coordinates: ${coords.latitude.toFixed(
                  2
                )}, ${coords.longitude.toFixed(2)}`
              : 'Geolocation enabled'
            : 'Enable geolocation tracking'
        "
      >
        <UButton
          size="xl"
          color="neutral"
          variant="ghost"
          :icon="
            isGeolocationEnabled
              ? 'i-lucide-navigation'
              : 'i-lucide-navigation-off'
          "
          :to="isGeolocationEnabled ? undefined : '/settings'"
          :loading="isRequestingGeolocationPermission"
          class="flex items-center justify-center text-xl pointer-auto"
        />
      </UTooltip>

      <UTooltip
        :text="
          areNotificationsEnabled
            ? 'Go to notifcations'
            : 'Enable push notifications'
        "
      >
        <UButton
          size="xl"
          color="neutral"
          variant="ghost"
          :icon="
            areNotificationsEnabled ? 'i-lucide-bell' : 'i-lucide-bell-off'
          "
          :to="areNotificationsEnabled ? '/notifications' : '/settings'"
          :loading="isRequestingNotificationPermission"
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
