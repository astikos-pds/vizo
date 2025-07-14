<script lang="ts" setup>
import type { AccordionItem } from "@nuxt/ui";
import type { AffiliationRequest, Municipality } from "~/types/domain";

definePageMeta({
  layout: "official",
});

const { params } = useRoute();
const municipalityId = params.municipalityId as string;

// const municipality = useNuxtData<Municipality>(`municipality-${municipalityId}`)
const municipality: Municipality = {
  id: "",
  name: "São Paulo",
  emailDomain: "",
  iconUrl: "",
  createdAt: "",
  updatedAt: "",
};

const requests: AffiliationRequest[] = [
  {
    id: "",
    officialId: "",
    municipalityId: "",
    status: "PENDING",
    createdAt: "2023-06-03",
    approvedById: null,
    approvedAt: null,
  },
  {
    id: "",
    officialId: "",
    municipalityId: "",
    status: "APPROVED",
    createdAt: "2023-06-03",
    approvedById: null,
    approvedAt: null,
  },
];

const pendingRequests = computed(() =>
  requests.filter((a) => a.status === "PENDING")
);

const approvedRequests = computed(() =>
  requests.filter((a) => a.status === "APPROVED")
);

const rejectedRequests = computed(() =>
  requests.filter((a) => a.status === "REJECTED")
);

const items = ref<AccordionItem[]>([
  {
    label: "All",
    icon: "i-lucide-swatch-book",
    slot: "all" as const,
  },
  {
    label: "Pending",
    icon: "i-lucide-clock",
    slot: "pending" as const,
  },
  {
    label: "Approved",
    icon: "i-lucide-check",
    slot: "approved" as const,
  },
  {
    label: "Rejected",
    icon: "i-lucide-ban",
    slot: "rejected" as const,
  },
]);
</script>

<template>
  <!-- <div v-if="pending" class="mt-8 text-center">
    {{ t("municipalitiesId.loading.municipality") }}
  </div>

  <div v-else-if="error || !municipality" class="mt-8 text-center">
    {{ t("municipalitiesId.error.notFound", { id: municipalityId }) }}
  </div> -->

  <OfficialPage
    title="Affiliation requests"
    :description="`Manage the affiliation requests for ${municipality.name}.`"
  >
    <UAccordion type="multiple" :items="items">
      <template #all>
        <AffiliationRequestMenu
          :items="requests"
          empty-text="No requests were found."
        />
      </template>

      <template #pending>
        <AffiliationRequestMenu
          :items="pendingRequests"
          empty-text="No pending requests were found."
        />
      </template>

      <template #approved>
        <AffiliationRequestMenu
          :items="approvedRequests"
          empty-text="No approved requests were found."
        />
      </template>

      <template #rejected>
        <AffiliationRequestMenu
          :items="rejectedRequests"
          empty-text="No rejected requests were found."
        />
      </template>
    </UAccordion>
  </OfficialPage>
</template>
