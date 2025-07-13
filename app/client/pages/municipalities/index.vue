<script lang="ts" setup>
import { getMunicipalitiesAffiliationsUseCase } from "~/services/users";

const { t } = useI18n();

useHead({
  title: t("head.municipalities.title"),
  meta: [
    {
      name: "description",
      content: t("head.municipalities.description"),
    },
  ],
});

definePageMeta({
  layout: "official",
  middleware: ["auth", "official"],
});

const { data: affiliations } = useAsyncData("municipalities-affiliations", () =>
  getMunicipalitiesAffiliationsUseCase()
);

const approvedAffiliations = computed(() =>
  affiliations.value?.filter((a) => a.affiliationRequest.status === "APPROVED")
);

const requestedAffiliations = computed(() =>
  affiliations.value?.filter((a) => a.affiliationRequest.status !== "APPROVED")
);
</script>

<template>
  <div class="w-[75%] 2xl:w-[60%] p-3 flex flex-col items-center">
    <header class="my-8 text-center flex flex-col gap-2">
      <h1 class="text-2xl font-semibold">{{ t("municipalities.header") }}</h1>
      <p class="text-sm">
        {{ t("municipalities.subheader") }}
      </p>
    </header>

    <main class="w-full flex flex-col gap-3">
      <MunicipalityMenu
        v-if="approvedAffiliations"
        :title="t('municipalities.myMunicipalities')"
        :items="approvedAffiliations"
        default-open
      >
        <template #empty>{{ t("municipalities.noMunicipality") }}</template>

        <template #body>
          <MunicipalityCard
            v-for="affiliation in approvedAffiliations"
            :municipality="affiliation.municipality"
          />
        </template>
      </MunicipalityMenu>

      <MunicipalityMenu
        v-if="requestedAffiliations"
        :title="t('municipalities.requests')"
        :items="requestedAffiliations"
        default-open
      >
        <template #empty>{{ t("municipalities.noRequest") }}</template>

        <template #body>
          <MunicipalityAffiliationRequestCard
            v-for="affiliation in requestedAffiliations"
            :affiliation-request="affiliation.affiliationRequest"
            :municipality="affiliation.municipality"
          />
        </template>
      </MunicipalityMenu>
    </main>
  </div>
</template>
