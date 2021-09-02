package kr.ac.uos.ai.robot.intelligent.taskReasoner.action.argument;

import java.util.ArrayList;

import org.json.simple.JSONObject;

import kr.ac.uos.ai.arbi.model.GeneralizedList;

public class ServiceAppendArgument {

	private String serviceName;
	private String serviceID;
	private GeneralizedList								workflow;
	private GeneralizedList								precondition;
	private String										utility;
	
	private ArrayList<String> conditionNameList;
	
	public ServiceAppendArgument() {
		serviceName = "";
		serviceID = "";
		workflow = null;
		precondition = null;
		utility = null;
		
	}
	
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getServiceID() {
		return serviceID;
	}
	public void setServiceID(String serviceID) {
		this.serviceID = serviceID;
	}
	public GeneralizedList getWorkflow() {
		return workflow;
	}
	public void setWorkflow(GeneralizedList workflow) {
		this.workflow = workflow;
	}
	public GeneralizedList getPrecondition() {
		return precondition;
	}

	public void setPrecondition(GeneralizedList precondition) {
		this.precondition = precondition;
	}
	public String getUtility() {
		return utility;
	}
	public void setUtility(String utility) {
		this.utility = utility;
	}
	public ArrayList<String> getConditionNameList() {
		return conditionNameList;
	}
	public void setConditionNameList(ArrayList<String> conditionNameList) {
		this.conditionNameList = conditionNameList;
	}



	@Override
	public String toString() {
		JSONObject json = new JSONObject();
		
		json.put("ServiceName", serviceName);
		json.put("Precondition", conditionNameList);
		json.put("Utility", utility);
		System.out.println(json.toJSONString());
		return json.toJSONString();
	}
}
