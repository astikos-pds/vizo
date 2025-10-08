import { getToken } from "firebase/messaging";

export const usePush = () => {
  const isPermissionGranted = ref(false);
  const isRequestingPermission = ref(false);

  const { registerPushToken } = useMe();

  async function requestPermissionForNotifications() {
    if (!("Notification" in window)) return;
    if (isRequestingPermission.value) return;

    isRequestingPermission.value = true;

    try {
      const permission = await Notification.requestPermission();

      if (permission === "granted") {
        isPermissionGranted.value = true;
        await registerPushTokenIfPossible();
      }
    } finally {
      isRequestingPermission.value = false;
    }
  }

  async function registerPushTokenIfPossible() {
    if (!import.meta.client) return;

    if (!("Notification" in window)) return;
    if (!isPermissionGranted.value || isRequestingPermission.value) return;

    const pushWasAlreadyRegistered = sessionStorage.getItem("push-registered");

    if (pushWasAlreadyRegistered) return;

    const { $messaging, $config } = useNuxtApp();
    const vapidKey = $config.public.firebaseVapidKey;
    if (!vapidKey) return;

    const registration = await navigator.serviceWorker.ready;
    const token = await getToken($messaging, {
      vapidKey,
      serviceWorkerRegistration: registration,
    });

    if (!token) return;

    const stored = localStorage.getItem("push-token");
    if (stored === token) return;

    await registerPushToken({ token, platform: "WEB" });
    localStorage.setItem("push-token", token);
    sessionStorage.setItem("push-registered", "true");
  }

  onMounted(() => {
    if (!("Notification" in window)) return;
    isPermissionGranted.value = Notification.permission === "granted";

    if (isPermissionGranted.value) {
      registerPushTokenIfPossible();
    }
  });

  return {
    isPermissionGranted,
    isRequestingPermission,
    requestPermissionForNotifications,
    registerPushTokenIfPossible,
  };
};
