package com.soze.grind.core.game.storage;

import com.soze.grind.core.game.resource.ResourceEnum;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BinaryOperator;

/**
 * A resource storage that has one total capacity.
 */
public class TotalCapacityResourceStorage implements ResourceStorage {

	private final Map<ResourceEnum, Integer> resources = new HashMap<>();
	private int capacity;

	public TotalCapacityResourceStorage(int capacity) {
		this.capacity = capacity;
	}

	@Override
	public int addResource(ResourceEnum resource, int amount) {
		int actualAmount = Math.min(this.maxCapacity(resource), amount);

		this.resources.compute(resource, (res, count) -> {
			if (count == null) {
				return actualAmount;
			}

			return count + actualAmount;
		});

		return actualAmount;
	}

	@Override
	public int removeResource(ResourceEnum resource, int amount) {
		int actualAmount = Math.min(this.count(resource), amount);

		this.resources.compute(resource, (res, count) -> {
			if (count == null) {
				return 0;
			}

			return count - actualAmount;
		});

		return actualAmount;
	}

	@Override
	public int count(ResourceEnum resource) {
		return resources.getOrDefault(resource, 0);
	}

	@Override
	public int capacityFilled() {
		return resources.values().stream().reduce(0, Integer::sum);
	}

	@Override
	public int capacity(ResourceEnum resource) {
		return this.capacity - this.getTakenCapacity();
	}

	@Override
	public int maxCapacity(ResourceEnum resource) {
		return capacity;
	}

	@Override
	public Map<ResourceEnum, Integer> getResources() {
		return resources;
	}

	/**
	 * Returns total capacity taken by all resources.
	 */
	private int getTakenCapacity() {
		return resources.values().stream().reduce(0, Integer::sum);
	}
}
