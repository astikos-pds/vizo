<script setup lang="ts">
import { h, resolveComponent, ref, computed } from 'vue'
import { useRouter } from '#app'
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

const router = useRouter()

const data = ref<Problem[]>([
  { id: '1001', date: '2025-07-10T09:30:00', title: 'Buraco na rua', status: 'pendente' },
  { id: '1002', date: '2025-07-09T14:15:00', title: 'Luz queimada', status: 'em_andamento' },
  { id: '1003', date: '2025-07-08T17:45:00', title: 'Vazamento de água', status: 'concluido' },
  { id: '1004', date: '2025-07-07T11:20:00', title: 'Coleta de lixo atrasada', status: 'pendente' },
  { id: '1005', date: '2025-07-06T16:00:00', title: 'Árvore caída', status: 'em_andamento' }
])

const search = ref('')

const filteredData = computed(() => {
  const term = search.value.trim().toLowerCase()
  if (!term) return data.value

  return data.value.filter(item => {
    return (
      item.id.includes(term) ||
      item.title.toLowerCase().includes(term) ||
      item.status.toLowerCase().includes(term)
    )
  })
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
            router.push(`/municipality/problems/${row.original.id}`)
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
        class="h-96"
      />
    </div>
  </div>
</template>