    I'm running linux mint with the following:
        1. IntelliJ IDEA from https://www.jetbrains.com/idea/
        2. Linux packages openjdk-8-jdk, openjdk-8-jre, openjfx installed via terminal
        3. Junit 4.12.jar
        4. sqlite-jdbc-3.21.0.jar,hamcrest-core-1.3.jar(provided in project root)

    You should be able to simply import this project into Intellij and run it via /src/Main. If the import is not clean you may
    need to edit class path to include the two .jar files in the root directory. 
