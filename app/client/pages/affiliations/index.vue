<script lang="ts" setup>
import type { AccordionItem } from "@nuxt/ui";
import type { Pagination } from "~/types/domain/pagination";

const { t } = useI18n();

useHead({
  title: t("head.affiliations.title"),
  meta: [
    {
      name: "description",
      content: t("head.affiliations.description"),
    },
  ],
});

definePageMeta({
  name: "Affiliations",
  middleware: ["auth"],
});

const { getMyAffiliations } = useMe();

const approvedAffiliationsPagination = reactive<Pagination>({
  page: 0,
  size: 15,
});

const { data: approvedAffiliationsPage } = await getMyAffiliations({
  ...approvedAffiliationsPagination,
  status: "APPROVED",
});

const {
  currentPage: approvedAffiliationsCurrentPage,
  totalElements: approvedAffiliationsTotalElements,
} = usePagination(approvedAffiliationsPagination, approvedAffiliationsPage);

const requestedAffiliationsPagination = reactive<Pagination>({
  page: 0,
  size: 15,
});

const { data: requestedAffiliationsPage } = await getMyAffiliations({
  ...requestedAffiliationsPagination,
});

const {
  currentPage: requestedAffiliationsCurrentPage,
  totalElements: requestedAffiliationsTotalElements,
} = usePagination(requestedAffiliationsPagination, requestedAffiliationsPage);

const items = ref<AccordionItem[]>([
  {
    label: t("pages.affiliations.municipalities"),
    icon: "i-lucide-building-2",
    slot: "approved",
  },
  {
    label: t("pages.affiliations.requestedAffiliations"),
    icon: "i-lucide-clock",
    slot: "requested",
  },
]);

const openSections = ref(["0", "1"]);
</script>

<template>
  <AffiliationsPage>
    <div class="h-full w-[85%] 2xl:w-[70%]">
      <header class="w-full py-6 mt-10 text-center flex flex-col gap-2">
        <h1 class="text-3xl font-semibold">
          {{ t("pages.affiliations.title") }}
        </h1>
        <span class="text-sm">{{ t("pages.affiliations.description") }}</span>
      </header>

      <UAccordion :items="items" type="multiple" v-model="openSections">
        <template #approved>
          <AffiliationsSection
            v-if="approvedAffiliationsPage"
            :items="approvedAffiliationsPage"
            :empty-text="t('pages.affiliations.noApprovedAffiliations')"
            purpose="APPROVED"
          >
            <template #pagination>
              <UPagination
                v-model:page="approvedAffiliationsCurrentPage"
                :items-per-page="approvedAffiliationsPagination.size"
                :total="approvedAffiliationsTotalElements"
              />
            </template>
          </AffiliationsSection>
        </template>

        <template #requested>
          <AffiliationsSection
            v-if="requestedAffiliationsPage"
            :items="requestedAffiliationsPage"
            :empty-text="t('pages.affiliations.noRequestedAffiliations')"
            purpose="REQUESTED"
          >
            <template #pagination>
              <UPagination
                v-model:page="requestedAffiliationsCurrentPage"
                :items-per-page="requestedAffiliationsPagination.size"
                :total="requestedAffiliationsTotalElements"
              />
            </template>
          </AffiliationsSection>
        </template>
      </UAccordion>
    </div>
  </AffiliationsPage>
</template>
