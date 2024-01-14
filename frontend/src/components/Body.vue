<template>
  <a-layout>
    <a-layout-header style="background: #fff; padding: 0">
      <div class="btn-group">
        <a-button class="editable-add-btn" style="margin-bottom: 10px" @click="showDrawer">
          <template #icon><PlusOutlined /></template>
          添加公司
        </a-button>
      </div>
    </a-layout-header>
    <a-layout-content style="margin: 0 16px">
      <a-breadcrumb style="margin: 16px 0">
        <a-breadcrumb-item></a-breadcrumb-item>
        <a-breadcrumb-item></a-breadcrumb-item>
      </a-breadcrumb>

      <div :style="{ padding: '24px', background: '#fff', minHeight: '360px' }">

        <a-table bordered :data-source="dataSource" :columns="columns">
          <template #bodyCell="{ column, text, record }" >
            <template v-if="column.dataIndex === 'name' ||
                            column.dataIndex === 'taxNum'">
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
              <div class="editable-row-operations">
                <span v-if="editableData[record.key]">
                  <a-typography-link  @click="save(record.key)">保存</a-typography-link>
                  <a-typography-link style="margin-left: 10px" @click="cancel(record.key)">取消</a-typography-link>
                </span>
                <span v-else>
                  <a @click="edit(record.key)">编辑</a>
                </span>
              </div>
            </template>
          </template>
        </a-table>
      </div>
    </a-layout-content>
    <a-layout-footer style="text-align: center">
      Ant Design ©2018 Created by Ant UED
    </a-layout-footer>
    <a-drawer
        title="添加新公司"
        :width="720"
        :open="open"
        :body-style="{ paddingBottom: '80px' }"
        :footer-style="{ textAlign: 'right' }"
        @close="onClose"
    >
      <a-form :model="form" :rules="rules" layout="vertical">
        <a-row :gutter="16">
          <a-col :span="24">
            <a-form-item label="名称" name="name">
              <a-input v-model:value="form.name"  />
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="16">
          <a-col :span="24">
            <a-form-item label="税号" name="taxNum">
              <a-input v-model:value="form.taxNum"  />
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
      <template #extra>
        <a-space>
          <a-button @click="onClose">取消</a-button>
          <a-button :disabled="disabled" type="primary" @click="onSubmitCompany">确定</a-button>
        </a-space>
      </template>
    </a-drawer>
  </a-layout>

</template>
<script lang="ts" setup>
import { PlusOutlined } from '@ant-design/icons-vue';
import type { Rule } from 'ant-design-vue/es/form';
import {computed, onMounted, reactive, ref} from 'vue';
import type { Ref, UnwrapRef } from 'vue';
import { CheckOutlined, EditOutlined } from '@ant-design/icons-vue';
import { cloneDeep } from 'lodash-es';
import {TableColumnsType} from "ant-design-vue";

const form = reactive({
  name: null,
  taxNum: null,
});
const rules: Record<string, Rule[]> = {
  name: [{ required: true, message: '' }],
};
const open = ref<boolean>(false);

const showDrawer = () => {
  open.value = true;
};

const onClose = () => {
  open.value = false;
};

const disabled = computed(() => {
  return !(form.name);
})
const onSubmitCompany = () => {
    //提交新公司表单
};

interface DataItem {
  key: string;
  name: string;
  taxNum: string;
}


const columns: TableColumnsType = [
  { title: '名称', dataIndex: 'name', fixed: 'left', width: '40%',},
  { title: '税号', dataIndex: 'taxNum', width: '40%',},
  { title: '操作', dataIndex: 'operation', fixed: "right",},
];
const dataSource: Ref<DataItem[]> = ref([
  {
    key: '1',
    name: '嘉兴博羽股份有限公司',
    taxNum: '001',
  },
  {
    key: '2',
    name: '嘉兴猪猪股份有限公司',
    taxNum: '001',
  },
]);
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
};
const save = (key: string) => {
  Object.assign(dataSource.value.filter(item => key === item.key)[0], editableData[key]);
  delete editableData[key];
};
const cancel = (key: string) => {
  delete editableData[key];
};


const handleAdd = () => {
  const newData = {
    key: `${count.value}`, //  自动生成的key
    name: null,
    taxNum: null,
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
