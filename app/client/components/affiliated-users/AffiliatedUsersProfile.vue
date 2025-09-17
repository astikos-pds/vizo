<script lang="ts" setup>
import type { DropdownMenuItem } from "@nuxt/ui";
import type { AffiliatedUser } from "~/types/domain/affiliated-user";
import type { Pagination } from "~/types/domain/pagination";

const { t, locale } = useI18n();

const affiliatedUser = defineProps<AffiliatedUser>();

const profileLink = "";

const route = useRoute();
const municipalityId = route.params.municipalityId as string;

const pagination = reactive<Pagination>({
  page: 0,
  size: 100,
});

const { getMyAssignmentsInMunicipality } = useMe();

const { data: page } = await getMyAssignmentsInMunicipality(
  municipalityId,
  pagination
);

const { items: assignments } = usePagination(pagination, page);

const departments = computed(() => assignments.value.map((a) => a.department));

function assign(departmentId: string) {
  console.log(departmentId);
}

const items = ref<DropdownMenuItem[]>([
  {
    label: t("components.affiliatedUsers.assignToDepartment"),
    icon: "i-lucide-forward",
    children: departments.value?.map((d) => {
      return {
        label: d.name,
        avatar: {
          src: d.iconUrl?.toString(),
          alt: d.name,
        },
        onSelect: (_) => assign(d.id),
      };
    }),
  },
]);
</script>

<template>
  <UButton
    :to="profileLink"
    color="neutral"
    variant="ghost"
    :avatar="{
      src: affiliatedUser.user.avatarUrl?.toString(),
      alt: affiliatedUser.user.name,
      size: 'xl',
    }"
    size="xl"
  >
    <div class="ml-2 w-full flex justify-between item-center">
      <div class="flex flex-col items-start">
        <span class="text-sm 2xl:text-base">{{
          affiliatedUser.user.name
        }}</span>
        <span class="text-xs 2xl:text-sm text-muted font-normal">{{
          affiliatedUser.institutionalEmail
        }}</span>
      </div>

      <div class="my-auto">
        <UBadge
          :color="affiliatedUser.isAdmin ? 'info' : 'neutral'"
          variant="subtle"
          size="sm"
        >
          {{ affiliatedUser.isAdmin ? "ADMIN" : "COMMOM" }}
        </UBadge>
      </div>

      <div class="my-auto text-xs 2xl:text-sm font-normal">
        {{ t("components.affiliatedUsers.since") }}
        {{ affiliatedUser.affiliatedAt.toLocaleDateString(locale) }}
      </div>

      <div v-if="affiliatedUser.isAdmin" class="my-auto">
        <UDropdownMenu :items="items">
          <UButton
            icon="i-lucide-ellipsis-vertical"
            color="neutral"
            variant="ghost"
          />
        </UDropdownMenu>
      </div>
    </div>
  </UButton>
</template>
