import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import Components from 'unplugin-vue-components/vite';
import { AntDesignVueResolver } from 'unplugin-vue-components/resolvers';

// https://vitejs.dev/config/
export default defineConfig({
  server: {
    host: '0.0.0.0', //ip地址，0.0.0.0的时候为允许来自局域网的电脑访问
    port: 8080, //端口号
    open: true, //启动后是否自动打开浏览器
    strictPort: true, // 是否严格为设置的端口，如果为false，则在设置的端口被占用的时候会自己找别的端口
  },
  plugins: [
    vue(),
    Components({
      resolvers: [
        AntDesignVueResolver({
          importStyle: false, // css in js
        }),
      ],
    }),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  }
})
