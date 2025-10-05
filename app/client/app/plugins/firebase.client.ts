import { initializeApp, getApps } from "firebase/app";
import { getMessaging, onMessage, type Messaging } from "firebase/messaging";

declare module "#app" {
  interface NuxtApp {
    $messaging: Messaging;
  }
}

export default defineNuxtPlugin((nuxtApp) => {
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

  const toast = useToast();

  onMessage(messaging, (payload) => {
    if (!payload.notification) return;

    toast.add({
      title: payload.notification.title,
      description: payload.notification.body,
      color: "primary",
    });
  });

  return {
    provide: {
      messaging,
    },
  };
});
