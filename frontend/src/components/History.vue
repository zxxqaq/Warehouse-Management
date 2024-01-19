<template>
  <a-layout>
    <a-layout-content style="margin: 0 16px">
      <a-breadcrumb style="margin: 16px 0">
        <a-breadcrumb-item></a-breadcrumb-item>
        <a-breadcrumb-item></a-breadcrumb-item>
      </a-breadcrumb>

      <div :style="{ padding: '24px', background: '#fff', minHeight: '360px' }">
        <a-space direction="vertical" style="margin-bottom: 10px">
          <a-select
              v-model:value="defaultSelectCompany"
              size="middle"
              style="width: 250px"
              :options="options"
              @select="handleCompanyChange"
          ></a-select>
        </a-space>



        <a-table bordered :data-source="dataSource" :columns="columns" :scroll="{x: 1500, y: 500}">
          <template #emptyText>
            <InboxOutlined style="font-size: xxx-large" />
            <p style="margin-top: 10px; margin-bottom: 0">没有数据，请先选择公司名称</p>
          </template>

          <template #bodyCell="{ column, text, record }" >

            <template v-if="column.dataIndex === 'operation'">
              <a @click="showDrawer(record.itemId)">入库</a>
            </template>
          </template>
        </a-table>
      </div>
    </a-layout-content>


  </a-layout>
</template>
<script lang="ts" setup>
import type {Ref, UnwrapRef} from 'vue';
import {computed, onMounted, reactive, ref} from 'vue';
import {message, SelectProps, TableColumnsType} from "ant-design-vue";
import {InboxOutlined, MoneyCollectOutlined, PlusOutlined} from '@ant-design/icons-vue';
import type {Rule} from 'ant-design-vue/es/form';
import {useStore} from "vuex";
import dayjs from 'dayjs';
import 'dayjs/locale/zh-cn';

const store = useStore();
const defaultSelectCompany = ref('选择公司')
const companyList = store.getters.getCompanyList;
const options = ref<SelectProps['options']>(companyList.map(item => ({
  value: String(item.companyId),
  label: item.companyName,
})));

const handleCompanyChange  = (id: number) => {
  // 向后端发送companyId，查询对应的itemId和信息
}

interface Record {
  recordId: number
  itemName: string,
  standard: string,
  specification: string,
  material: string,
  level: string,
  surface: string,
  unitWeight: number,
  type: string,
  date: string,
  amount: number,
  unit: string,
  totalWeight: number,
  direction: string,
  userName: string,
  unitPrice: number
}
const dataSource: Ref<Record[]> = ref([]);

</script>

<style lang="less" scoped>
.btn-group{
  display: flex;
  align-items: center;
  height: 100%;
  padding-left: 20px;
}
.editable-cell {
  position: relative;
  .editable-cell-input-wrapper,
  .editable-cell-text-wrapper {
    padding-right: 24px;
  }

  .editable-cell-text-wrapper {
    padding: 5px 24px 5px 5px;
  }

  .editable-cell-icon,
  .editable-cell-icon-check {
    position: absolute;
    right: 0;
    width: 20px;
    cursor: pointer;
  }

  .editable-cell-icon {
    margin-top: 4px;
    display: none;
  }

  .editable-cell-icon-check {
    line-height: 28px;
  }

  .editable-cell-icon:hover,
  .editable-cell-icon-check:hover {
    color: #108ee9;
  }

  .editable-add-btn {
    margin: 8px;
  }
}
.editable-cell:hover .editable-cell-icon {
  display: inline-block;
}
</style>
