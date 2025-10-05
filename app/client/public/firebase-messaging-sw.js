importScripts(
  "https://www.gstatic.com/firebasejs/9.22.2/firebase-app-compat.js"
);
importScripts(
  "https://www.gstatic.com/firebasejs/9.22.2/firebase-messaging-compat.js"
);

const config = {
  firebaseApiKey: process.env.NUXT_PUBLIC_FIREBASE_API_KEY ?? "",
  firebaseAuthDomain: process.env.NUXT_PUBLIC_FIREBASE_AUTH_DOMAIN ?? "",
  firebaseProjectId: process.env.NUXT_PUBLIC_FIREBASE_PROJECT_ID ?? "",
  firebaseStorageBucket: process.env.NUXT_PUBLIC_FIREBASE_STORAGE_BUCKET ?? "",
  firebaseMessagingSenderId:
    process.env.NUXT_PUBLIC_FIREBASE_MESSAGING_SENDER_ID ?? "",
  firebaseAppId: process.env.NUXT_PUBLIC_FIREBASE_APP_ID ?? "",
  firebaseVapidKey: process.env.NUXT_PUBLIC_FIREBASE_VAPID_KEY ?? "",
};

const app = firebase.initializeApp({
  apiKey: config.firebaseApiKey,
  authDomain: config.firebaseAuthDomain,
  projectId: config.firebaseProjectId,
  storageBucket: config.firebaseStorageBucket,
  messagingSenderId: config.firebaseMessagingSenderId,
  appId: config.firebaseAppId,
});

const messaging = firebase.messaging();

messaging.onBackgroundMessage((payload) => {
  console.log("Received a bg message: ", payload);

  const title = payload.notification?.title || "Vizo";
  const options = {
    body: payload.notification?.body || "",
    icon: "/icons/icon-192x192.png",
  };

  self.registration.showNotification(title, options);
});
