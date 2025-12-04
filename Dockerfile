# --- Steg 1: Bygg applikationen (Builder) ---
# Vi använder en image med JDK för att kunna kompilera koden
FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app

# 1. Kopiera Gradle-filer först för att cacha beroenden
# Detta gör att Docker inte behöver ladda ner alla bibliotek igen om du bara ändrar i din källkod.
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# 2. Ge kör-rättigheter till gradlew och hämta beroenden
RUN chmod +x ./gradlew
RUN ./gradlew dependencies --no-daemon

# 3. Kopiera källkoden och bygg JAR-filen
COPY src src
# Vi hoppar över tester (-x test) här för att bygget inte ska krascha pga saknad RabbitMQ/Databas under byggfasen
RUN ./gradlew bootJar --no-daemon -x test

# --- Steg 2: Kör applikationen (Runtime) ---
# Här använder vi en JRE-image som är mycket mindre eftersom vi bara ska köra koden
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# 4. Kopiera den färdiga JAR-filen från bygg-steget
# Gradle sparar oftast filen i build/libs/
COPY --from=builder /app/build/libs/*.jar app.jar

# 5. Exponera porten (Se till att denna matchar server.port i application.properties)
EXPOSE 8080

# 6. Starta applikationen
ENTRYPOINT ["java", "-jar", "app.jar"]