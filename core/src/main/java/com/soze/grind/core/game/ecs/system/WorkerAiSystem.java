package com.soze.grind.core.game.ecs.system;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySubscription;
import com.artemis.World;
import com.artemis.annotations.All;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.soze.grind.core.game.ecs.component.PositionComponent;
import com.soze.grind.core.game.ecs.component.ResourceComponent;
import com.soze.grind.core.game.ecs.component.ResourceStorageComponent;
import com.soze.grind.core.game.ecs.component.WarehouseComponent;
import com.soze.grind.core.game.ecs.component.WorkerAiComponent;
import com.soze.grind.core.game.ecs.component.WorkerAiComponent.WorkerEvent;
import com.soze.grind.core.game.ecs.component.WorkerAiComponent.WorkerState;
import com.soze.grind.core.game.resource.ResourceEnum;
import com.soze.grind.core.game.storage.ResourceStorage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Service;

@Service
@All({WorkerAiComponent.class, PositionComponent.class, ResourceStorageComponent.class})
public class WorkerAiSystem extends BaseEntitySystem {

  private ComponentMapper<WorkerAiComponent> workerAiMapper;
  private ComponentMapper<PositionComponent> positionMapper;
  private ComponentMapper<ResourceStorageComponent> resourceStorageMapper;

  private int workerId;

  @Override
  protected void processSystem() {

    IntBag activeEntities = getSubscription().getEntities();
    int[] ids = activeEntities.getData();

    for (int i = 0; i < activeEntities.size(); i++) {

      int id = ids[i];

      this.workerId = id;

      WorkerAiComponent workerAiComponent = workerAiMapper.get(id);

      WorkerState workerState = workerAiComponent.getState();

      switch (workerState) {
        case IDLE:
          handleIdle();
          break;
        case SEARCHING_FOR_RESOURCE:
          handleSearchingForResource();
          break;
        case SEARCHING_FOR_PATH:
          handleSearchingForPath();
          break;
        case SEARCHING_FOR_WAREHOUSE:
          handleSearchingForWarehouse();
          break;
        case TRAVELLING:
          handleTravelling();
          break;
        case WORKING:
          handleWorking();
          break;
      }
    }
  }

  /** Handles the IDLE state. */
  private void handleIdle() {

    ResourceStorage resourceStorage = resourceStorageMapper.get(workerId).getResourceStorage();

    if (resourceStorage.capacity(ResourceEnum.WOOD) == 0) {
      sendEvent(WorkerEvent.START_SEARCHING_WAREHOUSE);
    } else {
      sendEvent(WorkerEvent.START_SEARCHING_RESOURCE);
    }
  }

  /** Handles searching for resources */
  private void handleSearchingForResource() {

    EntitySubscription entitySubscription =
        world.getAspectSubscriptionManager().get(Aspect.all(ResourceComponent.class));

    IntBag entityIds = entitySubscription.getEntities();
    int[] ids = entityIds.getData();

    List<Integer> resources = new ArrayList<>();

    ComponentMapper<ResourceStorageComponent> resourceStorageMapper =
        world.getMapper(ResourceStorageComponent.class);

    ComponentMapper<ResourceComponent> resourceComponentMapper =
        world.getMapper(ResourceComponent.class);

    for (int i = 0; i < entityIds.size(); i++) {
      int id = ids[i];

      ResourceStorageComponent resourceStorageComponent = resourceStorageMapper.get(id);
      ResourceComponent resourceComponent = resourceComponentMapper.get(id);

      ResourceStorage resourceStorage = resourceStorageComponent.getResourceStorage();

      if (resourceStorage.count(resourceComponent.getResourceEnum()) > 0) {
        resources.add(id);
      }
    }

    resources.sort(
        (o1, o2) -> {
          float distance1 = distance(world.getEntity(o1));
          float distance2 = distance(world.getEntity(o2));

          return Float.compare(distance1, distance2);
        });

    if (!resources.isEmpty()) {

      int targetEntityId = resources.get(0);

      Map<String, Object> headers = new HashMap<>();

      headers.put("target", targetEntityId);

      sendEvent(new GenericMessage<>(WorkerEvent.START_SEARCHING_PATH, headers));
    }
  }

  /** Handles travelling to the target. */
  private void handleTravelling() {

    Integer targetEntityId = getTarget();

    if (Objects.isNull(targetEntityId)) {
      return;
    }

    Entity target = world.getEntity(targetEntityId);

    float distance = distance(target);

    if (distance < 12f) {
      // arrived

      if (Objects.nonNull(target.getComponent(ResourceComponent.class))) {
        sendEvent(WorkerEvent.START_WORKING);
      } else if (Objects.nonNull(target.getComponent(WarehouseComponent.class))) {
        transferAllResources(target);
        sendEvent(WorkerEvent.STOP);
      }

    } else {

      PositionComponent targetPositionComponent = target.getComponent(PositionComponent.class);
      PositionComponent workerPositionComponent = positionMapper.get(workerId);

      float angle =
          new Vector2(targetPositionComponent.getX(), targetPositionComponent.getY())
              .sub(new Vector2(workerPositionComponent.getX(), workerPositionComponent.getY()))
              .nor()
              .angleRad();

      float cos = MathUtils.cos(angle);
      float sin = MathUtils.sin(angle);

      float speed = 2.5f;

      workerPositionComponent.setPosition(
          workerPositionComponent.getX() + cos * speed,
          workerPositionComponent.getY() + sin * speed);
    }
  }

