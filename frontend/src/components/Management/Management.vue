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
        <a-button class="editable-add-btn" style="margin-left: 20px" @click="initializeItem">
          初始化/新建
        </a-button>


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

    <a-drawer
        title="初始化/新建"
        :width="720"
        :open="openInitializeForm"
        :body-style="{ paddingBottom: '80px' }"
        :footer-style="{ textAlign: 'right' }"
        @close="onCloseInitializeDrawer"
    >
      <a-form :model="initializeForm"  layout="vertical">
        <a-row :gutter="16">
          <a-col :span="6">
            <a-form-item label="名称" name="itemName" >
              <a-input  v-model:value="initializeForm.itemName" ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item label="标准" name="standard" >
              <a-input  v-model:value="initializeForm.standard" ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item label="规格" name="specification" >
              <a-input  v-model:value="initializeForm.specification" ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item label="表面处理" name="surface" >
              <a-input  v-model:value="initializeForm.surface" ></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="16">
          <a-col :span="6">
            <a-form-item label="材质" name="material" >
              <a-input  v-model:value="initializeForm.material" ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item label="等级" name="level" >
              <a-input  v-model:value="initializeForm.level" ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item label="单重" name="unitWeight" >
              <a-input  v-model:value="initializeForm.unitWeight" ></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item label="单位" name="unit" >
              <a-input  v-model:value="initializeForm.unit" ></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-item label="时间" name="date" >
              <a-space direction="vertical" :size="12">
                <a-config-provider :locale="zhCN">
                  <a-date-picker style="width: 20.5rem" v-model:value="initializeForm.date" />
                </a-config-provider>
              </a-space>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="数量" name="amount">
              <a-input-number v-model:value="initializeForm.amount" placeholder="请输入整数" style="width: 100%" />
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
      <template #extra>
        <a-space>
          <a-button @click="onCloseInitializeDrawer">取消</a-button>
          <a-button :disabled="submitInitializeDisabled" type="primary" @click="onCloseDrawer">提交</a-button>
        </a-space>
      </template>
    </a-drawer>
    <a-drawer
        title="库存管理"
        :width="720"
        :open="openForm"
        :body-style="{ paddingBottom: '80px' }"
        :footer-style="{ textAlign: 'right' }"
        @close="onCloseDrawer"
    >
      <a-form :model="form" :rules="rules" layout="vertical">
        <a-row :gutter="16">
          <a-col :span="6">
            <a-form-item label="名称" name="itemName" >
              <a-input disabled  v-model:value="form.itemName" :placeholder="form.itemName"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item label="标准" name="standard" >
              <a-input disabled  v-model:value="form.standard" :placeholder="form.standard"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item label="规格" name="specification" >
              <a-input disabled  v-model:value="form.specification" :placeholder="form.specification"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item label="表面处理" name="surface" >
              <a-input disabled  v-model:value="form.surface" :placeholder="form.surface"></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="16">
          <a-col :span="6">
            <a-form-item label="材质" name="material" >
              <a-input disabled  v-model:value="form.material" :placeholder="form.material"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item label="等级" name="level" >
              <a-input disabled  v-model:value="form.level" :placeholder="form.level"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item label="单重" name="unitWeight" >
              <a-input disabled  v-model:value="form.unitWeight" :placeholder="form.unitWeight"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item label="单位" name="unit" >
              <a-input disabled  v-model:value="form.unit" :placeholder="form.unit"></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-item label="时间" name="date" >
              <a-space direction="vertical" :size="12">
                <a-config-provider :locale="zhCN">
                  <a-date-picker style="width: 20.5rem" v-model:value="form.date" />
                </a-config-provider>
              </a-space>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="单价" name="unitPrice">
              <a-input-number v-model:value="form.unitPrice" prefix="￥" style="width: 100%" />
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-item label="类型" name="recordType">
              <a-select
                  ref="select"
                  v-model:value="form.recordType"
                  style="width: 20.5rem"
              >
                <a-select-option value="1">入库</a-select-option>
                <a-select-option value="0">出库</a-select-option>
                <a-select-option value="2">初始化</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item v-if="form.recordType === '0' " label="出库方向" name="direction">
              <a-input v-model:value="form.direction"></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-item label="方式" name="">
              <a-select
                  ref="select"
                  v-model:value="isWeight"
              >
                <a-select-option value="1">称重（kg）</a-select-option>
                <a-select-option value="0">数量</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
            <a-col v-if="isWeight === '1'" :span="12">
              <a-form-item label="重量" name="totalWeight">
                <a-input v-model:value="form.totalWeight" placeholder=""></a-input>
              </a-form-item>
            </a-col>

            <a-col v-else-if="isWeight === '0'" :span="12">
              <a-form-item label="数量" name="amount">
                <a-input v-model:value="form.amount" placeholder="请输入数量"></a-input>
              </a-form-item>
            </a-col>


        </a-row>
        <a-row :gutter="16">
          <a-col v-if="isWeight === '1'" :span="12">
            <a-form-item label="数量" name="amount">
              <a-input disabled  :placeholder="form.amount"></a-input>
            </a-form-item>
          </a-col>
          <a-col v-else-if="isWeight === '0'"  :span="12">
            <a-form-item label="重量" name="totalWeight">
              <a-input disabled  :placeholder="form.totalWeight"></a-input>
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
      <template #extra>
        <a-space>
          <a-button @click="onCloseDrawer">取消</a-button>
          <a-button :disabled="submitDisabled" type="primary" @click="onCloseDrawer">提交</a-button>
        </a-space>
      </template>
    </a-drawer>

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
import zhCN from "ant-design-vue/es/locale/zh_CN";

