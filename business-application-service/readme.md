Business App Initial Content
=============================

Your project description here.
if you get error:
[ERROR] Failed to execute goal org.springframework.boot:spring-boot-maven-plugin:2.2.10.RELEASE:run (default-cli) on project business-application-service: Could not exec java: Cannot run program "C:\Program Files\Java\jdk1.8.0_301\jre\bin\java.exe" (in directory "c:\shCode\business-application-service"): CreateProcess error=206, The filename or extension is too long -> [Help 1]

Step1>> mklink /J c:\repo c:\Users\<USERNAME>\.m2\repository

Step2>> mvn -Dmaven.repo.local=c:\repo spring-boot:run