<script setup lang="ts">
import { h, resolveComponent, ref, computed } from 'vue'
import type { TableColumn } from '@nuxt/ui'

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
  status: 'concluido' | 'em_andamento' | 'pendente'
}

const data = ref<Problem[]>([
  { id: '1001', date: '2025-07-10T09:30:00', title: 'Buraco na rua', status: 'pendente' },
  { id: '1002', date: '2025-07-09T14:15:00', title: 'Luz queimada em poste', status: 'em_andamento' },
  { id: '1003', date: '2025-07-08T17:45:00', title: 'Vazamento de água', status: 'concluido' },
  { id: '1004', date: '2025-07-07T11:20:00', title: 'Coleta de lixo atrasada', status: 'pendente' },
  { id: '1005', date: '2025-07-06T16:00:00', title: 'Árvore caída', status: 'em_andamento' },
  { id: '1006', date: '2025-07-05T10:30:00', title: 'Problema no semáforo', status: 'concluido' },
  { id: '1007', date: '2025-07-04T13:15:00', title: 'Falta de sinalização', status: 'pendente' },
  { id: '1008', date: '2025-07-03T15:45:00', title: 'Rua esburacada', status: 'em_andamento' },
  { id: '1009', date: '2025-07-02T12:00:00', title: 'Problema na calçada', status: 'concluido' },
  { id: '1010', date: '2025-07-01T08:30:00', title: 'Falta de iluminação pública', status: 'pendente' },
  { id: '1011', date: '2025-06-30T11:00:00', title: 'Bueiro entupido', status: 'concluido' },
  { id: '1012', date: '2025-06-29T19:00:00', title: 'Pichação em monumento', status: 'em_andamento' },
  { id: '1013', date: '2025-06-28T10:10:00', title: 'Terreno baldio com mato alto', status: 'pendente' },
  { id: '1014', date: '2025-06-27T08:45:00', title: 'Animal abandonado na praça', status: 'em_andamento' },
  { id: '1015', date: '2025-06-26T14:30:00', title: 'Fio de alta tensão solto', status: 'concluido' },
  { id: '1016', date: '2025-06-25T16:20:00', title: 'Obstrução de via pública', status: 'pendente' },
  { id: '1017', date: '2025-06-24T09:00:00', title: 'Barulho excessivo de obra', status: 'em_andamento' },
  { id: '1018', date: '2025-06-23T13:00:00', title: 'Lixo acumulado em esquina', status: 'concluido' },
  { id: '1019', date: '2025-06-22T18:00:00', title: 'Calçada quebrada', status: 'pendente' },
  { id: '1020', date: '2025-06-21T22:15:00', title: 'Veículo abandonado', status: 'em_andamento' },
  { id: '1021', date: '2025-06-20T17:50:00', title: 'Sinal de trânsito quebrado', status: 'concluido' },
  { id: '1022', date: '2025-06-19T11:40:00', title: 'Falta de faixa de pedestres', status: 'pendente' },
  { id: '1023', date: '2025-06-18T09:25:00', title: 'Invasão de área pública', status: 'em_andamento' },
  { id: '1024', date: '2025-06-17T15:00:00', title: 'Esgoto a céu aberto', status: 'concluido' },
  { id: '1025', date: '2025-06-16T10:05:00', title: 'Ponto de ônibus sem cobertura', status: 'pendente' },
  { id: '1026', date: '2025-06-15T12:30:00', title: 'Descarte irregular de entulho', status: 'em_andamento' },
  { id: '1027', date: '2025-06-14T14:55:00', title: 'Vazamento em hidrante', status: 'concluido' },
  { id: '1028', date: '2025-06-13T16:10:00', title: 'Comércio ambulante irregular', status: 'pendente' },
  { id: '1029', date: '2025-06-12T20:00:00', title: 'Iluminação de praça piscando', status: 'em_andamento' },
  { id: '1030', date: '2025-06-11T07:30:00', title: 'Limpeza de córrego necessária', status: 'concluido' },
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
    header: 'ID',
    cell: ({ row }) => row.getValue('id')
  },
  {
    accessorKey: 'date',
    header: 'Data',
    cell: ({ row }) => {
      return new Date(row.getValue('date')).toLocaleString('pt-BR', {
        day: '2-digit',
        month: '2-digit',
        year: 'numeric',
        hour: '2-digit',
        minute: '2-digit',
        hour12: false
      })
    }
  },
  {
    accessorKey: 'title',
    header: 'Problema',
    cell: ({ row }) => row.getValue('title')
  },
  {
    accessorKey: 'status',
    header: 'Status',
    cell: ({ row }) => {
      const status = row.getValue('status') as Problem['status']
      const color = status === 'concluido' ? 'success' : status === 'em_andamento' ? 'warning' : 'error'
      const label = status === 'concluido' ? 'Concluído' : status === 'em_andamento' ? 'Em Andamento' : 'Pendente'
      return h(UBadge, { class: 'capitalize', variant: 'subtle', color }, () => label)
    }
  },
  {
    id: 'actions',
    cell: ({ row }) => {
      const items = [
        { type: 'label', label: 'Ações' },
        { type: 'separator' },
        {
          label: 'Ver Detalhes',
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
        Últimos Problemas
      </h3>

      <div class="my-4 px-4">
        <UInput
          v-model="search"
          placeholder="Buscar por ID, título ou status..."
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