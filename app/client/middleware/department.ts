export default defineNuxtRouteMiddleware((to, from) => {
  const { currentDepartment } = useDepartmentStore();
  const toast = useToast();

  if (!currentDepartment) {
    toast.add({
      title: "Forbidden",
      description: "You don't have permission to access this resource.",
      color: "error",
    });

    return navigateTo(from.path);
  }
});
