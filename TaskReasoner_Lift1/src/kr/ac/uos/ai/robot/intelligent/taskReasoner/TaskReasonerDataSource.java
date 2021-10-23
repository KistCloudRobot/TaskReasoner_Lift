package kr.ac.uos.ai.robot.intelligent.taskReasoner;

import kr.ac.uos.ai.arbi.ltm.DataSource;

public class TaskReasonerDataSource extends DataSource{

	private TaskReasoner_Lift2 taskReasoner;
	
	public TaskReasonerDataSource(TaskReasoner_Lift2 taskReasoner) {
		// TODO Auto-generated constructor stub
		this.taskReasoner = taskReasoner;
	}
	
	@Override
	public void onNotify(String content) {

		System.out.println("Notified!" + content);
		taskReasoner.onNotify("LTM", content);
	
	}
}
