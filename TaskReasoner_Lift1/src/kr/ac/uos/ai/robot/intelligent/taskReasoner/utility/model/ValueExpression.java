package kr.ac.uos.ai.robot.intelligent.taskReasoner.utility.model;

import kr.ac.uos.ai.robot.intelligent.taskReasoner.utility.UtilityCalculator;

public class ValueExpression extends FormulaExpression{

	private Float value;
	
	public ValueExpression() {
		// TODO Auto-generated constructor stub
	}
	
	public Float getValue() {
		return value;
	}
	public void setValue(Float value) {
		this.value = value;
	}
	
	@Override
	public Float evaluate(UtilityCalculator calculator, String serviceName) {
		// TODO Auto-generated method stub
		return value;
	}

	@Override
	public void print(UtilityCalculator calculator, String serviceName) {
		System.out.println(value);
		// TODO Auto-generated method stub
		
	}

}
