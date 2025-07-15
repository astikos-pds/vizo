<script lang="ts" setup generic="T">
import type { Official } from "~/types/domain";

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

const officials: Official[] = [
  {
    id: "",
    document: "",
    email: "mateus@gmail.com",
    name: "Mateus",
    avatar: null,
    role: "OFFICIAL",
    wasApproved: false,
    createdAt: "",
    updatedAt: "",
  },
  {
    id: "",
    document: "",
    email: "mateus@gmail.com",
    name: "Mateus",
    avatar: null,
    role: "OFFICIAL",
    wasApproved: false,
    createdAt: "",
    updatedAt: "",
  },
  {
    id: "",
    document: "",
    email: "mateus@gmail.com",
    name: "Mateus",
    avatar: null,
    role: "OFFICIAL",
    wasApproved: false,
    createdAt: "",
    updatedAt: "",
  },
];

interface Item {
  label: string;
  suffix: string;
  avatar: { src?: string; alt: string };
}

const items = computed<Item[]>(() =>
  officials.map<Item>((o) => {
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
  <UCommandPalette multiple v-model="values" :groups="groups" class="flex-1" />
</template>
