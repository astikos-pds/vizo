<script setup lang="ts">
import type { NuxtError } from "#app";

const { t } = useI18n();

const props = defineProps<{
  error: NuxtError;
}>();

const title = computed(() =>
  t(`error.${props.error.statusCode}.title`, t("error.default.title"))
);

const description = computed(() =>
  t(`error.${props.error.statusCode}.message`, t("error.default.message"))
);

useHead({
  title: `Vizo | ${title.value}`,
  meta: [
    {
      name: "description",
      content: description.value,
    },
  ],
});

const localizedError = computed(() => {
  return {
    statusCode: props.error.statusCode,
    statusMessage: title.value,
    message: description.value,
  };
});
</script>

<template>
  <UError
    redirect="/"
    :clear="{
      icon: 'i-lucide-arrow-left',
      label: t('error.default.button'),
    }"
    :error="localizedError"
  />
</template>
