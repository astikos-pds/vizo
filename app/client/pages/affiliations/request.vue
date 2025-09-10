<script lang="ts" setup>
import type { StepperItem } from "@nuxt/ui";

useHead({
  title: "Vizo | Request affiliation",
  meta: [
    {
      name: "description",
      content:
        "Request affiliation to a municipality with your institutional e-mail.",
    },
  ],
});

definePageMeta({
  middleware: ["auth"],
});

const steps = computed(
  () =>
    [
      {
        slot: "email" as const,
        title: "Email",
        description: "Give us your institutional e-mail to verify",
        icon: "i-lucide-mail",
      },
      {
        slot: "verify" as const,
        title: "Verification",
        description: "Enter the code sent to the institutional e-mail",
        icon: "i-lucide-badge-check",
      },
      {
        slot: "approval" as const,
        title: "Await approval",
        description: "Wait until an municipal admin approves your request",
        icon: "i-lucide-hourglass",
      },
    ] satisfies StepperItem[]
);

const stepper = useSteps();
stepper.setTotalSteps(steps.value.length);
</script>

<template>
  <AffiliationsPage>
    <div class="size-full flex flex-col justify-center items-center">
      <UStepper
        :items="steps"
        v-model="stepper.current.value"
        linear
        disabled
        class="size-full"
        :ui="{
          header: 'p-3 border-b border-default',
        }"
      >
        <template #email>
          <AffiliationsRequestEmailStep />
        </template>

        <template #verify>
          <AffiliationsRequestVerificationStep />
        </template>

        <template #approval>
          <AffiliationsRequestApprovalStep />
        </template>
      </UStepper>
    </div>
  </AffiliationsPage>
</template>
