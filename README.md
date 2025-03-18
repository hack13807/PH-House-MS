# 房屋管理系统
![License](https://img.shields.io/badge/license-Apache--2.0-blue.svg)

### 简介
一个基于SpringBoot+Thymeleaf渲染的房屋管理系统，现已支持免安装桌面应用模式。

### 特性
- 支持H2嵌入式数据库，无需安装MySQL
- 提供桌面应用界面，一键启动
- 支持租约管理、房间管理、会员管理等功能
- 简单易用，适合小型租赁管理

### 运行方式
#### 方式一：双击批处理文件
直接双击`start.bat`文件即可启动应用程序。

#### 方式二：命令行方式
```bash
# 编译应用程序
mvn clean package

# 运行应用程序
java --enable-preview -jar target/springbootdemo-0.0.1-SNAPSHOT.jar
```

### 默认账号
- 用户名：admin
- 密码：admin

### 数据库信息
- 嵌入式H2数据库
- 数据文件保存在程序运行目录的`data`文件夹内
- 可通过`http://localhost:8080/h2-console`访问数据库控制台
  - JDBC URL: jdbc:h2:file:./data/hms
  - 用户名: sa
  - 密码: 空