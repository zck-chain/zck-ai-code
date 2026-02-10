<template>
  <div class="user-manage-container">
    <div class="page-header">
      <h1>用户管理</h1>
      <button @click="showAddModal" class="add-btn">
        添加用户
      </button>
    </div>

    <!-- 搜索区域 -->
    <div class="search-section">
      <div class="search-form">
        <div class="form-item">
          <label>用户名</label>
          <input
            v-model="searchForm.userName"
            type="text"
            placeholder="请输入用户名"
            class="search-input"
          />
        </div>
        <div class="form-item">
          <label>账号</label>
          <input
            v-model="searchForm.userAccount"
            type="text"
            placeholder="请输入账号"
            class="search-input"
          />
        </div>
        <div class="form-item">
          <label>角色</label>
          <select
            v-model="searchForm.userRole"
            class="search-input"
          >
            <option value="">全部</option>
            <option value="user">普通用户</option>
            <option value="admin">管理员</option>
          </select>
        </div>
        <div class="form-actions">
          <button @click="handleSearch" class="search-btn">搜索</button>
          <button @click="resetSearch" class="reset-btn">重置</button>
        </div>
      </div>
    </div>

    <!-- 用户列表 -->
    <div class="user-list-section">
      <div class="table-container">
        <table class="user-table">
          <thead>
            <tr>
              <th>ID</th>
              <th>用户名</th>
              <th>账号</th>
              <th>头像</th>
              <th>角色</th>
              <th>创建时间</th>
              <th>操作</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="user in userList" :key="user.id">
              <td>{{ user.id }}</td>
              <td>{{ user.userName || '未命名' }}</td>
              <td>{{ user.userAccount || '未知' }}</td>
              <td>
                <img
                  v-if="user.userAvatar"
                  :src="user.userAvatar"
                  alt="用户头像"
                  class="user-avatar"
                />
                <span v-else>无头像</span>
              </td>
              <td>{{ user.userRole === 'admin' ? '管理员' : '普通用户' }}</td>
              <td>{{ formatDate(user.createTime) }}</td>
              <td class="action-buttons">
                <button
                  @click="showEditModal(user)"
                  class="edit-btn"
                >
                  编辑
                </button>
                <button
                  @click="confirmDelete(user.id!)"
                  class="delete-btn"
                >
                  删除
                </button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <!-- 空状态 -->
      <div v-if="userList.length === 0 && !loading" class="empty-state">
        <p>暂无用户数据</p>
      </div>

      <!-- 加载状态 -->
      <div v-if="loading" class="loading-state">
        <p>加载中...</p>
      </div>

      <!-- 分页 -->
      <div v-if="userList.length > 0" class="pagination">
        <button
          @click="handlePageChange(pagination.current - 1)"
          :disabled="pagination.current === 1"
          class="page-btn"
        >
          上一页
        </button>
        <span class="page-info">
          第 {{ pagination.current }} 页，共 {{ Math.ceil(pagination.total / pagination.pageSize) }} 页
        </span>
        <button
          @click="handlePageChange(pagination.current + 1)"
          :disabled="pagination.current >= Math.ceil(pagination.total / pagination.pageSize)"
          class="page-btn"
        >
          下一页
        </button>
        <div class="page-size">
          <label>每页条数：</label>
          <select v-model.number="pagination.pageSize" @change="getUserList" class="page-size-select">
            <option value="10">10</option>
            <option value="20">20</option>
            <option value="50">50</option>
            <option value="100">100</option>
          </select>
        </div>
      </div>
    </div>

    <!-- 添加/编辑用户模态框 -->
    <div v-if="modalVisible" class="modal-overlay" @click="handleModalCancel">
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h3>{{ isEdit ? '编辑用户' : '添加用户' }}</h3>
          <button @click="handleModalCancel" class="close-btn">×</button>
        </div>
        <div class="modal-body">
          <div class="form-group">
            <label>用户名 *</label>
            <input
              v-model="modalForm.userName"
              type="text"
              placeholder="请输入用户名"
              class="form-input"
            />
          </div>
          <div class="form-group">
            <label>账号 *</label>
            <input
              v-model="modalForm.userAccount"
              type="text"
              placeholder="请输入账号"
              class="form-input"
            />
          </div>
          <div class="form-group">
            <label>头像URL</label>
            <input
              v-model="modalForm.userAvatar"
              type="text"
              placeholder="请输入头像URL（可选）"
              class="form-input"
            />
          </div>
          <div class="form-group">
            <label>个人简介</label>
            <textarea
              v-model="modalForm.userProfile"
              placeholder="请输入个人简介（可选）"
              rows="3"
              class="form-textarea"
            ></textarea>
          </div>
          <div class="form-group">
            <label>角色 *</label>
            <select
              v-model="modalForm.userRole"
              class="form-input"
            >
              <option value="user">普通用户</option>
              <option value="admin">管理员</option>
            </select>
          </div>
        </div>
        <div class="modal-footer">
          <button @click="handleModalCancel" class="cancel-btn">取消</button>
          <button @click="handleModalOk" class="confirm-btn" :disabled="modalLoading">
            {{ modalLoading ? '处理中...' : '确定' }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { listUserVoByPage, addUser, updateUser, deleteUser } from '@/api/userController'

// 搜索表单
const searchForm = reactive<API.UserQueryRequest>({
  pageNum: 1,
  pageSize: 10,
  userName: '',
  userAccount: '',
  userRole: '',
})

// 表格数据
const userList = ref<API.UserVO[]>([])
const loading = ref(false)
const pagination = reactive({
  current: 1,
  pageSize: 10,
  total: 0,
})

// 模态框状态
const modalVisible = ref(false)
const isEdit = ref(false)
const modalLoading = ref(false)
const currentUserId = ref<number | null>(null)

// 模态框表单
const modalForm = reactive<API.UserAddRequest>({
  userName: '',
  userAccount: '',
  userAvatar: '',
  userProfile: '',
  userRole: 'user',
})

// 格式化日期
const formatDate = (dateString?: string) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit',
  })
}

