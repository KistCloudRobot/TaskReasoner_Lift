package kr.ac.uos.ai.robot.intelligent.taskReasoner.utility.model;

import kr.ac.uos.ai.robot.intelligent.taskReasoner.utility.UtilityCalculator;

public class VariableExpression extends FormulaExpression{
	
	private String identifier;
	private VariableExpression var;
	
	public String getIdentifier() {
		return identifier;
	}
	public VariableExpression getVar() {
		return var;
	}
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	public void setVar(VariableExpression var) {
		this.var = var;
	}
		
	@Override
	public Float evaluate(UtilityCalculator calculator, String serviceName) {
		Float value = calculator.getPolicyValue(identifier, serviceName);
		return value;
	}

	@Override
	public void print(UtilityCalculator calculator, String serviceName) {
		
		System.out.println(identifier);
		// TODO Auto-generated method stub
	}

}
