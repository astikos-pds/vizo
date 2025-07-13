<script lang="ts" setup>
import type { FormSubmitEvent } from "@nuxt/ui";
import z from "zod";
import { useSteps } from "~/composables/use-steps";

const { t } = useI18n();

const passwordRequirements = computed(() => {
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

const detailsSchema = z
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
    password: passwordSchema,
    confirmedPassword: z.string(),
  })
  .refine((fields) => fields.confirmedPassword === fields.password, {
    message: t("register.verification.confirmedPassword"),
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
  if (score.value === 0) return t("register.passwordStrength.enterPassword");
  if (score.value <= 2) return t("register.passwordStrength.weak");
  if (score.value === 3) return t("register.passwordStrength.medium");
  return t("register.passwordStrength.strong");
});

const store = useEmailStore();
const stepper = useSteps();
const { registerAsOfficial } = useAuth();

const onSubmit = async (event: FormSubmitEvent<DetailsSchema>) => {
  const ok = await registerAsOfficial({
    name: `${event.data.firstName} ${event.data.lastName}`.trim(),
    document: event.data.cpf,
    email: store.email,
    password: event.data.password,
  });

  if (!ok) return;

  store.setEmail("");
  stepper.next();
};
</script>

<template>
  <RegisterAsOfficialStep :title="t('registerOfficial.detailsStep.title')">
    <template #description>
      {{ t("registerOfficial.detailsStep.description") }}
    </template>

    <UForm
      :schema="detailsSchema"
      :state="form"
      @submit="onSubmit"
      class="size-full flex flex-col items-center gap-4 md:gap-3 mb-15"
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

      <UFormField label="CPF" name="cpf" class="w-full" required>
        <UInput
          icon="i-lucide-id-card"
          v-model="form.cpf"
          type="text"
          :placeholder="t('register.cpfPlaceholder')"
          class="w-full text-xl"
        />
      </UFormField>

      <PasswordInput
        v-model="form.password"
        :color="color"
        :show="showPassword"
        :label="t('register.password')"
        name="password"
        :placeholder="t('register.passwordPlaceholder')"
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
            {{ text }}. {{ t("register.passwordStrengthIndicator") }}
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
                    req.met
                      ? t("register.requirementMet")
                      : t("register.requirementNotMet")
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
        :label="t('register.confirmPassword')"
        name="confirmedPassword"
        :placeholder="t('register.confirmPasswordPlaceholder')"
        required
        @click="showConfimedPassword = !showConfimedPassword"
      />

      <UButton type="submit">{{ t("register.signUpButton") }}</UButton>
    </UForm>
  </RegisterAsOfficialStep>
</template>
