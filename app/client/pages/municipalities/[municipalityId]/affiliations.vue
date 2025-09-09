<script lang="ts" setup>
import type { AccordionItem } from "@nuxt/ui";
import { useAffiliations } from "~/composables/use-affiliated-users";
import type { Municipality } from "~/types/domain";
import type { Pageable } from "~/types/http";

useHead({
  title: "Vizo | Affiliation requests",
  meta: [
    {
      name: "description",
      content: "Find all affiliation requests of this municipality",
    },
  ],
});

definePageMeta({
  layout: "official",
  middleware: ["auth", "official"],
});

const route = useRoute();
const municipalityId = route.params.municipalityId as string;

const { data: municipality } = useNuxtData<Municipality>(
  `municipality-${municipalityId}`
);

const pageable = reactive<Pageable>({
  page: 0,
  size: 100,
});

const { getAffiliationsByMunicipalityId } = useAffiliations();

const { data: page, pending } = await getAffiliationsByMunicipalityId(
  municipalityId,
  pageable
);

const affiliations = computed(() => page.value?.content);

const pendingRequests = computed(() =>
  (affiliations.value ?? []).filter((a) => a.status === "PENDING")
);

const approvedRequests = computed(() =>
  (affiliations.value ?? []).filter((a) => a.status === "APPROVED")
);

const rejectedRequests = computed(() =>
  (affiliations.value ?? []).filter((a) => a.status === "REJECTED")
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

const active = ref(["1"]);
</script>

<template>
  <OfficialPage
    title="Affiliation requests"
    :description="`Manage the affiliation requests for ${municipality?.name}.`"
  >
    <div v-if="pending || !affiliations">
      <USkeleton class="h-20 w-full mb-4" v-for="i in 2" :key="i" />
    </div>

    <UAccordion v-else type="multiple" v-model="active" :items="items">
      <template #all>
        <AffiliationRequestMenu
          :items="affiliations"
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
