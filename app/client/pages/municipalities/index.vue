<script lang="ts" setup>
import type { AccordionItem } from "@nuxt/ui";
import type { AffiliationRequest } from "~/types/domain";

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

const affiliations = ref<AffiliationRequest[]>([
  {
    id: "",
    official: {
      id: "",
      document: "",
      email: "mates@gmail.com",
      name: "Mateus",
      avatar: null,
      role: "OFFICIAL",
      wasApproved: false,
      createdAt: "",
      updatedAt: "",
    },
    municipality: {
      id: "",
      name: "São Paulo",
      emailDomain: "",
      iconUrl: "",
      createdAt: "",
      updatedAt: "",
    },
    status: "PENDING",
    createdAt: "",
    approvedBy: null,
    approvedAt: null,
  },
]);

const approvedAffiliations = computed(() =>
  affiliations.value?.filter((a) => a.status === "APPROVED")
);

const requestedAffiliations = computed(() =>
  affiliations.value?.filter((a) => a.status !== "PENDING")
);

const items = ref<AccordionItem[]>([
  {
    label: t("municipalities.myMunicipalities"),
    icon: "i-lucide-building",
    slot: "municipalities",
  },
  {
    label: t("municipalities.requests"),
    icon: "i-lucide-clock",
    slot: "pending",
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
