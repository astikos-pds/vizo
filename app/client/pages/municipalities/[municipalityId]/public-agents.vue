<script lang="ts" setup>
import type { AccordionItem } from "@nuxt/ui";
import AffiliatedUsersList from "~/components/affiliated-users/AffiliatedUsersList.vue";
import { useAffiliatedUsers } from "~/composables/use-affiliated-users";
import type { Pagination } from "~/types/domain/pagination";

useHead({
  title: "Vizo | Public agents",
  meta: [
    {
      name: "description",
      content: "Find all public agents affiliated to municipality",
    },
  ],
});

definePageMeta({
  middleware: ["auth", "affiliated"],
});

const route = useRoute();
const municipalityId = route.params.municipalityId as string;

const { currentAffiliation } = useLoggedInUserStore();

const pagination = reactive<Pagination>({
  page: 0,
  size: 100,
});

const { getUsersAffiliatedToMunicipality } = useAffiliatedUsers();

const { data: page, pending } = await getUsersAffiliatedToMunicipality(
  municipalityId,
  pagination
);

const { items: filiates } = usePagination(pagination, page);

const commomFiliates = computed(() =>
  filiates.value
    .filter((f) => !f.isAdmin)
    .slice()
    .sort((a, b) => a.user.name.localeCompare(b.user.name))
);

const adminFiliates = computed(() =>
  filiates.value
    .filter((f) => f.isAdmin)
    .slice()
    .sort((a, b) => a.user.name.localeCompare(b.user.name))
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
  <AffiliatedUsersPage
    v-if="currentAffiliation"
    title="Public agents"
    :description="`See below the public agents affiliated with ${currentAffiliation.municipality.name}`"
    class="max-w-150 2xl:max-w-170"
  >
    <div v-if="pending">
      <USkeleton class="h-20 w-full mb-4" v-for="i in 2" :key="i" />
    </div>

    <UAccordion v-else type="multiple" v-model="active" :items="items">
      <template #agents>
        <AffiliatedUsersList :items="commomFiliates" />
      </template>

      <template #admins>
        <AffiliatedUsersList :items="adminFiliates" />
      </template>
    </UAccordion>
  </AffiliatedUsersPage>
</template>
