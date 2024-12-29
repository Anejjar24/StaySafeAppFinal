# Utilise Gradle 8.6 avec JDK 17 pour assurer la compatibilité
FROM gradle:8.6.0-jdk17 AS builder

# Définir le répertoire de travail
WORKDIR /app

# Copier les fichiers de configuration nécessaires pour Gradle
COPY gradle/wrapper/ gradle/wrapper/
COPY gradle/libs.versions.toml gradle/
COPY gradlew .
COPY gradlew.bat .
COPY settings.gradle.kts .
COPY build.gradle.kts .
COPY gradle.properties .

# Donner les droits d'exécution à gradlew
RUN chmod +x gradlew

# Copier le code source du projet
COPY app/ app/

# Exécuter une tâche Gradle pour vérifier la configuration et télécharger les dépendances
RUN ./gradlew build --no-daemon

# Étape finale, si vous voulez produire une image exécutable (optionnel)
FROM openjdk:17-jdk-slim

# Copier les artefacts du build depuis le conteneur précédent
COPY --from=builder /app /app

WORKDIR /app

CMD ["./gradlew", "run"]
