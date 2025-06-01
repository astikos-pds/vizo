import z from "zod";

export const loginSchema = z.object({
  document: z
    .string()
    .min(1, "Document required")
    .refine(
      (document) => validateDocument(document).isValid,
      "Invalid document"
    ),
  password: z.string().min(1, "Password required"),
});

export type LoginSchema = z.infer<typeof loginSchema>;
