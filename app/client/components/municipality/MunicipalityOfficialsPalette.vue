<script lang="ts" setup generic="T">
import { municipalityRepository } from "~/repositories/municipality-repository";
import type { Pageable } from "~/types/http";

const props = defineProps<{
  modelValue: T[];
}>();
const emit = defineEmits<{
  (e: "update:modelValue", values: T[]): void;
}>();

const values = ref(props.modelValue);

watch(values, (newVal) => {
  emit("update:modelValue", newVal as T[]);
});

const route = useRoute();
const municipalityId = route.params.municipalityId as string;

const pageable = reactive<Pageable>({
  page: 0,
  size: 100,
});

const { data: page, pending } = municipalityRepository.getAllAffiliations(
  municipalityId,
  pageable,
  {
    key: `municipality-${municipalityId}-affiliations`,
  }
);

const affiliations = computed(() => page.value?.content);

const officials = computed(() =>
  (affiliations.value ?? [])
    .map((a) => a.official)
    .filter((o) => o.role !== "ADMIN")
);

interface Item {
  label: string;
  suffix: string;
  avatar: { src?: string; alt: string };
}

const items = computed<Item[]>(() =>
  officials.value.map<Item>((o) => {
    return {
      label: o.name,
      suffix: o.email,
      avatar: {
        src: o.avatar?.url,
        alt: o.name,
      },
    };
  })
);

const groups = ref([
  {
    id: "users",
    label: "Users",
    items: items,
  },
]);
</script>

<template>
  <UCommandPalette
    multiple
    :loading="pending"
    v-model="values"
    :groups="groups"
    placeholder="Search for a user..."
    class="flex-1"
  />
</template>
