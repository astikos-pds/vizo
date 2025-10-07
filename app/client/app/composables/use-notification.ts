import type { Notification } from "~/types/domain/notification/notification";

export const useNotification = () => {
  const { $notificationService } = useNuxtApp();
  const { handle, loading } = useApiHandler();

  function markNotificationAsRead(id: Notification["id"]) {
    return handle(() => $notificationService.markNotificationAsRead(id));
  }

  return {
    loading,
    markNotificationAsRead,
  };
};
