<template>
  <a-layout>
    <a-layout-content style="margin: 0 16px">
      <a-breadcrumb style="margin: 16px 0">
        <a-breadcrumb-item></a-breadcrumb-item>
        <a-breadcrumb-item></a-breadcrumb-item>
      </a-breadcrumb>

      <div :style="{ padding: '24px', background: '#fff', minHeight: '360px' }">

        <a-button class="editable-add-btn" style="margin-bottom: 10px" @click="fetchData(0)">
          总历史
        </a-button>

        <a-button class="editable-add-btn" style="margin-left: 10px" @click="convertXLSX">
          excel导出
        </a-button>



        <a-table  :pagination="pagination" bordered :data-source="dataSource" :columns="columns" :scroll="{x: 2500, y: 600}">
          <template #emptyText>
            <a-skeleton active v-if="isLoading" />
            <div v-else>
              <InboxOutlined style="font-size: xxx-large" />
              <p style="margin-top: 10px; margin-bottom: 0">暂无数据</p>
            </div>
          </template>

          <template
              #customFilterDropdown="{ setSelectedKeys, selectedKeys, confirm, clearFilters, column }"
          >
            <div style="padding: 8px">
              <a-input
                  ref="searchInput"
                  :placeholder="`请输入搜索内容`"
                  :value="selectedKeys[0]"
                  style="width: 188px; margin-bottom: 8px; display: block"
                  @change="e => setSelectedKeys(e.target.value ? [e.target.value] : [])"
                  @pressEnter="handleSearch(selectedKeys, confirm, column.dataIndex)"
              />
              <a-button
                  type="primary"
                  size="small"
                  style="width: 90px; margin-right: 8px"
                  @click="handleSearch(selectedKeys, confirm, column.dataIndex)"
              >
                <template #icon><SearchOutlined /></template>
                搜索
              </a-button>
              <a-button size="small" style="width: 90px" @click="handleReset(clearFilters)">
                重置
              </a-button>
            </div>
          </template>

          <template #customFilterIcon="{ filtered }">
            <SearchOutlined :style="{ color: filtered ? '#108ee9' : undefined }" />
          </template>

          <template #bodyCell="{ column, text, record }" >
            <span v-if="state.searchText && state.searchedColumn === column.dataIndex">
              <template
                  v-for="(fragment, i) in text
                  .toString()
                  .split(new RegExp(`(?<=${state.searchText})|(?=${state.searchText})`, 'i'))"
              >
                <mark
                    v-if="fragment.toLowerCase() === state.searchText.toLowerCase()"
                    :key="i"
                    class="highlight"
                >
                  {{ fragment }}
                </mark>
                <template v-else>{{ fragment }}</template>
              </template>
            </span>


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
import {InboxOutlined, SearchOutlined} from '@ant-design/icons-vue';
import {useStore} from "vuex";
import 'dayjs/locale/zh-cn';
import type { Ref } from 'vue';
import {cloneDeep} from "lodash-es";
import zhCN from "ant-design-vue/es/locale/zh_CN";
import dayjs, { Dayjs } from 'dayjs';
import Footer from "./Footer.vue";
import * as XLSX from 'xlsx';

const convertXLSX = () => {

  let data = dataSource.value;
  data = data.map(item => {
    return {
      "类型": item.type,
      "时间": item.date,
      "已发票": item.isCheck,
      "出库方向": item.direction,
      "名称": item.itemName,
      "标准": item.standard,
      "规格": item.specification,
      "表面处理": item.surface,
      "材质": item.material,
      "等级": item.level,
      "单重": item.unitWeight,
      "单位": item.unit,
      "单价": item.unitPrice,
      "重量": item.totalWeight,
      "操作人": item.userName,
      "入库公司": item.companyName,
      "数量": item.amount,
    };
  })

  // 创建一个工作簿
  const workbook: XLSX.WorkBook = XLSX.utils.book_new();
  // 将数据转换为工作表
  const worksheet: XLSX.WorkSheet = XLSX.utils.json_to_sheet(data);

  // 将工作表添加到工作簿中
  XLSX.utils.book_append_sheet(workbook, worksheet, 'Sheet1');

  // 导出工作簿为 Excel 文件
  XLSX.writeFile(workbook, '操作历史.xlsx');
  message.success("导出成功")
}

