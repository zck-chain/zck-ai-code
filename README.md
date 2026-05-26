# AI Code Father

AI 驱动的代码生成平台 —— 通过自然语言描述，让 AI 帮你生成完整的网站和应用。

## 项目简介

AI Code Father 是一个基于 AI 的 NoCode 平台。用户只需用自然语言描述需求（例如"帮我做一个电商页面"），AI 即可自动生成完整的 HTML 页面、多文件项目或 Vue 3 应用。支持实时预览、在线编辑、版本管理、部署上线等全流程功能。

### 核心功能

- **AI 代码生成** — 支持三种模式：单 HTML 页面、多文件项目、完整 Vue 3 应用
- **智能类型路由** — 基于 LangGraph4j 工作流，自动识别用户意图并选择合适的生成模式
- **流式对话** — 基于 SSE 的实时流式响应，打字机效果展示 AI 思考过程
- **可视化编辑** — 在预览页面中选择元素，AI 精准定位修改
- **项目管理** — 创建、编辑、删除应用，版本代码管理
- **应用部署** — 一键部署生成的应用，获取公开访问链接
- **封面生成** — 自动为 Vue 项目截取封面图
- **提示词优化** — AI 自动优化用户输入的提示词
- **对话导出** — 支持导出对话记录为 Markdown 文件
- **用户系统** — 注册/登录、权限管理（用户/管理员）
- **精选广场** — 展示优秀案例，支持分类筛选

## 技术栈

### 后端

| 技术 | 说明 |
|------|------|
| Java 21 | 运行环境 |
| Spring Boot 3.5 | 主框架 |
| MyBatis-Flex | ORM 框架 |
| MySQL 8 | 关系型数据库 |
| Redis | 缓存 / Session / 限流 |
| LangChain4j | LLM 集成框架 |
| LangGraph4j | AI 工作流编排 |
| 通义千问 (DashScope) | 大语言模型 |
| Prometheus + Grafana | 监控可视化 |
| Selenium | 应用封面截图 |
| 腾讯云 COS | 对象存储 |

### 前端

| 技术 | 说明 |
|------|------|
| TypeScript | 类型安全 |
| Vue 3 (Composition API) | 前端框架 |
| Vite | 构建工具 |
| Ant Design Vue 4 | UI 组件库 |
| Pinia | 状态管理 |
| Vue Router 4 | 路由管理 |
| Highlight.js | 代码语法高亮 |
| Marked | Markdown 渲染 |

## 项目结构

```
ai-code-father/
├── src/main/java/com/zck/aicode/   # 后端源码
│   ├── ai/                          # AI 服务层（LangChain4j 集成）
│   ├── langgraph4j/                 # LangGraph4j 工作流（节点/状态/工具）
│   ├── core/                        # 代码生成核心引擎
│   ├── controller/                  # REST 控制器
│   ├── service/                     # 业务服务层
│   ├── config/                      # Spring 配置
│   ├── ratelimiter/                 # 分布式限流
│   └── utils/                       # 工具类
├── sql/                             # 数据库建表 SQL
├── src/main/resources/
│   ├── prompt/                      # AI 提示词模板
│   └── application.yml              # 主配置文件
├── zck-ai-code-vue/                 # Vue 3 前端
│   └── src/
│       ├── pages/                   # 页面组件
│       ├── components/              # 通用组件
│       ├── api/                     # API 客户端（自动生成）
│       └── utils/                   # 前端工具类
└── pom.xml                          # Maven 项目配置
```

## 快速开始

### 环境要求

- **JDK** 21+
- **Node.js** ^20.19.0 或 >=22.12.0
- **MySQL** 8.0+
- **Redis** 6.0+
- **Maven** 3.6+

### 后端启动

1. 创建 MySQL 数据库，执行 `sql/` 目录下的建表 SQL
2. 配置 `src/main/resources/application-local.yml` 中的数据库连接、Redis 连接、AI 模型密钥等信息
3. 启动后端服务：

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

### 前端启动

```bash
cd zck-ai-code-vue
npm install
npm run dev
```

## 作者

赵承康（康哥）

## License

MIT
