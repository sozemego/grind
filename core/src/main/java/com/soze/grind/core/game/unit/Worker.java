package com.soze.grind.core.game.unit;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.soze.grind.core.game.resource.Resource;
import com.soze.grind.core.game.resource.ResourceEnum;
import com.soze.grind.core.game.storage.ResourceStorage;
import com.soze.grind.core.game.ui.ProgressBar;
import com.soze.grind.core.game.ui.factory.UIElementFactory;
import com.soze.grind.core.game.unit.WorkerAI.WorkerState;
import com.soze.grind.core.game.world.MyWorld;
import java.util.Map;

public class Worker extends Actor implements ResourceStorage {

  private final String name;

  private final Texture texture;

  private final WorkerAI workerAI = new WorkerAI(this);

  private final ResourceStorage storage;

  private final ProgressBar progressBar;

  public Worker(String name, Texture texture, ResourceStorage storage, UIElementFactory uiElementFactory) {
    this.name = name;
    this.texture = texture;
    this.storage = storage;
    this.progressBar = uiElementFactory.createGameWorldProgressBar(this.getWorkerAI()::getWorkingProgress);
  }

  /**
   * Updates the worker.
   */
  public void update(float delta, MyWorld myWorld) {
    this.workerAI.act(delta, myWorld);
  }

  public WorkerAI getWorkerAI() {
    return workerAI;
  }

  /**
   * If the unit is in WORKING state,
   * it tries to get a text to describe what the worker is doing ('harvesting wood` or 'mining stone').
   *
   * In other states, returns name of the state.
   *
   * @return string describing the current state
   */
  public String getStateText() {
    if (this.getState() != WorkerState.WORKING) {
      return this.getState().name();
    }

    Object target = getTarget();

    if (target instanceof Resource) {
      Resource resource = (Resource) target;

      switch (resource.getResourceEnum()) {
        case WOOD: return "Chopping wood";
        case STONE: return "Chipping stone";
        case IRON: return "Mining iron";
        case GOLD: return "Mining gold";
      }

    }

    return "";
  }

  /**
   * If the worker is travelling or working on something, retrieves the current target.
   */
  public Object getTarget() {
    return this.workerAI.getTarget();
  }

  /**
   * Returns the current state of the worker.
   */
  public WorkerState getState() {
    return this.workerAI.getState();
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public int addResource(ResourceEnum resource, int amount) {
    return storage.addResource(resource, amount);
  }

  @Override
  public int removeResource(ResourceEnum resource, int amount) {
    return storage.removeResource(resource, amount);
  }

  @Override
  public int count(ResourceEnum resource) {
    return storage.count(resource);
  }

  @Override
  public int capacity(ResourceEnum resource) {
    return storage.capacity(resource);
  }

  @Override
  public int maxCapacity(ResourceEnum resource) {
    return storage.maxCapacity(resource);
  }

  /**
   * Gets all stored resources.
   */
  public Map<ResourceEnum, Integer> getResources() {
    return storage.getResources();
  }

  @Override
  public void draw(Batch batch, float parentAlpha) {
    Color color = getColor();
    batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
    batch.draw(texture, this.getX(), this.getY(), this.getWidth(), this.getHeight());

    WorkerState workerState = this.getWorkerAI().getState();

    this.progressBar.setVisible(workerState == WorkerState.WORKING);

    if (this.progressBar.isVisible()) {

      this.progressBar.setX(getX());
      this.progressBar.setY(getY());
      this.progressBar.setWidth(64f);
      this.progressBar.setHeight(12f);

      this.progressBar.draw(batch, parentAlpha);

    }

  }

}
