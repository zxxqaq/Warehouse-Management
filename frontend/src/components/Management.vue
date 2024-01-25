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


        <a-table :pagination="pagination" bordered :data-source="dataSource" :columns="columns" :scroll="{x: 1800, y: 600}">
          <template #emptyText>
            <a-skeleton active v-if="isLoading" />
            <div v-else>
              <InboxOutlined style="font-size: xxx-large" />
              <p style="margin-top: 10px; margin-bottom: 0">暂无数据</p>
            </div>
          </template>

          <template #bodyCell="{ column, text, record }" >
            <template v-if="column.dataIndex === 'operation'">
              <a v-if="showLink"  @click="showInputDrawer(record.itemId)">入库</a>
              <a v-if="showLink" style="margin-left: 10px" @click="showOutputDrawer(record.itemId)">出库</a>
              <a style="margin-left: 10px" @click="checkItemHistory(record.itemId)">历史</a>
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
              <a-input v-model:value="initializeForm.amount" placeholder="请输入整数" style="width: 100%" />
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="16">
          <a-col :span="24">
            <a-alert message="数量用于初始化库存，如果只是新建一个零件，数量请输入0" type="warning" show-icon />
          </a-col>
        </a-row>
      </a-form>
      <template #extra>
        <a-space>
          <a-button @click="onCloseInitializeDrawer">取消</a-button>
          <a-button :disabled="submitInitializeDisabled" type="primary" @click="onSubmitInitializeForm">提交</a-button>
        </a-space>
      </template>
    </a-drawer>
    <a-drawer
        title="入库"
        :width="720"
        :open="openInputForm"
        :body-style="{ paddingBottom: '80px' }"
        :footer-style="{ textAlign: 'right' }"
        @close="onCloseInputDrawer"
    >
      <a-form :model="inputForm" :rules="rules" layout="vertical">
        <a-row :gutter="16">
          <a-col :span="6">
            <a-form-item label="名称" name="itemName" >
              <a-input disabled v-model:value="inputForm.itemName" :placeholder="inputForm.itemName"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item label="标准" name="standard" >
              <a-input disabled v-model:value="inputForm.standard" :placeholder="inputForm.standard"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item label="规格" name="specification" >
              <a-input disabled v-model:value="inputForm.specification" :placeholder="inputForm.specification"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item label="表面处理" name="surface" >
              <a-input disabled v-model:value="inputForm.surface" :placeholder="inputForm.surface"></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="16">
          <a-col :span="6">
            <a-form-item label="材质" name="material" >
              <a-input disabled v-model:value="inputForm.material" :placeholder="inputForm.material"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item label="等级" name="level" >
              <a-input disabled v-model:value="inputForm.level" :placeholder="inputForm.level"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item label="单重" name="unitWeight" >
              <a-input disabled v-model:value="inputForm.unitWeight" :placeholder="inputForm.unitWeight"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item label="单位" name="unit" >
              <a-input disabled v-model:value="inputForm.unit" :placeholder="inputForm.unit"></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-item label="时间" name="date" >
              <a-space direction="vertical" :size="12">
                <a-config-provider :locale="zhCN">
                  <a-date-picker style="width: 20.5rem" v-model:value="inputForm.date" />
                </a-config-provider>
              </a-space>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="单价" name="unitPrice">
              <a-input v-model:value="inputForm.unitPrice" prefix="￥" style="width: 100%" />
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-item label="重量（kg）" name="totalWeight">
              <a-input v-model:value="inputForm.totalWeight" style="width: 100%" />
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
      <template #extra>
        <a-space>
          <a-button @click="onCloseInputDrawer">取消</a-button>
          <a-button :disabled="submitDisabled" type="primary" @click="onSubmitInputDrawer">提交</a-button>
        </a-space>
      </template>
    </a-drawer>
    <a-drawer
        title="出库"
        :width="720"
        :open="openOutputForm"
        :body-style="{ paddingBottom: '80px' }"
        :footer-style="{ textAlign: 'right' }"
        @close="onCloseOutputDrawer"
    >
      <a-form :model="outputForm" :rules="rules" layout="vertical">
        <a-row :gutter="16">
          <a-col :span="6">
            <a-form-item label="名称" name="itemName" >
              <a-input disabled v-model:value="outputForm.itemName" :placeholder="outputForm.itemName"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item label="标准" name="standard" >
              <a-input disabled v-model:value="outputForm.standard" :placeholder="outputForm.standard"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item label="规格" name="specification" >
              <a-input disabled v-model:value="outputForm.specification" :placeholder="outputForm.specification"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item label="表面处理" name="surface" >
              <a-input disabled v-model:value="outputForm.surface" :placeholder="outputForm.surface"></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="16">
          <a-col :span="6">
            <a-form-item label="材质" name="material" >
              <a-input disabled v-model:value="outputForm.material" :placeholder="outputForm.material"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item label="等级" name="level" >
              <a-input disabled v-model:value="outputForm.level" :placeholder="outputForm.level"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item label="单重" name="unitWeight" >
              <a-input disabled v-model:value="outputForm.unitWeight" :placeholder="outputForm.unitWeight"></a-input>
            </a-form-item>
          </a-col>
          <a-col :span="6">
            <a-form-item label="单位" name="unit" >
              <a-input disabled v-model:value="outputForm.unit" :placeholder="outputForm.unit"></a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-item label="时间" name="date" >
              <a-space direction="vertical" :size="12">
                <a-config-provider :locale="zhCN">
                  <a-date-picker style="width: 20.5rem" v-model:value="outputForm.date" />
                </a-config-provider>
              </a-space>
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="数量" name="amount">
              <a-input v-model:value="outputForm.amount"  style="width: 100%" />
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-item label="出库方向" name="direction">
              <a-input v-model:value="outputForm.direction" style="width: 100%" />
            </a-form-item>
          </a-col>
        </a-row>


      </a-form>
      <template #extra>
        <a-space>
          <a-button @click="onCloseOutputDrawer">取消</a-button>
          <a-button :disabled="submitOutputDisabled" type="primary" @click="onSubmitOutputDrawer">提交</a-button>
        </a-space>
      </template>
    </a-drawer>

  </a-layout>
