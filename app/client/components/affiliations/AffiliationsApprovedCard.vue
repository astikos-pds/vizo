<script lang="ts" setup>
import type { DropdownMenuItem } from "@nuxt/ui";
import type { AffiliatedUser } from "~/types/domain/affiliated-user";

const { locale } = useI18n();

const affiliatedUser = defineProps<AffiliatedUser>();

const actions = ref<DropdownMenuItem[]>([
  {
    label: "Disaffiliate",
    icon: "i-lucide-split",
    color: "error",
    onSelect: () => console.log(affiliatedUser.id),
  },
]);

function exitMunicipality(id: string) {}
</script>

<template>
  <NuxtLink :to="`/municipalities/${affiliatedUser.municipality.id}`">
    <UCard
      :ui="{
        footer: 'p-2 sm:p-2',
      }"
    >
      <div class="flex items-center gap-3">
        <UAvatar
          :src="affiliatedUser.municipality.iconUrl?.toString()"
          :alt="affiliatedUser.municipality.name"
          size="3xl"
        />

        <div class="flex-1 flex flex-col">
          <h3 class="font-semibold text-md">
            {{ affiliatedUser.municipality.name }}
          </h3>
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

        <UDropdownMenu
          :items="actions"
          :content="{
            side: 'right',
          }"
        >
          <UButton
            variant="ghost"
            color="neutral"
            icon="i-lucide-ellipsis"
            size="xl"
          />
        </UDropdownMenu>
      </div>

      <template #footer v-if="!affiliatedUser.isAdmin">
        <div class="w-full flex justify-between items-center">
          <div class="text-xs flex flex-col">
            <span v-if="affiliatedUser.approvedAt"
              >Since
              {{
                affiliatedUser.approvedAt.toLocaleDateString(locale, {
                  dateStyle: "full",
                })
              }}</span
            >
            <span v-if="affiliatedUser.approver"
              >Approved by {{ affiliatedUser.approver.user.name }}</span
            >
          </div>
        </div>
      </template>
    </UCard>
  </NuxtLink>
</template>
