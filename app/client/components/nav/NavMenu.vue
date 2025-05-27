<script lang="ts" setup>
interface Props {
  collapsed?: boolean;
}
const props = defineProps<Props>();

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
    class="size-full flex flex-col justify-between text-[1.2rem] text-zinc-950 transition-all"
    :class="props.collapsed ? 'p-3' : 'pr-4'"
  >
    <header class="h-[15%] flex justify-center items-center">Vizo</header>
    <main class="h-full flex flex-col gap-2 py-3">
      <NavItem
        v-for="option in navOptions"
        :selected="selectedItem === option.id"
        :collapsed="props.collapsed"
        @click="() => (selectedItem = option.id)"
        :icon="option.icon"
        :label="option.label"
      />
    </main>
    <footer class="w-full h-[15%] flex items-center">
      <NavItem :collapsed="props.collapsed" icon="transfer-out" label="Sair" />
    </footer>
  </aside>
</template>
