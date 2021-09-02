package test;

import kr.ac.uos.ai.arbi.agent.ArbiAgent;
import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;
import kr.ac.uos.ai.arbi.ltm.DataSource;
import kr.ac.uos.ai.arbi.model.GLFactory;

public class TestOfTest extends ArbiAgent{

	private static final String brokerURI 				= "tcp://192.168.2.183:61616";
	private static final String	agentURIPrefix			= "agent://";
	private static final String	dsURIPrefix				= "ds://";
	private static final String TR_URI					= "agent://www.arbi.com/taskReasoner";
	private static final String TM_URI					= "www.arbi.com/testCode";
	private static final String CM_URI				 	= "agent://www.arbi.com/contextManager";
	private static final String KM_URI 					= "agent://www.arbi.com/knowledgeManager";
	
	public TestOfTest() {
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
		
		//if(data.equals("(PostGoal (RoomPrepared \"Meeting001\" \"527-Ho\"))")) {
		//	this.send(sender, "(context (RoomPrepared \"527-Ho\"))");
		//}
	}


	public static void main(String[] args) {
		ArbiAgent tm = new TestOfTest();
		
		ArbiAgentExecutor.execute(brokerURI, agentURIPrefix + TM_URI, tm, 2);
		
		
		String test = "(serviceModel \"SchedulePrepareation\" \"educationService\" "
				+ "(workflow \"RoomPrepared\" \"MeetingAnnounced\") "
				+ "(precondition "
				+ "(MeetingTimeApproached) "
				+ "(userIntention $user \"meetingPreparation\" $arg) "
				+ "(MeetingInformation $meetingID $meetingName $roomID $startTime $importance)) "
				+ "(utility \"10 * TestPolicy + TTPolicy\"))";

		tm.send(TR_URI, test);
		System.out.println("1st service send success");
		

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		test = "(serviceModel \"FallService\" \"fallService\" "
				+ "(workflow \"callOneOneNine\" \"goToHospital\") "
				+ "(precondition "
				+ "(Fall $user)) "
				+ "(utility \"50 * FallPolicy\"))";
		tm.send(TR_URI, test);
		System.out.println("2nd service send success");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String arg1 = GLFactory.escape("$value = 10;");

		test = "(policy (TestPolicy \"educationService\" \"" + arg1+"\" (contextList (MeetingTimeApproached))))";

		tm.send(TR_URI, test);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		test = "(policy (TTPolicy \"educationService\" \"" + arg1+"\" (contextList (userIntention $user \"meetingPreparation\" $arg))))";

		tm.send(TR_URI, test);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		test = "(policy (FallPolicy \"fallService\" \"" + arg1+"\" (contextList (Fall $user))))";

		tm.send(TR_URI, test);
		System.out.println("policy send success");		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		test = "(goal (RoomPrepared $meetingID $roomID) (precondition (MeetingInformation $meetingID $meetingName $roomID $startTime $importance)) (postcondition (RoomPrepared $roomID)))";
		tm.send(TR_URI, test);
		System.out.println("goal send success");

		
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		test = "(goal (MeetingAnnounced $meetingID) (precondition (RoomPrepared $meetingID $roomID)) (postcondition))" ;
		tm.send(TR_URI, test);
		System.out.println("gaol send success");

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		test = "(goal (goToHospital) (precondition) (postcondition))" ;
		tm.send(TR_URI, test);
		System.out.println("goal send success");

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		test = "(goal (callOneOneNine) (precondition) (postcondition))" ;
		tm.send(TR_URI, test);
		System.out.println("goal send success");

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		test = "(context (MeetingTimeApproached))" ;
		tm.send(TR_URI, test);
		System.out.println("1 context send success");


		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		test = "(context (userIntention \"person001\" \"meetingPreparation\" (argument \"null\")))" ;
		tm.send(TR_URI, test);
		System.out.println("2 context send success");

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		test = "(context (MeetingInformation \"Meeting001\" \"DemoMeeting\" \"527-Ho\" \"startTime\" \"urgent\"))" ;
		tm.send(TR_URI, test);
		System.out.println("3 context send success");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		test = "(context (Fall \"person001\"))" ;
		tm.send(TR_URI, test);
		System.out.println("4 context send success");
	}

	
}
	
