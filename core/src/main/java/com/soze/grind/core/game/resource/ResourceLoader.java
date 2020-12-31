package com.soze.grind.core.game.resource;

import com.soze.grind.core.game.assets.AssetService;
import com.soze.grind.core.game.storage.ResourceStorage;
import com.soze.grind.core.game.storage.TotalCapacityResourceStorage;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResourceLoader {

  private static final Logger LOG = LogManager.getLogger(ResourceLoader.class);

  private final AssetService assetService;

  private final List<Resource> resources = new ArrayList<>();

  @Autowired
  public ResourceLoader(AssetService assetService) {
    this.assetService = assetService;
  }

  @PostConstruct
  public void setup() {
    LOG.info("Loading resources");

    ResourceStorage resourceStorage1 = new TotalCapacityResourceStorage(50);
    resourceStorage1.addResource(ResourceEnum.WOOD, 50);

    Resource tree1 =
        new Resource(
            this.assetService.getTexture("medievalEnvironment_03.png"),
            ResourceEnum.WOOD,
            resourceStorage1
        );

    tree1.setPosition(64 * 25, 64 * 35);
    tree1.setSize(64, 64);

    ResourceStorage resourceStorage2 = new TotalCapacityResourceStorage(70);
    resourceStorage2.addResource(ResourceEnum.WOOD, 70);

    Resource tree2 =
        new Resource(
            this.assetService.getTexture("medievalEnvironment_03.png"),
            ResourceEnum.WOOD,
            resourceStorage2
        );

    tree2.setPosition(64 * 35, 64 * 35);
    tree2.setSize(64, 64);

    ResourceStorage resourceStorage3 = new TotalCapacityResourceStorage(25);
    resourceStorage3.addResource(ResourceEnum.WOOD, 20);

    Resource tree3 =
        new Resource(
            this.assetService.getTexture("medievalEnvironment_03.png"),
            ResourceEnum.WOOD,
            resourceStorage3
        );

    tree3.setPosition(64 * 45, 64 * 52);
    tree3.setSize(64, 64);

    this.resources.add(tree1);
    this.resources.add(tree2);
    this.resources.add(tree3);

    ResourceStorage resourceStorage4 = new TotalCapacityResourceStorage(65);
    resourceStorage4.addResource(ResourceEnum.STONE, 65);

    Resource rock1 =
        new Resource(
            this.assetService.getTexture("medievalEnvironment_09.png"),
            ResourceEnum.STONE,
            resourceStorage4
        );

    rock1.setPosition(64 * 56, 64 * 50);
    rock1.setSize(64, 64);

    ResourceStorage resourceStorage5 = new TotalCapacityResourceStorage(45);
    resourceStorage5.addResource(ResourceEnum.STONE, 45);

    Resource rock2 =
        new Resource(
            this.assetService.getTexture("medievalEnvironment_09.png"),
            ResourceEnum.STONE,
            resourceStorage5
        );

    rock2.setPosition(64 * 52, 64 * 52);
    rock2.setSize(64, 64);

    ResourceStorage resourceStorage6 = new TotalCapacityResourceStorage(50);
    resourceStorage6.addResource(ResourceEnum.STONE, 50);

    Resource rock3 =
        new Resource(
            this.assetService.getTexture("medievalEnvironment_09.png"),
            ResourceEnum.STONE,
            resourceStorage6
        );

    rock3.setPosition(64 * 51, 64 * 45);
    rock3.setSize(64, 64);

    this.resources.add(rock1);
    this.resources.add(rock2);
    this.resources.add(rock3);
  }

  public List<Resource> getResources() {
    return this.resources;
  }
}
