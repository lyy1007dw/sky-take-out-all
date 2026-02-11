# 苍穹外卖 (Sky Take Out)

## 📖 项目介绍

苍穹外卖是一个致力于提供高效、便捷外卖服务的餐饮系统。本项目采用前后端分离架构，涵盖了管理端（Web）和用户端（小程序/H5）两大核心子系统。

- **管理端**：供餐饮企业内部员工使用，主要功能包括菜品管理、套餐管理、订单管理、员工管理、数据统计等。
- **用户端**：供消费者使用，主要功能包括浏览菜单、购物车、下单支付、个人中心等。

## 🛠 技术栈

### 后端 (Backend)

- **开发语言**: Java 8/11
- **核心框架**: Spring Boot 2.7.3
- **持久层**: MyBatis, PageHelper
- **数据库连接池**: Druid
- **缓存**: Redis (Spring Data Redis, Spring Cache)
- **API 文档**: Knife4j (Swagger)
- **工具库**: Lombok, FastJSON, Commons Lang
- **对象存储**: 阿里云 OSS
- **支付**: 微信支付
- **地图服务**: 百度地图 API

### 前端 (Frontend)

- **Web 管理端**: Vue.js + Element UI (部署于 Nginx)
- **移动端**: Uniapp / H5 (部署于 Nginx)

### 基础设施

- **数据库**: MySQL 5.7+
- **中间件**: Nginx

## 📂 项目结构

```
sky-take-out-all
├── sky-take-out          # 后端工程根目录
│   ├── sky-common        # 公共模块 (常量、工具类、异常处理等)
│   ├── sky-pojo          # 数据模型 (Entity, DTO, VO)
│   └── sky-server        # 核心业务模块 (Controller, Service, Mapper)
├── frontend              # 前端资源目录
│   ├── nginx-1.20.2      # Nginx 服务器 (包含前端静态资源)
│   └── ...
└── README.md             # 项目说明文档
```

## 🚀 快速开始

### 1. 环境准备

确保您的开发环境已安装以下软件：

- JDK 1.8 或以上
- Maven 3.6+
- MySQL 5.7+
- Redis
- Nginx (用于前端部署，可选)

### 2. 数据库配置

1. 创建数据库 `sky_take_out`。
2. 导入 SQL 脚本。
   > **注意**: 本仓库根目录下未直接包含 `.sql` 脚本文件。请检查项目配套资料或联系提供方获取 `sky_take_out.sql` 数据库脚本。
3. 修改 `sky-server/src/main/resources/application-dev.yml` 中的数据库连接信息：

```yaml
sky:
  datasource:
    host: localhost
    port: 3306
    database: sky_take_out
    username: root
    password: your_password
```

### 3. Redis 配置

修改 `application-dev.yml` 中的 Redis 配置：

```yaml
sky:
  redis:
    host: localhost
    port: 6379
    password: your_password
    database: 10
```

### 4. 启动后端

1. 进入 `sky-take-out` 目录。
2. 使用 Maven 编译安装：`mvn clean install`。
3. 启动 `SkyApplication` (位于 `sky-server` 模块)。
4. 服务启动后，API 文档地址：`http://localhost:8080/doc.html`。

### 5. 启动前端

本项目前端资源通过 Nginx 托管，且 Nginx 配置了反向代理以解决跨域问题。

1. 进入 `frontend/nginx-1.20.2` 目录。
2. 启动 `nginx.exe`。
3. 访问入口：
    - **管理端**: `http://localhost` (默认加载 `html/sky/index.html`)
    - **API 代理**: Nginx 将 `/api/` 请求转发至后端 `/admin/`，`/user/` 转发至后端 `/user/`。

## ✨ 主要功能

### 管理端
- **员工管理**: 添加、修改、禁用员工账号。
- **分类管理**: 维护菜品和套餐的分类。
- **菜品管理**: 维护菜品信息，支持图片上传。
- **套餐管理**: 组合菜品发布套餐。
- **订单管理**: 接单、拒单、派送、完成订单。
- **数据报表**: 营业额统计、用户统计、订单统计、销量排名。

### 用户端
- **微信登录**: 用户一键登录。
- **浏览菜单**: 查看分类、菜品、套餐详情。
- **购物车**: 添加商品、修改数量。
- **下单支付**: 选择地址、备注、微信支付。
- **历史订单**: 查看历史订单状态。

## 📝 注意事项

- 本项目依赖阿里云 OSS 进行文件存储，需配置相关 Key。
- 微信支付功能需配置商户号及证书。
- 开发环境下默认使用 `dev` 配置文件。

## 🤝 贡献

欢迎提交 Issue 和 Pull Request！

---
Copyright © 2026 Sky Take Out. All rights reserved.
