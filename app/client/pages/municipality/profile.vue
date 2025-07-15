<script lang="ts" setup>
import { ref } from "vue";
import { useI18n } from "vue-i18n";
import type { TabsItem } from '@nuxt/ui'

const { t } = useI18n();

useHead({
  meta: [{ name: "viewport", content: "width=device-width, initial-scale=1.0" }],
});

definePageMeta({
  layout: 'admin',
});

const items = [
  {
    label: 'Minha Conta',
    description: 'Faça alterações no seu perfil, como nome de usuário e email.',
    icon: 'i-lucide-user',
    slot: 'account' as const
  },
  {
    label: 'Administração',
    description: 'Faça Gerenciamento de usuários e permissões.',
    icon: 'i-lucide-lock',
    slot: 'password' as const
  }
] satisfies TabsItem[]

const user = reactive({
  name: "Joaquim Souza",
  username: "joaquimsouza",
  avatar: "https://avatar.iran.liara.run/public/46",
  email: "example@email.com",
  phone: "+55 11 91234-5678",
  address: "Rua Exemplo, 123",
  city: "São Paulo",
  team: "Asfalto e Cimento",
  helpedPeople: 723,
  problemsSolved: 423,
  permission: "Administrador",
})

const reports = ref([
  { id: '1001', date: '2025-07-10 09:30:00', title: 'Buraco na rua', description: "Encontrei esse buraco no canto da rua, esta juntando mosquitos da dengue até.", image: "https://s2-g1.glbimg.com/BXoCVbSSUMqwk8SrldbMK3pYYbg=/0x0:1280x960/984x0/smart/filters:strip_icc()/i.s3.glbimg.com/v1/AUTH_59edd422c0c84a879bd37670ae4f538a/internal_photos/bs/2018/1/p/JbO1BoTCu5FmmTAWCQvA/cratera-joao-pessoa-bayeux.jpg", status: 'pendente' },
  { id: '1002', date: '2025-07-09 14:15:00', title: 'Luz queimada em poste', description: "A luz do poste esta queimada quase fui atropelado por um cara que gritava \" chora garotinho\".", image: "https://s2-g1.glbimg.com/BXoCVbSSUMqwk8SrldbMK3pYYbg=/0x0:1280x960/984x0/smart/filters:strip_icc()/i.s3.glbimg.com/v1/AUTH_59edd422c0c84a879bd37670ae4f538a/internal_photos/bs/2018/1/p/JbO1BoTCu5FmmTAWCQvA/cratera-joao-pessoa-bayeux.jpg", status: 'em_andamento' },
  { id: '1003', date: '2025-07-08 17:45:00', title: 'Vazamento de água', description: "SDhjnkdfjdsbnkfjsdbfjsdl,.fhusdlfhjdsf,ljsdfghbu", image: "https://s2-g1.glbimg.com/BXoCVbSSUMqwk8SrldbMK3pYYbg=/0x0:1280x960/984x0/smart/filters:strip_icc()/i.s3.glbimg.com/v1/AUTH_59edd422c0c84a879bd37670ae4f538a/internal_photos/bs/2018/1/p/JbO1BoTCu5FmmTAWCQvA/cratera-joao-pessoa-bayeux.jpg", status: 'concluido' },
]);

const badges = ref([
  { icon: 'i-lucide-headset', label: 'Resolvedor de Problemas' },
  { icon: 'i-lucide-heart-handshake', label: 'Ajudante' },
]);

const avatarOptions = [
  "https://avatar.iran.liara.run/public/4",
  "https://avatar.iran.liara.run/public/74",
  "https://avatar.iran.liara.run/public/57",
  "https://avatar.iran.liara.run/public/10",
  "https://avatar.iran.liara.run/public/86",
  "https://avatar.iran.liara.run/public/43",
  "https://avatar.iran.liara.run/public/30",
  "https://avatar.iran.liara.run/public/64",
  "https://avatar.iran.liara.run/public/90",
  "https://avatar.iran.liara.run/public/14",
  "https://avatar.iran.liara.run/public/19",
  "https://avatar.iran.liara.run/public/96"
]
</script>

