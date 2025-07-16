<script setup lang="ts">
import { ref } from 'vue';
import { useRoute } from 'vue-router';
import { useI18n } from 'vue-i18n';

const { t } = useI18n();
const route = useRoute();
const problemId = Array.isArray(route.params.id) ? route.params.id[0] : (route.params.id || '1001');

const problem = ref({
  id: problemId,
  title: 'Buraco na rua',
  status: 'pending',
  address: 'Rua pedro vicente, 18912, Canindé',
  latitude: -23.525395,
  longitude: -46.621716,
  description: 'Buraco grande na esquina, perigoso para carros e pedestres.',
  date: '2025-07-10 09:30:00',
  photos: [
    'https://s2-g1.glbimg.com/BXoCVbSSUMqwk8SrldbMK3pYYbg=/0x0:1280x960/984x0/smart/filters:strip_icc()/i.s3.glbimg.com/v1/AUTH_59edd422c0c84a879bd37670ae4f538a/internal_photos/bs/2018/1/p/JbO1BoTCu5FmmTAWCQvA/cratera-joao-pessoa-bayeux.jpg',
    'https://images.unsplash.com/photo-1506744038136-46273834b3fb'
  ]
});

const reports = ref([
  {
    username: 'joaquimsouza',
    date: '2025-07-10 09:30:00',
    description: 'EDFDSFdsfsfsdfdsfsdfsdfdsfsfsfwerfewEDFDSFdsfsfsdfdsfsdfsdfdsfsfsfwerfewEDFDSFdsfsfsdfdsfsdfsdfdsfsfsfwerfew',
    photo: 'https://s2-g1.glbimg.com/BXoCVbSSUMqwk8SrldbMK3pYYbg=/0x0:1280x960/984x0/smart/filters:strip_icc()/i.s3.glbimg.com/v1/AUTH_59edd422c0c84a879bd37670ae4f538a/internal_photos/bs/2018/1/p/JbO1BoTCu5FmmTAWCQvA/cratera-joao-pessoa-bayeux.jpg'
  },
  {
    username: 'mariaoliveira',
    date: '2025-07-11 10:00:00',
    description: 'O buraco aumentou, está perigoso para crianças.',
    photo: 'https://images.unsplash.com/photo-1506744038136-46273834b3fb'
  }
]);
</script>

<template>
  <div class="grid grid-cols-1 gap-6 p-4 sm:p-6 md:p-8 xl:grid-cols-1">
    <div class="overflow-hidden rounded-2xl sm:px-6 flex flex-col items-center">
      <div class="w-full max-w-2xl mb-6">
        <Map
          class="rounded-xl border border-default mb-4"
          :zoom="16"
          :center="[problem.latitude, problem.longitude]"
          style="height: 250px; width: 100%;"
        >
          <Marker
            :lat-lng="{ latitude: problem.latitude, longitude: problem.longitude}"
          />
        </Map>
      </div>
      <h2 class="text-2xl font-bold mb-2">{{ t('details.title') }}</h2>
      <div class="w-full max-w-2xl">
        <div class="grid grid-cols-1 md:grid-cols-2 gap-4 mb-6">
          <div class="flex flex-col gap-4">
            <UForm :state="problem" class="flex flex-col gap-4">
              <UFormField :label="t('details.form.id')" name="id">
                <UInput v-model="problem.id" disabled />
              </UFormField>
              <UFormField :label="t('details.form.title')" name="title">
                <UInput v-model="problem.title" disabled />
              </UFormField>
              <UFormField :label="t('details.form.status')" name="status">
                <UBadge :color="problem.status === 'completed' ? 'success' : problem.status === 'in_progress' ? 'warning' : 'error'" variant="subtle">
                  {{ t('details.statusLabel.' + problem.status) }}
                </UBadge>
              </UFormField>
              <UFormField :label="t('details.form.date')" name="date">
                <UInput v-model="problem.date" disabled />
              </UFormField>
              <UFormField :label="t('details.form.address')" name="address">
                <UTextarea v-model="problem.address" :rows="2" autoresize disabled class="max-h-20 overflow-auto break-words" />
              </UFormField>
            </UForm>
          </div>
          <div class="flex flex-col gap-4">
            <UFormField :label="t('details.form.description')" name="description">
              <UTextarea v-model="problem.description" :rows="4" autoresize disabled class="max-h-40 overflow-auto break-words" />
            </UFormField>
          </div>
        </div>
        <div class="mb-6">
          <span class="font-semibold">{{ t('details.photos') }}</span>
          <div class="flex gap-4 mt-2 flex-wrap">
            <img v-for="(photo, idx) in problem.photos" :key="idx" :src="photo" :alt="t('details.reports.imageAlt')" class="rounded-lg w-[120px] h-[120px] object-cover aspect-square border" />
          </div>
        </div>
        <div class="rounded-xl border border-gray-200 bg-white px-4 pb-3 pt-4 dark:border-gray-800 dark:bg-white/[0.03] p-6 mt-8">
          <h3 class="text-xl font-semibold mb-6">{{ t('details.reports.title') }}</h3>
          <div class="flex flex-col gap-8">
            <div v-for="(report, idx) in reports" :key="idx" class="flex flex-col gap-4">
              <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
                <div class="flex flex-col gap-2">
                  <div><span class="font-semibold">{{ t('details.reports.user') }}</span> {{ report.username }}</div>
                  <div><span class="font-semibold">{{ t('details.reports.date') }}</span> {{ report.date }}</div>
                </div>
                <div class="flex flex-col gap-2 md:col-span-2">
                  <div><span class="font-semibold">{{ t('details.reports.description') }}</span></div>
                  <div class="bg-white dark:bg-neutral-900 rounded-lg border border-gray-200 dark:border-gray-700 p-3 max-h-40 overflow-auto break-words">{{ report.description }}</div>
                  <div class="w-[120px] h-[120px] mt-2">
                    <img :src="report.photo" :alt="t('details.reports.imageAlt')" class="rounded-lg w-full h-full object-cover aspect-square" />
                  </div>
                </div>
              </div>
              <hr v-if="idx < reports.length - 1" class="border-t border-gray-300 dark:border-gray-700 my-4" />
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>