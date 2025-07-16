<script setup lang="ts">
import { h, resolveComponent, ref, computed } from 'vue'
import type { TableColumn } from '@nuxt/ui'
import { useI18n } from 'vue-i18n'

const { t } = useI18n()

const UInput = resolveComponent('UInput')
const UButton = resolveComponent('UButton')
const UBadge = resolveComponent('UBadge')
const UDropdownMenu = resolveComponent('UDropdownMenu')

useHead({
  meta: [{ name: "viewport", content: "width=device-width, initial-scale=1.0" }],
});

definePageMeta({
  layout: 'admin',
})

type Problem = {
  id: string
  date: string
  title: string
  status: 'completed' | 'in_progress' | 'pending'
}

const data = ref<Problem[]>([
  { id: '1001', date: '2025-07-10 09:30:00', title: 'Buraco na rua', status: 'pending' },
  { id: '1002', date: '2025-07-09 14:15:00', title: 'Luz queimada em poste', status: 'in_progress' },
  { id: '1003', date: '2025-07-08 17:45:00', title: 'Vazamento de água', status: 'completed' },
  { id: '1001', date: '10/07/2025 09:30:00', title: 'Buraco na rua', status: 'pending' },
  { id: '1002', date: '09/07/2025 14:15:00', title: 'Luz queimada em poste', status: 'in_progress' },
  { id: '1003', date: '08/07/2025 17:45:00', title: 'Vazamento de água', status: 'completed' },
  { id: '1004', date: '07/07/2025 11:20:00', title: 'Coleta de lixo atrasada', status: 'pending' },
  { id: '1005', date: '06/07/2025 16:00:00', title: 'Árvore caída', status: 'in_progress' },
  { id: '1006', date: '05/07/2025 10:30:00', title: 'Problema no semáforo', status: 'completed' },
  { id: '1007', date: '04/07/2025 13:15:00', title: 'Falta de sinalização', status: 'pending' },
  { id: '1008', date: '03/07/2025 15:45:00', title: 'Rua esburacada', status: 'in_progress' },
  { id: '1009', date: '02/07/2025 12:00:00', title: 'Problema na calçada', status: 'completed' },
  { id: '1010', date: '01/07/2025 08:30:00', title: 'Falta de iluminação pública', status: 'pending' },
  { id: '1011', date: '30/06/2025 11:00:00', title: 'Bueiro entupido', status: 'completed' },
  { id: '1012', date: '29/06/2025 19:00:00', title: 'Pichação em monumento', status: 'in_progress' },
  { id: '1013', date: '28/06/2025 10:10:00', title: 'Terreno baldio com mato alto', status: 'pending' },
  { id: '1014', date: '27/06/2025 08:45:00', title: 'Animal abandonado na praça', status: 'in_progress' },
  { id: '1015', date: '26/06/2025 14:30:00', title: 'Fio de alta tensão solto', status: 'completed' },
  { id: '1016', date: '25/06/2025 16:20:00', title: 'Obstrução de via pública', status: 'pending' },
  { id: '1017', date: '24/06/2025 09:00:00', title: 'Barulho excessivo de obra', status: 'in_progress' },
  { id: '1018', date: '23/06/2025 13:00:00', title: 'Lixo acumulado em esquina', status: 'completed' },
  { id: '1019', date: '22/06/2025 18:00:00', title: 'Calçada quebrada', status: 'pending' },
  { id: '1020', date: '21/06/2025 22:15:00', title: 'Veículo abandonado', status: 'in_progress' },
  { id: '1021', date: '20/06/2025 17:50:00', title: 'Sinal de trânsito quebrado', status: 'completed' },
  { id: '1022', date: '19/06/2025 11:40:00', title: 'Falta de faixa de pedestres', status: 'pending' },
  { id: '1023', date: '18/06/2025 09:25:00', title: 'Invasão de área pública', status: 'in_progress' },
  { id: '1024', date: '17/06/2025 15:00:00', title: 'Esgoto a céu aberto', status: 'completed' },
  { id: '1025', date: '16/06/2025 10:05:00', title: 'Ponto de ônibus sem cobertura', status: 'pending' },
  { id: '1026', date: '15/06/2025 12:30:00', title: 'Descarte irregular de entulho', status: 'in_progress' },
  { id: '1027', date: '14/06/2025 14:55:00', title: 'Vazamento em hidrante', status: 'completed' },
  { id: '1028', date: '13/06/2025 16:10:00', title: 'Comércio ambulante irregular', status: 'pending' },
  { id: '1029', date: '12/06/2025 20:00:00', title: 'Iluminação de praça piscando', status: 'in_progress' },
  { id: '1030', date: '11/06/2025 07:30:00', title: 'Limpeza de córrego necessária', status: 'completed' },
])

