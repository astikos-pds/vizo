<template>
  <ClientOnly fallback-tag="span" fallback="Loading...">
    <UButton
      v-if="
        useNuxtApp().$pwa?.showInstallPrompt && !useNuxtApp().$pwa?.needRefresh
      "
      icon="i-lucide-download"
      color="neutral"
      variant="outline"
      size="xl"
      :disabled="useNuxtApp().$pwa?.isPWAInstalled"
      class="cursor-pointer"
      :ui="{
        leadingIcon: 'text-lg xl:text-xl',
      }"
      @click="useNuxtApp().$pwa?.install()"
    />
    <UButton
      v-else-if="useNuxtApp().$pwa?.needRefresh"
      icon="i-lucide-refresh-ccw"
      color="neutral"
      variant="outline"
      class="cursor-pointer"
      :ui="{
        leadingIcon: 'text-lg xl:text-xl',
      }"
      @click="useNuxtApp().$pwa?.updateServiceWorker()"
    />
    <UButton
      v-else
      :icon="
        useNuxtApp().$pwa?.isPWAInstalled
          ? 'i-lucide-monitor-check'
          : 'i-lucide-download'
      "
      color="neutral"
      variant="outline"
      size="xl"
      :disabled="useNuxtApp().$pwa?.isPWAInstalled"
      class="cursor-pointer"
      :ui="{
        leadingIcon: 'text-lg xl:text-xl',
      }"
    />
  </ClientOnly>
</template>
