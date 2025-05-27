<script lang="ts" setup>
interface Props {
  icon?: string;
  label?: string;
  selected?: boolean;
}
const props = defineProps<Props>();

const icon = ref(`tabler:${props.icon ?? "favicon"}`);
</script>

<template>
  <section
    class="p-2 flex flex-row items-center gap-2 pl-5 rounded-e-full transition cursor-pointer"
    :class="selected ? 'bg-blue-700 text-zinc-50' : 'hover:bg-zinc-100'"
  >
    <Icon :name="icon" mode="svg" size="1.9rem" />
    <span>{{ props.label ?? "Label" }}</span>
  </section>
</template>
