<script lang="ts" setup>
import type { FormSubmitEvent } from "@nuxt/ui";
import z from "zod";
import type { PasswordRequirements } from "~/types/domain";
import { isDocumentValid } from "~/utils/document-validation";
import { vMaska } from "maska/vue";

const { t } = useI18n();

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
      .refine(
        (cpf) => isDocumentValid(cpf),
        t("register.verification.invalidCpf")
      ),
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

const store = useEmailStore();

const form = reactive<RegisterSchema>({
  firstName: "",
  lastName: "",
  cpf: "",
  email: store.email ? store.email : "",
  birthDate: "",
  password: "",
  confirmedPassword: "",
});
const showPassword = ref<boolean>(false);
const showConfimedPassword = ref<boolean>(false);

const { loading, register } = useAuth();

const toast = useToast();

const onSubmit = async (event: FormSubmitEvent<RegisterSchema>) => {
  const name = `${event.data.firstName} ${event.data.lastName}`.trim();

  const ok = await register({
    name,
    document: event.data.cpf,
    email: event.data.email,
    password: event.data.password,
  });

  if (!ok) return;

  toast.add({
    title: t("toast.success.title"),
    description: t("toast.success.description.signedUp"),
    color: "success",
  });

  await navigateTo("/login");
};

const stepper = useSteps();
</script>

<template>
  <RegisterStep
    title="Complete your registration"
    description="Fill your personal details in the form below."
  >
    <UForm
      :state="form"
      :schema="registerSchema"
      @submit="onSubmit"
      :disabled="loading"
      class="flex flex-col items-center gap-4 md:gap-3"
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
        <UButtonGroup class="w-full">
          <UInput
            icon="i-lucide-at-sign"
            v-model="form.email"
            type="email"
            disabled
            :placeholder="t('register.emailPlaceholder')"
            class="w-full text-xl"
          />

          <UButton
            color="neutral"
            variant="outline"
            icon="i-lucide-pencil"
            @click="stepper.goToStart"
          />
        </UButtonGroup>
      </UFormField>

      <div class="flex flex-col md:flex-row gap-2 2xl:gap-3 w-full">
        <UFormField label="CPF" name="cpf" class="w-full" required>
          <UInput
            icon="i-lucide-id-card"
            v-model="form.cpf"
            type="text"
            :placeholder="t('register.cpfPlaceholder')"
            v-maska="CPF_MASK"
            class="w-full text-xl"
          />
        </UFormField>
        <UFormField
          :label="t('register.birthDate')"
          name="birthDate"
          class="w-full"
          required
        >
          <UInput v-model="form.birthDate" type="date" class="w-full text-xl" />
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
        <RegisterPasswordStrength
          :password="form.password"
          :password-requirements="passwordRequirements"
        />
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

      <UButton type="submit" :loading="loading">{{
        t("register.signUpButton")
      }}</UButton>
    </UForm>
  </RegisterStep>
</template>
