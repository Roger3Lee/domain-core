#!/usr/bin/env pwsh
# Gradle wrapper script for PowerShell
# This script provides better PowerShell compatibility than gradlew.bat

$ErrorActionPreference = 'Stop'

$APP_HOME = Split-Path -Parent $MyInvocation.MyCommand.Definition
$CLASSPATH = Join-Path $APP_HOME 'gradle\wrapper\gradle-wrapper.jar'

# Find java
if ($env:JAVA_HOME) {
    $JAVA_EXE = Join-Path $env:JAVA_HOME 'bin\java.exe'
    if (-not (Test-Path $JAVA_EXE)) {
        Write-Error "JAVA_HOME is set to an invalid directory: $env:JAVA_HOME"
        exit 1
    }
} else {
    $JAVA_EXE = 'java'
}

# Execute Gradle
$GRADLE_USER_HOME_ARG = ''
if ($env:GRADLE_USER_HOME) {
    $GRADLE_USER_HOME_ARG = "-Dgradle.user.home=$($env:GRADLE_USER_HOME)"
}

& $JAVA_EXE -Xmx64m -Xms64m $env:JAVA_OPTS $env:GRADLE_OPTS $GRADLE_USER_HOME_ARG "-Dorg.gradle.appname=gradlew" -classpath $CLASSPATH org.gradle.wrapper.GradleWrapperMain @args
