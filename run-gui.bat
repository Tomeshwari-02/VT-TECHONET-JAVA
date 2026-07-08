@echo off
cd /d "%~dp0"
javac -d out %~dp0src\com\meditrack\Main.java %~dp0src\com\meditrack\gui\MediTrackGui.java %~dp0src\com\meditrack\model\*.java %~dp0src\com\meditrack\service\*.java %~dp0src\com\meditrack\util\*.java
if errorlevel 1 (
  echo.
  echo Build failed. Please check the error above.
  pause
  exit /b 1
)
java -cp out com.meditrack.gui.MediTrackGui
