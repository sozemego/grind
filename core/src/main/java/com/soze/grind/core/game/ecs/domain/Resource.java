package com.soze.grind.core.game.ecs.domain;

import com.artemis.Entity;
import com.soze.grind.core.game.ecs.component.ResourceComponent;
import com.soze.grind.core.game.resource.ResourceEnum;

public class Resource extends AbstractEntity {

	public Resource(Entity entity) {
		super(entity);
	}

	public ResourceEnum getResourceEnum() {
		return getComponent(ResourceComponent.class).getResourceEnum();
	}
}
