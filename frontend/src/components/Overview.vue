<template>
  <a-layout>

    <a-layout-content style="margin: 0 16px">
      <a-breadcrumb style="margin: 16px 0">
        <a-breadcrumb-item></a-breadcrumb-item>
        <a-breadcrumb-item></a-breadcrumb-item>
      </a-breadcrumb>

      <div :style="{ padding: '24px', background: '#fff', minHeight: '360px' }">
        <a-button class="editable-add-btn" style="margin-bottom: 10px" @click="showDrawer">
          <template #icon><PlusOutlined /></template>
          添加公司
        </a-button>

        <a-table :pagination="pagination" bordered :data-source="dataSource" :columns="columns">
          <template #emptyText>
            <a-skeleton active v-if="isLoading" />
            <div v-else>
              <InboxOutlined style="font-size: xxx-large" />
              <p style="margin-top: 10px; margin-bottom: 0">没有数据，请先选择公司名称</p>
            </div>
          </template>

          <template #bodyCell="{ column, text, record }" >
            <template v-if="column.dataIndex === 'companyName' ||
                            column.dataIndex === 'taxNum'">
              <div class="editable-cell">
                <div v-if="editableData[record.companyId]" class="editable-cell-input-wrapper">
                  <a-input v-model:value="editableData[record.companyId][column.dataIndex]" @pressEnter="save(record.companyId)" />
                </div>
                <div v-else class="editable-cell-text-wrapper">
                  {{ text || ' ' }}
                </div>
              </div>
            </template>

            <template v-else-if="column.dataIndex === 'operation'">
              <div class="editable-row-operations">
                <span v-if="editableData[record.companyId]">
                  <a-typography-link  @click="save(record.companyId)">保存</a-typography-link>
                  <a-typography-link style="margin-left: 10px" @click="cancel(record.companyId)">取消</a-typography-link>
                </span>
                <span v-else>
                  <a @click="edit(record.companyId)">编辑</a>
                  <a style="margin-left: 10px" @click="handleCompanyClick(record.companyId)">详情</a>
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
        @close="onCloseCompany"
    >
      <a-form :model="companyForm" :rules="rules" layout="vertical">
        <a-row :gutter="16">
          <a-col :span="24">
            <a-form-item label="名称" name="companyName">
              <a-input v-model:value="companyForm.companyName"  />
            </a-form-item>
          </a-col>
        </a-row>
        <a-row :gutter="16">
          <a-col :span="24">
            <a-form-item label="税号（如果暂时没有，请填写0）" name="taxNum">
              <a-input v-model:value="companyForm.taxNum"  />
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
      <template #extra>
        <a-space>
          <a-button @click="onCloseCompany">取消</a-button>
          <a-button :disabled="disabled" type="primary" @click="onSubmitCompany">确定</a-button>
        </a-space>
      </template>
    </a-drawer>
  </a-layout>

</template>
<script lang="ts" setup>
import {InboxOutlined, PlusOutlined} from '@ant-design/icons-vue';
import type { Rule } from 'ant-design-vue/es/form';
import {computed, onBeforeUnmount, onMounted, reactive, ref} from 'vue';
import type { Ref, UnwrapRef } from 'vue';
import { CheckOutlined, EditOutlined } from '@ant-design/icons-vue';
import {before, cloneDeep} from 'lodash-es';
import {TableColumnsType} from "ant-design-vue";
import { useStore} from "vuex";
import { message } from 'ant-design-vue';
const store = useStore();

const pagination = ref({
  defaultPageSize: 50,
  hideOnSinglePage: false,
})
const isLoading = ref<boolean>(false);
const companyForm = reactive({
  companyName: null,
  taxNum: null,
});

const rules: Record<string, Rule[]> = {
  // name: [{ required: true, message: '' }],
};
const open = ref<boolean>(false);

const showDrawer = () => {
  open.value = true;
};

const onCloseCompany = () => {
  open.value = false;
};

const disabled = computed(() => {
  return !(companyForm.companyName && companyForm.taxNum);
})
const onSubmitCompany = async () => {
  try {
    const response = await fetch('http://localhost:7779/overview/addCompany',{
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'token' : store.getters.getToken,
      },
      body: JSON.stringify(companyForm),
    });
    const data = await response.json();
    if (data.code === 200){
      message.success('添加公司成功');
      await fetchData();
      companyForm.companyName = null;
      companyForm.taxNum = null;
      onCloseCompany();
    }else {
      message.error('添加公司失败');
    }
  }catch (error){
    console.error('An error occurred during add new company:',error);
  }
};


const handleCompanyClick = (companyId: string) => {
  store.commit('setSelectedMenuItem', "menu1");
  store.commit('setSelectedCompany', companyId);
}

interface Company {
  companyId: number;
  companyName: string;
  taxNum: string;
}


const columns: TableColumnsType = [
  { title: '名称', dataIndex: 'companyName', fixed: 'left', width: '40%',},
  { title: '税号', dataIndex: 'taxNum', width: '40%',},
  { title: '操作', dataIndex: 'operation', fixed: "right",},
];
const dataSource: Ref<Company[]> = ref([]);
const count = computed(() => dataSource.value.length + 1);
const editableData: UnwrapRef<Record<string, Company>> = reactive({});


const fetchData = async () => {
  try {
    console.log(store.getters.getToken)
    isLoading.value = true;
    const response = await fetch('http://localhost:7779/overview/companyList',{
      method: 'GET',
      headers: {
        'token' : store.getters.getToken,
      }
    });
    const data = await response.json();
    if (data.code === 200){
      dataSource.value = data.data;
      isLoading.value = false;
      store.commit('setCompanyList',dataSource.value);
    } else {
      console.log('Failed to fetch data:', data.message);
    }
  } catch (error) {
    console.error('An error occurred during fetch:', error);
  }
};

const edit = (companyId: number) => {
  editableData[companyId] = cloneDeep(dataSource.value.filter(item => companyId === item.companyId)[0]);
};
const save = async (companyId: string) => {
  try {
    const response = await fetch('http://localhost:7779/overview/updateCompany', {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        'token' : store.getters.getToken,
      },
      body:JSON.stringify(editableData[companyId])
    })
    const data = await response.json();
    if (data.code === 200){
      message.success('编辑公司信息成功');
      Object.assign(dataSource.value.filter(item => companyId === item.companyId)[0], editableData[companyId]);
      delete editableData[companyId];
    }else {
      message.error('编辑公司信息失败');
    }
  } catch (error) {
    console.error('An error occurred in saving edited company:', error);
  }
};
const cancel = (companyId: string) => {
  delete editableData[companyId];
};


onMounted(() => {
  fetchData();
})
onBeforeUnmount(() => {
  store.commit('setCompanyList',dataSource.value)
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
:where(.css-dev-only-do-not-override-1qb1s0s).ant-table-wrapper .ant-table-pagination.ant-pagination {
  margin: 16px 0 0 0;
}
</style>
