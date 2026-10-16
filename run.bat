@echo off
cd /d "%~dp0"
echo Compiling EventFlow...
if not exist "bin" mkdir bin
javac -d bin src\main\*.java src\model\*.java src\service\*.java src\ui\*.java src\algorithms\m1\*.java src\algorithms\m2\*.java src\algorithms\m3\*.java src\algorithms\m4\*.java src\algorithms\m5\*.java src\algorithms\m6\*.java
echo Starting EventFlow...
start javaw -cp bin main.Main
echo EventFlow launched successfully!
pause
