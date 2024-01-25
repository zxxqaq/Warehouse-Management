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
  </div>
</template>
<script lang="ts" setup>
import {computed, reactive} from 'vue';
import { useStore } from "vuex";
import router from "../router";

const store = useStore();
interface FormState {
  userName: string;
  password: string;
}

const disabled = computed(() => {
  return !(formState.userName && formState.password);
})
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

const submitForm = async () => {
  try {
    const response = await fetch('http://localhost:7779/login', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(formState),
    });

    const data = await response.json();
    console.log(data.data);
    if (data.code === 200) {
      store.commit('setAuthentication', true);
      store.commit('setToken', data.data);
      await router.push('/home');
    } else {
      console.error('Login failed:', data.message);
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
  background-image: url('src/assets/img/bg.png');
  background-size: cover;
}
</style>

