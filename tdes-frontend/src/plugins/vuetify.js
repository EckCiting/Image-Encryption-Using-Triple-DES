import Vue from "vue";
import Vuetify from "vuetify";
import colors from "vuetify/lib/util/colors";
import "vuetify/dist/vuetify.min.css";
Vue.use(Vuetify);
export default new Vuetify({
    theme: {
        themes: {
            light: {
                primary: colors.indigo.lighten2
            }
        }
    }
});