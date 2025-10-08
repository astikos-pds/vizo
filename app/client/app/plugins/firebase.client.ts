import { initializeApp, getApps } from "firebase/app";
import { getMessaging, onMessage, type Messaging } from "firebase/messaging";

declare module "#app" {
  interface NuxtApp {
    $messaging: Messaging;
  }
}

export default defineNuxtPlugin(async (nuxtApp) => {
  const config = nuxtApp.$config.public;

  const firebaseConfig = {
    apiKey: config.firebaseApiKey,
    authDomain: config.firebaseAuthDomain,
    projectId: config.firebaseProjectId,
    storageBucket: config.firebaseStorageBucket,
    messagingSenderId: config.firebaseMessagingSenderId,
    appId: config.firebaseAppId,
  };

  const app = getApps()[0] ?? initializeApp(firebaseConfig);

  const messaging = getMessaging(app);

  try {
    const registration = await navigator.serviceWorker.register(
      "/firebase-messaging-sw.js"
    );

    await navigator.serviceWorker.ready;

    console.info("[Firebase] Service Worker registered:", registration.scope);
  } catch (err) {
    if (config.nodeEnv === "development") {
      console.error(
        "[Firebase] Error during Service Worker registration:",
        err
      );
    }
  }

  const toast = useToast();

  onMessage(messaging, (payload) => {
    if (!payload.notification) return;

    toast.add({
      title: payload.notification.title,
      description: payload.notification.body,
      color: "primary",
      icon: "i-lucide-megaphone",
    });
  });

  return {
    provide: {
      messaging,
    },
  };
});
