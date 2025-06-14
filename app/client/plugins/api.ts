export default defineNuxtPlugin((nuxtApp) => {
  const toast = useToast();
  const t = nuxtApp.vueApp.config.globalProperties.$t;

  const refreshAccessToken = async () => {
    try {
      const refreshToken = useCookie("refresh_token").value;
      if (!refreshToken) throw new Error("No refresh token");

      const { refresh } = useAuth();
      await refresh({ token: refreshToken });
      return useCookie("access_token").value;
    } catch (error) {
      useCookie("access_token").value = null;
      useCookie("refresh_token").value = null;
      throw error;
    }
  };

  const api = $fetch.create({
    baseURL: nuxtApp.$config.public.apiBaseUrl,
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
        try {
          const newAccessToken = await refreshAccessToken();

          return await $fetch(request, {
            method: options.method as any,
            headers: {
              ...options.headers,
              Authorization: `Bearer ${newAccessToken}`,
            },
          });
        } catch (error) {
          await nuxtApp.runWithContext(() => navigateTo("/login"));
        }
      }

      toast.add({
        title: t("toast.error.title"),
        description: t(
          `toast.error.description.${response.status}`,
          t("toast.error.description.default")
        ),
        color: "error",
      });
    },
  });

  return {
    provide: {
      api,
    },
  };
});
