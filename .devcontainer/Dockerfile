FROM ubuntu:24.04

ENV DEBIAN_FRONTEND=noninteractive

# Install OpenJDK 21 (default in Ubuntu 24.04) and minimal tools
RUN apt-get update && apt-get install -y --no-install-recommends \
    openjdk-21-jdk \
    curl \
    git \
    ssh \
 && rm -rf /var/lib/apt/lists/*

# Set JAVA_HOME and PATH
ENV JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64
ENV PATH="$JAVA_HOME/bin:${PATH}"

# Install JUnit Console (avoid Maven)
# Also installs helper script so student can type junit ... to run tests
ARG JUNIT_VER=1.11.0
RUN mkdir -p /opt/junit \
 && curl -fsSL "https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/${JUNIT_VER}/junit-platform-console-standalone-${JUNIT_VER}.jar" \
      -o /opt/junit/junit-platform-console-standalone.jar \
 && printf '#!/usr/bin/env bash\nexec java -jar /opt/junit/junit-platform-console-standalone.jar "$@"\n' > /usr/local/bin/junit \
 && chmod +x /usr/local/bin/junit

WORKDIR /workspaces
