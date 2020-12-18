# Poc SonarQube

## What is SonarQube? 

Helps to maintain certain code level in a development team, this tool does code analysis (Vulnerabilities, Code Standard, Bugs, Code Coverage) 
and get metrics from that analysis. This tool can be implemented in an scenario  when multiple teams works in the  same project and all of them do several changes over the same code,
this type of scenario with the implementation of this tool  helps to maintain code conventions in order to achieve a good code quality standard and increase the code maintenance 
in the future.


### Features of SonarQube
- This tool can be implemented in several code languages such as (Java, C#, Python, Javascript and many others) 
- Metrics 
  - Unit Test Code Coverage
  - Code Smell 
    - Duplicate Code 
    - Large Code Methods (Bad design)
    - Method or Function with many parameters
    - Variables with a large or too short identifier   
  - Vulnerabities 
  - Bugs
  - CD\CI Integration (Not with the community Edition)
    
Rules for several languages that are supported by this tool 

## Comparison Community Version vs Developer Version

| Community   |      Developer      |
|----------|:-------------:|
| SonarQube & 60+ plugins | Analysis of Branches and Pull Requests |
| SonarLint|  SonarLint notifications      |
| 15 languages | 22 languages |
| One branch analisys (Master or Main)| Multiple Branches |

### Price
- **Community** - Free 
- **Developer** - Is charged by lines of code starts with $150 of 100,000 lines

## How implemented it? 

This sample I'm going to explain it how to implement SonarQube Community in a Java Solution, using 
Springboot and gradle. Running SonarQube locally using a docker image.

### Step 1: 
You need to run this docker-compose file : 

**Observation**: If you are running docker over windows you must have to run this before to execute the docker compose file

1. wsl -d docker-desktop
2. sysctl -w vm.max_map_count=262144


This is the docker-compose.yaml that I used to implement this tool:

```
version: "3"

services:
  sonarqube:
    image: sonarqube
    expose:
      - 9000
    ports:
      - "127.0.0.1:9000:9000"
    networks:
      - sonarnet
    environment:
      - SONARQUBE_JDBC_URL=jdbc:postgresql://db:5432/sonar
      - SONARQUBE_JDBC_USERNAME=sonar
      - SONARQUBE_JDBC_PASSWORD=sonar
    volumes:
      - sonarqube_conf:/opt/sonarqube/conf
      - sonarqube_data:/opt/sonarqube/data
      - sonarqube_extensions:/opt/sonarqube/extensions
      - sonarqube_bundled-plugins:/opt/sonarqube/lib/bundled-plugins

  db:
    image: postgres
    networks:
      - sonarnet
    environment:
      - POSTGRES_USER=sonar
      - POSTGRES_PASSWORD=sonar
    volumes:
      - postgresql:/var/lib/postgresql
      - postgresql_data:/var/lib/postgresql/data

networks:
  sonarnet:

volumes:
  sonarqube_conf:
  sonarqube_data:
  sonarqube_extensions:
  sonarqube_bundled-plugins:
  postgresql:
  postgresql_data:
```

### Step 2
This is the *build.gradle* file configuration:

#### Step 2.1 
Add these plugins to the configuration file 

```
	id "org.sonarqube" version "3.0"
	id 'jacoco'
```

#### Step 2.2
Apply these plugins 

```
apply plugin: 'org.sonarqube'
apply plugin: 'jacoco'
```

#### Step 2.3 
Use this test implementation libraries

```
    testImplementation 'junit:junit:4.12'
	testImplementation ('org.springframework.boot:spring-boot-starter-test:2.3.5.RELEASE') {
		exclude(module: 'junit-vintage-engine')
	}
```

#### Step 2.4 
Configure Jacoco in order to export this data to SonarQube

```
jacoco {
	toolVersion = "0.7.1.201405082137"
	reportsDir = file("${project.projectDir}/build/reports/")
}

jacocoTestReport {
	reports {
		html.enabled true
		xml.enabled true
		html.destination file("${project.projectDir}/build/reports/jacoco/")
		xml.destination file("${project.projectDir}/build/reports/test-coverage.xml")
	}
}

test {
	jacoco {
		destinationFile = file("${project.projectDir}/build/jacoco/test-coverage.exec")
	}
}
```

**Extra:** 
If you want to add test coverage verification before to send this data to SonarQube 

```
jacocoTestCoverageVerification {
	violationRules {
		rule {
			limit {
				minimum = 0.9
			}
		}

		rule {
			enabled = false
			element = 'CLASS'
			includes = ['org.gradle.*']

			limit {
				counter = 'LINE'
				value = 'TOTALCOUNT'
				maximum = 0.3
			}
		}
	}
}
```

#### Step 2.5
Set the configuration for SonarQube
```
sonarqube {
	properties {
		property "sonar.projectName", "POC SonarQubePoc"
		property "sonar.projectKey", "org.sonarqube:java-gradle-simple"
		property "sonar.projectVersion", "1.0"
		property "sonar.host.url", "http://localhost:9000"
		property "sonar.login", "admin"
		property "sonar.password", "admin2020"
		property "sonar.language", "java"
		property "sonar.sources", "src/main/java"
		property "sonar.tests", "src/test/java"
		property "sonar.dynamicAnalysis", "reuseReports"
		property "sonar.java.coveragePlugin", "jacoco"
		property "sonar.coverage.jacoco.xmlReportPaths", "build/reports/test-coverage.xml"
		property "sonar.scm.disabled", "true"
	}
}

tasks.named("sonarqube") {
	dependsOn(tasks.named("jacocoTestCoverageVerification"))
	dependsOn(tasks.named("jacocoTestReport"))
}
```

### Final gradle.build configuration file 

```
plugins {
	id 'org.springframework.boot' version '2.3.5.RELEASE'
	id 'io.spring.dependency-management' version '1.0.10.RELEASE'
	id 'java'
	id "org.sonarqube" version "3.0"
	id 'jacoco'
}

apply plugin: 'java'
apply plugin: 'org.sonarqube'
apply plugin: 'jacoco'

group = 'com.SonarQubePoc'
version = '0.0.1-SNAPSHOT'

repositories {
	mavenCentral()
}

dependencies {
	implementation 'org.springframework.boot:spring-boot-starter:2.3.5.RELEASE'
	implementation 'org.springframework.boot:spring-boot-starter-data-jpa:2.3.5.RELEASE'
	implementation 'org.springframework.boot:spring-boot-starter-web:2.3.5.RELEASE'
	implementation 'org.hibernate:hibernate-core:5.2.16.Final'
	implementation 'com.h2database:h2:1.4.200'
	implementation 'mysql:mysql-connector-java'
	testImplementation 'junit:junit:4.12'
	testImplementation ('org.springframework.boot:spring-boot-starter-test:2.3.5.RELEASE') {
		exclude(module: 'junit-vintage-engine')
	}
}

jacoco {
	toolVersion = "0.7.1.201405082137"
	reportsDir = file("${project.projectDir}/build/reports/")
}

jacocoTestCoverageVerification {
	violationRules {
		rule {
			limit {
				minimum = 0.9
			}
		}

		rule {
			enabled = false
			element = 'CLASS'
			includes = ['org.gradle.*']

			limit {
				counter = 'LINE'
				value = 'TOTALCOUNT'
				maximum = 0.3
			}
		}
	}
}

jacocoTestReport {
	reports {
		html.enabled true
		xml.enabled true
		html.destination file("${project.projectDir}/build/reports/jacoco/")
		xml.destination file("${project.projectDir}/build/reports/test-coverage.xml")
	}
}

test {
	jacoco {
		destinationFile = file("${project.projectDir}/build/jacoco/test-coverage.exec")
	}
}

sonarqube {
	properties {
		property "sonar.projectName", "POC SonarQubePoc"
		property "sonar.projectKey", "org.sonarqube:java-gradle-simple"
		property "sonar.projectVersion", "1.0"
		property "sonar.host.url", "http://localhost:9000"
		property "sonar.login", "admin"
		property "sonar.password", "admin2020"
		property "sonar.language", "java"
		property "sonar.sources", "src/main/java"
		property "sonar.tests", "src/test/java"
		property "sonar.dynamicAnalysis", "reuseReports"
		property "sonar.java.coveragePlugin", "jacoco"
		property "sonar.coverage.jacoco.xmlReportPaths", "build/reports/test-coverage.xml"
		property "sonar.scm.disabled", "true"
	}
}

tasks.named("sonarqube") {
	dependsOn(tasks.named("jacocoTestCoverageVerification"))
	dependsOn(tasks.named("jacocoTestReport"))
}

```
