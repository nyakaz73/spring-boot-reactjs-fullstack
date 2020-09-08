# Spring boot and React js fullstack application.
How to create a full stack application with Spring boot and React js, with web-pack and babel

I like explaining things in code so without wasting much of your time lets just jump straight in and i will be explaining as we go.

## 1. Spring Boot  (Initializer)
To start off with you can use Spring Initializr  to get the Spring Boot project structure for you, and this can be found [here](https://start.spring.io/)

Once you get there in this case im using Maven , and that's my personal preference over Gradle since it gives a nice xml layout for your setup , in terms of installing dependency , plugins etc. its essentially a tool for your project management, just like package.json for those who are familiar with node js.

You also need to add a couple of dependencies which are
* JPA - Data persistence in SQL stores with Java Persistence API
* thymeleaf - A modern server-side Java template engine
* WEB - Build web, including RESTful, applications using Spring MVC
* H2 - Provides a volatile in-memory database
* Lombok - Java annotation library which helps reduce boilierplate code

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
