@REM ----------------------------------------------------------------------------
@REM Maven Wrapper startup batch script
@REM ----------------------------------------------------------------------------
@IF "%__MVNW_ARG0_NAME__%"=="" (SET "BASE_DIR=%~dp0") ELSE (SET "BASE_DIR=%__MVNW_ARG0_NAME__%")

@SET MAVEN_PROJECTBASEDIR=%BASE_DIR%
@IF NOT "%MAVEN_BASEDIR%"=="" SET MAVEN_PROJECTBASEDIR=%MAVEN_BASEDIR%

@SET WRAPPER_JAR="%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.jar"
@SET WRAPPER_LAUNCHER=org.apache.maven.wrapper.MavenWrapperMain

@SET DOWNLOAD_URL="https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar"

@FOR /F "usebackq tokens=1,2 delims==" %%A IN ("%MAVEN_PROJECTBASEDIR%\.mvn\wrapper\maven-wrapper.properties") DO (
    @IF "%%A"=="wrapperUrl" SET DOWNLOAD_URL=%%B
)

@IF EXIST %WRAPPER_JAR% (
    @SET INIT_SCRIPT=set dummy=
) ELSE (
    @echo Downloading Maven Wrapper...
    @powershell -Command "&{"^
        "$webclient = new-object System.Net.WebClient;"^
        "if (-not ([string]::IsNullOrEmpty('%MVNW_USERNAME%') -and [string]::IsNullOrEmpty('%MVNW_PASSWORD%'))) {"^
        "$webclient.Credentials = new-object System.Net.NetworkCredential('%MVNW_USERNAME%', '%MVNW_PASSWORD%');"^
        "}"^
        "[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12;"^
        "$webclient.DownloadFile('%DOWNLOAD_URL%', %WRAPPER_JAR%)"^
        "}"
    @IF "%ERRORLEVEL%"=="0" (echo Done) ELSE (
        echo Failed to download Maven Wrapper jar. Please check your connection.
        exit /B 1
    )
)

@SET MAVEN_JAVA_EXE="%JAVA_HOME%/bin/java.exe"
@IF NOT EXIST %MAVEN_JAVA_EXE% SET MAVEN_JAVA_EXE=java

%MAVEN_JAVA_EXE% ^
  -classpath %WRAPPER_JAR% ^
  "-Dmaven.multiModuleProjectDirectory=%MAVEN_PROJECTBASEDIR%" ^
  %WRAPPER_LAUNCHER% %MAVEN_CONFIG% %*
