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
https://gist.github.com/nyakaz73/b67b3981145fc7a0f7c0038f0d77db2c
