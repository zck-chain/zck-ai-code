<template>
  <div class="contact-container">
    <!-- 粒子背景 -->
    <div class="particle-background"></div>

    <!-- 神经网络动画 -->
    <div class="neural-network"></div>

    <!-- 英雄区域 -->
    <section class="hero-section">
      <div class="hero-content">
        <h1>联系我们</h1>
        <p>如有任何问题或需求，请随时与我们联系</p>
        <!-- 数据流动画 -->
        <div class="data-flow"></div>
      </div>
    </section>

    <!-- 联系内容 -->
    <section class="contact-content">
      <div class="container">
        <h2>联系我们</h2>
        <div class="contact-grid">
          <!-- 联系表单 -->
          <div class="contact-form-section">
            <!-- 全息投影效果 -->
            <div class="hologram-effect"></div>
            <h3>发送消息</h3>
            <form @submit.prevent="submitForm" class="contact-form">
              <div class="form-group">
                <label for="name">姓名 *</label>
                <div class="form-input-container">
                  <div class="input-glow"></div>
                  <input
                    type="text"
                    id="name"
                    v-model="formData.name"
                    required
                    placeholder="请输入您的姓名"
                    class="form-input"
                  />
                </div>
              </div>
              <div class="form-group">
                <label for="email">邮箱 *</label>
                <div class="form-input-container">
                  <div class="input-glow"></div>
                  <input
                    type="email"
                    id="email"
                    v-model="formData.email"
                    required
                    placeholder="请输入您的邮箱"
                    class="form-input"
                  />
                </div>
              </div>
              <div class="form-group">
                <label for="phone">电话</label>
                <div class="form-input-container">
                  <div class="input-glow"></div>
                  <input
                    type="tel"
                    id="phone"
                    v-model="formData.phone"
                    placeholder="请输入您的电话"
                    class="form-input"
                  />
                </div>
              </div>
              <div class="form-group">
                <label for="message">留言内容 *</label>
                <div class="form-input-container">
                  <div class="input-glow"></div>
                  <textarea
                    id="message"
                    v-model="formData.message"
                    required
                    placeholder="请输入您的留言内容"
                    rows="5"
                    class="form-textarea"
                  ></textarea>
                </div>
              </div>
              <button type="submit" class="submit-btn" :disabled="submitting">
                <span>{{ submitting ? '提交中...' : '提交' }}</span>
                <div class="btn-glow"></div>
              </button>
            </form>
          </div>

          <!-- 联系信息 -->
          <div class="contact-info-section">
            <!-- 全息投影效果 -->
            <div class="hologram-effect"></div>
            <h3>联系信息</h3>
            <div class="info-items">
              <div class="info-item">
                <div class="info-icon">
                  <div class="icon-glow"></div>
                  <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0 1 18 0z"></path><circle cx="12" cy="10" r="3"></circle></svg>
                </div>
                <div class="info-content">
                  <h4>公司地址</h4>
                  <p>北京市海淀区中关村科技园区8号楼</p>
                </div>
              </div>
              <div class="info-item">
                <div class="info-icon">
                  <div class="icon-glow"></div>
                  <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M22 16.92v3a2 2 0 0 1-2.18 2 19.79 19.79 0 0 1-8.63-3.07 19.5 19.5 0 0 1-6-6 19.79 19.79 0 0 1-3.07-8.67A2 2 0 0 1 4.11 2h3a2 2 0 0 1 2 1.72 12.84 12.84 0 0 0 .7 2.81 2 2 0 0 1-.45 2.11L8.09 9.91a16 16 0 0 0 6 6l1.27-1.27a2 2 0 0 1 2.11-.45 12.84 12.84 0 0 0 2.81.7A2 2 0 0 1 22 16.92z"></path></svg>
                </div>
                <div class="info-content">
                  <h4>联系电话</h4>
                  <p>400-123-4567</p>
                </div>
              </div>
              <div class="info-item">
                <div class="info-icon">
                  <div class="icon-glow"></div>
                  <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M4 4h16c1.1 0 2 .9 2 2v12c0 1.1-.9 2-2 2H4c-1.1 0-2-.9-2-2V6c0-1.1.9-2 2-2z"></path><polyline points="22,6 12,13 2,6"></polyline></svg>
                </div>
                <div class="info-content">
                  <h4>电子邮箱</h4>
                  <p>contact@aizeroapp.com</p>
                </div>
              </div>
              <div class="info-item">
                <div class="info-icon">
                  <div class="icon-glow"></div>
                  <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="10"></circle><polyline points="12 6 12 12 16 14"></polyline></svg>
                </div>
                <div class="info-content">
                  <h4>工作时间</h4>
                  <p>周一至周五: 9:00 - 18:00</p>
                  <p>周六至周日: 休息</p>
                </div>
              </div>
            </div>

            <!-- 地图位置 -->
            <div class="map-section">
              <h3>公司位置</h3>
              <div class="map-container">
                <!-- 地图全息效果 -->
                <div class="map-hologram"></div>
                <img
                  src="https://images.unsplash.com/photo-1526778548025-fa2f459cd5ce?ixlib=rb-4.0.3&auto=format&fit=crop&w=1200&q=80"
                  alt="公司位置地图"
                  class="map-image"
                />
                <div class="map-overlay">
                  <div class="map-marker">
                    <div class="marker-glow"></div>
                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="currentColor" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0 1 18 0z"></path><circle cx="12" cy="10" r="3"></circle></svg>
                  </div>
                  <div class="map-info">
                    <h4>AI零代码应用生成平台</h4>
                    <p>北京市海淀区中关村科技园区8号楼</p>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- 常见问题 -->
    <section class="faq-section">
      <div class="container">
        <h2>常见问题</h2>
        <div class="faq-items">
          <div class="faq-item">
            <h3>如何开始使用你们的服务？</h3>
            <p>您可以注册我们的平台账号，然后在首页输入您的应用需求，我们的AI会自动生成相应的应用代码。您也可以联系我们的客服团队，获取一对一的咨询服务。</p>
          </div>
          <div class="faq-item">
            <h3>你们的服务费用是多少？</h3>
            <p>我们提供免费和付费两种服务模式。免费模式可以生成基础应用，付费模式提供更多高级功能和技术支持。具体价格请联系我们的销售团队获取详细报价。</p>
          </div>
          <div class="faq-item">
            <h3>应用生成需要多长时间？</h3>
            <p>简单应用通常在几分钟内即可生成，复杂应用可能需要更长时间。生成时间取决于应用的复杂度和功能需求。</p>
          </div>
          <div class="faq-item">
            <h3>生成的应用可以部署到自己的服务器吗？</h3>
            <p>是的，我们支持将生成的应用代码导出，您可以部署到自己的服务器上。同时，我们也提供云端部署服务，方便您快速上线应用。</p>
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
import { ref, onMounted } from 'vue'
import { message } from 'ant-design-vue'

