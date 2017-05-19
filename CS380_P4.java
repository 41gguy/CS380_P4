import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream; 
import java.net.Socket;
import java.lang.StringBuilder;

public class CS380_P4 {
	public static void main(String[] args) {
		System.setProperty("http.proxyHost", "127.0.0.1");
	    System.setProperty("http.proxyPort", "38004");
	    
	    try {
	    	Socket socket = new Socket("localhost", 38004);
	        InputStream is = socket.getInputStream();
	        DataInputStream dis = new DataInputStream(is);
	        
	        
	        OutputStream os = socket.getOutputStream();
	        DataOutputStream dos = new DataOutputStream(os);
	        
	        int payloadlength = 2;
	       for (int i = 0; i < 12; i++) {
	    	   
	    	   int[] sentBytes = new int[40];
	    	   sentBytes = dataPackage(payloadlength, socket.getLocalAddress().getAddress(), socket.getInetAddress().getAddress());
	    	   for (int z = 0; z < 40; z++) {
	    		   dos.writeByte(sentBytes[z]);
	    		   
	    	   }
	    	   
	    	   System.out.println("Payload length: " + payloadlength);
	    	   payloadlength *= 2;
	    	   
	    	   try {
	    		   StringBuilder sb = new StringBuilder();
	    		   
	    		   for (int n = 0; n < 4; n++) {
	    			   int response = dis.readUnsignedByte();
	    			   if (response < 16) {
	    				   sb.append("0");
	    			   }
	    			   sb.append(Integer.toHexString(response));
	    		   }
		    	   String fullresponse = sb.toString();
		    	   System.out.println("Response: 0x" + fullresponse);
	    	   }
	    	   catch (Exception e) {
	    		   System.out.println("Error reading response.");
	    	   }
	       }
	       
	    
   		
	       
	       System.out.println();
	       System.out.println("Picture complete!");
	       
   
	        
	    }
	    catch (Exception e) {
	    	e.printStackTrace();
	    }
	}
	
	public static int[] dataPackage(int length, byte[] localaddress, byte[] remoteaddress) {
		 int[] bytepackage = new int[40];
     	bytepackage[0] = 96;
     	for (int v = 1; v < 4; v++) {
     		bytepackage[v] = 0;
     	}
     	
     	//variable payload length 
     	
     	if (length > 255) {
     		
     		bytepackage[4] = (length - length%256)/256;
     		bytepackage[5] = length%256;
     	}
     	else {
     		bytepackage[4] = 0;
     		bytepackage[5] = length%256;
     	}
     	
     	bytepackage[6] = 17;
     	bytepackage[7] = 20;
     	
     	for (int z = 8; z < 14; z++) {
     		bytepackage[z] = 0;
     	}
     	bytepackage[14] = 255;
     	bytepackage[15] = 255;
     	
         int m = 16;
         for (int i = 0; i < localaddress.length; i++) {
         	bytepackage[m] = 0;
         	m++; 
         	bytepackage[m] = localaddress[i];
         	m++; 
         }
         for (int z = 24; z < 30; z++) {
     		bytepackage[z] = 0;
     	}
         bytepackage[30] = 255;
         bytepackage[31] = 255;
         
         
         
         m = 32;
         for (int i = 0; i < remoteaddress.length; i++) {
         	bytepackage[m] = 0;
         	m++; 
         	bytepackage[m] = remoteaddress[i];
         	m++; 
         }
         
         return bytepackage;
	}
}
