import { reportUseCase } from "~/services/report";
import {
  uploadImageUseCase,
  type UploadImageRequest,
  type UploadImageResponse,
} from "~/services/upload";
import type { Report } from "~/types/domain";

export const useReports = () => {
  const { loading, handle } = useApiHandler();

  async function uploadImage(request: UploadImageRequest): Promise<string> {
    const response = await handle<UploadImageResponse>(() =>
      uploadImageUseCase(request)
    );

    return response?.secure_url ?? "";
  }

  interface RawReportRequest {
    description: string;
    images: File[];
    latitude: number;
    longitude: number;
  }
  async function report(request: RawReportRequest): Promise<Report | null> {
    const imagesUrls = await Promise.all(
      request.images.map((image) => uploadImage({ file: image }))
    );

    return await handle<Report>(() =>
      reportUseCase({
        description: request.description,
        imagesUrls: imagesUrls.filter((imageUrl) => imageUrl !== ""),
        latitude: request.latitude,
        longitude: request.longitude,
      })
    );
  }

  return { loading, report };
};
