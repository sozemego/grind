package com.soze.grind.core.game.building;

import com.soze.grind.core.game.storage.ResourceStorage;
import java.util.Optional;

/**
 * Interface which marks all buildings.
 */
public interface Building {

	/**
	 * Returns the name of the building.
	 */
	String getName();

	/**
	 * Returns the resource storage if it exists.
	 */
	Optional<ResourceStorage> getResourceStorage();

}
