package com.jetty.websocket;

import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonException;
import javax.json.JsonObject;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

public class FigureDecoder implements Decoder.Text<Figure> {
	@Override
    public Figure decode(String string) throws DecodeException {
        JsonObject jsonObject = Json.createReader(new StringReader(string)).readObject();
        return  new Figure(jsonObject);
    }

    @Override
    public boolean willDecode(String string) {
        try {
            Json.createReader(new StringReader(string)).readObject();
            return true;
        } catch (JsonException ex) {
            ex.printStackTrace();
            return false;
        }
    
    }

    @Override
    public void init(EndpointConfig ec) {
        //System.out.println("init");
    }

    @Override
    public void destroy() {
        //System.out.println("destroy");
    }
    
}