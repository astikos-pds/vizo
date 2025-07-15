<script lang="ts" setup>
import type { Department, Official } from "~/types/domain";

defineProps<Department>();

const emit = defineEmits<{ close: [] }>();

const selectedOfficials = ref<Official[]>([]);

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
      <MunicipalityOfficialsPalette v-model="selectedOfficials" />
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