  /** Handles searching for path for the target. */
  private void handleSearchingForPath() {
    sendEvent(WorkerEvent.START_TRAVELLING);
  }

  /** Finds the nearest warehouse to transfer goods to. */
  private void handleSearchingForWarehouse() {

    EntitySubscription entitySubscription =
        world.getAspectSubscriptionManager().get(Aspect.all(WarehouseComponent.class));

    IntBag entityIds = entitySubscription.getEntities();
    int[] ids = entityIds.getData();

    List<Integer> buildings = new ArrayList<>();

    for (int i = 0; i < entityIds.size(); i++) {
      int id = ids[i];

      buildings.add(id);
    }

    buildings.sort(
        (o1, o2) -> {
          float distance1 = distance(world.getEntity(o1));
          float distance2 = distance(world.getEntity(o2));

          return Float.compare(distance1, distance2);
        });

    if (!buildings.isEmpty()) {

      int targetEntityId = buildings.get(0);

      Map<String, Object> headers = new HashMap<>();

      headers.put("target", targetEntityId);

      sendEvent(new GenericMessage<>(WorkerEvent.START_SEARCHING_PATH, headers));
    }
  }

  private void handleWorking() {
    Map<Object, Object> variables = getStateMachine().getExtendedState().getVariables();

    float workingTime = (float) variables.get("workingTime");
    float maxWorkingTime = (float) variables.get("maxWorkingTime");

    workingTime += getWorld().getDelta();

    if (workingTime >= maxWorkingTime) {
      // FINISHED WORKING
      Integer targetEntityId = getTarget();

      Entity targetEntity = world.getEntity(targetEntityId);

      ResourceComponent resourceComponent = targetEntity.getComponent(ResourceComponent.class);
      ResourceStorage targetStorage =
          targetEntity.getComponent(ResourceStorageComponent.class).getResourceStorage();

      int amountRemoved = targetStorage.removeResource(resourceComponent.getResourceEnum(), 1);

      ResourceStorage workerStorage = resourceStorageMapper.get(workerId).getResourceStorage();

      workerStorage.addResource(resourceComponent.getResourceEnum(), amountRemoved);

      sendEvent(WorkerEvent.STOP);

    } else {
      variables.put("workingTime", workingTime);
    }
  }

  /**
   * Gets the current state machine of the worker.
   *
   * @return StateMachine
   */
  private StateMachine<WorkerState, WorkerEvent> getStateMachine() {
    WorkerAiComponent workerAiComponent = workerAiMapper.get(workerId);

    return workerAiComponent.getStateMachine();
  }

  /**
   * Sends a WorkerEvent to the state machine.
   *
   * @param workerEvent event to send
   */
  private void sendEvent(WorkerEvent workerEvent) {
    this.getStateMachine().sendEvent(workerEvent);
  }

  /**
   * Sends a WorkerEvent to the state machine.
   *
   * @param workerEvent event to send
   */
  private void sendEvent(GenericMessage<WorkerEvent> workerEvent) {
    this.getStateMachine().sendEvent(workerEvent);
  }

  /** Returns the worker distance to the target. */
  private float distance(Entity targetEntity) {

    PositionComponent positionComponent = targetEntity.getComponent(PositionComponent.class);
    PositionComponent workerPositionComponent = positionMapper.get(workerId);

    return new Vector2(positionComponent.getX(), positionComponent.getY())
        .dst(new Vector2(workerPositionComponent.getX(), workerPositionComponent.getY()));
  }

  /** Gets the target from extended state. */
  private Integer getTarget() {
    return this.getStateMachine().getExtendedState().get("target", Integer.class);
  }

  /** Attempts to transfer all resources to given target. */
  private void transferAllResources(Entity target) {

    ResourceStorageComponent resourceStorageComponent =
        target.getComponent(ResourceStorageComponent.class);

    if (Objects.isNull(resourceStorageComponent)) {
      return;
    }

    ResourceStorage targetStorage = resourceStorageComponent.getResourceStorage();

    if (Objects.nonNull(targetStorage)) {

      ResourceStorage workerStorage = resourceStorageMapper.get(workerId).getResourceStorage();

      Map<ResourceEnum, Integer> workerResources = workerStorage.getResources();

      for (ResourceEnum resourceEnum : workerResources.keySet()) {
        int count = workerResources.get(resourceEnum);

        int amountAdded = targetStorage.addResource(resourceEnum, count);
        workerStorage.removeResource(resourceEnum, amountAdded);
      }
    }
  }

}
