#!/usr/bin/env bash
mkdir -p bin
javac -cp lib/mysql-connector-j.jar -d bin src/*.java
java -cp "bin:lib/mysql-connector-j.jar" CinemaBookingDB
