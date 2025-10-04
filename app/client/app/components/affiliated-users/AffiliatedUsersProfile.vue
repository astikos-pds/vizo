<script lang="ts" setup>
import type { BadgeProps, DropdownMenuItem } from "@nuxt/ui";
import type { AffiliatedUser } from "~/types/domain/affiliated-user";
import type { Pagination } from "~/types/domain/pagination";

const { t, locale } = useI18n();

const affiliatedUser = defineProps<AffiliatedUser>();

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

const items = computed<DropdownMenuItem[]>(() => {
  const items = [
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
          onSelect: () => assign(d.id),
        };
      }),
    },
  ];

  return items;
});

const adminBadge: BadgeProps = {
  color: "primary",
  variant: "solid",
  label: "ADMIN",
  icon: "i-lucide-shield",
};

const commonBadge: BadgeProps = {
  color: "neutral",
  variant: "subtle",
  label: "COMMON",
  icon: "i-lucide-user",
};

const badge = computed<BadgeProps>(() => {
  return { ...(affiliatedUser.isAdmin ? adminBadge : commonBadge), size: "md" };
});
</script>

<template>
  <li class="flex items-center justify-between gap-3 py-3 px-4 sm:px-6">
    <div class="flex items-center gap-3 min-w-0">
      <UAvatar
        v-bind="{
          src: affiliatedUser.user.avatarUrl?.toString(),
          alt: affiliatedUser.user.name,
        }"
        size="lg"
      />

      <div class="text-xs lg:text-sm min-w-0">
        <p class="text-highlighted font-medium truncate">
          {{ affiliatedUser.user.name }}
        </p>
        <p class="text-muted truncate">
          {{ affiliatedUser.institutionalEmail }}
        </p>
      </div>
    </div>

    <div class="flex items-center gap-3">
      <p v-if="affiliatedUser.approvedAt" class="text-xs xl:text-sm text-muted">
        Since
        {{
          affiliatedUser.approvedAt.toLocaleDateString(locale, {
            dateStyle: "long",
          })
        }}
      </p>

      <UBadge v-bind="badge" />

      <UDropdownMenu
        v-if="!affiliatedUser.isAdmin"
        :items="items"
        :content="{ align: 'end' }"
      >
        <UButton
          icon="i-lucide-ellipsis-vertical"
          color="neutral"
          variant="ghost"
        />
      </UDropdownMenu>
    </div>
  </li>
</template>
