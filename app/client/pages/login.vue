<script lang="ts" setup>
import type { FormSubmitEvent } from "@nuxt/ui";
import * as z from "zod";
import { useAuth } from "~/composables/use-auth";
import { useI18n } from "vue-i18n";
import { vMaska } from "maska/vue";

const { t } = useI18n();

useHead({
  title: t("head.login.title"),
  meta: [{ name: "description", content: t("head.login.description") }],
});

definePageMeta({
  layout: "guest",
});

const loginSchema = z.object({
  document: z.string().min(1, t("login.verification.documentRequired")),
  password: z.string().min(1, t("login.verification.passwordRequired")),
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

  if (!ok) return;

  toast.add({
    title: t("toast.success.title"),
    description: t("toast.success.description.loggedIn"),
    color: "success",
  });

  await navigateTo("/");
};
</script>

<template>
  <LogoGradient />
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
            v-maska="CPF_MASK"
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
          <span>
            {{ t("login.dontHaveAccount") }}
            <NuxtLink to="/register" class="text-primary">{{
              t("login.signUpHere")
            }}</NuxtLink>
          </span>
        </div>

        <UButton type="submit" :loading="loading">{{
          t("login.signInButton")
        }}</UButton>
      </UForm>
    </section>
  </section>
</template>
