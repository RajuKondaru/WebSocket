package com.android.screenshot;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.RawImage;
import com.android.ddmlib.TimeoutException;
import java.awt.image.BufferedImage;
import java.io.IOException;
/**
* Connects to a device using ddmlib and dumps its event log as long as the device is connected.
*/
public class Screenshot {
	
	/*
	* Grab an image from an ADB-connected device.
	*/
	public static BufferedImage getDeviceImage(IDevice device, boolean landscape)	throws IOException {
		RawImage rawImage = null;
		try {
			
			rawImage = device.getScreenshot();
		} catch (TimeoutException e) {
			System.err.println("Unable to get frame buffer: timeout");
			
		} catch (Exception ioe) {
			System.err.println("Unable to get frame buffer: " + ioe.getMessage());
			
		}
		// device/adb not available?
		
		if (landscape) {
			rawImage = rawImage.getRotated();
		}
		// convert raw data to an Image
		BufferedImage image = new BufferedImage(rawImage.width, rawImage.height, BufferedImage.TYPE_INT_ARGB);
		int index = 0;
		int IndexInc = rawImage.bpp >> 3;
		for (int y = 0 ; y < rawImage.height ; y++) {
			for (int x = 0 ; x < rawImage.width ; x++) {
				int value = rawImage.getARGB(index);
				index += IndexInc;
				image.setRGB(x, y, value);
			}
		}
		/*if (!ImageIO.write(image, "png", new File(filepath))) {
			throw new IOException("Failed to find png writer");
		}*/
		return image;
	}
	
	public static void printAndExit(String message, boolean terminate) {
		System.out.println(message);
		if (terminate) {
		//	AndroidDebugBridge.terminate();
		}
		//System.exit(1);
	}
}