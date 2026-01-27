<template>
  <div class="user-manage-page">
    <div class="page-header">
      <h2>用户管理</h2>
      <a-button type="primary" @click="showAddModal">
        <template #icon>
          <PlusOutlined />
        </template>
        添加用户
      </a-button>
    </div>

    <!-- 搜索和筛选表单 -->
    <div class="search-form">
      <a-form :model="searchForm" layout="inline" @finish="handleSearch" class="query-form">
        <a-form-item label="用户名" class="query-item">
          <a-input
            v-model:value="searchForm.userName"
            placeholder="请输入用户名"
            style="width: 180px"
          />
        </a-form-item>
        <a-form-item label="账号" class="query-item">
          <a-input
            v-model:value="searchForm.userAccount"
            placeholder="请输入账号"
            style="width: 180px"
          />
        </a-form-item>
        <a-form-item label="角色" class="query-item">
          <a-select
            v-model:value="searchForm.userRole"
            placeholder="请选择角色"
            style="width: 120px"
          >
            <a-select-option value="">全部</a-select-option>
            <a-select-option value="user">普通用户</a-select-option>
            <a-select-option value="admin">管理员</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item class="query-actions">
          <a-button type="primary" html-type="submit"> 搜索 </a-button>
          <a-button @click="resetSearch" style="margin-left: 8px"> 重置 </a-button>
        </a-form-item>
      </a-form>
    </div>

    <!-- 用户列表表格 -->
    <div class="user-table">
      <a-table
        :columns="columns"
        :data-source="userList"
        :loading="loading"
        :pagination="pagination"
        @change="handleTableChange"
        row-key="id"
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'userAvatar'">
            <a-avatar :src="record.userAvatar" :size="32" />
          </template>
          <template v-else-if="column.key === 'userRole'">
            <a-tag :color="record.userRole === 'admin' ? 'blue' : 'green'">
              {{ record.userRole === 'admin' ? '管理员' : '普通用户' }}
            </a-tag>
          </template>
          <template v-else-if="column.key === 'createTime'">
            {{ formatDate(record.createTime) }}
          </template>
          <template v-else-if="column.key === 'actions'">
            <a-button type="link" @click="showEditModal(record)" :disabled="loading">
              编辑
            </a-button>
            <a-button type="link" danger @click="confirmDelete(record.id)" :disabled="loading">
              删除
            </a-button>
          </template>
        </template>
      </a-table>
    </div>

    <!-- 添加/编辑用户模态框 -->
    <a-modal
      v-model:visible="modalVisible"
      :title="isEdit ? '编辑用户' : '添加用户'"
      @ok="handleModalOk"
      @cancel="handleModalCancel"
      :confirm-loading="modalLoading"
    >
      <a-form :model="modalForm" :rules="modalRules" layout="vertical">
        <a-form-item label="用户名" name="userName">
          <a-input v-model:value="modalForm.userName" placeholder="请输入用户名" />
        </a-form-item>
        <a-form-item label="账号" name="userAccount">
          <a-input v-model:value="modalForm.userAccount" placeholder="请输入账号" />
        </a-form-item>
        <a-form-item label="头像URL" name="userAvatar">
          <a-input v-model:value="modalForm.userAvatar" placeholder="请输入头像URL（可选）" />
        </a-form-item>
        <a-form-item label="个人简介" name="userProfile">
          <a-textarea
            v-model:value="modalForm.userProfile"
            placeholder="请输入个人简介（可选）"
            rows="3"
          />
        </a-form-item>
        <a-form-item label="角色" name="userRole">
          <a-select v-model:value="modalForm.userRole" placeholder="请选择角色">
            <a-select-option value="user">普通用户</a-select-option>
            <a-select-option value="admin">管理员</a-select-option>
          </a-select>
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { message, Modal } from 'ant-design-vue'
import { PlusOutlined } from '@ant-design/icons-vue'
import { listUserVoByPage, addUser, updateUser, deleteUser } from '@/api/userController'

