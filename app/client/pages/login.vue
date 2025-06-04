<script lang="ts" setup>
import type { FormSubmitEvent } from "@nuxt/ui";
import * as z from "zod";
import { validateDocument } from "~/utils/document-validation";
import { CPF_MASK, CNPJ_MASK } from "~/utils/masks";

const { t } = useI18n();

useHead({
  title: t("head.login.title"),
  meta: [{ name: "description", content: t("head.login.description") }],
});

definePageMeta({
  layout: "guest",
});

const loginSchema = z.object({
  document: z
    .string()
    .min(1, t("loginCitizen.verification.documentRequired"))
    .refine(
      (document) => validateDocument(document).isValid,
      t("loginCitizen.verification.invalidDocument")
    ),
  password: z.string().min(1, t("loginCitizen.verification.passwordRequired")),
});

type LoginSchema = z.infer<typeof loginSchema>;

const form = reactive<LoginSchema>({
  document: "",
  password: "",
});
const showPassword = ref<boolean>(false);

const { loading, error, login } = useAuth();
const toast = useToast();
const onSubmit = async (event: FormSubmitEvent<LoginSchema>) => {
  await login({ document: event.data.document, password: event.data.password });

  if (error.value) {
    toast.add({
      title: t("toast.error.title"),
      description: t(
        `toast.error.description.${error.value}`,
        t("toast.error.description.500")
      ),
      color: "error",
    });
  } else {
    toast.add({
      title: t("toast.success.title"),
      description: t("toast.success.description.loggedIn"),
      color: "success",
    });

    await navigateTo("/");
  }
};
</script>

<template>
  <section
    class="lg:min-w-[45%] xl:min-w-[55%] bg-linear-to-tr from-primary to-neutral-400"
  ></section>
  <section
    class="md:w-[70%] md:h-[80%] md:rounded-md lg:rounded-none bg-[#FFFFFF] lg:size-full flex flex-col items-center justify-center py-20 overflow-y-scroll"
  >
    <h1 class="text-4xl font-semibold text-neutral-900">
      {{ t("loginCitizen.title") }}
    </h1>

    <UForm
      :state="form"
      :schema="loginSchema"
      @submit="onSubmit"
      :disabled="loading"
      class="min-w-[50%] flex flex-col items-center gap-5 mt-8"
    >
      <UFormField
        :label="t('loginCitizen.cpf')"
        name="document"
        size="xl"
        class="w-full"
        required
      >
        <UInput
          icon="i-lucide-id-card"
          v-model="form.document"
          type="text"
          size="xl"
          :placeholder="t('loginCitizen.cpfPlaceholder')"
          class="w-full text-xl"
        />
      </UFormField>

      <PasswordInput
        v-model="form.password"
        color="neutral"
        :show="showPassword"
        :label="t('loginCitizen.password')"
        name="password"
        :placeholder="t('loginCitizen.passwordPlaceholder')"
        required
        @click="showPassword = !showPassword"
      />

      <span
        >{{ t("loginCitizen.dontHaveAccount") }}
        <NuxtLink to="/register/citizen" class="text-primary">{{
          t("loginCitizen.signUpHere")
        }}</NuxtLink></span
      >

      <UButton
        type="submit"
        size="xl"
        class="cursor-pointer"
        :loading="loading"
        >{{ t("loginCitizen.signInButton") }}</UButton
      >
    </UForm>

    <LocalePicker class="absolute bottom-5" />
  </section>
</template>
