package com.soze.grind.core.game.ecs.component;

import com.artemis.Component;
import com.soze.grind.core.game.storage.ResourceStorage;

public class ResourceStorageComponent extends Component {

	private ResourceStorage resourceStorage;

	public ResourceStorageComponent() {

	}

	public ResourceStorageComponent(ResourceStorage resourceStorage) {
		this.resourceStorage = resourceStorage;
	}

	public ResourceStorage getResourceStorage() {
		return resourceStorage;
	}

	public void setResourceStorage(ResourceStorage resourceStorage) {
		this.resourceStorage = resourceStorage;
	}
}
