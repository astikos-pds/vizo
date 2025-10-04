<script lang="ts" setup>
import type { AccordionItem } from "@nuxt/ui";
import AffiliatedUsersList from "~/components/affiliated-users/AffiliatedUsersList.vue";
import { useAffiliatedUsers } from "~/composables/use-affiliated-users";
import type { Pagination } from "~/types/domain/pagination";

const { t } = useI18n();

useHead({
  title: t("head.publicAgents.title"),
  meta: [
    {
      name: "description",
      content: t("head.publicAgents.description"),
    },
  ],
});

definePageMeta({
  name: "Public agents",
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

const search = ref("");

const filteredFiliates = computed(() =>
  filiates.value
    .filter((filiates) => {
      return (
        filiates.user.name.search(new RegExp(search.value, "i")) !== -1 ||
        filiates.institutionalEmail.search(new RegExp(search.value, "i")) !== -1
      );
    })
    .sort((a, b) => a.user.name.localeCompare(b.user.name))
);
</script>

<template>
  <AffiliatedUsersPage
    v-if="currentAffiliation"
    :title="t('pages.publicAgents.title')"
    :description="
      t('pages.publicAgents.description', {
        municipalityName: currentAffiliation.municipality.name,
      })
    "
  >
    <div v-if="pending">
      <USkeleton class="h-20 w-full mb-4" v-for="i in 2" :key="i" />
    </div>

    <UPageCard
      v-else
      variant="subtle"
      :ui="{
        container: 'p-0 sm:p-0 gap-y-0',
        wrapper: 'items-stretch',
        header: 'p-4 mb-0 border-b border-default',
      }"
      class="w-full mt-3"
    >
      <template #header>
        <UInput
          v-model="search"
          icon="i-lucide-search"
          placeholder="Search members"
          autofocus
          class="w-full"
        />
      </template>

      <AffiliatedUsersList :items="filteredFiliates" class="w-full" />
    </UPageCard>

    <!-- <UAccordion v-else type="multiple" v-model="active" :items="items">
      <template #agents>
        <AffiliatedUsersList :items="commomFiliates" />
      </template>

      <template #admins>
        <AffiliatedUsersList :items="adminFiliates" />
      </template>
    </UAccordion> -->
  </AffiliatedUsersPage>
</template>
