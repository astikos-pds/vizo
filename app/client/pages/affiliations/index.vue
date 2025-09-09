<script lang="ts" setup>
import type { AccordionItem } from "@nuxt/ui";
import type { AffiliatedUser } from "~/types/domain/affiliated-user";
import type { PageDTO, Pagination } from "~/types/domain/pagination";

useHead({
  title: "Vizo | Affiliations",
  meta: [
    {
      name: "description",
      content: "View all yours approved and requested affiliations.",
    },
  ],
});

definePageMeta({
  middleware: ["auth"],
});

const approvedAffiliationsPagination = reactive<Pagination>({
  page: 0,
  size: 15,
});

const approvedAffiliationsPage = ref<PageDTO<AffiliatedUser> | null>({
  content: [
    {
      id: "123",
      user: {
        id: "",
        document: "",
        email: "",
        name: "",
        avatarUrl: null,
        credibilityPoints: 0,
        createdAt: new Date(),
        updatedAt: new Date(),
      },
      municipality: {
        id: "123",
        name: "São Paulo",
        emailDomain: "prefeitura.sp",
        iconUrl: null,
        createdAt: new Date(),
        updatedAt: new Date(),
      },
      institutionalEmail: "",
      isAdmin: false,
      status: "APPROVED",
      affiliatedAt: new Date(),
      approver: {
        id: "",
        user: {
          id: "",
          document: "",
          email: "",
          name: "Mateus",
          avatarUrl: null,
          credibilityPoints: 0,
          createdAt: new Date(),
          updatedAt: new Date(),
        },
        municipality: {
          id: "",
          name: "",
          emailDomain: "",
          iconUrl: null,
          createdAt: new Date(),
          updatedAt: new Date(),
        },
        institutionalEmail: "",
        isAdmin: false,
        status: "APPROVED",
        affiliatedAt: new Date(),
        approver: null,
        approvedAt: null,
      },
      approvedAt: new Date(),
    },
  ],
  page: 0,
  size: 15,
  totalPages: 1,
  totalElements: 15,
});

const {
  currentPage: approvedAffiliationsCurrentPage,
  totalElements: approvedAffiliationsTotalElements,
} = usePagination(approvedAffiliationsPagination, approvedAffiliationsPage);

const requestedAffiliationsPagination = reactive<Pagination>({
  page: 0,
  size: 15,
});

const requestedAffiliationsPage = ref<PageDTO<AffiliatedUser> | null>({
  content: [
    {
      id: "123",
      user: {
        id: "",
        document: "",
        email: "",
        name: "",
        avatarUrl: null,
        credibilityPoints: 0,
        createdAt: new Date(),
        updatedAt: new Date(),
      },
      municipality: {
        id: "123",
        name: "São Paulo",
        emailDomain: "prefeitura.sp",
        iconUrl: null,
        createdAt: new Date(),
        updatedAt: new Date(),
      },
      institutionalEmail: "",
      isAdmin: false,
      status: "PENDING",
      affiliatedAt: new Date(),
      approver: {
        id: "",
        user: {
          id: "",
          document: "",
          email: "",
          name: "Mateus",
          avatarUrl: null,
          credibilityPoints: 0,
          createdAt: new Date(),
          updatedAt: new Date(),
        },
        municipality: {
          id: "",
          name: "",
          emailDomain: "",
          iconUrl: null,
          createdAt: new Date(),
          updatedAt: new Date(),
        },
        institutionalEmail: "",
        isAdmin: false,
        status: "APPROVED",
        affiliatedAt: new Date(),
        approver: null,
        approvedAt: null,
      },
      approvedAt: new Date(),
    },
    {
      id: "123",
      user: {
        id: "",
        document: "",
        email: "",
        name: "",
        avatarUrl: null,
        credibilityPoints: 0,
        createdAt: new Date(),
        updatedAt: new Date(),
      },
      municipality: {
        id: "123",
        name: "São Paulo",
        emailDomain: "prefeitura.sp",
        iconUrl: null,
        createdAt: new Date(),
        updatedAt: new Date(),
      },
      institutionalEmail: "",
      isAdmin: false,
      status: "APPROVED",
      affiliatedAt: new Date(),
      approver: {
        id: "",
        user: {
          id: "",
          document: "",
          email: "",
          name: "Mateus",
          avatarUrl: null,
          credibilityPoints: 0,
          createdAt: new Date(),
          updatedAt: new Date(),
        },
        municipality: {
          id: "",
          name: "",
          emailDomain: "",
          iconUrl: null,
          createdAt: new Date(),
          updatedAt: new Date(),
        },
        institutionalEmail: "",
        isAdmin: false,
        status: "APPROVED",
        affiliatedAt: new Date(),
        approver: null,
        approvedAt: null,
      },
      approvedAt: new Date(),
    },
    {
      id: "123",
      user: {
        id: "",
        document: "",
        email: "",
        name: "",
        avatarUrl: null,
        credibilityPoints: 0,
        createdAt: new Date(),
        updatedAt: new Date(),
      },
      municipality: {
        id: "123",
        name: "São Paulo",
        emailDomain: "prefeitura.sp",
        iconUrl: null,
        createdAt: new Date(),
        updatedAt: new Date(),
      },
      institutionalEmail: "",
      isAdmin: false,
      status: "REJECTED",
      affiliatedAt: new Date(),
      approver: {
        id: "",
        user: {
          id: "",
          document: "",
          email: "",
          name: "Mateus",
          avatarUrl: null,
          credibilityPoints: 0,
          createdAt: new Date(),
          updatedAt: new Date(),
        },
        municipality: {
          id: "",
          name: "",
          emailDomain: "",
          iconUrl: null,
          createdAt: new Date(),
          updatedAt: new Date(),
        },
        institutionalEmail: "",
        isAdmin: false,
        status: "APPROVED",
        affiliatedAt: new Date(),
        approver: null,
        approvedAt: null,
      },
      approvedAt: new Date(),
    },
  ],
  page: 0,
  size: 15,
  totalPages: 2,
  totalElements: 18,
});

const {
  currentPage: requestedAffiliationsCurrentPage,
  totalElements: requestedAffiliationsTotalElements,
} = usePagination(requestedAffiliationsPagination, requestedAffiliationsPage);

const items = ref<AccordionItem[]>([
  {
    label: "Municipalities",
    icon: "i-lucide-building-2",
    slot: "approved",
  },
  {
    label: "Requested affiliations",
    icon: "i-lucide-clock",
    slot: "requested",
  },
]);

const openSections = ref(["0", "1"]);
</script>

<template>
  <AffiliationsPage>
    <div class="h-full w-[85%] 2xl:w-[70%]">
      <header class="w-full py-5 text-center flex flex-col gap-2">
        <h1 class="text-3xl font-semibold">Affiliations</h1>
        <span class="text-sm"
          >Below are listed the municipalities you are affiliated with and your
          pending affiliation requests.</span
        >
      </header>

      <UAccordion :items="items" type="multiple" v-model="openSections">
        <template #approved>
          <AffiliationsSection
            v-if="approvedAffiliationsPage"
            :items="approvedAffiliationsPage"
            empty-text="No approved affiliations"
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
            empty-text="No requested affiliations"
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
