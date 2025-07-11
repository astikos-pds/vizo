<script lang="ts" setup>
import { ref } from 'vue'
import { useI18n } from 'vue-i18n'

const { t } = useI18n()

const problems = ref([
  { name: 'Buraco', variants: 2, neighborhood: 'Belém', status: 'Resolvido' },
  { name: 'Buraco', variants: 1, neighborhood: 'Brás', status: 'Em Andamento' },
  { name: 'Árvore Caída', variants: 2, neighborhood: 'Parque São Vicente', status: 'Resolvido' },
  { name: 'Enxente', variants: 2, neighborhood: 'Canindé', status: 'Em Análise' },
  { name: 'Semáforo Quebrado', variants: 1, neighborhood: 'Canindé', status: 'Resolvido' },
])

const reports = ref([
  { user: '@joao', description: 'Encontrei esse buraco', img: 'https://s2-g1.glbimg.com/BXoCVbSSUMqwk8SrldbMK3pYYbg=/0x0:1280x960/984x0/smart/filters:strip_icc()/i.s3.glbimg.com/v1/AUTH_59edd422c0c84a879bd37670ae4f538a/internal_photos/bs/2018/1/p/JbO1BoTCu5FmmTAWCQvA/cratera-joao-pessoa-bayeux.jpg' },
  { user: '@maria', description: 'Árvore caída na praça', img: 'https://s2-g1.glbimg.com/BXoCVbSSUMqwk8SrldbMK3pYYbg=/0x0:1280x960/984x0/smart/filters:strip_icc()/i.s3.glbimg.com/v1/AUTH_59edd422c0c84a879bd37670ae4f538a/internal_photos/bs/2018/1/p/JbO1BoTCu5FmmTAWCQvA/cratera-joao-pessoa-bayeux.jpg' },
  //{ user: '@pedro', description: 'Semáforo quebrado na esquina', img: 'https://s2-g1.glbimg.com/BXoCVbSSUMqwk8SrldbMK3pYYbg=/0x0:1280x960/984x0/smart/filters:strip_icc()/i.s3.glbimg.com/v1/AUTH_59edd422c0c84a879bd37670ae4f538a/internal_photos/bs/2018/1/p/JbO1BoTCu5FmmTAWCQvA/cratera-joao-pessoa-bayeux.jpg' },
  //{ user: '@ana', description: 'Enxente na rua principal', img: 'https://s2-g1.glbimg.com/BXoCVbSSUMqwk8SrldbMK3pYYbg=/0x0:1280x960/984x0/smart/filters:strip_icc()/i.s3.glbimg.com/v1/AUTH_59edd422c0c84a879bd37670ae4f538a/internal_photos/bs/2018/1/p/JbO1BoTCu5FmmTAWCQvA/cratera-joao-pessoa-bayeux.jpg' },
  //{ user: '@lucas', description: 'Buraco na calçada', img: 'https://s2-g1.glbimg.com/BXoCVbSSUMqwk8SrldbMK3pYYbg=/0x0:1280x960/984x0/smart/filters:strip_icc()/i.s3.glbimg.com/v1/AUTH_59edd422c0c84a879bd37670ae4f538a/internal_photos/bs/2018/1/p/JbO1BoTCu5FmmTAWCQvA/cratera-joao-pessoa-bayeux.jpg' },
  //{ user: '@fernanda', description: 'Árvore caída na praça central', img: 'https://s2-g1.glbimg.com/BXoCVbSSUMqwk8SrldbMK3pYYbg=/0x0:1280x960/984x0/smart/filters:strip_icc()/i.s3.glbimg.com/v1/AUTH_59edd422c0c84a879bd37670ae4f538a/internal_photos/bs/2018/1/p/JbO1BoTCu5FmmTAWCQvA/cratera-joao-pessoa-bayeux.jpg' },
  //{ user: '@carlos', description: 'Semáforo quebrado na avenida principal', img: 'https://s2-g1.glbimg.com/BXoCVbSSUMqwk8SrldbMK3pYYbg=/0x0:1280x960/984x0/smart/filters:strip_icc()/i.s3.glbimg.com/v1/AUTH_59edd422c0c84a879bd37670ae4f538a/internal_photos/bs/2018/1/p/JbO1BoTCu5FmmTAWCQvA/cratera-joao-pessoa-bayeux.jpg' },
  { user: '@roberto', description: 'Enxente na rua do mercado', img: 'https://s2-g1.glbimg.com/BXoCVbSSUMqwk8SrldbMK3pYYbg=/0x0:1280x960/984x0/smart/filters:strip_icc()/i.s3.glbimg.com/v1/AUTH_59edd422c0c84a879bd37670ae4f538a/internal_photos/bs/2018/1/p/JbO1BoTCu5FmmTAWCQvA/cratera-joao-pessoa-bayeux.jpg' },
  { user: '@juliana', description: 'Buraco na esquina da escola', img: 'https://s2-g1.glbimg.com/BXoCVbSSUMqwk8SrldbMK3pYYbg=/0x0:1280x960/984x0/smart/filters:strip_icc()/i.s3.glbimg.com/v1/AUTH_59edd422c0c84a879bd37670ae4f538a/internal_photos/bs/2018/1/p/JbO1BoTCu5FmmTAWCQvA/cratera-joao-pessoa-bayeux.jpg' },
])

