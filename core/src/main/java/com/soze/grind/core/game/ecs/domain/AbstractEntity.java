package com.soze.grind.core.game.ecs.domain;

import com.artemis.Component;
import com.artemis.Entity;
import com.badlogic.gdx.math.Vector2;
import com.soze.grind.core.game.ecs.component.HealthComponent;
import com.soze.grind.core.game.ecs.component.NameComponent;
import com.soze.grind.core.game.ecs.component.PositionComponent;
import com.soze.grind.core.game.ecs.component.ResourceStorageComponent;
import com.soze.grind.core.game.storage.ResourceStorage;
import java.util.Objects;

/**
 * A parent of all classes that encapsulate some <code>Entity</code>.
 * Contains some useful methods, but more specialised ones are implemented in the child classes.
 *
 * The main purpose (so far) is to be used in UI elements (so it does not deal with components directly).
 */
public abstract class AbstractEntity {

	final Entity entity;

	public AbstractEntity(Entity entity) {
		this.entity = entity;
	}

	/**
	 * Returns the name of the entity.
	 */
	public String getName() {
		NameComponent nameComponent = getComponent(NameComponent.class);
		return Objects.nonNull(nameComponent) ? nameComponent.getName() : "NO_NAME";
	}

	/**
	 * Returns current health.
	 */
	public int getHealth() {
		HealthComponent healthComponent = getComponent(HealthComponent.class);
		return Objects.nonNull(healthComponent) ? healthComponent.getHealth() : -1;
	}

	/**
	 * Returns max health of the entity.
	 */
	public int getMaxHealth() {
		HealthComponent healthComponent = getComponent(HealthComponent.class);
		return Objects.nonNull(healthComponent) ? healthComponent.getMaxHealth() : -1;
	}

	/**
	 * Returns ResourceStorage or null if there is no storage.
	 */
	public ResourceStorage getResourceStorage() {
		ResourceStorageComponent resourceStorageComponent = getComponent(ResourceStorageComponent.class);
		return Objects.nonNull(resourceStorageComponent) ? resourceStorageComponent.getResourceStorage() : null;
	}

	/**
	 * Returns position of the entity or null if there is no position.
	 */
	public Vector2 getPosition() {
		PositionComponent positionComponent = getComponent(PositionComponent.class);

		if (Objects.nonNull(positionComponent)) {
			return new Vector2(positionComponent.getX(), positionComponent.getY());
		}

		return null;
	}

	/**
	 * If the entity has a PositionComponent, sets the position. If not, does nothing.
	 */
	public void setPosition(float x, float y) {
		PositionComponent positionComponent = getComponent(PositionComponent.class);

		if (Objects.nonNull(positionComponent)) {
			positionComponent.setPosition(x, y);
		}

	}

	public <T extends Component> T getComponent(Class<T> type) {
		return entity.getComponent(type);
	}
}
