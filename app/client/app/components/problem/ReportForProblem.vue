<script lang="ts" setup>
import type { Report } from "~/types/domain/report";

const { t, locale } = useI18n();

interface Props {
  report: Report;
}
const { report } = defineProps<Props>();
</script>

<template>
  <UCard
    variant="outline"
    :ui="{
      header: 'p-2 sm:px-2 2xl:p-2',
      body: 'p-0 sm:p-0',
      footer: 'p-2 sm:px-2 2xl:p-2 flex items-end justify-end',
    }"
  >
    <template #header>
      <div class="w-full flex gap-3 items-center">
        <aside class="h-full flex justify-center items-center">
          <UAvatar
            :src="report.user.avatarUrl?.toString()"
            :alt="report.user.name"
            size="2xl"
          />
        </aside>
        <main class="flex flex-col">
          <span class="font-medium">{{ report.user.name }}</span>
          <span class="text-sm">{{ report.user.email.split("@")[0] }}</span>
        </main>
      </div>
    </template>

    <div class="size-full flez flex-col gap-2">
      <div class="w-full p-3">
        <span>{{ report.description }}</span>
      </div>

      <section class="flex flex-col gap-3">
        <NuxtImg
          v-for="(imageUrl, index) in report.imagesUrls"
          :key="`${report.id}-${index}`"
          :src="imageUrl.toString()"
          class="w-full h-full object-cover"
          :alt="`Image ${index} of report with id ${report.id}`"
        />
      </section>
    </div>

    <template #footer>
      <span class="text-sm">
        {{ t("components.problem.reportedAt") }}
        {{ report.createdAt.toLocaleDateString(locale, { dateStyle: "full" }) }}
      </span>
    </template>
  </UCard>
</template>
