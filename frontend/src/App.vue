<template>
  <nav>
    <Header/>
  </nav>
  <router-view/>
</template>

<style>

</style>

<script>
import Header from "@/components/Header.vue";

export default {
  name: 'NavBar',
  components: {
    Header
  }
}
</script>
