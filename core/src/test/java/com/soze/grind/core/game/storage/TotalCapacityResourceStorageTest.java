package com.soze.grind.core.game.storage;

import static org.junit.jupiter.api.Assertions.*;

import com.soze.grind.core.game.resource.ResourceEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class TotalCapacityResourceStorageTest {

  @ParameterizedTest
  @DisplayName("addResource: enough space, should add given resource in full amount")
  @ValueSource(ints = {1, 2, 4, 8, 10, 15, 25, 50, 125})
  void addResourceEnoughSpace(int amount) {
    TotalCapacityResourceStorage storage = new TotalCapacityResourceStorage(500);

    int amountAdded = storage.addResource(ResourceEnum.STONE, amount);

    assertEquals(amount, storage.count(ResourceEnum.STONE));
    assertEquals(amount, amountAdded);
  }

  @ParameterizedTest
  @DisplayName("addResource: not enough space, should add given resource up to capacity")
  @ValueSource(ints = {10, 25, 50, 100, 125})
  void addResourceNotEnoughSpace(int amount) {
    TotalCapacityResourceStorage storage = new TotalCapacityResourceStorage(5);

    int amountAdded = storage.addResource(ResourceEnum.STONE, amount);

    assertEquals(5, storage.count(ResourceEnum.STONE));
    assertEquals(5, amountAdded);
  }

  @Test
  @DisplayName("addResource: add same resource multiple times")
  void addResourceMultipleTimes() {
    TotalCapacityResourceStorage storage = new TotalCapacityResourceStorage(5);

    for (int i = 0; i < 25; i++) {
      storage.addResource(ResourceEnum.WOOD, 1);
    }

    assertEquals(25, storage.count(ResourceEnum.WOOD));
  }

  @Test
  @DisplayName("removeResource: remove resource that exists fully")
  void removeResourceExists() {
    TotalCapacityResourceStorage storage = new TotalCapacityResourceStorage(5);
    storage.addResource(ResourceEnum.STONE, 5);

    int amountRemoved = storage.removeResource(ResourceEnum.STONE, 4);

    assertEquals(4, amountRemoved);
    assertEquals(1, storage.count(ResourceEnum.STONE));
  }

  @Test
  @DisplayName("removeResource: remove resource does not exist fully")
  void removeResourceNotFullAmount() {
    TotalCapacityResourceStorage storage = new TotalCapacityResourceStorage(25);
    storage.addResource(ResourceEnum.STONE, 10);

    int amountRemoved = storage.removeResource(ResourceEnum.STONE, 12);

    assertEquals(10, amountRemoved);
    assertEquals(0, storage.count(ResourceEnum.STONE));
  }

  @Test
  @DisplayName("removeResource: remove one resource, other stays")
  void removeResourceOthersExist() {
    TotalCapacityResourceStorage storage = new TotalCapacityResourceStorage(50);
    storage.addResource(ResourceEnum.STONE, 10);
    storage.addResource(ResourceEnum.WOOD, 10);

    int amountRemoved = storage.removeResource(ResourceEnum.STONE, 5);

    assertEquals(5, amountRemoved);
    assertEquals(5, storage.count(ResourceEnum.STONE));
    assertEquals(10, storage.count(ResourceEnum.WOOD));
  }

  @Test
  @DisplayName("count: should return valid count of given resource")
  void count() {
    TotalCapacityResourceStorage storage = new TotalCapacityResourceStorage(50);
    storage.addResource(ResourceEnum.STONE, 20);
    storage.addResource(ResourceEnum.WOOD, 30);

    assertEquals(20, storage.count(ResourceEnum.STONE));
    assertEquals(30, storage.count(ResourceEnum.WOOD));

    storage.removeResource(ResourceEnum.STONE, 50);
    storage.removeResource(ResourceEnum.WOOD, 50);

    assertEquals(0, storage.count(ResourceEnum.STONE));
    assertEquals(0, storage.count(ResourceEnum.WOOD));
  }

  @Test
  @DisplayName("capacity: should return valid capacity for given resource")
  void capacity() {
    TotalCapacityResourceStorage storage = new TotalCapacityResourceStorage(50);

    assertEquals(50, storage.capacity(ResourceEnum.STONE));
    assertEquals(50, storage.capacity(ResourceEnum.WOOD));

    storage.addResource(ResourceEnum.WOOD, 12);

    assertEquals(38, storage.capacity(ResourceEnum.STONE));
    assertEquals(38, storage.capacity(ResourceEnum.WOOD));
  }

  @ParameterizedTest
  @DisplayName("capacityFilled: should return valid values after being filled")
  @ValueSource(ints = {10, 25, 50, 100, 125})
  void capacityFilled(int amount) {
    TotalCapacityResourceStorage storage = new TotalCapacityResourceStorage(5000);

    storage.addResource(ResourceEnum.STONE, amount);

    assertEquals(amount, storage.capacityFilled());
  }

}