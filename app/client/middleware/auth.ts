export default defineNuxtRouteMiddleware(async (to, from) => {
  const { accessToken } = useAuth();

  // if (!accessToken.value) {
  //   return navigateTo("/login");
  // }
});
