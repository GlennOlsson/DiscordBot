@if "%DEBUG%" == "" @echo off
@rem ##########################################################################
@rem
@rem  DiscordBot.Glenn.Kakan startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%..

@rem Add default JVM options here. You can also use JAVA_OPTS and DISCORD_BOT_GLENN_KAKAN_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS=

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if "%ERRORLEVEL%" == "0" goto init

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto init

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:init
@rem Get command-line arguments, handling Windows variants

if not "%OS%" == "Windows_NT" goto win9xME_args

:win9xME_args
@rem Slurp the command line arguments.
set CMD_LINE_ARGS=
set _SKIP=2

:win9xME_args_slurp
if "x%~1" == "x" goto execute

set CMD_LINE_ARGS=%*

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%\lib\DiscordBot.Glenn.Kakan-1.0.jar;%APP_HOME%\lib\JDA-3.3.1_279.jar;%APP_HOME%\lib\jsoup-1.10.3.jar;%APP_HOME%\lib\jsch-0.1.54.jar;%APP_HOME%\lib\json-simple-1.1.1.jar;%APP_HOME%\lib\miglayout-swing-5.0.jar;%APP_HOME%\lib\pgslookandfeel-1.1.4.jar;%APP_HOME%\lib\slf4j-api-1.7.25.jar;%APP_HOME%\lib\jsr305-3.0.2.jar;%APP_HOME%\lib\commons-collections4-4.1.jar;%APP_HOME%\lib\json-20160810.jar;%APP_HOME%\lib\trove4j-3.0.3.jar;%APP_HOME%\lib\jna-4.4.0.jar;%APP_HOME%\lib\nv-websocket-client-2.2.jar;%APP_HOME%\lib\okhttp-3.8.1.jar;%APP_HOME%\lib\junit-4.10.jar;%APP_HOME%\lib\miglayout-core-5.0.jar;%APP_HOME%\lib\okio-1.13.0.jar;%APP_HOME%\lib\hamcrest-core-1.1.jar

@rem Execute DiscordBot.Glenn.Kakan
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %DISCORD_BOT_GLENN_KAKAN_OPTS%  -classpath "%CLASSPATH%" main.DiscordBot %CMD_LINE_ARGS%

:end
@rem End local scope for the variables with windows NT shell
if "%ERRORLEVEL%"=="0" goto mainEnd

:fail
rem Set variable DISCORD_BOT_GLENN_KAKAN_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
if  not "" == "%DISCORD_BOT_GLENN_KAKAN_EXIT_CONSOLE%" exit 1
exit /b 1

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
