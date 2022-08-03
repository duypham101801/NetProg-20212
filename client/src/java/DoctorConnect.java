package client;

import java.io.*;
import java.net.*;
import java.util.Scanner;
  
public class DoctorConnect
{
    final static int ServerPort = 8080;
  
    @SuppressWarnings("resource")
	public static void main(String args[]) throws UnknownHostException, IOException 
    {
        Scanner scn = new Scanner(System.in);
          
        // getting localhost ip
        //InetAddress ip = InetAddress.getByName("localhost");
        byte[] ipAddr = new byte[] {(byte) 192, (byte) 168, (byte) 1, (byte) 86};
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
     	boolean keyLogin = false;
     	boolean keyAssignReq = false;

     	
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
     		System.out.println("Message to client: " + reply + "\n");
     		if (reply.contains("true")) {
     			keyLogin = true;
     			System.out.println("Login succesfully!");
     		} else {
     			System.out.println("Login again!");
     		}
     		// dos.close();
     		dis.ready();
        } catch (IOException e) {
         	e.printStackTrace();
         	return ;
        }
        
        /*==========================================================================*/
        if (keyLogin) {
        	// format request assign patient
            System.out.println("Request for assign patient!");
            msg = "ASSIGN_REQ ";
            
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
         		System.out.println("Message to client: " + reply + "\n");
         		if (reply.contains("success")) {
         			keyAssignReq = true;
         			System.out.println("Waiting for cliets!");
         		} else {
         			System.out.println("Message request to assign failed");
         		}
         		// dos.close();
         		dis.ready();
            } catch (IOException e) {
             	e.printStackTrace();
             	return ;
            }
        } else {
        	System.out.println("Failed to assign!");
        }
        
        /*=======================================================================================*/
        if(keyAssignReq) {
        	System.out.println("Start chat with patient!");
        	
        	while(true) {
        		try {
        			// write on output stream
             		msg = scn.nextLine();
             		msg = "DOC_SEND " + "<" + msg + ">";
                 	System.out.println("Message to client: " + msg);
                 	dos.write(msg);
                 	dos.flush();
                 	msg = "";
        			
        			// read message to this patient(pat -> ser -> doc)
             		BufferedReader br = new BufferedReader(dis);
             		char[] buffer = new char[10000];
             		int count = br.read(buffer, 0, 10000);
             		reply = new String(buffer, 0, count);
             		if (reply.contains("DOC_RECV")) {
             			System.out.println("Message from client: " + reply + "\n");
             			continue;
             		} else {
             			System.out.println("Format message not to doctor!");
             			s.close();
             		}
             		// dos.close();
             		dis.ready();         		
             		
        		} catch(IOException e) {
        			e.printStackTrace();
        			return ;
        		}
        	}
        } else {
        	System.out.println("Assignment not yet!");
        }
    }
}
