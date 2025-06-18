<script lang="ts" setup>
import type { FormSubmitEvent } from "@nuxt/ui";
import * as z from "zod";
import { useAuth } from "~/composables/use-auth";
import { validateDocument } from "~/utils/document-validation";

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

const { loading, login } = useAuth();

const toast = useToast();
const onSubmit = async (event: FormSubmitEvent<LoginSchema>) => {
  const ok = await login({
    document: event.data.document,
    password: event.data.password,
  });

  if (ok) {
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
    class="lg:min-w-[45%] xl:min-w-[50%] h-full bg-linear-to-tr from-primary to-neutral-200 dark:to-neutral-500"
  ></section>
  <section
    class="relative size-full flex flex-col items-center justify-center py-20"
  >
    <ConfigHeader />
    <section class="w-[70%] md:w-[50%] lg:w-[60%] xl:w-[50%]">
      <h1
        class="text-4xl font-semibold text-wrap text-neutral-900 dark:text-neutral-50 text-center"
      >
        {{ t("loginCitizen.title") }}
      </h1>

      <UForm
        :state="form"
        :schema="loginSchema"
        @submit="onSubmit"
        :disabled="loading"
        class="flex flex-col items-center gap-5 mt-8"
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
          class="cursor-pointer text-neutral-50 font-semibold"
          :loading="loading"
          >{{ t("loginCitizen.signInButton") }}</UButton
        >
      </UForm>
    </section>
  </section>
</template>
