<template>
  <a-layout>
    <a-layout-header style="background: #fff; padding: 0">
      <div class="btn-group">

      </div>
    </a-layout-header>
    <a-layout-content style="margin: 0 16px">
      <a-breadcrumb style="margin: 16px 0">
        <a-breadcrumb-item></a-breadcrumb-item>
        <a-breadcrumb-item></a-breadcrumb-item>
      </a-breadcrumb>

      <div :style="{ padding: '24px', background: '#fff', minHeight: '360px' }">
        <a-button class="editable-add-btn" style="margin-bottom: 10px" @click="handleAdd">添加行</a-button>
        <a-table bordered :data-source="dataSource" :columns="columns">
          <template #bodyCell="{ column, text, record }" >
            <template v-if="column.dataIndex === 'name' ||
                            column.dataIndex === 'money' ||
                            column.dataIndex === 'taxNum' ||
                            column.dataIndex === 'taxRate' ||
                            column.dataIndex === 'invoiceCode' ||
                            column.dataIndex === 'invoiceNum'">
              <div class="editable-cell">
                <div v-if="editableData[record.key]" class="editable-cell-input-wrapper">
                  <a-input v-model:value="editableData[record.key][column.dataIndex]" @pressEnter="save(record.key)" />
                  <!--                  <check-outlined class="editable-cell-icon-check" @click="save(record.key)" />-->
                </div>
                <div v-else class="editable-cell-text-wrapper">
                  {{ text || ' ' }}
                  <!--                  <edit-outlined class="editable-cell-icon" @click="edit(record.key)" />-->
                </div>
              </div>
            </template>

            <template v-else-if="column.dataIndex === 'operation'">
              <a-popconfirm
                  v-if="dataSource.length"
                  title="确定删除?"
                  @confirm="onDelete(record.key)"
              >
                <a >删除</a>
              </a-popconfirm>
              <a v-if="editRowStatus" style="margin-left: 10px" @click="save(record.key)">保存</a>
              <a v-else style="margin-left: 10px" @click="edit(record.key)">编辑</a>

            </template>
          </template>
        </a-table>
      </div>
    </a-layout-content>
    <a-layout-footer style="text-align: center">
      Ant Design ©2018 Created by Ant UED
    </a-layout-footer>
  </a-layout>
</template>
<script lang="ts" setup>
import {computed, onMounted, reactive, ref} from 'vue';
import type { Ref, UnwrapRef } from 'vue';
import { CheckOutlined, EditOutlined } from '@ant-design/icons-vue';
import { cloneDeep } from 'lodash-es';
import {TableColumnsType} from "ant-design-vue";

interface DataItem {
  key: string;
  name: string;
  money: string;
  taxNum: string;
  taxRate: string;
  invoiceCode: string;
  invoiceNum: string;
}


const columns: TableColumnsType = [
  { title: '名称', dataIndex: 'name', fixed: 'left', width: '20%',},
  { title: '金额', dataIndex: 'money', width: '10%'},
  { title: '税号', dataIndex: 'taxNum', width: '10%',},
  { title: '税率', dataIndex: 'taxRate', width: '8%'},
  { title: '发票代码', dataIndex: 'invoiceCode', width: '15%',},
  { title: '发票号码', dataIndex: 'invoiceNum', width: '15%',},
  { title: '操作', dataIndex: 'operation', fixed: "right",},
];
const dataSource: Ref<DataItem[]> = ref([]);
const count = computed(() => dataSource.value.length + 1);
const editableData: UnwrapRef<Record<string, DataItem>> = reactive({});
const editRowStatus = ref(false);

const fetchData = async () => {
  try {
    const response = await fetch('http://localhost:8080/getOverview');
    const data = await response.json();
    if (data.code === 200){
      dataSource.value = data.data;
    } else {
      console.error('Failed to fetch data:', data.message);
    }
  } catch (error) {
    console.error('An error occurred during fetch:', error);
  }
};

const edit = (key: string) => {
  editableData[key] = cloneDeep(dataSource.value.filter(item => key === item.key)[0]);
  editRowStatus.value = !editRowStatus.value;
};
const save = (key: string) => {
  Object.assign(dataSource.value.filter(item => key === item.key)[0], editableData[key]);
  delete editableData[key];
  editRowStatus.value = !editRowStatus.value;
};

const onDelete = async (key: string) => {
  try {
    // 在前端删除数据
    dataSource.value = dataSource.value.filter(item => item.key !== key);

    // 发送请求到后端删除数据
    const response = await fetch(`http://localhost:8080/deleteOverview/${key}`, {
      method: 'DELETE',
    });
    const data = await response.json();

    if (data.code !== 200) {
      console.error('Failed to delete data from backend:', data.message);
    }
  } catch (error) {
    console.error('An error occurred during delete from backend:', error);
  }
};
const handleAdd = () => {
  const newData = {
    key: `${count.value}`, //  自动生成的key
    name: null,
    money: null,
    taxNum: null,
    taxRate: null,
    invoiceCode: null,
    invoiceNum: null,
  };
  dataSource.value.push(newData);
};

onMounted(() => {
  fetchData();
})
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
