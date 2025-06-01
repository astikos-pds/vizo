<script lang="ts" setup>
const model = defineModel<string>();
interface Props {
  label: string;
  name: string;
  placeholder: string;
  required: boolean;
  color:
    | "error"
    | "primary"
    | "neutral"
    | "secondary"
    | "success"
    | "info"
    | "warning"
    | undefined;
  show: boolean;
}
const { color, show, label, name, placeholder, required } =
  defineProps<Props>();
const emit = defineEmits<{ (e: "click"): void }>();
</script>

<template>
  <UFormField
    :label="label"
    :name="name"
    size="xl"
    class="w-full"
    :required="required"
    :error="false"
  >
    <UInput
      v-model="model"
      size="xl"
      :color="color"
      :type="show ? 'text' : 'password'"
      :placeholder="placeholder"
      class="w-full"
    >
      <template #trailing>
        <UButton
          color="neutral"
          class="cursor-pointer text-xl"
          variant="link"
          size="xl"
          :icon="show ? 'i-lucide-eye-closed' : 'i-lucide-eye'"
          :aria-label="show ? 'Hide password' : 'Show password'"
          @click="emit('click')"
        />
      </template>
    </UInput>

    <slot></slot>
  </UFormField>
</template>
