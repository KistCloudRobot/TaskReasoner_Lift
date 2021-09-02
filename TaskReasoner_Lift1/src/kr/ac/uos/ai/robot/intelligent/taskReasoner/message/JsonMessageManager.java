package kr.ac.uos.ai.robot.intelligent.taskReasoner.message;

import java.util.Iterator;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import kr.ac.uos.ai.arbi.model.GLFactory;
import kr.ac.uos.ai.arbi.model.GeneralizedList;
import kr.ac.uos.ai.arbi.model.parser.ParseException;
import kr.ac.uos.ai.robot.intelligent.taskReasoner.policy.PolicyHandler;

public class JsonMessageManager {
	
	private PolicyHandler policyHandler;
	
	
	public JsonMessageManager(PolicyHandler handler) {
		this.policyHandler = handler;
		// TODO Auto-generated constructor stub
	}
	
	public String generateJsonContext(String str) {
		GeneralizedList gl;
		JSONObject json = new JSONObject();

		try {
			gl = GLFactory.newGLFromGLString(str);
			
			
			json.put("contextName", gl.getName());
			
			JSONArray array = new JSONArray();
			
			for(int i = 0; i < gl.getExpressionsSize(); i++) {
				array.add(removeQuotationMarks(gl.getExpression(i).toString()));
			}
			json.put("argument", array);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return json.toString();
	}
	
	
	public void updateLMPolicyValue(String str) {
		JSONParser parser = new JSONParser();
		JSONObject json;
		System.out.println("msg received " + str);
		try {
			json = (JSONObject) parser.parse(str);
			
			Set keySet = json.keySet();
			Iterator<String> iterator = keySet.iterator();
			while(iterator.hasNext()) {
				String policyName = iterator.next();
				Float policyValue = Float.parseFloat(json.get(policyName).toString());
				policyHandler.updateLMPolicyValue(policyName, policyValue);
			}
		
		} catch (org.json.simple.parser.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
