package com.websocket;

import java.io.IOException;

import javax.net.websocket.Endpoint;
import javax.net.websocket.Session;

public class HelloClient extends Endpoint {

	@Override
	public void onOpen(Session session) {
		// TODO Auto-generated method stub
		try {
			session.getRemote().sendString("Hello You!!!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

}
