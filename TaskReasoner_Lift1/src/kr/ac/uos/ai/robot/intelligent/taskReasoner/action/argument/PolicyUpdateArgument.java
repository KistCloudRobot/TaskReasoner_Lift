package kr.ac.uos.ai.robot.intelligent.taskReasoner.action.argument;

import org.json.simple.JSONObject;

public class PolicyUpdateArgument {

	private String name;
	private Float value;
	
	public PolicyUpdateArgument() {
		name = "";
		value = 0f;
		// TODO Auto-generated constructor stub
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public Float getValue() {
		return value;
	}
	public void setValue(Float newValue) {
		this.value = newValue;
	}
	@Override
	public String toString() {
		JSONObject json = new JSONObject();
		
		json.put("Name", name);
		json.put("Value", value);
		
		return json.toJSONString();
	}
}
