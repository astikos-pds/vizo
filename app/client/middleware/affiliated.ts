export default defineNuxtRouteMiddleware(async (to, from) => {
  const { ensureUserIsAffiliatedTo, setCurrentAffiliation } =
    useLoggedInUserStore();
  const toast = useToast();

  const municipalityId = to.params.municipalityId as string;

  if (!municipalityId) return navigateTo("/affiliations");

  const affiliatedUser = ensureUserIsAffiliatedTo(municipalityId);

  if (!affiliatedUser) {
    toast.add({
      title: "Forbidden",
      description: "You are not affiliated to this municipality",
      color: "error",
    });
    return navigateTo("/affiliations");
  }

  setCurrentAffiliation(affiliatedUser);
});