const state = reactive({
  searchText: '',
  searchedColumn: '',
});
const searchInput = ref();
const handleSearch = (selectedKeys, confirm, dataIndex) => {
  confirm();
  state.searchText = selectedKeys[0];
  state.searchedColumn = dataIndex;
};
const handleReset = clearFilters => {
  clearFilters({ confirm: true });
  state.searchText = '';
};
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
  companyId.value = store.getters.getSelectedCompany
  let msg;

  if (itemId.value !== null) {
    msg = '现在显示的是您刚刚点击的零件，入库/出库的历史记录。点击关闭这条提示信息'
    await fetchItemData(0, itemId.value);
    message.warning({
      content: () => msg,
      duration: 0,
      key: 1,
      onClick: e => {
        message.destroy(1)
      }
    });
  }else {
    // TODO 获取所有的操作历史
    fetchData(0);
  }

  if ( companyId.value !== 0 ){
    for (const company of companyList) {
      if (companyId.value == company.companyId) {
        defaultSelectCompany.value = company.companyName;
      }
    }
    msg = '现在显示的是该公司的入库记录。点击关闭这条提示信息'
    await fetchData(companyId.value);
    message.warning({
      content: () => msg,
      duration: 0,
      key: 1,
      onClick: e => {
        message.destroy(1)
      }
    });
  }



  // 清空全局中的item和company变量
  store.commit('setItemId', null);
  store.commit('setSelectedCompany', 0);
})


const handleCompanyChange  = (id: number) => {
  message.destroy(1);
  companyId.value = id;
  fetchData(id);
}



