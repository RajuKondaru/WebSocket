
package test;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.zip.GZIPOutputStream;

import javax.imageio.ImageIO;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class ImageCompression {
	//test code .....
    public static void main(String[] args) {
        try {
            // TODO code application logic here
            long start = Calendar.getInstance().getTimeInMillis();
            BufferedImage oldImage = ImageIO.read(new File(
                    "C:\\Users\\developer1\\Desktop\\sample\\image.png"));
            BufferedImage newImage = ImageIO.read(new File(
                                         "C:\\Users\\developer1\\Desktop\\sample\\image2.png"));
            ByteArrayOutputStream oldBaos = new ByteArrayOutputStream();
            ByteArrayOutputStream newBaos = new ByteArrayOutputStream();
            ImageIO.write( oldImage, "jpg", oldBaos );
            ImageIO.write( newImage, "jpg", newBaos );
            oldBaos.flush();
            newBaos.flush();
            byte[] olddata = oldBaos.toByteArray();
            byte[] newdata = newBaos.toByteArray();
            //list will be perfect to get me the actual size that i should reserve for the xor byte[]
            ArrayList<Byte> list = new ArrayList<Byte>();
            System.out.println( olddata.length + " " + newdata.length);
            for (int i = 0; i < olddata.length; i++) {
                //i have to handle the exception becoz size of old byte[](olddata)
                //is != size of new byte[](newdata)
                try{
                byte by = (byte) (olddata[i] ^ newdata[i]);
                list.add(by);
                }catch(ArrayIndexOutOfBoundsException ex){
                    break;
                }
                
            
            }
            byte[] xorData = new byte[list.size()];
            for (int i = 0; i < list.size(); i++) {
                xorData[i] = (byte) list.get(i);
//                System.out.println( xorData[i] );
            }
            
            ByteArrayOutputStream compressBaos = new ByteArrayOutputStream();
            GZIPOutputStream gZIPOutputStream = new GZIPOutputStream( compressBaos );
            gZIPOutputStream.write(xorData);
            gZIPOutputStream.flush();
            byte[] compressedBytes = compressBaos.toByteArray();
            long end = Calendar.getInstance().getTimeInMillis();
            System.out.println( "Time taken = " + (end - start) + " "  + compressedBytes.length);
            
        } catch (IOException ex) {
            Logger.getLogger(ImageCompression.class.getName()).log(Level.ALL, null, ex);
        }
        
    }


}
