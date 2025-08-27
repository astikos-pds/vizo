import type { User, UserDTO, UserMapper } from "./user";

export interface PointOfInterestDTO {
  id: string;
  user: UserDTO;
  name: string;
  latitude: number;
  longitude: number;
  radius: number;
  createdAt: string;
  updatedAt: string;
}

export interface PointOfInterest {
  id: string;
  user: User;
  name: string;
  latitude: number;
  longitude: number;
  radius: number;
  createdAt: Date;
  updatedAt: Date;
}

export class PointOfInterestMapper {
  constructor(private readonly userMapper: UserMapper) {}

  public toModel(dto: PointOfInterestDTO): PointOfInterest {
    return {
      ...dto,
      user: this.userMapper.toModel(dto.user),
      createdAt: new Date(dto.createdAt),
      updatedAt: new Date(dto.updatedAt),
    };
  }
}
