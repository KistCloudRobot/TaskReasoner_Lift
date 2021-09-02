package kr.ac.uos.ai.robot.intelligent.taskReasoner;

import java.io.File;
import java.util.ArrayList;

import uos.ai.jam.Interpreter;
import uos.ai.jam.parser.JAMParser;

public class PlanLoader {

	private Interpreter 			interpreter;
	
	public PlanLoader(Interpreter interpreter) {
		
		this.interpreter = interpreter;
		
	}
	
	public void loadPlan(String path) {
		File plan = new File(path);
		if (!plan.exists()) {
			System.out.println("Plan File is not found");
			return;
		}
		if (!plan.isFile()) {
			System.out.println("This Path is not a File");
			return;
		}
		JAMParser.parseFile(interpreter, new File(path));
	}
	public void parsePlan(String plan) {
		System.out.println("plan to parse : \n" + plan);
		JAMParser.parseString(interpreter, plan);
	}
	
	public void loadPlanPackage(String folder) {
		File directory = new File(folder);
		if (!directory.exists()) {
			System.out.println("Plan Package is not found");
			return;
		}
		if (directory.isFile()) {
			System.out.println("This Path is not a Package");
			return;
		}
		ArrayList<File> files = new ArrayList<File>();
		

		findPath(files, directory);
		

		for (File file : files) {
			JAMParser.parseFile(interpreter, file);
		}
	}	private void findPath(ArrayList<File> files, File directory) {
		for (File childFile : directory.listFiles()) {
			if (childFile.isDirectory()) {
				findPath(files, childFile);
			}else{
				files.add(childFile);
			}
		}

	}
}
