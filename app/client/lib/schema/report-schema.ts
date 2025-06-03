import * as z from "zod";

export const reportSchema = z.object({
  description: z.string().min(1, "Description is required"),
  images: z
    .custom<File[]>()
    .refine(
      (files) => files.length <= 5,
      "You can upload a maximum of 5 images"
    )
    .refine(
      (files) => files.every((file) => file.type.startsWith("image/")),
      "All files must be images"
    )
    .refine(
      (files) => files.every((file) => file.size <= MAX_FILE_SIZE_IN_BYTES),
      `Each image must have at most ${MAX_FILE_SIZE_IN_MB} mb`
    ),
  location: z.string(),
});

export type ReportSchema = z.output<typeof reportSchema>;
