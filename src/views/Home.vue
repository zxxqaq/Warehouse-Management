<template>
  <a-layout style="min-height: 100vh">
    <Sider></Sider>
    <Body v-if="selectedMenuItem === '0'"></Body>
    <BoyuManagement v-if="selectedMenuItem === 'BoyuManagement'"></BoyuManagement>
    <BoyuHistory v-if="selectedMenuItem === 'BoyuHistory'"></BoyuHistory>
  </a-layout>
</template>
<script setup>
import Sider from "@/components/Sider.vue";
import store from "@/store";
import {ref, watch} from "vue";
import BoyuManagement from "@/components/Management/BoyuManagement.vue";
import BoyuHistory from "@/components/History/BoyuHistory.vue";

const selectedMenuItem = ref(store.getters.getSelectedMenuItem);

watch(() => store.state.selectedMenuItem, (newVal) => {
  selectedMenuItem.value = newVal;
});
</script>
<style scoped>
</style>
