<script lang="ts" setup></script>

<template>
  <div class="h-screen flex flex-col">
    <ConfigHeader class="w-full" />
    <div class="w-full flex justify-center">
      <slot />
    </div>
  </div>
</template>
