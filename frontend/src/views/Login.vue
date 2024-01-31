<template>
  <div class="login-form">
    <a-card title="用户登录" style="width: 500px">
      <template #extra><a @click="showModal">注册</a></template>
      <a-form
          :model="formState"
          name="basic"
          :label-col="{ span: 8 }"
          :wrapper-col="{ span: 16 }"
          autocomplete="off"
          @finish="onFinish"
          @finishFailed="onFinishFailed"
      >
        <a-form-item
            label="用户名"
            name="userName"
            :rules="[{ required: true, message: '请输入用户名!' }]"
        >
          <a-input v-model:value="formState.userName" />
        </a-form-item>

        <a-form-item
            label="密码"
            name="password"
            :rules="[{ required: true, message: '请输入密码!' }]"
        >
          <a-input-password v-model:value="formState.password" />
        </a-form-item>

        <a-form-item :wrapper-col="{ offset: 8, span: 16 }">
          <a-button :disabled="disabled" type="primary" html-type="submit" @click="submitForm">登录</a-button>
        </a-form-item>
      </a-form>
    </a-card>

      <a-modal v-model:open="openModal" title="注册用户" centered @ok="register">
        <a-form
            :model="registerForm"
            name="basic"
            :label-col="{ span: 8 }"
            :wrapper-col="{ span: 16 }"
            autocomplete="off"
        >
          <a-form-item
              label="用户名"
              name="userName"
              :rules="[{ required: true, message: '请输入用户名!' }]"
          >
            <a-input v-model:value="registerForm.userName" />
          </a-form-item>

          <a-form-item
              label="密码"
              name="password"
              :rules="[{ required: true, message: '请输入密码!' }]"
          >
            <a-input-password v-model:value="registerForm.password" />
          </a-form-item>
        </a-form>
      </a-modal>


  </div>
</template>
<script lang="ts" setup>
import {computed, reactive, ref} from 'vue';
import { useStore } from "vuex";
import router from "../router";
import {message} from "ant-design-vue";
import zhCN from "ant-design-vue/es/locale/zh_CN";


const openModal = ref<boolean>(false);
const showModal = () => {
  openModal.value = true;
};
const store = useStore();
interface FormState {
  userName: string;
  password: string;
}


const disabled = computed(() => {
  return !(formState.userName && formState.password);
})


const registerForm = reactive<FormState>({
  userName: null,
  password: null,
});
const formState = reactive<FormState>({
  userName: '',
  password: '',
});
const onFinish = (values: any) => {
  console.log('Success:', values);
};

const onFinishFailed = (errorInfo: any) => {
  console.log('Failed:', errorInfo);
};
const register = async () => {
  if (registerForm.password !== null && registerForm.userName !== null){
    message.loading({
      content: () => '注册中',
      duration: 0,
      key: 0,
    })
    try {
      const response = await fetch('http://192.168.2.43:7779/register', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(registerForm),
      });
      const data = await response.json();
      console.log(data.data);
      message.destroy(0);
      if (data.code === 200) {
        openModal.value = false;
        message.success('注册成功')
      } else {
        console.error('Register failed:', data.message);
        message.error('注册失败，请重试')
      }
    } catch (error) {
      console.error('An error occurred during register:', error);
    }
  }else {
    message.warning('请输入用户名和密码完成注册')
  }
}
const submitForm = async () => {
  message.loading({
    content: () => '登录中',
    duration: 0,
    key: 0,
  })
  try {
    const response = await fetch('http://192.168.2.43:7779/login', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(formState),
    });
    const data = await response.json();
    console.log(data.data);
    message.destroy(0);
    if (data.code === 200) {
      store.commit('setAuthentication', true);
      store.commit('setToken', data.data);
      await router.push('/home');
      message.success('登录成功')
    } else {
      console.error('Login failed:', data.message);
      message.error('登录失败，请重试')
    }
  } catch (error) {
    console.error('An error occurred during login:', error);
  }
};
</script>

<style scoped>
.login-form{
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100vh;
  background-image: url('/bg.png');
  background-size: cover;
}
</style>

