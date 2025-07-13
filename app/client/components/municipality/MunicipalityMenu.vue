<script lang="ts" setup>
defineProps<{
  title: string;
  items: any[];
  defaultOpen?: boolean;
}>();
</script>

<template>
  <UCollapsible class="w-full" :default-open="defaultOpen">
    <UButton
      color="neutral"
      variant="ghost"
      trailing-icon="i-lucide-chevron-down"
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
          <p class="text-center p-3">
            <slot name="empty" />
          </p>
        </div>

        <main
          v-else
          class="w-full grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-2 lg:gap-3"
        >
          <slot name="body" />
        </main>
      </div>
    </template>
  </UCollapsible>
</template>
