<script lang="ts" setup>
import type {
  AffiliationRequest,
  AffiliationRequestStatus,
} from "~/types/domain";
import type { Badge } from "~/types/ui";

const { t } = useI18n();

const affiliation = defineProps<AffiliationRequest>();

const formattedRequestedAt = useDateFormat(affiliation.createdAt, "DD/MM/YYYY");

const statusBadge = computed<Badge>(() => {
  const status = affiliation.status;

  const badgeMap: Record<AffiliationRequestStatus, Badge> = {
    PENDING: { color: "warning", text: status },
    REJECTED: { color: "error", text: status },
    APPROVED: { color: "primary", text: status },
  };

  return badgeMap[status];
});
</script>

<template>
  <UCard>
    <div class="flex items-center gap-3">
      <UAvatar
        :src="affiliation.municipality.iconUrl"
        :alt="affiliation.municipality.name"
        size="3xl"
      />
      <div class="flex flex-col gap-0 lg:gap-1">
        <h3 class="font-semibold text-lg">
          {{ affiliation.municipality.name }}
        </h3>
        <div class="text-sm flex flex-col">
          <span>
            {{ t("municipalities.status") }}:
            <UBadge :color="statusBadge.color" variant="subtle">
              {{ t(`municipalities.${statusBadge.text.toLowerCase()}`) }}
            </UBadge>
          </span>
          <span>
            {{
              t("municipalities.requestedAt", { date: formattedRequestedAt })
            }}
          </span>
        </div>
      </div>
    </div>
  </UCard>
</template>