const columns: any[] = [
  { title: '类型', dataIndex: 'type',width: 80, fixed: 'left',
    filters: [
      {
        text: '入库',
        value: '入库',
      },
      {
        text: '出库',
        value: '出库',
      },
    ],
    onFilter: (value: string, record: Records) => record.type.indexOf(value) === 0,
    sortDirections: ['descend'],
  },
  { title: '时间', dataIndex: 'date', fixed: 'left',width: 120,
    sorter: (a: Records, b: Records) => {
     const dateA = new Date(a.date);
     const dateB = new Date(b.date);
     return dateA - dateB;
    },
  },
  { title: '已发票', dataIndex: 'isCheck',width: 100,
    filters: [
      {
        text: '是',
        value: true,
      },
      {
        text: '否',
        value: false,
      },
    ],
    onFilter: (value: boolean, record: Records) => record.isCheck == value,
    sortDirections: ['descend'],
  },
  { title: '出库方向', dataIndex: 'direction',
    customFilterDropdown: true,
    onFilter: (value, record) =>  record.direction.toString().toLowerCase().includes(value.toLowerCase()),
    onFilterDropdownOpenChange: visible => {
      if (visible) {
        setTimeout(() => {
          searchInput.value.focus();
        }, 100);
      }
    },
  },
  { title: '名称', dataIndex: 'itemName',
    customFilterDropdown: true,
    onFilter: (value, record) =>  record.itemName.toString().toLowerCase().includes(value.toLowerCase()),
    onFilterDropdownOpenChange: visible => {
      if (visible) {
        setTimeout(() => {
          searchInput.value.focus();
        }, 100);
      }
    },
  },
  { title: '标准', dataIndex: 'standard',
    customFilterDropdown: true,
    onFilter: (value, record) =>  record.standard.toString().toLowerCase().includes(value.toLowerCase()),
    onFilterDropdownOpenChange: visible => {
      if (visible) {
        setTimeout(() => {
          searchInput.value.focus();
        }, 100);
      }
    },
  },
  { title: '规格', dataIndex: 'specification', width: 100,
    customFilterDropdown: true,
    onFilter: (value, record) =>  record.specification.toString().toLowerCase().includes(value.toLowerCase()),
    onFilterDropdownOpenChange: visible => {
      if (visible) {
        setTimeout(() => {
          searchInput.value.focus();
        }, 100);
      }
    },
  },
  { title: '表面处理', dataIndex: 'surface', width: 150,
    customFilterDropdown: true,
    onFilter: (value, record) =>  record.surface.toString().toLowerCase().includes(value.toLowerCase()),
    onFilterDropdownOpenChange: visible => {
      if (visible) {
        setTimeout(() => {
          searchInput.value.focus();
        }, 100);
      }
    },
  },
  { title: '材质', dataIndex: 'material', width: 100,
    customFilterDropdown: true,
    onFilter: (value, record) =>  record.material.toString().toLowerCase().includes(value.toLowerCase()),
    onFilterDropdownOpenChange: visible => {
      if (visible) {
        setTimeout(() => {
          searchInput.value.focus();
        }, 100);
      }
    },
  },
  { title: '等级', dataIndex: 'level', width: 150,
    customFilterDropdown: true,
    onFilter: (value, record) =>  record.level.toString().toLowerCase().includes(value.toLowerCase()),
    onFilterDropdownOpenChange: visible => {
      if (visible) {
        setTimeout(() => {
          searchInput.value.focus();
        }, 100);
      }
    },
  },
  { title: '单重', dataIndex: 'unitWeight', width: 80,
    customFilterDropdown: true,
    onFilter: (value, record) =>  record.unitWeight.toString().toLowerCase().includes(value.toLowerCase()),
    onFilterDropdownOpenChange: visible => {
      if (visible) {
        setTimeout(() => {
          searchInput.value.focus();
        }, 100);
      }
    },
  },
  { title: '单位', dataIndex: 'unit', width: 70},
  { title: '单价', dataIndex: 'unitPrice', width: 100,
    customFilterDropdown: true,
    onFilter: (value, record) =>  record.unitPrice.toString().toLowerCase().includes(value.toLowerCase()),
    onFilterDropdownOpenChange: visible => {
      if (visible) {
        setTimeout(() => {
          searchInput.value.focus();
        }, 100);
      }
    },
  },
  { title: '重量', dataIndex: 'totalWeight', width: 100,
    customFilterDropdown: true,
    onFilter: (value, record) =>  record.totalWeight.toString().toLowerCase().includes(value.toLowerCase()),
    onFilterDropdownOpenChange: visible => {
      if (visible) {
        setTimeout(() => {
          searchInput.value.focus();
        }, 100);
      }
    },
  },
  { title: '操作人', dataIndex: 'userName', width: 100,
    customFilterDropdown: true,
    onFilter: (value, record) =>  record.userName.toString().toLowerCase().includes(value.toLowerCase()),
    onFilterDropdownOpenChange: visible => {
      if (visible) {
        setTimeout(() => {
          searchInput.value.focus();
        }, 100);
      }
    },
  },
  { title: '入库公司', dataIndex: 'companyName',
    customFilterDropdown: true,
    onFilter: (value, record) =>  record.companyName.toString().toLowerCase().includes(value.toLowerCase()),
    onFilterDropdownOpenChange: visible => {
      if (visible) {
        setTimeout(() => {
          searchInput.value.focus();
        }, 100);
      }
    },
  },
  { title: '数量', dataIndex: 'amount',width: 150, fixed:'right'},
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
const dataSource = ref([]);
const fetchItemData = async (companyId: number, itemId:number) => {
  try {
    isLoading.value = true;
    let response;
    if (companyId == 0){
      response = await fetch(`http://localhost:7779/historyRecord/itemId/${itemId}`,{
        headers: {
          'token' : store.getters.getToken,
        }
      });
    }else {
      response = await fetch(`http://localhost:7779/historyRecord/${companyId}/${itemId}`,{
        headers: {
          'token' : store.getters.getToken,
        }
      });
    }

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
    let response;
    if (companyId == 0){
      response = await fetch(`http://localhost:7779/historyRecord`,{
        headers: {
          'token' : store.getters.getToken,
        }
      });
    }else {
      response = await fetch(`http://localhost:7779/historyRecord/companyId/${companyId}`,{
        headers: {
          'token' : store.getters.getToken,
        }
      });
    }

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

        if (item.isCheck === false){
          item.isCheck = null;
        }else if (item.isCheck === true){
          item.isCheck = '是';
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
