package kr.ac.uos.ai.robot.intelligent.taskReasoner.action.argument;

import kr.ac.uos.ai.arbi.model.GeneralizedList;

public class ContextArgument {

	private String name;
	private String stringGL;
	private GeneralizedList glContext;
	
	public ContextArgument() {
		// TODO Auto-generated constructor stub
		name = "";
		stringGL = "";
		glContext = null;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStringGL() {
		return stringGL;
	}

	public void setStringGL(String stringGL) {
		this.stringGL = stringGL;
	}

	
	public GeneralizedList getGlContext() {
		return glContext;
	}

	public void setGlContext(GeneralizedList glContext) {
		this.glContext = glContext;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return name;
	}
}
