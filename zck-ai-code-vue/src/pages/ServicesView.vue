<template>
  <div class="services-container">
    <!-- 粒子背景 -->
    <div class="particle-background"></div>

    <!-- 神经网络动画 -->
    <div class="neural-network"></div>

    <!-- 英雄区域 -->
    <section class="hero-section">
      <div class="hero-content">
        <h1>我们的服务</h1>
        <p>AI驱动的零代码解决方案，让应用开发变得简单高效</p>
        <!-- 数据流动画 -->
        <div class="data-flow"></div>
      </div>
    </section>

    <!-- 服务概览 -->
    <section class="services-overview">
      <div class="container">
        <h2>服务概览</h2>
        <p class="overview-description">
          我们提供全方位的AI零代码应用开发服务，从需求分析到应用部署，为您打造专业级应用
        </p>
        <div class="services-grid">
          <div class="service-card">
            <!-- 服务卡片发光效果 -->
            <div class="service-card-glow"></div>
            <div class="service-icon">
              <svg xmlns="http://www.w3.org/2000/svg" width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><rect x="2" y="3" width="20" height="14" rx="2" ry="2"></rect><line x1="8" y1="21" x2="16" y2="21"></line><line x1="12" y1="17" x2="12" y2="21"></line></svg>
            </div>
            <h3>零代码应用开发</h3>
            <p>通过AI对话生成应用，无需编程知识，快速构建专业级应用</p>
            <!-- 数据可视化图表 -->
            <div class="data-visualization">
              <div class="chart-bar" v-for="i in 5" :key="i" :style="{ height: Math.random() * 80 + 20 + '%', animationDelay: i * 0.2 + 's' }"></div>
            </div>
          </div>
          <div class="service-card">
            <!-- 服务卡片发光效果 -->
            <div class="service-card-glow"></div>
            <div class="service-icon">
              <svg xmlns="http://www.w3.org/2000/svg" width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><polygon points="12 2 15.09 8.26 22 9.27 17 14.14 18.18 21.02 12 17.77 5.82 21.02 7 14.14 2 9.27 8.91 8.26 12 2"></polygon></svg>
            </div>
            <h3>AI智能设计</h3>
            <p>AI自动生成美观的UI设计，符合现代设计标准和用户体验</p>
            <!-- 数据可视化图表 -->
            <div class="data-visualization">
              <div class="chart-bar" v-for="i in 5" :key="i" :style="{ height: Math.random() * 80 + 20 + '%', animationDelay: i * 0.2 + 's' }"></div>
            </div>
          </div>
          <div class="service-card">
            <!-- 服务卡片发光效果 -->
            <div class="service-card-glow"></div>
            <div class="service-icon">
              <svg xmlns="http://www.w3.org/2000/svg" width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M18 10h-4V4a2 2 0 0 0-2-2h-4a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h4a2 2 0 0 0 2-2v-8"></path><path d="M22 18h-8"></path><path d="M22 10h-8"></path></svg>
            </div>
            <h3>应用部署托管</h3>
            <p>一键部署应用到云端，提供稳定的托管服务和访问链接</p>
            <!-- 数据可视化图表 -->
            <div class="data-visualization">
              <div class="chart-bar" v-for="i in 5" :key="i" :style="{ height: Math.random() * 80 + 20 + '%', animationDelay: i * 0.2 + 's' }"></div>
            </div>
          </div>
          <div class="service-card">
            <!-- 服务卡片发光效果 -->
            <div class="service-card-glow"></div>
            <div class="service-icon">
              <svg xmlns="http://www.w3.org/2000/svg" width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"></path></svg>
            </div>
            <h3>技术支持</h3>
            <p>专业的技术支持团队，为您解决应用开发和部署过程中的问题</p>
            <!-- 数据可视化图表 -->
            <div class="data-visualization">
              <div class="chart-bar" v-for="i in 5" :key="i" :style="{ height: Math.random() * 80 + 20 + '%', animationDelay: i * 0.2 + 's' }"></div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- 服务详情 -->
    <section class="service-details">
      <div class="container">
        <h2>服务详情</h2>

        <!-- 零代码应用开发 -->
        <div class="service-detail-item">
          <div class="service-detail-content">
            <div class="service-detail-text">
              <h3>零代码应用开发</h3>
              <p>我们的核心服务是通过AI对话生成应用，您只需描述您的需求，AI就能理解并生成相应的应用代码。</p>
              <h4>服务特点：</h4>
              <ul>
                <li>通过自然语言描述需求，无需编程知识</li>
                <li>支持多种应用类型：网站、管理系统、工具应用等</li>
                <li>实时生成代码，可视化预览效果</li>
                <li>支持多次迭代优化，直到满意为止</li>
              </ul>
              <h4>应用场景：</h4>
              <p>适合快速原型开发、小型项目构建、企业内部工具、个人创意实现等场景。</p>
            </div>
            <div class="service-detail-image">
              <img src="https://images.unsplash.com/photo-1551288049-bebda4e38f71?ixlib=rb-4.0.3&auto=format&fit=crop&w=1200&q=80" alt="零代码应用开发" />
            </div>
          </div>
        </div>

        <!-- AI智能设计 -->
        <div class="service-detail-item reverse">
          <div class="service-detail-content">
            <div class="service-detail-text">
              <h3>AI智能设计</h3>
              <p>我们的AI设计系统能够根据您的需求和品牌风格，自动生成美观、专业的UI设计。</p>
              <h4>服务特点：</h4>
              <ul>
                <li>AI自动生成符合现代设计标准的UI界面</li>
                <li>支持自定义品牌风格和色彩方案</li>
                <li>响应式设计，适配各种设备尺寸</li>
                <li>提供多种设计风格选择</li>
              </ul>
              <h4>应用场景：</h4>
              <p>适合需要专业设计但缺乏设计资源的项目，如创业公司、个人项目、营销活动页面等。</p>
            </div>
            <div class="service-detail-image">
              <img src="https://images.unsplash.com/photo-1558655146-364adaf1fcc9?ixlib=rb-4.0.3&auto=format&fit=crop&w=1200&q=80" alt="AI智能设计" />
            </div>
          </div>
        </div>

        <!-- 应用部署托管 -->
        <div class="service-detail-item">
          <div class="service-detail-content">
            <div class="service-detail-text">
              <h3>应用部署托管</h3>
              <p>我们提供一键部署和托管服务，让您的应用快速上线并稳定运行。</p>
              <h4>服务特点：</h4>
              <ul>
                <li>一键部署，无需配置服务器</li>
                <li>提供稳定的云端托管服务</li>
                <li>自动生成可访问的应用链接</li>
                <li>支持自定义域名</li>
                <li>提供基础的访问统计</li>
              </ul>
              <h4>应用场景：</h4>
              <p>适合需要快速上线的项目，如营销活动、产品展示、内部工具等。</p>
            </div>
            <div class="service-detail-image">
              <img src="https://images.unsplash.com/photo-1518770660439-4636190af475?ixlib=rb-4.0.3&auto=format&fit=crop&w=1200&q=80" alt="应用部署托管" />
            </div>
          </div>
        </div>

        <!-- 技术支持 -->
        <div class="service-detail-item reverse">
          <div class="service-detail-content">
            <div class="service-detail-text">
              <h3>技术支持</h3>
              <p>我们的专业技术支持团队为您提供全方位的技术支持，确保您的应用顺利开发和运行。</p>
              <h4>服务特点：</h4>
              <ul>
                <li>7x24小时在线技术支持</li>
                <li>快速响应问题，提供解决方案</li>
                <li>定期应用维护和更新</li>
                <li>提供技术咨询和最佳实践建议</li>
              </ul>
              <h4>应用场景：</h4>
              <p>适合对技术支持有较高要求的企业用户，确保应用稳定运行和持续优化。</p>
            </div>
            <div class="service-detail-image">
              <img src="https://images.unsplash.com/photo-1557800636-894a64c1696f?ixlib=rb-4.0.3&auto=format&fit=crop&w=1200&q=80" alt="技术支持" />
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- 服务优势 -->
    <section class="service-advantages">
      <div class="container">
        <h2>我们的优势</h2>
        <div class="advantages-grid">
          <div class="advantage-item">
            <div class="advantage-icon">
              <svg xmlns="http://www.w3.org/2000/svg" width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="10"></circle><polyline points="12 6 12 12 16 14"></polyline></svg>
            </div>
            <h3>快速高效</h3>
            <p>相比传统开发，节省70%以上的开发时间，快速上线应用</p>
          </div>
          <div class="advantage-item">
            <div class="advantage-icon">
              <svg xmlns="http://www.w3.org/2000/svg" width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="10"></circle><path d="M12 16v-4"></path><path d="M12 8h.01"></path></svg>
            </div>
            <h3>成本节约</h3>
            <p>无需专业开发团队，大幅降低开发成本和维护成本</p>
          </div>
          <div class="advantage-item">
            <div class="advantage-icon">
              <svg xmlns="http://www.w3.org/2000/svg" width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"></path><polyline points="22 4 12 14.01 9 11.01"></polyline></svg>
            </div>
            <h3>技术先进</h3>
            <p>基于最新的AI技术和前端框架，确保应用质量和性能</p>
          </div>
          <div class="advantage-item">
            <div class="advantage-icon">
              <svg xmlns="http://www.w3.org/2000/svg" width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M21 16V8a2 2 0 0 0-1-1.73l-7-4a2 2 0 0 0-2 0l-7 4A2 2 0 0 0 3 8v8a2 2 0 0 0 1 1.73l7 4a2 2 0 0 0 2 0l7-4A2 2 0 0 0 21 16z"></path><polyline points="3.27 6.96 12 12.01 20.73 6.96"></polyline><line x1="12" y1="22.08" x2="12" y2="12"></line></svg>
            </div>
            <h3>用户友好</h3>
            <p>直观的操作界面，无需技术背景，任何人都能轻松使用</p>
          </div>
          <div class="advantage-item">
            <div class="advantage-icon">
              <svg xmlns="http://www.w3.org/2000/svg" width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><rect x="3" y="3" width="18" height="18" rx="2" ry="2"></rect><line x1="8" y1="12" x2="16" y2="12"></line><line x1="12" y1="8" x2="12" y2="16"></line></svg>
            </div>
            <h3>灵活定制</h3>
            <p>支持深度定制和个性化需求，满足各种复杂场景</p>
          </div>
          <div class="advantage-item">
            <div class="advantage-icon">
              <svg xmlns="http://www.w3.org/2000/svg" width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"></path></svg>
            </div>
            <h3>专业支持</h3>
            <p>提供专业的技术支持和咨询服务，确保项目成功</p>
          </div>
        </div>
      </div>
    </section>

    <!-- 服务流程 -->
    <section class="service-process">
      <div class="container">
        <h2>服务流程</h2>
        <div class="process-steps">
          <div class="process-step">
            <div class="process-step-number">1</div>
            <h3>需求分析</h3>
            <p>您描述应用需求，我们的AI和专业团队分析并理解您的需求</p>
          </div>
          <div class="process-step">
            <div class="process-step-number">2</div>
            <h3>应用生成</h3>
            <p>AI根据需求生成应用代码和设计，您可以实时预览效果</p>
          </div>
          <div class="process-step">
            <div class="process-step-number">3</div>
            <h3>优化迭代</h3>
            <p>您可以提出修改意见，我们进行优化迭代，直到满意为止</p>
          </div>
          <div class="process-step">
            <div class="process-step-number">4</div>
            <h3>部署上线</h3>
            <p>一键部署应用到云端，生成访问链接，正式上线使用</p>
          </div>
          <div class="process-step">
            <div class="process-step-number">5</div>
            <h3>后续支持</h3>
            <p>提供技术支持和维护服务，确保应用稳定运行</p>
          </div>
        </div>
      </div>
    </section>

    <!-- 右下角互动小人 -->
    <div class="interactive-character">
      👋
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted } from 'vue'

