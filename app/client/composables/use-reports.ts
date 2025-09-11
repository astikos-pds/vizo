import { useImageUpload } from "~/composables/use-image-upload";
import type { ProblemType } from "~/types/domain/problem";

export const useReports = () => {
  const { loading, handle } = useApiHandler();
  const { uploadImage } = useImageUpload();

  const { $reportService } = useNuxtApp();

  interface RawReportRequest {
    description: string;
    images: File[];
    latitude: number;
    longitude: number;
    problemType: ProblemType;
  }
  async function report(request: RawReportRequest) {
    const imagesUrls = await Promise.all(
      request.images.map((image) => uploadImage({ file: image }))
    );

    return await handle(() =>
      $reportService.reportProblem({
        imagesUrls: imagesUrls.filter((imageUrl) => imageUrl !== ""),
        ...request,
      })
    );
  }

  return { loading, report };
};
