export default defineNuxtRouteMiddleware(async (to, from) => {
  const { ensureAuthenticated } = useAuth();

  const ok = await ensureAuthenticated();
  if (!ok) {
    return navigateTo("/login");
  }
});
