import tailwindcss from "@tailwindcss/vite";

// https://nuxt.com/docs/api/configuration/nuxt-config
export default defineNuxtConfig({
  compatibilityDate: "2024-11-01",
  devtools: { enabled: true },
  runtimeConfig: {
    public: {
      apiBaseUrl: process.env.NUXT_API_BASE_URL || "http://localhost:8080",
    },
  },
  css: ["~/assets/css/main.css"],
  vite: {
    plugins: [tailwindcss()],
  },
  plugins: ["~/plugins/vue-the-mask.ts", "~/plugins/api.ts"],
  modules: ["@nuxtjs/leaflet", "@nuxt/icon", "@vueuse/nuxt", "@nuxt/ui"],
});