const search = ref('')

import { reactive } from 'vue'

const pagination = reactive({
  page: 0,
  size: 15,
})

const currentPage = computed({
  get: () => (pagination.page ?? 0) + 1,
  set: (val: number) => (pagination.page = val - 1),
})

const filteredData = computed(() => {
  const term = search.value.trim().toLowerCase()
  let filtered = data.value
  if (term) {
    filtered = filtered.filter(item => {
      return (
        item.id.includes(term) ||
        item.title.toLowerCase().includes(term) ||
        item.status.toLowerCase().includes(term)
      )
    })
  }

  const start = pagination.page * pagination.size
  const end = start + pagination.size
  return filtered.slice(start, end)
})

const totalItems = computed(() => {
  const term = search.value.trim().toLowerCase()
  if (!term) return data.value.length
  return data.value.filter(item => {
    return (
      item.id.includes(term) ||
      item.title.toLowerCase().includes(term) ||
      item.status.toLowerCase().includes(term)
    )
  }).length
})

const columns: TableColumn<Problem>[] = [
  {
    accessorKey: 'id',
    header: t('problems.id'),
    cell: ({ row }) => row.getValue('id')
  },
  {
    accessorKey: 'date',
    header: t('problems.date'),
    cell: ({ row }) => row.getValue('date')
  },
  {
    accessorKey: 'title',
    header: t('problems.problem'),
    cell: ({ row }) => row.getValue('title')
  },
  {
    accessorKey: 'status',
    header: t('problems.status'),
    cell: ({ row }) => {
      const status = row.getValue('status') as Problem['status']
      const color = status === 'completed' ? 'success' : status === 'in_progress' ? 'warning' : 'error'
      const label = t(`problems.statusLabel.${status}`)
      return h(UBadge, { class: 'capitalize', variant: 'subtle', color }, () => label)
    }
  },
  {
    id: 'actions',
    cell: ({ row }) => {
      const items = [
        { type: 'label', label: t('problems.actions') },
        { type: 'separator' },
        {
          label: t('problems.viewDetails'),
          icon: 'i-lucide-arrow-right',
          onSelect: () => {
            // TODO: Implementar a navegação para os detalhes do problema
          }
        }
      ]
      return h(
        'div',
        { class: 'text-right' },
        h(
          UDropdownMenu,
          {
            content: { align: 'end' },
            items,
            'aria-label': 'Actions dropdown'
          },
          () =>
            h(UButton, {
              icon: 'i-lucide-ellipsis-vertical',
              color: 'neutral',
              variant: 'ghost',
              class: 'ml-auto',
              'aria-label': 'Actions dropdown'
            })
        )
      )
    }
  }
]
</script>

<template>
  <div class="grid grid-cols-1 gap-6 p-4 sm:p-6 md:p-8 md:grid-cols-1 xl:grid-cols-1">
    <div
      class="overflow-hidden rounded-2xl border border-gray-200 bg-white px-4 pb-3 pt-4 dark:border-gray-800 dark:bg-white/[0.03] sm:px-6"
    >
      <h3 class="text-lg font-semibold text-gray-800 dark:text-white/90">
        {{ t('problems.title') }}
      </h3>

      <div class="my-4 px-4">
        <UInput
          v-model="search"
          :placeholder="t('problems.searchPlaceholder')"
          clearable
          icon="i-lucide-search"
        />
      </div>

      <UTable
        ref="table"
        :data="filteredData"
        :columns="columns"
        sticky
        class="h-[935px]"
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