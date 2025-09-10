<script lang="ts" setup>
import type { AffiliatedUser } from "~/types/domain/affiliated-user";
import type { Pagination } from "~/types/domain/pagination";

const selectedFiliates = defineModel<AffiliatedUser[]>();

const route = useRoute();
const municipalityId = route.params.municipalityId as string;

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

const commomFiliates = computed(() => filiates.value.filter((f) => !f.isAdmin));

interface Item {
  label: string;
  suffix: string;
  avatar: { src?: string; alt: string };
}

const items = computed<Item[]>(() =>
  commomFiliates.value.map<Item>((f) => {
    return {
      label: f.user.name,
      suffix: f.institutionalEmail,
      avatar: {
        src: f.user.avatarUrl?.toString(),
        alt: f.user.name,
      },
    };
  })
);

const groups = ref([
  {
    id: "users",
    label: "Users",
    items: items,
  },
]);
</script>

<template>
  <UCommandPalette
    multiple
    :loading="pending"
    v-model="selectedFiliates"
    :groups="groups"
    placeholder="Search for a user..."
    class="flex-1"
  />
</template>
