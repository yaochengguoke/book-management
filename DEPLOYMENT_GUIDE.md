# 智慧图书管理系统 - 部署指南

## 📋 项目概述

这是一个基于 Spring Boot + React 的图书管理系统，支持以下功能：
- 图书管理（增删查改）
- 借阅管理
- 用户管理
- 积分系统（签到、积分管理）
- 全局搜索
- 管理员模块

## 🚀 部署方式

### 方式一：Docker Compose 一键部署（推荐）

#### 前置条件
- 安装 Docker 和 Docker Compose
- 确保 80 端口和 8080 端口未被占用

#### 部署步骤

1. **克隆项目**
```bash
git clone <repository-url>
cd book-management
```

2. **启动服务**
```bash
docker-compose up -d
```

3. **访问系统**
- 前端地址：`http://localhost`
- 后端API：`http://localhost:8080/api`

4. **默认管理员账号**
- 用户名：`admin`
- 密码：`admin`

5. **停止服务**
```bash
docker-compose down
```

### 方式二：本地开发运行

#### 后端启动

```bash
cd backend
# 方式1：使用Maven运行
mvn spring-boot:run

# 方式2：使用已编译的JAR包
java -jar target/book-management-1.0.0.jar
```

#### 前端启动

```bash
cd frontend
npm install
npm run dev
```

访问地址：`https://localhost:3000`

## ☁️ 免费云端部署

### 推荐平台

| 平台 | 特点 | 免费额度 |
|------|------|----------|
| **Railway** | 支持Docker，自动部署 | $5/月免费额度 |
| **Render** | 支持Docker，静态站点免费 | 部分免费 |
| **Fly.io** | 全球CDN，支持Docker | $5/月免费额度 |
| **Heroku** | 容器支持，简单易用 | 免费层有限 |

### Railway 部署指南

1. **注册 Railway 账号**
   - 访问 https://railway.app/
   - 使用 GitHub 账号登录

2. **创建新项目**
   - 点击 "New Project"
   - 选择 "Deploy from GitHub repo"
   - 选择你的仓库

3. **配置环境变量**
   ```
   PORT: 8080
   JWT_SECRET: your-secret-key-change-this-in-production
   ```

4. **部署前端**
   - 在 Railway 创建新的 Static Site
   - 上传 `frontend/dist` 文件夹
   - 设置前端环境变量：`API_URL=https://your-backend-url/api`

### Render 部署指南

1. **注册 Render 账号**
   - 访问 https://render.com/

2. **部署后端**
   - 创建新的 Web Service
   - 选择 GitHub 仓库
   - 设置构建命令：`cd backend && mvn package -DskipTests`
   - 设置启动命令：`java -jar backend/target/book-management-1.0.0.jar`

3. **部署前端**
   - 创建新的 Static Site
   - 选择 frontend 文件夹
   - 设置构建命令：`npm install && npm run build`

## 🔧 环境变量配置

### 后端环境变量

| 变量名 | 描述 | 默认值 |
|--------|------|--------|
| `PORT` | 服务端口 | 8080 |
| `JWT_SECRET` | JWT密钥 | book-management-secret-key |

### 数据库配置

系统使用 SQLite 数据库，数据文件存储在 `backend/data/example_db.sqlite`

## 📁 项目结构

```
book-management/
├── backend/                 # Spring Boot 后端
│   ├── src/main/java/       # Java 源代码
│   ├── src/main/resources/  # 配置文件
│   ├── target/              # 编译输出
│   └── Dockerfile           # 后端Docker配置
├── frontend/                # React 前端
│   ├── dist/                # 构建产物
│   ├── index.html           # 主页面
│   ├── vite.config.ts       # Vite配置
│   └── package.json         # 依赖配置
├── docker-compose.yml       # Docker Compose配置
├── nginx.conf               # Nginx配置
└── DEPLOYMENT_GUIDE.md      # 部署指南
```

## 🔌 API 接口

### 认证接口
- `POST /api/auth/login` - 用户登录

### 图书接口
- `GET /api/books` - 获取图书列表
- `POST /api/books` - 添加图书
- `DELETE /api/books/{id}` - 删除图书
- `GET /api/books/search?title=xxx` - 搜索图书

### 借阅接口
- `GET /api/borrows` - 获取借阅列表
- `POST /api/borrows` - 新建借阅
- `PUT /api/borrows/{id}/return` - 归还图书

### 用户接口
- `GET /api/users` - 获取用户列表
- `POST /api/users` - 添加用户
- `DELETE /api/users/{id}` - 删除用户

### 积分接口
- `GET /api/points/user/{userId}` - 获取用户积分
- `POST /api/points/user/{userId}/signin` - 签到
- `POST /api/points/user/{userId}/earn` - 添加积分

## 🔒 安全建议

1. **生产环境**
   - 修改默认密码
   - 使用强 JWT_SECRET
   - 配置 HTTPS

2. **数据库**
   - 定期备份 SQLite 文件
   - 不要将数据库文件暴露在公共目录

3. **部署**
   - 使用 Docker 隔离环境
   - 配置防火墙规则

## 📱 移动端支持

系统已适配移动端访问，响应式设计支持：
- 手机端（<768px）
- 平板端（768px-1024px）
- 桌面端（>1024px）

## 🐛 常见问题

**Q: 前端无法访问后端？**
- 检查后端服务是否启动
- 确认 API_BASE_URL 配置正确
- 检查 CORS 配置

**Q: 登录失败？**
- 默认账号：admin/admin
- 检查后端服务状态

**Q: 数据丢失？**
- 确认使用 SQLite 而非 H2 内存数据库
- 检查数据目录挂载是否正确
