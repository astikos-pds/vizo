import type { UserDTO, UserMapper } from "~/types/domain/user";

export interface GetUserWithFiltersParams {
  email: string;
  document?: string;
}

export class UserService {
  constructor(
    private readonly httpClient: HttpClient,
    private readonly userMapper: UserMapper
  ) {}

  public async getUserWithFilters(params: GetUserWithFiltersParams) {
    const response = await this.httpClient.get<UserDTO>("/users", params);

    return this.userMapper.toModel(response);
  }
}
