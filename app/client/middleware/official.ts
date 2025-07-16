export default defineNuxtRouteMiddleware((to, from) => {
  const { user } = useUserStore();
  const toast = useToast();

  console.log(user);

  if (!user || user.userType !== "OFFICIAL") {
    toast.add({
      title: "Forbidden",
      description: "You don't have permission to access this resource.",
      color: "error",
    });

    return navigateTo(to.path === from.path ? "/login" : from.path);
  }
});
