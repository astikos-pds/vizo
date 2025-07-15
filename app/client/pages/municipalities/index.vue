<script lang="ts" setup>
import type { AccordionItem } from "@nuxt/ui";
import { type MunicipalityAffiliation } from "~/services/users";

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

// const { data: affiliations } = useAsyncData("municipalities-affiliations", () =>
//   getMunicipalitiesAffiliationsUseCase()
// );

const affiliations = ref<MunicipalityAffiliation[]>([
  {
    affiliationRequest: {
      id: "",
      officialId: "",
      municipalityId: "",
      status: "PENDING",
      createdAt: "2023-05-21",
      approvedById: null,
      approvedAt: null,
    },
    municipality: {
      id: "",
      name: "São Paulo",
      emailDomain: "",
      iconUrl: "",
      createdAt: "2023-05-21",
      updatedAt: "",
    },
  },
]);

const approvedAffiliations = computed(() =>
  affiliations.value?.filter((a) => a.affiliationRequest.status === "APPROVED")
);

const requestedAffiliations = computed(() =>
  affiliations.value?.filter((a) => a.affiliationRequest.status !== "APPROVED")
);

const items = ref<AccordionItem[]>([
  {
    label: t("municipalities.myMunicipalities"),
    icon: "i-lucide-building",
    slot: "municipalities",
    content: "ola",
  },
  {
    label: t("municipalities.requests"),
    icon: "i-lucide-clock",
    slot: "pending",
    content: "ola",
  },
]);

const active = ref(["0", "1"]);
</script>

<template>
  <OfficialPage
    :title="t('municipalities.header')"
    :description="t('municipalities.subheader')"
  >
    <UAccordion type="multiple" v-model="active" :items="items">
      <template #municipalities>
        <MunicipalityMenu
          :items="approvedAffiliations"
          :empty-text="t('municipalities.noMunicipality')"
        >
          <template #item="{ item }">
            <MunicipalityCard v-bind="item" />
          </template>
        </MunicipalityMenu>
      </template>

      <template #pending>
        <MunicipalityMenu
          :items="requestedAffiliations"
          :empty-text="t('municipalities.noRequest')"
        >
          <template #item="{ item }">
            <MunicipalityAffiliationRequestCard v-bind="item" />
          </template>
        </MunicipalityMenu>
      </template>
    </UAccordion>
  </OfficialPage>
</template>
