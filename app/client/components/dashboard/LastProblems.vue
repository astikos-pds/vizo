<script lang="ts" setup>
import { ref } from 'vue'
import { useI18n } from 'vue-i18n'

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
            {{ t('lastProblems.title') }}
          </h3>
        </div>
      </div>

      <div class="custom-scrollbar max-w-full overflow-x-auto">
        <table class="min-w-full">
          <thead>
            <tr class="border-t border-gray-100 dark:border-gray-800">
              <th class="py-3 text-left">
                <p class="text-xs font-medium text-gray-500 dark:text-gray-400">
                  {{ t('lastProblems.problem') }}
                </p>
              </th>
              <th class="py-3 text-left">
                <p class="text-xs font-medium text-gray-500 dark:text-gray-400">
                  {{ t('lastProblems.neighborhood') }}
                </p>
              </th>
              <th class="py-3 text-left">
                <p class="text-xs font-medium text-gray-500 dark:text-gray-400">
                  {{ t('lastProblems.status') }}
                </p>
              </th>
              <th class="py-3 text-left">
                <p class="text-xs font-medium text-gray-500 dark:text-gray-400">
                  {{ t('lastProblems.actions') }}
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
                      >{{ problem.variants }} {{ t('lastProblems.reports') }}</span
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
                  {{ t('lastProblems.statuses.' + problem.status) }}
                </span>
              </td>
              <td class="whitespace-nowrap py-3">
                <a
                  href="#"
                  class="text-primary-600 hover:underline text-xs font-medium"
                >
                  {{ t('lastProblems.seeMore') }}
                </a>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
</template>