# Cinema Seat Booking
Java + MySQL console application to view, book, and cancel seats.

## Setup
1. Put MySQL connector (mysql-connector-j.jar) into lib/ folder.
2. Run SQL script: mysql -u root -p < sql/cinema_db_setup.sql
3. Edit DB credentials in src/DBConnection.java
4. Compile:
   mkdir -p bin
   javac -cp lib/mysql-connector-j.jar -d bin src/*.java
5. Run:
   java -cp "bin:lib/mysql-connector-j.jar" CinemaBookingDB
