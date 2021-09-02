package test;

import kr.ac.uos.ai.arbi.agent.ArbiAgent;
import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;
import kr.ac.uos.ai.arbi.ltm.DataSource;
import kr.ac.uos.ai.arbi.model.GLFactory;
import kr.ac.uos.ai.arbi.model.GeneralizedList;
import kr.ac.uos.ai.arbi.model.Value;

public class PolicyTest extends ArbiAgent{

	private static final String brokerURI 				= "tcp://localhost:61616";
	private static final String	agentURIPrefix			= "agent://";
	private static final String	dsURIPrefix				= "ds://";
	private static final String TR_URI					= "agent://www.arbi.com/taskReasoner";
	private static final String TM_URI					= "www.arbi.com/taskManager";
	private static final String CM_URI				 	= "agent://www.arbi.com/contextManager";
	private static final String KM_URI 					= "agent://www.arbi.com/knowledgeManager";
	
	public PolicyTest() {
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
		
	}


	public static void main(String[] args) throws InterruptedException {
		ArbiAgent tm = new PolicyTest();
		
		ArbiAgentExecutor.execute(brokerURI, agentURIPrefix + TM_URI, tm, 2);
		String test;
		
		String arg1 = GLFactory.escape("$value = $policyHandler.getLMPolicyValue($policyName);");

		test = "(policy (Safety \"testService\" \"" + arg1+"\" (contextList (EmergencyHandling $user $robot) )))";
		//�ȸ��ø� ������ policyValue�� �ؾߵ�
		
		tm.send(TR_URI, test);
		System.out.println("1st send success");
		Thread.sleep(1000);
		test = "(context (TableCleanRequest \"table001\"))";
		tm.send(TR_URI, test);
		System.out.println("2nd send success");
		Thread.sleep(1000);
		test =  "(context (EmergencyHandling \"http://robot-arbi.kr/ontologies/complexService.owl#person001\" \"http://robot-arbi.kr/ontologies/isro_medical.owl#fall\"))";
		tm.send(TR_URI, test);
		System.out.println("3rd send success");
		//Thread.sleep(1000);
		//test =  "(context (UserInformation \"http://robot-arbi.kr/ontologies/complexService.owl#person001\"))" ;
		//tm.send(TR_URI, test);
		//System.out.println("2rd send success");
	}

	
}
	
