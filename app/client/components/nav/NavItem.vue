<script lang="ts" setup>
interface Props {
  icon?: string;
  label?: string;
  selected?: boolean;
  collapsed?: boolean;
}
const props = defineProps<Props>();

const icon = ref(`tabler:${props.icon ?? "favicon"}`);
</script>

<template>
  <button
    class="w-full flex flex-row items-center gap-2 transition cursor-pointer"
    :class="[
      selected ? 'bg-blue-700 text-zinc-50' : 'hover:bg-zinc-100',
      props.collapsed ? 'rounded-md p-2' : 'rounded-e-full py-2 px-5',
    ]"
  >
    <Icon :name="icon" mode="svg" size="1.9rem" />
    <span v-if="!props.collapsed">{{ props.label ?? "Label" }}</span>
  </button>
</template>
