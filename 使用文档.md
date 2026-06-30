# Interview Community — 面试社区项目文档

## 项目概述

一个面向技术面试的社区后端项目，提供用户认证、帖子管理、分类标签、评论互动等核心功能。基于 Spring Boot 3 + MyBatis-Plus + Spring Security + JWT 构建。

| 项目 | 说明 |
|------|------|
| 技术栈 | Spring Boot 3.4.1, MyBatis-Plus, Spring Security, MySQL, JWT, Redis |
| 端口 | 8080 |
| 包路径 | `com.community` |
| 数据库 | `interview_community` |

---

## 项目结构

```
src/main/java/com/community/
├── common/               # 通用工具和异常处理
│   ├── GlobalExceptionHandler.java
│   ├── JwtUtil.java
│   └── Result.java
├── config/               # 配置类
│   ├── JwtAuthenticationFilter.java
│   ├── MyBatisPlusConfig.java
│   └── SecurityConfig.java
├── controller/           # 接口层 (REST API)
│   ├── UserController.java
│   ├── PostController.java
│   ├── CategoryController.java
│   ├── CommentController.java
│   ├── TagController.java
│   └── TestController.java
├── dto/                  # 数据传输对象
│   ├── LoginRequest.java / LoginResponse.java
│   ├── RegisterRequest.java
│   ├── CreatePostRequest.java / PostVO.java
│   ├── CreateCommentRequest.java / CommentVO.java
├── entity/               # 数据库实体
│   ├── User.java / Post.java / Comment.java
│   ├── Category.java / Tag.java / PostTag.java
├── mapper/               # MyBatis-Plus 数据访问层
│   ├── UserMapper.java / PostMapper.java / CommentMapper.java
│   ├── CategoryMapper.java / TagMapper.java / PostTagMapper.java
├── service/              # 业务接口
│   └── impl/             # 业务实现
└── InterviewCommunityApplication.java
```

---

## 数据库设计

### 表结构

#### user（用户表）

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT (PK, 自增) | 用户ID |
| username | VARCHAR(50) | 用户名（唯一） |
| password | VARCHAR(255) | BCrypt 加密密码 |
| nickname | VARCHAR(50) | 昵称 |
| email | VARCHAR(100) | 邮箱 |
| avatar | VARCHAR(255) | 头像URL |
| deleted | TINYINT | 逻辑删除标志 |
| created_at | DATETIME | 创建时间 |
| updated_at | DATETIME | 更新时间 |

#### post（帖子表）

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT (PK, 自增) | 帖子ID |
| user_id | BIGINT | 作者ID |
| category_id | BIGINT | 分类ID |
| title | VARCHAR(100) | 标题 |
| content | TEXT | 内容 |
| view_count | INT | 浏览量 |
| like_count | INT | 点赞数 |
| deleted | TINYINT | 逻辑删除标志 |
| created_at | DATETIME | 创建时间 |
| updated_at | DATETIME | 更新时间 |

#### comment（评论表）

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT (PK, 自增) | 评论ID |
| post_id | BIGINT | 所属帖子ID |
| user_id | BIGINT | 评论者ID |
| parent_id | BIGINT | 父评论ID（支持楼中楼） |
| content | TEXT | 评论内容 |
| deleted | TINYINT | 逻辑删除标志 |
| created_at | DATETIME | 创建时间 |
| updated_at | DATETIME | 更新时间 |

#### category（分类表）

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT (PK, 自增) | 分类ID |
| name | VARCHAR(50) | 分类名称 |
| sort | INT | 排序值 |
| deleted | TINYINT | 逻辑删除标志 |
| created_at | DATETIME | 创建时间 |
| updated_at | DATETIME | 更新时间 |

#### tag（标签表）

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT (PK, 自增) | 标签ID |
| name | VARCHAR(50) | 标签名称（唯一） |
| deleted | TINYINT | 逻辑删除标志 |
| created_at | DATETIME | 创建时间 |
| updated_at | DATETIME | 更新时间 |