onMounted(() => {
  // 页面加载时的初始化逻辑
  document.title = '服务 - AI零代码应用生成平台'
})
</script>

<style scoped>
.services-container {
  min-height: 100vh;
  background-color: var(--background-page);
  position: relative;
  overflow: hidden;
}

/* 粒子背景 */
.particle-background {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: -1;
  background: radial-gradient(circle at 50% 50%, rgba(59, 130, 246, 0.05) 0%, rgba(139, 92, 246, 0.1) 100%);
  /* 性能优化：减少重绘 */
  will-change: transform;
}

.particle-background::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-image:
    radial-gradient(circle at 20% 30%, rgba(59, 130, 246, 0.1) 0%, transparent 20%),
    radial-gradient(circle at 80% 70%, rgba(139, 92, 246, 0.1) 0%, transparent 20%),
    radial-gradient(circle at 40% 80%, rgba(236, 72, 153, 0.1) 0%, transparent 20%),
    radial-gradient(circle at 60% 20%, rgba(16, 185, 129, 0.1) 0%, transparent 20%);
  animation: particleFloat 20s ease-in-out infinite;
  /* 性能优化：减少重绘 */
  will-change: transform, opacity;
}

/* 响应式设计：在移动设备上调整特效 */
@media (max-width: 768px) {
  .particle-background::before {
    background-image:
      radial-gradient(circle at 20% 30%, rgba(59, 130, 246, 0.08) 0%, transparent 15%),
      radial-gradient(circle at 80% 70%, rgba(139, 92, 246, 0.08) 0%, transparent 15%),
      radial-gradient(circle at 40% 80%, rgba(236, 72, 153, 0.08) 0%, transparent 15%),
      radial-gradient(circle at 60% 20%, rgba(16, 185, 129, 0.08) 0%, transparent 15%);
    animation: particleFloat 15s ease-in-out infinite;
  }

  .neural-network::before {
    background-size: 30px 30px;
    animation: neuralPulse 15s ease-in-out infinite;
  }

  .data-flow {
    width: 150px;
  }

  .hero-content h1 {
    font-size: 2.5rem;
  }

  section {
    padding: 80px 0;
    min-height: 80vh;
  }

  section:not(.hero-section) {
    min-height: 60vh;
  }

  /* 调整服务卡片 */
  .services-grid {
    grid-template-columns: 1fr;
  }

  .service-card {
    padding: 32px 20px;
  }

  /* 调整数据可视化图表 */
  .data-visualization {
    height: 30px;
  }
}

