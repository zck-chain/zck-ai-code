<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import api from '@/api'

// 状态管理
const router = useRouter()
const apps = ref<API.App[]>([])
const pageNum = ref(1)
const pageSize = ref(20)
const total = ref(0)
const loading = ref(false)
const searchParams = ref<API.AppQueryRequest>({
  appName: '',
  codeGenType: '',
  priority: undefined,
  userId: undefined
})

// 加载应用列表
const loadApps = async () => {
  try {
    loading.value = true
    const response = await api.appController.adminListAppByPage({
      ...searchParams.value,
      pageNum: pageNum.value,
      pageSize: pageSize.value
    })

    if (response.data.code === 0 && response.data.data) {
      apps.value = response.data.data.records || []
      total.value = response.data.data.totalRow || 0
    } else {
      alert('加载应用列表失败：' + response.data.message)
    }
  } catch (error) {
    console.error('加载应用列表失败', error)
    alert('加载应用列表失败，请稍后重试')
  } finally {
    loading.value = false
  }
}

// 编辑应用
const editApp = (appId: number) => {
  // 跳转到应用信息修改页
  router.push(`/app/edit/${appId}`)
}

// 删除应用
const deleteApp = async (appId: number) => {
  if (!confirm('确定要删除这个应用吗？')) return

  try {
    const response = await api.appController.adminDeleteApp({
      id: appId
    })

    if (response.data.code === 0 && response.data.data) {
      alert('删除成功')
      // 重新加载列表
      loadApps()
    } else {
      alert('删除失败：' + response.data.message)
    }
  } catch (error) {
    console.error('删除应用失败', error)
    alert('删除应用失败，请稍后重试')
  }
}

// 精选应用（设置优先级为99）
const featureApp = async (appId: number) => {
  try {
    const response = await api.appController.adminUpdateApp({
      id: appId,
      priority: 99
    })

    if (response.data.code === 0 && response.data.data) {
      alert('设置精选成功')
      // 重新加载列表
      loadApps()
    } else {
      alert('设置精选失败：' + response.data.message)
    }
  } catch (error) {
    console.error('设置精选失败', error)
    alert('设置精选失败，请稍后重试')
  }
}

// 搜索
const search = () => {
  pageNum.value = 1
  loadApps()
}

// 重置搜索
const resetSearch = () => {
  searchParams.value = {
    appName: '',
    codeGenType: '',
    priority: undefined,
    userId: undefined
  }
  pageNum.value = 1
  loadApps()
}

// 分页
const handlePageChange = (page: number) => {
  pageNum.value = page
  loadApps()
}

// 页面加载时初始化
onMounted(() => {
  loadApps()
})
</script>

<template>
  <div class="app-manage-container">
    <div class="page-header">
      <h1>应用管理</h1>
    </div>

    <!-- 搜索区域 -->
    <div class="search-section">
      <div class="search-form">
        <div class="form-item">
          <label>应用名称</label>
          <input
            v-model="searchParams.appName"
            type="text"
            placeholder="请输入应用名称"
            class="search-input"
          />
        </div>
        <div class="form-item">
          <label>生成类型</label>
          <input
            v-model="searchParams.codeGenType"
            type="text"
            placeholder="请输入生成类型"
            class="search-input"
          />
        </div>
        <div class="form-item">
          <label>优先级</label>
          <input
            v-model.number="searchParams.priority"
            type="number"
            placeholder="请输入优先级"
            class="search-input"
          />
        </div>
        <div class="form-item">
          <label>用户ID</label>
          <input
            v-model.number="searchParams.userId"
            type="number"
            placeholder="请输入用户ID"
            class="search-input"
          />
        </div>
        <div class="form-actions">
          <button @click="search" class="search-btn">搜索</button>
          <button @click="resetSearch" class="reset-btn">重置</button>
        </div>
      </div>
    </div>

    <!-- 应用列表 -->
    <div class="app-list-section">
      <div class="table-container">
        <table class="app-table">
          <thead>
            <tr>
              <th>ID</th>
              <th>应用名称</th>
              <th>封面</th>
              <th>生成类型</th>
              <th>优先级</th>
              <th>用户ID</th>
              <th>创建时间</th>
              <th>操作</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="app in apps" :key="app.id">
              <td>{{ app.id }}</td>
              <td>{{ app.appName || '未命名' }}</td>
              <td>
                <img
                  v-if="app.cover"
                  :src="app.cover"
                  alt="应用封面"
                  class="app-cover"
                />
                <span v-else>无封面</span>
              </td>
              <td>{{ app.codeGenType || '未知' }}</td>
              <td>{{ app.priority || 0 }}</td>
              <td>{{ app.userId }}</td>
              <td>{{ new Date(app.createTime || '').toLocaleString('zh-CN') }}</td>
              <td class="action-buttons">
                <button
                  @click="editApp(app.id!)"
                  class="edit-btn"
                >
                  编辑
                </button>
                <button
                  @click="deleteApp(app.id!)"
                  class="delete-btn"
                >
                  删除
                </button>
                <button
                  @click="featureApp(app.id!)"
                  class="feature-btn"
                >
                  精选
                </button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <!-- 空状态 -->
      <div v-if="apps.length === 0 && !loading" class="empty-state">
        <p>暂无应用数据</p>
      </div>

      <!-- 加载状态 -->
      <div v-if="loading" class="loading-state">
        <p>加载中...</p>
      </div>

      <!-- 分页 -->
      <div v-if="apps.length > 0" class="pagination">
        <button
          @click="handlePageChange(pageNum - 1)"
          :disabled="pageNum === 1"
          class="page-btn"
        >
          上一页
        </button>
        <span class="page-info">
          第 {{ pageNum }} 页，共 {{ Math.ceil(total / pageSize) }} 页
        </span>
        <button
          @click="handlePageChange(pageNum + 1)"
          :disabled="pageNum >= Math.ceil(total / pageSize)"
          class="page-btn"
        >
          下一页
        </button>
        <div class="page-size">
          <label>每页条数：</label>
          <select v-model.number="pageSize" @change="loadApps" class="page-size-select">
            <option value="10">10</option>
            <option value="20">20</option>
            <option value="50">50</option>
            <option value="100">100</option>
          </select>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.app-manage-container {
  padding: 24px;
  background-color: #f5f7fa;
  min-height: 100vh;
}

