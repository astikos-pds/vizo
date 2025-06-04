export default defineNuxtRouteMiddleware(async (to, from) => {
  const { ensureAuthenticated, accessToken, refreshToken } = useAuth();

  console.log(accessToken, refreshToken);
  const ok = await ensureAuthenticated();
  console.log(ok, accessToken, refreshToken);
  if (!ok) {
    return navigateTo("/login");
  }
});
