package servlets;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class CSConnection {
	final static int ServerPort = 8080;

	static Scanner scn;
	static byte[] ipAddr = new byte[] { (byte) 192, (byte) 168, (byte) 1, (byte) 135 };
	static Socket s = null;
	static String msg = null;
	static String reply = null;
	static boolean keySympReq = false;
	static boolean keyAnsReq = false;
	static boolean keyDiag = false;
	static boolean keyChat = false;
	static String activeUsername = "";

	public static String getActiveUsername() {
		return activeUsername;
	}

	public static void setActiveUsername(String input) {
		activeUsername = input;
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
}