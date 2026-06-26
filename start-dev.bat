@echo off
chcp 65001 >nul
echo 印刷厂物料匹配系统 - 一键启动
echo.

REM 启动后端
echo [后端] 启动中...
start "Backend-8092" cmd /c "cd /d %~dp0yts_project && DASHSCOPE_API_KEY=placeholder mvn spring-boot:run"
echo [后端] 已在单独窗口启动（端口 8092）
timeout /t 8 /nobreak >nul

REM 启动前端
echo [前端] 启动中...
start /B cmd /c "cd /d %~dp0yts_project_vueai && npm run dev" > %~dp0yts_project_vueai\dev-server.log 2>&1
timeout /t 3 /nobreak >nul

echo.
echo 全部启动完成！
echo 前端: http://localhost:5173
echo 后端: http://localhost:8092
echo.
echo 关闭: 直接关窗口或用 taskkill /F /IM java.exe
pause
