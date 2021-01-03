package com.soze.grind.core.game.unit;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.soze.grind.core.game.building.Warehouse;
import com.soze.grind.core.game.resource.Resource;
import com.soze.grind.core.game.resource.ResourceEnum;
import com.soze.grind.core.game.storage.ResourceStorage;
import com.soze.grind.core.game.world.MyWorld;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.StateMachineBuilder;
import org.springframework.statemachine.config.StateMachineBuilder.Builder;

/** Represents the AI of a worker. */
public class WorkerAI {

  private final Worker worker;
  private final StateMachine<WorkerState, WorkerEvent> state;

  public WorkerAI(Worker worker) {
    this.worker = worker;
    this.state = initialState();
  }

  /**
   * Gets the progress of the current 'WORKING' state (or 0 if in different state).
   */
  public float getWorkingProgress() {
    Map<Object, Object> variables = this.state.getExtendedState().getVariables();

    float workingTime = (float) variables.getOrDefault("workingTime", 0f);
    float maxWorkingTime = (float) variables.getOrDefault("maxWorkingTime", 0f);

    if (maxWorkingTime == 0f) {
      return 0f;
    }

    return workingTime / maxWorkingTime;
  }

  /**
   * Gets the current worker state.
   */
  public WorkerState getState() {
    return this.state.getState().getId();
  }

  /**
   * Updates the Worker AI.
   */
  public void act(float delta, MyWorld myWorld) {

    WorkerState workerState = state.getState().getId();

    switch (workerState) {
      case IDLE:
        handleIdle(delta);
        break;
      case SEARCHING_FOR_RESOURCE:
        handleSearchingForResource(delta, myWorld);
        break;
      case SEARCHING_FOR_PATH:
        handleSearchingForPath(delta);
        break;
      case SEARCHING_FOR_WAREHOUSE:
        handleSearchingForWarehouse(delta, myWorld);
        break;
      case TRAVELLING:
        handleTravelling(delta);
        break;
      case WORKING:
        handleWorking(delta);
        break;
    }
  }

  /**
   * Handles the IDLE state.
   */
  private void handleIdle(float delta) {

    if (this.worker.capacity(ResourceEnum.WOOD) == 0) {
      this.state.sendEvent(WorkerEvent.START_SEARCHING_WAREHOUSE);
    } else {
      this.state.sendEvent(WorkerEvent.START_SEARCHING_RESOURCE);
    }
  }

  /**
   * Handles searching for resources
   */
  private void handleSearchingForResource(float delta, MyWorld myWorld) {

    List<Resource> resources = new ArrayList<>(myWorld.getResourceLayer().getResources());

    resources.removeIf(resource -> resource.getAmountResource() == 0);

    resources.sort(
        (o1, o2) -> {
          float distance1 = distance(o1);
          float distance2 = distance(o2);

          return Float.compare(distance1, distance2);
        });

    if (!resources.isEmpty()) {

      Resource resource = resources.get(0);

      Map<String, Object> headers = new HashMap<>();

      headers.put("target", resource);

      this.state.sendEvent(new GenericMessage<>(WorkerEvent.START_SEARCHING_PATH, headers));
    }
  }

  /**
   * Finds the nearest warehouse to transfer goods to.
   */
  private void handleSearchingForWarehouse(float delta, MyWorld myWorld) {

    List<Actor> buildings = new ArrayList<>(myWorld.getBuildingsLayer().getBuildings());

    buildings.sort(
        (o1, o2) -> {
          float distance1 = distance(o1);
          float distance2 = distance(o2);

          return Float.compare(distance1, distance2);
        });

    if (!buildings.isEmpty()) {

      Actor building = buildings.get(0);

      Map<String, Object> headers = new HashMap<>();

      headers.put("target", building);

      this.state.sendEvent(new GenericMessage<>(WorkerEvent.START_SEARCHING_PATH, headers));
    }
  }

  /**
   * Handles searching for path for the target.
   */
  private void handleSearchingForPath(float delta) {
    this.state.sendEvent(WorkerEvent.START_TRAVELLING);
  }

