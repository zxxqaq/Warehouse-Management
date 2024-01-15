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
                @change="handleCompanyChange"
            ></a-select>
          </a-space>


        <a-table bordered :data-source="dataSource" :columns="columns" :scroll="{x: 1500, y: 500}">
          <template #emptyText>
            <InboxOutlined style="font-size: xxx-large" />
            <p style="margin-top: 10px; margin-bottom: 0">没有数据，请先选择公司名称</p>
          </template>

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

            <template v-else-if="column.dataIndex === 'operation'">
              <a @click="showDrawer(record.key)">入库</a>
              <a v-if="editRowStatus" style="margin-left: 10px" @click="save(record.key)">保存</a>
              <a v-else style="margin-left: 10px" @click="edit(record.key)">编辑</a>
            </template>
          </template>
        </a-table>
      </div>
    </a-layout-content>

    <a-drawer
        title="入库"
        :width="720"
        :open="open"
        :body-style="{ paddingBottom: '80px' }"
        :footer-style="{ textAlign: 'right' }"
        @close="onClose"
    >
      <a-form :model="form" :rules="rules" layout="vertical">
        <a-row :gutter="16">
          <a-col :span="6">
            <a-form-item label="名称" name="name" >
              <a-input  v-model:value="form.name" :placeholder="form.name"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item label="标准" name="standard" >
              <a-input  v-model:value="form.standard" :placeholder="form.standard"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item label="规格" name="specification" >
              <a-input  v-model:value="form.specification" :placeholder="form.specification"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item label="表面处理" name="surface" >
              <a-input  v-model:value="form.surface" :placeholder="form.surface"></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="16">
          <a-col :span="6">
            <a-form-item label="等级" name="level" >
              <a-input  v-model:value="form.level" :placeholder="form.level"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item label="入库时间" name="time" >
              <a-input  v-model:value="form.time" :placeholder="form.time"></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="16">
          <a-col :span="6">
            <a-form-item label="当前库存数量" name="totalCount" >
              <a-input  v-model:value="form.totalCount" :placeholder="form.totalCount"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item label="入库数量" name="singleCount" >
              <a-input v-model:value="form.singleCount" :placeholder="form.singleCount"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item label="入库后总库存" name="afterTotalCount" >
              <a-input  :placeholder="afterTotalCount"></a-input>
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
      <template #extra>
        <a-space>
          <a-button @click="onClose">取消</a-button>
          <a-button type="primary" @click="onClose">提交</a-button>
        </a-space>
      </template>
    </a-drawer>

  </a-layout>
</template>
<script lang="ts" setup>
import {computed, onMounted, reactive, ref} from 'vue';
import type { Ref, UnwrapRef } from 'vue';
import { CheckOutlined, EditOutlined } from '@ant-design/icons-vue';
import { cloneDeep } from 'lodash-es';
import {SelectProps, TableColumnsType} from "ant-design-vue";
import { InboxOutlined } from '@ant-design/icons-vue';
import type { Rule } from 'ant-design-vue/es/form';
import { useStore} from "vuex";
import {SelectValue} from "ant-design-vue/es/select";

const store = useStore();
interface DataItem {
  key: string;
  name: string;
  standard: string;
  specification: string;
  surface: string;
  material: string;
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
  { title: '名称', dataIndex: 'name', fixed: 'left',},
  { title: '标准', dataIndex: 'standard', fixed: 'left'},
  { title: '规格', dataIndex: 'specification', width: 100},
  { title: '表面处理', dataIndex: 'surface', width: 100},
  { title: '材质', dataIndex: 'material', width: 100},
  { title: '等级', dataIndex: 'level', width: 100},
  { title: '单位', dataIndex: 'unit', width: 80},
  { title: '进库数量', dataIndex: 'inCount', width: 100},
  { title: '出库数量', dataIndex: 'outCount', width: 100},
  { title: '公斤', dataIndex: 'kg', width: 100},
  { title: '单重', dataIndex: 'singleKg', width: 100},
  { title: '单价', dataIndex: 'singlePrice', width: 100},
  { title: '库存数量', dataIndex: 'totalCount', fixed: "right"},
  { title: '操作', dataIndex: 'operation', fixed: "right",},
];
const dataSource: Ref<DataItem[]> = ref([

]);

