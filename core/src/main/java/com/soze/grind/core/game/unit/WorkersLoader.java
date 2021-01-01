package com.soze.grind.core.game.unit;

import com.soze.grind.core.game.assets.AssetService;
import com.soze.grind.core.game.storage.TotalCapacityResourceStorage;
import com.soze.grind.core.game.ui.factory.UIElementFactory;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** A service which loads heroes. */
//@Service
public class WorkersLoader {

  private static final Logger LOG = LogManager.getLogger(WorkersLoader.class);

  private final AssetService assetService;
  private final UIElementFactory uiElementFactory;

  private final List<Worker> workers = new ArrayList<>();

  @Autowired
  public WorkersLoader(AssetService assetService, UIElementFactory uiElementFactory) {
    this.assetService = assetService;
    this.uiElementFactory = uiElementFactory;
  }

  @PostConstruct
  public void setup() {
    LOG.info("Loading workers");

    Worker worker =
        new Worker(
            "Worker",
            this.assetService.getTexture("medievalUnit_12.png"),
            new TotalCapacityResourceStorage(2),
            uiElementFactory
        );

    worker.setPosition(48 * 64, 50 * 64);
    worker.setWidth(64);
    worker.setHeight(64);

    this.workers.add(worker);

    Worker worker1 =
        new Worker(
            "Worker",
            this.assetService.getTexture("medievalUnit_12.png"),
            new TotalCapacityResourceStorage(3),
            uiElementFactory
        );

    worker1.setPosition(47 * 64, 50 * 64);
    worker1.setWidth(64);
    worker1.setHeight(64);

    this.workers.add(worker1);
  }

  public List<Worker> getWorkers() {
    return this.workers;
  }
}
