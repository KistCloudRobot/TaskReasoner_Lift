package kr.ac.uos.ai.robot.intelligent.taskReasoner.action;

import kr.ac.uos.ai.arbi.agent.logger.ActionBody;
import kr.ac.uos.ai.robot.intelligent.taskReasoner.message.GLMessageManager;

public class RetractContextAction implements ActionBody{

	private GLMessageManager glMessageManager;
	
	public RetractContextAction(GLMessageManager glMessageManager) {
		
		this.glMessageManager = glMessageManager;
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public Object execute(Object o) {
		// TODO Auto-generated method stub
		return null;
	}
}
