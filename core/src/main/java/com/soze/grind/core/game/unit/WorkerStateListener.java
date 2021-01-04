package com.soze.grind.core.game.unit;

import com.soze.grind.core.game.ecs.component.WorkerAiComponent.WorkerEvent;
import com.soze.grind.core.game.ecs.component.WorkerAiComponent.WorkerState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

public class WorkerStateListener
    extends StateMachineListenerAdapter<WorkerState, WorkerEvent> {

  private static final Logger LOG = LogManager.getLogger(WorkerStateListener.class);
  private static final Marker WORKER_AI_MARKER = MarkerManager.getMarker("WORKER_AI");

  private String uuid;

  @Override
  public void stateMachineStarted(StateMachine<WorkerState, WorkerEvent> stateMachine) {
    this.uuid = stateMachine.getUuid().toString();
    LOG.trace(WORKER_AI_MARKER, "Machine with id = [{}] started in state [{}]", this.uuid, stateMachine.getState().getIds());
  }

  @Override
  public void stateEntered(State<WorkerState, WorkerEvent> state) {
    LOG.trace(WORKER_AI_MARKER, "Machine with id = [{}] entered state = [{}]", this.uuid, state.getId());
  }

}
