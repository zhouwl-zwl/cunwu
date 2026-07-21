@echo off
chcp 65001 >nul
title 村级智慧村务平台启动器

echo ============================================
echo         村级智慧村务平台启动器
echo ============================================
echo.
echo 正在启动项目，请稍候...
echo.

cd /d "%~dp0"

echo 1. 检查Node.js环境...
node --version >nul 2>&1
if %errorlevel% neq 0 (
    echo [错误] 未安装Node.js，请先安装Node.js
    echo 下载地址: https://nodejs.org/zh-cn/download/
    pause
    exit /b 1
)
echo [OK] Node.js环境正常

echo.
echo 2. 检查依赖是否已安装...
if not exist node_modules (
    echo [提示] 正在安装依赖，请耐心等待...
    npm install
    if %errorlevel% neq 0 (
        echo [错误] 依赖安装失败
        pause
        exit /b 1
    )
)
echo [OK] 依赖检查完成

echo.
echo 3. 启动开发服务器...
echo 服务将在 http://localhost:3000 运行
echo 按 Ctrl+C 停止服务
echo.

npm run dev

pause