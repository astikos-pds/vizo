<script lang="ts" setup>
import type { StepperItem } from "@nuxt/ui";

const { t } = useI18n();

useHead({
  title: t("head.requestAffiliation.title"),
  meta: [
    {
      name: "description",
      content: t("head.requestAffiliation.description"),
    },
  ],
});

definePageMeta({
  name: "Request new affiliation",
  middleware: ["auth"],
});

const steps = computed(
  () =>
    [
      {
        slot: "email" as const,
        title: t("pages.affiliationRequest.steps.email.title"),
        description: t("pages.affiliationRequest.steps.email.description"),
        icon: "i-lucide-mail",
      },
      {
        slot: "verify" as const,
        title: t("pages.affiliationRequest.steps.verify.title"),
        description: t("pages.affiliationRequest.steps.verify.description"),
        icon: "i-lucide-badge-check",
      },
      {
        slot: "approval" as const,
        title: t("pages.affiliationRequest.steps.approval.title"),
        description: t("pages.affiliationRequest.steps.approval.description"),
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
