<script setup lang="ts">
import { h, resolveComponent, ref, computed } from "vue";
import type { TableColumn } from "@nuxt/ui";

const UInput = resolveComponent("UInput");

const { t } = useI18n();

useHead({
  title: "Vizo | Notificações",
  meta: [{ name: "description", content: "Notificações recentes do sistema." }],
});

type Notification = {
  id: string;
  date: string;
  title: string;
  description: string;
};

const notifications = ref<Notification[]>([
  {
    id: "2001",
    date: "15/07/2025 09:00",
    title: "Novo problema reportado",
    description: "Um novo problema foi reportado na sua área.",
  },
  {
    id: "2002",
    date: "14/07/2025 16:30",
    title: "Problema resolvido",
    description: 'O problema "Buraco na rua" foi marcado como concluído.',
  },
  {
    id: "2003",
    date: "13/07/2025 11:45",
    title: "Comentário em relatório",
    description: "Seu relatório recebeu um novo comentário.",
  },
  {
    id: "2004",
    date: "12/07/2025 14:10",
    title: "Atualização de status",
    description:
      'O status do problema "Luz queimada em poste" foi alterado para "Em andamento".',
  },
  {
    id: "2005",
    date: "11/07/2025 18:20",
    title: "Nova mensagem",
    description: "Você recebeu uma nova mensagem da administração.",
  },
]);

const search = ref("");

import { reactive } from "vue";

const pagination = reactive({
  page: 0,
  size: 10,
});

const currentPage = computed({
  get: () => (pagination.page ?? 0) + 1,
  set: (val: number) => (pagination.page = val - 1),
});

const filteredData = computed(() => {
  const term = search.value.trim().toLowerCase();
  let filtered = notifications.value;
  if (term) {
    filtered = filtered.filter((item) => {
      return (
        item.id.includes(term) ||
        item.title.toLowerCase().includes(term) ||
        item.description.toLowerCase().includes(term)
      );
    });
  }
  const start = pagination.page * pagination.size;
  const end = start + pagination.size;
  return filtered.slice(start, end);
});

const totalItems = computed(() => {
  const term = search.value.trim().toLowerCase();
  if (!term) return notifications.value.length;
  return notifications.value.filter((item) => {
    return (
      item.id.includes(term) ||
      item.title.toLowerCase().includes(term) ||
      item.description.toLowerCase().includes(term)
    );
  }).length;
});

const columns: TableColumn<Notification>[] = [
  {
    accessorKey: "date",
    header: t("notifications.table.date"),
    cell: ({ row }) => row.getValue("date"),
  },
  {
    accessorKey: "title",
    header: t("notifications.table.title"),
    cell: ({ row }) => row.getValue("title"),
  },
  {
    accessorKey: "description",
    header: t("notifications.table.description"),
    cell: ({ row }) => row.getValue("description"),
  },
];
</script>

<template>
  <div
    class="grid grid-cols-1 gap-6 p-4 sm:p-6 md:p-8 md:grid-cols-1 xl:grid-cols-1"
  >
    <div
      class="overflow-hidden rounded-2xl border border-gray-200 bg-white px-4 pb-3 pt-4 dark:border-gray-800 dark:bg-white/[0.03] sm:px-6"
    >
      <h3 class="text-lg font-semibold text-gray-800 dark:text-white/90">
        {{ t("notifications.title") }}
      </h3>

      <div class="my-4 px-4">
        <UInput
          v-model="search"
          :placeholder="t('notifications.searchPlaceholder')"
          clearable
          icon="i-lucide-search"
        />
      </div>

      <UTable
        ref="table"
        :data="filteredData"
        :columns="columns"
        sticky
        class="h-[600px]"
      />
      <div class="mt-4 flex justify-center">
        <UPagination
          v-model:page="currentPage"
          :items-per-page="pagination.size"
          :total="totalItems"
        />
      </div>
    </div>
  </div>
</template>