@keyframes particleFloat {
  0%, 100% {
    transform: translateY(0px) rotate(0deg);
  }
  25% {
    transform: translateY(-10px) rotate(1deg);
  }
  50% {
    transform: translateY(0px) rotate(0deg);
  }
  75% {
    transform: translateY(10px) rotate(-1deg);
  }
}

/* 神经网络动画 */
.neural-network {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: -1;
  opacity: 0.3;
}

.neural-network::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background:
    linear-gradient(to right, transparent 99%, rgba(59, 130, 246, 0.3) 100%),
    linear-gradient(to bottom, transparent 99%, rgba(139, 92, 246, 0.3) 100%);
  background-size: 50px 50px;
  animation: neuralPulse 10s ease-in-out infinite;
}

@keyframes neuralPulse {
  0%, 100% {
    opacity: 0.2;
    transform: scale(1);
  }
  50% {
    opacity: 0.4;
    transform: scale(1.02);
  }
}

/* 数据流动画 */
.data-flow {
  position: absolute;
  bottom: 20px;
  left: 50%;
  transform: translateX(-50%);
  width: 200px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: space-around;
}

.data-flow::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 2px;
  background: linear-gradient(90deg, transparent, rgba(59, 130, 246, 0.8), transparent);
  animation: dataFlow 2s linear infinite;
}

