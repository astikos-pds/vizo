<script lang="ts" setup>
import type { PasswordRequirements } from "~/types/domain/password";

const { password, passwordRequirements } = defineProps<{
  password: string;
  passwordRequirements: PasswordRequirements;
}>();

const { t } = useI18n();

function checkStrength(password: string) {
  return Object.values(passwordRequirements).map((req) => ({
    met: req.regex.test(password),
    text: req.text,
  }));
}
const strength = computed(() => checkStrength(password));

const score = computed(() => strength.value.filter((req) => req.met).length);

const color = computed(() => {
  if (score.value === 0) return "neutral";
  if (score.value <= 1) return "error";
  if (score.value <= 3) return "warning";
  return "success";
});

const text = computed(() => {
  if (score.value === 0) return t("register.passwordStrength.enterPassword");
  if (score.value <= 2) return t("register.passwordStrength.weak");
  if (score.value === 3) return t("register.passwordStrength.medium");
  return t("register.passwordStrength.strong");
});
</script>

<template>
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
</template>
