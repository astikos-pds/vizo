<script lang="ts" setup>
const { logout } = useAuth();

await logout();

await navigateTo("/login");
</script>

<template></template>
