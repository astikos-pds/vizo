export interface ExistsUserParams {
  email: string;
  document?: string;
}

export class UserService {
  constructor(private readonly httpClient: HttpClient) {}

  public async existsUserByParams(params: ExistsUserParams) {
    return await this.httpClient.get<boolean>("/users/exists", params);
  }
}
