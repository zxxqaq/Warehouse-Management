<template>
  <a-layout style="min-height: 100vh">
    <Sider></Sider>
    <Overview v-if="selectedMenuItem === 'menu0'"></Overview>
    <Management v-if="selectedMenuItem === 'menu1'"></Management>
    <BoyuHistory v-if="selectedMenuItem === 'BoyuHistory'"></BoyuHistory>
  </a-layout>
</template>
<script setup>
import Sider from "@/components/Sider.vue";
import store from "@/store";
import {ref, watch} from "vue";
import BoyuHistory from "@/components/History/BoyuHistory.vue";
import Overview from "@/components/Overview.vue";

const selectedMenuItem = ref(store.getters.getSelectedMenuItem);

watch(() => store.state.selectedMenuItem, (newVal) => {
  selectedMenuItem.value = newVal;
});
</script>
<style scoped>
</style>
