export default defineNuxtRouteMiddleware((to, from) => {
  const { accessToken } = useAuth();

  console.log(accessToken.value);

  if (!accessToken.value) {
    return navigateTo("/login");
  }
});
