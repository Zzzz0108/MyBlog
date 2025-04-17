module.exports = {
  devServer: {
    port: 8080,         // 前端端口
    proxy: {
      '/api': {         // 拦截所有以 /api 开头的请求
        target: 'http://localhost:9090', // 后端地址
        changeOrigin: true,              // 允许跨域
      }
    }
  }
}