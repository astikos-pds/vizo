<script lang="ts" setup>
import type { FormSubmitEvent } from "@nuxt/ui";
import z from "zod";
import { useUsers } from "~/composables/use-users";
import { vMaska } from "maska/vue";

const credentialsSchema = z.object({
  cpf: z.string().min(11, "Enter a valid CPF.").max(14, "Enter a valid CPF."),
  email: z.string().email("Enter a valid e-mail."),
});

type CredentialsSchema = z.output<typeof credentialsSchema>;

const store = useEmailStore();

const form = reactive<CredentialsSchema>({
  cpf: "",
  email: "",
});

const stepper = useSteps();

const { loading, existsUserByParams } = useUsers();
const toast = useToast();

const onSubmit = async (event: FormSubmitEvent<CredentialsSchema>) => {
  const { email, cpf } = event.data;

  const existsUserWithDocumentAndEmail = await existsUserByParams({
    email,
    document: cpf,
  });

  if (existsUserWithDocumentAndEmail === null) return;

  if (!existsUserWithDocumentAndEmail) {
    toast.add({
      title: "Error",
      description: "We couldn't found any user with these credentials",
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
    title="Credentials"
    description="Verify if your account is registered in the system"
  >
    <UForm
      :schema="credentialsSchema"
      :state="form"
      @submit="onSubmit"
      class="size-full flex flex-col items-center gap-4"
    >
      <UFormField label="CPF" name="cpf" required class="w-full">
        <UInput
          v-model="form.cpf"
          placeholder="000.000.00-00"
          class="w-full"
          v-maska="CPF_MASK"
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