#### post_tag（帖子-标签中间表）

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT (PK, 自增) | 关联ID |
| post_id | BIGINT | 帖子ID |
| tag_id | BIGINT | 标签ID |
| created_at | DATETIME | 创建时间 |

---

## API 接口总览

### 用户模块 — `/api/user`

| 方法 | 路径 | 认证 | 说明 |
|------|------|------|------|
| POST | `/api/user/register` | 无需 | 用户注册 |
| POST | `/api/user/login` | 无需 | 用户登录 |

### 帖子模块 — `/api/post`

| 方法 | 路径 | 认证 | 说明 |
|------|------|------|------|
| POST | `/api/post` | 需要 | 创建帖子 |
| GET | `/api/post/{id}` | 无需 | 帖子详情（浏览量+1） |
| PUT | `/api/post/{id}` | 需要 | 修改帖子（仅作者） |
| DELETE | `/api/post/{id}` | 需要 | 删除帖子（仅作者） |
| GET | `/api/post` | 无需 | 分页列表（可按分类筛选） |
| GET | `/api/post/search` | 无需 | 按标题搜索 |
| GET | `/api/post/hot` | 无需 | 热门排行榜 |

### 分类模块 — `/api/category`

| 方法 | 路径 | 认证 | 说明 |
|------|------|------|------|
| GET | `/api/category` | 无需 | 全量列表 |
| GET | `/api/category/{id}` | 无需 | 按ID查询 |
| POST | `/api/category` | 无需 | 创建分类 |
| PUT | `/api/category/{id}` | 无需 | 更新分类 |
| DELETE | `/api/category/{id}` | 无需 | 删除分类 |

### 评论模块 — `/api/comment`

| 方法 | 路径 | 认证 | 说明 |
|------|------|------|------|
| POST | `/api/comment` | 需要 | 发表评论 |
| DELETE | `/api/comment/{id}` | 需要 | 删除评论（仅作者） |
| GET | `/api/comment` | 无需 | 按帖子分页查询 |

### 标签模块 — `/api/tag`

| 方法 | 路径 | 认证 | 说明 |
|------|------|------|------|
| GET | `/api/tag` | 无需 | 全量列表 |
| GET | `/api/tag/{id}` | 无需 | 按ID查询 |
| POST | `/api/tag` | 无需 | 创建标签（名称唯一） |
| PUT | `/api/tag/{id}` | 无需 | 更新标签 |
| DELETE | `/api/tag/{id}` | 无需 | 删除标签 |

---

## 接口详细说明

### 1. 用户注册

```
POST /api/user/register
Content-Type: application/json
```

**请求体：**
```json
{
    "username": "alice",
    "password": "123456",
    "nickname": "Alice"
}
```

| 参数 | 类型 | 必填 | 校验规则 |
|------|------|------|----------|
| username | String | 是 | 2-20个字符 |
| password | String | 是 | 6-20个字符 |
| nickname | String | 否 | 最长50字符，不传则默认等于username |

**响应（200）：**
```json
{
    "code": 200,
    "msg": "操作成功",
    "data": {
        "userId": 1,
        "username": "alice",
        "nickname": "Alice",
        "token": "eyJhbGciOiJIUzI1NiJ9..."
    }
}
```

> 注册成功后直接返回 JWT token，前端无需再调登录接口。

---

### 2. 用户登录

```
POST /api/user/login
Content-Type: application/json
```

**请求体：**
```json
{
    "username": "alice",
    "password": "123456"
}
```

**响应（200）：**
```json
{
    "code": 200,
    "msg": "操作成功",
    "data": {
        "userId": 1,
        "username": "alice",
        "nickname": "Alice",
        "token": "eyJhbGciOiJIUzI1NiJ9..."
    }
}
```

> 登录失败统一提示"用户名或密码错误"，不区分用户名不存在还是密码错误（安全规范）。

---

### 3. 创建帖子

```
POST /api/post
Content-Type: application/json
Authorization: Bearer <token>
```

