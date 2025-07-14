<script lang="ts" setup>
import CollapsibleMenu from "~/components/CollapsibleMenu.vue";
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
  // middleware: ["auth", "official"],
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
  <OfficialPage
    :title="t('municipalities.header')"
    :description="t('municipalities.subheader')"
  >
    <CollapsibleMenu
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
    </CollapsibleMenu>

    <CollapsibleMenu
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
    </CollapsibleMenu>
  </OfficialPage>
</template>
