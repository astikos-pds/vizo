export default defineNuxtPlugin(async (nuxtApp) => {
  const L = await import("leaflet");
  return {
    provide: {
      leafet: L,
    },
  };
});
