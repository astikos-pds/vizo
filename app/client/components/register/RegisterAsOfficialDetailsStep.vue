<script lang="ts" setup>
import type { FormSubmitEvent } from "@nuxt/ui";
import z from "zod";

const { t } = useI18n();

const passwordRequirements = computed(() => {
  return {
    minLength: {
      regex: /.{8,}/,
      text: t("registerCitizen.passwordRequirements.length"),
    },
    hasNumber: {
      regex: /\d/,
      text: t("registerCitizen.passwordRequirements.number"),
    },
    hasLowercase: {
      regex: /[a-z]/,
      text: t("registerCitizen.passwordRequirements.lowercase"),
    },
    hasUppercase: {
      regex: /[A-Z]/,
      text: t("registerCitizen.passwordRequirements.uppercase"),
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

const detailsSchema = z
  .object({
    firstName: z.string().min(1, t("registerCitizen.verification.name")),
    lastName: z.string().optional(),
    cpf: z
      .string()
      .min(1, t("registerCitizen.verification.cpf"))
      .refine((cpf) => {
        const result = validateDocument(cpf);
        return result.isValid && result.type === "cpf";
      }, t("registerCitizen.verification.invalidCpf")),
    password: passwordSchema,
    confirmedPassword: z.string(),
  })
  .refine((fields) => fields.confirmedPassword === fields.password, {
    message: t("registerCitizen.verification.confirmedPassword"),
    path: ["confirmedPassword"],
  });

type DetailsSchema = z.output<typeof detailsSchema>;

const form = reactive<DetailsSchema>({
  firstName: "",
  lastName: "",
  cpf: "",
  password: "",
  confirmedPassword: "",
});

const showPassword = ref<boolean>(false);
const showConfimedPassword = ref<boolean>(false);

function checkStrength(password: string) {
  return Object.values(passwordRequirements.value).map((req) => ({
    met: req.regex.test(password),
    text: req.text,
  }));
}
const strength = computed(() => checkStrength(form.password ?? ""));
const score = computed(() => strength.value.filter((req) => req.met).length);

const color = computed(() => {
  if (score.value === 0) return "neutral";
  if (score.value <= 1) return "error";
  if (score.value <= 2) return "warning";
  if (score.value === 3) return "warning";
  return "success";
});

const text = computed(() => {
  if (score.value === 0)
    return t("registerCitizen.passwordStrength.enterPassword");
  if (score.value <= 2) return t("registerCitizen.passwordStrength.weak");
  if (score.value === 3) return t("registerCitizen.passwordStrength.medium");
  return t("registerCitizen.passwordStrength.strong");
});

const store = useOfficialStore();

const onSubmit = async (event: FormSubmitEvent<DetailsSchema>) => {
  console.log({
    ...event.data,
    email: store.email,
  });

  await navigateTo("/login");
};
</script>

<template>
  <RegisterAsOfficialStep title="Personal information">
    <template #description>
      Email verified! Now fill out the form below with your personal
      information.
    </template>

    <UForm
      :schema="detailsSchema"
      :state="form"
      @submit="onSubmit"
      class="size-full flex flex-col items-center gap-4 md:gap-3 mb-15"
    >
      <div class="flex flex-col md:flex-row gap-2 2xl:gap-3 w-full">
        <UFormField
          :label="t('registerCitizen.firstName')"
          name="firstName"
          class="w-full"
          required
        >
          <UInput
            icon="i-lucide-user"
            v-model="form.firstName"
            type="text"
            :placeholder="t('registerCitizen.firstNamePlaceholder')"
            class="w-full text-xl"
          />
        </UFormField>

        <UFormField
          :label="t('registerCitizen.lastName')"
          name="lastName"
          class="w-full"
          :hint="t('registerCitizen.optional')"
        >
          <UInput
            v-model="form.lastName"
            type="text"
            :placeholder="t('registerCitizen.lastNamePlaceholder')"
            class="w-full text-xl"
          />
        </UFormField>
      </div>

      <UFormField label="CPF" name="cpf" class="w-full" required>
        <UInput
          icon="i-lucide-id-card"
          v-model="form.cpf"
          type="text"
          :placeholder="t('registerCitizen.cpfPlaceholder')"
          class="w-full text-xl"
        />
      </UFormField>

      <PasswordInput
        v-model="form.password"
        :color="color"
        :show="showPassword"
        :label="t('registerCitizen.password')"
        name="password"
        :placeholder="t('registerCitizen.passwordPlaceholder')"
        required
        @click="showPassword = !showPassword"
      >
        <div>
          <UProgress
            :color="color"
            :indicator="text"
            :model-value="score"
            :max="4"
            size="sm"
            class="w-full h-1 my-2"
          />

          <p id="password-strength" class="text-sm font-medium text-left">
            {{ text }}. {{ t("registerCitizen.passwordStrengthIndicator") }}
          </p>

          <ul class="space-y-1 text-xl" aria-label="Password requirements">
            <li
              v-for="(req, index) in strength"
              :key="index"
              class="flex items-center gap-1 text-neutral-600 m-1"
              :class="req.met ? 'text-success' : 'text-muted'"
            >
              <UIcon
                :name="req.met ? 'i-lucide-circle-check' : 'i-lucide-circle-x'"
                class="size-4 shrink-0"
              />

              <span class="text-sm font-light">
                {{ req.text }}
                <span class="sr-only">
                  {{
                    req.met ? " - Requirement met" : " - Requirement not met"
                  }}
                </span>
              </span>
            </li>
          </ul>
        </div>
      </PasswordInput>

      <PasswordInput
        v-model="form.confirmedPassword"
        color="neutral"
        :show="showConfimedPassword"
        :label="t('registerCitizen.confirmPassword')"
        name="confirmedPassword"
        :placeholder="t('registerCitizen.confirmPasswordPlaceholder')"
        required
        @click="showConfimedPassword = !showConfimedPassword"
      />

      <UButton type="submit">Submit</UButton>
    </UForm>
  </RegisterAsOfficialStep>
</template>
