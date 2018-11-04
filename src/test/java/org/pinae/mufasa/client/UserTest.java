package org.pinae.mufasa.client;

import org.junit.Test;
import org.pinae.mufasa.client.codec.decoder.JsonDecoder;
import org.pinae.mufasa.client.codec.encoder.JsonEncoder;

public class UserTest {
	
	@Test
	public void testGetName() {
		UserService userService = MufasaClient.builder().encoder(new JsonEncoder()).decoder(new JsonDecoder()).host("http://127.0.0.1:8081").build(UserService.class);
		User user = userService.getUserById("1");
		
		System.out.println(user.getUserName());
	}
}
