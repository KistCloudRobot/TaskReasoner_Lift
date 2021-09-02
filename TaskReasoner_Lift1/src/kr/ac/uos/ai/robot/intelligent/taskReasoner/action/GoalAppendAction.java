package kr.ac.uos.ai.robot.intelligent.taskReasoner.action;

import kr.ac.uos.ai.arbi.agent.logger.ActionBody;
import kr.ac.uos.ai.robot.intelligent.taskReasoner.action.argument.GoalAppendArgument;
import kr.ac.uos.ai.robot.intelligent.taskReasoner.service.ServiceModelGenerator;

public class GoalAppendAction implements ActionBody{

	private ServiceModelGenerator generator;
	
	public GoalAppendAction(ServiceModelGenerator generator) {
		this.generator = generator;
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public Object execute(Object o) {
		GoalAppendArgument argument = (GoalAppendArgument) o;
		
		generator.generateGoal(argument);
		// TODO Auto-generated method stub
		return null;
	}
}
