<template>
  <div style="max-width: 1000px">
    <v-row>
      <v-col>
        <EncryptionComp/>
      </v-col>
      <v-col>
        <DecryptionComp/>
      </v-col>

    </v-row>
  </div>
</template>

<script>
import EncryptionComp from "@/components/EncryptionComp";
import DecryptionComp from "@/components/DecryptionComp";

export default {
  name: "indexPage",
  components: {DecryptionComp, EncryptionComp},
}
</script>

<style scoped>

</style>