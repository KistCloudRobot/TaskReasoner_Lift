package kr.ac.uos.ai.robot.intelligent.taskReasoner.policy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kr.ac.uos.ai.arbi.model.GLFactory;
import kr.ac.uos.ai.arbi.model.GeneralizedList;
import kr.ac.uos.ai.arbi.model.parser.ParseException;
import kr.ac.uos.ai.robot.intelligent.taskReasoner.TaskReasoner_Lift1;
import kr.ac.uos.ai.robot.intelligent.taskReasoner.action.argument.PolicyAppendArgument;
import kr.ac.uos.ai.robot.intelligent.taskReasoner.action.argument.PolicyUpdateArgument;
import uos.ai.jam.Interpreter;
import uos.ai.jam.expression.Binding;
import uos.ai.jam.expression.Expression;
import uos.ai.jam.expression.Relation;
import uos.ai.jam.expression.Symbol;
import uos.ai.jam.expression.Value;
import uos.ai.jam.expression.Variable;

public class PolicyHandler {
	private TaskReasoner_Lift1 taskReaosner;
	private Interpreter interpreter;
	
	private ArrayList<String> 	policyList;
	
	private String policyName;
	private String serviceName;
	private String evaluation;
	
	private GeneralizedList glContextList;
	
	private HashMap<String, Float>  lmPolicyMap;
	

	public PolicyHandler(TaskReasoner_Lift1 taskReasoner, Interpreter interpreter) {
		this.taskReaosner = taskReasoner;
		this.interpreter = interpreter;
		lmPolicyMap = new HashMap<String, Float>();
		policyList = new ArrayList<String>();
		
		lmPolicyMap.put("Safety", 10f);
	}
	
	public Float getLMPolicyValue(String policyName) {
		return lmPolicyMap.get(policyName);
	}
	
	public void updateLMPolicyValue(String policyName, Float value) {
		if (lmPolicyMap.get(policyName)!= null) {
			lmPolicyMap.remove(policyName);
			lmPolicyMap.put(policyName, value);
		} else {
			lmPolicyMap.put(policyName, value);
		}
	}
	
	
	public int getPolicyListSize() {
		return policyList.size();
	}
	
	public String getPolicyNameFromPolicyList(int i) {
		return policyList.get(i);
	}
	
