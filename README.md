
Documenting my issues getting errai to generate marshallers when two entry point modules exist,
and they depend on two different modules containing jax-rs services.


To launch the GWT development mode, change into the project directory (name
corresponding to the provided artifactId) and type:

    mvn gwt:run (launch hosted mode)
    mvn gwt:debug (launch hosted with debug settings)

The example application comes with an integration test suite that exercises
most of its client-side and server-side code. To run the test suite, type:

    mvn test -Pintegration-test

To generate a set of HTML documents under target/site/jacoco/ detailing code
coverage of the most recent test run, type:

    mvn site



By default the archetype does package the web application for Development Mode
execution. To deploy your application to JBoss AS 7, you need to execute a
clean rebuild using the JBoss profile (e.g. -Pjboss7).

    mvn -Pjboss7 clean install
    cp target/gwt-app.war $JBOSS_HOME/standalone/deployments
