package kr.ac.uos.ai.robot.intelligent.taskReasoner.action.argument;

import org.json.simple.JSONObject;

import kr.ac.uos.ai.arbi.model.GeneralizedList;

public class GoalAppendArgument {

	private String name;
	private GeneralizedList glGoal;
	private GeneralizedList precondition;
	
	public GoalAppendArgument() {
		// TODO Auto-generated constructor stub
		name = "";
		glGoal = null;
		precondition = null;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public GeneralizedList getGlGoal() {
		return glGoal;
	}


	public void setGlGoal(GeneralizedList glGoal) {
		this.glGoal = glGoal;
	}
	
	
	public GeneralizedList getPrecondition() {
		return precondition;
	}
	
	public void setPrecondition(GeneralizedList precondition) {
		this.precondition = precondition;
	}
	@Override
	public String toString() {
		
		return name;
	}
}
