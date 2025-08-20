<script lang="ts" setup>
import type { FormSubmitEvent } from "@nuxt/ui";
import * as z from "zod";
import { validateDocument } from "~/utils/document-validation";
import { MIN_AGE } from "~/utils/constants";
import { useAuth } from "~/composables/use-auth";
import type { PasswordRequirements } from "~/types/domain";

const { t } = useI18n();

useHead({
  title: t("head.registerCitizen.title"),
  meta: [
    { name: "description", content: t("head.registerCitizen.description") },
  ],
});

definePageMeta({
  layout: "guest",
});

const passwordRequirements = computed<PasswordRequirements>(() => {
  return {
    minLength: {
      regex: /.{8,}/,
      text: t("register.passwordRequirements.length"),
    },
    hasNumber: {
      regex: /\d/,
      text: t("register.passwordRequirements.number"),
    },
    hasLowercase: {
      regex: /[a-z]/,
      text: t("register.passwordRequirements.lowercase"),
    },
    hasUppercase: {
      regex: /[A-Z]/,
      text: t("register.passwordRequirements.uppercase"),
    },
  };
});

const passwordSchema = z
  .string()
  .min(8, passwordRequirements.value.minLength.text)
  .regex(
    passwordRequirements.value.hasNumber.regex,
    passwordRequirements.value.hasNumber.text
  )
  .regex(
    passwordRequirements.value.hasLowercase.regex,
    passwordRequirements.value.hasLowercase.text
  )
  .regex(
    passwordRequirements.value.hasUppercase.regex,
    passwordRequirements.value.hasUppercase.text
  );

const registerSchema = z
  .object({
    firstName: z.string().min(1, t("register.verification.name")),
    lastName: z.string().optional(),
    cpf: z
      .string()
      .min(1, t("register.verification.cpf"))
      .refine((cpf) => {
        const result = validateDocument(cpf);
        return result.isValid && result.type === "cpf";
      }, t("register.verification.invalidCpf")),
    email: z.string().email(t("register.verification.email")),
    birthDate: z
      .string()
      .regex(/^\d{4}-\d{2}-\d{2}$/, t("register.verification.date"))
      .refine((date) => {
        const birthDate = new Date(date);
        const today = new Date();
        const minAgeDate = new Date(
          today.getFullYear() - MIN_AGE,
          today.getMonth(),
          today.getDate()
        );
        return birthDate <= minAgeDate;
      }, t("register.verification.minAge", { age: MIN_AGE })),
    password: passwordSchema,
    confirmedPassword: z.string(),
  })
  .refine((fields) => fields.confirmedPassword === fields.password, {
    message: t("register.verification.confirmedPassword"),
    path: ["confirmedPassword"],
  });

type RegisterSchema = z.output<typeof registerSchema>;

const form = reactive<RegisterSchema>({
  firstName: "",
  lastName: "",
  cpf: "",
  email: "",
  birthDate: "",
  password: "",
  confirmedPassword: "",
});
const showPassword = ref<boolean>(false);
const showConfimedPassword = ref<boolean>(false);

const { loading, registerAsCitizen } = useAuth();

const toast = useToast();
const onSubmit = async (event: FormSubmitEvent<RegisterSchema>) => {
  const ok = await registerAsCitizen({
    name: `${event.data.firstName} ${event.data.lastName}`.trim(),
    document: event.data.cpf,
    email: event.data.email,
    password: event.data.password,
  });

  if (ok!) return;
  
  toast.add({
    title: t("toast.success.title"),
    description: t("toast.success.description.signedUp"),
    color: "success",
  });

  await navigateTo("/login");
};
</script>

<template>
  <section class="relative size-full flex flex-col items-center">
    <ConfigHeader class="w-full" />
    <section class="size-full flex flex-col items-center overflow-y-auto">
      <section class="w-[70%] md:w-[50%] lg:w-[65%] 2xl:w-[50%] my-20">
        <h1 class="text-4xl font-semibold text-center">
          {{ t("registerCitizen.title") }}
        </h1>

        <UForm
          :state="form"
          :schema="registerSchema"
          @submit="onSubmit"
          :disabled="loading"
          class="flex flex-col items-center gap-4 md:gap-3 mt-8"
        >
          <div class="flex flex-col md:flex-row gap-2 2xl:gap-3 w-full">
            <UFormField
              :label="t('register.firstName')"
              name="firstName"
              class="w-full"
              required
            >
              <UInput
                icon="i-lucide-user"
                v-model="form.firstName"
                type="text"
                :placeholder="t('register.firstNamePlaceholder')"
                class="w-full text-xl"
              />
            </UFormField>

            <UFormField
              :label="t('register.lastName')"
              name="lastName"
              class="w-full"
              :hint="t('register.optional')"
            >
              <UInput
                v-model="form.lastName"
                type="text"
                :placeholder="t('register.lastNamePlaceholder')"
                class="w-full text-xl"
              />
            </UFormField>
          </div>

          <UFormField label="E-mail" name="email" class="w-full" required>
            <UInput
              icon="i-lucide-at-sign"
              v-model="form.email"
              type="email"
              :placeholder="t('register.emailPlaceholder')"
              class="w-full text-xl"
            />
          </UFormField>

          <div class="flex flex-col md:flex-row gap-2 2xl:gap-3 w-full">
            <UFormField label="CPF" name="cpf" class="w-full" required>
              <UInput
                icon="i-lucide-id-card"
                v-model="form.cpf"
                type="text"
                :placeholder="t('register.cpfPlaceholder')"
                class="w-full text-xl"
              />
            </UFormField>
            <UFormField
              :label="t('register.birthDate')"
              name="birthDate"
              class="w-full"
              required
            >
              <UInput
                v-model="form.birthDate"
                type="date"
                class="w-full text-xl"
              />
            </UFormField>
          </div>

          <PasswordInput
            v-model="form.password"
            color="neutral"
            :show="showPassword"
            :label="t('register.password')"
            name="password"
            :placeholder="t('register.passwordPlaceholder')"
            required
            @click="showPassword = !showPassword"
          >
            <RegisterPasswordStrength :password="form.password" :password-requirements="passwordRequirements" />
          </PasswordInput>

          <PasswordInput
            v-model="form.confirmedPassword"
            color="neutral"
            :show="showConfimedPassword"
            :label="t('register.confirmPassword')"
            name="confirmedPassword"
            :placeholder="t('register.confirmPasswordPlaceholder')"
            required
            @click="showConfimedPassword = !showConfimedPassword"
          />

          <div class="text-center text-sm">
            <span
              >{{ t("register.alreadyHaveAccount") }}
              <NuxtLink to="/login" class="text-primary">{{
                t("register.logInHere")
              }}</NuxtLink></span
            >
          </div>

          <UButton type="submit" :loading="loading">{{
            t("register.signUpButton")
          }}</UButton>
        </UForm>
      </section>
    </section>
  </section>
  <section
    class="lg:min-w-[45%] xl:min-w-[50%] h-full bg-linear-to-tr from-primary to-neutral-200 dark:to-neutral-500"
  ></section>
</template>