// 表单数据
const formData = ref({
  name: '',
  email: '',
  phone: '',
  message: ''
})

// 提交状态
const submitting = ref(false)

// 提交表单
const submitForm = async () => {
  try {
    submitting.value = true

    // 模拟表单提交
    await new Promise(resolve => setTimeout(resolve, 1000))

    message.success('留言提交成功，我们会尽快与您联系！')

    // 重置表单
    formData.value = {
      name: '',
      email: '',
      phone: '',
      message: ''
    }
  } catch (error) {
    console.error('提交失败:', error)
    message.error('留言提交失败，请稍后重试')
  } finally {
    submitting.value = false
  }
}

onMounted(() => {
  // 页面加载时的初始化逻辑
  document.title = '联系我们 - AI零代码应用生成平台'
})
</script>

<style scoped>
.contact-container {
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
  background: radial-gradient(circle at 50% 50%, rgba(16, 185, 129, 0.05) 0%, rgba(59, 130, 246, 0.1) 100%);
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
    radial-gradient(circle at 20% 30%, rgba(16, 185, 129, 0.1) 0%, transparent 20%),
    radial-gradient(circle at 80% 70%, rgba(59, 130, 246, 0.1) 0%, transparent 20%),
    radial-gradient(circle at 40% 80%, rgba(139, 92, 246, 0.1) 0%, transparent 20%),
    radial-gradient(circle at 60% 20%, rgba(236, 72, 153, 0.1) 0%, transparent 20%);
  animation: particleFloat 20s ease-in-out infinite;
  /* 性能优化：减少重绘 */
  will-change: transform, opacity;
}

