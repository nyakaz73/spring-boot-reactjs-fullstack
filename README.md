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