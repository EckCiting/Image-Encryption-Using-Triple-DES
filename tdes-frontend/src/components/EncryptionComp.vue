<template>
  <div style="width: 450px;margin-left: 30px">
    <h1 style="margin-bottom: 20px">Encryption</h1>
    <v-file-input
        outlined
        v-model="imgFile"
        color="#f7b249"
        accept="image/* "
        prepend-icon=""
        :rules="[rules.required]"
    >
    </v-file-input>
    <v-text-field
        outlined
        ref="password"
        v-model="password"
        :rules="[rules.required]"
        label="Password"
        placeholder="Please enter your password"
        color="primary"
        type="password"
    ></v-text-field>
    <!-- Date and Time-->
    <v-row>
      <v-col
          cols="6"
      >
        <!--  Date picker  -->

        <v-menu
            v-model="menu"
            :close-on-content-click="false"
            :nudge-right="40"
            transition="scale-transition"
            offset-y
            min-width="auto"
        >
          <template v-slot:activator="{ on, attrs }">
            <v-text-field
                v-model="date"
                label="Pick the expire time"
                prepend-icon=""
                readonly
                v-bind="attrs"
                v-on="on"
                :rules="[rules.required]"
            ></v-text-field>
          </template>
          <v-date-picker
              v-model="date"
              @input="menu = false"
          ></v-date-picker>
        </v-menu>
      </v-col>
      <!-- Time picker   -->
      <v-col
          cols="6"
      >
        <v-menu
            ref="menu2"
            v-model="menu2"
            :close-on-content-click="false"
            :nudge-right="40"
            :return-value.sync="time"
            transition="scale-transition"
            offset-y
            max-width="290px"
            min-width="290px"
        >
          <template v-slot:activator="{ on, attrs }">
            <v-text-field
                v-model="time"
                label="Pick the expire time"
                prepend-icon=""
                readonly
                v-bind="attrs"
                v-on="on"
                :rules="[rules.required]"
            ></v-text-field>
          </template>
          <v-time-picker
              v-if="menu2"
              v-model="time"
              full-width
              format="24hr"
              @click:minute="$refs.menu2.save(time)"
          ></v-time-picker>
        </v-menu>
      </v-col>
    </v-row>
    <v-btn
        depressed
        style="border:#cccccc solid 1px; color:#777777; width:100px; margin: 20px"
        @click="encryption"
        :disabled="imgFile === ''"
    >
      upload
    </v-btn
    >
    <v-btn
        depressed
        style="border:#cccccc solid 1px; color:#777777; width:100px; margin: 20px"
        @click="encryptionDemo"
        :disabled="imgFile === ''"
    >
      check
    </v-btn
    >
    <EncryptionDemoComp :salt="salt" :key1="key1" :key2="key2" :key3="key3" :stage1="stage1" :stage2="stage2"
                        :stage3="stage3"/>
  </div>
</template>

<script>
import EncryptionDemoComp from "@/components/EncryptionDemoComp";

export default {
  name: "EncryptionComp",
  components: {EncryptionDemoComp},
  data() {
    return {
      imgFile: "",
      date: null,
      time: null,
      menu: false,
      menu2: false,
      password: "",
      dateTime:"",
      rules: {
        required: (value) => !!value || "This field is required."
      },
      salt: "",
      key1: "",
      key2: "",
      key3: "",
      stage1: "",
      stage2: "",
      stage3: ""
    };
  },
  methods: {
    async encryption() {
      this.dateTime = this.date + "T" +  this.time
      if (this.imgFile === "") {
        alert("null file")
      }
      let originalFile = new FormData();
      originalFile.append("image", this.imgFile);
      originalFile.append("password", this.password);
      originalFile.append("expireDate", this.dateTime);

      await this.$axios({
        method: "post",
        url: "https://tdes.chnnhc.com/api/encryptimage",
        data: originalFile,
        responseType: 'blob'
      }).then((res) => {
        this.encryptionDemo()

        const url = window.URL.createObjectURL(new Blob([res.data]));
        const link = document.createElement('a');
        let headerLine = res.headers['content-disposition'];
        let filenameRegex = /filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/;
        let matches = filenameRegex.exec(headerLine);
        var filename = "";

        if (matches != null && matches[1]) {
          filename = matches[1].replace(/['"]/g, '');
        }
        link.href = url;
        link.setAttribute('download', filename); //or any other extension
        document.body.appendChild(link);
        link.click();
      })

    },
    async encryptionDemo() {
      if (this.imgFile === "") {
        alert("null file")
      }
      let originalFile = new FormData();
      originalFile.append("image", this.imgFile);
      await this.$axios({
        method: "post",
        url: "https://tdes.chnnhc.com/api/encryptimagedemo",
        data: originalFile,
      }).then((res) => {
        this.salt = res.data.salt;
        this.key1 = res.data.key1;
        this.key2 = res.data.key2;
        this.key3 = res.data.key3;
        this.stage1 = res.data.stage1;
        this.stage2 = res.data.stage2;
        this.stage3 = res.data.stage3;
      })
    }

  }
}
</script>

<style scoped>
</style>