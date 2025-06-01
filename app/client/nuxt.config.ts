import tailwindcss from "@tailwindcss/vite";

// https://nuxt.com/docs/api/configuration/nuxt-config
export default defineNuxtConfig({
  compatibilityDate: "2024-11-01",
  devtools: { enabled: true },
  css: ["~/assets/css/main.css"],
  vite: {
    plugins: [tailwindcss()],
  },
  plugins: ["~/plugins/vue-the-mask.ts"],
  modules: ["@nuxtjs/leaflet", "@nuxt/icon", "@vueuse/nuxt", "@nuxt/ui"],
});