<template>
  <div class="grid grid-cols-1 gap-6 p-4 sm:p-6 md:p-8 xl:grid-cols-1">
    <div class="overflow-hidden rounded-2xl border border-gray-200 bg-white px-4 pb-3 pt-4 dark:border-gray-800 dark:bg-white/[0.03] sm:px-6 flex flex-col items-center">
      <img :src="user.avatar || 'https://avatar.iran.liara.run/public'" height="120" width="120" class="rounded-full mb-4" /> <!-- placeholder do avatar-->
      <UModal title="Escolha um Avatar">
        <UButton class="mb-4 px-4 py-2">Alterar avatar</UButton>
        <template #body>
          <div class="flex flex-wrap gap-4 justify-center items-center py-4">
            <div
              v-for="(avatar, idx) in avatarOptions"
              :key="avatar"
              class="cursor-pointer border-2 rounded-full p-1 transition-all duration-200"
              :class="user.avatar === avatar ? 'border-blue-500' : 'border-transparent'"
              @click="user.avatar = avatar"
            >
              <img :src="avatar" width="80" height="80" class="rounded-full" />
            </div>
          </div>
        </template>
      </UModal>
      <UTabs :items="items" variant="link" :ui="{ trigger: 'grow' }" class="gap-4 w-full">
        <template #account="{ item }">
          <p class="text-muted mb-4">
            {{ item.description }}
          </p>

          <UForm :state="user" class="flex flex-col gap-4">
            <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div class="flex flex-col gap-4">
                <UFormField label="Nome" name="name">
                  <UInput v-model="user.name" class="w-100" required />
                </UFormField>
                <UFormField label="Nome de Usuário" name="username">
                  <UInput v-model="user.username" class="w-100" required :disabled="true" />
                </UFormField>
                <UFormField label="Email" name="email">
                  <UInput v-model="user.email" class="w-100" required :disabled="true" />
                </UFormField>
              </div>
              <div class="flex flex-col gap-4">
                <UFormField label="Celular" name="cellphone">
                  <UInput v-model="user.phone" class="w-100" required />
                </UFormField>
                <UFormField label="Endereço" name="address">
                  <UInput v-model="user.address" class="w-100" required />
                </UFormField>
              </div>
            </div>
            <UButton label="Salvar Mudanças" type="submit" variant="soft" class="self-end mt-4" />
          </UForm>
          <div class="bg-gray-50 dark:bg-neutral-800 rounded-xl border border-gray-200 dark:border-gray-700 p-6 mt-8">
            <h2 class="text-xl font-semibold mb-6">Reportes Realizados</h2>
            <div class="flex flex-col gap-8">
              <div v-for="(report, idx) in reports" :key="report.id" class="flex flex-col gap-4">
                <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
                  <div class="flex flex-col gap-4">
                    <UFormField label="Tipo de Reporte" :name="`reportType-${idx}`">
                      <UInput v-model="report.title" :disabled="true" />
                    </UFormField>
                    <UFormField label="Data" :name="`reportDate-${idx}`">
                      <UInput v-model="report.date" :disabled="true" />
                    </UFormField>
                  </div>
                  <div class="flex flex-col gap-4">
                    <UFormField label="Descrição do Reporte" :name="`reportDescription-${idx}`">
                      <UTextarea v-model="report.description" :rows="5" :maxrows="5" autoresize class="w-full" :disabled="true"/>
                    </UFormField>
                    <div class="flex justify-end">
                      <UButton icon="i-lucide-trash-2" size="md" color="error" variant="solid" class="mt-2" />
                    </div>
                    <div class="w-[200px] h-[200px]">
                      <img :src="report.image" class="rounded-lg w-full h-full object-cover aspect-square" />
                    </div>
                  </div>
                </div>
                <hr v-if="idx < reports.length - 1" class="border-t border-gray-300 dark:border-gray-700 my-4" />
              </div>
            </div>
          </div>
        </template>

        <template #password="{ item }">
          <div class="flex flex-col items-center justify-center gap-8 py-12">
            <div class="grid grid-cols-1 md:grid-cols-2 gap-8 w-full">
              <div class="flex flex-col items-center justify-center">
                <h1 class="text-4xl font-bold mb-6 text-center">
                  <span class="text-gray-700">Problemas Resolvidos</span>
                  <br>
                  <span class="text-green-600 text-5xl font-extrabold">{{ user.problemsSolved }}</span>
                </h1>
              </div>
              <div class="flex flex-col items-center justify-center">
                <h1 class="text-4xl font-bold mb-6 text-center">
                  <span class="text-gray-700">Pessoas Beneficiadas por você</span>
                  <br>
                  <span class="text-green-600 text-5xl font-extrabold">{{ user.helpedPeople }}</span>
                </h1>
              </div>
            </div>
            <div class="flex flex-row items-center justify-center gap-4 mt-6">
              <template v-if="user.problemsSolved > 0">
                <UBadge
                  :icon="badges[0].icon"
                  size="md"
                  :color="user.problemsSolved > 300 ? 'info' : user.problemsSolved > 100 ? 'success' : 'primary'"
                  variant="solid"
                >{{ badges[0].label }}</UBadge>
              </template>
              <template v-if="user.helpedPeople > 0">
                <UBadge
                  :icon="badges[1].icon"
                  size="md"
                  :color="user.helpedPeople > 300 ? 'info' : user.helpedPeople > 100 ? 'success' : 'primary'"
                  variant="solid"
                >{{ badges[1].label }}</UBadge>
              </template>
            </div>
          </div>
          <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
            <div class="flex flex-col gap-4">
              <UFormField label="Permissão" :name="'permission'">
                <UInput v-model="user.permission" :disabled="true" />
              </UFormField>
              <UFormField label="Cidade" :name="'city'">
                <UInput v-model="user.city" :disabled="true" />
              </UFormField>
              <UFormField label="Equipe" :name="'team'">
                <UInput v-model="user.team" :disabled="true" />
              </UFormField>
            </div>
          </div>
        </template>
      </UTabs>
    </div>
  </div>
</template>