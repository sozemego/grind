package com.soze.grind.core.game.loader;

import com.artemis.World;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.soze.grind.core.game.ecs.component.ResourceStorageComponent;
import com.soze.grind.core.game.ecs.component.factory.ComponentFactory;
import com.soze.grind.core.game.resource.ResourceEnum;
import com.soze.grind.core.game.storage.ResourceStorage;
import java.util.Objects;
import java.util.function.Consumer;

public class EntityLoader {

  private final World world;
  private final int entityId;
  private final JsonNode node;
  private final ComponentFactory componentFactory;

  public EntityLoader(World world, JsonNode node, ComponentFactory componentFactory) {
    this.world = world;
    this.entityId = world.create();
    this.node = node;
    this.componentFactory = componentFactory;
  }

  public void loadEntity() {

    callIfExists(node.get("position"), this::loadPositionComponent);
    callIfExists(node.get("actor"), this::loadActorComponent);
    callIfExists(node.get("building"), this::loadBuildingComponent);
    callIfExists(node.get("name"), this::loadNameComponent);
    callIfExists(node.get("resource"), this::loadResourceComponent);
    callIfExists(node.get("resourceStorage"), this::loadResourceStorageComponent);
    callIfExists(node.get("warehouse"), this::loadWarehouseComponent);
    callIfExists(node.get("workerAi"), this::loadWorkerAiComponent);
    callIfExists(node.get("worker"), this::loadWorkerComponent);

  }

  private void callIfExists(JsonNode jsonNode, Consumer<JsonNode> consumer) {
    if (Objects.nonNull(jsonNode)) {
      consumer.accept(jsonNode);
    }
  }

  /** Loads the PositionComponent. */
  private void loadPositionComponent(JsonNode positionNode) {

    float x = positionNode.get("x").asInt();
    float y = positionNode.get("y").asInt();
    float width = getFloat(positionNode, "width", 1);
    float height = getFloat(positionNode, "height", 1);

    componentFactory.createPositionComponent(entityId, x * 64, y * 64, width * 64, height * 64);
  }

  /** Loads the ActorComponent. */
  private void loadActorComponent(JsonNode actorNode) {

    String texture = actorNode.get("texture").asText();

    componentFactory.createImageActorComponent(entityId, texture);
  }

  /** Loads the BuildingComponent. */
  private void loadBuildingComponent(JsonNode buildingNode) {

    componentFactory.createBuildingComponent(entityId);
  }

  /**
   * Loads the NameComponent.
   */
  private void loadNameComponent(JsonNode nameNode) {
    componentFactory.createNameComponent(entityId, nameNode.asText());
  }

  /**
   * Loads the ResourceComponent.
   */
  private void loadResourceComponent(JsonNode resourceNode) {
    componentFactory.createResourceComponent(entityId, ResourceEnum.valueOf(resourceNode.asText()));
  }

  /**
   * Loads the ResourceStorageComponent.
   */
  private void loadResourceStorageComponent(JsonNode resourceStorageNode) {

    int capacity = resourceStorageNode.get("capacity").asInt();

    ResourceStorageComponent resourceStorageComponent = componentFactory.createResourceStorageComponent(entityId, capacity);

    ResourceStorage resourceStorage = resourceStorageComponent.getResourceStorage();

    JsonNode resourcesNode = resourceStorageNode.get("resources");

    if (Objects.nonNull(resourcesNode) && resourcesNode.getNodeType() == JsonNodeType.OBJECT) {

      ObjectNode resourcesMap = (ObjectNode) resourcesNode;

      for (ResourceEnum value : ResourceEnum.values()) {
        JsonNode resourceNode = resourcesMap.get(value.name());

        if (Objects.nonNull(resourceNode)) {
          resourceStorage.addResource(value, resourceNode.asInt());
        }

      }
    }

  }

  /**
   * Loads the WarehouseComponent.
   */
  private void loadWarehouseComponent(JsonNode warehouseNode) {
    componentFactory.createWarehouseComponent(entityId);
  }

  /**
   * Loads the WorkerAiComponent and WorkerProgressBarComponent.
   */
  private void loadWorkerAiComponent(JsonNode workerAiNode) {
    componentFactory.createWorkerAiComponent(entityId);
    componentFactory.createWorkerProgressBarComponent(entityId);
  }

  /**
   * Loads the WorkerComponent.
   */
  private void loadWorkerComponent(JsonNode workerNode) {
    componentFactory.createWorkerComponent(entityId);
  }

  /**
   * Gets a float from a property or defaultValue if it does not exist.
   *
   * @param node node to get the float from
   * @param propertyName name of the property
   * @param defaultValue default value
   * @return float
   */
  private float getFloat(JsonNode node, String propertyName, float defaultValue) {
    JsonNode floatNode = node.get(propertyName);

    if (Objects.isNull(floatNode) || floatNode.getNodeType() == JsonNodeType.NULL) {
      return defaultValue;
    }

    return (float) floatNode.asDouble();
  }
}
