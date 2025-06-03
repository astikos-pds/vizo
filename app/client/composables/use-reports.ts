import {
  reportUseCase,
  type ReportRequest,
  type ReportResponse,
} from "~/services/report";
import {
  uploadImageUseCase,
  type UploadImageRequest,
  type UploadImageResponse,
} from "~/services/upload";

export const useReports = () => {
  const { loading, error, handle } = useApiHandler();

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
  async function report(
    request: RawReportRequest
  ): Promise<ReportResponse | null> {
    const imagesUrls = await Promise.all(
      request.images.map((image) => uploadImage({ file: image }))
    );

    return await handle<ReportResponse>(() =>
      reportUseCase({
        description: request.description,
        imagesUrls: imagesUrls.filter((imageUrl) => imageUrl !== ""),
        latitude: request.latitude,
        longitude: request.longitude,
      })
    );
  }

  async function getReportsFromProblem(problemId: string) {
    useFetch("", {
      method: "GET",
    });
  }

  return { loading, error, report };
};
