export default defineNuxtPlugin((nuxtApp) => {
  const config = useRuntimeConfig();

  const api = $fetch.create({
    baseURL: config.public.apiBaseUrl,
    headers: {
      "Content-Type": "application/json",
    },
    onRequest({ request, options }) {
      console.debug(`[API] Request: ${request}`, options);

      if (request.toString().includes("auth")) return;

      const accessToken = useCookie("access_token").value;

      if (accessToken) {
        options.headers.set("Authorization", `Bearer ${accessToken}`);
      }
    },

    onResponse({ response }) {
      console.debug(`[API] Response:`, response);
    },

    async onResponseError({ response, request, options }) {
      console.error(`[API] Error:`, response.status, response._data);

      if (response.status === 401 && !request.toString().includes("auth")) {
        const refreshToken = useCookie("refresh_token").value;

        if (!refreshToken) {
          await nuxtApp.runWithContext(() => navigateTo("/login"));
        }

        try {
          const { refresh } = useAuth();

          await refresh({ token: refreshToken ?? "" });

          const accessToken = useCookie("access_token").value;

          return await $fetch(request, {
            method: options.method as any,
            headers: {
              ...options.headers,
              Authorization: `Bearer ${accessToken}`,
            },
          });
        } catch (error) {
          await nuxtApp.runWithContext(() => navigateTo("/login"));
        }
      }
    },
  });

  return {
    provide: {
      api,
    },
  };
});
