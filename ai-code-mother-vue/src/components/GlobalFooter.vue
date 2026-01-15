<template>
  <a-layout-footer class="global-footer">
    <div class="footer-content">
      <p>
        编程导航原创项目 by 
        <a href="https://www.codefather.cn" target="_blank" rel="noopener noreferrer">
          程序员鱼皮
        </a>
      </p>
    </div>
  </a-layout-footer>
</template>

<script setup lang="ts">
// GlobalFooter 组件
</script>

<style scoped>
.global-footer {
  background: #001529;
  color: rgba(255, 255, 255, 0.65);
  padding: 20px 0;
  position: relative;
  width: 100%;
}

.footer-content {
  width: 100%;
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 24px;
  text-align: center;
}

.footer-content p {
  margin: 0;
  font-size: 14px;
}

.footer-content a {
  color: #1890ff;
  text-decoration: none;
}

.footer-content a:hover {
  text-decoration: underline;
}

@media (max-width: 768px) {
  .footer-content {
    padding: 0 16px;
  }
}
</style>