import tailwindcss from "@tailwindcss/vite";

// https://nuxt.com/docs/api/configuration/nuxt-config
export default defineNuxtConfig({
  compatibilityDate: "2024-11-01",
  devtools: { enabled: true },
  ssr: true,
  nitro: {
    preset: "vercel",
  },
  runtimeConfig: {
    public: {
      apiBaseUrl: process.env.NUXT_API_BASE_URL || "http://localhost:8080",
      cloudinaryName: process.env.NUXT_CLOUDINARY_NAME || "",
    },
  },
  css: ["~/assets/css/main.css"],
  vite: {
    plugins: [tailwindcss()],
  },
  plugins: ["~/plugins/vue-the-mask.ts", "~/plugins/api.ts"],
  modules: [
    "@nuxtjs/leaflet",
    "@nuxt/icon",
    "@vueuse/nuxt",
    "@nuxt/ui",
    "@nuxtjs/i18n",
    "@vite-pwa/nuxt",
  ],
  i18n: {
    defaultLocale: "pt-BR",
    strategy: "no_prefix",
    lazy: true,
    locales: [
      { code: "pt-BR", name: "Português (Brasil)", file: "pt-BR.json" },
      { code: "en", name: "English", file: "en.json" },
    ],
  },
  app: {
    head: {
      link: [
        {
          rel: "apple-touch-icon",
          href: "/apple-touch-icon.png",
          sizes: "180x180",
        },
      ],
    },
  },
  pwa: {
    registerType: "autoUpdate",
    manifest: {
      name: "Vizo",
      short_name: "Vizo",
      description: "Collaborative mapping of urban problems",
      background_color: "#ffffff",
      theme_color: "#0003ff",
      display: "standalone",
      start_url: "/",
      icons: [
        {
          src: "/pwa-192x192.png",
          sizes: "192x192",
          type: "image/png",
        },
        {
          src: "/pwa-512x512.png",
          sizes: "512x512",
          type: "image/png",
        },
      ],
    },
    workbox: {
      cleanupOutdatedCaches: true,
      runtimeCaching: [
        {
          urlPattern: process.env.NUXT_API_BASE_URL || "http://localhost:8080",
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
