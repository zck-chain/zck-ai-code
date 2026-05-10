declare namespace API {
  type adminGetAppByIdParams = {
    /** 应用ID */
    id: number
  }

  type App = {
    id?: number
    appName?: string
    cover?: string
    initPrompt?: string
    codeGenType?: string
    deployKey?: string
    deployedTime?: string
    priority?: number
    userId?: number
    editTime?: string
    createTime?: string
    updateTime?: string
    isDelete?: number
  }

  type AppAddRequest = {
    initPrompt?: string
  }

  type AppDeployRequest = {
    appId?: number
  }

  type AppQueryRequest = {
    pageNum?: number
    pageSize?: number
    sortField?: string
    sortOrder?: string
    appName?: string
    codeGenType?: string
    priority?: number
    userId?: number
  }

  type AppUpdateRequest = {
    id?: number
    appName?: string
    cover?: string
    priority?: number
  }

  type BaseResponse = {
    code?: number
    data?: Record<string, any>
    message?: string
  }

  type BaseResponseApp = {
    code?: number
    data?: App
    message?: string
  }

  type BaseResponseBoolean = {
    code?: number
    data?: boolean
    message?: string
  }

  type BaseResponseDiffResult = {
    code?: number
    data?: DiffResult
    message?: string
  }

  type BaseResponseListVersionCode = {
    code?: number
    data?: VersionCode[]
    message?: string
  }

  type BaseResponseLoginUserVO = {
    code?: number
    data?: LoginUserVO
    message?: string
  }

  type BaseResponseLong = {
    code?: number
    data?: number
    message?: string
  }

  type BaseResponseMapStringObject = {
    code?: number
    data?: Record<string, any>
    message?: string
  }

  type BaseResponsePageApp = {
    code?: number
    data?: PageApp
    message?: string
  }

  type BaseResponsePageChatHistory = {
    code?: number
    data?: PageChatHistory
    message?: string
  }

  type BaseResponsePageUserVO = {
    code?: number
    data?: PageUserVO
    message?: string
  }

  type BaseResponseString = {
    code?: number
    data?: string
    message?: string
  }

  type BaseResponseUser = {
    code?: number
    data?: User
    message?: string
  }

  type BaseResponseUserVO = {
    code?: number
    data?: UserVO
    message?: string
  }

  type BaseResponseVersionCode = {
    code?: number
    data?: VersionCode
    message?: string
  }

  type BaseResponseVersionStatistics = {
    code?: number
    data?: VersionStatistics
    message?: string
  }

  type ChatHistory = {
    id?: number
    message?: string
    messageType?: string
    appId?: number
    userId?: number
    createTime?: string
    updateTime?: string
    isDelete?: number
  }

  type ChatHistoryQueryRequest = {
    pageNum?: number
    pageSize?: number
    sortField?: string
    sortOrder?: string
    id?: number
    message?: string
    messageType?: string
    appId?: number
    userId?: number
    lastCreateTime?: string
  }

  type chatToGenCodeParams = {
    /** 生成代码的描述 */
    message: string
    /** 应用ID */
    appId: number
  }

  type CodeChange = {
    type?: 'ADD' | 'DELETE' | 'MODIFY' | 'EQUAL'
    originalLineNum?: number
    revisedLineNum?: number
    originalLines?: string[]
    revisedLines?: string[]
    changeContext?: string
  }

  type CompareLatestRequest = {
    appId: number
    codeContent: string
  }

  type CompareRequest = {
    appId: number
    versionId1: number
    versionId2: number
  }

  type compareVersionsParams = {
    request: CompareRequest
  }

  type CreateVersionRequest = {
    appId: number
    codeContent: string
    commitMessage: string
    author: number
  }

  type deleteAppVersionsParams = {
    /** 应用ID */
    appId: number
  }

  type DeleteRequest = {
    id?: number
  }

  type DiagramTask = {
    mermaidCode?: string
    description?: string
  }

  type DiffResult = {
    fromVersion?: string
    toVersion?: string
    changes?: CodeChange[]
    statistics?: DiffStatistics
  }

  type DiffStatistics = {
    addedLines?: number
    deletedLines?: number
    modifiedLines?: number
    totalChanges?: number
    similarity?: number
  }

  type downloadAppCodeParams = {
    appId: number
  }

  type executeWorkflowParams = {
    prompt: string
  }

  type executeWorkflowWithFluxParams = {
    prompt: string
  }

  type getAiCodeGenPromptParams = {
    prompt: string
  }

  type getAppByIdParams = {
    /** 应用ID */
    id: number
  }

  type getBuildStatusParams = {
    appId: number
  }

  type getLatestVersionParams = {
    /** 应用ID */
    appId: number
  }

  type getUserByIdParams = {
    /** 用户ID */
    id: number
  }

  type getUserInfoVoParams = {
    /** 用户ID */
    id: number
  }

  type getVersionDetailParams = {
    /** 版本ID */
    versionId: number
  }

  type getVersionListParams = {
    /** 应用ID */
    appId: number
  }

  type getVersionStatisticsParams = {
    /** 应用ID */
    appId: number
  }

  type IllustrationTask = {
    query?: string
  }

  type ImageCollectionPlan = {
    contentImageTasks?: ImageSearchTask[]
    illustrationTasks?: IllustrationTask[]
    diagramTasks?: DiagramTask[]
    logoTasks?: LogoTask[]
  }

  type ImageResource = {
    category?: 'CONTENT' | 'LOGO' | 'ILLUSTRATION' | 'ARCHITECTURE'
    description?: string
    url?: string
  }

  type ImageSearchTask = {
    query?: string
  }

  type listAppChatHistoryParams = {
    /** 应用ID */
    appId: number
    /** 页面大小 */
    pageSize?: number
    /** 最后一条记录的创建时间 */
    lastCreateTime?: string
  }

  type LoginUserVO = {
    id?: number
    userAccount?: string
    userName?: string
    userAvatar?: string
    userProfile?: string
    userRole?: string
    createTime?: string
    updateTime?: string
  }

  type LogoTask = {
    description?: string
  }

  type PageApp = {
    records?: App[]
    pageNumber?: number
    pageSize?: number
    totalPage?: number
    totalRow?: number
    optimizeCountQuery?: boolean
  }

  type PageChatHistory = {
    records?: ChatHistory[]
    pageNumber?: number
    pageSize?: number
    totalPage?: number
    totalRow?: number
    optimizeCountQuery?: boolean
  }

  type PageUserVO = {
    records?: UserVO[]
    pageNumber?: number
    pageSize?: number
    totalPage?: number
    totalRow?: number
    optimizeCountQuery?: boolean
  }

  type QualityResult = {
    isValid?: boolean
    errors?: string[]
    suggestions?: string[]
  }

  type RollbackRequest = {
    appId: number
    targetVersionId: number
    commitMessage?: string
    author: number
  }

  type searchVersionsParams = {
    /** 应用ID */
    appId: number
    /** 搜索关键字 */
    keyword?: string
  }

  type ServerSentEventString = true

  type serveStaticResourceParams = {
    /** 部署密钥 */
    deployKey: string
  }

  type User = {
    id?: number
    userAccount?: string
    userPassword?: string
    userName?: string
    userAvatar?: string
    userProfile?: string
    userRole?: string
    editTime?: string
    createTime?: string
    updateTime?: string
    isDelete?: number
  }

  type UserAddRequest = {
    userName?: string
    userAccount?: string
    userAvatar?: string
    userProfile?: string
    userRole?: string
  }

  type UserLoginRequest = {
    userAccount?: string
    userPassword?: string
  }

  type UserQueryRequest = {
    pageNum?: number
    pageSize?: number
    sortField?: string
    sortOrder?: string
    id?: number
    userName?: string
    userAccount?: string
    userProfile?: string
    userRole?: string
  }

  type UserRegisterRequest = {
    userAccount?: string
    userPassword?: string
    checkPassword?: string
  }

  type UserUpdateRequest = {
    id?: number
    userName?: string
    userAvatar?: string
    userProfile?: string
    userRole?: string
  }

  type UserVO = {
    id?: number
    userAccount?: string
    userName?: string
    userAvatar?: string
    userProfile?: string
    userRole?: string
    createTime?: string
  }

  type VersionCode = {
    id?: number
    appId?: number
    version?: string
    commitMessage?: string
    author?: number
    codeContent?: string
    parentVersionId?: number
    fileStructure?: string
    creatorTime?: string
    updateTime?: string
    isDelete?: number
  }

  type VersionStatistics = {
    appId?: number
    totalVersions?: number
    firstVersion?: string
    latestVersion?: string
    lastUpdated?: string
  }

  type WorkflowContext = {
    currentStep?: string
    originalPrompt?: string
    imageListStr?: string
    imageList?: ImageResource[]
    enhancedPrompt?: string
    generationType?: 'HTML' | 'MULTI_FILE' | 'VUE_PROJECT'
    generatedCodeDir?: string
    buildResultDir?: string
    errorMessage?: string
    qualityResult?: QualityResult
    imageCollectionPlan?: ImageCollectionPlan
    contentImages?: ImageResource[]
    illustrations?: ImageResource[]
    diagrams?: ImageResource[]
    logos?: ImageResource[]
  }
}
