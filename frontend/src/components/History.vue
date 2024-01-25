<template>
  <a-layout>
    <a-layout-content style="margin: 0 16px">
      <a-breadcrumb style="margin: 16px 0">
        <a-breadcrumb-item></a-breadcrumb-item>
        <a-breadcrumb-item></a-breadcrumb-item>
      </a-breadcrumb>

      <div :style="{ padding: '24px', background: '#fff', minHeight: '360px' }">
        <a-space direction="horizontal" style="margin-bottom: 10px">
          <a-select
              v-model:value="defaultSelectCompany"
              size="middle"
              style="width: 250px"
              :options="options"
              @select="handleCompanyChange"
          ></a-select>
        </a-space>



        <a-table  :pagination="pagination" bordered :data-source="dataSource" :columns="columns" :scroll="{x: 2000, y: 600}">
          <template #emptyText>
            <a-skeleton active v-if="isLoading" />
            <div v-else>
              <InboxOutlined style="font-size: xxx-large" />
              <p style="margin-top: 10px; margin-bottom: 0">暂无数据</p>
            </div>
          </template>

          <template #bodyCell="{ column, text, record }" >
            <template v-if="column.dataIndex === 'isCheck'">
              {{record.isCheck ? '是' : null}}
            </template>


            <template v-if="column.dataIndex === 'type'">
              <span>
                <a-tag
                    :color="getTagColor(text)"
                >
                  {{ text }}
                </a-tag>
              </span>
            </template>

            <template v-else-if="column.dataIndex === 'operation'">
              <div class="editable-row-operations">

                  <a @click="editRecord(record.recordId)">编辑</a>

                  <a-config-provider :locale="zhCN">
                    <a-popconfirm
                        v-if="dataSource.length"
                        title="确定删除?"
                        @confirm="saveDeletion(record.recordId)"
                        @cancel="cancelDeletion(record.recordId)"
                    >
                      <a style="margin-left: 10px" @click="deleteRecord(record.recordId)" >删除</a>
                    </a-popconfirm>

                    <a-popconfirm
                        v-if="dataSource.length"
                        title="确定已开发票?"
                        @confirm="saveCheck(record.recordId)"
                        @cancel="cancelCheck(record.recordId)"
                    >
                      <a style="margin-left: 10px" @click="checkRecord(record.recordId)" >发票</a>
                    </a-popconfirm>
                  </a-config-provider>

              </div>
            </template>
          </template>
        </a-table>
      </div>
    </a-layout-content>
    <a-drawer
        title="入库修改"
        :width="720"
        :open="openInputForm"
        :body-style="{ paddingBottom: '80px' }"
        :footer-style="{ textAlign: 'right' }"
        @close="onCloseInputDrawer"
    >
      <a-form :model="inputForm"  layout="vertical">
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
            <a-form-item label="时间" name="date">
              <a-input v-model:value="inputForm.date"  style="width: 100%" />
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
          <a-button  type="primary" @click="onSubmitInputDrawer">提交</a-button>
        </a-space>
      </template>
    </a-drawer>
    <a-drawer
        title="出库修改"
        :width="720"
        :open="openOutputForm"
        :body-style="{ paddingBottom: '80px' }"
        :footer-style="{ textAlign: 'right' }"
        @close="onCloseOutputDrawer"
    >
      <a-form :model="outputForm" layout="vertical">
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
            <a-form-item label="时间" name="date">
              <a-input v-model:value="outputForm.date"  style="width: 100%" />
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
          <a-button type="primary" @click="onSubmitOutputDrawer">提交</a-button>
        </a-space>
      </template>
    </a-drawer>


  </a-layout>
</template>
<script lang="ts" setup>
import {onBeforeUnmount, onMounted, reactive, ref, UnwrapRef} from 'vue';
import {message, SelectProps, TableColumnsType} from "ant-design-vue";
import {InboxOutlined} from '@ant-design/icons-vue';
import {useStore} from "vuex";
import 'dayjs/locale/zh-cn';
import type { Ref } from 'vue';
import {cloneDeep} from "lodash-es";
import zhCN from "ant-design-vue/es/locale/zh_CN";
import dayjs, { Dayjs } from 'dayjs';
import Footer from "./Footer.vue";

