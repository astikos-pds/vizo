import type {
  Notification,
  NotificationDTO,
  NotificationMapper,
} from "~/types/domain/notification/notification";

export class NotificationService {
  public constructor(
    private readonly httpClient: HttpClient,
    private readonly notificatonMapper: NotificationMapper
  ) {}

  public async markNotificationAsRead(id: Notification["id"]) {
    const response = await this.httpClient.patch<NotificationDTO>(
      `/notifications/${id}/read`
    );

    return this.notificatonMapper.toModel(response);
  }
}
