<script lang="ts" setup>
import type { FormSubmitEvent } from "@nuxt/ui";
import z from "zod";
import { useUsers } from "~/composables/use-users";

const emailSchema = z.object({
  email: z.string().email("Enter a valid e-mail."),
});

type EmailSchema = z.output<typeof emailSchema>;

const store = useEmailStore();

const form = reactive<EmailSchema>({
  email: store.email ? store.email : "",
});

const stepper = useSteps();

const { getUserWithFilters } = useUsers();
const toast = useToast();

const loading = ref<boolean>(false);

const onSubmit = async (event: FormSubmitEvent<EmailSchema>) => {
  const email = event.data.email;

  loading.value = true;
  const { data: existingUser } = await getUserWithFilters({ email });
  loading.value = false;

  if (existingUser) {
    toast.add({
      title: "Error",
      description: "This e-mail is already in use by another user.",
      color: "error",
    });

    return;
  }

  store.setEmail(email);
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

      <UButton type="submit" :loading="loading">Next</UButton>
    </UForm>
  </RegisterStep>
</template>
