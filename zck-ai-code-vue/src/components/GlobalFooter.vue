<template>
  <a-layout-footer class="global-footer">
    <div class="footer-content">
      <p class="copyright">
        开发人员赵承康 QQ邮箱：<a href="mailto:2493188169@qq.com">2493188169@qq.com</a>
      </p>
      <p class="project-info">
        项目开发时间：2026-01-21 | 技术栈：Vue 3 + TypeScript + Ant Design Vue
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
  line-height: 1.6;
}

.footer-content .copyright {
  margin-bottom: 8px;
}

.footer-content .project-info {
  font-size: 12px;
  color: rgba(255, 255, 255, 0.45);
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