.data-flow::after {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 2px;
  background: linear-gradient(90deg, transparent, rgba(139, 92, 246, 0.8), transparent);
  animation: dataFlow 2s linear infinite 1s;
}

@keyframes dataFlow {
  0% {
    transform: translateX(-100%);
  }
  100% {
    transform: translateX(100%);
  }
}

/* 英雄区域 */
.hero-section {
  background: linear-gradient(135deg, var(--primary-color) 0%, var(--primary-hover) 100%);
  color: white;
  padding: 120px 0;
  min-height: 60vh;
  display: flex;
  align-items: center;
  text-align: center;
  position: relative;
  overflow: hidden;
}

.hero-section::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: radial-gradient(circle at 50% 50%, rgba(255, 255, 255, 0.1) 0%, transparent 70%);
  animation: heroPulse 4s ease-in-out infinite;
}

@keyframes heroPulse {
  0%, 100% {
    transform: scale(1);
    opacity: 0.5;
  }
  50% {
    transform: scale(1.1);
    opacity: 0.8;
  }
}

.hero-content {
  position: relative;
  z-index: 1;
}

.hero-content h1 {
  font-size: 3rem;
  font-weight: var(--font-weight-bold);
  margin-bottom: 16px;
}

.hero-content p {
  font-size: 1.25rem;
  opacity: 0.9;
}

