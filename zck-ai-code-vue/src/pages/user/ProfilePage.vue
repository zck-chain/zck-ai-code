<template>
  <div class="profile-page">
    <div class="profile-container">
      <!-- 页面标题 -->
      <div class="page-header">
        <h1>个人主页</h1>
        <p>管理您的个人信息和账号设置</p>
      </div>

      <!-- 主内容区域 -->
      <div class="profile-content">
        <!-- 左侧：个人资料卡片 -->
        <div class="profile-card">
          <!-- 头像展示与上传 -->
          <div class="avatar-section">
            <div class="avatar-container">
              <a-avatar
                :src="userInfo.userAvatar || defaultAvatar"
                :alt="userInfo.userName"
                class="user-avatar"
              />
              <div class="avatar-upload-overlay">
                <a-upload
                  :show-upload-list="false"
                  :before-upload="handleAvatarUpload"
                  accept="image/*"
                >
                  <a-button size="small" type="primary" icon="camera"> 更换头像 </a-button>
                </a-upload>
              </div>
            </div>
            <div v-if="uploading" class="uploading-indicator">
              <a-spin tip="上传中..." />
            </div>
          </div>

          <!-- 基本信息 -->
          <div class="basic-info">
            <h2 class="user-name">{{ userInfo.userName }}</h2>
            <p class="user-account">账号：{{ userInfo.userAccount }}</p>
            <div class="user-profile">
              <h3>个人简介</h3>
              <div v-if="editingProfile" class="edit-profile">
                <a-textarea
                  v-model:value="editForm.userProfile"
                  placeholder="请输入个人简介"
                  rows="3"
                  maxlength="200"
                />
                <div class="edit-actions">
                  <a-button @click="saveProfile">保存</a-button>
                  <a-button @click="cancelEditProfile">取消</a-button>
                </div>
              </div>
              <div v-else class="view-profile">
                <p>{{ userInfo.userProfile || '暂无简介' }}</p>
                <a-button @click="startEditProfile" size="small" type="link"> 编辑 </a-button>
              </div>
            </div>
          </div>
        </div>

        <!-- 右侧：详细信息表单 -->
        <div class="detail-form">
          <div class="form-section">
            <h3>账号信息</h3>
            <a-form :model="editForm" layout="vertical" :rules="rules" ref="formRef">
              <!-- 昵称编辑 -->
              <a-form-item label="昵称" name="userName">
                <a-input
                  v-model:value="editForm.userName"
                  placeholder="请输入昵称"
                  maxlength="20"
                />
              </a-form-item>

              <!-- 账号（不可编辑） -->
              <a-form-item label="账号">
                <a-input :value="userInfo.userAccount" disabled class="disabled-input" />
              </a-form-item>

              <!-- 邮箱 -->
              <a-form-item label="邮箱" name="email">
                <a-input v-model:value="editForm.email" placeholder="请输入邮箱" type="email" />
              </a-form-item>

              <!-- 姓名 -->
              <a-form-item label="姓名" name="realName">
                <a-input
                  v-model:value="editForm.realName"
                  placeholder="请输入真实姓名"
                  maxlength="20"
                />
              </a-form-item>

              <!-- 性别 -->
              <a-form-item label="性别" name="gender">
                <a-radio-group v-model:value="editForm.gender">
                  <a-radio value="male">男</a-radio>
                  <a-radio value="female">女</a-radio>
                  <a-radio value="other">其他</a-radio>
                </a-radio-group>
              </a-form-item>

              <!-- 生日 -->
              <a-form-item label="生日" name="birthday">
                <a-date-picker
                  v-model:value="editForm.birthday"
                  format="YYYY-MM-DD"
                  placeholder="请选择生日"
                />
              </a-form-item>

              <!-- 操作按钮 -->
              <div class="form-actions">
                <a-button type="primary" @click="saveUserInfo">保存修改</a-button>
                <a-button @click="resetForm">重置</a-button>
              </div>
            </a-form>
          </div>

          <!-- 账号安全信息 -->
          <div class="form-section security-section">
            <h3>账号安全</h3>
            <div class="security-item">
              <span class="security-label">密码</span>
              <a-button type="link" @click="goToChangePassword">修改密码</a-button>
            </div>
            <div class="security-item">
              <span class="security-label">手机验证</span>
              <a-button type="link" @click="goToBindPhone">绑定手机</a-button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import { useLoginUserStore } from '@/stores/loginUser'
