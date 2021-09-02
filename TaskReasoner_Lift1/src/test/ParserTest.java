package test;

import java.io.ByteArrayInputStream;

import kr.ac.uos.ai.robot.intelligent.taskReasoner.utility.model.FormulaExpression;
import kr.ac.uos.ai.robot.intelligent.taskReasoner.utility.parser.ParseException;
import kr.ac.uos.ai.robot.intelligent.taskReasoner.utility.parser.UtilityFunctionParser;

public class ParserTest {

	public static void main(String[] args) {
		String stringFunction = "TestPolicy * 10 - TTPolicy";

		UtilityFunctionParser parser = new UtilityFunctionParser(new ByteArrayInputStream(stringFunction.getBytes()));
		
		try {
			FormulaExpression expression = parser.parse();
			
			expression.print(null, null);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
