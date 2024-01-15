
<template>
  <a-layout-sider v-model:collapsed="collapsed" collapsible>
    <div class="logo" />
    <a-menu v-model:selectedKeys="selectedKeys" theme="dark" mode="inline" @click="handleMenuClick">
      <a-menu-item key="menu0">
        <desktop-outlined />
        <span>总览</span>
      </a-menu-item>
      <a-menu-item key="menu1">
        <team-outlined />
        <span>库存管理</span>
      </a-menu-item>
      <a-menu-item key="menu2">
        <team-outlined />
        <span>操作历史</span>
      </a-menu-item>
    </a-menu>
  </a-layout-sider>
</template>
<script setup>

import {DesktopOutlined, FileOutlined, PieChartOutlined, TeamOutlined, UserOutlined} from "@ant-design/icons-vue";
import {ref} from "vue";
import { useStore} from "vuex";

const store = useStore();
const collapsed = ref(false);
const selectedKeys = ref(['menu0']);

const handleMenuClick = ({ key }) => {
  store.commit('setSelectedMenuItem', key);
  console.log(key);
}
</script>
<style scoped>
.logo {
  height: 32px;
  margin: 16px;
  background: rgba(255, 255, 255, 0.3);
}
</style>
