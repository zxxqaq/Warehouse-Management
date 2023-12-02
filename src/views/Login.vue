<template>
  <div class="login-form">
    <a-card title="用户登录" style="width: 500px">
      <template #extra><a href="#">帮助</a></template>
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
            name="username"
            :rules="[{ required: true, message: '请输入用户名!' }]"
        >
          <a-input v-model:value="formState.username" />
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
  </div>
</template>
<script lang="ts" setup>
import {computed, reactive} from 'vue';
import { useStore } from "vuex";
import router from "../router";

const store = useStore();
interface FormState {
  username: string;
  password: string;
}

const disabled = computed(() => {
  return !(formState.username && formState.password);
})
const formState = reactive<FormState>({
  username: '',
  password: '',
});
const onFinish = (values: any) => {
  console.log('Success:', values);
};

const onFinishFailed = (errorInfo: any) => {
  console.log('Failed:', errorInfo);
};

const submitForm = async () => {
  try {
    const response = await fetch('http://localhost:8080/login', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(formState),
    });

    if (response.ok) {
      // 登录成功，处理后端返回的数据
      const data = await response.json();
      const userFullName = data.userFullName;
      store.commit('setAuthentication', true);
      store.commit('setUsername', userFullName);


      // 存储 auth token，例如使用 Vuex 或其他状态管理工具
      // 这里只是示例，具体实现取决于你的应用架构
      // store.commit('setAuthToken', authToken);

      // 跳转到 Home.vue 或其他成功登录后的页面
      await router.push('/home');
    } else {
      // 登录失败，处理错误信息
      console.error('Login failed:', response.statusText);
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
  background-image: url("src/assets/img/bg.png");
  background-size: cover;
}
</style>