import { updateUser, getUserInfoVo } from '@/api/userController'
import type { FormInstance } from 'ant-design-vue'

const router = useRouter()
const loginUserStore = useLoginUserStore()
const formRef = ref<FormInstance>()

// 上传状态
const uploading = ref(false)

// 编辑状态
const editingProfile = ref(false)

// 加载状态
const loading = ref(false)

// 默认头像
const defaultAvatar =
  'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=user%20avatar%20icon%20simple%20flat%20design%20blue%20background&image_size=square'

// 用户信息
const userInfo = ref({
  id: 0,
  userAccount: '',
  userName: '',
  userAvatar: '',
  userProfile: '',
  email: '',
  realName: '',
  gender: '',
  birthday: null,
})

// 编辑表单
const editForm = reactive({
  userName: '',
  userProfile: '',
  email: '',
  realName: '',
  gender: '',
  birthday: null,
})

// 表单验证规则
const rules = reactive({
  userName: [
    { required: true, message: '请输入昵称', trigger: 'blur' },
    { min: 2, max: 20, message: '昵称长度应在2-20个字符之间', trigger: 'blur' },
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入有效的邮箱地址', trigger: 'blur' },
  ],
  realName: [
    { required: true, message: '请输入真实姓名', trigger: 'blur' },
    { min: 2, max: 20, message: '姓名长度应在2-20个字符之间', trigger: 'blur' },
  ],
})

// 初始化用户信息
onMounted(async () => {
  await loadUserInfo()
})

// 加载用户信息
const loadUserInfo = async () => {
  try {
    loading.value = true

    // 从登录用户存储中获取基本信息
    const loginUser = loginUserStore.loginUser
    if (loginUser && loginUser.id) {
      // 调用API获取详细用户信息
      const response = await getUserInfoVo({ id: loginUser.id })
      if (response.data.code === 0 && response.data.data) {
        const userData = response.data.data
        userInfo.value = {
          ...userInfo.value,
          id: userData.id || 0,
          userAccount: userData.userAccount || '',
          userName: userData.userName || '',
          userAvatar: userData.userAvatar || '',
          userProfile: userData.userProfile || '',
        }

        // 初始化编辑表单
        editForm.userName = userInfo.value.userName
        editForm.userProfile = userInfo.value.userProfile
      } else {
        message.error('获取用户信息失败')
      }
    }
  } catch (error) {
    console.error('加载用户信息失败:', error)
    message.error('加载用户信息失败，请稍后重试')
  } finally {
    loading.value = false
  }
}

// 处理头像上传
const handleAvatarUpload = async (file: File) => {
  uploading.value = true

  try {
    // 这里简化处理，实际项目中需要先上传文件到服务器获取URL
    // 然后使用updateUser接口更新头像URL
    // 这里使用一个模拟的头像URL
    const avatarUrl = `https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=user%20avatar%20portrait%20professional%20clean&image_size=square`

    const response = await updateUser({
      id: userInfo.value.id,
      userAvatar: avatarUrl
    })

    if (response.data.code === 0) {
      userInfo.value.userAvatar = avatarUrl
      // 更新登录用户存储中的头像
      if (loginUserStore.loginUser) {
        loginUserStore.loginUser.userAvatar = avatarUrl
      }
      message.success('头像更新成功')
    } else {
      message.error('头像更新失败')
    }
  } catch (error) {
    console.error('头像更新失败:', error)
    message.error('头像更新失败，请稍后重试')
  } finally {
    uploading.value = false
  }

  return false // 阻止自动上传
}

