<script lang="ts" setup>
import type { PageDTO, Pagination } from "~/types/domain/pagination";
import type { Report } from "~/types/domain/report";
import type { LatLng } from "~/types/geolocation";

const { t } = useI18n();

useHead({
  title: t("head.reportHistory.title"),
  meta: [{ name: "description", content: t("head.reportHistory.description") }],
});

definePageMeta({
  name: "History of reports",
  middleware: ["auth"],
});

const pagination = reactive<Pagination>({
  page: 0,
  size: 15,
});

const { getMyReports } = useMe();

const { data: page, pending } = await getMyReports(pagination);

const {
  items: reports,
  currentPage,
  totalElements,
} = usePagination(pagination, page);

const { map, center, zoom } = useMap();

const zoomToMarker = (marker: LatLng) => {
  map.value?.flyTo([marker.latitude, marker.longitude], 16, {
    animate: false,
    duration: 1,
  });
};
</script>

<template>
  <ReportsPage class="flex">
    <template #aside>
      <EmptyMessage v-if="pending">{{
        t("pages.reports.loading")
      }}</EmptyMessage>
      <EmptyMessage v-else-if="!page">
        {{ t("pages.reports.failedToFetch") }}
      </EmptyMessage>
      <EmptyMessage v-else-if="reports.length === 0">
        <span>
          {{ t("pages.reports.noReports") }}
          <NuxtLink to="/reports/new" class="text-primary">
            {{ t("pages.reports.reportNew") }}
          </NuxtLink>
        </span>
      </EmptyMessage>
      <section
        v-else
        class="size-full flex flex-col justify-between overflow-y-auto"
      >
        <main class="flex-1 size-full p-3 2xl:p-5">
          <ReportsCard
            v-for="report in reports"
            :key="report.id"
            v-bind="report"
            @zoom-in="zoomToMarker(report)"
          />
        </main>
        <footer
          v-if="page && page.totalPages > 1"
          class="w-full border-t border-default flex justify-center items-center p-3"
        >
          <UPagination
            v-model:page="currentPage"
            :items-per-page="pagination.size"
            :total="totalElements"
          />
        </footer>
      </section>
    </template>

    <Map ref="map" :center="center" :zoom="zoom">
      <Marker
        v-for="report in reports"
        :key="report.id"
        v-bind:lat-lng="report"
        @click="zoomToMarker(report)"
      />

      <CurrentPositionMarker />
    </Map>
  </ReportsPage>
</template>
