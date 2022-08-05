package servlets;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class DSConnection {
	final static int ServerPort = 8080;
	Scanner scn = new Scanner(System.in);
	
	public static int update = 0;
	static boolean keyAssignReq = false;

    // read message to deliver
 	static String msg = null;
 	static String reply = null;
	
	public static void main() throws UnknownHostException, IOException {
		keyAssignReq = false;
        	// format request assign patient
            System.out.println("Request for assign patient!");
            msg = "ASSIGN_REQ ";
            
            try {
             	// write on output stream
             	System.out.println("Message from client: " + msg);
             	CSConnection.dos.write(msg);
             	CSConnection.dos.flush();
             	msg = "";
             	// System.out.println("Check dos: " + dos);
             	// dos.close();
             			
             	// System.out.println("Socket closed: " + s.isClosed());
             			
             	// read message to this client
         		BufferedReader br = new BufferedReader(CSConnection.dis);
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
         		CSConnection.dis.ready();
            } catch (IOException e) {
             	e.printStackTrace();
             	return ;
            }
        /*======================================================================================*/
        if(keyAssignReq) {
        	System.out.println("Request for patient diagnosis!");
            msg = "DOC_DIAG ";
            
            try {
             	// write on output stream
             	System.out.println("Message from client: " + msg);
             	CSConnection.dos.write(msg);
             	CSConnection.dos.flush();
             	msg = "";
             	// System.out.println("Check dos: " + dos);
             	// dos.close();
             			
             	// System.out.println("Socket closed: " + s.isClosed());
             			
             	// read message to this client
         		BufferedReader br = new BufferedReader(CSConnection.dis);
         		char[] buffer = new char[10000];
         		int count = br.read(buffer, 0, 10000);
         		reply = new String(buffer, 0, count);
         		System.out.println("Message to client: " + reply + "\n");
         		// dos.close();
         		CSConnection.dis.ready();
            } catch (IOException e) {
             	e.printStackTrace();
             	return ;
            }
        } else {
        	System.out.println("Assignment not yet!");
        }
    }
	
	public static void DisplayPatientArrival() {
		
		update = 1;
	}
}