// 获取用户列表
const getUserList = async () => {
  try {
    loading.value = true

    const response = await listUserVoByPage({
      ...searchForm,
      pageNum: pagination.current,
      pageSize: pagination.pageSize,
    })
    const result = response.data

    if (result.code === 0 && result.data) {
      userList.value = result.data.records || []
      pagination.total = result.data.totalRow || 0
      pagination.current = result.data.pageNumber || 1
      pagination.pageSize = result.data.pageSize || 10
    } else {
      alert('获取用户列表失败：' + result.message)
    }
  } catch (error) {
    console.error('获取用户列表失败', error)
    alert('获取用户列表失败，请稍后重试')
  } finally {
    loading.value = false
  }
}

// 处理分页
const handlePageChange = (page: number) => {
  pagination.current = page
  getUserList()
}

// 处理搜索
const handleSearch = () => {
  pagination.current = 1
  getUserList()
}

// 重置搜索
const resetSearch = () => {
  searchForm.userName = ''
  searchForm.userAccount = ''
  searchForm.userRole = ''
  pagination.current = 1
  getUserList()
}

// 显示添加用户模态框
const showAddModal = () => {
  isEdit.value = false
  currentUserId.value = null
  Object.assign(modalForm, {
    userName: '',
    userAccount: '',
    userAvatar: '',
    userProfile: '',
    userRole: 'user',
  })
  modalVisible.value = true
}

// 显示编辑用户模态框
const showEditModal = (user: API.UserVO) => {
  isEdit.value = true
  currentUserId.value = user.id || null
  Object.assign(modalForm, {
    userName: user.userName || '',
    userAccount: user.userAccount || '',
    userAvatar: user.userAvatar || '',
    userProfile: user.userProfile || '',
    userRole: user.userRole || 'user',
  })
  modalVisible.value = true
}

// 处理模态框确定
const handleModalOk = async () => {
  try {
    // 表单验证
    if (!modalForm.userName) {
      alert('请输入用户名')
      return
    }
    if (!modalForm.userAccount) {
      alert('请输入账号')
      return
    }
    if (!modalForm.userRole) {
      alert('请选择角色')
      return
    }

    modalLoading.value = true

    if (isEdit.value && currentUserId.value) {
      // 编辑用户
      const response = await updateUser({
        id: currentUserId.value,
        ...modalForm,
      })
      const result = response.data

      if (result.code === 0) {
        alert('编辑用户成功')
        modalVisible.value = false
        getUserList()
      } else {
        alert('编辑用户失败：' + result.message)
      }
    } else {
      // 添加用户
      const response = await addUser(modalForm)
      const result = response.data

      if (result.code === 0) {
        alert('添加用户成功')
        modalVisible.value = false
        getUserList()
      } else {
        alert('添加用户失败：' + result.message)
      }
    }
  } catch (error) {
    console.error('处理用户数据失败', error)
    alert('网络错误，请稍后重试')
  } finally {
    modalLoading.value = false
  }
}

