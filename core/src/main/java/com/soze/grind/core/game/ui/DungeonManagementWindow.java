package com.soze.grind.core.game.ui;

import com.artemis.Entity;
import com.artemis.EntitySubscription;
import com.artemis.World;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.google.common.eventbus.Subscribe;
import com.soze.grind.core.game.Dungeon;
import com.soze.grind.core.game.ecs.Aspects;
import com.soze.grind.core.game.ecs.domain.Hero;
import com.soze.grind.core.game.event.DungeonSelectedEvent;
import com.soze.grind.core.game.ui.factory.UIElementFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** Window in which the player sends heroes to the dungeon. */
public class DungeonManagementWindow extends Window {

  private final UIElementFactory uiElementFactory;
  private final World world;

  private final Table rootTable = new Table();

  private Dungeon dungeon;

  public DungeonManagementWindow(
      String title, WindowStyle style, UIElementFactory uiElementFactory, World world) {
    super(title, style);

    this.uiElementFactory = uiElementFactory;
    this.world = world;

    setModal(false);
    setMovable(true);

    getTitleTable().padTop(25f);

//    debugAll();

    add(rootTable).expandX().expandY().top().left().padTop(45f);

    addListener(
        new ClickListener() {
          @Override
          public void clicked(InputEvent event, float x, float y) {}
        });
  }

  @Subscribe
  public void handleDungeonSelectedEvent(DungeonSelectedEvent dungeonSelectedEvent) {
    Dungeon selectedDungeon = dungeonSelectedEvent.getDungeon();
    // 4 options

    // 1 nothing selected previously, nothing selected now
    if (Objects.isNull(dungeon) && Objects.isNull(selectedDungeon)) {
      clearCurrentTable();
    }

    // 2 something selected previously, nothing selected now
    if (Objects.nonNull(dungeon) && Objects.isNull(selectedDungeon)) {
      clearCurrentTable();
    }

    // 3 nothing selected previously, now something is selected
    if (Objects.isNull(dungeon) && Objects.nonNull(selectedDungeon)) {
      createSelectedTable(selectedDungeon);
    }

    // 4 something selected previously, now something is selected
    if (Objects.nonNull(dungeon) && Objects.nonNull(selectedDungeon)) {

      if (dungeon != selectedDungeon) {
        clearCurrentTable();
        createSelectedTable(selectedDungeon);
      }
    }
  }

  public Dungeon getDungeon() {
    return dungeon;
  }

  public void setDungeon(Dungeon dungeon) {
    this.dungeon = dungeon;
  }

  @Override
  public void act(float delta) {
    super.act(delta);

    resize();

    setVisible(Objects.nonNull(dungeon));
  }

  /** Resizes this element based on window size. */
  private void resize() {
    float minWidth = 256f;
    float minHeight = 256f;

    Group parent = getParent();
    if (parent instanceof Table) {
      Table parentTable = (Table) parent;

      Cell cell = parentTable.getCell(this);

      cell.width(Math.max(minWidth, Gdx.graphics.getWidth() * 0.55f));
      cell.height(Math.max(minHeight, Gdx.graphics.getHeight() * 0.55f));
    }
  }

  private void clearCurrentTable() {
    setDungeon(null);
    rootTable.clearChildren();
  }

  private void createSelectedTable(Dungeon dungeon) {
    setDungeon(dungeon);

    Table listOfHeroes = new Table();

    Label title = uiElementFactory.createTextLabel();
    title.setText("Heroes");

    listOfHeroes.add(title).row();

    listOfHeroes.add(uiElementFactory.createDivider()).row();

    List<Hero> heroes = getHeroes();

    for (Hero hero : heroes) {
      Label heroName = uiElementFactory.createTextLabel();
      heroName.setText(hero.getName());

      listOfHeroes.add(heroName).row();
    }

    rootTable.add(listOfHeroes);
  }

  private List<Hero> getHeroes() {
    EntitySubscription entitySubscription = world.getAspectSubscriptionManager().get(Aspects.HERO);

    IntBag entityIds = entitySubscription.getEntities();
    int[] ids = entityIds.getData();

    List<Hero> heroes = new ArrayList<>();

    for (int i = 0; i < entityIds.size(); i++) {
      int id = ids[i];

      Entity hero = world.getEntity(id);

      heroes.add(new Hero(hero));
    }

    return heroes;
  }

}
