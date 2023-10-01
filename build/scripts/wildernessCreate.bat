@rem
@rem Copyright 2015 the original author or authors.
@rem
@rem Licensed under the Apache License, Version 2.0 (the "License");
@rem you may not use this file except in compliance with the License.
@rem You may obtain a copy of the License at
@rem
@rem      https://www.apache.org/licenses/LICENSE-2.0
@rem
@rem Unless required by applicable law or agreed to in writing, software
@rem distributed under the License is distributed on an "AS IS" BASIS,
@rem WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@rem See the License for the specific language governing permissions and
@rem limitations under the License.
@rem

@if "%DEBUG%" == "" @echo off
@rem ##########################################################################
@rem
@rem  wildernessCreate startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%..

@rem Resolve any "." and ".." in APP_HOME to make it shorter.
for %%i in ("%APP_HOME%") do set APP_HOME=%%~fi

@rem Add default JVM options here. You can also use JAVA_OPTS and WILDERNESS_CREATE_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS=

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if "%ERRORLEVEL%" == "0" goto execute

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto execute

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%\lib\wildernessCreate-1.0-SNAPSHOT.jar;%APP_HOME%\lib\observability-bridge-0.1.jar;%APP_HOME%\lib\immutable-collections-0.1.jar;%APP_HOME%\lib\neo4j-java-driver-5.0.0.jar;%APP_HOME%\lib\vertx-rx-java3-4.4.5.jar;%APP_HOME%\lib\vertx-rx-java3-gen-4.4.5.jar;%APP_HOME%\lib\rxjava-3.1.8.jar;%APP_HOME%\lib\vertx-micrometer-metrics-4.4.5.jar;%APP_HOME%\lib\vertx-mysql-client-4.4.5.jar;%APP_HOME%\lib\vertx-shell-4.4.5.jar;%APP_HOME%\lib\vertx-unit-4.4.5.jar;%APP_HOME%\lib\vertx-sockjs-service-proxy-3.9.1.jar;%APP_HOME%\lib\vertx-web-4.4.5.jar;%APP_HOME%\lib\vertx-service-proxy-4.4.5.jar;%APP_HOME%\lib\logback-classic-1.2.3.jar;%APP_HOME%\lib\java-client-3.0.5.jar;%APP_HOME%\lib\commons-math3-3.6.1.jar;%APP_HOME%\lib\guava-30.1.1-jre.jar;%APP_HOME%\lib\core-io-2.0.6.jar;%APP_HOME%\lib\reactor-core-3.4.22.jar;%APP_HOME%\lib\reactive-streams-1.0.4.jar;%APP_HOME%\lib\vertx-rx-gen-4.4.5.jar;%APP_HOME%\lib\vertx-sql-client-4.4.5.jar;%APP_HOME%\lib\vertx-auth-common-4.4.5.jar;%APP_HOME%\lib\vertx-web-common-4.4.5.jar;%APP_HOME%\lib\vertx-bridge-common-4.4.5.jar;%APP_HOME%\lib\vertx-core-4.4.5.jar;%APP_HOME%\lib\termd-core-1.1.6.jar;%APP_HOME%\lib\netty-handler-proxy-4.1.97.Final.jar;%APP_HOME%\lib\netty-codec-http2-4.1.97.Final.jar;%APP_HOME%\lib\netty-codec-http-4.1.97.Final.jar;%APP_HOME%\lib\netty-resolver-dns-4.1.97.Final.jar;%APP_HOME%\lib\netty-handler-4.1.97.Final.jar;%APP_HOME%\lib\netty-tcnative-classes-2.0.54.Final.jar;%APP_HOME%\lib\micrometer-core-1.11.3.jar;%APP_HOME%\lib\HdrHistogram-2.1.12.jar;%APP_HOME%\lib\slf4j-api-2.0.7.jar;%APP_HOME%\lib\sshd-core-1.2.0.jar;%APP_HOME%\lib\logback-core-1.2.3.jar;%APP_HOME%\lib\failureaccess-1.0.1.jar;%APP_HOME%\lib\listenablefuture-9999.0-empty-to-avoid-conflict-with-guava.jar;%APP_HOME%\lib\jsr305-3.0.2.jar;%APP_HOME%\lib\checker-qual-3.8.0.jar;%APP_HOME%\lib\error_prone_annotations-2.5.1.jar;%APP_HOME%\lib\j2objc-annotations-1.3.jar;%APP_HOME%\lib\netty-transport-native-unix-common-4.1.97.Final.jar;%APP_HOME%\lib\netty-codec-socks-4.1.97.Final.jar;%APP_HOME%\lib\netty-codec-dns-4.1.97.Final.jar;%APP_HOME%\lib\netty-codec-4.1.97.Final.jar;%APP_HOME%\lib\netty-transport-4.1.97.Final.jar;%APP_HOME%\lib\netty-buffer-4.1.97.Final.jar;%APP_HOME%\lib\netty-resolver-4.1.97.Final.jar;%APP_HOME%\lib\netty-common-4.1.97.Final.jar;%APP_HOME%\lib\vertx-codegen-4.4.5.jar;%APP_HOME%\lib\jackson-databind-2.15.0.jar;%APP_HOME%\lib\jackson-annotations-2.15.0.jar;%APP_HOME%\lib\jackson-core-2.15.0.jar;%APP_HOME%\lib\micrometer-observation-1.11.3.jar;%APP_HOME%\lib\micrometer-commons-1.11.3.jar;%APP_HOME%\lib\LatencyUtils-2.0.3.jar;%APP_HOME%\lib\fastdoubleparser-0.8.0.jar


@rem Execute wildernessCreate
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %WILDERNESS_CREATE_OPTS%  -classpath "%CLASSPATH%"  %*

:end
@rem End local scope for the variables with windows NT shell
if "%ERRORLEVEL%"=="0" goto mainEnd

:fail
rem Set variable WILDERNESS_CREATE_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
if  not "" == "%WILDERNESS_CREATE_EXIT_CONSOLE%" exit 1
exit /b 1

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
