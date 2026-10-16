@echo off
cd /d "%~dp0"
if not exist "bin" mkdir bin
javac -d bin src\main\*.java src\model\*.java src\service\*.java src\ui\*.java src\algorithms\m1\*.java src\algorithms\m2\*.java src\algorithms\m3\*.java src\algorithms\m4\*.java src\algorithms\m5\*.java src\algorithms\m6\*.java
javac -cp bin -d bin test\EventFlowTests.java
java -cp bin EventFlowTests
pause