const expandedIndex = ref<number | null>(null)

function toggleExpand(index: number) {
  expandedIndex.value = expandedIndex.value === index ? null : index
}

useHead({
  meta: [{ name: "viewport", content: "width=device-width, initial-scale=1.0" }],
});

definePageMeta({
  layout: 'admin',
})
</script>

<template>
  <div class="grid grid-cols-1 gap-6 p-4 sm:p-6 md:p-8 md:grid-cols-1 xl:grid-cols-1">
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
          <template v-for="(problem, index) in problems" :key="index">
            <tr
              class="border-t border-gray-100 dark:border-gray-800"
            >
              <td class="whitespace-nowrap py-3">
                <div class="flex items-center gap-3">
                  <div>
                    <p class="text-sm font-medium text-gray-800 dark:text-white/90">
                      {{ problem.name }}
                    </p>
                    <span class="text-xs text-gray-500 dark:text-gray-400">
                      {{ problem.variants }} {{ t('lastProblems.reports') }}
                    </span>
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
                  @click.prevent="toggleExpand(index)"
                >
                  {{ expandedIndex === index ? 'Fechar' : 'Expandir' }}
                </a>
              </td>
            </tr>
            <tr v-if="expandedIndex === index" class="bg-gray-50 dark:bg-gray-900/30">
              <td colspan="4" class="py-4 px-6">
                <div>
                  <template v-for="(report) in reports" :key="reportIndex">
                    <div class="mb-4"> <p><strong class="font-semibold text-gray-800 dark:text-white/90">Usuário:</strong> {{ report.user }}</p>
                      <p><strong class="font-semibold text-gray-800 dark:text-white/90">Descrição:</strong> {{ report.description }}</p>
                    </div>
                  </template>
                  <p class="mt-6 mb-2 font-semibold text-gray-800 dark:text-white/90"><strong>Banco de Imagens:</strong></p>
                  <div class="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 lg:grid-cols-5 gap-4">
                    <div
                      v-for="(report, imgIndex) in reports"
                      :key="imgIndex"
                      class="relative group"
                    >
                      <img
                        :src="report.img"
                        alt="Imagem do relatório"
                        class="h-[200px] w-[200px] rounded-lg object-cover"
                      />
                      <div
                        class="absolute bottom-0 left-0 right-0 p-2 text-center text-white bg-black bg-opacity-70 opacity-0 group-hover:opacity-100 transition-opacity duration-300 rounded-b-lg"
                      >
                        {{ report.user }}
                      </div>
                    </div>
                  </div>
                </div>
              </td>
            </tr>
          </template>
        </tbody>
      </table>
    </div>
  </div>
</template>