const store = useStore();
const companyList = store.getters.getCompanyList;
const options = ref<SelectProps['options']>(companyList.map(item => ({
  value: String(item.companyId),
  label: item.companyName,
})));

let defaultSelectCompany = ref('选择公司');
const company = store.getters.getSelectedCompany;
store.commit("setSelectedCompany",null)
const companyId = ref()
onMounted( () => {
  if (company !== null) {
    defaultSelectCompany.value = company.companyName;
    handleCompanyChange(company.companyId)
  }
})

const handleCompanyChange  = (id: number) => {
  companyId.value = id;
  // 向后端发送companyId，查询对应的itemId和信息
  pushCompanyDataSource(id); //这里是模仿获取到了信息
}
interface ItemSummary {
  itemId: number;
  itemName: string;
  standard: string;
  specification: string;
  surface: string;
  material: string;
  level: string;
  unitWeight: number;
  unit: string;

  inCount: number;
  outCount: number;
  weightCount: number;
  totalCount: number;
}
const pushCompanyDataSource = (companyId: number) => {
  const newData = {
    itemId: 1,
    itemName: "六角螺栓",
    standard: "GB/T 5783-2016",
    specification: "M8*35",
    surface: "35K 光身",
    material: "铁",
    level: "6.8",
    unitWeight: 16.2, //kg
    unit: "个",
    inCount: 200,
    outCount: 100,
    weightCount: 1620, //totalCount * unitWeight
    totalCount: 100, //inCount-outCount
  };
  dataSource.value.push(newData);
};
const columns: TableColumnsType = [
  { title: '名称', dataIndex: 'itemName', fixed: 'left',},
  { title: '标准', dataIndex: 'standard', fixed: 'left'},
  { title: '规格', dataIndex: 'specification', width: 100},
  { title: '表面处理', dataIndex: 'surface', width: 100},
  { title: '材质', dataIndex: 'material', width: 100},
  { title: '等级', dataIndex: 'level', width: 100},
  { title: '单重', dataIndex: 'unitWeight', width: 100},
  { title: '单位', dataIndex: 'unit', width: 80},

  { title: '总进库数量', dataIndex: 'inCount', width: 100},
  { title: '总出库数量', dataIndex: 'outCount', width: 100},
  { title: '总公斤', dataIndex: 'weightCount', width: 100},
  { title: '总库存数量', dataIndex: 'totalCount', fixed: "right"},
  { title: '操作', dataIndex: 'operation', fixed: "right",},
];
const dataSource: Ref<ItemSummary[]> = ref([

]);