// 处理模态框取消
const handleModalCancel = () => {
  modalVisible.value = false
}

// 确认删除
const confirmDelete = (userId: string) => {
  if (!confirm('确定要删除这个用户吗？此操作不可恢复。')) return
  handleDelete(userId)
}

// 处理删除
const handleDelete = async (userId: string) => {
  try {
    loading.value = true

    const response = await deleteUser({ id: userId })
    const result = response.data

    if (result.code === 0) {
      alert('删除用户成功')
      getUserList()
    } else {
      alert('删除用户失败：' + result.message)
    }
  } catch (error) {
    console.error('删除用户失败', error)
    alert('网络错误，请稍后重试')
  } finally {
    loading.value = false
  }
}

// 初始加载
onMounted(() => {
  getUserList()
})
</script>

<style scoped>
.user-manage-container {
  padding: 24px;
  background-color: #f5f7fa;
  min-height: 100vh;
}

.page-header {
  margin-bottom: 32px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.page-header h1 {
  font-size: 24px;
  font-weight: bold;
  color: #333;
  margin: 0;
}

.add-btn {
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

.add-btn:hover {
  background-color: #45a049;
  transform: translateY(-1px);
  box-shadow: 0 2px 8px rgba(76, 175, 80, 0.3);
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

.user-list-section {
  background-color: white;
  padding: 24px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.table-container {
  overflow-x: auto;
  margin-bottom: 24px;
}

.user-table {
  width: 100%;
  border-collapse: collapse;
  font-size: 14px;
}

.user-table th,
.user-table td {
  padding: 12px 16px;
  text-align: left;
  border-bottom: 1px solid #e0e0e0;
}

.user-table th {
  background-color: #f9f9f9;
  font-weight: bold;
  color: #333;
  white-space: nowrap;
}

.user-table td {
  color: #666;
}

.user-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  object-fit: cover;
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

/* 模态框样式 */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal-content {
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  width: 90%;
  max-width: 500px;
  max-height: 90vh;
  overflow-y: auto;
}

.modal-header {
  padding: 20px;
  border-bottom: 1px solid #e0e0e0;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.modal-header h3 {
  font-size: 18px;
  font-weight: bold;
  color: #333;
  margin: 0;
}

.close-btn {
  background: none;
  border: none;
  font-size: 24px;
  cursor: pointer;
  color: #999;
  transition: color 0.3s ease;
}

.close-btn:hover {
  color: #333;
}

.modal-body {
  padding: 20px;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  font-size: 14px;
  font-weight: 500;
  color: #666;
}

.form-input {
  width: 100%;
  padding: 8px 12px;
  border: 1px solid #e0e0e0;
  border-radius: 4px;
  font-size: 14px;
  outline: none;
  transition: all 0.3s ease;
}

.form-input:focus {
  border-color: #4CAF50;
  box-shadow: 0 0 0 3px rgba(76, 175, 80, 0.1);
}

.form-textarea {
  width: 100%;
  padding: 8px 12px;
  border: 1px solid #e0e0e0;
  border-radius: 4px;
  font-size: 14px;
  outline: none;
  transition: all 0.3s ease;
  resize: vertical;
}

.form-textarea:focus {
  border-color: #4CAF50;
  box-shadow: 0 0 0 3px rgba(76, 175, 80, 0.1);
}

.modal-footer {
  padding: 20px;
  border-top: 1px solid #e0e0e0;
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}

.cancel-btn {
  padding: 8px 24px;
  background-color: #f0f0f0;
  color: #333;
  border: 1px solid #e0e0e0;
  border-radius: 4px;
  font-size: 14px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.cancel-btn:hover {
  background-color: #e0e0e0;
}

.confirm-btn {
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

.confirm-btn:hover:not(:disabled) {
  background-color: #45a049;
  transform: translateY(-1px);
  box-shadow: 0 2px 8px rgba(76, 175, 80, 0.3);
}

.confirm-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .user-manage-container {
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

  .page-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }

  .modal-content {
    width: 95%;
    margin: 20px;
  }
}
</style>
