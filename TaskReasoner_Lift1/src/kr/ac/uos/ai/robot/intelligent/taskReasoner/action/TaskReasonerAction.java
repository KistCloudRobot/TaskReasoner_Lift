package kr.ac.uos.ai.robot.intelligent.taskReasoner.action;


import kr.ac.uos.ai.arbi.agent.logger.AgentAction;
import kr.ac.uos.ai.arbi.agent.logger.LogTiming;
import kr.ac.uos.ai.arbi.agent.logger.LoggerManager;
import kr.ac.uos.ai.arbi.model.GLFactory;
import kr.ac.uos.ai.arbi.model.GeneralizedList;
import kr.ac.uos.ai.arbi.model.parser.ParseException;
import kr.ac.uos.ai.robot.intelligent.taskReasoner.TaskReasoner_Lift1;
import kr.ac.uos.ai.robot.intelligent.taskReasoner.action.argument.ContextArgument;
import kr.ac.uos.ai.robot.intelligent.taskReasoner.action.argument.GoalPostArgument;
import uos.ai.jam.Interpreter;

public class TaskReasonerAction {
	
	private AgentAction assertContextAction;
	private AgentAction policyUpdateAction;
	private AgentAction serviceAppendAction;
	private AgentAction policyAppendAction;
	private AgentAction retractContextAction;
	private AgentAction serviceFinishAction;
	private AgentAction GoalAppendAction;
	private AgentAction GoalPostAction;
	private AgentAction GoalUnpostAction;
	
	private TaskReasoner_Lift1 taskReasoner;
	private Interpreter interpreter;
	private LoggerManager loggerManager;

	public TaskReasonerAction(TaskReasoner_Lift1 reasoner, Interpreter interpreter, LoggerManager loggerManager) {
		this.taskReasoner = reasoner;
		this.interpreter = interpreter;
		this.loggerManager = loggerManager;
		
		init();
	}
	
	public void init() {
		assertContextAction = new AgentAction("assertContext", new AssertContextAction(taskReasoner.getGlMessageManager()));
		retractContextAction = new AgentAction("retractContext", new RetractContextAction(taskReasoner.getGlMessageManager()));
		policyAppendAction = new AgentAction("policyAppend", new PolicyAppendAction(taskReasoner.getPolicyHandler()));
		policyUpdateAction = new AgentAction("policyUpdate", new PolicyUpdateAction(taskReasoner.getPolicyHandler()));
		serviceAppendAction = new AgentAction("serviceAppend", new ServiceAppendAction(taskReasoner.getServiceModelGenerator()));
		serviceFinishAction = new AgentAction("serviceFinish", new ServiceFinishAction());
		GoalAppendAction = new AgentAction("goalAppend", new GoalAppendAction(taskReasoner.getServiceModelGenerator()));
		GoalPostAction = new AgentAction("goalPost", new GoalPostAction(taskReasoner));
		GoalUnpostAction = new AgentAction("goalUnpost", new GoalUnpostAction(taskReasoner));

		loggerManager.registerAction(assertContextAction,LogTiming.Prior);
		loggerManager.registerAction(retractContextAction,LogTiming.Prior);
		loggerManager.registerAction(policyAppendAction,LogTiming.Prior);
		loggerManager.registerAction(policyUpdateAction,LogTiming.Prior);
		loggerManager.registerAction(serviceAppendAction,LogTiming.Prior);
		loggerManager.registerAction(serviceFinishAction,LogTiming.Prior);
		loggerManager.registerAction(GoalAppendAction,LogTiming.Prior);
		loggerManager.registerAction(GoalPostAction,LogTiming.Prior);
		loggerManager.registerAction(GoalUnpostAction, LogTiming.Prior);
		
		assertContextAction.changeAction(true);
		retractContextAction.changeAction(true);
		policyAppendAction.changeAction(true);
		policyUpdateAction.changeAction(true);
		serviceAppendAction.changeAction(true);
		serviceFinishAction.changeAction(true);
		GoalAppendAction.changeAction(true);
		GoalPostAction.changeAction(true);
		GoalUnpostAction.changeAction(true);
	}

	
	public void action(String str, Object o) {
		System.out.println(str);
		loggerManager.getAction(str).execute(o);
	}
	
	public ContextArgument generateContextArgument(String glString) {
		
		ContextArgument argument = new ContextArgument();
		
		GeneralizedList gl = null;
		GeneralizedList glContext = null;
		try {
			gl = GLFactory.newGLFromGLString(glString);
			glContext = gl.getExpression(0).asGeneralizedList();
			argument.setName(glContext.getName());
			argument.setGlContext(glContext);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return argument;
	}
	
	public GoalPostArgument generateGoalPostArgument(String serviceName, String goalName, Object... args) {
		GoalPostArgument argument = new GoalPostArgument();
		
		argument.setGoalName(goalName);
		argument.setServiceName(serviceName);
		argument.setArguments(args);
		
		return argument;
	}
	
	public GoalPostArgument generateGoalUnpostArgument(String serviceName, String goalName) {
		GoalPostArgument argument = new GoalPostArgument();
		
		argument.setGoalName(goalName);
		argument.setServiceName(serviceName);
		return argument;
		
	}
}
