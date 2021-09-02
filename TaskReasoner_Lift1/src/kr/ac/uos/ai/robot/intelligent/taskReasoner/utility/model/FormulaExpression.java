package kr.ac.uos.ai.robot.intelligent.taskReasoner.utility.model;

import kr.ac.uos.ai.robot.intelligent.taskReasoner.utility.UtilityCalculator;

public abstract class FormulaExpression {
	private String serviceName;
	
	public String getServiceName() {
		return serviceName;
	}
	
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	
	public abstract Float evaluate(UtilityCalculator calculator, String serviceName);
	
	public abstract void print(UtilityCalculator calculator, String serviceName);

}
