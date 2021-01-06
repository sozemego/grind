package com.soze.grind.core.game.ecs.domain;

import com.artemis.Entity;
import com.soze.grind.core.game.ecs.component.HealthComponent;
import com.soze.grind.core.game.ecs.component.NameComponent;

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
		NameComponent nameComponent = entity.getComponent(NameComponent.class);
		return nameComponent.getName();
	}

	/**
	 * Returns current health.
	 */
	public int getHealth() {
		HealthComponent healthComponent = entity.getComponent(HealthComponent.class);
		return healthComponent.getHealth();
	}

	/**
	 * Returns max health of the entity.
	 */
	public int getMaxHealth() {
		HealthComponent healthComponent = entity.getComponent(HealthComponent.class);
		return healthComponent.getMaxHealth();
	}

}
