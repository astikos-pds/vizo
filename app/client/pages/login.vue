<script lang="ts" setup>
import { loginSchema, type LoginSchema } from "~/lib/schema/login-schema";

definePageMeta({
  layout: "guest",
});

const form = reactive<LoginSchema>({
  document: "",
  password: "",
});
const showPassword = ref<boolean>(false);
</script>

<template>
  <section
    class="w-[55%] bg-linear-to-tr from-primary to-neutral-400"
  ></section>
  <section
    class="w-[45%] h-full flex flex-col items-center justify-center py-20 overflow-y-scroll"
  >
    <h1 class="text-4xl font-semibold text-neutral-900">
      Log in to your account
    </h1>

    <UForm
      :state="form"
      :schema="loginSchema"
      class="min-w-[50%] flex flex-col items-center gap-5 mt-8"
    >
      <UFormField
        label="Document"
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
          placeholder="Enter your document"
          class="w-full text-xl"
          v-mask="[CPF_MASK, CNPJ_MASK]"
        />
      </UFormField>

      <PasswordInput
        v-model="form.password"
        color="neutral"
        :show="showPassword"
        label="Password"
        name="password"
        placeholder="Enter your password"
        required
        @click="showPassword = !showPassword"
      />

      <span
        >Don't have an account?
        <NuxtLink to="/register/citizen" class="text-primary"
          >Sign up here</NuxtLink
        ></span
      >

      <UButton type="submit" size="xl">Sign In</UButton>
    </UForm>
  </section>
</template>
