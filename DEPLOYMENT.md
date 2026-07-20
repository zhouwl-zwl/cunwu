
# 村级智慧村务管理系统 - 部署文档

## 一、环境要求

### 后端环境
- Java 11+
- MySQL 8.0+
- Maven 3.6+

### 前端环境
- Node.js 16+
- npm 或 yarn

## 二、数据库配置

### 1. 创建数据库
```sql
CREATE DATABASE IF NOT EXISTS cunwu_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 2. 导入初始化数据
```bash
mysql -u root -p cunwu_db < backend/src/main/resources/init.sql
```

### 3. 默认账号
| 账号 | 密码 | 角色 |
|------|------|------|
| admin | 123456 | 管理员 |
| village_official | 123456 | 村干部 |
| resident_official | 123456 | 驻村干部 |
| villager | 123456 | 普通村民 |

## 三、后端部署

### 1. 修改配置文件
编辑 `backend/src/main/resources/application.yml`：

```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/cunwu_db?useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: your_password
    driver-class-name: com.mysql.cj.jdbc.Driver

jwt:
  secret: your_jwt_secret_key
  expire: 86400000
```

### 2. 打包构建
```bash
cd backend
mvn clean package -DskipTests
```

### 3. 运行
```bash
java -jar target/cunwu-0.0.1-SNAPSHOT.jar
```

### 4. 访问地址
后端服务：http://localhost:8080

## 四、移动端H5部署

### 1. 修改API地址
编辑 `mobile-h5/src/utils/request.js`：

```javascript
const instance = axios.create({
  baseURL: 'http://localhost:8080/api',
  timeout: 10000
})
```

### 2. 安装依赖
```bash
cd mobile-h5
npm install
```

### 3. 开发模式运行
```bash
npm run dev
```

### 4. 构建生产版本
```bash
npm run build
```

### 5. 访问地址
开发环境：http://localhost:5173
生产环境：部署 `dist` 目录到静态服务器

## 五、PC管理后台部署

### 1. 修改API地址
编辑 `pc-admin/src/utils/request.js`：

```javascript
const instance = axios.create({
  baseURL: 'http://localhost:8080/api',
  timeout: 10000
})
```

### 2. 安装依赖
```bash
cd pc-admin
npm install
```

### 3. 开发模式运行
```bash
npm run dev
```

### 4. 构建生产版本
```bash
npm run build
```

### 5. 访问地址
开发环境：http://localhost:5174
生产环境：部署 `dist` 目录到静态服务器

## 六、Nginx配置示例

```nginx
server {
    listen 80;
    server_name your_domain.com;

    location /api/ {
        proxy_pass http://localhost:8080/api/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }

    location /mobile/ {
        root /path/to/mobile-h5/dist;
        index index.html;
        try_files $uri $uri/ /mobile/index.html;
    }

    location /admin/ {
        root /path/to/pc-admin/dist;
        index index.html;
        try_files $uri $uri/ /admin/index.html;
    }
}
```

## 七、文件存储配置

### 本地存储（默认）
文件存储在 `backend/upload/` 目录下

### 阿里云OSS配置（可选）
在 `application.yml` 中添加：

```yaml
oss:
  enabled: true
  endpoint: your-endpoint
  access-key-id: your-access-key
  access-key-secret: your-secret
  bucket-name: your-bucket
```

## 八、注意事项

1. 确保MySQL服务正常运行，端口开放
2. 后端启动前确保数据库已创建并导入数据
3. 前端API地址需根据实际部署环境修改
4. 生产环境建议配置HTTPS
5. JWT密钥建议使用复杂随机字符串

## 九、项目结构

```
cunwu/
├── backend/          # SpringBoot后端
│   ├── src/main/java/com/example/cunwu/
│   ├── src/main/resources/
│   └── pom.xml
├── mobile-h5/        # 移动端H5
│   ├── src/
│   └── package.json
├── pc-admin/         # PC管理后台
│   ├── src/
│   └── package.json
└── DEPLOYMENT.md     # 部署文档
```
