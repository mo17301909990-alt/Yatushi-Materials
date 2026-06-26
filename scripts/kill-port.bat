@echo off
REM 定向杀端口，不误杀其他进程
REM 用法: kill-port.bat 8092

set PORT=%1
if "%PORT%"=="" (
  echo 用法: kill-port.bat <端口号>
  echo 示例: kill-port.bat 8092
  exit /b 1
)

for /f "tokens=5" %%a in ('netstat -ano ^| findstr ":%PORT% " ^| findstr LISTEN') do (
  echo 杀死 PID %%a (端口 %PORT%)
  taskkill /F /PID %%a 2>nul
)

REM 等端口释放
timeout /t 2 /nobreak >nul
echo 端口 %PORT% 已释放
