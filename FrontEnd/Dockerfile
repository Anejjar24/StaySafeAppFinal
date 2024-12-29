# Use Gradle 8.6 with JDK 17 for compatibility
FROM gradle:8.6.0-jdk17 AS builder

# Set the working directory
WORKDIR /app

# Create local.properties file with SDK location
RUN echo "sdk.dir=/opt/android-sdk" > local.properties

# Set a default Maps API key (you should override this in docker-compose.yml)
ARG MAPS_API_KEY=default_key
ENV MAPS_API_KEY=${MAPS_API_KEY}

# Copy the necessary configuration files for Gradle
COPY gradle/wrapper/ gradle/wrapper/
COPY gradle/libs.versions.toml gradle/
COPY gradlew .
COPY gradlew.bat .
COPY settings.gradle.kts .
COPY build.gradle.kts .
COPY gradle.properties .

# Give execution rights to gradlew
RUN chmod +x gradlew

# Copy the source code
COPY app/ app/

# Create a debug.properties file with the Maps API key
RUN echo "MAPS_API_KEY=${MAPS_API_KEY}" > app/debug.properties

# Install Android SDK components
RUN apt-get update && apt-get install -y wget unzip \
    && mkdir -p /opt/android-sdk \
    && wget https://dl.google.com/android/repository/commandlinetools-linux-9477386_latest.zip \
    && unzip commandlinetools-linux-9477386_latest.zip -d /opt/android-sdk/cmdline-tools \
    && mv /opt/android-sdk/cmdline-tools/cmdline-tools /opt/android-sdk/cmdline-tools/latest \
    && yes | /opt/android-sdk/cmdline-tools/latest/bin/sdkmanager --licenses \
    && /opt/android-sdk/cmdline-tools/latest/bin/sdkmanager "platform-tools" "platforms;android-34" "build-tools;34.0.0" \
    && rm commandlinetools-linux-9477386_latest.zip

# Run the build
RUN ./gradlew build --no-daemon

# Final stage
FROM openjdk:17-jdk-slim
COPY --from=builder /app /app
WORKDIR /app
CMD ["./gradlew", "run"]