package com.bw.test.v2;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import utils.AbstractTest;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.UrlMapping.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GoRestAPITests extends AbstractTest {

	private static String bearerAccessToken;

	@BeforeClass
	public static void restAssuredConfig() {
		//todo: move token to the secret-key on AWS or to a folder with 0600 permissions
		bearerAccessToken = "2afd68846912328567d63e3a12fa6f46407207d0f308296b9e6eeb7f6d182422";
	}

	//todo: move json to a file in folder with 0600 permissions
	@Test
	public void shouldCreateUser() {
		long currentTimestamp = System.currentTimeMillis();
		//given
		String userDataJson = "{\"name\":\"Dennis DeMenise\", \"gender\":\"male\", \"email\":\"denis_" + currentTimestamp + "@demenise.com\", \"status\":\"active\"}";

		//given
		RequestSpecification requestSpecification = given().
				header("Accept", "application/json").
				header("Content-Type", "application/json").
				header("Authorization", "Bearer " + bearerAccessToken);

		//when
		Response response = requestSpecification.
				given().
				body(userDataJson).
				when().
				post(USERS_V2_URL);

		//then
		response.then().statusCode(201);
		Map<String, Object> userData = response.body().jsonPath().getMap("");
		assertThat(userData.size(), is(5));
		assertThat(userData.get("id"), notNullValue());
		assertThat(userData.get("name"), is("Dennis DeMenise"));
		assertThat(userData.get("email"), is("denis_" + currentTimestamp + "@demenise.com"));
		assertThat(userData.get("gender"), is("male"));
		assertThat(userData.get("status"), is("active"));
	}

	@Test
	public void shouldGetUserWithId() {
		long currentTimestamp = System.currentTimeMillis();
		//given
		String userDataJson = "{\"name\":\"Dennis DeMenise\", \"gender\":\"male\", \"email\":\"denis_" + currentTimestamp + "@demenise.com\", \"status\":\"active\"}";

		RequestSpecification requestSpecification = given().
				header("Accept", "application/json").
				header("Content-Type", "application/json").
				header("Authorization", "Bearer " + bearerAccessToken);

		Response response = requestSpecification.
				given().
				body(userDataJson).
				when().
				post(USERS_V2_URL);

		response.then().statusCode(201);
		Map<String, Object> userData = response.body().jsonPath().getMap("");

		//given
		RequestSpecification requestSpecificationCreatedUser = given().
				header("Accept", "application/json").
				header("Content-Type", "application/json").
				header("Authorization", "Bearer " + bearerAccessToken);

		//when
		Response responseCreatedUser = requestSpecificationCreatedUser.
				given().
				when().
				get(USERS_V2_URL + "/" + userData.get("id"));

		//then
		responseCreatedUser.then().statusCode(200);
		Map<String, Object> userDataCreatedUser = responseCreatedUser.body().jsonPath().getMap("");
		assertThat(userDataCreatedUser.size(), is(5));
		assertThat(userDataCreatedUser.get("id"), notNullValue());
		assertThat(userDataCreatedUser.get("name"), is("Dennis DeMenise"));
		assertThat(userDataCreatedUser.get("email"), is("denis_" + currentTimestamp + "@demenise.com"));
		assertThat(userDataCreatedUser.get("gender"), is("male"));
		assertThat(userDataCreatedUser.get("status"), is("active"));
	}

	@Test
	public void shouldUpdateUserWithPut() {
		long currentTimestamp = System.currentTimeMillis();
		//given
		String userDataJson = "{\"name\":\"Dennis DeMenise\", \"gender\":\"male\", \"email\":\"denis_" + currentTimestamp + "@demenise.com\", \"status\":\"active\"}";

		RequestSpecification requestSpecification = given().
				header("Accept", "application/json").
				header("Content-Type", "application/json").
				header("Authorization", "Bearer " + bearerAccessToken);

		Response response = requestSpecification.
				given().
				body(userDataJson).
				when().
				post(USERS_V2_URL);

		response.then().statusCode(201);
		Map<String, Object> userData = response.body().jsonPath().getMap("");

		long currentTimestampUpdate = System.currentTimeMillis();
		//given
		String userDataUpdateJson = "{\"name\":\"Update User\", \"email\":\"email_" + currentTimestampUpdate + "@updated.com\", \"status\":\"active\"}";

		//given
		RequestSpecification requestSpecificationUpdate = given().
				header("Accept", "application/json").
				header("Content-Type", "application/json").
				header("Authorization", "Bearer " + bearerAccessToken);

		//when
		Response responseForUpdate = requestSpecificationUpdate.
				given().
				body(userDataUpdateJson).
				when().
				put(USERS_V2_URL + "/" + userData.get("id"));

		//then
		responseForUpdate.then().statusCode(200);
		Map<String, Object> updatedUserData = responseForUpdate.body().jsonPath().getMap("");
		assertThat(updatedUserData.size(), is(5));
		assertThat(updatedUserData.get("id"), notNullValue());
		assertThat(updatedUserData.get("name"), is("Update User"));
		assertThat(updatedUserData.get("email"), is("email_" + currentTimestampUpdate + "@updated.com"));
		assertThat(updatedUserData.get("gender"), is("male"));
		assertThat(updatedUserData.get("status"), is("active"));
	}

	@Test
	public void shouldUpdateUserWithPatch() {
		long currentTimestamp = System.currentTimeMillis();
		//given
		String userDataJson = "{\"name\":\"Dennis DeMenise\", \"gender\":\"male\", \"email\":\"denis_" + currentTimestamp + "@demenise.com\", \"status\":\"active\"}";

		RequestSpecification requestSpecification = given().
				header("Accept", "application/json").
				header("Content-Type", "application/json").
				header("Authorization", "Bearer " + bearerAccessToken);

		Response response = requestSpecification.
				given().
				body(userDataJson).
				when().
				post(USERS_V2_URL);

		response.then().statusCode(201);
		Map<String, Object> userData = response.body().jsonPath().getMap("");

		long currentTimestampUpdate = System.currentTimeMillis();
		//given
		String userDataUpdateJson = "{\"name\":\"Update User\", \"email\":\"email_" + currentTimestampUpdate + "@updated.com\", \"status\":\"active\"}";

		//given
		RequestSpecification requestSpecificationUpdate = given().
				header("Accept", "application/json").
				header("Content-Type", "application/json").
				header("Authorization", "Bearer " + bearerAccessToken);

		//when
		Response responseForUpdate = requestSpecificationUpdate.
				given().
				body(userDataUpdateJson).
				when().
				patch(USERS_V2_URL + "/" + userData.get("id"));

		//then
		responseForUpdate.then().statusCode(200);
		Map<String, Object> updatedUserData = responseForUpdate.body().jsonPath().getMap("");
		assertThat(updatedUserData.size(), is(5));
		assertThat(updatedUserData.get("id"), notNullValue());
		assertThat(updatedUserData.get("name"), is("Update User"));
		assertThat(updatedUserData.get("email"), is("email_" + currentTimestampUpdate + "@updated.com"));
		assertThat(updatedUserData.get("gender"), is("male"));
		assertThat(updatedUserData.get("status"), is("active"));
	}

	@Test
	public void shouldDeleteUser() {
		long currentTimestamp = System.currentTimeMillis();
		//given
		String userDataJson = "{\"name\":\"Dennis DeMenise\", \"gender\":\"male\", \"email\":\"denis_" + currentTimestamp + "@demenise.com\", \"status\":\"active\"}";

		RequestSpecification requestSpecification = given().
				header("Accept", "application/json").
				header("Content-Type", "application/json").
				header("Authorization", "Bearer " + bearerAccessToken);

		Response response = requestSpecification.
				given().
				body(userDataJson).
				when().
				post(USERS_V2_URL);

		response.then().statusCode(201);
		Map<String, Object> userData = response.body().jsonPath().getMap("");

		//when
		RequestSpecification requestSpecificationDeleteUser = given().
				header("Accept", "application/json").
				header("Content-Type", "application/json").
				header("Authorization", "Bearer " + bearerAccessToken);

		Response responseForDeleteUser = requestSpecificationDeleteUser.
				given().
				body(userDataJson).
				when().
				delete(USERS_V2_URL + "/" + userData.get("id"));

		//then
		responseForDeleteUser.then().statusCode(204);
	}

	@Test
	public void shouldGetListOfUsers() {
		//given
		RequestSpecification requestSpecification = given().
				header("Accept", "application/json").
				header("Content-Type", "application/json").
				header("Authorization", "Bearer " + bearerAccessToken);

		//when
		Response response = requestSpecification.
				given().
				when().
				get(USERS_V2_URL);

		//then
		response.then().statusCode(200);
		List<Object> users = response.body().jsonPath().getList("");
		assertThat(users.size(), is(10));
	}

	@Test
	public void shouldGetListOfUsersPerPage() {
		//given
		RequestSpecification requestSpecification = given().
				header("Accept", "application/json").
				header("Content-Type", "application/json").
				header("Authorization", "Bearer " + bearerAccessToken);

		//when
		Response response = requestSpecification.
				given().
				when().
				get(USERS_V2_URL + "?page=2");

		//then
		response.then().statusCode(200);
		List<Object> users = response.body().jsonPath().getList("");
		assertThat(users.size(), is(10));
	}

	@Test
	public void shouldNotCreateUserWithAnExistingEmail() {
		//given
		String userDataJson = "{\"name\":\"Dennis DeMenise\", \"gender\":\"male\", \"email\":\"denis_@demenise.com\", \"status\":\"active\"}";

		RequestSpecification requestSpecification = given().
				header("Accept", "application/json").
				header("Content-Type", "application/json").
				header("Authorization", "Bearer " + bearerAccessToken);

		Response response = requestSpecification.
				given().
				body(userDataJson).
				when().
				post(USERS_V2_URL);

		response.then().statusCode(201);


		//when
		RequestSpecification requestSpecificationEmail = given().
				header("Accept", "application/json").
				header("Content-Type", "application/json").
				header("Authorization", "Bearer " + bearerAccessToken);

		Response responseEmail = requestSpecificationEmail.
				given().
				body(userDataJson).
				when().
				post(USERS_V2_URL);

		//then
		responseEmail.then().statusCode(422);
		List<Object> userData = responseEmail.body().jsonPath().getList("");
		assertThat(userData.size(), is(1));
		assertThat(userData.get(0).toString(), containsString("{field=email, message=has already been taken}"));
	}

	@Test
	public void shouldGetListOfPosts() {
		//given
		RequestSpecification requestSpecification = given().
				header("Accept", "application/json").
				header("Content-Type", "application/json").
				header("Authorization", "Bearer " + bearerAccessToken);

		//when
		Response response = requestSpecification.
				given().
				when().
				get(POSTS_V2_URL);

		//then
		response.then().statusCode(200);
		List<Object> users = response.body().jsonPath().getList("");
		assertThat(users.size(), is(10));
	}

	@Test
	public void shouldGetEmptyListOfPostsForUser() {
		long currentTimestamp = System.currentTimeMillis();
		//given
		String userDataJson = "{\"name\":\"Dennis DeMenise\", \"gender\":\"male\", \"email\":\"denis_" + currentTimestamp + "@demenise.com\", \"status\":\"active\"}";

		RequestSpecification requestSpecification = given().
				header("Accept", "application/json").
				header("Content-Type", "application/json").
				header("Authorization", "Bearer " + bearerAccessToken);

		Response response = requestSpecification.
				given().
				body(userDataJson).
				when().
				post(USERS_V2_URL);

		response.then().statusCode(201);
		Map<String, Object> userData = response.body().jsonPath().getMap("");


		//given
		RequestSpecification requestSpecificationPost = given().
				header("Accept", "application/json").
				header("Content-Type", "application/json").
				header("Authorization", "Bearer " + bearerAccessToken);

		//when
		Response responsePost = requestSpecificationPost.
				given().
				when().
				get(USERS_V2_URL + "/" + userData.get("id") + "/posts");

		//then
		responsePost.then().statusCode(200);
		List<Object> users = responsePost.body().jsonPath().getList("");
		assertThat(users.size(), is(0));
	}

	@Test
	public void shouldGetListOfComments() {
		//given
		RequestSpecification requestSpecification = given().
				header("Accept", "application/json").
				header("Content-Type", "application/json").
				header("Authorization", "Bearer " + bearerAccessToken);

		//when
		Response response = requestSpecification.
				given().
				when().
				get(COMMENTS_V2_URL);

		//then
		response.then().statusCode(200);
		List<Object> users = response.body().jsonPath().getList("");
		assertThat(users.size(), is(10));
	}

	@Test
	public void shouldGetEmptyListOfCommentsForUser() {
		long currentTimestamp = System.currentTimeMillis();
		//given
		String userDataJson = "{\"name\":\"Dennis DeMenise\", \"gender\":\"male\", \"email\":\"denis_" + currentTimestamp + "@demenise.com\", \"status\":\"active\"}";

		RequestSpecification requestSpecification = given().
				header("Accept", "application/json").
				header("Content-Type", "application/json").
				header("Authorization", "Bearer " + bearerAccessToken);

		Response response = requestSpecification.
				given().
				body(userDataJson).
				when().
				post(USERS_V2_URL);

		response.then().statusCode(201);
		Map<String, Object> userData = response.body().jsonPath().getMap("");

		//given
		RequestSpecification requestSpecificationComments = given().
				header("Accept", "application/json").
				header("Content-Type", "application/json").
				header("Authorization", "Bearer " + bearerAccessToken);

		//when
		Response responseComments = requestSpecificationComments.
				given().
				when().
				get(POSTS_V2_URL + "/" + userData.get("id") + "/comments");

		//then
		responseComments.then().statusCode(200);
		List<Object> users = responseComments.body().jsonPath().getList("");
		assertThat(users.size(), is(0));
	}

	@Test
	public void shouldGetListOfTodos() {
		//given
		RequestSpecification requestSpecification = given().
				header("Accept", "application/json").
				header("Content-Type", "application/json").
				header("Authorization", "Bearer " + bearerAccessToken);

		//when
		Response response = requestSpecification.
				given().
				when().
				get(TODO_V2_URL);

		//then
		response.then().statusCode(200);
		List<Object> users = response.body().jsonPath().getList("");
		assertThat(users.size(), is(10));
	}

	@Test
	public void shouldGetEmptyListOfTodosForUser() {
		long currentTimestamp = System.currentTimeMillis();
		//given
		String userDataJson = "{\"name\":\"Dennis DeMenise\", \"gender\":\"male\", \"email\":\"denis_" + currentTimestamp + "@demenise.com\", \"status\":\"active\"}";

		RequestSpecification requestSpecification = given().
				header("Accept", "application/json").
				header("Content-Type", "application/json").
				header("Authorization", "Bearer " + bearerAccessToken);

		Response response = requestSpecification.
				given().
				body(userDataJson).
				when().
				post(USERS_V2_URL);

		response.then().statusCode(201);
		Map<String, Object> userData = response.body().jsonPath().getMap("");

		//given
		RequestSpecification requestSpecificationTodo = given().
				header("Accept", "application/json").
				header("Content-Type", "application/json").
				header("Authorization", "Bearer " + bearerAccessToken);

		//when
		Response responseTodo = requestSpecificationTodo.
				given().
				when().
				get(USERS_V2_URL + "/" + userData.get("id") + "/todos");

		//then
		responseTodo.then().statusCode(200);
		List<Object> users = responseTodo.body().jsonPath().getList("");
		assertThat(users.size(), is(0));
	}

}