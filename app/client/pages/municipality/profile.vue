<script lang="ts" setup>
import { ref } from "vue";
import { useI18n } from "vue-i18n";

const { t } = useI18n();

useHead({
  meta: [{ name: "viewport", content: "width=device-width, initial-scale=1.0" }],
});

definePageMeta({
  layout: 'admin',
});

const user = ref({
  name: "Natalia Alves",
  role: "Coordenadora",
  email: "natalia.alves@example.com",
  cellphone: "(11) 91234-5678",
  permission: "Funcionário",
  city: "São Paulo",
});

const permissions = ["Administrador", "Funcionário"];

const sectors = ref([
  { name: "Saúde", permission: "Leitura", active: true },
  { name: "Educação", permission: "Administração", active: false },
  { name: "Transporte", permission: "Leitura", active: true },
]);
</script>

<template>
  <div class="p-4 sm:p-6 md:p-8">
    <div class="max-w-2xl mx-auto bg-white rounded-2xl border border-gray-200 dark:border-gray-800 dark:bg-white/[0.03] p-6">
        <div class="grid grid-cols-1 gap-6 p-4 sm:p-6 md:p-8 md:grid-cols-1 xl:grid-cols-1">
      <h2 class="text-xl font-bold mb-2">Perfil do Usuário</h2>
      <div class="mb-1">
        <span class="font-semibold">Nome:</span> {{ user.name }}
      </div>
      <div class="mb-4">
        <span class="font-semibold">Cargo:</span> {{ user.role }}
      </div>
      <hr class="my-4 border-gray-300" />
      <h3 class="text-lg font-semibold mb-2">Informações gerais</h3>
      <div class="grid grid-cols-2 gap-4 mb-2">
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Email</label>
          <input type="text" :value="user.email" disabled class="w-full border rounded px-2 py-1 bg-gray-100" />
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Telefone</label>
          <input type="text" :value="user.cellphone" disabled class="w-full border rounded px-2 py-1 bg-gray-100" />
        </div>
      </div>
      <div class="grid grid-cols-2 gap-4 mb-2">
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Permissão</label>
          <select v-model="user.permission" class="w-full border rounded px-2 py-1">
            <option v-for="perm in permissions" :key="perm" :value="perm">{{ perm }}</option>
          </select>
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">Cidade</label>
          <input type="text" :value="user.city" disabled class="w-full border rounded px-2 py-1 bg-gray-100" />
        </div>
      </div>
      <hr class="my-4 border-gray-300" />
      <h3 class="text-lg font-semibold mb-2">Setores</h3>
      <table class="w-full text-sm border-collapse">
        <thead>
          <tr class="bg-gray-100">
            <th class="py-2 px-2 text-left">Setor</th>
            <th class="py-2 px-2 text-left">Permissão</th>
            <th class="py-2 px-2 text-left">Ativo</th>
            <th class="py-2 px-2 text-left">Detalhes</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="sector in sectors" :key="sector.name" class="border-t">
            <td class="py-2 px-2">{{ sector.name }}</td>
            <td class="py-2 px-2">{{ sector.permission }}</td>
            <td class="py-2 px-2">
              <span :class="sector.active ? 'text-green-600' : 'text-red-600'">
                {{ sector.active ? 'Sim' : 'Não' }}
              </span>
            </td>
            <td class="py-2 px-2">
              <button class="text-blue-600 hover:underline">Ver detalhes</button>
            </td>
          </tr>
        </tbody>
      </table>
      </div>
    </div>
  </div>
</template>