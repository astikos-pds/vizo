<script lang="ts" setup>
import type { FormSubmitEvent } from "@nuxt/ui";
import z from "zod";
import { useEmailStore } from "~/stores/email";
import { useSteps } from "~/composables/use-steps";
import type { Municipality } from "~/types/domain";

const { t } = useI18n();

const emailSchema = z.object({
  email: z.string().email(t("register.verification.email")),
});

type EmailSchema = z.output<typeof emailSchema>;

const store = useEmailStore();

const form = reactive<EmailSchema>({
  email: store.email,
});

const municipalityCardOpen = ref(false);
const municipality = ref<Municipality>();

const { loading, getMunicipalityByDomain } = useMunicipality();
const toast = useToast();

const onSubmit = async (event: FormSubmitEvent<EmailSchema>) => {
  store.setEmail(event.data.email);

  const domain = event.data.email.split("@")[1];
  if (!domain) return;

  const response = await getMunicipalityByDomain({ domain });
  if (!response) {
    toast.add({
      title: t("registerOfficial.emailStep.toast.title"),
      description: t("registerOfficial.emailStep.toast.description"),
      color: "error",
    });
    return;
  }

  municipality.value = response;
  municipalityCardOpen.value = true;
};

const stepper = useSteps();

const onConfirm = () => {
  stepper.next();
};
</script>

<template>
  <RegisterAsOfficialStep :title="t('registerOfficial.emailStep.title')">
    <template #description>
      {{ t("registerOfficial.emailStep.description") }}
    </template>

    <UForm
      :schema="emailSchema"
      :state="form"
      @submit="onSubmit"
      class="size-full flex flex-col items-center gap-4 md:gap-3"
    >
      <UFormField
        :label="t('registerOfficial.emailStep.label')"
        name="email"
        class="w-full"
      >
        <UInput
          v-model="form.email"
          type="text"
          :placeholder="t('registerOfficial.emailStep.placeholder')"
          class="w-full"
        />
      </UFormField>

      <UButton type="submit" :loading="loading">{{
        t("register.submitButton")
      }}</UButton>

      <UCollapsible
        v-model:open="municipalityCardOpen"
        class="w-full"
        :ui="{
          content: 'p-1',
        }"
      >
        <template #content>
          <UCard
            v-if="municipality"
            class="w-full"
            variant="outline"
            :ui="{
              header: 'p-3 sm:px-3',
              body: 'p-3 sm:p-3',
              footer: 'p-3 sm:px-3',
            }"
          >
            <template #header>
              <h2 class="font-semibold text-lg">
                {{ t("registerOfficial.emailStep.municipalityCard.title") }}
              </h2>
              <p>
                {{
                  t("registerOfficial.emailStep.municipalityCard.description")
                }}
              </p>
            </template>

            <div class="flex items-center gap-3">
              <UAvatar
                :src="municipality.iconUrl"
                :alt="municipality.name"
                size="3xl"
              />
              <div class="flex flex-col">
                <h3 class="font-semibold text-lg">{{ municipality.name }}</h3>
                <p>
                  {{ t("registerOfficial.emailStep.municipalityCard.domain") }}:
                  {{ municipality.emailDomain }}
                </p>
              </div>
            </div>

            <template #footer>
              <div class="flex justify-end gap-2">
                <UButton
                  variant="outline"
                  color="neutral"
                  @click="municipalityCardOpen = false"
                >
                  {{ t("registerOfficial.emailStep.municipalityCard.cancel") }}
                </UButton>
                <UButton @click="onConfirm">{{
                  t("registerOfficial.emailStep.municipalityCard.confirm")
                }}</UButton>
              </div>
            </template>
          </UCard>
        </template>
      </UCollapsible>
    </UForm>
  </RegisterAsOfficialStep>
</template>
