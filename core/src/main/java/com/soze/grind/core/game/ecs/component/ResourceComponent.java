package com.soze.grind.core.game.ecs.component;

import com.artemis.Component;
import com.soze.grind.core.game.resource.ResourceEnum;

public class ResourceComponent extends Component {

	private ResourceEnum resourceEnum;

	public ResourceEnum getResourceEnum() {
		return resourceEnum;
	}

	public void setResourceEnum(ResourceEnum resourceEnum) {
		this.resourceEnum = resourceEnum;
	}
}
