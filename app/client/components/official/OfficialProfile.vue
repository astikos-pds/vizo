<script lang="ts" setup>
import type { DropdownMenuItem } from "@nuxt/ui";
import { municipalityRepository } from "~/repositories/municipality-repository";
import type { Official } from "~/types/domain";
import type { Pageable } from "~/types/http";

const official = defineProps<Official>();

const profileLink = `/profile/${official.id}`;

const registeredAt = Date.parse(official.createdAt);
const formattedRegisteredAt = useDateFormat(registeredAt, "DD/MM/YYYY");

const route = useRoute();
const municipalityId = route.params.municipalityId as string;

const pageable = reactive<Pageable>({
  page: 0,
  size: 100,
});

const { data: page } = municipalityRepository.getAllDepartments(
  municipalityId,
  pageable,
  {
    key: `municipality-${municipalityId}-departments`,
  }
);
const departments = computed(() => page.value?.content);

function assign(departmentId: string) {
  console.log(departmentId);
}

const items = ref<DropdownMenuItem[]>([
  {
    label: "Assign to department",
    icon: "i-lucide-forward",
    children: departments.value?.map((d) => {
      return {
        label: d.name,
        avatar: {
          src: d.iconUrl,
          alt: d.name,
        },
        onSelect: (_) => assign(d.id),
      };
    }),
  },
]);

const { isAdmin } = useUserStore();
</script>

<template>
  <UButton
    :to="profileLink"
    color="neutral"
    variant="link"
    :avatar="{
      src: avatar?.url,
      alt: name,
      size: 'xl',
    }"
    size="xl"
  >
    <div class="w-full flex justify-between item-center">
      <div class="flex flex-col">
        <span>{{ name }}</span>
        <span class="text-sm text-muted font-normal">{{ email }}</span>
      </div>

      <div class="my-auto">
        <UBadge :color="role === 'ADMIN' ? 'info' : 'neutral'" variant="subtle">
          {{ role.toLowerCase() }}
        </UBadge>
      </div>

      <div class="my-auto text-sm font-normal">
        Since {{ formattedRegisteredAt }}
      </div>

      <div v-if="role !== 'ADMIN' && isAdmin" class="my-auto">
        <UDropdownMenu :items="items">
          <UButton
            icon="i-lucide-ellipsis-vertical"
            color="neutral"
            variant="ghost"
          />
        </UDropdownMenu>
      </div>
    </div>
  </UButton>
</template>
