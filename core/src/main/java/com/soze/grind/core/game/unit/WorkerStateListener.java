package com.soze.grind.core.game.unit;

import com.soze.grind.core.game.unit.WorkerAI.WorkerEvent;
import com.soze.grind.core.game.unit.WorkerAI.WorkerState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

public class WorkerStateListener
    extends StateMachineListenerAdapter<WorkerAI.WorkerState, WorkerAI.WorkerEvent> {

  private static final Logger LOG = LogManager.getLogger(WorkerStateListener.class);
  private static final Marker WORKER_AI_MARKER = MarkerManager.getMarker("WORKER_AI");

  private final Worker worker;
  private String uuid;

  public WorkerStateListener(Worker worker) {
    this.worker = worker;
  }

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
