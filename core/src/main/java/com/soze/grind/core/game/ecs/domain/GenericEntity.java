package com.soze.grind.core.game.ecs.domain;

import com.artemis.Entity;

/**
 * Represents a Generic entity, where it does not matter what the type of the entity really is,
 * but we want access to AbstractEntity methods.
 */
public class GenericEntity extends AbstractEntity {

	public GenericEntity(Entity entity) {
		super(entity);
	}
}
