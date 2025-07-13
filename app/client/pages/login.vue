<script lang="ts" setup>
import type { FormSubmitEvent } from "@nuxt/ui";
import * as z from "zod";
import { useAuth } from "~/composables/use-auth";
import { useUser } from "~/composables/use-user";
import { validateDocument } from "~/utils/document-validation";
import { useI18n } from "vue-i18n";

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
    .min(1, t("login.verification.documentRequired"))
    .refine(
      (document) => validateDocument(document).isValid,
      t("login.verification.invalidDocument")
    ),
  password: z.string().min(1, t("login.verification.passwordRequired")),
});

type LoginSchema = z.infer<typeof loginSchema>;

const form = reactive<LoginSchema>({
  document: "",
  password: "",
});
const showPassword = ref<boolean>(false);

const { loading, login } = useAuth();
const { getProfile } = useUser();

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

    const user = await getProfile();
    if (user) {
      useUserStore().setUser(user);

      if (user.userType === "CITIZEN") await navigateTo("/");
      else if (user.userType === "OFFICIAL")
        await navigateTo("/municipalities");
    }
  }
};
</script>

<template>
  <section
    class="lg:min-w-[45%] xl:min-w-[50%] h-full bg-linear-to-tr from-primary to-neutral-200 dark:to-neutral-500"
  ></section>
  <section class="relative size-full flex flex-col items-center">
    <ConfigHeader class="w-full" />
    <section class="w-[70%] md:w-[50%] lg:w-[55%] 2xl:w-[45%] my-auto">
      <h1
        class="text-4xl font-semibold text-wrap text-neutral-900 dark:text-neutral-50 text-center"
      >
        {{ t("login.title") }}
      </h1>

      <UForm
        :state="form"
        :schema="loginSchema"
        @submit="onSubmit"
        :disabled="loading"
        class="flex flex-col items-center gap-5 mt-8"
      >
        <UFormField
          :label="t('login.cpf')"
          name="document"
          class="w-full"
          required
        >
          <UInput
            icon="i-lucide-id-card"
            v-model="form.document"
            type="text"
            :placeholder="t('login.cpfPlaceholder')"
            class="w-full text-xl"
          />
        </UFormField>

        <PasswordInput
          v-model="form.password"
          :show="showPassword"
          :label="t('login.password')"
          name="password"
          :placeholder="t('login.passwordPlaceholder')"
          required
          @click="showPassword = !showPassword"
        />

        <div class="text-center text-sm flex flex-col">
          <i18n-t keypath="login.dontHaveAccount" tag="span">
            <template #citizen>
              <NuxtLink to="/citizen/register" class="text-primary">{{
                t("login.as.citizen")
              }}</NuxtLink>
            </template>
            <template #official>
              <NuxtLink to="/official/register" class="text-primary">{{
                t("login.as.official")
              }}</NuxtLink>
            </template>
          </i18n-t>
        </div>

        <UButton type="submit" :loading="loading">{{
          t("login.signInButton")
        }}</UButton>
      </UForm>
    </section>
  </section>
</template>
