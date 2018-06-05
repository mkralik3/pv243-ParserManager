## Parser Manager project for PV243 course

### Run wildfly:

add 

````
<jms-queue name="ChangedParsersQueue" entries="java:jboss/exported/jms/queue/ChangedParsersQueue"/>

````
to standalone-full-ha.xml

````
standalone.sh -c standalone-full-ha.xml
````

### How to deploy:

Run wildfly and run command:

````
mvn wildfly:deploy
````
