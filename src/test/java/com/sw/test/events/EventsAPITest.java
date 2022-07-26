package com.sw.test.events;

import com.google.common.collect.Maps;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import utils.AbstractTest;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static utils.UrlMapping.ARCHIVISTS_ASSETS_URL;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EventsAPITest extends AbstractTest {

	private static String jwtBearerAccessToken;

	@BeforeClass
	public static void restAssuredConfig() {
		//todo: move token to the secret-key on AWS or to a folder with 0600 permissions
		jwtBearerAccessToken = "eyJhbGciOiJSUzI1NiIsImtpZCI6InN0dW50aWRwIiwidHlwIjoiSldUIn0.eyJhdWQiOiJzdHVudC1pZHAiLCJlbWFpbCI6ImNhbmRpZGF0ZUBqaXRzdWluLmNvbSIsImV4cCI6MTY4OTk0ODc2OCwiaWF0IjoxNjU4NDEyNzY4LCJpc3MiOiJzdHVudC1pZHBAaml0c3Vpbi5jb20iLCJqaXRfb3JpZ2luYWxfaXNzIjoic3R1bnQtaWRwQGppdHN1aW4uY29tIiwiaml0X29yaWdpbmFsX3N1YiI6IkRlbmlzR2Vyc2hlbmdvcmVuIiwiaml0X3RlbmFudF9pZCI6InRlbmFudC85MGU1ZTEwNi0xNTQ4LTRmNGMtOTNiZi1jZjQxYTdiNTJkMDEiLCJuYW1lIjoiRGVuaXNHZXJzaGVuZ29yZW4iLCJyb2xlcyI6WyJhcmNoaXZpc3RfYWRtaW5pc3RyYXRvciJdLCJzdWIiOiI1NmYzNDZhYy1kNWUxLTQxMDgtYWYyNS00NGQwYzc4ZTkxYzcifQ.gcVzqNTOoaGkCKfVuK2OyQIiexi5j8DQqrRtoI6W50UXaEo7vaIX4QGQpboLP8JwStJL8my4gnj32pSnK6oPE_ggzTG1-BH7M4PFtChcszj3vT7b_aZVR-40-x02-SFaCtJgnWdJcKAIklUkaGKLGRqAvhPjyL57MxU5jJym04zpw678n6CejOODC0OHTKy1g2aOdzlRkwyt27I8nED4_LSPWSVteVZhEd2whhhQaaiPn4-xXqevOfsXdGC403SuZiAC-spOKmywS67bHn4PimmAUFcZHqW5v1hnf7UuY4aIaBEgC1FNsrtfhORVN3a-cZawPxt0UX2Y8oBeSlmmKzpU6VAfM5HU63vsSjCW23PZfAjJgJzA-ACHYb-FjNlhtzx8Udzv1TraYb_6lEwPIKc87AVDD5wUd8p7td_s3zQgHT0U14OMJawZJfjAnIii7hC30UWlmofOTT6hjzLBmwKcZD0HtUqxKiZ8KEq6utbc7KpO11BVl2b57F34sNBM9ucgLpR4bEomTbVaTsVi35_Fyd90imjnZgU5gSM7RUQMQW1LZHJ-JR9Z2OLHdtDHVPftyzktcZhB9YaNIpE58TZeLzbTjJuGjPiyhcDIf0hUlIV5UApvXM8UDLbNzGWBET-eE3-EpUAbZPfx4jqktcFbDdVCl6mMXdXDs5Vyr20";
	}

	//todo: move json to a file in folder with 0600 permissions
	@Test
	public void shouldCreateEventWithEventParameters() {
		//given
		String createEventJson = "{\n" +
				"  \"operation\": \"Record\",\n" +
				"  \"behaviour\": \"RecordEvidence\",\n" +
				"  \"event_attributes\": {\n" +
				"    \"arc_display_type\": \"Safety Conformance\",\n" +
				"    \"Safety Rating\": \"90\",\n" +
				"    \"inspector\": \"spacetime\"\n" +
				"  },\n" +
				"  \"timestamp_declared\": \"2019-11-27T14:44:19Z\",\n" +
				"  \"principal_declared\": {\n" +
				"    \"issuer\": \"idp.synsation.io/1234\",\n" +
				"    \"subject\": \"phil.b\",\n" +
				"    \"email\": \"phil.b@synsation.io\"\n" +
				"  }\n" +
				"}";

		String assetId = "/f5900d66-7492-4ede-94d8-121df81dbdd2";
		String eventPostfix = "/events";

		//given
		RequestSpecification requestSpecification = given().
				header("Content-Type", "application/json").
				header("Authorization", "Bearer " + jwtBearerAccessToken);

		//when
		Response response = requestSpecification.
				given().
				body(createEventJson).
				when().
				post(ARCHIVISTS_ASSETS_URL + assetId + eventPostfix);

		//then
		response.then().statusCode(200);
		Map<String, Object> publishedAsset = response.body().jsonPath().getMap("");
		assertThat(publishedAsset.size(), is(17));
		assertThat(publishedAsset.get("transaction_id"), is(""));
		assertThat(publishedAsset.get("timestamp_committed"), notNullValue());
		assertThat(publishedAsset.get("timestamp_declared"), notNullValue());
		assertThat(publishedAsset.get("timestamp_accepted"), notNullValue());
		assertThat(publishedAsset.get("block_number"), is(0));
		assertThat(publishedAsset.get("transaction_index"), is(0));
		assertThat(publishedAsset.get("asset_identity").toString(), containsString("assets/"));
		assertThat(publishedAsset.get("tenant_identity").toString(), is(""));
		assertThat(publishedAsset.get("confirmation_status"), is("PENDING"));
		assertThat(publishedAsset.get("asset_attributes"), notNullValue());
		assertThat(publishedAsset.get("identity").toString(), containsString("assets/"));
		assertThat(publishedAsset.get("identity").toString(), containsString("/events/"));
		assertThat(publishedAsset.get("behaviour"), is("RecordEvidence"));
		assertThat(publishedAsset.get("principal_declared").toString(), is("{subject=phil.b, display_name=, issuer=idp.synsation.io/1234, email=phil.b@synsation.io}"));
		assertThat(publishedAsset.get("from"), is(""));
		assertThat(publishedAsset.get("event_attributes"), notNullValue());
		assertThat(publishedAsset.get("operation"), is("Record"));
		assertThat(publishedAsset.get("principal_accepted").toString(), is("{subject=DenisGershengoren, display_name=DenisGershengoren, issuer=stunt-idp@jitsuin.com, email=candidate@jitsuin.com}"));
	}

	//todo: move json to a file in folder with 0600 permissions
	@Test
	public void shouldNotCreateEventForNotEnabledEvent() {
		//given
		String createEventJson = "{\n" +
				"  \"operation\": \"Record\",\n" +
				"  \"behaviour\": \"RecordEvidence\",\n" +
				"  \"event_attributes\": {\n" +
				"    \"arc_display_type\": \"Safety Conformance\",\n" +
				"    \"Safety Rating\": \"90\",\n" +
				"    \"inspector\": \"spacetime\"\n" +
				"  },\n" +
				"  \"timestamp_declared\": \"2019-11-27T14:44:19Z\",\n" +
				"  \"principal_declared\": {\n" +
				"    \"issuer\": \"idp.synsation.io/1234\",\n" +
				"    \"subject\": \"phil.b\",\n" +
				"    \"email\": \"phil.b@synsation.io\"\n" +
				"  }\n" +
				"}";

		String assetId = "/af69ee6f-b43e-415c-b1f0-9543e104466b";
		String eventPostfix = "/events";

		//given
		RequestSpecification requestSpecification = given().
				header("Content-Type", "application/json").
				header("Authorization", "Bearer " + jwtBearerAccessToken);

		//when
		Response response = requestSpecification.
				given().
				body(createEventJson).
				when().
				post(ARCHIVISTS_ASSETS_URL + assetId + eventPostfix);

		//then
		response.then().statusCode(400);
		Map<String, Object> asset = response.body().jsonPath().getMap("");
		assertThat(asset.get("code"), is(3));
		assertThat(asset.get("message"), is("behaviour not enabled for asset"));
	}

	//todo: move json to a file in folder with 0600 permissions
	//todo: need to check is why we are getting 404 error
	@Test
	public void shouldCreateEventWithEventAttributesAssetAttributes() {
		//given
		String createEventJson = "{\n" +
				"  \"identity\": \"assets/add30235-1424-4fda-840a-d5ef82c4c96f/events/11bf5b37-e0b8-42e0-8dcf-dc8c4aefc000\",\n" +
				"  \"asset_identity\": \"assets/add30235-1424-4fda-840a-d5ef82c4c96f\",\n" +
				"  \"operation\": \"Record\",\n" +
				"  \"behaviour\": \"RecordEvidence\",\n" +
				"  \"event_attributes\": {\n" +
				"    \"arc_display_type\": \"Safety Conformance\",\n" +
				"    \"arc_description\": \"Safety conformance approved for version 1.6. See attached conformance report\",\n" +
				"    \"arc_evidence\": \"DVA Conformance Report attached\",\n" +
				"    \"arc_attachments\": [\n" +
				"      {\n" +
				"        \"arc_display_name\": \"Conformance Report\",\n" +
				"        \"arc_attachment_identity\": \"blobs/e2a1d16c-03cd-45a1-8cd0-690831df1273\",\n" +
				"        \"arc_hash_value\": \"8a1eef8ab0ad431b7e2a900fc15ad8216f010fd8e4bf739604cec39fb1f94049\",\n" +
				"        \"arc_hash_alg\": \"SHA-256\"\n" +
				"      }]\n" +
				"  },\n" +
				"  \"asset_attributes\": {\n" +
				"    \"arc_attachments\": [\n" +
				"      {\n" +
				"        \"arc_display_name\": \"Latest Conformance Report\",\n" +
				"        \"arc_attachment_identity\": \"blobs/e2a1d16c-03cd-45a1-8cd0-690831df1273\",\n" +
				"        \"arc_hash_value\": \"8a1eef8ab0ad431b7e2a900fc15ad8216f010fd8e4bf739604cec39fb1f94049\",\n" +
				"        \"arc_hash_alg\": \"SHA-256\"\n" +
				"      }]\n" +
				"  },\n" +
				"  \"timestamp_accepted\": \"2019-11-27T15:13:21Z\",\n" +
				"  \"timestamp_declared\": \"2019-11-27T14:44:19Z\",\n" +
				"  \"timestamp_committed\": \"2019-11-27T15:15:02Z\",\n" +
				"  \"principal_declared\": {\n" +
				"    \"issuer\": \"idp.synsation.io/1234\",\n" +
				"    \"subject\": \"phil.b\",\n" +
				"    \"email\": \"phil.b@synsation.io\"\n" +
				"  },\n" +
				"  \"principal_accepted\": {\n" +
				"    \"issuer\": \"job.idp.server/1234\",\n" +
				"    \"subject\": \"bob@job\"\n" +
				"  },\n" +
				"  \"confirmation_status\": \"CONFIRMED\",\n" +
				"  \"block_number\": 12,\n" +
				"  \"transaction_index\": 5,\n" +
				"  \"transaction_id\": \"0x07569\"\n" +
				"}";

		String assetId = "/f5900d66-7492-4ede-94d8-121df81dbdd2";
		String eventPostfix = "/events";

		//given
		RequestSpecification requestSpecification = given().
				header("Content-Type", "application/json").
				header("Authorization", "Bearer " + jwtBearerAccessToken);

		//when
		Response response = requestSpecification.
				given().
				body(createEventJson).
				when().
				post(ARCHIVISTS_ASSETS_URL + assetId + eventPostfix);

		//then
		response.then().statusCode(404);
		Map<String, Object> asset = response.body().jsonPath().getMap("");
		assertThat(asset.get("code"), is(5));
		assertThat(asset.get("message"), is("attachment not found: blobs/e2a1d16c-03cd-45a1-8cd0-690831df1273"));	}

}