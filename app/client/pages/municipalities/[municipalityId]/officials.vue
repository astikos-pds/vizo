<script lang="ts" setup>
import type { AccordionItem } from "@nuxt/ui";
import { useAffiliations } from "~/composables/use-affiliated-users";
import type { Municipality, Official } from "~/types/domain";
import type { Pageable } from "~/types/http";

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

const affiliations = computed(() => page.value?.content ?? []);

const officials = computed<Official[]>(() =>
  affiliations.value.map((a) => a.official)
);

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
    :description="`See below the officials affiliated with ${municipality?.name}`"
    class="max-w-150"
  >
    <div v-if="pending">
      <USkeleton class="h-20 w-full mb-4" v-for="i in 2" :key="i" />
    </div>

    <UAccordion v-else type="multiple" v-model="active" :items="items">
      <template #agents>
        <OfficialList :items="commomOfficials" />
      </template>

      <template #admins>
        <OfficialList :items="adminOfficials" />
      </template>
    </UAccordion>
  </OfficialPage>
</template>
