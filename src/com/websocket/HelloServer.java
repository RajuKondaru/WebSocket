package com.websocket;

import java.io.IOException;

import javax.net.websocket.Endpoint;
import javax.net.websocket.MessageHandler;
import javax.net.websocket.Session;

public class HelloServer extends Endpoint {
	@Override
	public void onOpen(final Session session) {
		// TODO Auto-generated method stub
				session.addMessageHandler(new MessageHandler.Text(){
					public void onMessage(String text) {
						System.out.println("Someone said ::" +text );
						
						try {
							session.getRemote().sendString(text+"( From Server) ");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
	}

	

}