/* 通用容器样式 */
.container {
  width: 100%;
  max-width: var(--max-container-width);
  margin: 0 auto;
  padding: 0 var(--spacing-lg);
}

/* 通用部分样式 */
section {
  padding: 120px 0;
  min-height: 100vh;
  display: flex;
  align-items: center;
  width: 100%;
}

section:not(.hero-section) {
  min-height: 80vh;
}

section h2 {
  font-size: 2.5rem;
  font-weight: var(--font-weight-bold);
  text-align: center;
  margin-bottom: 64px;
  color: var(--text-primary);
}

/* 服务概览 */
.services-overview {
  background-color: white;
}

.overview-description {
  text-align: center;
  font-size: 1.125rem;
  max-width: 800px;
  margin: 0 auto 64px;
  color: var(--text-secondary);
  line-height: 1.8;
}

.services-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 32px;
}

.service-card {
  background-color: var(--background-light);
  border-radius: var(--border-radius-lg);
  padding: 40px 24px;
  text-align: center;
  box-shadow: var(--shadow-md);
  transition: transform var(--transition-normal), box-shadow var(--transition-normal);
  position: relative;
  overflow: hidden;
  border: 1px solid rgba(59, 130, 246, 0.2);
}

/* 服务卡片发光效果 */
.service-card-glow {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(135deg, rgba(59, 130, 246, 0.1) 0%, rgba(139, 92, 246, 0.1) 100%);
  border-radius: var(--border-radius-lg);
  opacity: 0;
  transition: opacity var(--transition-normal);
  z-index: 0;
}

.service-card:hover .service-card-glow {
  opacity: 1;
  animation: cardGlow 2s ease-in-out infinite;
}

@keyframes cardGlow {
  0%, 100% {
    transform: scale(1);
    opacity: 0.5;
  }
  50% {
    transform: scale(1.05);
    opacity: 0.8;
  }
}

.service-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 20px 25px -5px rgba(59, 130, 246, 0.1), 0 10px 10px -5px rgba(139, 92, 246, 0.04);
  border-color: rgba(59, 130, 246, 0.4);
}

