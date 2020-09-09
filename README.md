# Spring boot and React js fullstack application Part(1/2).
Full stack application with Spring boot and React js, with **WEBPACK & BABEL. JUNIT Tests , RESTful API.**

### Show some :heart: and :star: the repo to support the project 

I like explaining things in code so without wasting much of your time lets just jump straight in and i will be explaining as we go.

## 1. Spring Boot  (Initializer)
To start off with you can use Spring Initializr  to get the Spring Boot project structure for you, and this can be found [here](https://start.spring.io/)

Once you get there in this case im using Maven , and that's my personal preference over Gradle since it gives a nice xml layout for your setup , in terms of installing dependency , plugins etc. its essentially a tool for your project management, just like package.json for those who are familiar with node js.

You also need to add a couple of dependencies which are
* JPA - Data persistence in SQL stores with Java Persistence API
* thymeleaf - A modern server-side Java template engine
* WEB - Build web, including RESTful, applications using Spring MVC
* H2 - Provides a volatile in-memory database
* Lombok - Java annotation library which helps reduce boilerplate code

## 2. Rest API Service
Now lets create the REST API service for the backend application which will be able to perform basic **CRUD** (Create, Read, Update ,Delete) functionality.

### a. models
First create a package name models. 
Inside models create class User for your user model. See code below

```java

package com.datsystemz.nyakaz.springbootreactjsfullstack.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String surname;
    private String email;
    private String username;
    private String password;

    protected User(){}

    public User(String name, String surname, String email, String username, String password){
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public Long getId(){
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}


```

First you will notice the @Entity annotation- this tell Spring that it is a JPA entity and it is mapped to table named User. If you want to the the table name you will have to annotate it with @Table(name = "new_table_name_here")
You probably noticed that this is too much of code to create an entity not to worry, Remember the Lambok dependancy this is the best time to use it since it offers a functionality of writing code with less boiler plate of code.

```java
package com.datsystemz.nyakaz.springbootreactjsfullstack.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String surname;
    private String email;
    private String username;
    private String password;


}
```
In the above snippet you will notice we have added a couple of annotations
@Data - is a convenient shortcut that bundles features of @toString, @EqualsAndHashCode, @Getter / @Setter and @RequiredArgsConstructor .
@NoArgsConstructor provides the  default construct
@AllArgsConstructor bundles the non default constructor.

### b. repositories
Lets create a repositories package will an interface called UserRepository. The interface extends the Jpa repository so that we can have all methods that is provided by the JpaRepository that allows us to  query our database.
```java
package com.datsystemz.nyakaz.springbootreactjsfullstack.repositories;

import com.datsystemz.nyakaz.springbootreactjsfullstack.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
}

```
### c. controllers

Now that you got you repository setup its time to create a controllers package and with a class called UserController. 
The is where the REST service with Spring begins. The controller will save all end points of our user controller, which will then be consumed by the outside world.
All the action feature required by our CRUD app are going to seat here ie GET, POST, PUT and DELETE actions. See code below

```java
package com.datsystemz.nyakaz.springbootreactjsfullstack.controllers;

import com.datsystemz.nyakaz.springbootreactjsfullstack.exceptions.ResourceNotFoundException;
import com.datsystemz.nyakaz.springbootreactjsfullstack.models.User;
import com.datsystemz.nyakaz.springbootreactjsfullstack.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    private UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @PostMapping("/user/save")
    public User saveUser(@RequestBody User user){
        return this.userRepository.save(user);
    }

    @GetMapping("/user/all")
    public ResponseEntity<List<User>> getUsers(){
        return ResponseEntity.ok(
          this.userRepository.findAll()
        );
    }
    
    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUser(@PathVariable(value = "id" ) Long id){
        User user = this.userRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("User not found")
        );

        return  ResponseEntity.ok().body(user);
    }
    
    @PutMapping("user/{id}")
    public User updateUser(@RequestBody User newUser, @PathVariable(value = "id") Long id){
        return this.userRepository.findById(id)
                .map(user -> {
                    user.setName(newUser.getName());
                    user.setSurname(newUser.getSurname());
                    user.setEmail(newUser.getEmail());
                    user.setUsername(newUser.getUsername());
                    user.setPassword(newUser.getPassword());
                    return this.userRepository.save(user);
                })
                .orElseGet(()->{
                   newUser.setId(id);
                   return this.userRepository.save(newUser);
                });
    }

    @DeleteMapping("user/{id}")
    public ResponseEntity<Void> removeUser(@PathVariable(value = "id") Long id){
        User user =this.userRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("User not found"+id)
        );

        this.userRepository.delete(user);
        return ResponseEntity.ok().build();
    }




}

```
In the above code the class is annotated with @RestController telling Spring that the data returned by each method will be written straight into the response  body instead of rendering a template.
The UserRepository is injected by constructor into the controller. The @Autowired enable automatic dependency injection.

The @PostMapping , @GetMapping, @PutMapping and @DeleteMapping corresponds to the POST, GET, UPDATE and DELETE actions. 
One thing to note here is the @DeleteMapping and @GetMapping which is calling a ResourceNotFoundException class that will output the runtime exception.

Lets quickly implement the ResourceNotFoundException class. 

### d. exceptions
I like to keep my code clean and packaged. So just quickly create a package called exceptions with a class ResourceNotFoundException. See code below:

```java
package com.datsystemz.nyakaz.springbootreactjsfullstack.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value =  HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException{
    public  ResourceNotFoundException(String message){
        super(message);
    }
}

```
The above class extends the RuntimeException which will give us access to all methods that are in that class. In this case just call super in the constructor with  message variable. 
That's it .,now whenever a user is not found it will return the run time exception with user not Found message and status of HttpStatus.NOT_FOUND.
### e. database

* **NB** Since we have added the H2 in -memory DB . Spring automatically runs it whenever there is persistence to the db. Please note that H2 Database is volatile meaning it will be automatically be created when the server ran and whenever you stop the server it will tear down the db and its contents.
By default, Spring Boot configures the application to connect to an in-memory store with the username sa and an empty password. However, we can change those parameters by adding the following properties to the application.properties file:

```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
# Enabling H2 Console
spring.h2.console.enabled=true

# Custom H2 Console URL
spring.h2.console.path=/h2
# Whether to enable remote access.
spring.h2.console.settings.web-allow-others=true

server.error.include-stacktrace=never
```
### f. REST API TESTING(JPA UNIT TESTING)
Now that everything is setup its time for the truth to be reviewed. I like to do things differently so instead of testing our API using Postman im going to do JPA Unit testing.
If you are familiar with JPA Unit Testing feel free to test with Postman or jump to next section **(INTEGRATING REACTJS)**

Now to get things started quickly install JPA Unit testing dependency in your pom.xml file.

```xml

<dependency>
    <groupId>junit</groupId>
    <artifactId>junit</artifactId>
    <scope>test</scope>
</dependency>
```
In your **test/java/com.<application-name>/** create a file class UserTests with the following testing code:

```java
package com.datsystemz.nyakaz.springbootreactjsfullstack;

import com.datsystemz.nyakaz.springbootreactjsfullstack.models.User;
import com.datsystemz.nyakaz.springbootreactjsfullstack.repositories.UserRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


@DataJpaTest
public class UserTests {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testSaveUser(){
        User user = new User("John","Doe","john.doe@email.com","johhny","strong-password");
        userRepository.save(user);
        userRepository.findById(new Long(1))
                .map(newUser ->{
                    Assert.assertEquals("John",newUser.getName());
                    return true;
                });
    }

    @Test
    public void getUser(){
        User user = new User("John","Doe","john.doe@email.com","johhny","strong-password");
        User user2 = new User("Daniel","Marcus","daniel@daniel.com","danie","super_strong_password");
        userRepository.save(user);

        userRepository.save(user2);

        userRepository.findById(new Long(1))
                .map(newUser ->{
                   Assert.assertEquals("danie",newUser.getUsername());
                   return true;
                });

    }

    @Test
    public void getUsers(){
        User user = new User("John","Doe","john.doe@email.com","johhny","strong-password");
        User user2 = new User("Daniel","Marcus","daniel@daniel.com","danie","super_strong_password");
        userRepository.save(user);
        userRepository.save(user2);

        Assert.assertNotNull(userRepository.findAll());
    }

    @Test
    public void deleteUser(){
        User user = new User("John","Doe","john.doe@email.com","johhny","strong-password");
        userRepository.save(user);
        userRepository.delete(user);
        Assert.assertTrue(userRepository.findAll().isEmpty());
    }


}
```
The @DataJpaTest tells Spring to test the persistence layer components that will autoconfigure in-memory embedded  H2 database and scan for @Entity classes and Spring Data JPA repositories.
We need to autowire the UserRepository so that we will have functions provided by the JPA to be able to query our DB.
Spring will look for function annotated with @Test. The JUNIT ASSERT i going to assert most of the tests.

In your terminal You can now run :

```cmd
mvn test
```
If everything runs well you should have an output similar to this

```
[INFO] 
[INFO] Results:
[INFO] 
[INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 11.845 s
[INFO] Finished at: 2020-09-08T19:01:25+02:00
[INFO] Final Memory: 32M/370M
[INFO] ------------------------------------------------------------------------
```
* **NB** At this point of your test runs successfully congratulations you have made it :) . Now you can jump in to the next section ie **(INTEGRATING REACTJS)** 

## 3. INTEGRATING REACTJS

Now lets jump in to the frontend of things of our fullstack application. 

In this section i am assuming you have already installed **nodejs** in your working environment. If not just quickly install link can be found [here](https://nodejs.org/en/)

We are not going to use npm create-react-app <app-name> to create our frontend since we dont want to separately run the react js application with its own proxy port usually port 3000.
We want our Spring backend application to be able to **serve** the front at its default port 8080, that way we get to experience the **fullstack** of things without having to separately running two instances servers for our front end and backend application.

Now you can prepare the react js project structure by creating these folders in your **root folder** of your application.

```cmd
$ mkdir -p ./frontend/src/{components,actions,reducers}
```
The above command will create a folder structure that look like this:
```cmd
frontend/
  src/
    actions/
    components/
    reducers/
  
```
This is were your react frontend application is going to seat in.

### 3a Installing packages

We now need to create a package.json file which will have all your dependencies that is going to be used by  reactjs framework. 
To create the package.json file just run
```cmd
$ npm init -y
```
Since you have already installed node this npm command should run just fine, and you now should be able to see the package.json file in your root folder of your project.

Now at this point you now need to install the following Dev Dependencies and Dependencies for your react .

```cmd
$ npm i -D webpack webpack-cli
$ npm i -D babel-loader @babel/core @babel/preset-env @babel/preset-react @babel/plugin-proposal-class-properties
$ npm i -D sass-loader css-loader
$ npm i react react-dom react-router-dom
$ npm i redux react-redux redux-thunk redux-devtools-extension
$ npm i redux-form
$ npm i axios
$ npm i lodash
```
This will install the Dev Dependencies required by react js .
* webpack - will bundle Javascript files for usage in a browser
* babel - is a transpiler ie a Javascript  compiler used to convert ECMAScript 2015+ code into a backwards compatible version of Javascript in current and older browser environments.
* react - Javascript library for building user interfaces. Developed by facebook
* redux - The is a predictable state container for Js apps
* axios - Promise based HTTP client
* lodash(optional) - Lodash is a reference library made with JavaScript.

### 3b Babel Configuration
Add a file name .babelrc to the root directory and configure babel:

```
{
  "presets": [
    [
      "@babel/preset-env",
      {
        "targets": {
          "node": "current"
        },
        "useBuiltIns": "usage",
        "corejs": 3
      }
    ],
    "@babel/preset-react"
  ],
  "plugins": ["@babel/plugin-proposal-class-properties"]
}
```


### 3b. Webpack Configuration

Add a file named webpack.config.js to the root directory  and configure webpack:

```js
var path = require('path');

module.exports = {
    entry: './src/main/js/App.js',
    devtool: 'sourcemaps',
    cache: true,
    mode: 'development',
    output: {
        path: __dirname,
        filename: './src/main/resources/static/built/bundle.js'
    },
    module: {
        rules: [
            {
                test: path.join(__dirname, '.'),
                exclude: /(node_modules)/,
                use: [{
                    loader: 'babel-loader',
                    options: {
                        presets: ["@babel/preset-env", "@babel/preset-react"]
                    }
                }]
            },
            {
                test: /\.css$/,
                use: [
                    'style-loader',
                    'css-loader'
                ]
            },
            {
                test: /\.(png|svg|jpg|gif|eot|otf|ttf|woff|woff2)$/,
                use: [
                    {
                        loader: 'url-loader',
                        options: {}
                    }
                ]
            }
        ]
    }
};
```
"""Quote Spring Docs"""
This webpack configuration file:

* Defines the entry point as ./src/main/js/App.js. In essence, App.js (a module you will write shortly) is the proverbial public static void main() of our JavaScript application. webpack must know this in order to know what to launch when the final bundle is loaded by the browser.

* Creates sourcemaps so that, when you are debugging JS code in the browser, you can link back to original source code.

* Compile ALL of the JavaScript bits into ./src/main/resources/static/built/bundle.js, which is a JavaScript equivalent to a Spring Boot uber JAR. All your custom code AND the modules pulled in by the require() calls are stuffed into this file.

* It hooks into the babel engine, using both es2015 and react presets, in order to compile ES6 React code into a format able to be run in any standard browser.

With this webpack configuration file setup we now need to create a couple of directories

### 3c. React boiler plate Setup
1. Notice how the above webpack is referencing to ./src/main/js/App.js . Let create a js folder in main and an App.js file inside 
The App.js file should have this following code:
```typescript jsx
import React, { Component } from "react";
import ReactDOM from "react-dom";


export class App extends Component {
    render() {
        return (
            <div>
                <h1>Welcome to React Front End Served by Spring Boot</h1>
            </div>
        );
    }
}

export default App;

ReactDOM.render(<App />, document.querySelector("#app"));

```
2. Now we now need to prepare the html that will render the App.js we just created in Spring

Remember the Thymeleaf dependency we installed at the beginning in the Spring Initializer it time to get that to use
To get started  you need to create an index page in **src/main/resources/templates/index.html**

```html
<!DOCTYPE html>
<html xmlns:th="https://www.thymeleaf.org">
<head lang="en">
    <meta charset="UTF-8"/>
    <title>ReactJS + Spring Data REST</title>
    <link rel="stylesheet" href="/main.css" />
</head>
<body>

    <div id="app"></div>

    <script src="built/bundle.js"></script>

</body>
</html>
```
The key part in this template is the <div id="app"></div> component in the middle. It is where you will direct React to plug in the rendered output.

The bundle.js will automatically generated when we run our application.

Spring will automatically know that the main.css file will be created under the static folder in your resources folder so go ahead and create it will use it later.

With that set up you should have the Final project structure now looking to something like this:

```cmd
- frontend
- sample-project-root
    - src
        - main
            -java
            - js
                App.js
                index.js
            - static
                main.css
            - resources
                - templates
                    index.html
 webpack.config.js
 package.json
 pom.xml
```
If this is you project string you are good to go and now left with one last step to test this out.

### 3c. Script Setup in package.json
In the package.json file we need to finally replace the script tag with the following

```json
 ...
 ...
"scripts": {
    "watch": "webpack --watch -d --output ./target/classes/static/built/bundle.js"
  },

...
...
```
This will run the webpack and tell it to render the output (bundle.js) to ./taget/classes/static/built/ folder.
The --watch tag will tell the webpack to constantly watch for changes in our code so that when there is such it will update the bundle.js file.

### 3d. Final Setup Step 
Remember when we create our UserController it had a @RestController annotation to serve the rest methods in that class.
If can still recall we said the @RestController tells Spring that the data returned by each method will be written straight into the response  body instead of rendering a template.
Now we need an endpoint that will render a template. S
So lets create a separate class in our controllers called WebMainController which will render the index page of our react App.js main component.

```java
package com.datsystemz.com.microfinancedatsystemz.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebMainController {

    @RequestMapping(value = "/")
    public String index() {
        return "index";
    }
}

```

## 4. FINAL TEST
Now lets test the full stack application
First run your server 
```cmd
$ mvn spring-boot:run
```
If there are no error you are good it means we haven't messed anything up since our last JUNIT TESTS.
Finally transpile your react app by running:
```cmd
$ npm run-script watch
```
Now you can go to you browser http://localhost:8080/ and if this appears on your browser:

# Welcome to React Front End Served by Spring Boot

Then you have successfully integrated react js and spring boot. Full stack application. If you make changes in your app webpack should be able to update those changes, and all you have to do is to restart your browser.

I know this was a long tutorial I will make it in parts and will create a Part 2 of this as soon as i can . So that you will now Learn React.

Thank you for taking your time in reading this article.

!!END

### Source Code
The source code can be found on my git repository [here](https://github.com/nyakaz73/spring-boot-reactjs-fullstack)

### Pull Requests
I Welcome and i encourage all Pull Requests

## Created and Maintained by
* Author: [Tafadzwa Lameck Nyamukapa](https://github.com/nyakaz73) :
* Email:  [tafadzwalnyamukapa@gmail.com]
* Open for any collaborations and Remote Work!!
* Happy Coding!!

### License

```
MIT License

Copyright (c) 2020 Tafadzwa Lameck Nyamukapa

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.


```
