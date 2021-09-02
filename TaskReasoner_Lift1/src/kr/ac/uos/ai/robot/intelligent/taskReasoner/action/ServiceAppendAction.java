package kr.ac.uos.ai.robot.intelligent.taskReasoner.action;

import kr.ac.uos.ai.arbi.agent.logger.ActionBody;
import kr.ac.uos.ai.robot.intelligent.taskReasoner.action.argument.ServiceAppendArgument;
import kr.ac.uos.ai.robot.intelligent.taskReasoner.service.ServiceModelGenerator;

public class ServiceAppendAction implements ActionBody{

	private ServiceModelGenerator generator;

	public ServiceAppendAction(ServiceModelGenerator generator) {
		
		this.generator = generator;
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public Object execute(Object o) {
		System.out.println("generate service model");

		ServiceAppendArgument argument = (ServiceAppendArgument) o;
		
		generator.generateServiceModel(argument);
		// TODO Auto-generated method stub
		return null;
	}
}
