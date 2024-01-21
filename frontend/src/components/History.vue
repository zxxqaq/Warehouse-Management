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



        <a-table bordered :data-source="dataSource" :columns="columns" :scroll="{x: 1500, y: 500}">
          <template #emptyText>
            <a-skeleton active v-if="isLoading" />
            <div v-else>
              <InboxOutlined style="font-size: xxx-large" />
              <p style="margin-top: 10px; margin-bottom: 0">暂无数据</p>
            </div>
          </template>

          <template #bodyCell="{ column, text, record }" >
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
                <span v-if="editableData[record.recordId]">
                  <a-typography-link  @click="saveDeletion(record.recordId)">保存</a-typography-link>
                  <a-typography-link style="margin-left: 10px" @click="cancelDeletion(record.recordId)">取消</a-typography-link>
                </span>
                <span v-else>
                  <a @click="deleteRecord(record.recordId)">删除</a>
                </span>
              </div>
            </template>
          </template>
        </a-table>
      </div>
    </a-layout-content>


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

const isLoading = ref<boolean>(false);
const store = useStore();
const editableData: UnwrapRef<Record<string, Records>> = reactive({});
const fetchCompanyList = async () => {
  try {
    const response = await fetch('http://localhost:7779/overview/companyList');
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
  { title: '出库方向', dataIndex: 'direction',width: 100},
  { title: '名称', dataIndex: 'itemName',width: 110 },
  { title: '标准', dataIndex: 'standard', },
  { title: '规格', dataIndex: 'specification', width: 100},
  { title: '表面处理', dataIndex: 'surface', width: 100},
  { title: '材质', dataIndex: 'material', width: 80},
  { title: '等级', dataIndex: 'level', width: 80},
  { title: '单重', dataIndex: 'unitWeight', width: 80},
  { title: '单位', dataIndex: 'unit', width: 80},
  { title: '单价', dataIndex: 'unitPrice', width: 80},
  { title: '重量', dataIndex: 'totalWeight', width: 80},
  { title: '操作人', dataIndex: 'userName', width: 80},
  { title: '数量', dataIndex: 'amount', width: 80,fixed:'right'},
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
  unitPrice: number
}
const dataSource: Ref<Records[]> = ref([]);
const fetchItemData = async (companyId: number, itemId:number) => {
  try {
    isLoading.value = true;
    const response = await fetch(`http://localhost:7779/historyRecord/${companyId}/${itemId}`);
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
    const response = await fetch(`http://localhost:7779/historyRecord/${companyId}`);
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

const deleteRecord = (recordId: number) => {
  editableData[recordId] = cloneDeep(dataSource.value.filter(item => recordId === item.recordId)[0]);
};
const cancelDeletion = (recordId: number) => {
  delete editableData[recordId];
};

const saveDeletion = async (recordId: number) => {
  try {
    const response = await fetch(`http://localhost:7779/historyRecord/${recordId}`, {
      method: 'DELETE',
      headers: {
        'Content-Type': 'application/json',
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
</style>
