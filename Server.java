// A Java program for a Server
import java.net.*;
import java.io.*;


public class Server extends Database
{
	//initialize socket and input stream

	private ServerSocket server = null;
	private static Socket socket = null;
	
	private Database db = null;

	// constructor with port
	public Server(int port) {
		// starts server and waits for a connection
		try
		{
			server = new ServerSocket(port);
			System.out.println("Server started");
			System.out.println("Waiting for a client ...");

			db = new Database();

			while(true){
				socket = server.accept();
				

				ClientHandler newClient = new ClientHandler (socket, db);

				//Thread t = new Thread (newClient);
				newClient.start();
				

				System.out.println("Client accepted");
			}
			

		} catch(IOException i) {
			System.out.println(i);
		}
		
		
	}

	public static class ClientHandler extends Thread{
		
		Database cdb;
		private Socket clientSocket = null;
		private DataInputStream in	 = null;


		private DataOutputStream output = null;


		public ClientHandler(Socket socket, Database db){	
			this.cdb = db;
			this.clientSocket = socket;
		}

		public void run() {
					

			// takes input from the client socket
			try {
				in = new DataInputStream(
				new BufferedInputStream(clientSocket.getInputStream()));
				
				output = new DataOutputStream(clientSocket.getOutputStream());

				String line = "";
	
				// reads message from client until "Over" is sent
				while (!line.equals("Over")) {	
					
					try {
						line = in.readUTF();
						
						if(line.equals("R")){
							cdb.read(1);
							output.writeUTF("Reader request finished");

							System.out.println("Reader inside me Ahh!");

						}
						
	
						else if(line.equals("W")){
							cdb.write(1);
							output.writeUTF("Writer request finished");
							System.out.println("Writer inside me Ahh!");
						}
						else {System.out.println(line);}
	
					}
					catch(IOException i)
					{
						System.out.println(i);
					}
				}
				System.out.println("Closing connection");
	
				// close connection
				clientSocket.close();
				in.close();
			}catch(IOException i){
				System.out.println(i);
			}

		}


	}

	public static void main(String args[])
	{
		Server server = new Server(5020);
	}

}
