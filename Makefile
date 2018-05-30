SWARM_JAR='rest/target/ParserManager.jar'

.PHONY: build
build:
	mvn package -DskipTests

.PHONY: run
run:
	java rest/target/*.war

.PHONY: open
open:
	-(sleep 8 && xdg-open http://localhost:8081 &> /dev/null)&
