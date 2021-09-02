package kr.ac.uos.ai.robot.intelligent.taskReasoner.utility.model;

import kr.ac.uos.ai.robot.intelligent.taskReasoner.utility.UtilityCalculator;

public class OperatorExpression extends FormulaExpression{
	private FormulaExpression left;
	private FormulaExpression right;
	private String operator;

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public FormulaExpression getLeft() {
		return left;
	}

	public void setLeft(FormulaExpression left) {
		this.left = left;
	}

	public FormulaExpression getRight() {
		return right;
	}

	public void setRight(FormulaExpression right) {
		this.right = right;
	}

	
	@Override
	public Float evaluate(UtilityCalculator calculator, String serviceName) {
		switch(operator) {
		case "+":
			return (Float) left.evaluate(calculator, serviceName) + (Float) right.evaluate(calculator, serviceName);
		case "-":
			return (Float) left.evaluate(calculator, serviceName) - (Float) right.evaluate(calculator, serviceName);
		case "*":
			return (Float) left.evaluate(calculator, serviceName) * (Float) right.evaluate(calculator, serviceName);
		case "/":
			return (Float) left.evaluate(calculator, serviceName) / (Float) right.evaluate(calculator, serviceName);
		}
		System.out.println("OperatorExpressionError");
		return null;
	}

	@Override
	public void print(UtilityCalculator calculator, String serviceName) {
		// TODO Auto-generated method stub
		System.out.println("left is :");
		left.print(calculator, serviceName);
		System.out.println("operator is :");
		System.out.println(operator);
		System.out.println("right is :");
		right.print(calculator, serviceName);
	}
}
