import * as z from "zod";

export const passwordRequirements = {
  minLength: { regex: /.{8,}/, text: "register.passwordRequirements.length" },
  hasNumber: { regex: /\d/, text: "register.passwordRequirements.number" },
  hasLowercase: {
    regex: /[a-z]/,
    text: "register.passwordRequirements.lowercase",
  },
  hasUppercase: {
    regex: /[A-Z]/,
    text: "register.passwordRequirements.uppercase",
  },
};

export const passwordSchema = z
  .string()
  .min(8, passwordRequirements.minLength.text)
  .regex(
    passwordRequirements.hasNumber.regex,
    passwordRequirements.hasNumber.text
  )
  .regex(
    passwordRequirements.hasLowercase.regex,
    passwordRequirements.hasLowercase.text
  )
  .regex(
    passwordRequirements.hasUppercase.regex,
    passwordRequirements.hasUppercase.text
  );

export const registerSchema = z
  .object({
    firstName: z.string().min(1, "Name is required"),
    lastName: z.string().optional(),
    cpf: z
      .string()
      .min(1, "CPF is required")
      .refine((cpf) => {
        const result = validateDocument(cpf);
        return result.isValid && result.type === "cpf";
      }, "Invalid CPF"),
    email: z.string().email("Invalid email"),
    birthDate: z
      .string()
      .regex(/^\d{4}-\d{2}-\d{2}$/, "Invalid date")
      .refine((date) => {
        const birthDate = new Date(date);
        const today = new Date();
        const minAgeDate = new Date(
          today.getFullYear() - MIN_AGE,
          today.getMonth(),
          today.getDate()
        );
        return birthDate <= minAgeDate;
      }, `You must be at least ${MIN_AGE} years old`),
    password: passwordSchema,
    confirmedPassword: z.string(),
  })
  .refine((fields) => fields.confirmedPassword === fields.password, {
    message: "Passwords don't match",
    path: ["confirmedPassword"],
  });

export type RegisterSchema = z.output<typeof registerSchema>;
