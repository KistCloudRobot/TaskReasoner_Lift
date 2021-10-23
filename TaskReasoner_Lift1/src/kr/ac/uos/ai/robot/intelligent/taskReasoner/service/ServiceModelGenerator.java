package kr.ac.uos.ai.robot.intelligent.taskReasoner.service;

import java.util.ArrayList;

import kr.ac.uos.ai.arbi.model.GLFactory;
import kr.ac.uos.ai.arbi.model.GeneralizedList;
import kr.ac.uos.ai.arbi.model.parser.ParseException;
import kr.ac.uos.ai.robot.intelligent.taskReasoner.TaskReasoner_Lift2;
import kr.ac.uos.ai.robot.intelligent.taskReasoner.action.argument.GoalAppendArgument;
import kr.ac.uos.ai.robot.intelligent.taskReasoner.action.argument.ServiceAppendArgument;

public class ServiceModelGenerator {

	private TaskReasoner_Lift2								taskReasoner;
	
	private String										servicePackageID;
	private String										servicePackageName;
	private GeneralizedList								workflow;
	private GeneralizedList								precondition;
	private String										utility;
	/*
	private GeneralizedList								trigger;
	private GeneralizedList								required;
	private GeneralizedList								safety;
	*/
	
	public ServiceModelGenerator(TaskReasoner_Lift2 taskReasoner) {
		this.taskReasoner = taskReasoner;
	}
	
