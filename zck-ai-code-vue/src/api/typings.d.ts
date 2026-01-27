declare namespace API {
  // 泛型基础响应类型
  type BaseResponse<T> = {
    code?: number
    data?: T
    message?: string
  }

  // 具体响应类型（保持向后兼容）
  type BaseResponseBoolean = BaseResponse<boolean>
  type BaseResponseLoginUserVO = BaseResponse<LoginUserVO>
  type BaseResponseLong = BaseResponse<number>
  type BaseResponsePageUserVO = BaseResponse<PageUserVO>
  type BaseResponseUser = BaseResponse<User>
  type BaseResponseUserVO = BaseResponse<UserVO>

  // 错误码枚举
  enum ErrorCode {
    SUCCESS = 0,
    PARAMS_ERROR = 40000,
    NOT_LOGIN_ERROR = 40100,
    NO_AUTH_ERROR = 40101,
    NOT_FOUND_ERROR = 40400,
    FORBIDDEN_ERROR = 40300,
    SYSTEM_ERROR = 50000,
    OPERATION_ERROR = 50001,
  }

  type DeleteRequest = {
    id?: number
  }

  type getUserByIdParams = {
    id: number
  }

  type getUserInfoVoParams = {
    id: number
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

  type PageUserVO = {
    records?: UserVO[]
    pageNumber?: number
    pageSize?: number
    totalPage?: number
    totalRow?: number
    optimizeCountQuery?: boolean
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
}
