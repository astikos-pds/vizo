importScripts(
  "https://www.gstatic.com/firebasejs/9.22.2/firebase-app-compat.js"
);
importScripts(
  "https://www.gstatic.com/firebasejs/9.22.2/firebase-messaging-compat.js"
);

const app = firebase.initializeApp({
  apiKey: "AIzaSyDN9bOnrT0eSvCaNRY9jY85ivN2jYYaiDQ",
  authDomain: "astikos-vizo.firebaseapp.com",
  projectId: "astikos-vizo",
  storageBucket: "astikos-vizo.firebasestorage.app",
  messagingSenderId: "611810149761",
  appId: "1:611810149761:web:c52725f3faad6a72b51480",
  measurementId: "G-5VNZE0C6RB",
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
