package test;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import kr.ac.uos.ai.arbi.model.GLFactory;
import kr.ac.uos.ai.arbi.model.GeneralizedList;
import kr.ac.uos.ai.arbi.model.parser.ParseException;

public class JsonTest {
	
	
	public static void main(String[] args) {
		JsonTest test = new JsonTest();
		
		String str = "(userIntention \"testPerson\")";
		JSONObject js = test.generateContextJson(str);
		
		System.out.println(js.toJSONString());
		System.out.println(js.get("contextName"));
	}
	public JSONObject generateContextJson(String str) {
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
		
		
		return json;
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