</template>
<script lang="ts" setup>
import type {Ref, UnwrapRef} from 'vue';
import {computed, onBeforeUnmount, onMounted, reactive, ref, watch} from 'vue';
import {message, SelectProps, TableColumnsType} from "ant-design-vue";
import {FallOutlined, InboxOutlined, MoneyCollectOutlined, PlusOutlined} from '@ant-design/icons-vue';
import type {Rule} from 'ant-design-vue/es/form';
import {useStore} from "vuex";
import dayjs from 'dayjs';
import 'dayjs/locale/zh-cn';
import zhCN from "ant-design-vue/es/locale/zh_CN";
import {paginationConfig} from "ant-design-vue/es/pagination";
import Footer from "./Footer.vue";

const isLoading = ref<boolean>(false);
const showLink = ref<boolean>(false);
const pagination = ref({
  defaultPageSize: 50,
  hideOnSinglePage: false,
})
const fetchCompanyList = async () => {
  try {
    const response = await fetch('http://localhost:7779/overview/companyList',{
      method: 'GET',
      headers: {
        'token' : store.getters.getToken,
      }
    });
    const data = await response.json();
    if (data.code === 200){
      store.commit('setCompanyList',data.data);
    } else {
      console.log('Failed to fetch data:', data.message);
    }
  } catch (error) {
    console.error('An error occurred during fetch:', error);
  }
};
const store = useStore();
let companyList = store.getters.getCompanyList;
const options = ref<SelectProps['options']>([
  { value: '0', label: '总库存' }, // 额外的选项
  ...companyList.map(item => ({
    value: String(item.companyId),
    label: item.companyName,
  }))
]);


let defaultSelectCompany = ref('总库存');
const companyId = ref();
watch(companyId,(newValue) => {
  showLink.value = newValue !== 0;
})


