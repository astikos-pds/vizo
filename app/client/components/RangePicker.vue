<script setup lang="ts">
import {
  CalendarDate,
  DateFormatter,
  getLocalTimeZone,
} from "@internationalized/date";

const { t } = useI18n();

const model = defineModel<{
  start: Date;
  end: Date;
}>({ required: true });

const { locale } = useI18n();

const df = new DateFormatter(locale.value, {
  dateStyle: "medium",
});

const modelValue = computed({
  get: () => ({
    start: new CalendarDate(
      model.value.start.getFullYear(),
      model.value.start.getMonth() + 1,
      model.value.start.getDate()
    ),
    end: new CalendarDate(
      model.value.end.getFullYear(),
      model.value.end.getMonth() + 1,
      model.value.end.getDate()
    ),
  }),
  set: (val: { start: CalendarDate; end: CalendarDate }) => {
    model.value.start = val.start.toDate(getLocalTimeZone());
    model.value.end = val.end.toDate(getLocalTimeZone());
  },
});
</script>

<template>
  <UPopover>
    <UButton color="neutral" variant="outline" icon="i-lucide-calendar">
      <template v-if="model.start">
        <template v-if="model.end">
          <span class="font-normal">
            {{ df.format(model.start) }} - {{ df.format(model.end) }}</span
          >
        </template>
        <template v-else>
          {{ df.format(model.start) }}
        </template>
      </template>
      <template v-else>{{ t('components.rangePicker.pickDate') }}</template>
    </UButton>

    <template #content>
      <UCalendar v-model="modelValue" class="p-2" :number-of-months="1" range />
    </template>
  </UPopover>
</template>
