package com.soze.grind.core.game.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.soze.grind.core.game.Dungeon;
import com.soze.grind.core.game.assets.AssetService;
import com.soze.grind.core.game.ui.factory.UIElementFactory;
import java.util.List;

/** Represents the table with a selection for the dungeons that a Hero can go into. */
public class DungeonSelectionTable extends Table {

  private final AssetService assetService;
  private final UIElementFactory uiElementFactory;

  private final List<Dungeon> dungeons;

  public DungeonSelectionTable(
      AssetService assetService, UIElementFactory uiElementFactory, List<Dungeon> dungeons) {
    this.assetService = assetService;
    this.uiElementFactory = uiElementFactory;
    this.dungeons = dungeons;

    setBackground(assetService.getNinePatchDrawable("grey_panel.png"));

    top();

    Label title = uiElementFactory.createTextLabel();
    title.setText("Dungeons");

    add(title).left().row();

    Table dungeonsTable = new Table();

    for (Dungeon dungeon : dungeons) {
      dungeonsTable.add(createDungeonElement(dungeon)).width(96f).height(96f).pad(5f);
    }

    add(dungeonsTable).expandY();
  }

  private Table createDungeonElement(Dungeon dungeon) {
    Table table = new Table();

    Image image = uiElementFactory.createImage(dungeon.getTexture());

    table.add(image);

    NinePatchDrawable ninePatchDrawable =
        assetService
            .getNinePatchDrawable("red_button00_hollow.png")
            .tint(new Color(1, 1, 1, 0.25f));

    table.setBackground(ninePatchDrawable);

    table.setTouchable(Touchable.enabled);

    table.addListener(
        new ClickListener() {
          @Override
          public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
            NinePatchDrawable background =
                assetService
                    .getNinePatchDrawable("red_button00_hollow.png")
                    .tint(new Color(1, 1, 1, 1f));

            table.setBackground(background);
          }

          @Override
          public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
            NinePatchDrawable background =
                assetService
                    .getNinePatchDrawable("red_button00_hollow.png")
                    .tint(new Color(1, 1, 1, 0.25f));

            table.setBackground(background);
          }

          @Override
          public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            super.touchUp(event, x, y, pointer, button);
          }
        });

    return table;
  }

  @Override
  public Actor hit(float x, float y, boolean touchable) {
    return super.hit(x, y, touchable);
  }
}
