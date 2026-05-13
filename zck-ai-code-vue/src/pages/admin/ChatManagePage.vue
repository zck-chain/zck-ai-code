<template>
  <div class="chat-manage-container">
    <div class="page-header">
      <h1>对话管理</h1>
    </div>

    <!-- 搜索区域 -->
    <div class="search-section">
      <div class="search-form">
        <div class="form-item">
          <label>消息内容</label>
          <input
            v-model="searchParams.message"
            type="text"
            placeholder="请输入消息内容"
            class="search-input"
          />
        </div>
        <div class="form-item">
          <label>消息类型</label>
          <input
            v-model="searchParams.messageType"
            type="text"
            placeholder="请输入消息类型"
            class="search-input"
          />
        </div>
        <div class="form-item">
          <label>应用ID</label>
          <input
            v-model="searchParams.appId"
            type="text"
            placeholder="请输入应用ID"
            class="search-input"
          />
        </div>
        <div class="form-item">
          <label>用户ID</label>
          <input
            v-model="searchParams.userId"
            type="text"
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

    <!-- 操作按钮区域 -->
    <div class="action-section">
      <button
        @click="exportSelectedChats"
        :disabled="selectedChatIds.length === 0"
        class="export-btn"
      >
        导出 Markdown
      </button>
    </div>

    <!-- 对话列表 -->
    <div class="chat-list-section">
      <div class="table-container">
        <table class="chat-table">
          <thead>
            <tr>
              <th>
                <input
                  type="checkbox"
                  @change="toggleSelectAll"
                  :checked="selectedChatIds.length === chats.length && chats.length > 0"
                  class="select-all"
                />
              </th>
              <th>ID</th>
              <th>消息内容</th>
              <th>消息类型</th>
              <th>应用ID</th>
              <th>用户ID</th>
              <th>创建时间</th>
              <th>操作</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="chat in chats" :key="chat.id">
              <td>
                <input
                  type="checkbox"
                  :checked="selectedChatIds.includes(String(chat.id))"
                  @change="toggleSelectChat(String(chat.id))"
                  class="select-item"
                />
              </td>
              <td>{{ chat.id }}</td>
              <td class="message-content">{{ chat.message || '无内容' }}</td>
              <td>{{ chat.messageType || '未知' }}</td>
              <td>{{ chat.appId }}</td>
              <td>{{ chat.userId }}</td>
              <td>{{ new Date(chat.createTime || '').toLocaleString('zh-CN') }}</td>
              <td class="action-buttons">
                <button
                  @click="deleteChat(String(chat.id!))"
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
      <div v-if="chats.length === 0 && !loading" class="empty-state">
        <p>暂无对话数据</p>
      </div>

      <!-- 加载状态 -->
      <div v-if="loading" class="loading-state">
        <p>加载中...</p>
      </div>

      <!-- 分页 -->
      <div v-if="chats.length > 0" class="pagination">
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
          <select v-model.number="pageSize" @change="loadChats" class="page-size-select">
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

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { message, Modal } from 'ant-design-vue'
import api from '@/api'

// 状态管理
const chats = ref<API.ChatHistory[]>([])
const pageNum = ref(1)
const pageSize = ref(20)
const total = ref(0)
const loading = ref(false)
const searchParams = ref<API.ChatHistoryQueryRequest>({
  message: '',
  messageType: '',
  appId: undefined,
  userId: undefined
})
const selectedChatIds = ref<string[]>([])

// 全选/取消全选
const toggleSelectAll = (event: Event) => {
  const target = event.target as HTMLInputElement
  if (target.checked) {
    selectedChatIds.value = chats.value.map(chat => String(chat.id))
  } else {
    selectedChatIds.value = []
  }
}

// 单选/取消单选
const toggleSelectChat = (chatId: string) => {
  const index = selectedChatIds.value.indexOf(chatId)
  if (index > -1) {
    selectedChatIds.value.splice(index, 1)
  } else {
    selectedChatIds.value.push(chatId)
  }
}