// 开始编辑简介
const startEditProfile = () => {
  editingProfile.value = true
  editForm.userProfile = userInfo.value.userProfile
}

// 保存简介
const saveProfile = async () => {
  try {
    loading.value = true

    const response = await updateUser({
      id: userInfo.value.id,
      userProfile: editForm.userProfile,
    })

    if (response.data.code === 0) {
      userInfo.value.userProfile = editForm.userProfile
      // 更新登录用户存储中的简介
      if (loginUserStore.loginUser) {
        loginUserStore.loginUser.userProfile = editForm.userProfile
      }
      editingProfile.value = false
      message.success('简介保存成功')
    } else {
      message.error('简介保存失败')
    }
  } catch (error) {
    console.error('保存简介失败:', error)
    message.error('保存简介失败，请稍后重试')
  } finally {
    loading.value = false
  }
}

// 取消编辑简介
const cancelEditProfile = () => {
  editingProfile.value = false
  editForm.userProfile = userInfo.value.userProfile
}

// 保存用户信息
const saveUserInfo = async () => {
  if (!formRef.value) return

  try {
    await formRef.value.validate()
    loading.value = true

    const response = await updateUser({
      id: userInfo.value.id,
      userName: editForm.userName,
    })

    if (response.data.code === 0) {
      // 更新用户信息
      userInfo.value = {
        ...userInfo.value,
        userName: editForm.userName,
        email: editForm.email,
        realName: editForm.realName,
        gender: editForm.gender,
        birthday: editForm.birthday,
      }

      // 更新登录用户存储中的用户名
      if (loginUserStore.loginUser) {
        loginUserStore.loginUser.userName = editForm.userName
      }

      message.success('用户信息保存成功')
    } else {
      message.error('用户信息保存失败')
    }
  } catch (error) {
    console.error('保存用户信息失败:', error)
    message.error('保存用户信息失败，请稍后重试')
  } finally {
    loading.value = false
  }
}

// 重置表单
const resetForm = () => {
  if (formRef.value) {
    formRef.value.resetFields()
    // 重置编辑表单为当前用户信息
    editForm.userName = userInfo.value.userName
    editForm.userProfile = userInfo.value.userProfile
  }
}

// 跳转到修改密码页面
const goToChangePassword = () => {
  router.push('/user/change-password')
}

// 跳转到绑定手机页面
const goToBindPhone = () => {
  router.push('/user/bind-phone')
}
</script>

<style scoped>
.profile-page {
  min-height: 100vh;
  background-color: var(--background-page);
  padding: var(--spacing-lg) 0;
}

.profile-container {
  max-width: var(--max-container-width);
  margin: 0 auto;
  padding: 0 var(--spacing-lg);
}

.page-header {
  margin-bottom: var(--spacing-xl);
  text-align: center;
}

.page-header h1 {
  font-size: var(--font-size-xxxl);
  font-weight: var(--font-weight-semibold);
  color: var(--text-primary);
  margin-bottom: var(--spacing-xs);
}

.page-header p {
  font-size: var(--font-size-md);
  color: var(--text-secondary);
  margin: 0;
}

.profile-content {
  display: grid;
  grid-template-columns: 320px 1fr;
  gap: var(--spacing-lg);
}

/* 个人资料卡片 */
.profile-card {
  background: var(--background-default);
  border-radius: var(--border-radius-lg);
  box-shadow: var(--shadow-md);
  padding: var(--spacing-lg);
  position: sticky;
  top: var(--spacing-lg);
  height: fit-content;
}

/* 头像区域 */
.avatar-section {
  text-align: center;
  margin-bottom: var(--spacing-lg);
  position: relative;
}

.avatar-container {
  position: relative;
  display: inline-block;
}

.user-avatar {
  width: 120px;
  height: 120px;
  border-radius: var(--border-radius-full);
  border: 4px solid var(--border-color);
  transition: all var(--transition-normal);
}

