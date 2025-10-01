export default defineNuxtRouteMiddleware((to, from) => {
  const { currentAffiliation } = useLoggedInUserStore();
  const toast = useToast();

  if (!currentAffiliation) {
    toast.add({
      title: "Forbidden",
      description: "You are not affiliated to this municipality",
      color: "error",
    });
    return navigateTo("/affiliations");
  }

  const municipalityId = to.params.municipalityId as string;

  if (!currentAffiliation.isAdmin) {
    toast.add({
      title: "Forbidden",
      description:
        "You cannot access this resource as a commom user in this municipality",
      color: "error",
    });
    return navigateTo(`/municipalities/${municipalityId}`);
  }
});