onMounted( async () => {
  if (companyList.length === 0) {
    await fetchCompanyList();
    companyList = store.getters.getCompanyList;
    options.value = companyList.map(item => ({
      value: String(item.companyId),
      label: item.companyName,
    }));
  }
  companyId.value = store.getters.getSelectedCompany
  if (companyId.value !== null) {
    for (const company of companyList) {
      if (company.companyId === companyId.value) {
        defaultSelectCompany.value = company.companyName;
      }
    }
    handleCompanyChange(companyId.value)
  }
  store.commit('setSelectedCompany',0);
})


const handleCompanyChange  = (id: number) => {
  console.log(id);
  companyId.value = id;
  fetchData(companyId.value);
}

const fetchData = async (id: number) => {
  try {
    isLoading.value = true;
    let response;
    if (id == 0){
      response = await fetch(`http://localhost:7779/management`,{
        headers: {
          'token' : store.getters.getToken,
        }
      });
    }else {
      response = await fetch(`http://localhost:7779/management/${id}`,{
        headers: {
          'token' : store.getters.getToken,
        }
      });
    }
    const data = await response.json();
    if (data.code === 200){
      dataSource.value = data.data
      isLoading.value = false;
    } else {
      console.log('Failed to fetch data:', data.message);
    }
  } catch (error) {
    console.error('An error occurred during fetch:', error);
  }
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
  initialCount: number;
  totalCount: number;
}

const columns: TableColumnsType = [
  { title: '名称', dataIndex: 'itemName', fixed: 'left',},
  { title: '标准', dataIndex: 'standard', fixed: 'left'},
  { title: '规格', dataIndex: 'specification', width: 100},
  { title: '表面处理', dataIndex: 'surface'},
  { title: '材质', dataIndex: 'material', width: 100},
  { title: '等级', dataIndex: 'level', width: 100},
  { title: '单重', dataIndex: 'unitWeight', width: 100},
  { title: '单位', dataIndex: 'unit', width: 80},

  { title: '总进库数', dataIndex: 'inCount', width: 100},
  { title: '总出库数', dataIndex: 'outCount', width: 100},
  { title: '初始化数', dataIndex: 'initialCount', width: 100},
  { title: '总库存数', dataIndex: 'totalCount', fixed: "right"},
  { title: '操作', dataIndex: 'operation', fixed: "right",},
];
const dataSource: Ref<ItemSummary[]> = ref([]);





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

const clearInitializeForm = () => {
  initializeForm.companyId = null;
  initializeForm.itemName = null;
  initializeForm.standard = null;
  initializeForm.specification = null;
  initializeForm.surface = null;
  initializeForm.material = null;
  initializeForm.level = null;
  initializeForm.unitWeight = null;
  initializeForm.unit = null;
  initializeForm.amount = null;
  initializeForm.date = null;
};



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
const onSubmitInitializeForm = async () => {
  onCloseInitializeDrawer();
  message.loading({
    content: () => '加载中',
    duration: 0,
    key: 0,
  })
  initializeForm.companyId = companyId.value;
  try {
    const response = await fetch('http://localhost:7779/management/initializeItem',{
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'token' : store.getters.getToken,
      },
      body: JSON.stringify(initializeForm),
    });
    const data = await response.json();
    message.destroy(0);
    if (data.code === 200){
      message.success('初始化/新建成功');
      await fetchData(companyId.value);
    }else if (data.code === 501){
      message.error('初始化/新建失败，零件已存在');
    }
    else {
      message.error('初始化/新建失败,请重试');
    }
    clearInitializeForm();
  }catch (error){
    console.error('An error occurred when submit initialize form:', error)
  }
}

const checkItemHistory = (itemId: number) =>{
  store.commit('setItemId',itemId);
  store.commit('setSelectedCompany', companyId.value);
  console.log('company');
  console.log(companyId.value);
  store.commit('setSelectedMenuItem', 'menu2');
}



