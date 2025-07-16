<script lang="ts" setup>
import type { AccordionItem } from "@nuxt/ui";
import { userRepository } from "~/repositories/user-repository";

const { t } = useI18n();

useHead({
  title: t("head.municipalities.title"),
  meta: [
    {
      name: "description",
      content: t("head.municipalities.description"),
    },
  ],
});

definePageMeta({
  layout: "official",
  middleware: ["auth", "official"],
});

const { data: affiliations, pending } =
  await userRepository.getAllUserAffiliations({
    key: "user-affiliations",
  });

const approvedAffiliations = computed(() =>
  (affiliations.value ?? []).filter((a) => a.status === "APPROVED")
);

const requestedAffiliations = computed(() =>
  (affiliations.value ?? []).filter((a) => a.status === "PENDING")
);

const items = ref<AccordionItem[]>([
  {
    label: t("municipalities.myMunicipalities"),
    icon: "i-lucide-building",
    slot: "municipalities",
  },
  {
    label: t("municipalities.requests"),
    icon: "i-lucide-clock",
    slot: "pending",
  },
]);

const openSections = ref(["0", "1"]);
</script>

<template>
  <OfficialPage
    :title="t('municipalities.header')"
    :description="t('municipalities.subheader')"
  >
    <div v-if="pending">
      <USkeleton class="h-20 w-full mb-4" v-for="i in 2" :key="i" />
    </div>

    <UAccordion v-else type="multiple" v-model="openSections" :items="items">
      <template #municipalities>
        <MunicipalityMenu
          :items="approvedAffiliations"
          :empty-text="t('municipalities.noMunicipality')"
        >
          <MunicipalityCard
            v-for="item in approvedAffiliations"
            v-bind="item"
            :key="item.id"
          />
        </MunicipalityMenu>
      </template>

      <template #pending>
        <MunicipalityMenu
          :items="requestedAffiliations"
          :empty-text="t('municipalities.noRequest')"
        >
          <MunicipalityPendingAffiliation
            v-for="item in requestedAffiliations"
            v-bind="item"
            :key="item.id"
          />
        </MunicipalityMenu>
      </template>
    </UAccordion>
  </OfficialPage>
</template>
