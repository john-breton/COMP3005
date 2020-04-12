# COMP3005
Created for the COMP 3005 course at Carleton University. Winter 2020.

## LookInnaBook
LookInnaBook is a book store application. Users can register to gain access to the bookstore. Normal users can search, add books to their cart, and place orders. Admins can access additional options. These options include the ability to add and remove books, users, and publishers. They also include the ability to edit a user's order status, the user's information itself, or a book's information. Additionally, they can also generate and view sales reports. 

## Tools

- IDE: [IntelliJ IDEA](https://www.jetbrains.com/idea/download/)
- JDK Version: [13](https://www.oracle.com/java/technologies/javase-jdk13-downloads.html)
- Build Tool: [Maven](https://maven.apache.org/download.cgi)

## Getting Started

1. From your terminal, run:
   ```
   git clone https://github.com/johnbreton/COMP3005
   ```
   This will create a folder called COMP3005.
    
2. IntelliJ IDEA
    1. Open the IntelliJ IDEA IDE, and click File -> Open.
    2. Select the COMP3005 folder that was just created and click Open.

3. The entry point of the program is the main method of the LookInnaBook class under the startup package. However, it is suggested that a database is first created within IntelliJ. Create a new postgresSQL database by navigating to the '+' on the Database tab, located on the right of the IDE. From there create a database called "lookinnabook". Use the username 'postgres' and leave the password field blank. Once this is done, run the UploadBookData class, located in the startup package. This will fill the database with dummy data. Afterwards, run the LookInnaBook class to begin the application.

4. The project can be built by invoking a `mvn package` command from the project's root directory. This will generate a
runnable jar file in the target directory. Please note that this .jar may have trouble connection to the database. To correct this, locate the USER and DATABASE fields in the classes DatabaseQueries and Reports (both located in the backend package). Update these fields to reflect the information of the external database that should be connected to. The local host might also need to be updated to reference an externally hosted address. This can be found on line 29 of the DatabaseQueries class and line 21 of the Reports class.

## Current Database Schema
<p style="text-align:right">
<img src="documentation/DB Schema/DB Schema - Project - COMP3005.png" alt="DB Schema">
</p>

## Current ER Diagram
<p style="text-align:right">
<img src="documentation/ER Diagram/ER Diagram - Project - COMP3005.png" alt="ER Diagram">
</p>
