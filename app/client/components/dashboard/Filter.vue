<script setup lang="ts">
import { ref, computed } from "vue";
import { useI18n } from "vue-i18n";

const emit = defineEmits<{
  (e: 'filter', range: [Date, Date] | null): void
}>();

const { t } = useI18n();

const items = ref([
  { label: t("filter.today") },
  { label: t("filter.week") },
  { label: t("filter.month") },
  { label: t("filter.year") },
  // { label: "Selecione uma data", icon: "i-lucide-calendar"},
]);

const selectedItem = ref(items.value[0]);
const showCalendar = computed(() => selectedItem.value.label === "Selecione uma data");

const calendarRange = ref<[Date, Date] | null>(null);

function closeCalendar() {
  selectedItem.value = items.value[0];
  calendarRange.value = null;
}

function filterCalendar() {
  emit('filter', calendarRange.value);
  selectedItem.value = items.value[0];
  calendarRange.value = null;
}
</script>

<template>
  <div class="relative w-48 h-15">
    <USelectMenu
      v-model="selectedItem"
      :search-input="false"
      :items="items"
      class="w-48"
    />
    <!-- <div v-if="showCalendar" class="absolute left-0 right-0 z-10 mt-2 bg-black rounded-lg shadow-lg p-4">
      <UCalendar
        range
        class="w-full"
      />
      <div class="flex justify-end mb-2 gap-2">
        <UButton
          class="mt-4 w-full bg-primary text-white rounded-lg py-2 hover:bg-primary-dark transition"
          @click="filterCalendar"
        >
          Filtrar
        </UButton>
        <UButton
          icon="i-lucide-x"
          @click="closeCalendar"
          class="mt-4 w-12 h-10 bg-red-500 text-white rounded-lg py-2 hover:bg-red-700 transition flex items-center justify-center"
        />
      </div>
    </div> -->
  </div>
</template>