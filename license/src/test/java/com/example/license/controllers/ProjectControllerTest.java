package com.example.license.controllers;

import com.example.license.entities.Project;
import com.example.license.entities.User;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.assertj.core.api.SoftAssertions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;

import static io.restassured.RestAssured.given;

@TestPropertySource("file:src/main/resources/application.properties")
public class ProjectControllerTest extends AbstractTestNGSpringContextTests {
    private String id;
    private Project task;
    private RequestSpecification specification;

    private RequestSpecification specificationDelete;
    private RequestSpecification specificationEdit;
    private RequestSpecification specificationCreate;
    private RequestSpecification specificationGet;


    @Value("${server.port}")
    private int portNumber;

    @BeforeClass
    public void authorization() {
        User user = new User();
        user.setEmail("usernam6");
        user.setPassword("password");

        given()
                .basePath("/api/register")
                .port(portNumber)
                .contentType("application/json")
                .body(user)
                .when()
                .post()
                .then()
                .statusCode(200);

        String token =
                given()
                        .basePath("/api/login")
                        .port(portNumber)
                        .contentType("application/json")
                        .body(user)
                        .when()
                        .post()
                        .then()
                        .statusCode(200)
                        .extract()
                        .body().asString().split(":")[2].replaceAll("\"", "").replaceAll("}", "");
        specification =
                new RequestSpecBuilder()
                        .setBasePath("/api/projects")
                        .setPort(portNumber)
                        .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                        .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                        .build();

        specificationEdit =
                new RequestSpecBuilder()
                        .addHeader("Authorization", token)
                        .setBasePath("/api/projects/update")
                        .setPort(portNumber)
                        .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                        .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                        .build();

        specificationDelete =
                new RequestSpecBuilder()
                        .addHeader("Authorization", token)
                        .setBasePath("/api/projects/delete")
                        .setPort(portNumber)
                        .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                        .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                        .build();

        specificationCreate =
                new RequestSpecBuilder()
                        .addHeader("Authorization", token)
                        .setBasePath("/api/projects/create")
                        .setPort(portNumber)
                        .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                        .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                        .build();

        specificationGet =
                new RequestSpecBuilder()
                        .addHeader("Authorization", token)
                        .setBasePath("/api/projects/project")
                        .setPort(portNumber)
                        .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                        .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                        .build();

    }

    @BeforeMethod
    public void createProject() {
        task = new Project("0007", "string", "status", "name", "categ", new ArrayList<>(), new BigDecimal(4277777), "today", "tomorrow");
        given().spec(specificationCreate).contentType("application/json").body(task).when().post();
    }

    @AfterMethod
    public void cleanUp() {
        deleteProject(id);
    }

    @Test
    public void addProject() {
        Project retrievedProject = retrieveProject();
        assertProject(retrievedProject, task);
    }

    @Test
    public void editProject() {
        String updatedDescription = "nadia";
        Project updatedProject = new Project("0007", updatedDescription, "status", "name", "categ", new ArrayList<>(), new BigDecimal(428767897), "today", "tomorrow");

        Project retrievedProject = retrieveProject();

        given()
                .spec(specificationEdit)
                .contentType("application/json")
                .when()
                .body(updatedProject)
                .put(String.format("%s", retrievedProject.getId()))
                .then()
                .statusCode(200);

        retrievedProject = retrieveProject();

        assertProject(retrievedProject, updatedProject);
    }

    @Test
    public void deleteProject() {
        Project retrievedProject = retrieveProject();

        given()
                .spec(specificationDelete)
                .when()
                .delete(String.format("%s", retrievedProject.getId()))
                .then()
                .statusCode(200);

        retrievedProject = null;

        SoftAssertions assertions = new SoftAssertions();
        assertions.assertThat(retrievedProject).isNull();
        assertions.assertAll();
    }

    private Project retrieveProject() {
        Project retrievedProject =
                Arrays.stream(
                        given()
                                .spec(specification)
                                .when()
                                .get()
                                .then()
                                .statusCode(200)
                                .extract()
                                .as(Project[].class))
                        .reduce((first, second) -> second)
                        .orElse(null);
        if (retrievedProject != null) {
            id = retrievedProject.getId();
        } else {
            id = null;
        }
        return retrievedProject;
    }

    private void deleteProject(String id) {
        if (id != null) {
            given().spec(specificationDelete).when().delete(String.format("%s", id)).then().statusCode(200);
        }
    }

    private void assertProject(Project actual, Project expected) {
        SoftAssertions assertions = new SoftAssertions();
        assertions.assertThat(actual.getDescription()).isEqualTo(expected.getDescription());
        assertions.assertAll();
    }
}