// 表格列定义
const columns = [
  {
    title: '序号',
    key: 'index',
    width: 60,
    customRender: (_: any, __: any, index: number) => {
      // 计算序号：(当前页码 - 1) * 每页大小 + 索引 + 1
      return (pagination.current - 1) * pagination.pageSize + index + 1
    },
  },
  {
    title: 'ID',
    dataIndex: 'id',
    key: 'id',
    width: 80,
  },
  {
    title: '头像',
    key: 'userAvatar',
    width: 80,
  },
  {
    title: '用户名',
    dataIndex: 'userName',
    key: 'userName',
    ellipsis: true,
  },
  {
    title: '账号',
    dataIndex: 'userAccount',
    key: 'userAccount',
    ellipsis: true,
  },
  {
    title: '角色',
    key: 'userRole',
    width: 100,
  },
  {
    title: '创建时间',
    key: 'createTime',
    width: 180,
  },
  {
    title: '操作',
    key: 'actions',
    width: 120,
    fixed: 'right',
  },
]

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
  showSizeChanger: true,
  pageSizeOptions: ['10', '20', '50', '100'],
  showTotal: (total: number) => `共 ${total} 条记录`,
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

// 模态框表单验证规则
const modalRules = {
  userName: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 2, max: 20, message: '用户名长度应在2-20个字符之间', trigger: 'blur' },
  ],
  userAccount: [
    { required: true, message: '请输入账号', trigger: 'blur' },
    { min: 3, max: 50, message: '账号长度应在3-50个字符之间', trigger: 'blur' },
  ],
  userRole: [{ required: true, message: '请选择角色', trigger: 'change' }],
}

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
      message.error('获取用户列表失败')
    }
  } catch (error) {
    message.error('网络错误，请稍后重试')
  } finally {
    loading.value = false
  }
}

// 处理表格分页和排序
const handleTableChange = (pagination: any, filters: any, sorter: any) => {
  pagination.current = pagination.current
  pagination.pageSize = pagination.pageSize
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
    modalLoading.value = true

    if (isEdit.value && currentUserId.value) {
      // 编辑用户
      const response = await updateUser({
        id: currentUserId.value,
        ...modalForm,
      })
      const result = response.data

      if (result.code === 0) {
        message.success('编辑用户成功')
        modalVisible.value = false
        getUserList()
      } else {
        message.error(result.message || '编辑用户失败')
      }
    } else {
      // 添加用户
      const response = await addUser(modalForm)
      const result = response.data

      if (result.code === 0) {
        message.success('添加用户成功')
        modalVisible.value = false
        getUserList()
      } else {
        message.error(result.message || '添加用户失败')
      }
    }
  } catch (error) {
    message.error('网络错误，请稍后重试')
  } finally {
    modalLoading.value = false
  }
}

// 处理模态框取消
const handleModalCancel = () => {
  modalVisible.value = false
}

// 确认删除
const confirmDelete = (userId: number) => {
  Modal.confirm({
    title: '删除用户',
    content: '确定要删除这个用户吗？此操作不可恢复。',
    okText: '确定',
    okType: 'danger',
    cancelText: '取消',
    onOk: () => handleDelete(userId),
  })
}

// 处理删除
const handleDelete = async (userId: number) => {
  try {
    loading.value = true

    const response = await deleteUser({ id: userId })
    const result = response.data

    if (result.code === 0) {
      message.success('删除用户成功')
      getUserList()
    } else {
      message.error(result.message || '删除用户失败')
    }
  } catch (error) {
    message.error('网络错误，请稍后重试')
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
.user-manage-page {
  padding: 24px;
  min-height: calc(100vh - 64px - 70px);
  background: #f0f2f5;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  padding-bottom: 16px;
  border-bottom: 1px solid #e8e8e8;
}

.page-header h2 {
  margin: 0;
  color: #1890ff;
  font-size: 20px;
  font-weight: bold;
}

.search-form {
  background: #fff;
  padding: 20px;
  border-radius: 4px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.09);
  margin-bottom: 24px;
}

/* 查询表单布局优化 */
.query-form {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 16px;
}

.query-item {
  margin-bottom: 0;
}

.query-actions {
  margin-bottom: 0;
  display: flex;
  align-items: center;
}

/* 响应式调整 */
@media (max-width: 768px) {
  .query-form {
    flex-direction: column;
    align-items: stretch;
    gap: 12px;
  }

  .query-item {
    width: 100%;
  }

  .query-item :deep(.ant-input),
  .query-item :deep(.ant-select) {
    width: 100% !important;
  }

  .query-actions {
    justify-content: center;
    margin-top: 8px;
  }
}

.user-table {
  background: #fff;
  padding: 20px;
  border-radius: 4px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.09);
}

@media (max-width: 768px) {
  .user-manage-page {
    padding: 16px;
  }

  .page-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }

  .search-form {
    padding: 16px;
  }

  .user-table {
    padding: 16px;
    overflow-x: auto;
  }
}
</style>
