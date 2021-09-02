package kr.ac.uos.ai.robot.intelligent.taskReasoner.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

import kr.ac.uos.ai.robot.intelligent.taskReasoner.TaskReasoner_Lift1;

public class Server implements Runnable{
	
	private ServerSocket serverSocket;
	private Socket socket;
	private int port_number = 9090; 
	private TaskReasoner_Lift1 reasoner;
	private BufferedWriter writer;
	private BufferedReader reader;
	
	
	public Server(TaskReasoner_Lift1 reasoner) {
		this.reasoner = reasoner;
	}
	
	public void sendToLM(String str) {
		
		try {
			System.out.println("sent msg "+ str);
			writer.write(str);
			writer.newLine();
			writer.flush();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			serverSocket = new ServerSocket(port_number);
			socket = serverSocket.accept();
			
			System.out.println("accepted!");
			writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			String msg = null;
			while((msg = reader.readLine()) != null) {
				System.out.println(msg);
				reasoner.receivedPolicyMessage(msg);
				msg = null;
				
				Thread.sleep(1);
			}
			
			System.out.println("connection closed");
			socket.close();
			serverSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
