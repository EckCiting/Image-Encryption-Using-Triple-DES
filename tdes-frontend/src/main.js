import Vue from 'vue'
import App from './App.vue'
import Vuetify from "vuetify";
import vuetify from "@/plugins/vuetify";
import axios from "axios";

Vue.config.productionTip = false
Vue.prototype.$axios = axios;
Vue.use(Vuetify);

new Vue({
  render: h => h(App),
  vuetify,
}).$mount('#app')
