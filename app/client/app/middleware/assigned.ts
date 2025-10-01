export default defineNuxtRouteMiddleware(async (to, from) => {
  const { currentAffiliation, ensureUserIsAssignedTo, setCurrentAssignment } =
    useLoggedInUserStore();
  const toast = useToast();

  if (!currentAffiliation) {
    return navigateTo("/affiliations");
  }

  const departmentId = to.params.departmentId as string;

  if (!departmentId)
    return navigateTo(
      `/municipalities/${currentAffiliation.municipality.id}/departments`
    );

  const assignedUser = ensureUserIsAssignedTo(departmentId);

  if (!assignedUser) {
    toast.clear();

    toast.add({
      title: "Forbidden",
      description: "You are not assigned to this department",
      color: "error",
    });

    return navigateTo(
      `/municipalities/${currentAffiliation.municipality.id}/departments`
    );
  }

  setCurrentAssignment(assignedUser);
});