/* 响应式设计：在移动设备上调整特效 */
@media (max-width: 768px) {
  .particle-background::before {
    background-image:
      radial-gradient(circle at 20% 30%, rgba(16, 185, 129, 0.08) 0%, transparent 15%),
      radial-gradient(circle at 80% 70%, rgba(59, 130, 246, 0.08) 0%, transparent 15%),
      radial-gradient(circle at 40% 80%, rgba(139, 92, 246, 0.08) 0%, transparent 15%),
      radial-gradient(circle at 60% 20%, rgba(236, 72, 153, 0.08) 0%, transparent 15%);
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

  /* 调整联系表单和信息 */
  .contact-grid {
    grid-template-columns: 1fr;
  }

  /* 调整表单输入 */
  .form-input,
  .form-textarea {
    padding: 10px 14px;
  }

  /* 调整地图 */
  .map-overlay {
    position: static;
    margin-top: 16px;
    box-shadow: var(--shadow-md);
  }

  /* 调整全息投影效果 */
  .hologram-effect {
    animation: hologramPulse 4s ease-in-out infinite;
  }

  /* 调整图标发光效果 */
  .icon-glow {
    animation: iconGlow 3s ease-in-out infinite;
  }

  /* 调整地图全息效果 */
  .map-hologram {
    animation: mapHologram 4s ease-in-out infinite;
  }

  /* 调整地图标记发光效果 */
  .marker-glow {
    animation: markerGlow 3s ease-in-out infinite;
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
    linear-gradient(to right, transparent 99%, rgba(16, 185, 129, 0.3) 100%),
    linear-gradient(to bottom, transparent 99%, rgba(59, 130, 246, 0.3) 100%);
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
  background: linear-gradient(90deg, transparent, rgba(16, 185, 129, 0.8), transparent);
  animation: dataFlow 2s linear infinite;
}

.data-flow::after {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 2px;
  background: linear-gradient(90deg, transparent, rgba(59, 130, 246, 0.8), transparent);
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

/* 联系内容 */
.contact-content {
  background-color: white;
}

.contact-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 64px;
}

/* 联系表单 */
.contact-form-section h3,
.contact-info-section h3 {
  font-size: 1.5rem;
  font-weight: var(--font-weight-bold);
  margin-bottom: 32px;
  color: var(--text-primary);
}

/* 常见问题 */
.faq-section {
  background-color: var(--background-light);
}

.contact-form {
  display: flex;
  flex-direction: column;
  gap: 24px;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.form-group label {
  font-size: 1rem;
  font-weight: var(--font-weight-medium);
  color: var(--text-primary);
}

/* 通用输入样式 */
.form-input,
.form-textarea {
  padding: 12px 16px;
  border: 1px solid var(--border-color);
  border-radius: var(--border-radius-md);
  font-size: 1rem;
  outline: none;
  transition: all var(--transition-normal);
  background-color: white;
  color: var(--text-primary);
}

.form-input:focus,
.form-textarea:focus {
  border-color: var(--primary-color);
  box-shadow: 0 0 0 3px rgba(24, 144, 255, 0.1);
}

.form-textarea {
  resize: vertical;
  min-height: 120px;
}

/* 联系信息 */
.info-items {
  display: flex;
  flex-direction: column;
  gap: 24px;
  margin-bottom: 48px;
}

.info-item {
  display: flex;
  gap: 16px;
  align-items: flex-start;
}

.info-icon {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  background-color: rgba(24, 144, 255, 0.1);
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--primary-color);
  flex-shrink: 0;
}

.info-content h4 {
  font-size: 1.125rem;
  font-weight: var(--font-weight-bold);
  margin-bottom: 8px;
  color: var(--text-primary);
}

.info-content p {
  font-size: 1rem;
  color: var(--text-secondary);
  line-height: 1.6;
}

/* 地图位置 */
.map-section h3 {
  font-size: 1.25rem;
  font-weight: var(--font-weight-bold);
  margin-bottom: 16px;
  color: var(--text-primary);
}

.map-container {
  position: relative;
  border-radius: var(--border-radius-lg);
  overflow: hidden;
  box-shadow: var(--shadow-lg);
}

.map-image {
  width: 100%;
  height: auto;
  display: block;
}

.map-overlay {
  position: absolute;
  bottom: 24px;
  left: 24px;
  background-color: white;
  border-radius: var(--border-radius-md);
  padding: 16px;
  box-shadow: var(--shadow-lg);
  display: flex;
  gap: 12px;
  align-items: center;
}

.map-marker {
  color: var(--primary-color);
  flex-shrink: 0;
}

.map-info h4 {
  font-size: 1rem;
  font-weight: var(--font-weight-bold);
  margin-bottom: 4px;
  color: var(--text-primary);
}

.map-info p {
  font-size: 0.875rem;
  color: var(--text-secondary);
  line-height: 1.4;
}

/* 常见问题 */
.faq-items {
  max-width: 800px;
  margin: 0 auto;
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 32px;
}

.faq-item {
  background-color: white;
  border-radius: var(--border-radius-lg);
  padding: 32px;
  box-shadow: var(--shadow-md);
  transition: transform var(--transition-normal), box-shadow var(--transition-normal);
}

.faq-item:hover {
  transform: translateY(-8px);
  box-shadow: var(--shadow-lg);
}

.faq-item h3 {
  font-size: 1.25rem;
  font-weight: var(--font-weight-bold);
  margin-bottom: 16px;
  color: var(--text-primary);
}

.faq-item p {
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

  .contact-grid {
    grid-template-columns: 1fr;
    gap: 48px;
  }

  .faq-items {
    grid-template-columns: 1fr;
    gap: 24px;
  }

  .submit-btn {
    align-self: stretch;
  }

  .map-overlay {
    position: static;
    margin-top: 16px;
    box-shadow: var(--shadow-md);
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

  .contact-form-section h3,
  .contact-info-section h3 {
    font-size: 1.5rem;
  }

  .form-input,
  .form-textarea {
    padding: 10px 14px;
  }
}

/* 全息投影效果 */
.hologram-effect {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: radial-gradient(circle at 50% 50%, rgba(16, 185, 129, 0.05) 0%, rgba(59, 130, 246, 0.05) 100%);
  border-radius: var(--border-radius-lg);
  opacity: 0;
  transition: opacity var(--transition-normal);
  z-index: 0;
}

.contact-form-section:hover .hologram-effect,
.contact-info-section:hover .hologram-effect {
  opacity: 1;
}

/* 表单输入容器 */
.form-input-container {
  position: relative;
  display: flex;
  align-items: center;
}

/* 输入框发光效果 */
.input-glow {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(59, 130, 246, 0.1) 50%, transparent);
  border-radius: var(--border-radius-md);
  opacity: 0;
  transition: opacity var(--transition-normal);
}

.form-input:focus + .input-glow,
.form-textarea:focus + .input-glow {
  opacity: 1;
}

/* 增强表单输入样式 */
.form-input,
.form-textarea {
  padding: var(--spacing-md);
  border: 1px solid var(--border-color);
  border-radius: var(--border-radius-md);
  font-size: 1rem;
  color: var(--text-primary);
  background-color: white;
  transition: border-color var(--transition-normal), box-shadow var(--transition-normal);
  position: relative;
  z-index: 1;
}

.form-input:focus,
.form-textarea:focus {
  outline: none;
  border-color: var(--primary-color);
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
}

/* 提交按钮 */
.submit-btn {
  padding: var(--spacing-md) var(--spacing-xl);
  background: linear-gradient(135deg, var(--primary-color) 0%, var(--primary-hover) 100%);
  color: white;
  border: none;
  border-radius: var(--border-radius-md);
  font-size: 1rem;
  font-weight: var(--font-weight-medium);
  cursor: pointer;
  transition: transform var(--transition-normal), box-shadow var(--transition-normal);
  position: relative;
  z-index: 1;
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: var(--spacing-sm);
  align-self: flex-start;
}

/* 按钮发光效果 */
.btn-glow {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: radial-gradient(circle at 50% 50%, rgba(59, 130, 246, 0.2) 0%, transparent 70%);
  border-radius: var(--border-radius-md);
  opacity: 0;
  transition: opacity var(--transition-normal);
}

.submit-btn:hover:not(:disabled) .btn-glow {
  opacity: 1;
}

.submit-btn:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(59, 130, 246, 0.3);
}

.submit-btn:disabled {
  background-color: var(--border-light);
  cursor: not-allowed;
  transform: none;
  box-shadow: none;
}

/* 图标发光效果 */
.info-icon {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1;
}

.icon-glow {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: radial-gradient(circle, rgba(59, 130, 246, 0.2) 0%, transparent 70%);
  border-radius: 50%;
  opacity: 0;
  transition: opacity var(--transition-normal);
}

.info-item:hover .icon-glow {
  opacity: 1;
}

/* 地图全息效果 */
.map-container {
  position: relative;
  border-radius: var(--border-radius-lg);
  overflow: hidden;
  box-shadow: var(--shadow-md);
  transition: box-shadow var(--transition-normal);
}

.map-container:hover {
  box-shadow: var(--shadow-lg);
}

.map-hologram {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: radial-gradient(circle at 50% 50%, rgba(16, 185, 129, 0.05) 0%, rgba(59, 130, 246, 0.05) 100%);
  border-radius: var(--border-radius-lg);
  opacity: 0;
  transition: opacity var(--transition-normal);
}

.map-container:hover .map-hologram {
  opacity: 1;
}

/* 地图标记发光效果 */
.map-marker {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1;
}

.marker-glow {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: radial-gradient(circle, rgba(59, 130, 246, 0.3) 0%, transparent 70%);
  border-radius: 50%;
  opacity: 0;
  transition: opacity var(--transition-normal);
}

.map-container:hover .marker-glow {
  opacity: 1;
}

/* 增强地图信息样式 */
.map-info {
  position: relative;
  z-index: 1;
  background: white;
  padding: var(--spacing-md);
  border-radius: var(--border-radius-md);
  box-shadow: var(--shadow-md);
  transition: box-shadow var(--transition-normal);
}

.map-container:hover .map-info {
  box-shadow: 0 4px 12px rgba(59, 130, 246, 0.2);
}

/* 增强标题样式 */
.contact-form-section h3,
.contact-info-section h3 {
  position: relative;
  z-index: 1;
  transition: color var(--transition-normal);
}

.contact-form-section:hover h3,
.contact-info-section:hover h3 {
  color: var(--primary-color);
}

/* 增强标签样式 */
.form-group label {
  position: relative;
  z-index: 1;
  transition: color var(--transition-normal);
}

.contact-form-section:hover .form-group label {
  color: var(--primary-color);
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
