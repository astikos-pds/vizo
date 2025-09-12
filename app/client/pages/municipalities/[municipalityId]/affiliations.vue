<script lang="ts" setup>
import type { AccordionItem } from "@nuxt/ui";
import AffiliationsRequestSection from "~/components/affiliations/AffiliationsRequestSection.vue";
import { useAffiliationSection } from "~/composables/use-affiliation-section";

useHead({
  title: "Vizo | Affiliation requests",
  meta: [
    {
      name: "description",
      content: "Manage all affiliation requests of this municipality",
    },
  ],
});

definePageMeta({
  middleware: ["auth", "affiliated", "affiliated-as-admin"],
});

const route = useRoute();
const municipalityId = route.params.municipalityId as string;

const { currentAffiliation } = useLoggedInUserStore();

const allSection = await useAffiliationSection(municipalityId);
const pendingSection = await useAffiliationSection(municipalityId, {
  status: "PENDING",
});
const approvedSection = await useAffiliationSection(municipalityId, {
  status: "APPROVED",
});
const rejectedSection = await useAffiliationSection(municipalityId, {
  status: "REJECTED",
});

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
  <AffiliatedUsersPage
    v-if="currentAffiliation"
    title="Affiliation requests"
    :description="`Manage the affiliation requests for ${currentAffiliation.municipality.name}.`"
  >
    <UAccordion type="multiple" v-model="active" :items="items">
      <template #all>
        <div v-if="allSection.pending.value">
          <USkeleton class="h-20 w-full mb-4" v-for="i in 2" :key="i" />
        </div>

        <div v-else-if="!allSection.data.value">
          <NotFoundMessage>Failed to fetch all affiliations.</NotFoundMessage>
        </div>

        <AffiliationsRequestSection
          v-else
          :items="allSection.data.value"
          empty-text="No requests were found."
        >
          <UPagination
            v-model:page="allSection.currentPage.value"
            :items-per-page="allSection.pagination.size"
            :total="allSection.totalElements.value"
          />
        </AffiliationsRequestSection>
      </template>

      <template #pending>
        <div v-if="pendingSection.pending.value">
          <USkeleton class="h-20 w-full mb-4" v-for="i in 2" :key="i" />
        </div>

        <div v-else-if="!pendingSection.data.value">
          <NotFoundMessage>Failed to fetch all affiliations.</NotFoundMessage>
        </div>

        <AffiliationsRequestSection
          v-else
          :items="pendingSection.data.value"
          empty-text="No requests were found."
        >
          <UPagination
            v-model:page="pendingSection.currentPage.value"
            :items-per-page="pendingSection.pagination.size"
            :total="pendingSection.totalElements.value"
          />
        </AffiliationsRequestSection>
      </template>

      <template #approved>
        <div v-if="approvedSection.pending.value">
          <USkeleton class="h-20 w-full mb-4" v-for="i in 2" :key="i" />
        </div>

        <div v-else-if="!approvedSection.data.value">
          <NotFoundMessage>Failed to fetch all affiliations.</NotFoundMessage>
        </div>

        <AffiliationsRequestSection
          v-else
          :items="approvedSection.data.value"
          empty-text="No requests were found."
        >
          <UPagination
            v-model:page="approvedSection.currentPage.value"
            :items-per-page="approvedSection.pagination.size"
            :total="approvedSection.totalElements.value"
          />
        </AffiliationsRequestSection>
      </template>

      <template #rejected>
        <div v-if="rejectedSection.pending.value">
          <USkeleton class="h-20 w-full mb-4" v-for="i in 2" :key="i" />
        </div>

        <div v-else-if="!rejectedSection.data.value">
          <NotFoundMessage>Failed to fetch affiliations.</NotFoundMessage>
        </div>

        <AffiliationsRequestSection
          v-else
          :items="rejectedSection.data.value"
          empty-text="No requests were found."
        >
          <UPagination
            v-model:page="rejectedSection.currentPage.value"
            :items-per-page="rejectedSection.pagination.size"
            :total="rejectedSection.totalElements.value"
          />
        </AffiliationsRequestSection>
      </template>
    </UAccordion>
  </AffiliatedUsersPage>
</template>
