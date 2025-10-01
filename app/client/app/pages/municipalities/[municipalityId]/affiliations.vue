<script lang="ts" setup>
import type { AccordionItem } from "@nuxt/ui";
import AffiliationsRequestSection from "~/components/affiliations/AffiliationsRequestSection.vue";
import { useAffiliationSection } from "~/composables/use-affiliation-section";

const { t } = useI18n();

useHead({
  title: t("head.affiliationRequests.title"),
  meta: [
    {
      name: "description",
      content: t("head.affiliationRequests.description"),
    },
  ],
});

definePageMeta({
  name: "Affiliation requests",
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
    label: t("pages.affiliationRequests.sections.all"),
    icon: "i-lucide-swatch-book",
    slot: "all" as const,
  },
  {
    label: t("pages.affiliationRequests.sections.pending"),
    icon: "i-lucide-clock",
    slot: "pending" as const,
  },
  {
    label: t("pages.affiliationRequests.sections.approved"),
    icon: "i-lucide-check",
    slot: "approved" as const,
  },
  {
    label: t("pages.affiliationRequests.sections.rejected"),
    icon: "i-lucide-ban",
    slot: "rejected" as const,
  },
]);

const active = ref(["1"]);
</script>

<template>
  <AffiliatedUsersPage
    v-if="currentAffiliation"
    :title="t('pages.affiliationRequests.title')"
    :description="
      t('pages.affiliationRequests.description', {
        municipalityName: currentAffiliation.municipality.name,
      })
    "
  >
    <UAccordion type="multiple" v-model="active" :items="items">
      <template #all>
        <div v-if="allSection.pending.value">
          <USkeleton class="h-20 w-full mb-4" v-for="i in 2" :key="i" />
        </div>

        <div v-else-if="!allSection.data.value">
          <EmptyMessage>{{
            t("pages.affiliationRequests.failedToFetch")
          }}</EmptyMessage>
        </div>

        <AffiliationsRequestSection
          v-else
          :items="allSection.data.value"
          :empty-text="t('pages.affiliationRequests.noRequests')"
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
          <EmptyMessage>{{
            t("pages.affiliationRequests.failedToFetch")
          }}</EmptyMessage>
        </div>

        <AffiliationsRequestSection
          v-else
          :items="pendingSection.data.value"
          :empty-text="t('pages.affiliationRequests.noRequests')"
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
          <EmptyMessage>{{
            t("pages.affiliationRequests.failedToFetch")
          }}</EmptyMessage>
        </div>

        <AffiliationsRequestSection
          v-else
          :items="approvedSection.data.value"
          :empty-text="t('pages.affiliationRequests.noRequests')"
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
          <EmptyMessage>{{
            t("pages.affiliationRequests.failedToFetch")
          }}</EmptyMessage>
        </div>

        <AffiliationsRequestSection
          v-else
          :items="rejectedSection.data.value"
          :empty-text="t('pages.affiliationRequests.noRequests')"
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
