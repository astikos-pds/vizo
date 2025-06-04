export default defineNuxtPlugin((nuxtApp) => {
  const config = useRuntimeConfig();

  const api = $fetch.create({
    baseURL: config.public.apiBaseUrl,
    headers: {
      "Content-Type": "application/json",
    },
    onRequest: async () => {
      const { ensureAuthenticated } = useAuth();
      const ok = await ensureAuthenticated();
      if (!ok) {
        await navigateTo("/login");
        throw new Error("User not authenticated");
      }
    },
  });

  return {
    provide: {
      api,
    },
  };
});
