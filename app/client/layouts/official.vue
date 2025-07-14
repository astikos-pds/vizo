<script lang="ts" setup></script>

<template>
  <div class="h-screen flex flex-col">
    <ConfigHeader class="w-full" />
    <div class="w-full flex flex-col items-center justify-center">
      <slot />
    </div>
  </div>
</template>
