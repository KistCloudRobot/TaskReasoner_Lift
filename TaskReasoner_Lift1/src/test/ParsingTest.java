package test;

import kr.ac.uos.ai.arbi.model.GLFactory;
import kr.ac.uos.ai.arbi.model.GeneralizedList;
import kr.ac.uos.ai.arbi.model.parser.ParseException;

public class ParsingTest {
	
	String serviceID;
	String serviceName;
	GeneralizedList workflow;
	GeneralizedList precondition;
	
	public static void main(String[] args) {
	
		
		ParsingTest parsingTest = new ParsingTest();
		
		parsingTest.setGLServiceModel("(serviceModel \"SchedulePrepareation\" \"http://www.arbi.com/ontologies/educationService\" (workflow \"RoomPrepared\" \"MeetingAnnounced\") (precondition (MeetingTimeApproached) (isro:userIntention $user \"meetingPreparation\") (MeetingInformation $meetingID $meetingName $roomID $startTime $importance)))");
		System.out.println(parsingTest.makeWorkflowPlan());
	}
	public void setGLServiceModel(String glString) {
		GeneralizedList gl = null;
		
		//Message protocol is...
		//(serviceModel "serviceID" "ServiceName" (workflow "first goal" "Second goal" "Third goal" ...) (precondition (context1 $arg1 $arg2) (context2 $arg1 $arg2 $arg3) ...)) (serviceUtility ???))
		
		try {
			gl = GLFactory.newGLFromGLString(glString);
			serviceID = gl.getExpression(0).toString();
			serviceName = gl.getExpression(1).toString();
			workflow = gl.getExpression(2).asGeneralizedList();
			precondition = gl.getExpression(3).asGeneralizedList();
			/*
			trigger = precondition.getExpression(0).asGeneralizedList();
			required = precondition.getExpression(1).asGeneralizedList();
			safety = precondition.getExpression(2).asGeneralizedList();
			*/

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public String makeWorkflowContext() {
		StringBuilder goalBuilder = new StringBuilder();
		
		goalBuilder.append("(context (GeneratedService \"" + removeQuotationMarks(serviceName) +"\"))" );
		
		return goalBuilder.toString();
	}
	
	public String makeWorkflowPlan() {		
		StringBuilder planBuilder = new StringBuilder();
		
		System.out.println("make workflow plan");
			
		planBuilder.append("PLAN ACHIEVE ThrowWorkflowGoal($serviceName) {\n"
				+ "ID : " + serviceID + "\n"
				+ "PRECONDITION : \n"
				+ "$serviceName == " + this.serviceName + ";\n");
		
		for(int i = 0; i < precondition.getExpressionsSize(); i++) {
			String stringPrecondition = makePredicateFromGL(precondition.getExpression(i).asGeneralizedList());
			planBuilder.append("fact " + stringPrecondition + ";\n");
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
				+ "System.out.println(\"start workflow\");\r\n");
		
		for(int i = 0; i < workflow.getExpressionsSize(); i++) {
			String goalName = removeQuotationMarks(workflow.getExpression(i).toString());
			planBuilder.append("PERFORM ThrowGoal(\"" + goalName + "\");\n" );
		}
		
		planBuilder.append("UTILITY : 1;\n}");

		return planBuilder.toString();
	}
	private String makePredicateFromGL(GeneralizedList gl) {
		
		StringBuilder predicateBuilder = new StringBuilder();
		predicateBuilder.append(gl.getName() + "(");
	
			for(int i = 0; i < gl.getExpressionsSize(); i++) {
				String arg = gl.getExpression(i).toString();
//				predicateBuilder.append(removeDoubleQuotationMarks(arg) + ", ");
				predicateBuilder.append(arg + ", ");
			}
			predicateBuilder.delete(predicateBuilder.length()-2, predicateBuilder.length());
			predicateBuilder.append(")");
			return predicateBuilder.toString();
		
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
