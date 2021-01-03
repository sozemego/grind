package com.soze.grind.core.game.storage;

import com.soze.grind.core.game.resource.ResourceEnum;
import java.util.Map;

/**
 * Interface for any type of resource storage for buildings or units. Storage can be per resource
 * (e.g. 100 of each resource) or it can has total capacity (e.g. can carry up to 5 of any
 * resource).
 */
public interface ResourceStorage {

  /**
   * Adds up to amount of given resource to the storage. Only adds up to the maximum capacity for
   * given resource.
   *
   * <p>Returns the actual amount of resource added.
   */
  int addResource(ResourceEnum resource, int amount);

  /**
   * Removes up to a given amount of resource.
   *
   * <p>Returns the actual amount of resource removed.
   *
   * @param resource
   * @param amount
   */
  int removeResource(ResourceEnum resource, int amount);

  /**
   * Returns how many of given resource this storage contains.
   *
   * @param resource
   */
  int count(ResourceEnum resource);

  /**
   * Returns the remaining capacity for given resource.
   *
   * @param resource
   */
  int capacity(ResourceEnum resource);

  /**
   * Returns the max capacity for a given resource.
   *
   * @param resource
   */
  int maxCapacity(ResourceEnum resource);

  /** Gets all stored resources. */
  Map<ResourceEnum, Integer> getResources();
}
