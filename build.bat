@echo off
rem ----------------------------------------------------------------
rem  Compile the project and deploy it to Tomcat 9 as "BikeRental".
rem  1) Edit the TOMCAT_HOME line below to your Tomcat 9 folder.
rem  2) Stop Tomcat if it is running (bin\shutdown.bat).
rem  3) Run this file (double-click it, or type .\build.bat in a terminal).
rem ----------------------------------------------------------------
set "TOMCAT_HOME=C:\apache-tomcat-9.0.123"

cd /d "%~dp0"

if not exist "%TOMCAT_HOME%\lib\servlet-api.jar" (
    echo ERROR: servlet-api.jar was not found in "%TOMCAT_HOME%\lib"
    echo Edit the TOMCAT_HOME line at the top of build.bat to your Tomcat 9 folder.
    pause
    exit /b 1
)

echo Compiling...
javac --release 17 -encoding UTF-8 -cp "%TOMCAT_HOME%\lib\servlet-api.jar" -d webapp\WEB-INF\classes src\com\bikerental\model\*.java src\com\bikerental\exception\*.java src\com\bikerental\service\*.java src\com\bikerental\util\*.java src\com\bikerental\servlet\*.java
if errorlevel 1 (
    echo.
    echo Compilation failed. See the messages above.
    pause
    exit /b 1
)

echo Deploying to Tomcat...
if exist "%TOMCAT_HOME%\webapps\BikeRental" rmdir /s /q "%TOMCAT_HOME%\webapps\BikeRental"
xcopy /e /i /y /q webapp "%TOMCAT_HOME%\webapps\BikeRental" >nul

echo.
echo Done. Now start Tomcat (bin\startup.bat) and open:
echo     http://localhost:8080/BikeRental/
pause
