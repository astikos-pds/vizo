<script lang="ts" setup>
import type { FormSubmitEvent } from "@nuxt/ui";
import z from "zod";
import { useOfficialStore } from "~/stores/official";

const emit = defineEmits<{
  (e: "next"): void;
}>();

const emailSchema = z.object({
  email: z.string().email("Invalid email"),
});

type EmailSchema = z.output<typeof emailSchema>;

const store = useOfficialStore();

const form = reactive<EmailSchema>({
  email: store.email,
});

const onSubmit = async (event: FormSubmitEvent<EmailSchema>) => {
  console.log(event);
  store.setEmail(event.data.email);
  emit("next");
};
</script>

<template>
  <RegisterAsOfficialStep title="E-mail">
    <UForm
      :schema="emailSchema"
      :state="form"
      @submit="onSubmit"
      class="size-full flex flex-col items-center gap-4 md:gap-3"
    >
      <UFormField label="E-mail" name="email" class="w-full">
        <UInput
          v-model="form.email"
          type="text"
          placeholder="Enter your institutional e-mail"
          class="w-full"
        />
      </UFormField>

      <UButton type="submit">Submit</UButton>
    </UForm>
  </RegisterAsOfficialStep>
</template>
