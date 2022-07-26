package com.sw.test.assets;

import com.google.common.collect.Maps;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import utils.AbstractTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertTrue;
import static utils.UrlMapping.ARCHIVISTS_ASSETS_URL;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AssetsAPITest extends AbstractTest {

	private static String jwtBearerAccessToken;
	private static String nameSpaceDoorEntry = "DenisGershengoren";

	@BeforeClass
	public static void restAssuredConfig() {
		//todo: move token to the secret-key on AWS or to a folder with 0600 permissions
		jwtBearerAccessToken = "eyJhbGciOiJSUzI1NiIsImtpZCI6InN0dW50aWRwIiwidHlwIjoiSldUIn0.eyJhdWQiOiJzdHVudC1pZHAiLCJlbWFpbCI6ImNhbmRpZGF0ZUBqaXRzdWluLmNvbSIsImV4cCI6MTY4OTk0ODc2OCwiaWF0IjoxNjU4NDEyNzY4LCJpc3MiOiJzdHVudC1pZHBAaml0c3Vpbi5jb20iLCJqaXRfb3JpZ2luYWxfaXNzIjoic3R1bnQtaWRwQGppdHN1aW4uY29tIiwiaml0X29yaWdpbmFsX3N1YiI6IkRlbmlzR2Vyc2hlbmdvcmVuIiwiaml0X3RlbmFudF9pZCI6InRlbmFudC85MGU1ZTEwNi0xNTQ4LTRmNGMtOTNiZi1jZjQxYTdiNTJkMDEiLCJuYW1lIjoiRGVuaXNHZXJzaGVuZ29yZW4iLCJyb2xlcyI6WyJhcmNoaXZpc3RfYWRtaW5pc3RyYXRvciJdLCJzdWIiOiI1NmYzNDZhYy1kNWUxLTQxMDgtYWYyNS00NGQwYzc4ZTkxYzcifQ.gcVzqNTOoaGkCKfVuK2OyQIiexi5j8DQqrRtoI6W50UXaEo7vaIX4QGQpboLP8JwStJL8my4gnj32pSnK6oPE_ggzTG1-BH7M4PFtChcszj3vT7b_aZVR-40-x02-SFaCtJgnWdJcKAIklUkaGKLGRqAvhPjyL57MxU5jJym04zpw678n6CejOODC0OHTKy1g2aOdzlRkwyt27I8nED4_LSPWSVteVZhEd2whhhQaaiPn4-xXqevOfsXdGC403SuZiAC-spOKmywS67bHn4PimmAUFcZHqW5v1hnf7UuY4aIaBEgC1FNsrtfhORVN3a-cZawPxt0UX2Y8oBeSlmmKzpU6VAfM5HU63vsSjCW23PZfAjJgJzA-ACHYb-FjNlhtzx8Udzv1TraYb_6lEwPIKc87AVDD5wUd8p7td_s3zQgHT0U14OMJawZJfjAnIii7hC30UWlmofOTT6hjzLBmwKcZD0HtUqxKiZ8KEq6utbc7KpO11BVl2b57F34sNBM9ucgLpR4bEomTbVaTsVi35_Fyd90imjnZgU5gSM7RUQMQW1LZHJ-JR9Z2OLHdtDHVPftyzktcZhB9YaNIpE58TZeLzbTjJuGjPiyhcDIf0hUlIV5UApvXM8UDLbNzGWBET-eE3-EpUAbZPfx4jqktcFbDdVCl6mMXdXDs5Vyr20";
	}

	//todo: move json to a file in folder with 0600 permissions
	@Test
	public void shouldCreateAssetUsingAssetParameters() {
		String json = "{\n" +
				"  \"attributes\": {\n" +
				"    \"arc_attachments\": [\n" +
				"      {\n" +
				"        \"arc_attachment_identity\": \"blobs/b15ff7fe-c42b-4e73-942e-ca6739ad2da5\",\n" +
				"        \"arc_display_name\": \"Picture from yesterday\",\n" +
				"        \"arc_hash_alg\": \"sha256\",\n" +
				"        \"arc_hash_value\": \"01ba4719c80b6fe911b091a7c05124b64eeece964e09c058ef8f9805daca546b\"\n" +
				"      }\n" +
				"    ],\n" +
				"    \"arc_firmware_version\": \"3.2.1\",\n" +
				"    \"arc_home_location_identity\": \"locations/42054f10-9952-4c10-a082-9fd0d10295ae\"\n" +
				"  },\n" +
				"  \"behaviours\": [\n" +
				"    \"RecordEvidence\",\n" +
				"    \"Attachments\"\n" +
				"  ],\n" +
				"  \"proof_mechanism\": \"SIMPLE_HASH\",\n" +
				"  \"public\": true\n" +
				"}";

		//given
		RequestSpecification requestSpecification = given().
				header("Authorization", "Bearer " + jwtBearerAccessToken);
		//when
		Response response = requestSpecification.
				given().
				contentType(ContentType.JSON).
				body(json).
				when().
				post(ARCHIVISTS_ASSETS_URL);

		//then
		response.then().statusCode(200);
		Map<String, Object> publishedAsset = response.body().jsonPath().getMap("");
		assertThat(publishedAsset.size(), is(12));
		assertThat(publishedAsset.get("owner"), is(""));
		assertThat(publishedAsset.get("proof_mechanism"), is("SIMPLE_HASH"));
		assertThat(publishedAsset.get("storage_integrity"), is("TENANT_STORAGE"));
		assertThat(publishedAsset.get("tenant_identity"), is(""));
		assertThat(publishedAsset.get("confirmation_status"), is("PENDING"));
		assertThat(publishedAsset.get("chain_id"), is("99"));
		assertThat(publishedAsset.get("public"), is(true));
		assertThat(publishedAsset.get("identity").toString(), containsString("assets/"));
		assertThat(publishedAsset.get("at_time"), notNullValue());
		assertThat(publishedAsset.get("attributes"), notNullValue());
		assertThat(publishedAsset.get("tracked"), is("TRACKED"));
		assertThat(publishedAsset.get("behaviours"), notNullValue());
	}

	@Test
	public void shouldNotFetchAllAssetsWithoutToken() {
		//given
		//when
		Response response = given().
				when().
				get(ARCHIVISTS_ASSETS_URL);

		//then
		response.then().statusCode(403);
		Map<String, Object> asset = response.body().jsonPath().getMap("");
		assertThat(asset.get("statusCode"), is(403));
		assertThat(asset.get("message"), is("Missing Authorization Header"));
	}

	@Test
	public void shouldNotFetchAllAssetsWithInvalidToken() {
		String invalidToken = "INVALID_TOKEN";

		//given
		RequestSpecification requestSpecification = given().
				header("Authorization", "Bearer " + invalidToken);

		//when
		Response response = requestSpecification.
				given().
				when().
				get(ARCHIVISTS_ASSETS_URL);

		//then
		response.then().statusCode(403);
		Map<String, Object> asset = response.body().jsonPath().getMap("");
		assertThat(asset.get("statusCode"), is(403));
		assertThat(asset.get("message"), is("Invalid Token"));
	}

	@Test
	public void shouldFetchAllAssets() {
		//given
		RequestSpecification requestSpecification = given().
				header("Authorization", "Bearer " + jwtBearerAccessToken);
		//when

		Response response = requestSpecification.
				given().
				when().
				get(ARCHIVISTS_ASSETS_URL);

		//then
		response.then().statusCode(200);
		List<HashMap<String, List<String>>> assets = (List<HashMap<String, List<String>>>) response.body().jsonPath().getMap("").get("assets");
		assertTrue(assets.size() > 1);
	}

	@Test
	public void shouldFetchAssets() {
		//given
		RequestSpecification requestSpecification = given().
				header("Authorization", "Bearer " + jwtBearerAccessToken);
		//when
		Response response = requestSpecification.
				given().
				when().
				get(ARCHIVISTS_ASSETS_URL);

		//then
		response.then().statusCode(200);
		List<HashMap<String, List<String>>> assets = (List<HashMap<String, List<String>>>) response.body().jsonPath().getMap("").get("assets");
		assertTrue(assets.size() > 100);
	}

	@Test
	public void shouldNotFetchAssetByInvalidIdentity() {
		String invalidAssetIdentity = "00000000-0000-0000-0000-650b6e6a71a6";
		//given
		RequestSpecification requestSpecification = given().
				header("Authorization", "Bearer " + jwtBearerAccessToken);
		//when
		Response response = requestSpecification.
				given().
				when().
				get(ARCHIVISTS_ASSETS_URL + invalidAssetIdentity);

		//then
		response.then().statusCode(404);
		Map<String, Object> asset = response.body().jsonPath().getMap("");
		assertThat(asset.get("statusCode"), is(404));
		assertThat(asset.get("message"), is("Resource not found"));
	}

	@Test
	public void shouldFetchAssetByIdentity() {
		String assetIdentity = "/e221b337-16de-45ec-9b7d-650b6e6a71a6";
		//given
		RequestSpecification requestSpecification = given().
				header("Authorization", "Bearer " + jwtBearerAccessToken);
		//when
		Response response = requestSpecification.
				given().
				when().
				get(ARCHIVISTS_ASSETS_URL + assetIdentity);

		//then
		response.then().statusCode(200);
		Map<String, Object> asset = response.body().jsonPath().getMap("");
		assertThat(asset.size(), is(12));
		assertThat(asset.get("identity"), is("assets" + assetIdentity));

		ArrayList<String> assetBehaviours =  (ArrayList) asset.get("behaviours");
		assertThat(assetBehaviours.size(), is(4));
		assertThat(assetBehaviours, hasItems("AssetCreator", "RecordEvidence", "Builtin", "AssetCreator"));

		HashMap assetAttributes =  (HashMap) asset.get("attributes");
		assertThat(assetAttributes.get("arc_display_name"), is("Gare du Nord apartments front door"));
		assertThat(assetAttributes.get("arc_firmware_version"), is("1.0"));
		assertThat(assetAttributes.get("arc_namespace"), is("door_entry_" + nameSpaceDoorEntry));
		assertThat(assetAttributes.get("wavestone_asset_id"), is("front.gdn.paris.wavestonedas"));
		assertThat(assetAttributes.get("arc_home_location_identity"), is("locations/461eaeae-e058-4c64-9f92-3c3e73c9c2e5"));
		assertThat(assetAttributes.get("arc_display_type"), is("Door access terminal"));
		assertThat(assetAttributes.get("arc_description"), is("Electronic door entry system controlling the front residential entrance to Apartements du Gare du Nord"));
		assertThat(assetAttributes.get("arc_serial_number"), is("das-x4-04"));

		ArrayList assetAttachmentsList =  (ArrayList) assetAttributes.get("arc_attachments");
		assertThat(assetAttachmentsList.size(), is(1));
		HashMap assetAttachments =  (HashMap) assetAttachmentsList.get(0);
		assertThat(assetAttachments.get("arc_hash_alg"), is("SHA256"));
		assertThat(assetAttachments.get("arc_hash_value"), is("369ab7feda91a45f4c3fdc4986c72b93593e6464e70c0c98b73e20ac8e4082e9"));
		assertThat(assetAttachments.get("arc_attachment_identity"), is("blobs/2b957728-02ed-44cd-bd0f-3186dc52b312"));
		assertThat(assetAttachments.get("arc_display_name"), is("arc_primary_image"));

		assertThat(asset.get("at_time"), notNullValue()); //time keep changing, will check in separate test
		assertThat(asset.get("storage_integrity"), is("TENANT_STORAGE"));
		assertThat(asset.get("proof_mechanism"), is("SIMPLE_HASH"));
		assertThat(asset.get("chain_id"), is("99"));
		assertThat(asset.get("public"), is(false));
		assertThat(asset.get("tenant_identity"), is("tenant/90e5e106-1548-4f4c-93bf-cf41a7b52d01"));
	}

	@Test
	public void shouldNotFetchAssetByIdentityAtGivenPointInInvalidTimeByIdentity() {
		String assetIdentityAndInvalidTime = "/f5900d66-7492-4ede-94d8-121df81dbdd2?at_time=";

		//given
		RequestSpecification requestSpecification = given().
				header("Authorization", "Bearer " + jwtBearerAccessToken);
		//when
		Response response = requestSpecification.
				given().
				when().
				get(ARCHIVISTS_ASSETS_URL + assetIdentityAndInvalidTime);

		//then
		response.then().statusCode(400);
		// what is this message?
		//message -> parsing field "at_time": parsing time "" as "2006-01-02T15:04:05.999999999Z07:00": cannot parse "" as "2006"
	}

	@Test
	public void shouldFetchAssetByIdentityAtGivenPointInTimeByIdentity() {
		String assetIdentity = "/f5900d66-7492-4ede-94d8-121df81dbdd2";
		String accessTimePrefix = "?at_time=";
		String assetAccessTime = "2022-07-22T18:19:13Z";
		//given
		RequestSpecification requestSpecification = given().
				header("Authorization", "Bearer " + jwtBearerAccessToken);
		//when
		Response response = requestSpecification.
				given().
				when().
				get(ARCHIVISTS_ASSETS_URL + assetIdentity + accessTimePrefix + assetAccessTime);

		//then
		response.then().statusCode(200);
		Map<String, Object> asset = response.body().jsonPath().getMap("");
		assertThat(asset.size(), is(12));
		assertThat(asset.get("identity"), is("assets" + assetIdentity));

		ArrayList<String> assetBehaviours =  (ArrayList) asset.get("behaviours");
		assertThat(assetBehaviours.size(), is(4));
		assertThat(assetBehaviours, hasItems("Attachments", "RecordEvidence", "Builtin", "AssetCreator"));

		HashMap assetAttributes =  (HashMap) asset.get("attributes");
		assertThat(assetAttributes.get("arc_display_name"), is("Bastille front door"));
		assertThat(assetAttributes.get("arc_firmware_version"), is("1.0"));
		assertThat(assetAttributes.get("arc_namespace"), is("door_entry_" + nameSpaceDoorEntry));
		assertThat(assetAttributes.get("wavestone_asset_id"), is("bastille.paris.wavestonedas"));
		assertThat(assetAttributes.get("arc_home_location_identity"), is("locations/ed8023d4-20b6-4143-8040-b282aa66e751"));
		assertThat(assetAttributes.get("arc_display_type"), is("Door access terminal"));
		assertThat(assetAttributes.get("arc_description"), is("Electronic door entry system controlling the main staff entrance to Bastille"));
		assertThat(assetAttributes.get("arc_serial_number"), is("das-x4-03"));

		ArrayList assetAttachmentsList =  (ArrayList) assetAttributes.get("arc_attachments");
		assertThat(assetAttachmentsList.size(), is(1));
		HashMap assetAttachments =  (HashMap) assetAttachmentsList.get(0);
		assertThat(assetAttachments.get("arc_hash_alg"), is("SHA256"));
		assertThat(assetAttachments.get("arc_hash_value"), is("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855"));
		assertThat(assetAttachments.get("arc_attachment_identity"), is("blobs/8a891b81-d4dd-434d-bb46-bedc1046d493"));
		assertThat(assetAttachments.get("arc_display_name"), is("arc_primary_image"));

		assertThat(asset.get("confirmation_status"), is("CONFIRMED"));
		assertThat(asset.get("tracked"), is("TRACKED"));
		assertThat(asset.get("owner"), is("0x479eDA09957C6E22467414ade0a1887EEEC6487e"));
		assertThat(asset.get("at_time"), is(assetAccessTime));
		assertThat(asset.get("storage_integrity"), is("TENANT_STORAGE"));
		assertThat(asset.get("proof_mechanism"), is("SIMPLE_HASH"));
		assertThat(asset.get("chain_id"), is("99"));
		assertThat(asset.get("public"), is(false));
		assertThat(asset.get("tenant_identity"), is("tenant/90e5e106-1548-4f4c-93bf-cf41a7b52d01"));
	}

	@Test
	public void shouldNotFetchAssetByInvalidName() {
		String assetNamePrefix = "?attributes.arc_display_name=";
		String invalidAssetName = "INVALID";
		//given
		RequestSpecification requestSpecification = given().
				header("Authorization", "Bearer " + jwtBearerAccessToken);
		//when
		Response response = requestSpecification.
				given().
				when().
				get(ARCHIVISTS_ASSETS_URL + assetNamePrefix + invalidAssetName);

		//then
		response.then().statusCode(200);
		Map<String, Object> asset = response.body().jsonPath().getMap("");
		ArrayList assets =  (ArrayList) asset.get("assets");
		assertThat(assets.size(), is(0));
	}

	@Test
	public void shouldFetchAssetByName() {
		String assetNamePrefix = "?attributes.arc_display_name=";
		String assetName = "synsation.assets.securitycamera_0";
		//given
		RequestSpecification requestSpecification = given().
				header("Authorization", "Bearer " + jwtBearerAccessToken);
		//when
		Response response = requestSpecification.
				given().
				when().
				get(ARCHIVISTS_ASSETS_URL + assetNamePrefix + assetName);

		//then
		response.then().statusCode(200);
		Map<String, Object> asset = response.body().jsonPath().getMap("");
		ArrayList assets =  (ArrayList) asset.get("assets");
		HashMap assetData =  (HashMap) assets.get(0);
		assertThat(assetData.get("owner"), is("0x479eDA09957C6E22467414ade0a1887EEEC6487e"));
		assertThat(assetData.get("proof_mechanism"), is("SIMPLE_HASH"));
		assertThat(assetData.get("storage_integrity"), is("TENANT_STORAGE"));
		assertThat(assetData.get("tenant_identity"), is("tenant/90e5e106-1548-4f4c-93bf-cf41a7b52d01"));
		assertThat(assetData.get("confirmation_status"), is("CONFIRMED"));
		assertThat(assetData.get("chain_id"), is("99"));
		assertThat(assetData.get("public"), is(false));
		assertThat(assetData.get("identity"), is("assets/5bbbab4d-1d3b-40a7-b7a4-bb59a0d15a3b"));
		assertThat(assetData.get("at_time"), notNullValue()); //time keep changing, will check in separate test

		HashMap assetAttributes =  (HashMap) assetData.get("attributes");
		assertThat(assetAttributes.get("arc_home_location_identity"), is("locations/e42073ea-939b-471f-be9d-9b141fa4e35e"));

		ArrayList assetAttachmentsList =  (ArrayList) assetAttributes.get("arc_attachments");
		assertThat(assetAttachmentsList.size(), is(1));
		HashMap assetAttachments =  (HashMap) assetAttachmentsList.get(0);
		assertThat(assetAttachments.get("arc_hash_alg"), is("SHA256"));
		assertThat(assetAttachments.get("arc_hash_value"), is("2c96ca673458692ce2a23e87f43a69bf79e0e2b6c25a8ff02fb7bec2fbfdf4c3"));
		assertThat(assetAttachments.get("arc_attachment_identity"), is("blobs/d31dc119-0d5b-4a02-8976-ffcdc2298537"));
		assertThat(assetAttributes.get("arc_display_type"), is("Security Camera"));
		assertThat(assetAttributes.get("arc_firmware_version"), is("1.0"));
		assertThat(assetAttributes.get("arc_display_name"), is(assetName));
		assertThat(assetAttributes.get("arc_namespace"), is("synsation_" + nameSpaceDoorEntry));
		assertThat(assetAttributes.get("arc_description"), is("This is my Security Camera. There are many like it, but this one is #0"));
		assertThat(assetAttributes.get("arc_serial_number"), is("f867662g.1"));

		assertThat(assetData.get("tracked"), is("TRACKED"));
		ArrayList<String> assetBehaviours =  (ArrayList) assetData.get("behaviours");
		assertThat(assetBehaviours.size(), is(4));
		assertThat(assetBehaviours, hasItems("Attachments", "RecordEvidence", "Builtin", "AssetCreator"));
	}

	@Test
	public void shouldFetchAssetByType() {
		String displayTypePrefix = "?attributes.arc_display_type=";
		String displayType = "Street light controller";
		//given
		RequestSpecification requestSpecification = given().
				header("Authorization", "Bearer " + jwtBearerAccessToken);
		//when
		Response response = requestSpecification.
				given().
				when().
				get(ARCHIVISTS_ASSETS_URL + displayTypePrefix + displayType);

		//then
		response.then().statusCode(200);
		Map<String, Object> asset = response.body().jsonPath().getMap("");
		ArrayList assets =  (ArrayList) asset.get("assets");
		assertThat(assets.size(), is(8));

		for (int count = 0; count < assets.size(); count++) {
			HashMap assetData =  (HashMap) assets.get(count);
			HashMap assetAttributes =  (HashMap) assetData.get("attributes");
			assertThat(assetAttributes.get("arc_display_type"), is("Street light controller"));
		}
	}

	@Test
	public void shouldNotFetchAssetByInvalidType() {
		String displayTypePrefix = "?attributes.arc_display_type=";
		String invalidDisplayType = "INVALID";
		//given
		RequestSpecification requestSpecification = given().
				header("Authorization", "Bearer " + jwtBearerAccessToken);
		//when
		Response response = requestSpecification.
				given().
				when().
				get(ARCHIVISTS_ASSETS_URL + displayTypePrefix + invalidDisplayType);

		//then
		response.then().statusCode(200);
		Map<String, Object> asset = response.body().jsonPath().getMap("");
		ArrayList assets =  (ArrayList) asset.get("assets");
		assertThat(assets.size(), is(0));
	}

	@Test
	public void shouldNotFetchAssetByFilteringForPresenceOfAField() {
		String displayNamePrefix = "?attributes.arc_display_name=";
		String invalidDisplayName = "INVALID";
		//given
		RequestSpecification requestSpecification = given().
				header("Authorization", "Bearer " + jwtBearerAccessToken);
		//when
		Response response = requestSpecification.
				given().
				when().
				get(ARCHIVISTS_ASSETS_URL + displayNamePrefix + invalidDisplayName);

		//then
		response.then().statusCode(200);
		Map<String, Object> asset = response.body().jsonPath().getMap("");
		ArrayList assets =  (ArrayList) asset.get("assets");
		assertThat(assets.size(), is(0));
	}

	@Test
	public void shouldFetchAssetByFilteringForPresenceOfAField() {
		String displayNamePrefix = "?attributes.arc_display_name=";
		String displayName = "tcl.nmr.004";
		//given
		RequestSpecification requestSpecification = given().
				header("Authorization", "Bearer " + jwtBearerAccessToken);
		//when
		Response response = requestSpecification.
				given().
				when().
				get(ARCHIVISTS_ASSETS_URL + displayNamePrefix + displayName);

		//then
		response.then().statusCode(200);
		Map<String, Object> asset = response.body().jsonPath().getMap("");
		ArrayList assets =  (ArrayList) asset.get("assets");
		assertThat(assets.size(), is(1));

		for (int count = 0; count < assets.size(); count++) {
			HashMap assetData =  (HashMap) assets.get(count);
			HashMap assetAttributes =  (HashMap) assetData.get("attributes");
			assertThat(assetAttributes.get("arc_display_name"), is(displayName));
		}
	}

	@Test
	public void shouldFetchAllAssetByFilteringForPresenceOfAField() {
		String displayNamePrefix = "?attributes.arc_display_name=";
		String displayName = "*";
		//given
		RequestSpecification requestSpecification = given().
				header("Authorization", "Bearer " + jwtBearerAccessToken);
		//when
		Response response = requestSpecification.
				given().
				when().
				get(ARCHIVISTS_ASSETS_URL + displayNamePrefix + displayName);

		//then
		response.then().statusCode(200);
		Map<String, Object> asset = response.body().jsonPath().getMap("");
		ArrayList assets =  (ArrayList) asset.get("assets");
		assertThat(assets.size(), is(120));
	}

	@Test
	public void shouldNotFetchAssetWhenFieldIsMissing() {
		String invalidDisplayNamePrefix = "?attributes.arc_display_invalid_name=";
		String displayName = "*";
		//given
		RequestSpecification requestSpecification = given().
				header("Authorization", "Bearer " + jwtBearerAccessToken);
		//when
		Response response = requestSpecification.
				given().
				when().
				get(ARCHIVISTS_ASSETS_URL + invalidDisplayNamePrefix + displayName);

		//then
		response.then().statusCode(200);
		Map<String, Object> asset = response.body().jsonPath().getMap("");
		ArrayList assets =  (ArrayList) asset.get("assets");
		assertThat(assets.size(), is(0));
	}

	//todo: need to ask about this test, not sure about !
	@Test
	public void shouldFetchAssetWhenFieldIsMissing() {
		String invalidDisplayNamePrefix = "?attributes.arc_display_name!=";
		String displayName = "tcl.nmr.004";
		//given
		RequestSpecification requestSpecification = given().
				header("Authorization", "Bearer " + jwtBearerAccessToken);
		//when
		Response response = requestSpecification.
				given().
				when().
				get(ARCHIVISTS_ASSETS_URL + invalidDisplayNamePrefix + displayName);

		//then
		response.then().statusCode(200);
		Map<String, Object> asset = response.body().jsonPath().getMap("");
		ArrayList assets =  (ArrayList) asset.get("assets");
		assertThat(assets.size(), is(1));
	}

	//todo: problem - there is no public assets
	//https://app.soak.wild.jitsuin.io/archivist/v2/assets/86b61c4b-030e-4c07-9400-463612e6cee4:publicurl
	//returns
	//{
	//    "code": 5,
	//    "message": "could not get public asset",
	//    "details": []
	//}
	@Test @Ignore
	public void shouldFetchAssetByPublicAssetsURL() {
		String assetIdentity = "/5bbbab4d-1d3b-40a7-b7a4-bb59a0d15a3b";
		String accessTimePrefix = ":publicurl";

		//given
		RequestSpecification requestSpecification = given().
				header("Authorization", "Bearer " + jwtBearerAccessToken);
		//when
		Response response = requestSpecification.
				given().
				when().
				get(ARCHIVISTS_ASSETS_URL + assetIdentity + accessTimePrefix);

		//then
		response.then().statusCode(200);
		Map<String, Object> asset = response.body().jsonPath().getMap("");
		ArrayList assets =  (ArrayList) asset.get("assets");
		assertThat(assets.size(), is(1));
	}

	//todo: problem - there is no public assets/events
	@Test @Ignore
	public void shouldNotFetchAssetByPublicAssetsEventURL() {
		String assetIdentity = "/5bbbab4d-1d3b-40a7-b7a4-bb59a0d15a3b";
		String accessEvents = "/events";
		String assetPublicUrl = "/7da272ad-19d5-4106-b4af-2980a84c2721";
		String publicUrlPostfix = ":publicurl";

		//given
		RequestSpecification requestSpecification = given().
				header("Authorization", "Bearer " + jwtBearerAccessToken);
		//when
		Response response = requestSpecification.
				given().
				when().
				get(ARCHIVISTS_ASSETS_URL + assetIdentity + accessEvents + assetPublicUrl + publicUrlPostfix);

		//then
		response.then().statusCode(200);
		Map<String, Object> asset = response.body().jsonPath().getMap("");
		ArrayList assets =  (ArrayList) asset.get("assets");
		assertThat(assets.size(), is(1));
		/*{
			"code": 5,
				"message": "asset requested is not public",
				"details": []
		}*/
	}

	@Test
	public void shouldFetchAssetEvents() {
		String assetName = "/f5900d66-7492-4ede-94d8-121df81dbdd2";
		String assetEvents = "/events";
		//given
		RequestSpecification requestSpecification = given().
				header("Authorization", "Bearer " + jwtBearerAccessToken);
		//when
		Response response = requestSpecification.
				given().
				when().
				get(ARCHIVISTS_ASSETS_URL + assetName + assetEvents);

		//then
		response.then().statusCode(200);
		Map<String, Object> events = response.body().jsonPath().getMap("");
		ArrayList<String> event =  (ArrayList) events.get("events");
		assertTrue(event.size() > 1);
		ArrayList eventList =  (ArrayList) events.get("events");
		HashMap eventData =  (HashMap) eventList.get(0);
		assertThat(eventData.size(), is(17));
		assertThat(eventData.get("transaction_id"), is(""));
		assertThat(eventData.get("timestamp_committed"), notNullValue());
		assertThat(eventData.get("timestamp_declared"), notNullValue());
		assertThat(eventData.get("timestamp_accepted"), notNullValue());
		assertThat(eventData.get("block_number"), is(0));
		assertThat(eventData.get("transaction_index"), is(0));
		assertThat(eventData.get("asset_identity").toString(), containsString("assets/"));
		assertThat(eventData.get("tenant_identity").toString(), is("tenant/90e5e106-1548-4f4c-93bf-cf41a7b52d01"));
		assertThat(eventData.get("confirmation_status"), is("CONFIRMED"));
		assertThat(eventData.get("asset_attributes"), notNullValue());
		assertThat(eventData.get("identity").toString(), containsString("assets/"));
		assertThat(eventData.get("identity").toString(), containsString("/events/"));
		assertThat(eventData.get("behaviour"), is("RecordEvidence"));
		assertThat(eventData.get("principal_declared").toString(), is("{subject=phil.b, display_name=, issuer=idp.synsation.io/1234, email=phil.b@synsation.io}"));
		assertThat(eventData.get("from"), is("0x479eDA09957C6E22467414ade0a1887EEEC6487e"));
		assertThat(eventData.get("event_attributes"), notNullValue());
		assertThat(eventData.get("operation"), is("Record"));
		assertThat(eventData.get("principal_accepted").toString(), is("{subject=DenisGershengoren, display_name=DenisGershengoren, issuer=stunt-idp@jitsuin.com, email=candidate@jitsuin.com}"));
	}

	@Test
	public void shouldFetchAssetOpenApi() {
		String openApi = ":openapi";
		//given
		RequestSpecification requestSpecification = given().
				header("Authorization", "Bearer " + jwtBearerAccessToken);
		//when
		Response response = requestSpecification.
				given().
				when().
				get(ARCHIVISTS_ASSETS_URL + openApi);

		//then
		response.then().statusCode(200);
		Map<String, Object> api = response.body().jsonPath().getMap("");
		assertThat(api.size(), is(9));
		assertThat(api.get("basePath"), is("/_api"));
		assertThat(api.get("swagger"), is("2.0"));
	}

	@Test
	public void shouldFetchAssetOpenApiUI() {
		String openApi = ":openapi-ui";
		//given
		RequestSpecification requestSpecification = given().
				header("Authorization", "Bearer " + jwtBearerAccessToken);
		//when
		Response response = requestSpecification.
				given().
				when().
				get(ARCHIVISTS_ASSETS_URL + openApi);

		//then
		response.then().statusCode(200);
	}

}