	public PolicyAppendArgument generatePolicyAppendArgument(String glString) {
		PolicyAppendArgument argument = new PolicyAppendArgument();
		
		GeneralizedList gl = null;
		
		try {
			gl = GLFactory.newGLFromGLString(glString);
			GeneralizedList glPolicy = gl.getExpression(0).asGeneralizedList();
			argument.setPolicyName(glPolicy.getName());
			argument.setServiceName(glPolicy.getExpression(0).toString());
			argument.setEvaluation(GLFactory.unescape(glPolicy.getExpression(1).toString()));
			glContextList = glPolicy.getExpression(2).asGeneralizedList();
			argument.setContextList(glContextList);
			
			ArrayList<String> contextNameList = new ArrayList<String>();
			
			for(int i = 0; i < glContextList.getExpressionsSize(); i++) {
				contextNameList.add(glContextList.getExpression(i).asGeneralizedList().getName());
			}
			argument.setConditionNameList(contextNameList);
			
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return argument;
	}
	
	public PolicyUpdateArgument generatePolicyUpdateArgument(String name, Object value) {
		PolicyUpdateArgument argument = new PolicyUpdateArgument();
		String str = value.toString();
			
		argument.setName(name);
		argument.setValue(Float.parseFloat(str));
		
		return argument;
	}
	
	public PolicyUpdateArgument generatePolicyUpdateArgument(String str) {
		PolicyUpdateArgument argument = new PolicyUpdateArgument();
		
		GeneralizedList gl;
		GeneralizedList glPolicy;
		try {
			gl = GLFactory.newGLFromGLString(str);
			glPolicy = gl.getExpression(0).asGeneralizedList();
			
			argument.setName(glPolicy.getName());
			argument.setValue(Float.parseFloat(removeQuotationMarks(glPolicy.getExpression(0).toString())));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return argument;
	}
	
	public void generatePolicy(PolicyAppendArgument argument) {
		
		setPolicyArgument(argument);
		
		String policyPlan = generatePolicyPlan();
		
		assertPolicyValue();
		assertPolicyRelation();
		//System.out.println(policyPlan);
		taskReaosner.parsePlan(policyPlan);
	}
	
	public String getRelatedPolicy(String contextName) {
		List<Expression> expressionList = new ArrayList<Expression>();
		Value ctName = new Value(contextName);
		Variable relatedPolicyName = new Variable(new Symbol("policyName"));
		expressionList.add(relatedPolicyName);

		expressionList.add(ctName);
		Relation rel = interpreter.getWorldModel().newRelation("PolicyRelation", expressionList);
		
		Binding b = new Binding();
		b.unbindVariables(rel);
		boolean result = interpreter.getWorldModelManager().getWorldModel().match(rel, b);
		//System.out.println(result);
		
		String str = b.getValue(relatedPolicyName).toString();
		str = removeQuotationMarks(str);
		return str;
	}
	
	
	
	private void setPolicyArgument(PolicyAppendArgument argument) {
			
		policyName = argument.getPolicyName();
		serviceName = argument.getServiceName();
		evaluation = argument.getEvaluation();
		glContextList = argument.getContextList();
		policyList.add(policyName);
	}
	
	private void assertPolicyValue() {
		taskReaosner.assertFact("PolicyValue", policyName, removeQuotationMarks(serviceName), 0f);
		if (lmPolicyMap.get(policyName)!= null) {
		} else {
			lmPolicyMap.put(policyName, 0f);
		}
	}
	
	private void assertPolicyRelation() {
		for (int i = 0; i < glContextList.getExpressionsSize(); i++) {
			GeneralizedList glContext = glContextList.getExpression(i).asGeneralizedList();
			if (glContext.getName().equals("PolicyValue")) {
				//taskReaosner.assertFact("PolicyRelation", policyName, "consistOf", glContext.getExpression(0));
				taskReaosner.assertFact("PolicyRelation", policyName, removeQuotationMarks(glContext.getExpression(0).toString()));
			} else {
				//taskReaosner.assertFact("PolicyRelation", policyName,"has", glContext.getName());
				taskReaosner.assertFact("PolicyRelation", policyName, glContext.getName());
			}
		}
	}
	
	private String generatePolicyPlan() {
		StringBuilder planBuilder = new StringBuilder();
		planBuilder.append("PLAN PERFORM EvaluatePolicyValue($policyName) {\n"
				+ "ID : \"" + policyName+"," +removeQuotationMarks(serviceName) + "\"\n"
				+ "PRECONDITION : \n"
					+ "$policyName == \"" + policyName + "\";\n"
					+ "FACT PolicyHandler($policyHandler);\n");
					
		for (int i = 0; i < glContextList.getExpressionsSize(); i++) {
			planBuilder.append("fact "+ makePredicateFromGL(glContextList.getExpression(i).asGeneralizedList()) + ";\n");
		}
		
		planBuilder.append("BODY : \n"
					+ removeQuotationMarks(evaluation) +"\n"
					+ "System.out.println(\"" + policyName + " value is \" + $value);\n"
					+ "PERFORM UpdatePolicyValue(\"" + policyName + "\"," + serviceName + ", $value);\n");
		planBuilder.append("UTILITY : 1;\n}");
		/*
		PLAN PERFROM EvaluatePolicyValue ($policyName) {
		ID : "testPolicy"
		PRECONDITION : 
			$policyname == "testPolicy";
			fact testContext($arg);
			fact PolicyValue("lowPolicy", "testID", $lowValue);
		BODY : 
			$value = 100;
			System.out.println("testPolicy value is " + $value);
			PERFROM UpdatePolicyValue("testPolicy","testID", $value);
		UTILITY : 1;
		}
		 */
		return planBuilder.toString();
	}
	
	private String makePredicateFromGL(GeneralizedList gl) {
		
		StringBuilder predicateBuilder = new StringBuilder();
		predicateBuilder.append(gl.getName() + "(");
		
		if(gl.getExpressionsSize() == 0) {
			predicateBuilder.append(")");
			
			System.out.println(predicateBuilder.toString());
			return predicateBuilder.toString();
		} else {
			for(int i = 0; i < gl.getExpressionsSize(); i++) {
				String arg = gl.getExpression(i).toString();
//				predicateBuilder.append(removeDoubleQuotationMarks(arg) + ", ");
				predicateBuilder.append(arg + ", ");
			}
			predicateBuilder.delete(predicateBuilder.length()-2, predicateBuilder.length());
			predicateBuilder.append(")");
			return predicateBuilder.toString();
		}
	}

	private String removeQuotationMarks(String input){
//		System.out.println("quotation removed : " + input);
		if(input.startsWith("\"")){
			input = input.substring(1,input.length()-1);
		}
//		System.out.println("quotation removed after: " + input);
		return input;
	}
}
