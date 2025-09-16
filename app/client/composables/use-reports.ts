import { useImageUpload } from "~/composables/use-image-upload";
import type { ProblemType } from "~/types/domain/problem";
import type { Report } from "~/types/domain/report";

export const useReports = () => {
  const { loading, handle } = useApiHandler();
  const { uploadImage } = useImageUpload();

  const { $reportService } = useNuxtApp();

  interface RawReportRequest {
    description: string;
    images: File[];
    latitude: number;
    longitude: number;
  }

  async function getReport(id: Report["id"]) {
    return useAsyncData(`reports-${id}`, () => $reportService.getReport(id));
  }

  async function report(request: RawReportRequest) {
    const imagesUrls = await uploadImages(request.images);

    return await handle(() =>
      $reportService.reportProblem({
        imagesUrls,
        ...request,
      })
    );
  }

  async function updateReport(id: Report["id"], request: RawReportRequest) {
    const imagesUrls = await uploadImages(request.images);

    return await handle(() =>
      $reportService.updateReport(id, {
        imagesUrls,
        ...request,
      })
    );
  }

  async function uploadImages(images: File[]) {
    const response = await Promise.all(
      images.map((image) => uploadImage({ file: image }))
    );

    return response.filter((imageUrl) => imageUrl !== "");
  }

  async function deleteReport(id: Report["id"]) {
    await handle(() => $reportService.deleteReport(id));
  }

  return { loading, getReport, report, updateReport, deleteReport };
};
