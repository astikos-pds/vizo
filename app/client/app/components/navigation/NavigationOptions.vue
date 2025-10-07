<script lang="ts" setup>
import { getToken } from "firebase/messaging";

const { error } = useMapGeolocation();
const isPermissionForGeolocationDenied = computed(
  () => error.value?.code === 1
);

const isNotificationGranted = ref(false);

const { registerPushToken } = useMe();
const toast = useToast();

async function setToken() {
  const { $messaging, $config } = useNuxtApp();

  const vapidKey = $config.public.firebaseVapidKey;

  if (!vapidKey) {
    console.warn("VAPID not set.");
  }

  const token = await getToken($messaging, {
    vapidKey,
  });

  const response = await registerPushToken({ token, platform: "WEB" });

  if (response) return;

  toast.add({
    title: "Failure",
    description: "Failure to save your push token. Try again later.",
  });
}

async function onNotificationClick() {
  if (!("Notification" in window)) {
    alert("Seu navegador não suporta notificações.");
    return;
  }

  if (!isNotificationGranted.value) {
    const permission = await Notification.requestPermission();

    if (permission !== "granted") return;

    isNotificationGranted.value = true;
  }

  await setToken();
}

const { user } = useLoggedInUserStore();

onMounted(() => {
  if ("Notification" in window) {
    isNotificationGranted.value = window.Notification.permission === "granted";
  }
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
          class="rounded-full flex items-center justify-center text-xl pointer-auto"
        />
      </UTooltip>

      <UButton
        size="xl"
        color="neutral"
        variant="ghost"
        :icon="isNotificationGranted ? 'i-lucide-bell' : 'i-lucide-bell-off'"
        :to="isNotificationGranted ? '/notifications' : undefined"
        class="rounded-full flex items-center justify-center text-xl"
        @click="onNotificationClick"
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
