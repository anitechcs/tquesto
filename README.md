# tQuesto [![Build Status](https://travis-ci.org/anitechcs/tquesto.svg?branch=master)](https://travis-ci.org/anitechcs/tquesto)

> Drop in analytics for your web/mobile application

![](https://github.com/anitechcs/tquesto-dashboard/blob/master/src/assets/imgs/logo-blue.png)

tQuesto makes it easy to integrate a powerfull analytics system to your application with absolute minimum fuss. It is completely free to use/customize and always it will be.

Our primary goals are:

* Provide radically faster and widely accessible API's for your application

* 100% customizable events in your application context

* Beautiful dashboard with lots of graphs and visualization

* Silk and smooth mobile application to see your data on finger tip

* Absolutely no cost, True open source project


## Installation and Getting started

* Clone the source code from `git@github.com:anitechcs/tquesto.git`
* CD into project directory `cd tquesto`
* Run `mvn spring-boot:run` to start the application
* Open the browser and access the applicatin at `http://localhost:8080`
* You can access Swagger Services docs at `http://localhost:8080/swagger-ui.html`


## Useful Maven Commands

* `mvn spring-boot:run` - To run the application
* `mvn package` - To prepare the build
* `mvn clean` - To clean the old build artifacts
* `mvn install` - To download the libraries mentioned in pom.xml
* `mvn test` - To run unit test cases

## Run in Docker Container
 `mvn clean package -DskipTests docker:stop docker:build docker:run`
 