const openInputForm = ref<boolean>(false);
const openOutputForm = ref<boolean>(false);
const inputForm = reactive({
  date: null,
  unitPrice: null,
  totalWeight: null,

  recordId: null,
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
  date: null,
  direction: null,
  amount: null,

  recordId: null,
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
const formSource: Ref<Records[]> = ref([]);
const showInputDrawer = (recordId: number) => {
  formSource.value = dataSource.value.filter(item => item.recordId === recordId);
  const firstItem = formSource.value[0];
  for (const key in firstItem) {
    if (Object.prototype.hasOwnProperty.call(firstItem, key)) {
      inputForm[key] = firstItem[key];
    }
  }
  openInputForm.value = true;
};
const showOutputDrawer = (recordId: number) => {
  formSource.value = dataSource.value.filter(item => item.recordId === recordId);
  const firstItem = formSource.value[0];
  for (const key in firstItem) {
    if (Object.prototype.hasOwnProperty.call(firstItem, key)) {
      outputForm[key] = firstItem[key];
    }
  }
  openOutputForm.value = true;
};
const inputSubmitForm = reactive({
  recordId: null,
  totalWeight: null,
  unitPrice: null,
  date: null,
})
const outputSubmitForm = reactive({
  recordId: null,
  amount: null,
  direction: null,
  date: null,
})
const clearInputForm = () => {
  Object.keys(inputForm).forEach(key => {
    inputForm[key] = null;
  });
}
const clearInputSubmitForm = () => {
  Object.keys(inputSubmitForm).forEach(key => {
    inputSubmitForm[key] = null;
  });
}
const clearOutputSubmitForm = () => {
  Object.keys(outputSubmitForm).forEach(key => {
    outputSubmitForm[key] = null;
  });
}
const onCloseOutputDrawer = () => {
  openOutputForm.value = false;
};
const onSubmitOutputDrawer = async () => {
  Object.assign(outputSubmitForm, outputForm);
  onCloseOutputDrawer();
  message.loading({
    content: () => '加载中',
    duration: 0,
    key: 0,
  })
  try {
    const response = await fetch('http://localhost:7779/historyRecord/updateRecord', {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        'token' : store.getters.getToken,
      },
      body: JSON.stringify(outputSubmitForm)
    })
    const data = await response.json();
    message.destroy(0);
    if (data.code === 200) {
      message.success('修改历史成功');
      await fetchData(companyId.value);
    } else {
      message.error('修改历史失败，请重试');
    }
    //清空两个表
    clearOutputForm();
    clearOutputSubmitForm();
  } catch (error) {
    console.error('An error occurred in saving edited company:', error);
  }
}
const onSubmitInputDrawer = async () => {
  Object.assign(inputSubmitForm, inputForm);
  onCloseInputDrawer();
  message.loading({
    content: () => '加载中',
    duration: 0,
    key: 0,
  })
  try {
    const response = await fetch('http://localhost:7779/historyRecord/updateRecord', {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        'token' : store.getters.getToken,
      },
      body: JSON.stringify(inputSubmitForm)
    })
    const data = await response.json();
    message.destroy(0);
    if (data.code === 200) {
      message.success('修改历史成功');
      await fetchData(companyId.value);
    } else {
      message.error('修改历史失败，请重试');
    }
    //清空两个表
    clearInputForm();
    clearInputSubmitForm();
  } catch (error) {
    console.error('An error occurred in saving edited company:', error);
  }
}

const onCloseInputDrawer = () => {
  openInputForm.value = false;
};