// -------上面已检查---------
const inputForm = reactive({
  companyId: null,
  date: null,
  unitPrice: null,
  totalWeight: null,

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
const outputForm = reactive({
  companyId: null,
  date: null,
  direction: null,
  amount: null,

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
const clearOutputForm = () => {
  Object.keys(outputForm).forEach(key => {
    outputForm[key] = null;
  });
}
const clearInputForm = () => {
  Object.keys(inputForm).forEach(key => {
    inputForm[key] = null;
  });
}
const inputSubmitForm = reactive({
  companyId: null,
  itemId: null,
  totalWeight: null,
  unitPrice: null,
  date: null,
})
const outputSubmitForm = reactive({
  companyId: null,
  itemId: null,
  amount: null,
  direction: null,
  date: null,
})
const clearOutputSubmitForm = () => {
  Object.keys(outputSubmitForm).forEach(key => {
    outputSubmitForm[key] = null;
  });
}
const clearInputSubmitForm = () => {
  Object.keys(inputSubmitForm).forEach(key => {
    inputSubmitForm[key] = null;
  });
}
const openInputForm = ref<boolean>(false);
const openOutputForm = ref<boolean>(false);
const formSource: Ref<ItemSummary[]> = ref([]);
const showInputDrawer = (itemId: number) => {
  formSource.value = dataSource.value.filter(item => item.itemId === itemId);
  const firstItem = formSource.value[0];
  for (const key in firstItem) {
    if (Object.prototype.hasOwnProperty.call(firstItem, key)) {
      inputForm[key] = firstItem[key];
      inputForm["companyId"] = companyId.value;
    }
  }
  openInputForm.value = true;
};
const showOutputDrawer = (itemId: number) => {
  formSource.value = dataSource.value.filter(item => item.itemId === itemId);
  const firstItem = formSource.value[0];
  for (const key in firstItem) {
    if (Object.prototype.hasOwnProperty.call(firstItem, key)) {
      outputForm[key] = firstItem[key];
      outputForm["companyId"] = companyId.value;
    }
  }
  openOutputForm.value = true;
};
const submitOutputDisabled = computed(() => {
  return !(outputForm.date && outputForm.amount && outputForm.direction);
})
const submitDisabled = computed(() => {
  return !(inputForm.date && inputForm.unitPrice && inputForm.totalWeight);
})
dayjs.locale('zh-cn')
const onSubmitInputDrawer = async () => {
  Object.assign(inputSubmitForm, inputForm);
  onCloseInputDrawer();
  message.loading({
    content: () => '加载中',
    duration: 0,
    key: 0,
  })
  try {
    const response = await fetch('http://localhost:7779/management/inputItem', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'token' : store.getters.getToken,
      },
      body: JSON.stringify(inputSubmitForm)
    })
    const data = await response.json();
    message.destroy(0);
    if (data.code === 200) {
      await fetchData(companyId.value);
      message.success('入库成功');
    } else {
      message.error('入库失败，请重试');
    }
    //清空两个表
    clearInputForm();
    clearInputSubmitForm();
  } catch (error) {
    console.error('An error occurred in saving edited company:', error);
  }
}
const onSubmitOutputDrawer = async () => {
  Object.assign(outputSubmitForm, outputForm);
  onCloseOutputDrawer();
  message.loading({
    content: () => '加载中',
    duration: 0,
    key: 0,
  })
  try {
    const response = await fetch('http://localhost:7779/management/outputItem', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'token' : store.getters.getToken,
      },
      body: JSON.stringify(outputSubmitForm)
    })
    const data = await response.json();
    message.destroy(0);
    if (data.code === 200) {
      await fetchData(companyId.value);
      message.success('出库成功');
    } else {
      message.error('出库失败，请重试');
    }
    //清空两个表
    clearOutputForm();
    clearOutputSubmitForm();
  } catch (error) {
    console.error('An error occurred in saving edited company:', error);
  }
}
const onCloseInputDrawer = () => {
  openInputForm.value = false;
};
const onCloseOutputDrawer = () => {
  openOutputForm.value = false;
};
const count = computed(() => dataSource.value.length + 1);
const editableData: UnwrapRef<Record<string, ItemSummary>> = reactive({});



const rules: Record<string, Rule[]> = {
  singleCount: [{ required: true, message: '' }],
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
:where(.css-dev-only-do-not-override-1qb1s0s).ant-table-wrapper .ant-table-pagination.ant-pagination {
  margin: 16px 0 0 0;
}
</style>
