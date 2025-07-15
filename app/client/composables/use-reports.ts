import {
  getReportsUseCase,
  reportUseCase,
  type GetReportsParams,
  type GetReportsResponse,
} from "~/services/report";
import type { Report } from "~/types/domain";
import { useImage } from "~/composables/use-image";

export const useReports = () => {
  const { loading, handle } = useApiHandler();
  const { uploadImage } = useImage();

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

  async function getReports(
    params: GetReportsParams
  ): Promise<GetReportsResponse | null> {
    return await handle<GetReportsResponse>(() => getReportsUseCase(params));
  }

  return { loading, report, getReports };
};
