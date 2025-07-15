<script lang="ts" setup>
import type { AccordionItem } from "@nuxt/ui";
import type { Municipality, Official } from "~/types/domain";

useHead({
  title: "Vizo | Officials",
  meta: [
    {
      name: "description",
      content: "Find all officials affiliated to municipality",
    },
  ],
});

definePageMeta({
  layout: "official",
  // middleware: ["auth", "official"],
});

const route = useRoute();
const municipalityId = route.params.municipalityId as string;

// const municipality = useNuxtData<Municipality>(`municipality-${municipalityId}`)
const municipality: Municipality = {
  id: "",
  name: "São Paulo",
  emailDomain: "",
  iconUrl: "",
  createdAt: "",
  updatedAt: "",
};

const officials = ref<Official[]>([
  {
    id: "",
    document: "",
    email: "mateus@gmail.com",
    name: "Mateus",
    role: "OFFICIAL",
    wasApproved: false,
    createdAt: "2023-06-04",
    updatedAt: "",
  },
  {
    id: "",
    document: "",
    email: "mateus@gmail.com",
    name: "Mateus",
    role: "OFFICIAL",
    wasApproved: false,
    createdAt: "2023-06-04",
    updatedAt: "",
  },
  {
    id: "",
    document: "",
    email: "mateus@gmail.com",
    name: "Mateus",
    role: "OFFICIAL",
    wasApproved: false,
    createdAt: "2023-06-04",
    updatedAt: "",
  },
  {
    id: "",
    document: "",
    email: "mateus@gmail.com",
    name: "Ana",
    role: "OFFICIAL",
    wasApproved: false,
    createdAt: "2023-06-04",
    updatedAt: "",
  },
  {
    id: "",
    document: "",
    email: "mateus@gmail.com",
    name: "Mateus",
    role: "OFFICIAL",
    wasApproved: false,
    createdAt: "2023-06-04",
    updatedAt: "",
  },
  {
    id: "",
    document: "",
    email: "mateus@gmail.com",
    name: "Mateus",
    role: "OFFICIAL",
    wasApproved: false,
    createdAt: "2023-06-04",
    updatedAt: "",
  },
  {
    id: "",
    document: "",
    email: "mateus@gmail.com",
    name: "Mateus",
    role: "ADMIN",
    wasApproved: false,
    createdAt: "2023-06-04",
    updatedAt: "",
  },
]);

const commomOfficials = computed(() =>
  officials.value
    .filter((o) => o.role === "OFFICIAL")
    .slice()
    .sort((a, b) => a.name.localeCompare(b.name))
);

const adminOfficials = computed(() =>
  officials.value
    .filter((o) => o.role === "ADMIN")
    .slice()
    .sort((a, b) => a.name.localeCompare(b.name))
);

const items = ref<AccordionItem[]>([
  {
    label: "Agents",
    icon: "i-lucide-sticky-note",
    slot: "agents" as const,
  },
  {
    label: "Administrators",
    icon: "i-lucide-shield",
    slot: "admins" as const,
  },
]);

const active = ref(["0", "1"]);
</script>

<template>
  <OfficialPage
    title="Officials"
    :description="`See below the officials affiliated with ${municipality.name}`"
    class="max-w-150"
  >
    <UAccordion type="multiple" v-model="active" :items="items">
      <template #agents>
        <OfficialList :items="commomOfficials" />
      </template>

      <template #admins>
        <OfficialList :items="adminOfficials" />
      </template>
    </UAccordion>
  </OfficialPage>
</template>
