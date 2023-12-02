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
        <a-table bordered :data-source="dataSource" :columns="columns" :scroll="{x: 2000, y: 500}">
          <template #bodyCell="{ column, text, record }" >
            <template v-if="false">
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
            <template v-if="column.dataIndex === 'inOut'">
              <span>
                <a-tag
                    :color="text === '入库' ? 'blue' : 'pink'"
                >
                  {{ text }}
                </a-tag>
              </span>
            </template>
            <template v-else-if="column.dataIndex === 'operation'">
              <a >入库</a>
              <a v-if="editRowStatus" style="margin-left: 10px" @click="save(record.key)">保存</a>
              <a v-else style="margin-left: 10px" @click="edit(record.key)">编辑</a>

            </template>
          </template>
        </a-table>
      </div>
    </a-layout-content>


  </a-layout>
</template>
<script lang="ts" setup>
import {computed, onMounted, reactive, ref, watch} from 'vue';
import type { Ref, UnwrapRef } from 'vue';
import { CheckOutlined, EditOutlined } from '@ant-design/icons-vue';
import { cloneDeep } from 'lodash-es';
import {TableColumnsType} from "ant-design-vue";
import { PlusOutlined } from '@ant-design/icons-vue';
import type { Rule } from 'ant-design-vue/es/form';
import { useStore } from "vuex";

const store = useStore();
interface DataItem {
  key: number;
  inOut: string;
  time: string;
  singleCount: string;
  name: string;
  standard: string;
  specification: string;
  surface: string;
  level: string;
  unit: string;
  inCount: number;
  outCount: number;
  kg: number;
  singleKg: string;
  singlePrice: string;
  totalCount: number;
}


const columns: TableColumnsType = [
  { title: '入库/出库', dataIndex: 'inOut'},
  { title: '时间', dataIndex: 'time'},
  { title: '本次数量', dataIndex: 'singleCount'},
  { title: '名称', dataIndex: 'name'},
  { title: '标准', dataIndex: 'standard'},
  { title: '规格', dataIndex: 'specification', width: 100},
  { title: '表面处理', dataIndex: 'surface', width: 100},
  { title: '等级', dataIndex: 'level', width: 100},
  { title: '单位', dataIndex: 'unit', width: 100},
  { title: '进库总数', dataIndex: 'inCount', width: 100},
  { title: '出库总数', dataIndex: 'outCount', width: 100},
  { title: '公斤', dataIndex: 'kg', width: 100},
  { title: '单重', dataIndex: 'singleKg', width: 100},
  { title: '单价', dataIndex: 'singlePrice', width: 100},
  { title: '库存数量', dataIndex: 'totalCount', width: 100},
  { title: '操作', dataIndex: 'operation', fixed: "right",},
];


const dataSource: Ref<DataItem[]> = ref([

]);
const data: Ref<DataItem[]> = ref([
]);
const updateDataSource = () => {
  const forms = store.getters.getBoyuHistory;
  dataSource.value = forms.map((form, index) => ({
    key: index,
    inOut: form.inOut,
    time: form.time,
    singleCount: form.singleCount,
    name: form.name,
    standard: form.standard,
    specification: form.specification,
    surface: form.surface,
    level: form.level,
    unit: form.unit,
    inCount: form.inCount,
    outCount: form.outCount,
    kg: form.kg,
    singleKg: form.singleKg,
    singlePrice: form.singlePrice,
    totalCount: form.totalCount,
  }));
}
onMounted(updateDataSource);
watch(() => store.getters.getBoyuHistory, updateDataSource);
const count = computed(() => dataSource.value.length + 1);
const editableData: UnwrapRef<Record<string, DataItem>> = reactive({});
const editRowStatus = ref(false);

const edit = (key: number) => {
  editableData[key] = cloneDeep(dataSource.value.filter(item => key === item.key)[0]);
  editRowStatus.value = !editRowStatus.value;
};
const save = (key: number) => {
  Object.assign(dataSource.value.filter(item => key === item.key)[0], editableData[key]);
  delete editableData[key];
  editRowStatus.value = !editRowStatus.value;
};

const onDelete = (key: number) => {
  dataSource.value = dataSource.value.filter(item => item.key !== key);
};
const handleAdd = () => {
};

const open = ref<boolean>(false);

const onClose = () => {
  open.value = false;
};
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