**请求体：**
```json
{
    "title": "Java面试常见问题",
    "content": "2024年Java面试题汇总...",
    "categoryId": 1,
    "tagIds": [1, 2]
}
```

| 参数 | 类型 | 必填 | 校验规则 |
|------|------|------|----------|
| title | String | 是 | 10-100字符 |
| content | String | 是 | 最长50000字符 |
| categoryId | Long | 否 | 分类ID |
| tagIds | Long[] | 否 | 标签ID列表 |

**响应（200）：**
```json
{
    "code": 200,
    "msg": "操作成功",
    "data": {
        "id": 1,
        "userId": 1,
        "title": "Java面试常见问题",
        "content": "2024年Java面试题汇总...",
        "viewCount": 0,
        "likeCount": 0,
        "authorNickname": "Alice",
        "categoryName": "后端",
        "tagNames": ["Java", "面试"],
        "createdAt": "2026-06-30T12:00:00",
        "updatedAt": "2026-06-30T12:00:00"
    }
}
```

> `PostVO` 返回的是视图对象，比实体多出 `authorNickname`、`categoryName`、`tagNames` 三个展示字段，由 Service 层组装。

---

### 4. 帖子详情

```
GET /api/post/1
```

**响应（200）：** 返回 PostVO 格式（同上创建帖子的返回结构）。每次访问浏览量 +1。

---

### 5. 修改帖子

```
PUT /api/post/1
Content-Type: application/json
Authorization: Bearer <token>
```

**请求体：** 同创建帖子的结构。

> 仅帖子作者可以修改。非作者返回 `code: 400, msg: "无权修改"`。

---

### 6. 删除帖子

```
DELETE /api/post/1
Authorization: Bearer <token>
```

**响应（200）：**
```json
{"code": 200, "msg": "操作成功", "data": null}
```

> 逻辑删除（`deleted = 1`），数据仍在库中。仅作者可删除。

---

### 7. 帖子列表（分页）

```
GET /api/post?page=1&size=10&categoryId=1
```

| 参数 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| page | int | 1 | 页码 |
| size | int | 10 | 每页条数 |
| categoryId | Long | 不传则全部 | 按分类筛选 |

**响应（200）：**
```json
{
    "code": 200,
    "msg": "操作成功",
    "data": {
        "records": [PostVO, ...],
        "total": 25,
        "size": 10,
        "current": 1,
        "pages": 3
    }
}
```

> 按 `created_at` 倒序排列。

---

### 8. 按标题搜索

```
GET /api/post/search?keyword=Java&page=1&size=10
```

| 参数 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| keyword | String | 必填 | 搜索关键词（模糊匹配标题） |
| page | int | 1 | 页码 |
| size | int | 10 | 每页条数 |

**响应：** 分页格式同列表接口，按 `created_at` 倒序。

---

### 9. 热门帖子排行榜

```
GET /api/post/hot?page=1&size=10
```

**响应：** 分页格式，按 `view_count` 降序排列，浏览量相同则按 `created_at` 降序。

---

### 10. 发表评论

```
POST /api/comment
Content-Type: application/json
Authorization: Bearer <token>
```

**请求体：**
```json
{
    "postId": 1,
    "content": "好文章！",
    "parentId": null
}
```

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| postId | Long | 是 | 所属帖子ID |
| content | String | 是 | 评论内容 |
| parentId | Long | 否 | 父评论ID，为空表示顶楼评论，有值表示楼中楼回复 |

**响应（200）：**
```json
{
    "code": 200,
    "msg": "操作成功",
    "data": {
        "id": 1,
        "postId": 1,
        "userId": 1,
        "parentId": null,
        "content": "好文章！",
        "authorNickname": "Alice",
        "createdAt": "2026-06-30T12:00:00"
    }
}
```

> 楼中楼实现：通过 `parentId` 字段关联，`parentId IS NULL` 表示顶楼评论，`parentId = 某评论ID` 表示对该评论的回复。

---

### 11. 删除评论

```
DELETE /api/comment/1
Authorization: Bearer <token>
```

> 仅评论作者可删除。逻辑删除。

---

