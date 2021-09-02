package kr.ac.uos.ai.robot.intelligent.taskReasoner.message;

public class RecievedMessage {

	private String	sender;
	private String 	message;
	
	public RecievedMessage(String sender, String message) {
		// TODO Auto-generated constructor stub
		this.sender = sender;
		this.message = message;
	}

	public String getSender() {
		return sender;
	}

	public String getMessage() {
		return message;
	}	
}
