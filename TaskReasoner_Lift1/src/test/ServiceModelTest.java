package test;

import kr.ac.uos.ai.arbi.agent.ArbiAgent;
import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;
import kr.ac.uos.ai.arbi.ltm.DataSource;

public class ServiceModelTest extends ArbiAgent{

	private static final String brokerURI 				= "tcp://localhost:61616";
	private static final String	agentURIPrefix			= "agent://";
	private static final String	dsURIPrefix				= "ds://";
	private static final String TR_URI					= "agent://www.arbi.com/taskReasoner";
	private static final String TM_URI					= "www.arbi.com/taskManager";
	private static final String CM_URI				 	= "agent://www.arbi.com/contextManager";
	private static final String KM_URI 					= "agent://www.arbi.com/knowledgeManager";
	
	public ServiceModelTest() {
		// TODO Auto-generated constructor stub
	}
	public void onStart() {
		DataSource ds = new DataSource();
		ds.connect(brokerURI, dsURIPrefix+TM_URI, 2);
		}
	
	@Override
	public void send(String receiver, String data) {
		// TODO Auto-generated method stub
		super.send(receiver, data);
	}
	@Override
	public void onData(String sender, String data) {
		// TODO Auto-generated method stub
		System.out.println(sender);
		System.out.println(data);
		
		if(data.equals("(PostGoal (RoomPrepared \"Meeting001\" \"527-Ho\"))")) {
			this.send(sender, "(context (RoomPrepared \"527-Ho\"))");
		}
	}


	public static void main(String[] args) {
		ArbiAgent tm = new ServiceModelTest();
		
		ArbiAgentExecutor.execute(brokerURI, agentURIPrefix + TM_URI, tm, 2);
		String test = "(serviceModel \"SchedulePrepareation\" \"educationService\" "
				+ "(workflow \"RoomPrepared\" \"MeetingAnnounced\") "
				+ "(precondition "
				+ "(MeetingTimeApproached) "
				+ "(userIntention $user \"meetingPreparation\" $arg) "
				+ "(MeetingInformation $meetingID $meetingName $roomID $startTime $importance)) "
				+ "(utility 10))";

		tm.send(TR_URI, test);
		System.out.println("1st send success");

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		test = "(goal (RoomPrepared $meetingID $roomID) (precondition (MeetingInformation $meetingID $meetingName $roomID $startTime $importance)) (postcondition (RoomPrepared $roomID)))";
		tm.send(TR_URI, test);
		System.out.println("2nd send success");
		
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		test = "(goal (MeetingAnnounced $meetingID) (precondition (RoomPrepared $meetingID $roomID)) (postcondition))" ;
		tm.send(TR_URI, test);
		System.out.println("3rd send success");
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		test = "(context (MeetingTimeApproached))" ;
		tm.send(TR_URI, test);
		System.out.println("4th send success");


		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		test = "(context (userIntention \"person001\" \"meetingPreparation\" (argument \"null\")))" ;
		tm.send(TR_URI, test);
		System.out.println("5th send success");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		test = "(context (MeetingInformation \"Meeting001\" \"DemoMeeting\" \"527-Ho\" \"startTime\" \"urgent\"))" ;
		tm.send(TR_URI, test);
		System.out.println("6th send success");
	}

	
}
	
