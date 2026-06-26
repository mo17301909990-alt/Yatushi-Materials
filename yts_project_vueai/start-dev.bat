@echo off
chcp 65001 >nul
echo 启动前端开发服务器...
start /B cmd /c "npm run dev" > dev-server.log 2>&1
echo 前端已启动 (PID: !ERRORLEVEL!)
echo 访问: http://localhost:5173