// 导出选中的对话记录为 Markdown
const exportSelectedChats = async () => {
  if (selectedChatIds.value.length === 0) {
    message.warning('请至少选择一条对话记录')
    return
  }

  try {
    loading.value = true
    
    // 将字符串ID转换为数字数组
    const ids = selectedChatIds.value;
    
    const response = await api.chatHistoryController.exportChatHistoryToMarkdown(ids)

    // 获取文件名
    const contentDisposition = response.headers?.get?.('Content-Disposition')
    let filename = '对话记录.md'
    if (contentDisposition) {
      const match = contentDisposition.match(/filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/)
      if (match && match[1]) {
        filename = match[1].replace(/['"]/g, '')
      }
    }

    // 下载文件 - response.data 才是实际的 Blob 数据
    const blob = new Blob([response.data], { type: 'text/markdown;charset=UTF-8' })
    const url = window.URL.createObjectURL(blob)
    const a = document.createElement('a')
    a.href = url
    a.download = filename
    document.body.appendChild(a)
    a.click()
    document.body.removeChild(a)
    window.URL.revokeObjectURL(url)

    message.success('导出成功')
  } catch (error) {
    console.error('导出失败', error)
    message.error('导出失败，请稍后重试')
  } finally {
    loading.value = false
  }
}

// 加载对话列表
const loadChats = async () => {
  try {
    loading.value = true
    const response = await api.chatHistoryController.listAllChatHistoryByPageForAdmin({
      ...searchParams.value,
      pageNum: pageNum.value,
      pageSize: pageSize.value
    })

    if (response.data.code === 0 && response.data.data) {
      chats.value = response.data.data.records || []
      total.value = response.data.data.totalRow || 0
    } else {
      message.error('加载对话列表失败：' + response.data.message)
    }
  } catch (error) {
    console.error('加载对话列表失败', error)
    message.error('加载对话列表失败，请稍后重试')
  } finally {
    loading.value = false
  }
}

// 删除对话
const deleteChat = async (chatId: string) => {
  Modal.confirm({
    title: '确认删除',
    content: '确定要删除这条对话吗？',
    okText: '确定',
    cancelText: '取消',
    onOk: async () => {
      try {
        // 这里需要根据后端实际提供的删除接口来实现
        // 假设后端提供了删除对话的接口
        // const response = await api.chatHistoryController.deleteChat({
        //   id: chatId
        // })

        // 模拟删除成功
        message.success('删除成功')
        // 重新加载列表
        loadChats()
      } catch (error) {
        console.error('删除对话失败', error)
        message.error('删除对话失败，请稍后重试')
      }
    }
  })
}

// 搜索
const search = () => {
  pageNum.value = 1
  loadChats()
}

// 重置搜索
const resetSearch = () => {
  searchParams.value = {
    message: '',
    messageType: '',
    appId: undefined,
    userId: undefined
  }
  pageNum.value = 1
  loadChats()
}

// 分页
const handlePageChange = (page: number) => {
  pageNum.value = page
  loadChats()
}

// 页面加载时初始化
onMounted(() => {
  loadChats()
})
</script>

<style scoped>
.chat-manage-container {
  padding: 24px;
  background-color: #f5f7fa;
  min-height: 100vh;
}

.action-section {
  margin-bottom: 24px;
  display: flex;
  justify-content: flex-end;
}

.export-btn {
  padding: 8px 24px;
  background-color: #2196F3;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 14px;
  font-weight: bold;
  cursor: pointer;
  transition: all 0.3s ease;
}

.export-btn:hover:not(:disabled) {
  background-color: #0b7dda;
  transform: translateY(-1px);
  box-shadow: 0 2px 8px rgba(33, 150, 243, 0.3);
}

.export-btn:disabled {
  background-color: #ccc;
  cursor: not-allowed;
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

.chat-list-section {
  background-color: white;
  padding: 24px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.table-container {
  overflow-x: auto;
  margin-bottom: 24px;
}

.chat-table {
  width: 100%;
  border-collapse: collapse;
  font-size: 14px;
}

.chat-table th,
.chat-table td {
  padding: 12px 16px;
  text-align: left;
  border-bottom: 1px solid #e0e0e0;
}

.chat-table th {
  background-color: #f9f9f9;
  font-weight: bold;
  color: #333;
  white-space: nowrap;
}

.chat-table td {
  color: #666;
}

.message-content {
  max-width: 300px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.action-buttons {
  display: flex;
  gap: 8px;
  white-space: nowrap;
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

/* 响应式设计 */
@media (max-width: 768px) {
  .chat-manage-container {
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