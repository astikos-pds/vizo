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

  const alreadyRegistered = sessionStorage.getItem("pushRegistered");
  if (alreadyRegistered) return;

  const vapidKey = $config.public.firebaseVapidKey;

  if (!vapidKey) return;

  const registration = await navigator.serviceWorker.ready;

  const token = await getToken($messaging, {
    vapidKey,
    serviceWorkerRegistration: registration,
  });

  const savedToken = localStorage.getItem("pushToken");
  if (savedToken === token) {
    sessionStorage.setItem("pushRegistered", "true");
    return;
  }

  const response = await registerPushToken({ token, platform: "WEB" });

  if (!response) return;

  localStorage.setItem("pushToken", token);
  sessionStorage.setItem("pushRegistered", "true");
}

const { user } = useLoggedInUserStore();

onMounted(async () => {
  if (!("Notification" in window)) return;

  if (!user) return;

  let permission = window.Notification.permission;
  isNotificationGranted.value = permission === "granted";

  if (permission !== "granted") {
    permission = await Notification.requestPermission();

    if (permission !== "granted") return;
  }

  isNotificationGranted.value = permission === "granted";

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
