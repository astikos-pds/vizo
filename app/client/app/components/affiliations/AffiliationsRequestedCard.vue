<script lang="ts" setup>
import type {
  AffiliatedUser,
  AffiliationStatus,
} from "~/types/domain/affiliated-user";
import type { Badge } from "~/types/ui";

const { t, locale } = useI18n();

const affiliatedUser = defineProps<AffiliatedUser>();

const statusBadge = computed<Badge>(() => {
  const status = affiliatedUser.status;

  const badgeMap: Record<AffiliationStatus, Badge> = {
    PENDING: { color: "warning", text: status },
    REJECTED: { color: "error", text: status },
    APPROVED: { color: "success", text: status },
  };

  return badgeMap[status];
});
</script>

<template>
  <UCard :variant="affiliatedUser.status === 'APPROVED' ? 'outline' : 'subtle'">
    <div class="flex items-center gap-3">
      <UAvatar
        :src="affiliatedUser.municipality.iconUrl?.toString()"
        :alt="affiliatedUser.municipality.name"
        size="3xl"
      />
      <div class="flex flex-col">
        <div class="flex gap-1.5">
          <h3 class="font-semibold text-lg">
            {{ affiliatedUser.municipality.name }}
          </h3>
          <span>
            <UBadge :color="statusBadge.color" variant="subtle" size="sm">
              {{ t(`affiliation.status.${statusBadge.text.toLowerCase()}`) }}
            </UBadge>
          </span>
        </div>
        <div class="text-xs 2xl:text-sm flex flex-col">
          <span>
            Requested at
            {{
              affiliatedUser.affiliatedAt.toLocaleDateString(locale, {
                dateStyle: "full",
              })
            }}
          </span>
        </div>
      </div>
    </div>
  </UCard>
</template>
