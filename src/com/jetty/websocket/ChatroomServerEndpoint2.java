package com.jetty.websocket;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.imageio.ImageIO;
import javax.json.JsonValue;
import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.catalina.util.Base64;
import org.json.simple.JSONObject;

import com.android.ddmlib.AdbCommandRejectedException;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.ShellCommandUnresponsiveException;
import com.android.ddmlib.TimeoutException;
import com.android.screenshot.ADB;
import com.android.screenshot.Screenshot;

import sun.misc.BASE64Encoder;


@ServerEndpoint(value="/chatroom2", encoders = {FigureEncoder.class}, decoders = {FigureDecoder.class})
public class ChatroomServerEndpoint2 {
	static Set<Session> chatroomusers=	Collections.synchronizedSet(new HashSet<Session>());
	public List<IDevice> deviceList= new ArrayList<IDevice>();
	public JSONObject device;
	public JSONObject devices;
	public List<String> deviceNameList= new ArrayList<String>();
	public String  imgstr = null;
	public BufferedImage image = null;
	public byte[] imgBytes=null;
	public ByteBuffer byteBuffer= null;
	public int i;
	public boolean isTheardCreaded= false;
	public IDevice idevice = null;
//	public String filepath = FileUtils.getUserDirectory()+"/screencap.png";
	@SuppressWarnings("unchecked")
	@OnOpen
	public void onOpen(Session userSession, EndpointConfig arg1) throws IOException {
		// TODO Auto-generated method stub
		System.out.println("WebSocket opened: " + userSession.getQueryString());
		chatroomusers.add(userSession);
		System.out.println("Connected ::"+ userSession.toString());
		System.out.println(userSession.getId());
		Iterator<Session> itr =chatroomusers.iterator();
		System.out.println(itr.hasNext());
		while (itr.hasNext()) {
			if(itr.next().getId()==userSession.getId()){ 
				ADB adb= new ADB();
		    	if (!adb.initialize(null)) {
					System.out.println("Could not find adb, please install Android SDK and set path to adb.");
				}
				IDevice[] idevices = adb.getDevices();
				device = new JSONObject();
				devices = new JSONObject();
				if (devices != null) {
					//ArrayList<String> list = new ArrayList<String>();
					for ( i = 0; i < idevices.length; i++) {
						idevice = idevices[i];
						String deviceID= idevice.getSerialNumber();
						String deviceName= idevice.getName();
						System.out.println("deviceName:: "+deviceName);
						System.out.println("deviceID:: "+deviceID);
						deviceList.add(idevice);
						deviceNameList.add(deviceID);
						
						device.put(deviceID, deviceName);
						
					}
					devices.put("devices", device);
					System.out.println(devices.toJSONString());
					userSession.getAsyncRemote().sendText(devices.toJSONString());
				}
			}
		}
	}
	@OnClose
	public void onClose(Session session, CloseReason closeReason){
		System.out.println("Connection Closed ::"+ closeReason);
		//Screenshot.printAndExit("Connection Closed", true);
	}
	@OnError
    public void onError(Session session, Throwable thr){
		thr.printStackTrace();
		//System.out.println("Connection Error ::" );
    }
  
 	@OnMessage
    public void onMessage(Figure figure, final Session userSession) throws IOException, InterruptedException, 
    		TimeoutException, AdbCommandRejectedException, ShellCommandUnresponsiveException, InvocationTargetException{
 		JsonValue deviceID = null;
 		if(figure!=null){
			System.out.println(figure.getJson().toString());
			if(figure.getJson().containsKey("x")){
				figure.getJson().get("x");
				//System.out.println("X:: "+figure.getJson().get("x"));
			}
			if(figure.getJson().containsKey("y")){
				figure.getJson().get("y");
				//System.out.println("Y:: "+figure.getJson().get("y"));
			}
			if(figure.getJson().containsKey("x1")){
				figure.getJson().get("x1");
				//System.out.println("X1:: "+figure.getJson().get("x1"));
			}
			if(figure.getJson().containsKey("y1")){
				figure.getJson().get("y1");
				//System.out.println("Y1:: "+figure.getJson().get("y1"));
			}
			if(figure.getJson().containsKey("x2")){
				figure.getJson().get("x2");
				//System.out.println("X2:: "+figure.getJson().get("x2"));
			}
			if(figure.getJson().containsKey("y2")){
				figure.getJson().get("y2");
				//System.out.println("Y2:: "+figure.getJson().get("y2"));
			}
			if(figure.getJson().containsKey("deviceid")){
				deviceID=figure.getJson().get("deviceid");
				System.out.println("JsonValue "+deviceID.toString());
				if(deviceNameList.contains(deviceID.toString())){
					for (final IDevice device : deviceList) {
						if(device.getSerialNumber().equalsIgnoreCase(deviceID.toString())){
							new Thread(new Runnable() {
								@Override
								public void run() {
									 synchronized(this){
										 isTheardCreaded = true;
											while (isTheardCreaded) {
												/*try {
													Thread.sleep(2000);
												} catch (InterruptedException e1) {
													// TODO Auto-generated catch block
													e1.printStackTrace();
												}*/
												// TODO Auto-generated method stub
												try {
													image  = Screenshot.getDeviceImage(device,  false);
												} catch (IOException e) {
													// TODO Auto-generated catch block
													e.printStackTrace();
												}
												/*Image in Base64 String*/
												/*ByteArrayOutputStream baos = new ByteArrayOutputStream();
										        try {
										        ImageIO.write( image, "jpg", baos );
										        } catch( IOException ioe ) {
										        }
											 
											    String b64image = Base64.encode( baos.toByteArray() );*/
												
												/*Image in Bytes*/
												imgBytes = encodeToBytes(image, "png");
												byteBuffer = ByteBuffer.wrap(imgBytes);
											
												try {
													if(chatroomusers.contains(userSession)){
														//userSession.getAsyncRemote().sendText(b64image);
														userSession.getAsyncRemote().sendBinary(byteBuffer);
														//System.out.println("Image Sended.. From theard");
														byteBuffer.clear();
													}
												} catch(IllegalStateException e){
													isTheardCreaded = false;
													System.out.println("Connection Disconnected");
												}
											}
									 }
								}
							}).start();
						}
					}
				}
			
			}
			
			try{
				if(figure.getJson().containsKey("action")){
					JsonValue action= figure.getJson().get("action");
					//System.out.println("action:: "+action.toString());
					if(action.toString().contains("tap")){
						idevice.executeShellCommand("input tap "+figure.getJson().get("x")+" "+figure.getJson().get("y"), null);
					}else if(action.toString().contains("swipe")){
						idevice.executeShellCommand("input swipe "+figure.getJson().get("x1")+" "+figure.getJson().get("y1")+
								" "+figure.getJson().get("x2")+" "+figure.getJson().get("y2"), null);	
					}
				}
			}catch(NullPointerException e){
				
			}catch(Exception e){
				if(e.getMessage().contains("device offline")){
					System.out.println("device is offline");
				}else {
					e.printStackTrace();
				}
				
			}
			
		}
 		
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
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] imageBytes = null;
        try {
            ImageIO.write(image, type, bos);
            imageBytes = bos.toByteArray();
            //System.out.println(imageBytes.length);
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageBytes;
    }

    public static void main1(String[] args) throws InterruptedException, IOException {
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
