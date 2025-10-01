import tailwindcss from "@tailwindcss/vite";

// https://nuxt.com/docs/api/configuration/nuxt-config
export default defineNuxtConfig({
  compatibilityDate: "2024-11-01",
  devtools: { enabled: true },
  ssr: process.env.NUXT_PUBLIC_NODE_ENV === "production",
  runtimeConfig: {
    public: {
      apiBaseUrl:
        process.env.NUXT_PUBLIC_API_BASE_URL ?? "http://localhost:8080",
      cloudinaryName: process.env.NUXT_PUBLIC_CLOUDINARY_NAME ?? "",
      nodeEnv: process.env.NUXT_PUBLIC_NODE_ENV ?? "development",
    },
  },
  css: ["~/assets/css/main.css"],
  vite: {
    plugins: [tailwindcss()],
  },
  plugins: [
    "~/plugins/01.api.ts",
    "~/plugins/02.services.ts",
    "~/plugins/leaflet.client.ts",
    "~/plugins/apexcharts.client.ts",
  ],
  modules: [
    "@nuxtjs/leaflet",
    "@nuxt/icon",
    "@vueuse/nuxt",
    "@nuxt/ui",
    "@nuxtjs/i18n",
    "@vite-pwa/nuxt",
    "@nuxt/image",
    "@pinia/nuxt",
    "pinia-plugin-persistedstate/nuxt",
  ],
  icon: {
    serverBundle: {
      collections: ["lucide", "flagpack"],
    },
  },
  i18n: {
    defaultLocale: "pt-BR",
    strategy: "no_prefix",
    bundle: {
      optimizeTranslationDirective: false,
    },
    lazy: true,
    locales: [
      { code: "pt-BR", name: "Português (Brasil)", file: "pt-BR.json" },
      { code: "en", name: "English", file: "en.json" },
    ],
    detectBrowserLanguage: {
      useCookie: true,
      fallbackLocale: "pt-BR",
      cookieSecure: true,
    },
  },
  app: {
    head: {
      link: [
        {
          rel: "icon",
          type: "image/x-icon",
          href: "favicon.svg",
        },
        {
          rel: "apple-touch-icon",
          href: "apple-touch-icon.png",
          sizes: "180x180",
        },
      ],
      meta: [
        { name: "theme-color", content: "#0003ff" },
        { name: "mobile-web-app-capable", content: "yes" },
        { name: "apple-mobile-web-app-capable", content: "yes" },
        {
          name: "apple-mobile-web-app-status-bar-style",
          content: "black-translucent",
        },
      ],
    },
  },
  pwa: {
    client: {
      installPrompt: true,
    },
    devOptions: {
      enabled: false,
    },
    strategies: "generateSW",
    registerType: "autoUpdate",
    manifest: {
      name: "Vizo",
      short_name: "Vizo",
      description: "Collaborative mapping of urban problems",
      background_color: "#fafafa",
      theme_color: "#0003ff",
      display: "standalone",
      start_url: "/",
      icons: [
        {
          src: "icons/icon-48x48.png",
          sizes: "48x48",
          type: "image/png",
        },
        {
          src: "icons/icon-72x72.png",
          sizes: "72x72",
          type: "image/png",
        },
        {
          src: "icons/icon-96x96.png",
          sizes: "96x96",
          type: "image/png",
        },
        {
          src: "icons/icon-128x128.png",
          sizes: "128x128",
          type: "image/png",
        },
        {
          src: "icons/icon-144x144.png",
          sizes: "144x144",
          type: "image/png",
        },
        {
          src: "icons/icon-152x152.png",
          sizes: "152x152",
          type: "image/png",
        },
        {
          src: "icons/icon-192x192.png",
          sizes: "192x192",
          type: "image/png",
        },
        {
          src: "icons/icon-256x256.png",
          sizes: "256x256",
          type: "image/png",
        },
        {
          src: "icons/icon-384x384.png",
          sizes: "384x384",
          type: "image/png",
        },
        {
          src: "icons/icon-512x512.png",
          sizes: "512x512",
          type: "image/png",
        },
      ],
    },
    workbox: {
      cleanupOutdatedCaches: true,
      globPatterns: ["**/*.{js,css,html,png,svg,ico}"],
      runtimeCaching: [
        {
          urlPattern: new RegExp(
            process.env.NUXT_API_BASE_URL || "http://localhost:8080"
          ),
          handler: "NetworkFirst",
          options: {
            cacheName: "api-cache",
            expiration: {
              maxEntries: 50,
              maxAgeSeconds: 300,
            },
          },
        },
      ],
    },
  },
});
