interface QueryOptions {
  method: "GET" | "POST" | "PUT" | "PATCH" | "DELETE";
}

export const useQuery = <T>(url: string, options?: QueryOptions) => {
  const { ensureAuthenticated } = useAuth();
  const ok = ensureAuthenticated();
  if (!ok) {
    return navigateTo("/login");
  }

  const config = useRuntimeConfig();
  const accessToken = useCookie("access_token");

  return useFetch<T>(`${config.public.apiBaseUrl}/${url}`, {
    method: options ? (options.method.toLowerCase() as any) : "GET",
    headers: {
      Authorization: `Bearer ${accessToken.value}`,
    },
  });
};
