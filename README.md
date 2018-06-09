[![Build Status](https://travis-ci.org/mkralik3/pv243-ParserManager.svg?branch=master)](https://travis-ci.org/mkralik3/pv243-ParserManager)

## Parser Manager project for PV243 course

### How to run

#### Preapare wildfly

##### Install keycloak adapter

- Download keycloak wildfly adapter zip file from [webpage](https://www.keycloak.org/downloads.html) (Tested with version 4.0.0.Beta3)
- Unzip adapter into JBOSS_HOME directory
- Run command:
````
cd $JBOSS_HOME/bin
./jboss-cli.sh --file=adapter-install-offline.cli -Dserver.config=standalone-full.xml

````

if you are using older version of keycloak adapter -Dserver.config property won't work so you need to specify standalone-full.xml within cli file

##### Add jms-queue inside standalone-full.xml

````
<jms-queue name="ChangedParsersQueue" entries="java:jboss/exported/jms/queue/ChangedParsersQueue"/>

````

#### Run wildfly

````
standalone.sh -c standalone-full.xml
````

#### Deploy project:

Go to project directory and run:

````
mvn clean install wildfly:deploy
````

At that moment webpage should be accessible on http://localhost:8080/ParserManager-react and you should be prompted with login form from keycloak