
<template>
  <a-layout-sider v-model:collapsed="collapsed" collapsible>
    <div class="logo" />
    <a-menu v-model:selectedKeys="selectedKeys" theme="dark" mode="inline" @click="handleMenuClick">
      <a-menu-item key="0">
        <desktop-outlined />
        <span>总览</span>
      </a-menu-item>
      <a-sub-menu key="sub1">
        <template #title>
            <span>
              <team-outlined />
              <span>库存管理</span>
            </span>
        </template>
        <a-menu-item key="BoyuManagement">博羽</a-menu-item>
        <a-menu-item key="1_2">斯迈欧</a-menu-item>
      </a-sub-menu>
      <a-sub-menu key="sub2">
        <template #title>
            <span>
              <team-outlined />
              <span>历史记录</span>
            </span>
        </template>
        <a-menu-item key="BoyuHistory">博羽</a-menu-item>
        <a-menu-item key="2_2">斯迈欧</a-menu-item>
      </a-sub-menu>
    </a-menu>
  </a-layout-sider>
</template>
<script setup>

import {DesktopOutlined, FileOutlined, PieChartOutlined, TeamOutlined, UserOutlined} from "@ant-design/icons-vue";
import {ref} from "vue";
import { useStore} from "vuex";

const store = useStore();
const collapsed = ref(false);
const selectedKeys = ref(['0']);

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
