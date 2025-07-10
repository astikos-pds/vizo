<script setup lang="ts">
import { ref, onMounted, onBeforeUnmount, computed } from 'vue';

const props = defineProps<{
  options: Array<{ label: string; value: string }>,
  modelValue: string
}>();
const emit = defineEmits(['update:modelValue']);

const showDropdown = ref(false);

function handleClickOutside(event: MouseEvent) {
  const dropdown = document.getElementById('solved-dropdown');
  const button = document.getElementById('solved-dropdown-btn');
  if (
    dropdown &&
    !dropdown.contains(event.target as Node) &&
    button &&
    !button.contains(event.target as Node)
  ) {
    showDropdown.value = false;
  }
}

onMounted(() => {
  document.addEventListener('mousedown', handleClickOutside);
});
onBeforeUnmount(() => {
  document.removeEventListener('mousedown', handleClickOutside);
});

const selectedLabel = computed(() => {
  return props.options.find(o => o.value === props.modelValue)?.label || '';
});

function selectOption(value: string) {
  emit('update:modelValue', value);
  showDropdown.value = false;
}
</script>

<template>
  <div class="relative flex justify-end">
    <button
      id="solved-dropdown-btn"
      class="px-4 py-2 font-medium rounded-md bg-gray-100 text-gray-700 dark:bg-white/[0.03] dark:text-white hover:bg-gray-200 dark:hover:bg-gray-800 transition flex items-center border border-gray-200 dark:border-gray-700"
      @click="showDropdown = !showDropdown"
      type="button"
    >
      {{ selectedLabel }}
      <svg class="inline ml-2 w-4 h-4" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24">
        <path stroke-linecap="round" stroke-linejoin="round" d="M19 9l-7 7-7-7"/>
      </svg>
    </button>
    <div
      v-if="showDropdown"
      id="solved-dropdown"
      class="absolute right-0 mt-2 z-20 w-44 bg-white dark:bg-gray-900 rounded-xl shadow-lg border border-gray-200 dark:border-gray-700"
    >
      <ul class="py-2">
        <li v-for="option in options" :key="option.value">
          <button
            @click="selectOption(option.value)"
            :class="[
              modelValue === option.value
                ? 'bg-gray-100 dark:bg-gray-800 text-gray-900 dark:text-white'
                : 'bg-transparent text-gray-700 dark:text-gray-300',
              'w-full text-left px-4 py-2 rounded-md hover:bg-gray-100 dark:hover:bg-gray-800 transition'
            ]"
          >
            {{ option.label }}
          </button>
        </li>
      </ul>
    </div>
  </div>
</template>