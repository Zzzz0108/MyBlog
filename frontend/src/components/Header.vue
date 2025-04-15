<script>
export default {
    name: "Header"
  }
</script>

<template>
  <div style="height: 80px;line-height: 80px;border-bottom: 4px solid #8a2be2;display: flex">
    <div style="width: 200px;text-align: center">
      <router-link to="/"><el-icon size="50" color="#8a2be2" style="vertical-align: middle" ><Fries /></el-icon></router-link>
    </div>
    <div style="flex: 1;text-align: center">
      <router-link to="/"><h1 style="font-size: 40px"><strong>MyBlog</strong></h1></router-link>
    </div>
    <div style="width: 200px;text-align: center">
      <router-link to="/login"><el-button color="#8a2be2" :dark="isDark" plain round size="large">Sign in</el-button></router-link>
    </div>
  </div>
</template>

<style scoped>

</style>