### 12. 评论列表

```
GET /api/comment?postId=1&parentId=&page=1&size=10
```

| 参数 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| postId | Long | 必填 | 帖子ID |
| parentId | Long | 不传 | 筛选指定父评论下的回复，不传则只返回顶楼评论 |
| page | int | 1 | 页码 |
| size | int | 10 | 每页条数 |

---

### 13. 分类/标签 CRUD

分类和标签接口用法类似，以分类为例：

```
GET  /api/category          # 全量列表，按 sort 排序
GET  /api/category/1        # 按ID查询
POST /api/category?name=后端&sort=1   # 创建
PUT  /api/category/1?name=前端       # 更新
DELETE /api/category/1               # 删除
```

标签接口路径为 `/api/tag`，标签名称具有唯一性约束。

---

## 认证与安全

### JWT 认证流程

```
用户登录 → 服务端验证身份 → 生成JWT（24小时有效） → 返回给客户端
客户端后续请求 → Header携带 Authorization: Bearer <token>
JwtAuthenticationFilter → 拦截请求 → 解析token → 设置SecurityContext
```

### 安全配置

```yaml
白名单（无需登录即可访问）:
  - POST /api/user/register
  - POST /api/user/login
  - GET  /api/post/**
  - GET  /api/comment/**
  - GET  /api/category/**
  - GET  /api/tag/**
  - POST /api/category/**
  - POST /api/tag/**

需要登录:
  - POST/PUT/DELETE /api/post/**
  - POST/DELETE /api/comment/**
```

- 密码使用 BCrypt 加密存储
- JWT 包含 `userId` 和 `username`，24小时过期
- 帖子/评论的删除修改都做所有权校验

---

## 统一返回格式

所有接口统一返回 `Result<T>` 格式：

```json
{
    "code": 200,       // 200=成功，400=业务异常，500=服务器错误
    "msg": "操作成功",
    "data": {}         // 泛型数据
}
```

### 异常处理策略

| 异常类型 | 处理方式 |
|----------|----------|
| RuntimeException | 返回 `code: 400, msg: 异常信息` |
| @Valid 校验失败 | 返回 `code: 400, msg: 第一条错误消息` |
| 未知异常 | 返回 `code: 500, msg: "服务器内部错误"` |

---

## 运行与测试

### 启动项目

1. 确保 MySQL 已启动，创建数据库 `interview_community`
2. 确保 `application.yml` 中数据库连接配置正确
3. 运行：

```bash
cd interview-community
./mvnw spring-boot:run
```

### Postman 测试流程

```
1. 注册    → POST /api/user/register
2. 登录    → POST /api/user/login  → 拿到 token
3. 创建分类 → POST /api/category?name=后端&sort=1
4. 创建标签 → POST /api/tag  → Body: {"name": "Java"}
5. 创建帖子 → POST /api/post  → 需要 token（带 tagIds）
6. 列表    → GET  /api/post?page=1&size=10
7. 详情    → GET  /api/post/1
8. 搜索    → GET  /api/post/search?keyword=Java
9. 排行榜  → GET  /api/post/hot
10. 评论   → POST /api/comment  → 需要 token
11. 评论列表 → GET /api/comment?postId=1
```

---

## 关键设计说明

1. **逻辑删除**：所有表都有 `deleted` 字段，MyBatis-Plus 全局配置自动过滤，数据不真正删除。

2. **自动填充**：`createdAt` 和 `updatedAt` 由 `MetaObjectHandler` 自动填充，实体类只需加 `@TableField(fill = ...)` 注解。

3. **PostVO 组装**：帖子列表返回的 VO 包含 `authorNickname`、`categoryName`、`tagNames`，由 Service 层通过关联查询组装（现阶段存在 N+1 问题，数据量小可忽略）。

4. **帖子-标签多对多**：通过 `post_tag` 中间表维护。创建/更新帖子时先删后插标签关联。

5. **评论楼中楼**：通过 `parentId` 实现，`parentId IS NULL` 为顶楼评论，`parentId = 某ID` 为回复。
