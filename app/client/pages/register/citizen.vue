<script lang="ts" setup>
import {
  registerSchema,
  passwordRequirements,
  type RegisterSchema,
} from "~/lib/schema/register-schema";

definePageMeta({
  layout: "guest",
});

const form = reactive<RegisterSchema>({
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
</script>

<template>
  <section
    class="w-[45%] h-full flex flex-col items-center justify-center py-20 overflow-y-scroll"
  >
    <h1 class="text-4xl font-semibold text-neutral-900">Sign Up</h1>

    <UForm
      :state="form"
      :schema="registerSchema"
      class="min-w-[50%] flex flex-col items-center gap-4 mt-5"
    >
      <UFormField label="CPF" name="cpf" size="xl" class="w-full" required>
        <UInput
          icon="i-lucide-id-card"
          v-model="form.cpf"
          type="text"
          size="xl"
          placeholder="Enter your document"
          class="w-full text-xl"
        />
      </UFormField>

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
        <NuxtLink to="/login" class="text-primary">Login here</NuxtLink></span
      >

      <UButton type="submit" size="xl">Sign Up</UButton>
    </UForm>
  </section>
  <section
    class="w-[55%] bg-linear-to-tr to-primary from-neutral-400"
  ></section>
</template>
