<script lang="ts" setup>
import { ref } from 'vue'

const { t } = useI18n()

const problems = ref([
  {
    name: 'Buraco',
    variants: 2,
    neighborhood: 'Belém',
    status: 'Resolvido',
  },
  {
    name: 'Buraco',
    variants: 1,
    neighborhood: 'Brás',
    status: 'Em Andamento',
  },
  {
    name: 'Árvore Caída',
    variants: 2,
    neighborhood: 'Parque São Vicente',
    status: 'Resolvido',
  },
  {
    name: 'Enxente',
    variants: 2,
    neighborhood: 'Canindé',
    status: 'Em Análise',
  },
  {
    name: 'Semáforo Quebrado',
    variants: 1,
    neighborhood: 'Canindé',
    status: 'Resolvido',
  },
])
</script>

<template>
    <div
      class="overflow-hidden rounded-2xl border border-gray-200 bg-white px-4 pb-3 pt-4 dark:border-gray-800 dark:bg-white/[0.03] sm:px-6"
    >
      <div
        class="mb-4 flex flex-col gap-2 sm:flex-row sm:items-center sm:justify-between"
      >
        <div>
          <h3 class="text-lg font-semibold text-gray-800 dark:text-white/90">
            Últimos Problemas
          </h3>
        </div>

        <div class="flex items-center gap-3">
          <button
            class="inline-flex items-center gap-2 rounded-lg border border-gray-300 bg-white px-4 py-2.5 text-sm font-medium text-gray-700 shadow-theme-xs hover:bg-gray-50 hover:text-gray-800 dark:border-gray-700 dark:bg-gray-800 dark:text-gray-400 dark:hover:bg-white/[0.03] dark:hover:text-gray-200"
          >
            <svg
              class="fill-white stroke-current dark:fill-gray-800"
              width="20"
              height="20"
              viewBox="0 0 20 20"
              fill="none"
              xmlns="http://www.w3.org/2000/svg"
            >
              <path
                d="M2.29004 5.90393H17.7067"
                stroke=""
                stroke-width="1.5"
                stroke-linecap="round"
                stroke-linejoin="round"
              />
              <path
                d="M17.7075 14.0961H2.29085"
                stroke=""
                stroke-width="1.5"
                stroke-linecap="round"
                stroke-linejoin="round"
              />
              <path
                d="M12.0826 3.33331C13.5024 3.33331 14.6534 4.48431 14.6534 5.90414C14.6534 7.32398 13.5024 8.47498 12.0826 8.47498C10.6627 8.47498 9.51172 7.32398 9.51172 5.90415C9.51172 4.48432 10.6627 3.33331 12.0826 3.33331Z"
                fill=""
                stroke=""
                stroke-width="1.5"
              />
              <path
                d="M7.91745 11.525C6.49762 11.525 5.34662 12.676 5.34662 14.0959C5.34661 15.5157 6.49762 16.6667 7.91745 16.6667C9.33728 16.6667 10.4883 15.5157 10.4883 14.0959C10.4883 12.676 9.33728 11.525 7.91745 11.525Z"
                fill=""
                stroke=""
                stroke-width="1.5"
              />
            </svg>

            Filtrar
          </button>

          <button
            class="inline-flex items-center gap-2 rounded-lg border border-gray-300 bg-white px-4 py-2.5 text-sm font-medium text-gray-700 shadow-theme-xs hover:bg-gray-50 hover:text-gray-800 dark:border-gray-700 dark:bg-gray-800 dark:text-gray-400 dark:hover:bg-white/[0.03] dark:hover:text-gray-200"
          >
            Ver Todos
          </button>
        </div>
      </div>

      <div class="custom-scrollbar max-w-full overflow-x-auto">
        <table class="min-w-full">
          <thead>
            <tr class="border-t border-gray-100 dark:border-gray-800">
              <th class="py-3 text-left">
                <p class="text-xs font-medium text-gray-500 dark:text-gray-400">
                  Problema
                </p>
              </th>
              <th class="py-3 text-left">
                <p class="text-xs font-medium text-gray-500 dark:text-gray-400">
                  Bairro
                </p>
              </th>
              <th class="py-3 text-left">
                <p class="text-xs font-medium text-gray-500 dark:text-gray-400">
                  Status
                </p>
              </th>
              <th class="py-3 text-left">
                <p class="text-xs font-medium text-gray-500 dark:text-gray-400">
                  Ações
                </p>
              </th>
            </tr>
          </thead>
          <tbody>
            <tr
              v-for="(problem, index) in problems"
              :key="index"
              class="border-t border-gray-100 dark:border-gray-800"
            >
              <td class="whitespace-nowrap py-3">
                <div class="flex items-center gap-3">
                  <div>
                    <p
                      class="text-sm font-medium text-gray-800 dark:text-white/90"
                    >
                      {{ problem.name }}
                    </p>
                    <span class="text-xs text-gray-500 dark:text-gray-400"
                      >{{ problem.variants }} Reportes</span
                    >
                  </div>
                </div>
              </td>
              <td class="whitespace-nowrap py-3">
                <p class="text-sm text-gray-500 dark:text-gray-400">
                  {{ problem.neighborhood }}
                </p>
              </td>
              <td class="whitespace-nowrap py-3">
                <span
                  :class="{
                    'rounded-full px-2 py-0.5 text-xs font-medium': true,
                    'bg-success-50 text-success-600 dark:bg-success-500/15 dark:text-success-500': problem.status === 'Resolvido',
                    'bg-warning-50 text-warning-600 dark:bg-warning-500/15 dark:text-orange-400': problem.status === 'Em Andamento',
                    'bg-error-50 text-error-600 dark:bg-error-500/15 dark:text-error-500': problem.status === 'Em Análise',
                  }"
                >
                  {{ problem.status }}
                </span>
              </td>
              <td class="whitespace-nowrap py-3">
                <a
                  href="#"
                  class="text-primary-600 hover:underline text-xs font-medium"
                >
                  Ver mais
                </a>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
</template>