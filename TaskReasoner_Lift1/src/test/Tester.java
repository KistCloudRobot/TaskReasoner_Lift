package test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.json.simple.JSONObject;

import kr.ac.uos.ai.arbi.model.GLFactory;
import kr.ac.uos.ai.arbi.model.GeneralizedList;
import kr.ac.uos.ai.arbi.model.parser.ParseException;
import uos.ai.jam.*;
import uos.ai.jam.Goal;
import uos.ai.jam.Interpreter;
import uos.ai.jam.exception.AgentRuntimeException;
import uos.ai.jam.expression.Binding;
import uos.ai.jam.expression.Expression;
import uos.ai.jam.expression.Relation;
import uos.ai.jam.expression.Value;
import uos.ai.jam.expression.ValueLong;
import uos.ai.jam.plan.action.AchieveGoalAction;
import uos.ai.jam.plan.action.PerformGoalAction;

public class Tester{

	Interpreter interpreter;
	
	public Tester() {

		interpreter  = JAM.parse("plan/test");
		String string = "LearningMethod";
		addRelation(string, "video");
		addRelation(string, "audio");
		addRelation("UtilityCalculator", this);
		postPerformGoal("(ChangePolicy \"LearningMethod\")", null);
		Thread t = new Thread() {
			public void run() {
				interpreter.run();
			}
		};
		
		t.start();


	}
	
	public Tester(String str) {
		JSONObject json = new JSONObject();
		
		json.put("test", str);
		
		System.out.println(json.toJSONString());
	}
	
	public static void main(String[] args) {

		new Tester("bob");
	}

	public void addRelation(String name, Object... args) {
		LinkedList<Expression> expList = new LinkedList<Expression>();
		if(args != null) {
			for (Object o : args) {
				Value v; 
				if (o instanceof String) {
					v = new Value((String) o);
				} else {
					v = new Value(o);
				}
				expList.add(v);
			}
		}
		
		Relation r = interpreter.getWorldModel().newRelation(name, expList);

		System.out.println("relation added : " +  r.toString());
		interpreter.getWorldModel().assertFact(r, null);
	}
	
	public String postPerformGoal(String goal, String utility){
		String result = null;
		GeneralizedList gl = null;
		System.out.println("goal is " + goal);
			try {
				gl = GLFactory.newGLFromGLString(goal);
				String goalName = gl.getName();
				List<Expression> expList = new ArrayList<>();
				
				for(int i = 0; i < gl.getExpressionsSize();i++) {
					String expressionValue = gl.getExpression(i).toString();
					expressionValue = this.removeQuotationMarks(expressionValue);
					System.out.println(expressionValue);
					expList.add(new Value(expressionValue));
				}
				
				Relation r = interpreter.getWorldModel().newRelation(goalName, expList);
				PerformGoalAction generatedGoal = new PerformGoalAction(goalName, r, new Value(0), null, null, interpreter);

				interpreter.getIntentionStructure().addUnique(generatedGoal, null, null);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (AgentRuntimeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		System.out.println("post End");
		
		return result;
	}
	
	public String removeQuotationMarks(String input){
		System.out.println("quotation removed : " + input);
		if(input.startsWith("\"")){
			input = input.substring(1,input.length()-1);
		}
		System.out.println("quotation removed after: " + input);
		
		return input;
	}
	

	
	public void setUtility(String str) {
		System.out.println(str);
		System.out.println("test");
	}

	public void evaluate(String key, int value) {
		System.out.println("key is " + key);
		System.out.println("value is "+value);
	}

}


