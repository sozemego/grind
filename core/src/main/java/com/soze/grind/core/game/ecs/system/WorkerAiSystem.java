package com.soze.grind.core.game.ecs.system;

import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
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
import com.soze.grind.core.game.ecs.domain.AbstractEntity;
import com.soze.grind.core.game.ecs.domain.Resource;
import com.soze.grind.core.game.ecs.domain.Warehouse;
import com.soze.grind.core.game.ecs.domain.Worker;
import com.soze.grind.core.game.ecs.world.GameWorld;
import com.soze.grind.core.game.resource.ResourceEnum;
import com.soze.grind.core.game.storage.ResourceStorage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;

@Service
@All({WorkerAiComponent.class, PositionComponent.class, ResourceStorageComponent.class})
public class WorkerAiSystem extends BaseEntitySystem {

  private GameWorld gameWorld;

  private ComponentMapper<WorkerAiComponent> workerAiMapper;
  private ComponentMapper<PositionComponent> positionMapper;
  private ComponentMapper<ResourceStorageComponent> resourceStorageMapper;

  private Worker worker;

  @Override
  protected void processSystem() {

    IntBag activeEntities = getSubscription().getEntities();
    int[] ids = activeEntities.getData();

    for (int i = 0; i < activeEntities.size(); i++) {

      int id = ids[i];

      worker = new Worker(getWorld().getEntity(id));

      WorkerState workerState = worker.getWorkerState();

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

    ResourceStorage resourceStorage = worker.getResourceStorage();

    if (resourceStorage.capacity(ResourceEnum.WOOD) == 0) {
      sendEvent(WorkerEvent.START_SEARCHING_WAREHOUSE);
    } else {
      sendEvent(WorkerEvent.START_SEARCHING_RESOURCE);
    }
  }

  /** Handles searching for resources */
  private void handleSearchingForResource() {

    List<Resource> resources = getGameWorld().getResources();

    resources.sort(
        (o1, o2) -> {
          float distance1 = distance(o1);
          float distance2 = distance(o2);

          return Float.compare(distance1, distance2);
        });

    if (!resources.isEmpty()) {

      AbstractEntity target = resources.get(0);

      Map<String, Object> headers = new HashMap<>();

      headers.put("target", target);

      sendEvent(new GenericMessage<>(WorkerEvent.START_SEARCHING_PATH, headers));
    }
  }

  /** Handles travelling to the target. */
  private void handleTravelling() {

    AbstractEntity target = getTarget();

    if (Objects.isNull(target)) {
      return;
    }

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

      Vector2 targetPosition = target.getPosition();
      Vector2 workerPosition = worker.getPosition();

      float angle = targetPosition.sub(workerPosition).nor().angleRad();

      float cos = MathUtils.cos(angle);
      float sin = MathUtils.sin(angle);

      float speed = 2.5f;

      worker.setPosition(workerPosition.x + cos * speed, workerPosition.y + sin * speed);
    }
  }

  /** Handles searching for path for the target. */
  private void handleSearchingForPath() {
    sendEvent(WorkerEvent.START_TRAVELLING);
  }

  /** Finds the nearest warehouse to transfer goods to. */
  private void handleSearchingForWarehouse() {

    List<Warehouse> warehouses = getGameWorld().getWarehouses();

    warehouses.sort(
        (o1, o2) -> {
          float distance1 = distance(o1);
          float distance2 = distance(o2);

          return Float.compare(distance1, distance2);
        });

    if (!warehouses.isEmpty()) {

      Warehouse warehouse = warehouses.get(0);

      Map<String, Object> headers = new HashMap<>();

      headers.put("target", warehouse);

      sendEvent(new GenericMessage<>(WorkerEvent.START_SEARCHING_PATH, headers));
    }
  }

  private void handleWorking() {
    Map<Object, Object> variables = worker.getStateMachine().getExtendedState().getVariables();

    float workingTime = (float) variables.get("workingTime");
    float maxWorkingTime = (float) variables.get("maxWorkingTime");

    workingTime += getWorld().getDelta();

    if (workingTime >= maxWorkingTime) {
      // FINISHED WORKING
      AbstractEntity targetEntity = getTarget();

      ResourceComponent resourceComponent = targetEntity.getComponent(ResourceComponent.class);
      ResourceStorage targetStorage = targetEntity.getResourceStorage();

      int amountRemoved = targetStorage.removeResource(resourceComponent.getResourceEnum(), 1);

      ResourceStorage workerStorage = worker.getResourceStorage();

      workerStorage.addResource(resourceComponent.getResourceEnum(), amountRemoved);

      sendEvent(WorkerEvent.STOP);

    } else {
      variables.put("workingTime", workingTime);
    }
  }

  /**
   * Sends a WorkerEvent to the state machine.
   *
   * @param workerEvent event to send
   */
  private void sendEvent(WorkerEvent workerEvent) {
    worker.getStateMachine().sendEvent(workerEvent);
  }

  /**
   * Sends a WorkerEvent to the state machine.
   *
   * @param workerEvent event to send
   */
  private void sendEvent(GenericMessage<WorkerEvent> workerEvent) {
    worker.getStateMachine().sendEvent(workerEvent);
  }

  /** Returns the worker distance to the target. */
  private float distance(AbstractEntity targetEntity) {

    Vector2 targetPosition = targetEntity.getPosition();
    Vector2 workerPosition = worker.getPosition();

    return targetPosition.dst(workerPosition);
  }

  /** Gets the target from extended state. */
  private AbstractEntity getTarget() {
    return worker.getStateMachine().getExtendedState().get("target", AbstractEntity.class);
  }

  /** Attempts to transfer all resources to given target. */
  private void transferAllResources(AbstractEntity target) {

    ResourceStorage targetStorage = target.getResourceStorage();

    if (Objects.isNull(targetStorage)) {
      return;
    }

    ResourceStorage workerStorage = worker.getResourceStorage();

    Map<ResourceEnum, Integer> workerResources = workerStorage.getResources();

    for (ResourceEnum resourceEnum : workerResources.keySet()) {
      int count = workerResources.get(resourceEnum);

      int amountAdded = targetStorage.addResource(resourceEnum, count);
      workerStorage.removeResource(resourceEnum, amountAdded);
    }
  }

  private GameWorld getGameWorld() {
    if (Objects.isNull(gameWorld)) {
      gameWorld = new GameWorld(getWorld());
    }
    return gameWorld;
  }
}