const form = reactive({
  userName: null,
  companyId: null,
  recordType: null,
  date: null,
  amount: null,
  unitPrice: null,
  totalWeight: null,
  direction: null,

  itemId: null,
  itemName: null,
  standard: null,
  specification: null,
  surface: null,
  material: null,
  level: null,
  unitWeight: null,
  unit: null,
});

const initializeForm = reactive({
  companyId: null,
  itemName: null,
  standard: null,
  specification: null,
  surface: null,
  material: null,
  level: null,
  unitWeight: null,
  unit: null,
  amount: null,
  date: null,
});

const initializeItem = () =>{
  if (defaultSelectCompany.value === '选择公司'){
    message.warn("请先选择公司");
  }else {
    showInitializeDrawer();
  }
}
const openInitializeForm = ref<boolean>(false);
const onCloseInitializeDrawer = () =>{
  openInitializeForm.value = false;
}
const showInitializeDrawer = () => {
  openInitializeForm.value = true;
}

const submitInitializeDisabled = computed(() => {
  return !(initializeForm.itemName && initializeForm.standard && initializeForm.specification && initializeForm.surface
  && initializeForm.material && initializeForm.level && initializeForm.unitWeight && initializeForm.unit && initializeForm.date
  && initializeForm.amount);
})

const isWeight = ref();

const weightOrAmount = computed(() => {
  if (isWeight.value === '1'){
    form.amount = form.totalWeight/form.unitWeight;
  }
  if (isWeight.value === '0'){
    form.totalWeight = form.amount*form.unitWeight;
  }
})
const openForm = ref<boolean>(false);
const showDrawer = (itemId: number) => {
  dataSource.value = dataSource.value.filter(item => item.itemId === itemId);
  const firstItem = dataSource.value[0];
  for (const key in firstItem) {
    if (Object.prototype.hasOwnProperty.call(firstItem, key)) {
      form[key] = firstItem[key];
      form["companyId"] = defaultSelectCompany;
      form["userName"] = store.getters.getUserFullName;
    }
  }
  openForm.value = true;
};

const submitDisabled = computed(() => {
  return !(form.date);
})
dayjs.locale('zh-cn')

const count = computed(() => dataSource.value.length + 1);
const editableData: UnwrapRef<Record<string, ItemSummary>> = reactive({});



const rules: Record<string, Rule[]> = {
  singleCount: [{ required: true, message: '' }],
};


const updateDataSource = (keyToUpdate: string, newInCount: number, newTotalCount: number) => {
  // 在 dataSource 中查找特定的 key
  const targetItemIndex = dataSource.value.findIndex(item => item.itemId === keyToUpdate);

  if (targetItemIndex !== -1) {
    // 找到了目标对象，更新 inCount
    dataSource.value[targetItemIndex].inCount = newInCount;
    dataSource.value[targetItemIndex].totalCount = newTotalCount;
  } else {
    // 如果找不到目标对象，可以进行错误处理或记录日志
    console.error(`Unable to find item with key ${keyToUpdate}`);
  }
};


const onCloseDrawer = () => {
  openForm.value = false;
  // form.inCount = ref(parseInt(form.inCount) + parseInt(form.singleCount));
  // form.totalCount = ref(parseInt(form.totalCount) + parseInt(form.singleCount));
  // updateDataSource(form.key, form.inCount, form.totalCount);
  // console.log(form.totalCount);
  // store.dispatch('saveBoyuHistory',[form]);
  // console.log(form.singleCount);
  // console.log(store.getters.getBoyuHistory);
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
