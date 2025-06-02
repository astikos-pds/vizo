<script lang="ts" setup>
import type { FormSubmitEvent } from "@nuxt/ui";
import {
  registerSchema,
  passwordRequirements,
  type RegisterSchema,
} from "~/lib/schema/register-schema";

definePageMeta({
  layout: "guest",
});

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

function checkStrength(password: string) {
  return Object.values(passwordRequirements).map((req) => ({
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
  if (score.value === 0) return "Enter a password";
  if (score.value <= 2) return "Weak password";
  if (score.value === 3) return "Medium password";
  return "Strong password";
});

const { loading, error, registerAsCitizen } = useAuth();

const toast = useToast();
const onSubmit = async (event: FormSubmitEvent<RegisterSchema>) => {
  await registerAsCitizen({
    name: `${event.data.firstName} ${event.data.lastName}`.trim(),
    document: event.data.cpf,
    email: event.data.email,
    password: event.data.password,
  });

  if (error.value) {
    toast.add({
      title: "Error",
      description: error.value,
      color: "error",
    });
  } else {
    navigateTo("/login");
  }
};
</script>

<template>
  <section
    class="w-[45%] h-full flex flex-col items-center justify-center py-20 overflow-y-scroll"
  >
    <h1 class="text-4xl font-semibold text-neutral-900">Create an account</h1>

    <UForm
      :state="form"
      :schema="registerSchema"
      @submit="onSubmit"
      :disabled="loading"
      class="min-w-[55%] flex flex-col items-center gap-4 mt-6"
    >
      <div class="flex flex-row gap-3 w-full">
        <UFormField
          label="First name"
          name="firstName"
          size="xl"
          class="w-full"
          required
        >
          <UInput
            icon="i-lucide-user"
            v-model="form.firstName"
            type="text"
            size="xl"
            placeholder="Enter your first name"
            class="w-full text-xl"
          />
        </UFormField>

        <UFormField
          label="Last name"
          name="lastName"
          size="xl"
          class="w-full"
          hint="Optional"
        >
          <UInput
            v-model="form.lastName"
            type="text"
            size="xl"
            placeholder="Enter your last name"
            class="w-full text-xl"
          />
        </UFormField>
      </div>

      <UFormField label="E-mail" name="email" size="xl" class="w-full" required>
        <UInput
          icon="i-lucide-at-sign"
          v-model="form.email"
          type="email"
          size="xl"
          placeholder="Enter your e-mail"
          class="w-full text-xl"
        />
      </UFormField>

      <div class="flex flex-row gap-3 w-full">
        <UFormField label="CPF" name="cpf" size="xl" class="w-full" required>
          <UInput
            icon="i-lucide-id-card"
            v-model="form.cpf"
            type="text"
            size="xl"
            placeholder="Enter your document"
            class="w-full text-xl"
            v-mask="CPF_MASK"
          />
        </UFormField>
        <UFormField
          label="Birth date"
          name="birthDate"
          size="xl"
          class="w-full"
          required
        >
          <UInput
            icon="i-lucide-cake-slice"
            v-model="form.birthDate"
            type="date"
            size="xl"
            placeholder="Enter your e-mail"
            class="w-full text-xl"
          />
        </UFormField>
      </div>

      <PasswordInput
        v-model="form.password"
        :color="color"
        :show="showPassword"
        label="Password"
        name="password"
        placeholder="Enter your password"
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

          <p id="password-strength" class="text-sm font-medium">
            {{ text }}. Must contain:
          </p>

          <ul class="space-y-1 text-xl" aria-label="Password requirements">
            <li
              v-for="(req, index) in strength"
              :key="index"
              class="flex items-center gap-0.5 text-neutral-600"
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
        label="Confirm your password"
        name="confirmedPassword"
        placeholder="Confirm your password"
        required
        @click="showConfimedPassword = !showConfimedPassword"
      />

      <span
        >Already have an account?
        <NuxtLink to="/login" class="text-primary">Log in here</NuxtLink></span
      >

      <UButton type="submit" size="xl" :loading="loading" class="cursor-pointer"
        >Sign Up</UButton
      >
    </UForm>
  </section>
  <section
    class="w-[55%] bg-linear-to-tr to-primary from-neutral-400"
  ></section>
</template>
