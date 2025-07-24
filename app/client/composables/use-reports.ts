import type { Problem, ProblemType, Report } from "~/types/domain";
import { useImage } from "~/composables/use-image";
import {
  createReportRepository,
  type GetReportsByProblemIdParams,
  type GetReportsParams,
} from "~/repositories/report";

export const useReports = () => {
  const { loading, handle } = useApiHandler();
  const { uploadImage } = useImage();

  const { $api } = useNuxtApp();
  const reportRepository = createReportRepository($api);

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
      reportRepository.create({
        imagesUrls: imagesUrls.filter((imageUrl) => imageUrl !== ""),
        ...request,
      })
    );
  }

  function getReports(params: GetReportsParams) {
    return useAsyncData("reports", () => reportRepository.findAll(params));
  }

  function getReportsByProblemId(
    problemId: Problem["id"],
    params?: GetReportsByProblemIdParams
  ) {
    return useAsyncData(`problems-${problemId}-reports`, () =>
      reportRepository.findAllByProblemId(problemId, params)
    );
  }

  return { loading, report, getReports, getReportsByProblemId };
};