const data: Ref<DataItem[]> = ref([
  {
    key: '3',
    name: '六角螺栓',
    standard: 'GB/T 5783-2016',
    specification: 'M8*35',
    surface: '35K 光身',
    material: '铁',
    level: '6.8',
    unit: '个',
    inCount: 0,
    outCount: 5000,
    kg: 326,
    singleKg: '16.2',
    singlePrice: '6.5',
    totalCount: 15123
  }
]);
let defaultSelectCompany = ref('选择公司');
const selectCompany = store.getters.getSelectedCompany;
onMounted( () => {
  if (selectCompany !== '') {
    handleCompanyChange(selectCompany);
    defaultSelectCompany = ref("嘉兴博羽股份有限公司"); //找到key值一样的公司名称
  }
})


const options = ref<SelectProps['options']>([
  {
    value: '1',
    label: '嘉兴博羽股份有限公司',
  },
  {
    value: '2',
    label: '嘉兴猪猪股份有限公司',
    // disabled: true,
  }
]);

const handleCompanyChange  = (value: string) => {
  console.log(value);
  // 找到对应的value的公司表，获取数据,更新dataSource
  pushCompanyDataSource(value);
}
const count = computed(() => dataSource.value.length + 1);
const editableData: UnwrapRef<Record<string, DataItem>> = reactive({});
const editRowStatus = ref(false);

const edit = (key: string) => {
  editableData[key] = cloneDeep(dataSource.value.filter(item => key === item.key)[0]);
  editRowStatus.value = !editRowStatus.value;
};
const save = (key: string) => {
  Object.assign(dataSource.value.filter(item => key === item.key)[0], editableData[key]);
  delete editableData[key];
  editRowStatus.value = !editRowStatus.value;
};

const onDelete = (key: string) => {
  dataSource.value = dataSource.value.filter(item => item.key !== key);
};

const pushCompanyDataSource = (companyKey: string) => {
  const newData = {
    key: companyKey + `count.value`,
    name: '六角螺栓',
    standard: 'GB/T 5783-2016',
    specification: 'M8*35',
    surface: '35K 光身',
    material: '铁',
    level: '6.8',
    unit: '个',
    inCount: 0,
    outCount: 5000,
    kg: 326,
    singleKg: '16.2',
    singlePrice: '6.5',
    totalCount: 15123
  };
  dataSource.value.push(newData);
};

const form = reactive({
  key: null,
  name: null,
  standard: null,
  specification: null,
  surface: null,
  level: null,
  unit: null,
  inCount: null,
  outCount: null,
  kg: null,
  singleKg: null,
  singlePrice: null,
  totalCount: null,
  inOut: null,
  time: new Date().toLocaleString('zh-CN'),
  singleCount: null
});

const afterTotalCount = computed(() => {
  return parseInt(form.totalCount) + parseInt(form.singleCount) ;
})
const rules: Record<string, Rule[]> = {
  singleCount: [{ required: true, message: '' }],
};

const open = ref<boolean>(false);
const updateDataSource = (keyToUpdate: string, newInCount: number, newTotalCount: number) => {
  // 在 dataSource 中查找特定的 key
  const targetItemIndex = dataSource.value.findIndex(item => item.key === keyToUpdate);

  if (targetItemIndex !== -1) {
    // 找到了目标对象，更新 inCount
    dataSource.value[targetItemIndex].inCount = newInCount;
    dataSource.value[targetItemIndex].totalCount = newTotalCount;
  } else {
    // 如果找不到目标对象，可以进行错误处理或记录日志
    console.error(`Unable to find item with key ${keyToUpdate}`);
  }
};
const showDrawer = (key: string) => {
  data.value = dataSource.value.filter(item => item.key === key);
  const firstItem = data.value[0];
  for (const key in firstItem) {
    if (Object.prototype.hasOwnProperty.call(firstItem, key)) {
      form[key] = firstItem[key];
      form["inOut"] = ref('入库');
      form["singleCount"] = ref('0');
    }
  }
  open.value = true;
};

const onClose = () => {
  open.value = false;
  form.inCount = ref(parseInt(form.inCount) + parseInt(form.singleCount));
  form.totalCount = ref(parseInt(form.totalCount) + parseInt(form.singleCount));
  updateDataSource(form.key, form.inCount, form.totalCount);
  console.log(form.totalCount);
  store.dispatch('saveBoyuHistory',[form]);
  console.log(form.singleCount);
  console.log(store.getters.getBoyuHistory);
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
