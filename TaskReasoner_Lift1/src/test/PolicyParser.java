package test;

import kr.ac.uos.ai.arbi.model.GLFactory;
import kr.ac.uos.ai.arbi.model.GeneralizedList;
import kr.ac.uos.ai.arbi.model.parser.ParseException;

public class PolicyParser {
	
	private GeneralizedList glPolicy;
	private String policyName;
	private String serviceID;
	private String evaluation;
	
	private GeneralizedList glContextList;
	
	
	
	public static void main(String[] args) {
		PolicyParser policyParser = new PolicyParser();
		String str = "(policy (testPolicy \"testID\" \"$value = 100;\" "
				+ "(contextList (testContext $arg) (PolicyValue \"lowPolicy\" \"testID\" $lowValue))))";
		policyParser.setStringPolicy(str);
		String plan = policyParser.generatePolicyPlan(str);
		
		System.out.println(plan);
	}
	
	
	public void setStringPolicy(String glString) {
		GeneralizedList gl = null;
		try {
			gl = GLFactory.newGLFromGLString(glString);
			
			glPolicy = gl.getExpression(0).asGeneralizedList();
			policyName = glPolicy.getName();
			serviceID = glPolicy.getExpression(0).toString();
			evaluation = glPolicy.getExpression(1).toString();
			
			glContextList = glPolicy.getExpression(2).asGeneralizedList();
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			System.out.println("GLPolicy parsing failed");
			e.printStackTrace();
		}
	}
	
	public String generatePolicyPlan(String glString) {
		StringBuilder planBuilder = new StringBuilder();
		planBuilder.append("PLAN PERFORM EvaluatePolicyValue ($policyName) {\n"
				+ "ID : \"" + policyName+"\"\n"
				+ "PRECONDITION : \n"
					+ "FACT CurrentService($serviceID);\n"
					+ "$policyname == \"" + policyName + "\";\n");
					
		for (int i = 0; i < glContextList.getExpressionsSize(); i++) {
			planBuilder.append("fact "+ makePredicateFromGL(glContextList.getExpression(i).asGeneralizedList()) + ";\n");
		}
		
		planBuilder.append("BODY : \n"
					+ removeQuotationMarks(evaluation) +"\n"
					+ "System.out.println(\"" + policyName + " value is \" + $value);\n"
					+ "PERFORM UpdatePolicyValue(\"" + policyName + "\"," + serviceID + ", $value);\n");
		planBuilder.append("UTILITY : 1;\n}");
		
		
		/*
		 * PLAN PERFORM HandlePolicy ($policyName) {
	ID : "UserNaturePreference"
	PRECONDITION : 
		FACT UserNaturePreference($person, $boolean);
		FACT ServiceUser($user);
		FACT CurrentService($serviceID);
		$policyName == "UserNaturePreference";
	BODY :
		if ($user == $person && $boolean == "true") {
			$value = 100;
		} else {
			$value = 0;
		}
		System.out.println("value is " + $value);
		RETRACT Policy("UserNaturePreference", "testService", $value2);
		ASSERT Policy("UserNaturePreference", "testService", $value);
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
