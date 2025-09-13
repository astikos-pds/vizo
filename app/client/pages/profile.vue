<script lang="ts" setup>
import { ref, reactive } from "vue";
import { useI18n } from "vue-i18n";
import type { TabsItem } from "@nuxt/ui";
import type { Map as LeafletMap, PointExpression } from "leaflet";
import { useProblems } from "~/composables/use-problems";
import { useMapGeolocation } from "~/composables/use-map-geolocation";
import ProblemDetails from "~/components/problem/ProblemDetails.vue";
import type { Problem } from "~/types/domain/problem";
import type { LatLng } from "~/types/geolocation";


const { t } = useI18n();

useHead({
  meta: [
    { name: "viewport", content: "width=device-width, initial-scale=1.0" },
  ],
});

/* definePageMeta({
  layout: "official",
}); */

const { map, zoom, center } = useMap();

const items = [
  {
    label: t("profile.tab.account.label"),
    description: t("profile.tab.account.description"),
    icon: "i-lucide-user",
    slot: "account" as const,
  },
  {
    label: t("profile.tab.admin.label"),
    description: t("profile.tab.admin.description"),
    icon: "i-lucide-lock",
    slot: "password" as const,
  },
] satisfies TabsItem[];

const user = reactive({
  name: "Joaquim Souza",
  username: "joaquimsouza",
  avatar: "/avatar/avatar1.png",
  email: "example@email.com",
  phone: "+55 11 91234-5678",
  address: "Rua Exemplo, 123",
  city: "São Paulo"
});

const reports = [
  {
    id: "1",
    problem: "Buraco na rua",
    description: "Encontrei esse buraco profundo na rua.",
    latitude: "-23.6670",
    longitude: "-46.4610",
    images: "report.png",
    credibility: "10",
    createdAt: "07/07/2025"
  },
  {
    id: "2",
    problem: "Semáforo quebrado",
    description: "Encontrei esse semáforo quebrado na rua quase bati o carro kjnasdjnksaoldlksadf.",
    latitude: "12.4587",
    longitude: "-73.9214",
    images: "report.png",
    credibility: "10",
    createdAt: "07/07/2025"
  }
]

const badges = ref([
  { icon: "i-lucide-headset", label: t("profile.badges.solver") },
  { icon: "i-lucide-heart-handshake", label: t("profile.badges.helper") },
]);
const avatarOptions = Array.from({ length: 12 }, (_, i) => `/avatar/avatar${i + 1}.png`)
</script>

<template>
  <div class="grid grid-cols-1 gap-6 p-4 sm:p-6 md:p-8 xl:grid-cols-1 m-10 ml-50 mr-50">
    <div class="overflow-hidden rounded-2xl sm:px-6 flex flex-col items-left border border-gray-200 px-5 pt-5 dark:border-gray-800">
      <div class="flex items-center gap-6 mb-4">
        <img
          :src="user.avatar || `https://avatar.iran.liara.run/username?username=${user.name}`"
          height="120"
          width="120"
          class="rounded-full"
        />
        <div class="flex flex-col">
          <span class="text-lg font-semibold">@{{ user.username }}</span>
          <!-- Modal de editar -->
          <UModal title="Escolha um Avatar">
            <UButton
              icon="i-lucide-pencil"
              size="sm"
              class="mt-2 w-fit"
              color="primary"
              variant="soft"
            >
              Editar
            </UButton>
            <template #body>
              <div class="flex flex-wrap gap-4 justify-center items-center py-4">
                <div
                  v-for="(avatar, idx) in avatarOptions"
                  :key="avatar"
                  class="cursor-pointer border-2 rounded-full p-1 transition-all duration-200"
                  :class="user.avatar === avatar ? 'border-blue-500' : 'border-transparent'"
                  @click="user.avatar = avatar"
                >
                  <img :src="avatar" width="80" height="80" class="rounded-full" />
                </div>
              </div>
            </template>
          </UModal>
        </div>
      </div>
      
    </div>

    <!-- Reportes -->
      <div class="mt-8">
        <div v-if="reports.length === 0" class="text-gray-500">Nenhum relatório encontrado.</div>
        <div v-else class="space-y-6">
          <div
            v-for="report in reports"
            :key="report.id"
            class="overflow-hidden rounded-2xl sm:px-6 border border-gray-200 px-5 pt-5 dark:border-gray-800 grid grid-cols-2 gap-4"
          >
            <div class="flex flex-col gap-2">
              <div class="font-semibold">
                Problema: <span class="font-normal">{{ report.problem }}</span>
              </div>
              <div class="font-semibold">
                Descrição: <span class="font-normal">{{ report.description }}</span>
              </div>
              <div class="font-semibold flex items-center gap-2">
                Imagem:
                <img
                  v-if="report.images"
                  :src="`/images/${report.images}`"
                  alt="Imagem do relatório"
                  class="w-20 h-20 object-cover rounded"
                />
                <span v-else class="text-gray-400">Sem imagem</span>
              </div>
              <div class="font-semibold">Credibilidade: {{ report.credibility }}</div>
              <div class="text-xs text-gray-400">Criado em: {{ report.createdAt }}</div>
            </div>
            
            <div class="flex items-center justify-center m-10">
              <Map
                class="rounded-md border border-default"
                :zoom="16"
                :center="{
                  latitude: Number(report.latitude),
                  longitude: Number(report.longitude)
                }"
                style="height: 250px; width: 100%"
              >
                <Marker
                  :lat-lng="{
                    latitude: Number(report.latitude),
                    longitude: Number(report.longitude)
                  }"
                />
              </Map>
            </div>
          </div>
        </div>
      </div>
  </div>
</template>
