package webserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class Server implements Runnable{
	//实现多线程
	private int port;
	public Server(int port) {
		this.port=port;
	}
	@Override
	public void run(){
		try {
			ServerSocket serverSocket = null;
			serverSocket = new ServerSocket(port);
			System.out.println("thread monitor start");
			while (GetData.isRunning){
			    Socket socket = serverSocket.accept();
			    System.out.println("received a request");
			    Request req= new Request(socket);
			    new Thread(req).start();
			}
			serverSocket.close();
			serverSocket = null;
			// TODO Auto-generated method stub
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
