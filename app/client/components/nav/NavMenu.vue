<script lang="ts" setup>
interface NavOption {
  id: string;
  icon: string;
  label: string;
}

const navOptions: NavOption[] = [
  { id: "home", icon: "apps", label: "Início" },
  { id: "report", icon: "message-report", label: "Reportar" },
  { id: "settings", icon: "settings", label: "Configurações" },
];
const selectedItem = ref<NavOption["id"]>("home");
</script>

<template>
  <aside
    class="flex flex-col w-[20%] justify-between pr-3 text-[1.2rem] text-zinc-950"
  >
    <header class="h-[15%] flex justify-center items-center">Vizo</header>
    <main class="h-full flex flex-col gap-2 py-3">
      <NavItem
        v-for="option in navOptions"
        :selected="selectedItem === option.id"
        @click="() => (selectedItem = option.id)"
        :icon="option.icon"
        :label="option.label"
      />
    </main>
    <footer class="h-[15%]">
      <NavItem icon="transfer-out" label="Sair" />
    </footer>
  </aside>
</template>
