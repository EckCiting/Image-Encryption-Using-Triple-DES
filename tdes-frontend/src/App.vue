<template>
  <v-app>
    <index-page/>
  </v-app>
</template>

<script>
import IndexPage from "@/page/indexPage";

export default {
  name: 'App',
  components: {
    IndexPage
  }
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
