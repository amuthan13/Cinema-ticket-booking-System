\
    @echo off
    if not exist bin mkdir bin
    javac -cp lib\mysql-connector-j.jar -d bin src\*.java
    java -cp "bin;lib\mysql-connector-j.jar" CinemaBookingDB
