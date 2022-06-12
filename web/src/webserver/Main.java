package webserver;


public class Main {
	//Æô¶¯Ïß³Ì
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Server server=new Server(8083);
		new Thread(server).start();
	}

}
