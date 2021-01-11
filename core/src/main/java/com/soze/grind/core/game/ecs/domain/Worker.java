package com.soze.grind.core.game.ecs.domain;

import com.artemis.Entity;
import com.soze.grind.core.game.ecs.component.WorkerAiComponent;
import com.soze.grind.core.game.ecs.component.WorkerAiComponent.WorkerEvent;
import com.soze.grind.core.game.ecs.component.WorkerAiComponent.WorkerState;
import org.springframework.statemachine.StateMachine;

/**
 * Represents a Worker.
 */
public class Worker extends AbstractEntity {

	public Worker(Entity entity) {
		super(entity);
	}

	public WorkerState getWorkerState() {
		return getComponent(WorkerAiComponent.class).getState();
	}

	public StateMachine<WorkerState, WorkerEvent> getStateMachine() {
		return getComponent(WorkerAiComponent.class).getStateMachine();
	}

	public float getWorkingProgress() {
		return getComponent(WorkerAiComponent.class).getWorkingProgress();
	}
}
