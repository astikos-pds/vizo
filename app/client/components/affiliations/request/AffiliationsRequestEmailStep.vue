<script lang="ts" setup>
import type { FormSubmitEvent } from "@nuxt/ui";
import z from "zod";
import { useEmailStore } from "~/stores/email";
import { useSteps } from "~/composables/use-steps";
import { useMunicipalities } from "~/composables/use-municipalities";
import type { Municipality } from "~/types/domain/municipality";
import { useAffiliationRequestStore } from "~/stores/affiliation-request";

const { t } = useI18n();

const emailSchema = z.object({
  email: z.string().email(t("register.verification.email")),
});

type EmailSchema = z.output<typeof emailSchema>;

const store = useAffiliationRequestStore();

const form = reactive<EmailSchema>({
  email: store.institutionalEmail ? store.institutionalEmail : "",
});

const municipalityCardOpen = ref(false);
const municipality = ref<Municipality | null>(null);

const {
  loading: institutionalEmailAlreadyInUseLoading,
  existsAffiliatedUserByParams,
} = useAffiliatedUsers();
const { loading: emailAlreadyInUseLoading, existsUserByParams } = useUsers();
const { loading: municipalityLoading, getMunicipalityByEmailDomain } =
  useMunicipalities();

const loading = computed(
  () =>
    municipalityLoading ||
    institutionalEmailAlreadyInUseLoading ||
    emailAlreadyInUseLoading
);

const toast = useToast();

function notifyWithToast(description: string) {
  toast.clear();

  toast.add({
    title: "Error",
    description,
    color: "error",
  });
}

const onSubmit = async (event: FormSubmitEvent<EmailSchema>) => {
  const email = event.data.email;

  const domain = email.split("@")[1];
  if (!domain) return;

  const foundMunicipality = await getMunicipalityByEmailDomain(domain);

  if (!foundMunicipality) {
    toast.clear();

    toast.add({
      title: t("registerOfficial.emailStep.toast.title"),
      description: t("registerOfficial.emailStep.toast.description"),
      color: "error",
    });
    return;
  }

  const emailAlreadyInUseByCommomUser = await existsUserByParams({ email });
  if (emailAlreadyInUseByCommomUser === null) return;
  if (emailAlreadyInUseByCommomUser === true) {
    notifyWithToast("This e-mail is already in use by another user.");

    return;
  }

  const emailAlreadyInUseByInstitutionalUser =
    await existsAffiliatedUserByParams({
      institutionalEmail: email,
    });
  if (emailAlreadyInUseByInstitutionalUser === null) return;
  if (emailAlreadyInUseByInstitutionalUser === true) {
    notifyWithToast(
      "This institutional e-mail is already in use by another user."
    );

    return;
  }

  municipality.value = foundMunicipality;
  municipalityCardOpen.value = true;

  store.setInstitutionalEmail(email);
};

const stepper = useSteps();

const onConfirm = () => {
  if (!municipality.value) return;

  store.setMunicipalityId(municipality.value.id);

  stepper.next();
};
</script>

<template>
  <AffiliationsRequestStep :title="t('registerOfficial.emailStep.title')">
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

      <UButton type="submit" :loading="loading.value">{{
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
              <h2 class="font-semibold">
                {{ t("registerOfficial.emailStep.municipalityCard.title") }}
              </h2>
              <p class="text-sm">
                {{
                  t("registerOfficial.emailStep.municipalityCard.description")
                }}
              </p>
            </template>

            <div class="flex items-center gap-3">
              <UAvatar
                :src="municipality.iconUrl?.toString()"
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
  </AffiliationsRequestStep>
</template>
