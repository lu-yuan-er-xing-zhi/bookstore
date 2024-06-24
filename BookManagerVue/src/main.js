/*
 * @Description: 
 * @Author: 
 * @Date: 2024-02-21 18:13:21
 */
import Vue from 'vue'

import 'normalize.css/normalize.css' // A modern alternative to CSS resets

import ElementUI from 'element-ui'
import "@/api/initialize"
import 'element-ui/lib/theme-chalk/index.css'

import '@/styles/index.scss' // global css

import App from './App'
import store from './store'
import './assets/font2/iconfont.css'
import router from './router'

import '@/icons' // icon
import './assets/font/iconfont.css'
import '@/permission' // permission control
/**
 * If you don't want to use mock-server
 * you want to use MockJs for mock api
 * you can execute: mockXHR()
 *
 * Currently MockJs will be used in the production environment,
 * please remove it before going online ! ! !
 */
if (process.env.NODE_ENV === 'production') {
  const { mockXHR } = require('../mock')
  mockXHR()
}

Vue.use(ElementUI)

Vue.config.productionTip = false

new Vue({
  el: '#app',
  router,
  mounted() {
  },
  store,
  render: h => h(App)
})
