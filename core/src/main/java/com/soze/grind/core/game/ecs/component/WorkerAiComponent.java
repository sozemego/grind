package com.soze.grind.core.game.ecs.component;

import com.artemis.Component;
import com.soze.grind.core.game.unit.WorkerStateListener;
import java.util.EnumSet;
import java.util.Map;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.StateMachineBuilder;
import org.springframework.statemachine.config.StateMachineBuilder.Builder;

public class WorkerAiComponent extends Component {

  private StateMachine<WorkerState, WorkerEvent> state = initialState();

  public StateMachine<WorkerState, WorkerEvent> getStateMachine() {
    return state;
  }

  /** Gets the progress of the current 'WORKING' state (or 0 if in different state). */
  public float getWorkingProgress() {
    Map<Object, Object> variables = state.getExtendedState().getVariables();

    float workingTime = (float) variables.getOrDefault("workingTime", 0f);
    float maxWorkingTime = (float) variables.getOrDefault("maxWorkingTime", 0f);

    if (maxWorkingTime == 0f) {
      return 0f;
    }

    return workingTime / maxWorkingTime;
  }

  /** Gets the current worker state. */
  public WorkerState getState() {
    return state.getState().getId();
  }

  /**
   * Initializes the state machine of the worker AI.
   *
   * @return StateMachine
   */
  private StateMachine<WorkerState, WorkerEvent> initialState() {

    Builder<WorkerState, WorkerEvent> builder = StateMachineBuilder.builder();

    try {
      builder
          .configureConfiguration()
          .withConfiguration()
          .autoStartup(true)
          .listener(new WorkerStateListener());

      builder
          .configureTransitions()
          .withExternal()
          .source(WorkerState.IDLE)
          .target(WorkerState.SEARCHING_FOR_WAREHOUSE)
          .event(WorkerEvent.START_SEARCHING_WAREHOUSE);

      builder
          .configureStates()
          .withStates()
          .initial(WorkerState.IDLE)
          .states(EnumSet.allOf(WorkerState.class));

      builder
          .configureTransitions()
          .withExternal()
          .source(WorkerState.IDLE)
          .target(WorkerState.SEARCHING_FOR_RESOURCE)
          .event(WorkerEvent.START_SEARCHING_RESOURCE);

      builder
          .configureTransitions()
          .withExternal()
          .source(WorkerState.SEARCHING_FOR_RESOURCE)
          .target(WorkerState.SEARCHING_FOR_PATH)
          .event(WorkerEvent.START_SEARCHING_PATH)
          .action(onStartSearchingForPathToTarget());

      builder
          .configureTransitions()
          .withExternal()
          .source(WorkerState.SEARCHING_FOR_WAREHOUSE)
          .target(WorkerState.SEARCHING_FOR_PATH)
          .event(WorkerEvent.START_SEARCHING_PATH)
          .action(onStartSearchingForPathToTarget());

      builder
          .configureTransitions()
          .withExternal()
          .source(WorkerState.SEARCHING_FOR_PATH)
          .target(WorkerState.TRAVELLING)
          .event(WorkerEvent.START_TRAVELLING);

      builder
          .configureTransitions()
          .withExternal()
          .source(WorkerState.TRAVELLING)
          .target(WorkerState.WORKING)
          .event(WorkerEvent.START_WORKING)
          .action(onStartWorking());

      builder
          .configureTransitions()
          .withExternal()
          .target(WorkerState.IDLE)
          .source(WorkerState.TRAVELLING)
          .event(WorkerEvent.STOP);

      builder
          .configureTransitions()
          .withExternal()
          .target(WorkerState.IDLE)
          .source(WorkerState.WORKING)
          .event(WorkerEvent.STOP)
          .action(onStop());

    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }

    return builder.build();
  }

  /**
   * Action invoked when the worker starts searching for a path to the target.
   *
   * @return Action
   */
  private Action<WorkerState, WorkerEvent> onStartSearchingForPathToTarget() {
    return (context) -> {
      Object target = context.getMessage().getHeaders().get("target");

      Map<Object, Object> variables = context.getExtendedState().getVariables();

      variables.put("target", target);
    };
  }

  /**
   * Action called when the worker starts working.
   *
   * @return Action
   */
  private Action<WorkerState, WorkerEvent> onStartWorking() {
    return (context) -> {
      Map<Object, Object> variables = context.getExtendedState().getVariables();

      variables.put("workingTime", 0f);
      variables.put("maxWorkingTime", 10f);
    };
  }

  /**
   * Called when the worker stops whatever they were doing.
   *
   * @return Action
   */
  private Action<WorkerState, WorkerEvent> onStop() {
    return (context) -> {
      Map<Object, Object> variables = context.getExtendedState().getVariables();

      variables.put("workingTime", 0f);
      variables.put("maxWorkingTime", 0f);
    };
  }

  public enum WorkerState {
    IDLE,
    SEARCHING_FOR_RESOURCE,
    SEARCHING_FOR_PATH,
    SEARCHING_FOR_WAREHOUSE,
    TRAVELLING,
    WORKING;
  }

  public enum WorkerEvent {
    STOP,
    START_SEARCHING_RESOURCE,
    START_SEARCHING_PATH,
    START_SEARCHING_WAREHOUSE,
    START_TRAVELLING,
    START_WORKING;
  }
}
