<script setup lang="ts">
import { h, resolveComponent, ref } from "vue";
import { useRouter } from "#app";
import type { TableColumn } from "@nuxt/ui";
import { useI18n } from "vue-i18n";

const { t } = useI18n();

const UButton = resolveComponent("UButton");
const UBadge = resolveComponent("UBadge");
const UDropdownMenu = resolveComponent("UDropdownMenu");

type Problem = {
  id: string;
  date: string;
  title: string;
  status: "completed" | "in_progress" | "pending";
};

const router = useRouter();

const data = ref<Problem[]>([
  {
    id: "1001",
    date: "10/07/2025 09:30:00",
    title: "Buraco na rua",
    status: "pending",
  },
  {
    id: "1002",
    date: "09/07/2025 14:15:00",
    title: "Luz queimada",
    status: "in_progress",
  },
  {
    id: "1003",
    date: "08/07/2025 17:45:00",
    title: "Vazamento de água",
    status: "completed",
  },
  {
    id: "1004",
    date: "07/07/2025 11:20:00",
    title: "Coleta de lixo atrasada",
    status: "pending",
  },
  {
    id: "1005",
    date: "06/07/2025 16:00:00",
    title: "Árvore caída",
    status: "in_progress",
  },
]);

const totalProblems = computed(() => data.value.length);

const columns: TableColumn<Problem>[] = [
  {
    accessorKey: "id",
    header: t("lastProblems.id"),
    cell: ({ row }) => row.getValue("id"),
  },
  {
    accessorKey: "date",
    header: t("lastProblems.date"),
    cell: ({ row }) => row.getValue("date"),
  },
  {
    accessorKey: "title",
    header: t("lastProblems.problem"),
    cell: ({ row }) => row.getValue("title"),
  },
  {
    accessorKey: "status",
    header: t("lastProblems.status"),
    cell: ({ row }) => {
      const status = row.getValue("status") as Problem["status"];
      const color =
        status === "completed"
          ? "success"
          : status === "in_progress"
          ? "warning"
          : "error";
      const label = t(`statusTypes.${status}`);
      return h(
        UBadge,
        { class: "capitalize", variant: "subtle", color },
        () => label
      );
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
</script>

<template>
  <div class="rounded-md border border-default p-4 sm:px-6">
    <h3 class="text-lg font-semibold">
      {{ t("lastProblems.title") }} ({{ totalProblems }})
    </h3>
    <UTable ref="table" :data="data" :columns="columns" sticky />
  </div>
</template>
