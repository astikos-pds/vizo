import z from "zod";

export type LocationOption = "CURRENT" | "POINT";

export const makeReportSchema = (t: (key: string, params?: any) => string) =>
  z.object({
    description: z
      .string()
      .min(1, t("reportProblem.validation.descriptionRequired")),
    images: z
      .custom<File[]>()
      .refine(
        (files) => files.length <= 5,
        t("reportProblem.validation.maxImages", { count: 5 })
      )
      .refine(
        (files) => files.every((file) => file.type.startsWith("image/")),
        t("reportProblem.validation.mustBeImages")
      )
      .refine(
        (files) => files.every((file) => file.size <= MAX_FILE_SIZE_IN_BYTES),
        t("reportProblem.validation.imageSize", { size: MAX_FILE_SIZE_IN_MB })
      ),
    location: z.custom<LocationOption>(),
  });

export type ReportSchema = z.output<ReturnType<typeof makeReportSchema>>;

export const makeUpdateReportSchema = (
  t: (key: string, params?: any) => string
) =>
  z.object({
    description: z
      .string()
      .min(1, t("reportProblem.validation.descriptionRequired")),
    images: z
      .custom<File[]>()
      .refine(
        (files) => files.length <= 5,
        t("reportProblem.validation.maxImages", { count: 5 })
      )
      .refine(
        (files) => files.every((file) => file.type.startsWith("image/")),
        t("reportProblem.validation.mustBeImages")
      )
      .refine(
        (files) => files.every((file) => file.size <= MAX_FILE_SIZE_IN_BYTES),
        t("reportProblem.validation.imageSize", { size: MAX_FILE_SIZE_IN_MB })
      ),
  });

export type UpdateReportSchema = z.output<
  ReturnType<typeof makeUpdateReportSchema>
>;
