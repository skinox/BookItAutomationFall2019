package com.bookit.utilities;

import io.restassured.response.Response;

import static io.restassured.RestAssured.*;

public class APIUtilities {

    /**
     * static block used to initialize static variables.
     * has highest priority in the execution order:
     *  1. static block
     *  2. instance block
     *  3. constructor
     *  4. method
     */
    static {
        baseURI = Environment.BASE_URI;
    }

    /**
     * This method is used to retrieve token from the server.
     * Token bust be attached to the header of every API call
     *
     * @return token
     */
    public static String getToken() {
        Response response = given().
                queryParam("email", Environment.LEADER_USERNAME).
                queryParam("password", Environment.LEADER_PASSWORD).
                when().
                get("/sign");
        response.then().log().ifError();//if request failed, print response information
        String token = response.jsonPath().getString("accessToken");
        System.out.println("TOKEN :: " + token);
        return token;
    }

    /**
     * This method is used to retrieve authorization token from the server for specific role.
     * Token bust be attached to the header of every API call
     *
     * @return token
     */
    public static String getToken(String role) {
        String email = null;
        String password = null;

        if (role.toLowerCase().contains("teacher")) {
            email = Environment.TEACHER_USERNAME;
            password = Environment.TEACHER_PASSWORD;
        } else if (role.toLowerCase().contains("lead")) {
            email = Environment.LEADER_USERNAME;
            password = Environment.LEADER_PASSWORD;
        } else {
            email = Environment.MEMBER_USERNAME;
            password = Environment.MEMBER_PASSWORD;
        }

        Response response = given().
                queryParam("email", email).
                queryParam("password", password).
                when().
                get("/sign");
        response.then().log().ifError();//if request failed, print response information
        String token = response.jsonPath().getString("accessToken");
        System.out.println("TOKEN :: " + token);
        return token;
    }

    /**
     * This method is used to retrieve token from the server.
     * Token bust be attached to the header of every API call
     *
     * @param email    - email of the user
     * @param password - password the user
     * @return token
     */
    public static String getToken(String email, String password) {
        Response response = given().
                queryParam("email", email).
                queryParam("password", password).
                when().
                get("/sign");
        response.then().log().ifError();//if request failed, print response information
        String token = response.jsonPath().getString("accessToken");
        System.out.println("TOKEN :: " + token);
        return token;
    }

    /**
     * This method returns id of logged in user
     * {
     * "id": integer,
     * "firstName": "string",
     * "lastName": "string",
     * "role": "string"
     * }
     * User credentials
     *
     * @param email
     * @param password
     * @return user id, return -1 if user doesn't exist
     */
    public static int getUserID(String email, String password) {
        try {
            String token = getToken(email, password);
            Response response = given().auth().oauth2(token).when().get(Endpoints.GET_ME);
            response.then().log().ifError();//print response details in case of error
            response.then().statusCode(200);//ensure that it returns 200 status code
            return response.jsonPath().getInt("id");
        } catch (Exception e) {
            System.out.println("USER DOESN'T EXISTS!");
            System.out.println(e.getMessage());
        }
        return -1;
    }

    /**
     * This method deletes user based on id
     *
     * @param id of the user to delete
     * @return response object
     */
    public static Response deleteUserByID(int id) {
        String token = getToken("teacher");
        Response response = given().auth().oauth2(token).when().delete(Endpoints.DELETE_STUDENT, id);
        response.then().log().ifError();
        System.out.printf("User with id %s was deleted!", id);
        return response;
    }

    /**
     * Use this method to add new batch to the system
     *
     * @param batchNumber to add
     * @return response object
     */
    public static Response addBatch(int batchNumber) {
        String token = getToken("teacher");

        Response response = given().
                auth().oauth2(token).
                queryParam("batch-number", batchNumber).
                post(Endpoints.ADD_BATCH);
        response.then().log().ifError();
        return response;
    }

    /**
     * Use this method to add new team
     *
     * @param teamName    must be unique within specific batch number
     * @param location    VA or IL
     * @param batchNumber any number that already exist
     * @return response object
     */
    public static Response addTeam(String teamName, String location, int batchNumber) {
        String token = getToken("teacher");
        Response response = given().
                auth().oauth2(token).
                queryParam("team-name", teamName).
                queryParam("campus-location", location).
                queryParam("batch-number", batchNumber).
                when().
                post(Endpoints.ADD_TEAM);
        response.then().log().ifError();
        return response;
    }

    /**
     * Use this method before creating new user.
     * If user exists - this method will that user
     * @param email
     * @param password
     */
    public static void ensureUserDoesntExist(String email, String password) {
        int userID = getUserID(email, password);
        //condition is true if userID is a positive value
        //there is no users with 0 or negative id
        if (userID > 0) {
            deleteUserByID(userID);
        }
    }
}