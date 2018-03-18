package com.sp.sn;

import com.sp.sn.fm.FriendManagementApplication;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = FriendManagementApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FriendManagementApplicationTests {

	private final static String ver = "v1";
	private final static String ADD_FRIEND_URL = String.format("/api/%s/friends", ver);
	private final static String GET_FRIEND_URL = String.format("/api/%s/friends/all", ver);
	private final static String COMMON_FRIEND_URL = String.format("/api/%s/friends/common", ver);
	private final static String BLOCK_URL = String.format("/api/%s/person/block", ver);
	private final static String SUBSCRIBE_URL = String.format("/api/%s/subscriptions", ver);
	private final static String SUBSCRIBE_ELIGIBLE_URL = String.format("/api/%s/subscriptions/eligibility", ver);

	@LocalServerPort
	private int port;

	TestRestTemplate restTemplate = new TestRestTemplate();

	HttpHeaders headers = new HttpHeaders();

	@Before
	public void setup() {
		Locale.setDefault(new Locale("en", "US"));
		List<MediaType> mediaTypes = new ArrayList<>();
		mediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
		headers.setAccept(mediaTypes);
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
	}

	@Test
	public void aa1_AddFriends_invalidEmail() {
		Map<String, List<String>> map = new HashMap<>();
		List<String> emails = new ArrayList<>();
		emails.add("andy@.com");
		emails.add("john@example.com");
		map.put("friends", emails);
		ResponseEntity<String> response = requestToRestApi(HttpMethod.PUT, ADD_FRIEND_URL, map);
		String expected = "{\"status\":400,\"message\":\"must be a well-formed email address\"}";
		Assert.assertEquals("aa_AddFriends_invalidEmail", expected, response.getBody());
	}

	@Test
	public void aa2_AddFriends_notEnoughParams() {
		Map<String, List<String>> map = new HashMap<>();
		List<String> emails = new ArrayList<>();
		emails.add("john@example.com");
		map.put("friends", emails);
		ResponseEntity<String> response = requestToRestApi(HttpMethod.PUT, ADD_FRIEND_URL, map);
		String expected = "{\"status\":400,\"message\":\"Incorrect number of person in the request\"}";
		Assert.assertEquals("aa2_AddFriends_notEnoughParams", expected, response.getBody());
	}

	@Test
	public void aa_AddFriends_success() {
		Map<String, List<String>> map = new HashMap<>();
		List<String> emails = new ArrayList<>();
		emails.add("andy@example.com");
		emails.add("john@example.com");
		map.put("friends", emails);
		ResponseEntity<String> response = requestToRestApi(HttpMethod.PUT, ADD_FRIEND_URL, map);
		String expected = "{\"success\":true}";
		Assert.assertEquals("aa_AddFriends_success", expected, response.getBody());
	}

	@Test
	public void ab_AddFriends_personNotExist() {
		Map<String, List<String>> map = new HashMap<>();
		List<String> emails = new ArrayList<>();
		emails.add("lisa@example.com");
		emails.add("any@example.com");
		map.put("friends", emails);
		ResponseEntity<String> response = requestToRestApi(HttpMethod.PUT, ADD_FRIEND_URL, map);
		String expected = "{\"status\":404,\"message\":\"Person any@example.com does not exist\"}";
		Assert.assertEquals("testAddFriends_b_personNotExist", expected, response.getBody());
	}

	@Test
	public void ac_AddFriends_duplicate() {
		Map<String, List<String>> map = new HashMap<>();
		List<String> emails = new ArrayList<>();
		emails.add("john@example.com");
		emails.add("andy@example.com");
		map.put("friends", emails);
		ResponseEntity<String> response = requestToRestApi(HttpMethod.PUT, ADD_FRIEND_URL, map);
		String expected = "{\"status\":400,\"message\":\"Friendship already exists\"}";
		Assert.assertEquals("testAddFriends_c_duplicateAdd", expected, response.getBody());
	}

	@Test
	public void ba_GetAllFriends_success() {
		Map<String, List<String>> map = new HashMap<>();
		List<String> emails = new ArrayList<>();
		emails.add("john@example.com");
		emails.add("lisa@example.com");
		map.put("friends", emails);
		ResponseEntity<String> response = requestToRestApi(HttpMethod.PUT, ADD_FRIEND_URL, map);
		String expected = "{\"success\":true}";
		Assert.assertEquals("testGetAllFriends_a_success", expected, response.getBody());

		Map<String, String> getReq = new HashMap<>();
		getReq.put("email", "john@example.com");
		response = requestToRestApi(HttpMethod.PUT, GET_FRIEND_URL, getReq);
		expected = "{\"success\":true,\"friends\":[\"lisa@example.com\",\"andy@example.com\"],\"count\":2}";
		Assert.assertEquals("ba_GetAllFriends_success", expected, response.getBody());
	}

	@Test
	public void bb_GetAllFriends_personNotExist() {
		Map<String, String> getReq = new HashMap<>();
		getReq.put("email", "any@example.com");
		ResponseEntity<String> response = requestToRestApi(HttpMethod.PUT, GET_FRIEND_URL, getReq);
		String expected = "{\"status\":404,\"message\":\"Person any@example.com does not exist\"}";
		Assert.assertEquals("bb_GetAllFriends_personNotExist", expected, response.getBody());
	}

	@Test
	public void bc_GetAllFriends_hasNoFriends() {
		Map<String, String> getReq = new HashMap<>();
		getReq.put("email", "jike@example.com");
		ResponseEntity<String> response = requestToRestApi(HttpMethod.PUT, GET_FRIEND_URL, getReq);
		String expected = "{\"status\":400,\"message\":\"jike@example.com has no friends\"}";
		Assert.assertEquals("bc_GetAllFriends_hasNoFriends", expected, response.getBody());
	}

	@Test
	public void ca_GetCommonFriends_success() {
		Map<String, List<String>> map = new HashMap<>();
		List<String> emails = new ArrayList<>();
		emails.add("andy@example.com");
		emails.add("lisa@example.com");
		map.put("friends", emails);
		ResponseEntity<String> response = requestToRestApi(HttpMethod.PUT, COMMON_FRIEND_URL, map);
		String expected = "{\"success\":true,\"friends\":[\"john@example.com\"],\"count\":1}";
		Assert.assertEquals("ca_GetCommonFriends_success", expected, response.getBody());
	}

	@Test
	public void cb_GetCommonFriends_noCommonFriends() {
		Map<String, List<String>> map = new HashMap<>();
		List<String> emails = new ArrayList<>();
		emails.add("andy@example.com");
		emails.add("jike@example.com");
		map.put("friends", emails);
		ResponseEntity<String> response = requestToRestApi(HttpMethod.PUT, COMMON_FRIEND_URL, map);
		String expected = "{\"status\":404,\"message\":\"No common friends found\"}";
		Assert.assertEquals("cb_GetCommonFriends_noCommonFriends", expected, response.getBody());
	}

	@Test
	public void cc_GetCommonFriends_moreThanOne() {
		Map<String, List<String>> map = new HashMap<>();
		List<String> emails = new ArrayList<>();
		emails.add("andy@example.com");
		emails.add("susan@example.com");
		map.put("friends", emails);
		ResponseEntity<String> response = requestToRestApi(HttpMethod.PUT, ADD_FRIEND_URL, map);
		String expected = "{\"success\":true}";
		Assert.assertEquals("cc_GetCommonFriends_moreThanOne", expected, response.getBody());

		map = new HashMap<>();
		emails = new ArrayList<>();
		emails.add("susan@example.com");
		emails.add("lisa@example.com");
		map.put("friends", emails);
		response = requestToRestApi(HttpMethod.PUT, ADD_FRIEND_URL, map);
		expected = "{\"success\":true}";
		Assert.assertEquals("cc_GetCommonFriends_moreThanOne", expected, response.getBody());

		map = new HashMap<>();
		emails = new ArrayList<>();
		emails.add("andy@example.com");
		emails.add("lisa@example.com");
		map.put("friends", emails);
		response = requestToRestApi(HttpMethod.PUT, COMMON_FRIEND_URL, map);
		expected = "{\"success\":true,\"friends\":[\"susan@example.com\",\"john@example.com\"],\"count\":2}";
		Assert.assertEquals("cc_GetCommonFriends_moreThanOne", expected, response.getBody());
	}

	@Test
	public void da_Block_success() {
		Map<String, String> map = new HashMap<>();
		map.put("requestor", "susan@example.com");
		map.put("target", "george@example.com");
		ResponseEntity<String> response = requestToRestApi(HttpMethod.PUT, BLOCK_URL, map);
		String expected = "{\"success\":true}";
		Assert.assertEquals("da_Block_success", expected, response.getBody());
	}

	@Test
	public void db_Block_duplicate() {
		Map<String, String> map = new HashMap<>();
		map.put("requestor", "susan@example.com");
		map.put("target", "george@example.com");
		ResponseEntity<String> response = requestToRestApi(HttpMethod.PUT, BLOCK_URL, map);
		String expected = "{\"status\":400,\"message\":\"george@example.com has already been blocked by susan@example.com\"}";
		Assert.assertEquals("db_Block_duplicate", expected, response.getBody());
	}

	@Test
	public void dc_Block_existFriends() {
		Map<String, String> map = new HashMap<>();
		map.put("requestor", "lisa@example.com");
		map.put("target", "john@example.com");
		ResponseEntity<String> response = requestToRestApi(HttpMethod.PUT, BLOCK_URL, map);
		String expected = "{\"success\":true}";
		Assert.assertEquals("dc_Block_existFriends", expected, response.getBody());
	}

	@Test
	public void ea_Subscribe_toNonFriend() {
		Map<String, String> map = new HashMap<>();
		map.put("requestor", "lisa@example.com");
		map.put("target", "jike@example.com");
		ResponseEntity<String> response = requestToRestApi(HttpMethod.PUT, SUBSCRIBE_URL, map);
		String expected = "{\"success\":true}";
		Assert.assertEquals("ea_Subscribe_toNonFriend", expected, response.getBody());
	}

	@Test
	public void eaa_Subscribe_toNonFriend_revese() {
		Map<String, String> map = new HashMap<>();
		map.put("requestor", "jike@example.com");
		map.put("target", "lisa@example.com");
		ResponseEntity<String> response = requestToRestApi(HttpMethod.PUT, SUBSCRIBE_URL, map);
		String expected = "{\"success\":true}";
		Assert.assertEquals("eaa_Subscribe_toNonFriend_revese", expected, response.getBody());
	}

	@Test
	public void eab_Subscribe_toBlocked() {
		Map<String, String> map = new HashMap<>();
		map.put("requestor", "john@example.com");
		map.put("target", "lisa@example.com");
		ResponseEntity<String> response = requestToRestApi(HttpMethod.PUT, SUBSCRIBE_URL, map);
		String expected = "{\"status\":400,\"message\":\"john@example.com connection to lisa@example.com is blocked\"}";
		Assert.assertEquals("eab_Subscribe_toBlocked", expected, response.getBody());
	}

	@Test
	public void eb_Subscribe_duplicate() {
		Map<String, String> map = new HashMap<>();
		map.put("requestor", "lisa@example.com");
		map.put("target", "jike@example.com");
		ResponseEntity<String> response = requestToRestApi(HttpMethod.PUT, SUBSCRIBE_URL, map);
		String expected = "{\"status\":400,\"message\":\"lisa@example.com has already subscribed to the jike@example.com\"}";
		Assert.assertEquals("eb_Subscribe_duplicate", expected, response.getBody());
	}

	@Test
	public void ec_Subscribe_toFriend() {
		Map<String, String> map = new HashMap<>();
		map.put("requestor", "lisa@example.com");
		map.put("target", "susan@example.com");
		ResponseEntity<String> response = requestToRestApi(HttpMethod.PUT, SUBSCRIBE_URL, map);
		String expected = "{\"success\":true}";
		Assert.assertEquals("ec_Subscribe_toFriend", expected, response.getBody());
	}


	@Test
	public void fa_SubscribeEligible_success() {
		Map<String, String> map = new HashMap<>();
		map.put("sender", "lisa@example.com");
		map.put("text", "greeting!!!");
		ResponseEntity<String> response = requestToRestApi(HttpMethod.PUT, SUBSCRIBE_ELIGIBLE_URL, map);
		String expected = "{\"success\":true,\"recipients\":[\"susan@example.com\",\"jike@example.com\"]}";
		Assert.assertEquals("fa_SubscribeEligible_success", expected, response.getBody());
	}

	@Test
	public void fb_SubscribeEligible_noEligiblePersons() {
		Map<String, String> map = new HashMap<>();
		map.put("sender", "george@example.com");
		map.put("text", "greeting!!!");
		ResponseEntity<String> response = requestToRestApi(HttpMethod.PUT, SUBSCRIBE_ELIGIBLE_URL, map);
		String expected = "{\"status\":404,\"message\":\"No persons subscribed to george@example.com\"}";
		Assert.assertEquals("fa_SubscribeEligible_noEligiblePersons", expected, response.getBody());
	}

	private String createURLWithPort(String uri) {
		return "http://localhost:" + port + uri;
	}

	private ResponseEntity<String> requestToRestApi(HttpMethod method, String url, Map map) {
		HttpEntity<String> entity = new HttpEntity<String>(new JSONObject(map).toString(), headers);
		return restTemplate.exchange(createURLWithPort(url),
				method, entity, String.class);
	}
}