const editRecord = (recordId: number) => {
  const record = dataSource.value.find(item => item.recordId === recordId);
  if (record) {
    if (record.type === '入库'){
      showInputDrawer(recordId);
    }else if (record.type === '出库') {
      showOutputDrawer(recordId);
    }
  }
}
const pagination = ref({
  defaultPageSize: 50,
  hideOnSinglePage: false,
})
const isLoading = ref<boolean>(false);
const store = useStore();
const editableData: UnwrapRef<Record<string, Records>> = reactive({});
const fetchCompanyList = async () => {
  try {
    const response = await fetch('http://localhost:7779/overview/companyList',{
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
let defaultSelectCompany = ref('选择公司')
let companyList = store.getters.getCompanyList;
const options = ref<SelectProps['options']>(companyList.map(item => ({
  value: String(item.companyId),
  label: item.companyName,
})));

const companyId = ref();
const itemId = ref();
onMounted( async () => {
  if (companyList.length === 0) {
    await fetchCompanyList();
    companyList = store.getters.getCompanyList;
    options.value = companyList.map(item => ({
      value: String(item.companyId),
      label: item.companyName,
    }));
  }
  itemId.value = store.getters.getItemId;
  if (itemId.value !== null) {
    companyId.value = store.getters.getSelectedCompany
    for (const company of companyList) {
      if (companyId.value == company.companyId) {
        defaultSelectCompany.value = company.companyName;
      }
    }
    message.warning({
      content: () => '现在显示的是您刚刚点击的零件，在该公司的历史记录。点击关闭这条提示信息',
      duration: 0,
      key: 1,
      onClick: e => {
        message.destroy(1)
      }
    });
    await fetchItemData(companyId.value, itemId.value);
  }
  //清空全局中的item和company变量
  store.commit('setItemId', null);
  store.commit('setSelectedCompany', null);
})


const handleCompanyChange  = (id: number) => {
  message.destroy(1);
  companyId.value = id;
  fetchData(id);
}

const columns: TableColumnsType = [
  { title: '类型', dataIndex: 'type',width: 80, fixed: 'left',},
  { title: '时间', dataIndex: 'date', fixed: 'left',width: 120},
  { title: '已发票', dataIndex: 'isCheck',width: 100},
  { title: '出库方向', dataIndex: 'direction'},
  { title: '名称', dataIndex: 'itemName', },
  { title: '标准', dataIndex: 'standard', },
  { title: '规格', dataIndex: 'specification', width: 100},
  { title: '表面处理', dataIndex: 'surface', },
  { title: '材质', dataIndex: 'material', width: 100},
  { title: '等级', dataIndex: 'level', },
  { title: '单重', dataIndex: 'unitWeight', width: 80},
  { title: '单位', dataIndex: 'unit', width: 70},
  { title: '单价', dataIndex: 'unitPrice', width: 100},
  { title: '重量', dataIndex: 'totalWeight', width: 100},
  { title: '操作人', dataIndex: 'userName', width: 100},
  { title: '数量', dataIndex: 'amount',fixed:'right'},
  { title: '操作', dataIndex: 'operation', fixed: "right",},
];

interface Records {
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
  unitPrice: number,
  companyName: string
  isCheck: boolean,
}
const dataSource: Ref<Records[]> = ref([]);
const fetchItemData = async (companyId: number, itemId:number) => {
  try {
    isLoading.value = true;
    const response = await fetch(`http://localhost:7779/historyRecord/${companyId}/${itemId}`,{
      headers: {
        'token' : store.getters.getToken,
      }
    });
    const data = await response.json();
    if (data.code === 200){
      dataSource.value = data.data.map(item => {
        if (item.type === 'Initialization') {
          item.type = '新建';
        }else if (item.type === 'Output') {
          item.type = '出库';
        }else if (item.type === 'Input') {
          item.type = '入库';
        }
        // 检查 totalWeight 和 unitPrice 的值是否为 0.0，如果是则设置为 null
        if (item.totalWeight === 0.0) {
          item.totalWeight = null;
        }
        if (item.unitPrice === 0.0) {
          item.unitPrice = null;
        }
        return item;
      });
      isLoading.value = false;
    } else {
      console.log('Failed to fetch data:', data.message);
    }
  } catch (error) {
    console.error('An error occurred during fetch:', error);
  }
}
const fetchData = async (companyId: number) => {
  try {
    isLoading.value = true;
    const response = await fetch(`http://localhost:7779/historyRecord/companyId/${companyId}`,{
      headers: {
        'token' : store.getters.getToken,
      }
    });
    const data = await response.json();
    if (data.code === 200){
      dataSource.value = data.data.map(item => {
        if (item.type === 'Initialization') {
          item.type = '新建';
        }else if (item.type === 'Output') {
          item.type = '出库';
        }else if (item.type === 'Input') {
          item.type = '入库';
        }
        // 检查 totalWeight 和 unitPrice 的值是否为 0.0，如果是则设置为 null
        if (item.totalWeight === 0.0) {
          item.totalWeight = null;
        }
        if (item.unitPrice === 0.0) {
          item.unitPrice = null;
        }
        return item;
      });
      isLoading.value = false;
    } else {
      console.log('Failed to fetch data:', data.message);
    }
  } catch (error) {
    console.error('An error occurred during fetch:', error);
  }
}
const getTagColor = (text:string) => {
  switch (text){
    case '新建':
      return 'orange';
    case '入库':
      return 'blue';
    case '出库':
      return 'pink';
  }
}
onBeforeUnmount(() => {
  message.destroy(1);
})

const deleteRecord = (recordId: number) => {};

const checkRecord = (recordId: number) => {};
const cancelCheck = (recordId: number) => {};

const saveCheck = async (recordId: number) => {
  const record = dataSource.value.find(item => item.recordId === recordId);
  const recordCopy = cloneDeep(dataSource.value.find(item => item.recordId === recordId));
  if (recordCopy) {
    recordCopy.isCheck = true;
    try {
      const response = await fetch('http://localhost:7779/historyRecord/updateRecord', {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
          'token' : store.getters.getToken,
        },
        body: JSON.stringify(recordCopy)
      })
      const data = await response.json();
      if (data.code === 200) {
        message.success('已开发票');
        record.isCheck = true;
      } else {
        message.error('开发票失败，请重试');
      }
    } catch (error) {
      console.error('An error occurred in saving edited company:', error);
    }
  }
};

const cancelDeletion = (recordId: number) => {};

const saveDeletion = async (recordId: number) => {
  try {
    const response = await fetch(`http://localhost:7779/historyRecord/${recordId}`, {
      method: 'DELETE',
      headers: {
        'Content-Type': 'application/json',
        'token' : store.getters.getToken,
      },
    })
    const data = await response.json();
    if (data.code === 200){
      await fetchData(companyId.value);
      message.success('删除历史成功');
      delete editableData[companyId];
    }else {
      message.error('删除历史失败');
    }
  } catch (error) {
    console.error('An error occurred in saving edited company:', error);
  }
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
