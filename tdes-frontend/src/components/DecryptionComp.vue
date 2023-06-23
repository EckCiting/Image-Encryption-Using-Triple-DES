<template>
  <div style="width: 450px;">
    <h1  style="margin-bottom: 20px">Decryption</h1>
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

    <v-btn
        depressed
        style="border:#cccccc solid 1px; color:#777777; width:100px; margin: 20px"
        @click="decryption"
        :disabled="imgFile === ''"
    >
      upload
    </v-btn
    >
    <v-btn
        depressed
        style="border:#cccccc solid 1px; color:#777777; width:100px; margin: 20px"
        @click="decryptionDemo"
        :disabled="imgFile === ''"
    >
      check
    </v-btn
    >
    <DecryptionDemoComp :salt="salt" :key1="key1" :key2="key2" :key3="key3" :stage1="stage1" :stage2="stage2"
                        :stage3="stage3"/>
  </div>
</template>

<script>
import DecryptionDemoComp from "@/components/DecryptionDemoComp";

export default {
  name: "DecryptionComp",
  components: {DecryptionDemoComp},
  data() {
    return {
      imgFile: "",
      menu: false,
      menu2: false,
      password: "",
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
    async decryption() {
      if (this.imgFile === "") {
        alert("null file")
      }
      let originalFile = new FormData();
      originalFile.append("image", this.imgFile);
      originalFile.append("password", this.password);

      await this.$axios({
        method: "post",
        url: "https://tdes.chnnhc.com/api/decryptimage",
        data: originalFile,
        responseType: 'blob'
      }).then((res) => {
        this.decryptionDemo()

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
    async decryptionDemo() {
      if (this.imgFile === "") {
        alert("null file")
      }
      let originalFile = new FormData();
      originalFile.append("image", this.imgFile);
      await this.$axios({
        method: "post",
        url: "https://tdes.chnnhc.com/api/decryptimagedemo",
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