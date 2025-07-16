<script setup lang="ts">
import { h, resolveComponent, ref } from "vue";
import type { BadgeProps, TableColumn } from "@nuxt/ui";
import { useI18n } from "vue-i18n";
import type { Problem, ProblemStatus } from "~/types/domain";
import type { Pageable } from "~/types/http";
import { municipalityRepository } from "~/repositories/municipality-repository";

const { t, locale } = useI18n();

const UButton = resolveComponent("UButton");
const UBadge = resolveComponent("UBadge");
const UIcon = resolveComponent("UIcon");
const UDropdownMenu = resolveComponent("UDropdownMenu");

const pagination = defineModel<Pageable>("pagination");

const { currentDepartment } = useDepartmentStore();
const municipalityId = computed(() => currentDepartment?.municipality.id ?? "");
const departmentId = computed(() => currentDepartment?.id ?? "");

const { data: problems } =
  await municipalityRepository.getAllVisibleProblemsInDepartment(
    municipalityId.value,
    departmentId.value,
    pagination.value,
    {
      key: `municipalities-${municipalityId.value}-departments-${departmentId}-problems`,
      watch: [municipalityId, departmentId, pagination],
    }
  );

const data = computed(() => problems.value?.content ?? []);

const colorByStatus: Record<ProblemStatus, BadgeProps["color"]> = {
  ANALYSIS: "warning",
  IN_PROGRESS: "info",
  SOLVED: "success",
  REJECTED: "error",
};

const columns: TableColumn<Problem>[] = [
  {
    accessorKey: "id",
    header: "#",
    cell: ({ row }) => row.getValue("id"),
  },
  {
    accessorKey: "reportedAt",
    header: "First reported at",
    cell: ({ row }) => {
      return new Date(Date.parse(row.original.firstReportedAt)).toLocaleString(
        locale.value,
        {
          day: "numeric",
          month: "short",
          hour: "2-digit",
          minute: "2-digit",
          hour12: false,
        }
      );
    },
  },
  {
    accessorKey: "type",
    header: "Type",
    cell: ({ row }) => row.original.type,
  },
  {
    accessorKey: "status",
    header: t("lastProblems.status"),
    cell: ({ row }) => {
      const status = row.getValue("status") as Problem["status"];
      const label = status;
      return h(
        UBadge,
        {
          class: "capitalize",
          variant: "subtle",
          color: colorByStatus[status],
        },
        () => label
      );
    },
  },
  {
    accessorKey: "validated",
    header: "Validated",
    cell: ({ row }) => {
      const validated = row.original.validated;
      const color = validated ? "success" : "error";
      const name = validated
        ? "i-lucide-circle-check"
        : "i-lucide-circle-alert";
      return h(UIcon, {
        class: `text-lg text-${color}`,
        color,
        name,
        mode: "svg",
      });
    },
  },
  {
    id: "actions",
    cell: ({ row }) => {
      const items = [
        { type: "label", label: t("lastProblems.actions") },
        { type: "separator" },
        {
          label: t("lastProblems.viewDetails"),
          icon: "i-lucide-arrow-right",
          to: `/problems/${row.original.id}`,
        },
      ];
      return h(
        "div",
        { class: "text-right" },
        h(
          UDropdownMenu,
          {
            content: { align: "end" },
            items,
            "aria-label": "Actions dropdown",
          },
          () =>
            h(UButton, {
              icon: "i-lucide-ellipsis-vertical",
              color: "neutral",
              variant: "ghost",
              class: "ml-auto",
              "aria-label": "Actions dropdown",
            })
        )
      );
    },
  },
];

const currentPage = computed({
  get: () => (pagination.value?.page ?? 0) + 1,
  set: (val: number) => {
    if (pagination.value) {
      pagination.value.page = val - 1;
    }
  },
});

const search = ref("");

const filteredData = computed(() => {
  const term = search.value.trim().toLowerCase();
  let filtered = data.value;
  if (term) {
    filtered = filtered.filter((item) => {
      return (
        item.id.includes(term) ||
        item.type.toLowerCase().includes(term) ||
        item.status.toLowerCase().includes(term)
      );
    });
  }

  const start = (pagination.value?.page ?? 0) * (pagination.value?.size ?? 0);
  const end = start + (pagination.value?.size ?? 0);
  return filtered.slice(start, end);
});
</script>

<template>
  <div
    class="rounded-md flex flex-col divide-y divide-accented border border-default"
  >
    <div class="flex flex-col gap-3 px-4 py-3.5">
      <h3 class="text-lg font-semibold">
        {{ t("lastProblems.title") }} ({{ problems?.totalElements }})
      </h3>
      <UInput
        v-model="search"
        :placeholder="t('problems.searchPlaceholder')"
        clearable
        icon="i-lucide-search"
        class="w-70"
      />
    </div>

    <UTable
      ref="table"
      :data="filteredData"
      :columns="columns"
      sticky
      class="flex1"
    />

    <div
      v-if="problems && problems.totalPages > 1"
      class="w-full flex justify-center items-center py-3.5"
    >
      <UPagination
        v-if="pagination"
        v-model:page="currentPage"
        :items-per-page="pagination.size"
        :total="problems?.totalElements"
      />
    </div>
  </div>
</template>
