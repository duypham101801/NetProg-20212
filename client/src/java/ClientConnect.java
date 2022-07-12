package client;

import java.io.*;
import java.net.*;
import java.util.Scanner;
  
public class ClientConnect
{
    final static int ServerPort = 8080;
  
    @SuppressWarnings("resource")
	public static void main(String args[]) throws UnknownHostException, IOException 
    {
        Scanner scn = new Scanner(System.in);
          
        // getting localhost ip
        //InetAddress ip = InetAddress.getByName("localhost");
        byte[] ipAddr = new byte[] {(byte) 192, (byte) 168, (byte) 191, (byte) 130};
        InetAddress ip = InetAddress.getByAddress(ipAddr);
          
        // establish the connection
        Socket s = new Socket(ip, ServerPort);
          
        // obtaining input and out streams
        InputStreamReader dis = new InputStreamReader(s.getInputStream());
        // DataInputStream dis = new DataInputStream(s.getInputStream());
        OutputStreamWriter dos = new OutputStreamWriter(s.getOutputStream());
  
        // read message to deliver
     	String msg = null;
     	String reply = null;
     	boolean keySympReq = false;
     	boolean keyAnsReq = false;
     	boolean keyDiag = false;
     	boolean keyChat = false;
     	
     	/*==================================================================================*/
     	// format for login/registration message
        System.out.println("Client login to system!\nEnter username: ");
        String username = scn.nextLine();
        // System.out.println(username);
        System.out.println("Enter password: ");
        String password = scn.nextLine();
        // System.out.println(password);
        msg = "USERPASS user=<" + username + "> pass=<" + password +">";
         		
        try {
         	// write on output stream
         	System.out.println("Message from client: " + msg);
         	dos.write(msg);
         	dos.flush();
         	msg = "";
         	// System.out.println("Check dos: " + dos);
         	// dos.close();
         			
         	// System.out.println("Socket closed: " + s.isClosed());
         			
         	// read message to this client
     		BufferedReader br = new BufferedReader(dis);
     		char[] buffer = new char[10000];
     		int count = br.read(buffer, 0, 10000);
     		reply = new String(buffer, 0, count);
     		System.out.println("Message to client: " + reply);
     		if (reply.contains("true")) {
     			keySympReq = true;
     		}
     		// dos.close();
     			dis.ready();
        } catch (IOException e) {
         	e.printStackTrace();
         	return ;
        }
        
        /*==========================================================================================*/
        if (keySympReq) {
        	System.out.println("Login successfully!\n");
        	msg = "SYMP_REQ";
     		
            try {
             	// write on output stream
             	System.out.println("Message from client: " + msg);
             	dos.write(msg);
             	dos.flush();
             	// dos.close();	
             	// System.out.println("Socket closed: " + s.isClosed());
             			
             	// read message to this client
         		BufferedReader br = new BufferedReader(dis);
         		char[] buffer = new char[10000];
         		int count = br.read(buffer, 0, 10000);
         		reply = new String(buffer, 0, count);
         		System.out.println("Message to client: " + reply + "\n");
         		// dos.close();
         		dis.ready();
            } catch (IOException e) {
             	e.printStackTrace();
             	return ;
            }
        } else {
        	System.out.println("Login failed!");
        	return ;
        }
        /*=======================================================================================*/
        System.out.println("Waiting for symptom form filled ...!");
    	msg = "SYMP_SUBMIT <index symptom 1> - <index symptom 2> - <> -";
 		
        try {
         	// write on output stream
         	System.out.println("Message from client: " + msg);
         	dos.write(msg);
         	dos.flush();
         	// dos.close();	
         	// System.out.println("Socket closed: " + s.isClosed());
         			
         	// read message to this client
     		BufferedReader br = new BufferedReader(dis);
     		char[] buffer = new char[10000];
     		int count = br.read(buffer, 0, 10000);
     		reply = new String(buffer, 0, count);
     		System.out.println("Message to client: " + reply + "\n");
     		if (reply.contains("true")) {
     			keyAnsReq = true;
     		}
     		// dos.close();
     		dis.ready();
        } catch (IOException e) {
         	e.printStackTrace();
         	return ;
        }
        /*===========================================================================================*/
        if (keyAnsReq) {
        	System.out.println("Symptom form submit successully!");
        	msg = "ANS_REQ";
        	
        	try {
             	// write on output stream
             	System.out.println("Message from client: " + msg);
             	dos.write(msg);
             	dos.flush();
             	// dos.close();	
             	// System.out.println("Socket closed: " + s.isClosed());
             			
             	// read message to this client
         		BufferedReader br = new BufferedReader(dis);
         		char[] buffer = new char[10000];
         		int count = br.read(buffer, 0, 10000);
         		reply = new String(buffer, 0, count);
         		System.out.println("Message to client: " + reply + "\n");
         		// dos.close();
         		dis.ready();
            } catch (IOException e) {
             	e.printStackTrace();
             	return ;
            }
        } else {
        	System.out.println("Symptom form submit failed!");
        	return;
        }
        /*===================================================================================*/
        System.out.println("Waiting for answer form filled ...!");
    	msg = "ANS_SUBMIT <index symptom 1> <answer list 1> - <index symptom 2> <answer list 2> - <> <> -";
 		
        try {
         	// write on output stream
         	System.out.println("Message from client: " + msg);
         	dos.write(msg);
         	dos.flush();
         	// dos.close();	
         	// System.out.println("Socket closed: " + s.isClosed());
         			
         	// read message to this client
     		BufferedReader br = new BufferedReader(dis);
     		char[] buffer = new char[10000];
     		int count = br.read(buffer, 0, 10000);
     		reply = new String(buffer, 0, count);
     		System.out.println("Message to client: " + reply + "\n");
     		if (reply.contains("true")) {
     			keyDiag = true;
     		}
     		// dos.close();
     		dis.ready();
        } catch (IOException e) {
         	e.printStackTrace();
         	return ;
        }
        /*======================================================================================*/
        if (keyDiag) {
        	System.out.println("Answer form submit successfully!");
        	msg = "DIG_REQ";
        	
        	try {
             	// write on output stream
             	System.out.println("Message from client: " + msg);
             	dos.write(msg);
             	dos.flush();
             	// dos.close();	
             	// System.out.println("Socket closed: " + s.isClosed());
             			
             	// read message to this client
         		BufferedReader br = new BufferedReader(dis);
         		char[] buffer = new char[10000];
         		int count = br.read(buffer, 0, 10000);
         		reply = new String(buffer, 0, count);
         		System.out.println("Message to client: " + reply + "\n");
         		// dos.close();
         		dis.ready();
            } catch (IOException e) {
             	e.printStackTrace();
             	return ;
            }
        } else {
        	System.out.println("Answer form submit failed!");
        	return;
        }
        /*=================================================================================*/
        // update keyChat through click action
        
        if (keyChat) {
        	// start chat server
        	// wait for assign doctor - patient
        	System.out.println("Patient wants to chat directly to doctor!");
        	msg = "CONSULT_REQ ";
        } else {
        	// patients/doctor leave
        	System.out.println("Patient leaves the room!");
        	msg = "QUIT";
        }
        
        try {
         	// write on output stream
         	System.out.println("Message from client: " + msg);
         	dos.write(msg);
         	dos.flush();
         	// dos.close();	
         	// System.out.println("Socket closed: " + s.isClosed());
         			
         	// read message to this client
     		BufferedReader br = new BufferedReader(dis);
     		char[] buffer = new char[10000];
     		int count = br.read(buffer, 0, 10000);
     		reply = new String(buffer, 0, count);
     		System.out.println("Message to client: " + reply + "\n");
     		if (reply.contains("QUIT_ACCEPT")) {
     			System.out.println("Patient leaves successfully! Close socket to server!");
     			s.close();
     		}
     		else if (reply.contains("success")) {
     			System.out.println("Patient connects successfully! Waiting for assign doctor ...");
     			// chat action 
     			
     		}
     		// dos.close();
     		dis.ready();
        } catch (IOException e) {
         	e.printStackTrace();
         	return ;
        }
        
    }
}