.page-header {
  margin-bottom: 32px;
}

.page-header h1 {
  font-size: 24px;
  font-weight: bold;
  color: #333;
  margin: 0;
}

.search-section {
  background-color: white;
  padding: 24px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  margin-bottom: 24px;
}

.search-form {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  align-items: end;
}

.form-item {
  display: flex;
  flex-direction: column;
  gap: 8px;
  min-width: 200px;
}

.form-item label {
  font-size: 14px;
  font-weight: 500;
  color: #666;
}

.search-input {
  padding: 8px 12px;
  border: 1px solid #e0e0e0;
  border-radius: 4px;
  font-size: 14px;
  outline: none;
  transition: all 0.3s ease;
}

.search-input:focus {
  border-color: #4CAF50;
  box-shadow: 0 0 0 3px rgba(76, 175, 80, 0.1);
}

.form-actions {
  display: flex;
  gap: 12px;
}

.search-btn {
  padding: 8px 24px;
  background-color: #4CAF50;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 14px;
  font-weight: bold;
  cursor: pointer;
  transition: all 0.3s ease;
}

.search-btn:hover {
  background-color: #45a049;
  transform: translateY(-1px);
  box-shadow: 0 2px 8px rgba(76, 175, 80, 0.3);
}

.reset-btn {
  padding: 8px 24px;
  background-color: #f0f0f0;
  color: #333;
  border: 1px solid #e0e0e0;
  border-radius: 4px;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.reset-btn:hover {
  background-color: #e0e0e0;
}

.app-list-section {
  background-color: white;
  padding: 24px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.table-container {
  overflow-x: auto;
  margin-bottom: 24px;
}

.app-table {
  width: 100%;
  border-collapse: collapse;
  font-size: 14px;
}

.app-table th,
.app-table td {
  padding: 12px 16px;
  text-align: left;
  border-bottom: 1px solid #e0e0e0;
}

.app-table th {
  background-color: #f9f9f9;
  font-weight: bold;
  color: #333;
  white-space: nowrap;
}

.app-table td {
  color: #666;
}

.app-cover {
  width: 60px;
  height: 40px;
  object-fit: cover;
  border-radius: 4px;
}

.action-buttons {
  display: flex;
  gap: 8px;
  white-space: nowrap;
}

.edit-btn {
  padding: 4px 12px;
  background-color: #4CAF50;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 12px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.edit-btn:hover {
  background-color: #45a049;
}

.delete-btn {
  padding: 4px 12px;
  background-color: #f44336;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 12px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.delete-btn:hover {
  background-color: #da190b;
}

.feature-btn {
  padding: 4px 12px;
  background-color: #2196F3;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 12px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.feature-btn:hover {
  background-color: #0b7dda;
}

.empty-state {
  text-align: center;
  padding: 60px 20px;
  color: #999;
  font-size: 16px;
}

.loading-state {
  text-align: center;
  padding: 60px 20px;
  color: #666;
  font-size: 16px;
}

.pagination {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  gap: 16px;
  margin-top: 24px;
}

.page-btn {
  padding: 6px 12px;
  background-color: white;
  border: 1px solid #e0e0e0;
  border-radius: 4px;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.page-btn:hover:not(:disabled) {
  background-color: #f5f5f5;
  border-color: #4CAF50;
}

.page-btn:disabled {
  color: #999;
  cursor: not-allowed;
}

.page-info {
  font-size: 14px;
  color: #666;
}

.page-size {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  color: #666;
}

.page-size-select {
  padding: 4px 8px;
  border: 1px solid #e0e0e0;
  border-radius: 4px;
  font-size: 14px;
  outline: none;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .app-manage-container {
    padding: 16px;
  }

  .search-form {
    flex-direction: column;
    align-items: stretch;
  }

  .form-item {
    min-width: auto;
  }

  .form-actions {
    flex-direction: column;
  }

  .pagination {
    flex-direction: column;
    align-items: stretch;
    gap: 12px;
  }

  .action-buttons {
    flex-direction: column;
  }
}
</style>