	public ServiceAppendArgument generateServiceArgument(String glString) {
		ServiceAppendArgument argument = new ServiceAppendArgument();
		
		GeneralizedList gl = null;
		//Message protocol is...
		//(serviceModel "serviceID" "ServiceName" (workflow "first goal" "Second goal" "Third goal" ...) (precondition (context1 $arg1 $arg2) (context2 $arg1 $arg2 $arg3) ...)) (serviceUtility ???))

		try {
			gl = GLFactory.newGLFromGLString(glString);
			argument.setServiceID(gl.getExpression(0).toString());
			argument.setServiceName(gl.getExpression(1).toString());
			argument.setWorkflow(gl.getExpression(2).asGeneralizedList());
			precondition = gl.getExpression(3).asGeneralizedList();
			argument.setPrecondition(precondition);
			
			ArrayList<String> conditionNameList = new ArrayList<String>();
			
			for(int i =0; i< precondition.getExpressionsSize(); i++) {
				conditionNameList.add(precondition.getExpression(i).asGeneralizedList().getName());
			}
			argument.setConditionNameList(conditionNameList);
			argument.setUtility(removeQuotationMarks(gl.getExpression(4).asGeneralizedList().getExpression(0).toString()));
			System.out.println("utility : " + argument.getUtility());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return argument;
	}
	
	public void generateServiceModel(ServiceAppendArgument argument) {
		setServiceModel(argument);
		
		String workflowPlan = makeWorkflowPlan();
		taskReasoner.parsePlan(workflowPlan);

		String finalizePlan = makeServiceFinalizePlan();
		taskReasoner.parsePlan(finalizePlan);

		
//		System.out.println(workflowPlan);
//		System.out.println(finalizePlan);
		
		taskReasoner.putUtilityFunction(removeQuotationMarks(servicePackageName), utility);
		
		
		assertWorkflowContext();

		//String preconditionPlan = generator.makePreconditionPlan();
		//System.out.println("parse precondition plan");
		//System.out.println(preconditionPlan);
		//JAMParser.parseString(interpreter, preconditionPlan);
		//String preconditionGoal = generator.makePreconditionGoal();
		//System.out.println(workflowGoal);
		//postGoal(workflowGoal, null);
		//postGoal(preconditionGoal, null);
	}
	
	public GoalAppendArgument generateGoalArgument(String glString) {
		//Message protocol is...
		//(goal (goalName arg1 arg2 ...) (precodintion (context1 $arg1 $arg2...) (context2 ...)) (postCondition (context1 $arg1 $aeg2 ...) (context2 ...)))

		
		GoalAppendArgument argument = new GoalAppendArgument();
		GeneralizedList gl = null;
		try {
			gl = GLFactory.newGLFromGLString(glString);
			argument.setName(gl.getExpression(0).asGeneralizedList().getName());
			argument.setGlGoal(gl.getExpression(0).asGeneralizedList());
			argument.setPrecondition(gl.getExpression(1).asGeneralizedList());
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return argument;
	}

	public void generateGoal(GoalAppendArgument argument) {
		
		String stringPlan = makePlanForSingleGoal(argument);
		
		//System.out.println(stringPlan);
		taskReasoner.parsePlan(stringPlan);
	}
	
	
	private void setServiceModel(ServiceAppendArgument argument) {
		servicePackageID = argument.getServiceID();
		servicePackageName = argument.getServiceName();
		workflow = argument.getWorkflow();
		precondition = argument.getPrecondition();
		/*
		trigger = precondition.getExpression(0).asGeneralizedList();
		required = precondition.getExpression(1).asGeneralizedList();
		safety = precondition.getExpression(2).asGeneralizedList();
		*/
		utility = argument.getUtility();
	}
	
	private void assertWorkflowContext() {
		System.out.println("assert workflow context");
		taskReasoner.assertFact("GeneratedService", removeQuotationMarks(servicePackageName));
	}
	
	private String makeWorkflowPlan() {		
		StringBuilder planBuilder = new StringBuilder();
		
		System.out.println("make workflow plan");
			
		planBuilder.append("PLAN ACHIEVE ThrowWorkflowGoal($serviceName) {\n"
				+ "ID : " + servicePackageID + "\n"
				+ "PRECONDITION : \n"
				+ "$serviceName == " + servicePackageName + ";\n"
				+ "FACT UtilityCalculator($calculator);\n");
		
		for(int i = 0; i < precondition.getExpressionsSize(); i++) {
			String stringPrecondition = makePredicateFromGL(precondition.getExpression(i).asGeneralizedList());
			planBuilder.append("FACT " + stringPrecondition + ";\n");
		}
		/*
		for(int i = 0; i < trigger.getExpressionsSize(); i++) {
			String precondition = makePredicateFromGL(trigger.getExpression(i).asGeneralizedList());
			planBuilder.append("fact " + precondition + ";\n");
		}
		for(int i = 0; i < required.getExpressionsSize(); i++) {
			String precondition = makePredicateFromGL(required.getExpression(i).asGeneralizedList());
			planBuilder.append("fact " + precondition + ";\n");
		}
		*/
		
		planBuilder.append("BODY :\n"
				+ "System.out.println(\"start workflow\");\n"
				+ "System.out.println(\"==== current utility ====\");\n"
				+ "System.out.println($calculator.getServiceUtility($serviceName));\n"
				+ "PERFORM ChangeCurrentService(" + servicePackageName + "," + servicePackageID + ", $goalName);\n");
		
		for(int i = 0; i < workflow.getExpressionsSize(); i++) {
			String goalName = removeQuotationMarks(workflow.getExpression(i).toString());
			planBuilder.append("PERFORM ThrowGoal(\"" + goalName + "\" , " + servicePackageName + "," + servicePackageID +  ");\n" );
		}
		
		planBuilder.append("ASSERT ServiceFinished("+servicePackageName + ", " +servicePackageID + ");\n"
				+ "UTILITY : $calculator.getServiceUtility($serviceName);\n}");

		return planBuilder.toString();
	}
	
	private String makeServiceFinalizePlan()  {
		StringBuilder planBuilder = new StringBuilder();
		
		planBuilder.append("PLAN PERFORM FinalizeService($serviceName, $serviceID) {\n"
				+ "ID : \"Finalize" + removeQuotationMarks(servicePackageName) + "\"\n"
				+ "PRECONDITION : \n"
				+ "$serviceName == " + servicePackageName + ";\n"
				+ "BODY : \n");
		
		for(int i = 0; i < precondition.getExpressionsSize(); i++) {
			String stringPrecondition = makePredicateFromGL(precondition.getExpression(i).asGeneralizedList());
			planBuilder.append("RETRACT " + stringPrecondition + ";\n");
		}
		
		planBuilder.append("RETRACT CurrentService($serviceName, $serviceID);\n"
				+ "UTILITY : 1;\n}");
		
		return planBuilder.toString();
	}
	/*
	public String makePreconditionGoal() {
		StringBuilder goalBuilder = new StringBuilder();
		
		goalBuilder.append("(ThrowPreconditionGoal \"" + removeDoubleQuotationMarks(serviceName) +"\")" );

		return null;
	}
	
	public String makePreconditionPlan() {
		StringBuilder planBuilder = new StringBuilder();
		
		planBuilder.append("PLAN ACHIEVE ThrowPreconditionGoal($serviceName) {\n"
				+ "PRECONDITION : \n"
				+ "fact TaskReasoner($taskReasoner);\n"
				+ "$serviceName == " + this.serviceName + ";\n");
		for(int i = 0; i < trigger.getExpressionsSize(); i++) {
			String precondition = makePredicateFromGL(trigger.getExpression(i).asGeneralizedList());
			planBuilder.append("fact " + precondition + ";\n");
		}
		
		planBuilder.append("BODY :\n");
		
		for(int i = 0; i < required.getExpressionsSize(); i++) {
			GeneralizedList precondition = required.getExpression(i).asGeneralizedList();
			String goalName = precondition.getName();
			planBuilder.append("PERFORM ThrowGoal(\"" + goalName + "\");\n" );
		}
		
		planBuilder.append("UTILITY : 1;\n}");
		
		return planBuilder.toString();
	}
	*/
	
	private String makePlanForSingleGoal(GoalAppendArgument argument) {
		
		StringBuilder planBuilder = new StringBuilder();
		GeneralizedList glGoal = argument.getGlGoal();
		GeneralizedList preconditionList = argument.getPrecondition();
			String goalName = glGoal.getName();
		
		planBuilder.append("PLAN PERFORM ThrowGoal($goalName, $serviceName, $serviceID) {\n"
				+ "ID : \"" + goalName + "\"\n"
				+ "PRECONDITION :\n"
				+ "fact TaskReasoner($taskReasoner);\n"
				+ "fact LoggerManager($lm);\n"
				+ "$goalName == \"" + goalName +"\";\n");
		
		for (int i = 0; i < preconditionList.getExpressionsSize(); i++) {
			planBuilder.append("fact "+ 
				makePredicateFromGL(preconditionList.getExpression(i).asGeneralizedList()) + ";\n");
		}
		
		planBuilder.append("BODY :\n"
				+ "PERFORM UpdateCurrentService($serviceName, $serviceID, \""+goalName+"\");\n"
				+ "$arg = $lm.generateGoalPostArgument($serviceName, \"" + goalName +"\",");
		for(int i = 0; i < glGoal.getExpressionsSize() ; i++) {
			planBuilder.append(glGoal.getExpression(i).toString() + ",");
		}
		planBuilder.deleteCharAt(planBuilder.length() - 1);
		planBuilder.append(");\n");
		planBuilder.append("$lm.action(\"goalPost\", $arg);\n}");
		
		return planBuilder.toString();
			
	}
	
	private String makePredicateFromGL(GeneralizedList gl) {
		
		StringBuilder predicateBuilder = new StringBuilder();
		predicateBuilder.append(gl.getName() + "(");
		if(gl.getExpressionsSize() == 0) {
			predicateBuilder.append(")");
			
			//System.out.println(predicateBuilder.toString());
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
