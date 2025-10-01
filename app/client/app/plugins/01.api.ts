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

      const accessToken = useCookie("access_token").value;

      if (accessToken) {
        options.headers.set("Authorization", `Bearer ${accessToken}`);
      }
    },

    onResponse({ response }) {
      if (nuxtApp.$config.public.nodeEnv === "development") {
        console.debug(`[API] Response:`, response);
      }
    },

    async onResponseError({ response, request, options }): Promise<any> {
      if (nuxtApp.$config.public.nodeEnv === "development") {
        console.error(`[API] Error:`, response.status, response._data);
      }

      if (
        response.status === 401 &&
        !request.toString().includes("auth") &&
        !options.retry
      ) {
        options.retry = 1;

        try {
          const newAccessToken = await refreshAccessToken();

          return await api(request.toString(), {
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

      const { id: toastId } = toast.add({
        title: t("toast.error.title"),
        description: t(
          `toast.error.description.${response.status}`,
          t("toast.error.description.default")
        ),
        color: "error",
      });
      const store = useToastStore();
      store.setToastId(toastId);
      store.setStatus(response.status);
    },
  });

  return {
    provide: {
      api,
    },
  };
});
