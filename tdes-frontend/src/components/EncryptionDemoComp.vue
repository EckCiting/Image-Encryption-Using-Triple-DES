<template>
  <v-form>
    <v-container>
      <v-row>
        <v-col
            cols="12"
        >
          <v-text-field
              :value="salt"
              label="salt"
          ></v-text-field>
        </v-col>
      </v-row>
      <v-row>
        <v-col
            cols="6"
        >
          <v-text-field
              :value="key1"
              label="Key 1"
          ></v-text-field>
        </v-col>

        <v-col
            cols="6"
        >
          <v-text-field
              :value="stage1"
              label="Stage 1"
          ></v-text-field>
        </v-col>
      </v-row>
      <v-row>
        <v-col
            cols="6"
        >
          <v-text-field
              :value="key2"
              label="Key 2"
          ></v-text-field>
        </v-col>

        <v-col
            cols="6"
        >
          <v-text-field
              :value="stage2"
              label="Stage 2"
          ></v-text-field>
        </v-col>
      </v-row>
      <v-row>
        <v-col
            cols="6"
        >
          <v-text-field
              :value="key3"
              label="Key 3"
          ></v-text-field>
        </v-col>

        <v-col
            cols="6"
        >
          <v-text-field
              :value="stage3"
              label="Stage 3"
          ></v-text-field>
        </v-col>
      </v-row>
    </v-container>
  </v-form>

</template>

<script>
export default {
  name: "EncryptionDemoComp",
  props:['salt','key1','key2','key3','stage1','stage2','stage3'],
  data() {
    return {

    }
  },
  methods: {

  }
}
</script>

<style scoped>

</style>