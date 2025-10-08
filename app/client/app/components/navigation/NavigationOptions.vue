<script lang="ts" setup>
import { getToken } from "firebase/messaging";

const { error } = useMapGeolocation();
const isPermissionForGeolocationDenied = computed(
  () => error.value?.code === 1
);

const isNotificationGranted = ref(false);

const { registerPushToken } = useMe();

async function setToken() {
  const { $messaging, $config } = useNuxtApp();

  const vapidKey = $config.public.firebaseVapidKey;

  if (!vapidKey) return;

  const registration = await navigator.serviceWorker.ready;

  const token = await getToken($messaging, {
    vapidKey,
    serviceWorkerRegistration: registration,
  });

  await registerPushToken({ token, platform: "WEB" });
}

const { user } = useLoggedInUserStore();

onMounted(async () => {
  if (!("Notification" in window)) return;

  const permission = window.Notification.permission;
  isNotificationGranted.value = permission === "granted";

  if (permission !== "granted") {
    const permission = await Notification.requestPermission();

    if (permission !== "granted") return;
  }

  await setToken();
});
</script>

<template>
  <ClientOnly>
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
          class="flex items-center justify-center text-xl pointer-auto"
        />
      </UTooltip>

      <UButton
        size="xl"
        color="neutral"
        variant="ghost"
        :icon="isNotificationGranted ? 'i-lucide-bell' : 'i-lucide-bell-off'"
        :to="isNotificationGranted ? '/notifications' : undefined"
        class="flex items-center justify-center text-xl"
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
  </ClientOnly>
</template>
