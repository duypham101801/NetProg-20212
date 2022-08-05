package servlets;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;

public class CSConnection {
	final static int ServerPort = 8080;

	static Scanner scn;
	static byte[] ipAddr = new byte[] { (byte) 192, (byte) 168, (byte) 1, (byte) 135 };
	static Socket s = null;
	static String msg = null;
	static String reply = null;
	public static boolean keySympReq = false;
	public static boolean keyAnsReq = false;
	public static boolean keyDiag = false;
	public static boolean keyChat = false;
	static String activeUsername = "";
	public static String[] parts = new String[100];
	public static String[] ansparts = new String[100];
	public static String[] anspartsID = new String[100];
	public static int[] sympIndexArray = new int[100]; 
	public static String[] diagString = new String[100];
	
	static int numSymp = 0;
	static ArrayList<Integer> listIndexSymp = new ArrayList<Integer>();

	public static String getActiveUsername() {
		return activeUsername;
	}
	
	public static int getNumSymp() {
		return numSymp;
	}
	
	public static int[] GetSympIndexArray() {
		return sympIndexArray;
	}

	public static void setActiveUsername(String input) {
		activeUsername = input;
	}
	
	public static String getPartsAt(int i) {
		return parts[i];
	}

	static InputStreamReader dis;
	static OutputStreamWriter dos;
//    InetAddress ip = InetAddress.getByAddress(ipAddr);

	public static void main() throws UnknownHostException, IOException {
		scn = new Scanner(System.in);

		// getting localhost ip
		// InetAddress ip = InetAddress.getByName("localhost");
		InetAddress ip = InetAddress.getByAddress(ipAddr);

		// establish the connection
		s = new Socket(ip, ServerPort);

		// obtaining input and out streams
		dis = new InputStreamReader(s.getInputStream());
		// DataInputStream dis = new DataInputStream(s.getInputStream());
		dos = new OutputStreamWriter(s.getOutputStream());

		// read message to deliver
		msg = null;
		reply = null;
		keySympReq = false;
		keyAnsReq = false;
		keyDiag = false;
		keyChat = false;
		
		activeUsername = "";
		parts = new String[100];
		ansparts = new String[100];
		anspartsID = new String[100];
		sympIndexArray = new int[100];
		diagString = new String[100];
		
		numSymp = 0;
		listIndexSymp = new ArrayList<Integer>();

		System.out.println("CONNECT OK");
	}

	private static String GetHeader(String reply) {
		String[] parts = reply.split(" ");
		return parts[0];
	}

	public static int LogIn(String username, String password) {
		msg = "USERPASS user=<" + username + "> pass=<" + password + ">";
		
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
			if (GetHeader(reply).equals("REG_SUCCESS")) {
				dis.ready();
				if (reply.contains("false")) {
					System.out.println("LOGIN FALSE");
					return 0;
				}
				keySympReq = true;
				if (reply.contains("pat")) { // If user is PATIENT, return 1
					return 1;
				} else { // If user is DOCTOR, return 2
					return 2;
				}
			}
			// dos.close();
		} catch (IOException e) {
			e.printStackTrace();
			return -1;
		}
		return -1;
	}
	
	public static int Quit() { // End session and
		msg = "QUIT";
				
		return -1;
	}
	
	public static int Reset() {
		msg = "QUIT";
		
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
			if (GetHeader(reply).equals("QUIT_ACCEPT")) {
				dis.ready();
				s.close();
				s = null;
				keySympReq = false;
				keyAnsReq = false;
				keyChat = false;
				keyDiag = false;
			}
			// dos.close();
		} catch (IOException e) {
			e.printStackTrace();
			return -1;
		}
		return -1;
	}
	
	public static int RequestSymp() {
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
         		parts = null;
         		parts = reply.split("-");
         		// dos.close();
         		dis.ready();
            } catch (IOException e) {
             	e.printStackTrace();
             	return -1;
            }
        } else {
        	System.out.println("Login failed!");
        	return -1;
        }
		return -1;
	}
	
	public static int SubmitSymp(String[] checkedValues) {
		msg = "SYMP_SUBMIT";
		numSymp = checkedValues.length;
        for (int i=0; i<numSymp; i++) {
        	msg = msg + " <" + checkedValues[i] + ">";
        	listIndexSymp.add(Integer.parseInt(checkedValues[i]));
        }
        if(numSymp == 0) msg = "SYMP_SUBMIT <>";
 			
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
         	return -1;
        }
        return -1;
	}
	
	public static int RequestAnswers() {
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
         		String[] temp = reply.split("~");
         		for (int i = 0; i < numSymp; ++i) {
         			for (int j = 1; j <= 5; ++j) {
         				if (j == 1) { 
         					if (i == 0) {sympIndexArray[i]=Integer.parseInt(temp[i*5+1].substring(1, 2)); }
         					else { sympIndexArray[i]=Integer.parseInt(temp[i*5+1].substring(2, 3)); }
         					if (i == 0) {
         						if (temp[i*5+1].charAt(5) > '9') {
         							anspartsID[i*5+1]=Character.toString(temp[i*5+1].charAt(4));
         						} else {
         							anspartsID[i*5+1]=temp[i*5+1].substring(4,6);
         						}
         					} else {
         						if (temp[i*5+1].charAt(6) > '9') {
         							anspartsID[i*5+1]=Character.toString(temp[i*5+1].charAt(5));
         						} else {
         							anspartsID[i*5+1]=temp[i*5+1].substring(5,7);
         						}
         					}
         				}
         				int pos = 0;
         				while (!Character.isLetter(temp[i*5+j].charAt(pos))) {
         					pos++;
         				}
         				if (j > 1) {         					
         					if (temp[i*5+j].charAt(2) > '9') {
         						anspartsID[i*5+j]=Character.toString(temp[i*5+j].charAt(1));
         					} else {
         						anspartsID[i*5+j]=temp[i*5+j].substring(1,3);
         					}
         				}
         				ansparts[i*5+j]=temp[i*5+j].substring(pos,temp[i*5+j].length()-1);
         			}
         		}
         		
         		// dos.close();
         		dis.ready();
            } catch (IOException e) {
             	e.printStackTrace();
             	return -1;
            }
        } else {
        	System.out.println("Symptom form submit failed!");
        	return -1;
        }
		return -1;
	}
	
	public static int SubmitAns(String[] checkedValues) {
		msg = "ANS_SUBMIT ";
		for(int j=0; j<checkedValues.length; ++j) {
//    		    msg = msg + "<" + (5*(Integer.parseInt(String.valueOf(listIndexSymp.get(i))))+j-5) + "><y>~";
			msg = msg + "<" + checkedValues[j] + "><y>~";
		}
		msg = msg + "-";
 		
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
         	return -1;
        }
		return -1;
	}
	
	public static int RequestDiag() {
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
         		diagString = reply.split("-");
         		for (int i = 1; i < diagString.length; i++) {
         			int pos = 0;
         			while (!Character.isLetter(diagString[i].charAt(pos))) { pos++;	}
         			diagString[i] = diagString[i].substring(pos, diagString[i].length()-2);
         		}
         		// dos.close();
         		dis.ready();
            } catch (IOException e) {
             	e.printStackTrace();
             	return -1;
            }
        } else {
        	System.out.println("Answer form submit failed!");
        	return -1;
        }
		return -1;
	}
}