.avatar-upload-overlay {
  position: absolute;
  bottom: 0;
  left: 50%;
  transform: translateX(-50%);
  opacity: 0;
  transition: all var(--transition-normal);
  background: rgba(0, 0, 0, 0.6);
  padding: var(--spacing-sm) var(--spacing-md);
  border-radius: 20px;
  width: 100%;
  text-align: center;
}

.avatar-container:hover .avatar-upload-overlay {
  opacity: 1;
}

.uploading-indicator {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background: rgba(255, 255, 255, 0.9);
  padding: var(--spacing-lg);
  border-radius: var(--border-radius-lg);
  box-shadow: var(--shadow-md);
}

/* 基本信息 */
.basic-info {
  text-align: center;
}

.user-name {
  font-size: var(--font-size-xl);
  font-weight: var(--font-weight-semibold);
  color: var(--text-primary);
  margin: var(--spacing-md) 0 var(--spacing-sm);
}

.user-account {
  font-size: var(--font-size-sm);
  color: var(--text-tertiary);
  margin: 0 0 var(--spacing-lg);
}

.user-profile {
  text-align: left;
  margin-top: var(--spacing-lg);
  padding-top: var(--spacing-lg);
  border-top: 1px solid var(--border-color);
}

.user-profile h3 {
  font-size: var(--font-size-lg);
  font-weight: var(--font-weight-medium);
  color: var(--text-primary);
  margin-bottom: var(--spacing-md);
}

.view-profile {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-sm);
}

.view-profile p {
  font-size: var(--font-size-sm);
  line-height: 1.5;
  color: var(--text-secondary);
  margin: 0;
  white-space: pre-wrap;
}

.edit-profile {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-md);
}

.edit-actions {
  display: flex;
  gap: var(--spacing-sm);
  justify-content: flex-end;
}

/* 详细信息表单 */
.detail-form {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-lg);
}

.form-section {
  background: var(--background-default);
  border-radius: var(--border-radius-lg);
  box-shadow: var(--shadow-md);
  padding: var(--spacing-lg);
}

.form-section h3 {
  font-size: var(--font-size-xl);
  font-weight: var(--font-weight-medium);
  color: var(--text-primary);
  margin-bottom: var(--spacing-lg);
  padding-bottom: var(--spacing-md);
  border-top: 1px solid var(--border-color);
}

/* 表单样式 */
:deep(.ant-form-item) {
  margin-bottom: var(--spacing-md);
}

:deep(.ant-form-item-label > label) {
  font-weight: var(--font-weight-medium);
  color: var(--text-primary);
}

.disabled-input {
  background-color: var(--background-light);
  cursor: not-allowed;
}

.form-actions {
  display: flex;
  gap: var(--spacing-md);
  margin-top: var(--spacing-lg);
  padding-top: var(--spacing-lg);
  border-top: 1px solid var(--border-color);
}

/* 安全信息 */
.security-section {
  background: var(--background-light);
}

.security-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: var(--spacing-md) 0;
  border-bottom: 1px solid var(--border-color);
}

.security-item:last-child {
  border-bottom: none;
}

.security-label {
  font-size: var(--font-size-sm);
  color: var(--text-primary);
}

/* 响应式设计 */
@media (max-width: var(--breakpoint-md)) {
  .profile-content {
    grid-template-columns: 1fr;
  }

  .profile-card {
    position: static;
  }

  .profile-container {
    padding: 0 var(--spacing-md);
  }

  .page-header h1 {
    font-size: var(--font-size-xxl);
  }

  .user-avatar {
    width: 100px;
    height: 100px;
  }
}

@media (max-width: var(--breakpoint-xs)) {
  .profile-page {
    padding: var(--spacing-md) 0;
  }

  .profile-container {
    padding: 0 var(--spacing-sm);
  }

  .profile-card,
  .form-section {
    padding: var(--spacing-md);
  }

  .page-header h1 {
    font-size: var(--font-size-xl);
  }

  .form-actions {
    flex-direction: column;
  }

  .form-actions button {
    width: 100%;
  }
}
</style>
