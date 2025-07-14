<script lang="ts" setup>
const { defaultOpen } = defineProps<{
  title: string;
  items: any[];
  defaultOpen?: boolean;
}>();

const open = ref<boolean>(defaultOpen ?? false);
</script>

<template>
  <UCollapsible class="w-full" v-model:open="open">
    <UButton
      color="neutral"
      variant="ghost"
      :trailing-icon="open ? 'i-lucide-chevron-up' : 'i-lucide-chevron-down'"
      block
      class="w-full lg:mb-1"
    >
      {{ title }}
    </UButton>

    <template #content>
      <div class="p-1">
        <div
          v-if="!items || items.length === 0"
          class="w-full h-24 lg:h-48 flex justify-center items-center border border-default rounded-lg"
        >
          <p class="text-sm text-center p-3">
            <slot name="empty" />
          </p>
        </div>

        <main
          v-else
          class="w-full grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-2 lg:gap-3"
        >
          <slot name="body" />
        </main>
      </div>
    </template>
  </UCollapsible>
</template>
