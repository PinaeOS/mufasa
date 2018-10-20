package org.pinae.mufasa.client;

import org.junit.Test;

public class UserTest {
	
	@Test
	public void testGetName() {
		User user = MufasaClient.execute(User.class, "http://127.0.0.1:8081");
		String userName = user.getName("1");
		System.out.println(userName);
	}
}