  /**
   * Handles travelling to the target.
   */
  private void handleTravelling(float delta) {

    Actor target = getTarget(Actor.class);

    float distance = distance(target);

    if (distance < 12f) {
      // arrived

      if (target instanceof Resource) {
        this.state.sendEvent(WorkerEvent.START_WORKING);
      } else if (target instanceof Warehouse) {
        transferAllResources(target);
        this.state.sendEvent(WorkerEvent.STOP);
      }

    } else {

      float angle =
          new Vector2(target.getX(), target.getY())
              .sub(new Vector2(worker.getX(), worker.getY()))
              .nor()
              .angleRad();

      float cos = MathUtils.cos(angle);
      float sin = MathUtils.sin(angle);

      float speed = 2.5f;

      this.worker.moveBy(cos * speed, sin * speed);
    }
  }

  private void handleWorking(float delta) {
    Map<Object, Object> variables = this.state.getExtendedState().getVariables();

    float workingTime = (float) variables.get("workingTime");
    float maxWorkingTime = (float) variables.get("maxWorkingTime");

    workingTime += delta;

    if (workingTime >= maxWorkingTime) {
      // FINISHED WORKING
      Resource resource = getTarget(Resource.class);

      int amountRemoved = resource.removeResource(1);

      this.worker.addResource(resource.getResourceEnum(), amountRemoved);

      this.state.sendEvent(WorkerEvent.STOP);

    } else {
      variables.put("workingTime", workingTime);
    }
  }

  private StateMachine<WorkerState, WorkerEvent> initialState() {

    Builder<WorkerState, WorkerEvent> builder = StateMachineBuilder.builder();

    try {
      builder
          .configureConfiguration()
          .withConfiguration()
          .autoStartup(true)
          .listener(new WorkerStateListener(this.worker));

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
          .action(onStartSearchingForTarget());

      builder
          .configureTransitions()
          .withExternal()
          .source(WorkerState.SEARCHING_FOR_WAREHOUSE)
          .target(WorkerState.SEARCHING_FOR_PATH)
          .event(WorkerEvent.START_SEARCHING_PATH)
          .action(onStartSearchingForTarget());

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

  private Action<WorkerState, WorkerEvent> onStartSearchingForTarget() {
    return (context) -> {
      Object target = context.getMessage().getHeaders().get("target");

      Map<Object, Object> variables = context.getExtendedState().getVariables();

      variables.put("target", target);
    };
  }

  private Action<WorkerState, WorkerEvent> onStartWorking() {
    return (context) -> {
      Map<Object, Object> variables = context.getExtendedState().getVariables();

      variables.put("workingTime", 0f);
      variables.put("maxWorkingTime", 10f);
    };
  }

  private Action<WorkerState, WorkerEvent> onStop() {
    return (context) -> {
      Map<Object, Object> variables = context.getExtendedState().getVariables();

      variables.put("workingTime", 0f);
      variables.put("maxWorkingTime", 0f);
    };
  }

  /**
   * Gets the target from extended state.
   */
  private <T> T getTarget(Class<T> clazz) {
    return this.state.getExtendedState().get("target", clazz);
  }

  /**
   * Returns the worker distance to the target.
   */
  private float distance(Actor target) {
    return new Vector2(target.getX(), target.getY()).dst(new Vector2(worker.getX(), worker.getY()));
  }

  /** Attempts to transfer all resources to given target. */
  private void transferAllResources(Actor target) {

    ResourceStorage targetStorage = null;

    if (target instanceof Warehouse) {
      Warehouse warehouse = (Warehouse) target;

      if (warehouse.getResourceStorage().isPresent()) {
        targetStorage = warehouse.getResourceStorage().get();
      }
    }

    if (Objects.nonNull(targetStorage)) {

      Map<ResourceEnum, Integer> workerResources = this.worker.getResources();

      for (ResourceEnum resourceEnum : workerResources.keySet()) {
        int count = workerResources.get(resourceEnum);

        int amountAdded = targetStorage.addResource(resourceEnum, count);
        this.worker.removeResource(resourceEnum, amountAdded);
      }
    }
  }

  /**
   * Attempts to retrieve a object from extended state and casts it to given class.
   *
   * @param key - name of the variable
   * @param clazz - class to cast it to
   * @return object from extended state or null if it doesn't exist.
   */
  private <T> T getExtended(Object key, Class<T> clazz) {
    return this.state.getExtendedState().get(key, clazz);
  }

  /**
   * If the worker is travelling or working on something, retrieves the current target.
   */
  public Object getTarget() {
    return getExtended("target", Object.class);
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
