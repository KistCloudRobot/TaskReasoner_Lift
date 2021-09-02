package kr.ac.uos.ai.robot.intelligent.taskReasoner.utility;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kr.ac.uos.ai.robot.intelligent.taskReasoner.TaskReasoner_Lift1;
import kr.ac.uos.ai.robot.intelligent.taskReasoner.utility.model.FormulaExpression;
import kr.ac.uos.ai.robot.intelligent.taskReasoner.utility.parser.ParseException;
import kr.ac.uos.ai.robot.intelligent.taskReasoner.utility.parser.UtilityFunctionParser;
import uos.ai.jam.Interpreter;
import uos.ai.jam.expression.Binding;
import uos.ai.jam.expression.Expression;
import uos.ai.jam.expression.Relation;
import uos.ai.jam.expression.Symbol;
import uos.ai.jam.expression.Value;
import uos.ai.jam.expression.Variable;

public class UtilityCalculator {

	private Interpreter							interpreter;
	private HashMap<String, FormulaExpression>	utilityFunctionMap;
	private UtilityFunctionParser 				parser;
	
	public UtilityCalculator(Interpreter interpreter) {
		this.interpreter = interpreter;
		utilityFunctionMap = new HashMap<String, FormulaExpression>();
		// TODO Auto-generated constructor stub
	}
	
	public void putUtilityFunction(String serviceName, String stringFunction) {
		parser = new UtilityFunctionParser(new ByteArrayInputStream(stringFunction.getBytes()));
		
		FormulaExpression expression;
		try {
			//System.out.println("====string Utility Function====");
			//System.out.println(stringFunction);
			expression = parser.parse();
			System.out.println("utility function parsed!");
			utilityFunctionMap.put(serviceName, expression);
			//expression.print(this, serviceName);

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			System.out.println("Failed to parse utility function...");
			e.printStackTrace();
		}
	}
	
	public Float getServiceUtility(String serviceName) {
		FormulaExpression expression = utilityFunctionMap.get(serviceName);
		
		if (expression != null) {
			Float v = expression.evaluate(this, serviceName);
			return v;
		}
		
		return 0f;
	}
	
	public Float getPolicyValue(String policyName, String serviceName) {
		
		List<Expression> expressionList = new ArrayList<Expression>();
		Value pName = new Value(policyName);
		Value sName = new Value(serviceName);
		
		Variable policyValue = new Variable(new Symbol("policyValue"));
		
		expressionList.add(pName);
		expressionList.add(sName);
		expressionList.add(policyValue);

		Relation rel = interpreter.getWorldModel().newRelation("PolicyValue", expressionList);
		
		Binding b = new Binding();
		b.unbindVariables(rel);
		boolean result = interpreter.getWorldModelManager().getWorldModel().match(rel, b);
		//System.out.println(result);
		
		Float value = Float.parseFloat(b.getValue(policyValue).toString());
		return value;
	}
	
	
}
