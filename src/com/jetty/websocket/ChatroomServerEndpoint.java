package com.jetty.websocket;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.commons.io.FileUtils;

import com.android.ddmlib.AdbCommandRejectedException;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.ShellCommandUnresponsiveException;
import com.android.ddmlib.TimeoutException;
import com.android.screenshot.ADB;
import com.android.screenshot.Screenshot;

import sun.misc.BASE64Encoder;
@ServerEndpoint(value="/chatroom", encoders = {FigureEncoder.class}, decoders = {FigureDecoder.class})
public class ChatroomServerEndpoint {
	static Set<Session> chatroomusers=	Collections.synchronizedSet(new HashSet<Session>());
	public String  imgstr = null;
	public BufferedImage image = null;
	public byte[] imgBytes=null;
	public ByteBuffer byteBuffer= null;
	public int i;
	public boolean isTheardCreaded= false;
	public IDevice device = null;
	public String filepath = FileUtils.getUserDirectory()+"/screencap.png";
	@OnOpen
	public void onOpen(Session userSession, EndpointConfig arg1) {
		// TODO Auto-generated method stub
		System.out.println("WebSocket opened: " + userSession.getId());

		chatroomusers.add(userSession);
		System.out.println("Connected ::"+ userSession.toString());
		
		
		
		
	}
	@OnClose
	public void onClose(Session session,
		      CloseReason closeReason){
		
		System.out.println("Connection Closed ::"+ closeReason);
	}
	@OnError
    public void onError(Session session,
      Throwable thr){
		thr.printStackTrace();
    }
   /* @OnMessage
    public String onMessage(String message, Session userSession) throws IOException, InterruptedException{
    	String  imgstr = null;
    	BufferedImage image = null;
    	
    	String filepath = FileUtils.getUserDirectory()+"/screencap.png";
    	System.out.println(userSession.getId());
		Iterator<Session> itr =chatroomusers.iterator();
		System.out.println(itr.hasNext());
		while (itr.hasNext()) {
			if(itr.next().getId()==userSession.getId()){ 
				ADB adb= new ADB();
		    	if (!adb.initialize(null)) {
					System.out.println("Could not find adb, please install Android SDK and set path to adb.");
					
				}
				IDevice[] devices = adb.getDevices();
				if (devices != null) {
					//ArrayList<String> list = new ArrayList<String>();
					for (int i = 0; i < devices.length; i++) {
						image  = Screenshot.getDeviceImage(devices[i], filepath, false);
						imgstr = encodeToString(image, "png");
						//userSession.getBasicRemote().sendText(imgstr);
					}
				}
			}
		}
		return imgstr;
	}*/
	 	@OnMessage
	    public void onMessage(Figure figure, final Session userSession) throws IOException, InterruptedException, TimeoutException, AdbCommandRejectedException, ShellCommandUnresponsiveException{
	    	
	    	System.out.println(userSession.getId());
			Iterator<Session> itr =chatroomusers.iterator();
			System.out.println(itr.hasNext());
			while (itr.hasNext()) {
				if(itr.next().getId()==userSession.getId()){ 
					ADB adb= new ADB();
			    	if (!adb.initialize(null)) {
						System.out.println("Could not find adb, please install Android SDK and set path to adb.");
						
					}
					IDevice[] devices = adb.getDevices();
					
					if (devices != null) {
						//ArrayList<String> list = new ArrayList<String>();
						for ( i = 0; i < devices.length; i++) {
							if(figure!=null){
								 device = devices[i];
								System.out.println(figure.getJson().toString());
								if(figure.getJson().containsKey("x")){
									figure.getJson().get("x");
									System.out.println("X:: "+figure.getJson().get("x"));
								}
								if(figure.getJson().containsKey("y")){
									figure.getJson().get("y");
									System.out.println("Y:: "+figure.getJson().get("y"));
								}
								try{
									device.executeShellCommand("input tap "+figure.getJson().get("x")+" "+figure.getJson().get("y"), null);	
								}catch(Exception e){
									System.out.println(e.toString());
								}
								
							}
							if(!isTheardCreaded){
								new Thread(new Runnable() {
									@Override
									public void run() {
										 synchronized(this){
											 isTheardCreaded = true;
												while (isTheardCreaded) {
													try {
														Thread.sleep(1000);
													} catch (InterruptedException e1) {
														// TODO Auto-generated catch block
														e1.printStackTrace();
													}
													// TODO Auto-generated method stub
													
													try {
														image  = Screenshot.getDeviceImage(device, false);
													} catch (IOException e) {
														// TODO Auto-generated catch block
														e.printStackTrace();
													}
													imgBytes = encodeToBytes(image, "png");
													byteBuffer = ByteBuffer.wrap(imgBytes);
													//userSession.getBasicRemote().sendBinary(byteBuffer);
													try {
														userSession.getBasicRemote().sendBinary(byteBuffer);
														System.out.println("Image Sended.. From theard");
													} catch (IOException e) {
														// TODO Auto-generated catch block
														e.printStackTrace();
													}
												
												}
										 }
										
									}
								}).start();
								
							}
							
							
							
							
						}
					}
				}
			}
			//return byteBuffer;
		}
    public static String encodeToString(BufferedImage image, String type) {
        String imageString = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            ImageIO.write(image, type, bos);
            byte[] imageBytes = bos.toByteArray();

            BASE64Encoder encoder = new BASE64Encoder();
            imageString = encoder.encode(imageBytes);

            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageString;
    }
    public static byte[] encodeToBytes(BufferedImage image, String type) {
        String imageString = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] imageBytes = null;
        try {
            ImageIO.write(image, type, bos);
            imageBytes = bos.toByteArray();
            System.out.println(imageBytes.length);
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageBytes;
    }

    public static void main(String[] args) throws InterruptedException, IOException {
    	ADB adb= new ADB();
    	if (!adb.initialize(null)) {
			System.out.println("Could not find adb, please install Android SDK and set path to adb.");
			
		}
		IDevice[] devices = adb.getDevices();
		if (devices != null) {
			//ArrayList<String> list = new ArrayList<String>();
			for (int i = 0; i < devices.length; i++) {
				System.out.println(devices[i].toString());
			}
		}
		
	}
}
