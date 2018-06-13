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
./jboss-cli.sh --file=adapter-install-offline.cli -Dserver.config=standalone-full-ha.xml

````

if you are using older version of keycloak adapter -Dserver.config property won't work so you need to specify standalone-full.xml within cli file

##### Add jms-topic inside standalone-full-ha.xml

````
<jms-topic name="ChangedParsersTopic" entries="java:jboss/exported/jms/topic/ChangedParsersTopic"/>

````

+ Change default password from CHANGE ME!! to something else in acivemq subsystem in <cluster element

#### Datasource used for testing

````
<datasource jndi-name="java:jboss/datasources/ExampleDS" pool-name="ExampleDS" enabled="true" use-java-context="true">
    <connection-url>jdbc:h2:/tmp/ParserManager;AUTO_SERVER=TRUE;DB_CLOSE_ON_EXIT=FALSE</connection-url>
    <driver>h2</driver>
    <security>
        <user-name>sa</user-name>
        <password>sa</password>
    </security>
</datasource>
````

#### Run wildfly

````
./standalone.sh -c standalone-full-ha.xml
````

#### Deploy project:

Go to project directory and run:

````
mvn clean install wildfly:deploy
````

At that moment webpage should be accessible on http://localhost:8080/ParserManager-react and you should be prompted with login form from keycloak