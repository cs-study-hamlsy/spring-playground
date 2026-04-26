#!/bin/sh

DIR="$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)"
JAVA_EXE=java
exec "$JAVA_EXE" -classpath "$DIR/gradle/wrapper/gradle-wrapper.jar" org.gradle.wrapper.GradleWrapperMain "$@"