.service-icon {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  background-color: rgba(59, 130, 246, 0.1);
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 24px;
  color: var(--primary-color);
  position: relative;
  z-index: 1;
  transition: background-color var(--transition-normal), transform var(--transition-normal);
}

.service-card:hover .service-icon {
  background-color: rgba(59, 130, 246, 0.2);
  transform: scale(1.05);
  box-shadow: 0 0 20px rgba(59, 130, 246, 0.4);
  animation: iconPulse 2s ease-in-out infinite;
}

@keyframes iconPulse {
  0%, 100% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.1);
  }
}

.service-card h3 {
  font-size: 1.25rem;
  font-weight: var(--font-weight-bold);
  margin-bottom: 16px;
  color: var(--text-primary);
  position: relative;
  z-index: 1;
  transition: color var(--transition-normal);
}

.service-card:hover h3 {
  color: var(--primary-color);
  text-shadow: 0 0 10px rgba(59, 130, 246, 0.4);
}

.service-card p {
  font-size: 1rem;
  color: var(--text-secondary);
  line-height: 1.6;
  position: relative;
  z-index: 1;
}

/* 数据可视化图表 */
.data-visualization {
  display: flex;
  align-items: flex-end;
  gap: 4px;
  height: 40px;
  margin-top: 24px;
  position: relative;
  z-index: 1;
}

.chart-bar {
  flex: 1;
  background: linear-gradient(to top, rgba(59, 130, 246, 0.8), rgba(139, 92, 246, 0.8));
  border-radius: 2px 2px 0 0;
  animation: chartBarAnimation 2s ease-out infinite;
  opacity: 0.7;
}

@keyframes chartBarAnimation {
  0%, 100% {
    transform: scaleY(1);
  }
  50% {
    transform: scaleY(1.1);
  }
}

/* 服务详情 */
.service-details {
  background-color: var(--background-light);
}

.service-detail-item {
  margin-bottom: 128px;
}

.service-detail-item:last-child {
  margin-bottom: 0;
}

.service-detail-content {
  display: flex;
  gap: 64px;
  align-items: center;
}

.service-detail-item.reverse .service-detail-content {
  flex-direction: row-reverse;
}

.service-detail-text {
  flex: 1;
}

.service-detail-text h3 {
  font-size: 1.75rem;
  font-weight: var(--font-weight-bold);
  margin-bottom: 24px;
  color: var(--text-primary);
}

.service-detail-text p {
  font-size: 1.125rem;
  line-height: 1.8;
  margin-bottom: 24px;
  color: var(--text-secondary);
}

.service-detail-text h4 {
  font-size: 1.25rem;
  font-weight: var(--font-weight-bold);
  margin-bottom: 16px;
  color: var(--text-primary);
}

.service-detail-text ul {
  margin-bottom: 24px;
  padding-left: 24px;
}

.service-detail-text li {
  font-size: 1rem;
  line-height: 1.8;
  margin-bottom: 8px;
  color: var(--text-secondary);
}

.service-detail-image {
  flex: 1;
  border-radius: var(--border-radius-lg);
  overflow: hidden;
  box-shadow: var(--shadow-lg);
}

.service-detail-image img {
  width: 100%;
  height: auto;
  display: block;
}

/* 服务优势 */
.service-advantages {
  background-color: white;
}

.advantages-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 32px;
}

.advantage-item {
  text-align: center;
  padding: 32px;
  background-color: var(--background-light);
  border-radius: var(--border-radius-lg);
  box-shadow: var(--shadow-md);
  transition: transform var(--transition-normal), box-shadow var(--transition-normal);
}

.advantage-item:hover {
  transform: translateY(-8px);
  box-shadow: var(--shadow-lg);
}

.advantage-icon {
  width: 64px;
  height: 64px;
  border-radius: 50%;
  background-color: rgba(24, 144, 255, 0.1);
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 24px;
  color: var(--primary-color);
}

