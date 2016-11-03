package com.jetty.websocket;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class FigureEncoder implements Encoder.Text<Figure> {

	@Override
    public void init(EndpointConfig ec) {
       // System.out.println("init");
    }

    @Override
    public void destroy() {
        //System.out.println("destroy");
    }
    
	@Override
	public String encode(Figure figure) throws EncodeException {
		// TODO Auto-generated method stub
		return figure.getJson().toString();
	}
    
}