package kr.ac.uos.ai.robot.intelligent.taskReasoner.action.argument;

import java.util.ArrayList;

import org.json.simple.JSONObject;

import kr.ac.uos.ai.arbi.model.GeneralizedList;

public class PolicyAppendArgument {
	private String policyName;
	private String serviceName;
	private String evaluation;
	private GeneralizedList contextList;
	private ArrayList<String> conditionNameList;
	
	public PolicyAppendArgument() {
		policyName = "";
		serviceName = "";
		evaluation = "";
		contextList = null;
		conditionNameList = new ArrayList<String>();
		
		// TODO Auto-generated constructor stub
	}
	
	
	
	public String getPolicyName() {
		return policyName;
	}

	public void setPolicyName(String policyName) {
		this.policyName = policyName;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getEvaluation() {
		return evaluation;
	}

	public void setEvaluation(String evaluation) {
		this.evaluation = evaluation;
	}

	public GeneralizedList getContextList() {
		return contextList;
	}

	public void setContextList(GeneralizedList contextList) {
		this.contextList = contextList;
	}

	public void setConditionNameList(ArrayList<String> conditionNameList) {
		this.conditionNameList = conditionNameList;
	}

	public void addContextName(String str) {
		conditionNameList.add(str);
	}

	public ArrayList<String> getConditionNameList() {
		return conditionNameList;
	}

	
	@Override
	public String toString() {
		
		JSONObject json = new JSONObject();
		
		json.put("PolicyName", policyName);
		json.put("ServiceName", serviceName);
		json.put("Context", conditionNameList);
		
		return json.toJSONString();
	}
	
}
