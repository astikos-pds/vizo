<script lang="ts" setup>
import type { Department, Official } from "~/types/domain";

defineProps<Department>();

const emit = defineEmits<{ close: [] }>();

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
  avatar: { src: string; alt: string };
}

const items = computed<Item[]>(() =>
  officials.map<Item>((o) => {
    return {
      label: o.name,
      suffix: o.email,
      avatar: {
        src: "https://github.com/benjamincanac.png",
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

const value = ref([]);

function save() {}
</script>

<template>
  <UModal
    :title="`Assign officials to ${name}`"
    description="Add users to this department."
    :ui="{
      body: 'p-0 sm:p-0',
    }"
  >
    <template #body>
      <UCommandPalette
        multiple
        v-model="value"
        :groups="groups"
        class="flex-1"
      />
    </template>

    <template #footer>
      <div class="size-full flex gap-2 justify-end">
        <UButton color="neutral" variant="solid" @click="emit('close')"
          >Cancel</UButton
        >
        <UButton color="success" variant="subtle" @click="save">Save</UButton>
      </div>
    </template>
  </UModal>
</template>
