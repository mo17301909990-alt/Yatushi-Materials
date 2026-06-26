@echo off
chcp 65001 >nul
for /f "tokens=5" %%a in ('netstat -ano ^| findstr ":5173 " ^| findstr LISTEN') do (
  echo 杀死前端 PID %%a (端口 5173)
  taskkill /F /PID %%a 2>nul
)
timeout /t 2 /nobreak >nul
echo 前端已停止
