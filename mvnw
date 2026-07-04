#!/bin/sh
# ----------------------------------------------------------------------------
# Maven Wrapper startup shell script
# ----------------------------------------------------------------------------

if [ -z "$JAVA_HOME" ]; then
  JAVACMD='java'
else
  JAVACMD="$JAVA_HOME/bin/java"
fi

if [ ! -x "$JAVACMD" ] && [ ! -f "$JAVACMD" ]; then
  echo "Error: JAVA_HOME is not defined correctly." >&2
  echo "  We cannot execute $JAVACMD" >&2
  exit 1
fi

BASE_DIR=$(dirname "$0")
MAVEN_PROJECTBASEDIR="${MAVEN_BASEDIR:-$BASE_DIR}"
WRAPPER_JAR="$MAVEN_PROJECTBASEDIR/.mvn/wrapper/maven-wrapper.jar"
WRAPPER_LAUNCHER=org.apache.maven.wrapper.MavenWrapperMain

DOWNLOAD_URL="https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar"

# Read wrapperUrl from properties
if [ -f "$MAVEN_PROJECTBASEDIR/.mvn/wrapper/maven-wrapper.properties" ]; then
  DOWNLOAD_URL=$(grep "wrapperUrl" "$MAVEN_PROJECTBASEDIR/.mvn/wrapper/maven-wrapper.properties" | cut -d'=' -f2)
fi

if [ ! -f "$WRAPPER_JAR" ]; then
  echo "Downloading Maven Wrapper..."
  if command -v curl > /dev/null; then
    curl -o "$WRAPPER_JAR" "$DOWNLOAD_URL" -f
  elif command -v wget > /dev/null; then
    wget -O "$WRAPPER_JAR" "$DOWNLOAD_URL"
  else
    echo "Neither curl nor wget found. Cannot download Maven Wrapper." >&2
    exit 1
  fi
fi

exec "$JAVACMD" \
  -classpath "$WRAPPER_JAR" \
  "-Dmaven.multiModuleProjectDirectory=$MAVEN_PROJECTBASEDIR" \
  $MAVEN_OPTS \
  $WRAPPER_LAUNCHER "$@"
