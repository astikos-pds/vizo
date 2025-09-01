<script lang="ts" setup>
import type { FormSubmitEvent } from "@nuxt/ui";
import z from "zod";

const emailSchema = z.object({
  email: z.string().email("Enter a valid e-mail."),
});

type EmailSchema = z.output<typeof emailSchema>;

const store = useEmailStore();

const form = reactive<EmailSchema>({
  email: store.email ? store.email : "",
});

const stepper = useSteps();

const onSubmit = async (event: FormSubmitEvent<EmailSchema>) => {
  store.setEmail(event.data.email);
  stepper.next();
};
</script>

<template>
  <RegisterStep
    title="E-mail"
    description="Give your e-mail to receive a verification code."
  >
    <UForm
      :schema="emailSchema"
      :state="form"
      @submit="onSubmit"
      class="size-full flex flex-col items-center gap-4"
    >
      <UFormField label="E-mail" name="email" required class="w-full">
        <UInput
          v-model="form.email"
          placeholder="example@email.com"
          class="w-full"
        />
      </UFormField>

      <UButton type="submit">Next</UButton>
    </UForm>
  </RegisterStep>
</template>
