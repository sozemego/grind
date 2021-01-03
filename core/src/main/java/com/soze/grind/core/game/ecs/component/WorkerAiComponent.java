package com.soze.grind.core.game.ecs.component;

import com.artemis.Component;
import com.artemis.Entity;
import com.soze.grind.core.game.unit.WorkerAI;

public class WorkerAiComponent extends Component {

	private WorkerAI workerAI;

	public void setEntity(Entity entity) {
		this.workerAI = new WorkerAI(entity);
	}

	public WorkerAI getWorkerAI() {
		return workerAI;
	}
}
