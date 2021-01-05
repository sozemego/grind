package com.soze.grind.core.game.ecs.domain;

import com.artemis.Entity;
import com.soze.grind.core.game.ecs.component.HealthComponent;
import com.soze.grind.core.game.ecs.component.NameComponent;

/**
 * Static methods for interacting with <code>Entity</code> objects that are Heroes (they have a <code>HeroComponent</code>).
 *
 */
public class Hero {

	/**
	 * Returns the name of the Hero.
	 *
	 * @param entity entity
	 */
	public static String getName(Entity entity) {
		NameComponent nameComponent = entity.getComponent(NameComponent.class);
		return nameComponent.getName();
	}

	/**
	 * Returns current health.
	 */
	public static int getHealth(Entity entity) {
		HealthComponent healthComponent = entity.getComponent(HealthComponent.class);
		return healthComponent.getHealth();
	}

	/**
	 * Returns max health of the entity.
	 */
	public static int getMaxHealth(Entity entity) {
		HealthComponent healthComponent = entity.getComponent(HealthComponent.class);
		return healthComponent.getMaxHealth();
	}

}