.advantage-item h3 {
  font-size: 1.25rem;
  font-weight: var(--font-weight-bold);
  margin-bottom: 16px;
  color: var(--text-primary);
}

.advantage-item p {
  font-size: 1rem;
  color: var(--text-secondary);
  line-height: 1.6;
}

/* 服务流程 */
.service-process {
  background-color: var(--background-light);
}

.process-steps {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 32px;
  max-width: 1000px;
  margin: 0 auto;
}

.process-step {
  text-align: center;
  position: relative;
}

.process-step-number {
  width: 64px;
  height: 64px;
  border-radius: 50%;
  background-color: var(--primary-color);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.5rem;
  font-weight: var(--font-weight-bold);
  margin: 0 auto 24px;
}

.process-step h3 {
  font-size: 1.25rem;
  font-weight: var(--font-weight-bold);
  margin-bottom: 16px;
  color: var(--text-primary);
}

.process-step p {
  font-size: 1rem;
  color: var(--text-secondary);
  line-height: 1.6;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .hero-content h1 {
    font-size: 2.5rem;
  }

  section h2 {
    font-size: 2rem;
  }

  .service-detail-content {
    flex-direction: column;
    gap: 32px;
  }

  .service-detail-item.reverse .service-detail-content {
    flex-direction: column;
  }

  .process-steps {
    grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
    gap: 24px;
  }
}

@media (max-width: 480px) {
  .hero-content h1 {
    font-size: 2rem;
  }

  section h2 {
    font-size: 1.75rem;
  }

  section {
    padding: 64px 0;
  }

  .service-detail-item {
    margin-bottom: 80px;
  }

  .services-grid {
    grid-template-columns: 1fr;
  }

  .advantages-grid {
    grid-template-columns: 1fr;
  }

  .process-steps {
    grid-template-columns: 1fr;
  }
}

/* 互动小人样式 */
.interactive-character {
  position: fixed;
  bottom: 2rem;
  right: 2rem;
  width: 60px;
  height: 60px;
  background-color: var(--primary-color);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 1.5rem;
  cursor: pointer;
  transition: var(--transition-normal);
  box-shadow: 0 4px 6px rgba(59, 130, 246, 0.3);
  z-index: 1000;
  animation: bounce 2s infinite;
}

.interactive-character:hover {
  transform: scale(1.1) translateY(-2px);
  box-shadow: 0 10px 15px rgba(59, 130, 246, 0.4);
  background-color: var(--primary-hover);
  animation: none;
}

.interactive-character:hover::before {
  content: '你好！';
  position: absolute;
  bottom: 100%;
  right: 0;
  background-color: var(--text-primary);
  color: white;
  padding: 0.5rem 0.75rem;
  border-radius: var(--border-radius-md);
  font-size: 0.875rem;
  white-space: nowrap;
  margin-bottom: 0.5rem;
  box-shadow: var(--shadow-sm);
  z-index: -1;
}

.interactive-character:hover::after {
  content: '';
  position: absolute;
  bottom: 100%;
  right: 1rem;
  border-width: 0.5rem;
  border-style: solid;
  border-color: var(--text-primary) transparent transparent transparent;
  margin-bottom: -0.5rem;
  z-index: -1;
}

/* 动画定义 */
@keyframes bounce {
  0%, 20%, 50%, 80%, 100% {
    transform: translateY(0);
  }
  40% {
    transform: translateY(-10px);
  }
  60% {
    transform: translateY(-5px);
  }
}

/* 响应式调整 */
@media (max-width: 768px) {
  .interactive-character {
    bottom: 1.5rem;
    right: 1.5rem;
    width: 50px;
    height: 50px;
    font-size: 1.25rem;
  }
}

@media (max-width: 600px) {
  .interactive-character {
    bottom: 1rem;
    right: 1rem;
    width: 45px;
    height: 45px;
    font-size: 1.125rem;
  }
}
</style>
