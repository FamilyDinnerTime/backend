import org.jmailen.gradle.kotlinter.tasks.LintTask
import org.jooq.meta.jaxb.SchemaMappingType

plugins {
	kotlin("jvm") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
	id("org.springframework.boot") version "3.5.3"
	id("io.spring.dependency-management") version "1.1.7"

	id("nu.studer.jooq") version "5.2.1"

	id("org.jmailen.kotlinter") version "3.3.0"
}

group = "ru.kreslavski.family"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

repositories {
	mavenCentral()
}

jooq {
	version.set("3.19.7")

	val snakeProjectName = project.name.replace('-', '_')

	configurations {
		create("xml") {
			generateSchemaSourceOnCompilation.set(false)

			jooqConfiguration.apply {
				jdbc.apply {
					driver = "org.postgresql.Driver"
					url = "${project.properties.getOrDefault("dbUrl", "jdbc:postgresql://localhost:5402/dinner")}"
					user = "${project.properties.getOrDefault("dbUser", "postgres")}"
					password = "${project.properties.getOrDefault("dbPassword", "postgres")}"
				}
				generator.apply {
					name = "org.jooq.codegen.XMLGenerator"
					database.apply {
						name = "org.jooq.meta.postgres.PostgresDatabase"
						schemata.addAll(
							listOf(
								SchemaMappingType().withInputSchema("dinner"),
								SchemaMappingType().withInputSchema("dictionaries"),
								SchemaMappingType().withInputSchema("auth"),
								SchemaMappingType().withInputSchema("social"),
							)
						)
						includes = ".*"
					}
					generate.apply {
						isDeprecated = false
						isRecords = true
						isImmutablePojos = true
						isFluentSetters = true
					}
					target.apply {
						directory = "src/generated/jooq"
					}
					strategy.name = "org.jooq.codegen.DefaultGeneratorStrategy"
				}
			}
		}

		create("java") {
			generateSchemaSourceOnCompilation.set(false)

			jooqConfiguration.apply {
				generator.apply {
					database.apply {
						name = "org.jooq.meta.xml.XMLDatabase"
						properties.apply {
							add(org.jooq.meta.jaxb.Property().withKey("dialect").withValue("POSTGRES"))
							add(org.jooq.meta.jaxb.Property().withKey("xmlFile").withValue("src/generated/jooq/org/jooq/generated/information_schema.xml"))
						}
					}
					generate.apply {
						isRelations = true
						isDeprecated = false
						isRecords = true
						isPojos = true
						isFluentSetters = true
						isJavaTimeTypes = true
					}
					target.apply {
						packageName = "ru.kreslavski.family.dinnertime.jooq"
						directory = "src/main/java"
					}
				}
			}
		}

		for (configuration in configurations) {
			configuration.jooqConfiguration.logging = org.jooq.meta.jaxb.Logging.WARN
		}
	}
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-jooq")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("io.jsonwebtoken:jjwt-api:0.12.3")
	runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.3")
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.3")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0")

	val postgresDriverVersion = "42.2.16"
	runtimeOnly("org.postgresql:postgresql:$postgresDriverVersion")
	jooqGenerator("org.postgresql:postgresql:$postgresDriverVersion")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

kotlinter {
	ignoreFailures = false
	indentSize = 4
	reporters = arrayOf("checkstyle", "plain")
	experimentalRules = false
	disabledRules = emptyArray()
}


tasks.getByName<LintTask>("lintKotlinMain") {
	exclude { it.file.startsWith(buildDir) }
}

