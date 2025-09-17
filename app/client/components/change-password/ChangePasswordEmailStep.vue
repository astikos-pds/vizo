<script lang="ts" setup>
import type { FormSubmitEvent } from "@nuxt/ui";
import z from "zod";
import { useUsers } from "~/composables/use-users";

const emailSchema = z.object({
  cpf: z.string().min(11, "Enter a valid CPF.").max(14, "Enter a valid CPF."),
  email: z.string().email("Enter a valid e-mail."),
});

type EmailSchema = z.output<typeof emailSchema>;

const store = useEmailStore();

const form = reactive<EmailSchema>({
  cpf: "",
  email: store.email ? store.email : "",
});

const stepper = useSteps();

const { existsUserByParams } = useUsers();
const toast = useToast();

const loading = ref<boolean>(false);

const onSubmit = async (event: FormSubmitEvent<EmailSchema>) => {
  const { email, cpf } = event.data;

  loading.value = true;
  const { data: existsUserWithEmail } = await existsUserByParams({ email });
  loading.value = false;

  if (existsUserWithEmail) {
    toast.add({
      title: "Error",
      description: "This e-mail does not exist in our records.",
      color: "error",
    });

    return;
  }

  store.setEmail(email);
  stepper.next();
};
</script>

<template>
  <ChangePasswordStep
    title="E-mail"
    description="Give your e-mail and document to receive a verification code."
  >
    <UForm
      :schema="emailSchema"
      :state="form"
      @submit="onSubmit"
      class="size-full flex flex-col items-center gap-4"
    >
      <UFormField label="CPF" name="cpf" required class="w-full">
        <UInput
          v-model="form.cpf"
          placeholder="Insira seu CPF"
          class="w-full"
        />
      </UFormField>

      <UFormField label="E-mail" name="email" required class="w-full">
        <UInput
          v-model="form.email"
          placeholder="example@email.com"
          class="w-full"
        />
      </UFormField>

      <UButton type="submit" :loading="loading">Next</UButton>
    </UForm>
  </ChangePasswordStep>
</template>
