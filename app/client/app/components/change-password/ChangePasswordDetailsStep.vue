<script lang="ts" setup>
import type { FormSubmitEvent } from "@nuxt/ui";
import z from "zod";
import type { PasswordRequirements } from "~/types/domain/password";

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

const changePasswordSchema = z
  .object({
    password: passwordSchema,
    confirmedPassword: z.string(),
  })
  .refine((fields) => fields.confirmedPassword === fields.password, {
    message: t("register.verification.confirmedPassword"),
    path: ["confirmedPassword"],
  });

type ChangePasswordSchema = z.output<typeof changePasswordSchema>;

const store = useEmailStore();

const form = reactive<ChangePasswordSchema>({
  password: "",
  confirmedPassword: "",
});

const showPassword = ref<boolean>(false);
const showConfimedPassword = ref<boolean>(false);

const { loading, changePassword } = useAuth();

const toast = useToast();

const onSubmit = async (event: FormSubmitEvent<ChangePasswordSchema>) => {
  if (!store.email) return;

  await changePassword({
    email: store.email,
    newPassword: event.data.password,
  });

  toast.add({
    title: "Sucess",
    description: "Password redefined successfully.",
    color: "success",
  });

  store.setEmail(null);

  await navigateTo("/login");
};
</script>

<template>
  <ChangePasswordStep
    title="Complete your password change"
    description="Update your password in the form below."
  >
    <UForm
      :state="form"
      :schema="changePasswordSchema"
      @submit="onSubmit"
      :disabled="loading"
      class="flex flex-col items-center gap-4 md:gap-3"
    >
      <PasswordInput
        v-model="form.password"
        color="neutral"
        :show="showPassword"
        label="Nova Senha"
        name="password"
        placeholder="Digite sua nova senha"
        required
        @click="showPassword = !showPassword"
      >
        <PasswordStrengthIndicator
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

      <UButton type="submit" :loading="loading">Reset password</UButton>
    </UForm>
  </ChangePasswordStep>
</template>
