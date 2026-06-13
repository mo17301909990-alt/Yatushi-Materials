@echo off
chcp 65001 >nul
echo 正在启动开发模式应用...

REM 获取当前脚本所在目录
set PROJECT_DIR=%~dp0
set TEMP_DIR=%PROJECT_DIR%temp

REM 创建临时目录（如果不存在）
if not exist "%TEMP_DIR%" (
    mkdir "%TEMP_DIR%"
    echo 已创建临时目录: %TEMP_DIR%
)

REM 设置JVM参数，指定临时目录
set JAVA_OPTS=-Djava.io.tmpdir=%TEMP_DIR% -Dserver.tomcat.basedir=%TEMP_DIR%

REM 启动应用（开发模式，使用Maven）
echo 临时目录已设置为: %TEMP_DIR%
echo 正在启动Spring Boot应用（开发模式）...
call mvn spring-boot:run -Dspring-boot.run.jvmArguments="%JAVA_OPTS%"

pause






















