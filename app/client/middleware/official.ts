export default defineNuxtRouteMiddleware((to, from) => {
  const { user } = useUserStore();

  if (!user || user.userType !== "OFFICIAL") {
    return navigateTo(to.path === from.path ? "/" : "/login");
  }
});
