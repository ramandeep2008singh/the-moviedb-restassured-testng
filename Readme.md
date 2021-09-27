# The Movie DB REST API automation project
This project contains positive and negative tests with Extent reports

Project is built with:

* Java
* Maven
* TestNG
* RestAssured library
* Extent Reporter

## Prerequisites:
* Maven
* Git
* Java
* Enter Key & Token in the ApiConstants.java file:
````API_KEY````
````ACCESS_BEARER_TOKEN ````

Note: To generate the Access Token and to provide the write access, follow this link:
https://developers.themoviedb.org/4/auth/user-authorization-1

## Usage:
````
git clone git@github.com:ramandeep2008singh/the-moviedb-restassured-testng.git
cd restApi-automation-project-takeaway

right click on testng.xml file and click on run
OR
mvn clean test -DsuiteXmlFile=testng.xml
````

# Execution Command
````
mvn clean test -DsuiteXmlFile=testng.xml
````
