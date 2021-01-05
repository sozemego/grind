package com.soze.grind.core.game.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.soze.grind.core.game.resource.ResourceEnum;
import com.soze.grind.core.game.storage.ResourceStorage;
import com.soze.grind.core.game.ui.factory.UIElementFactory;

/**
 * Shows resources in a given ResourceStorage object.
 */
public class ResourceStorageTable extends Table {

	private final UIElementFactory uiElementFactory;

	private final ResourceStorage storage;

	public ResourceStorageTable(
			ResourceStorage storage,
			UIElementFactory uiElementFactory
	) {
		this.storage = storage;
		this.uiElementFactory = uiElementFactory;
	}

	@Override
	public void act(float delta) {
		super.act(delta);

		update();
	}

	/**
	 * Creates the UI element.
	 */
	private void update() {
		clearChildren();

		Label titleLabel = uiElementFactory.createTextLabel();
		titleLabel.setText("Storage (" + storage.capacityFilled() + "/" + storage.maxCapacity(ResourceEnum.WOOD) + ")");

		add(titleLabel).row();

		Table resourcesTable = new Table();

		int resourceTables = 0;

		for (ResourceEnum value : ResourceEnum.values()) {
			int count = storage.count(value);
			if (count > 0) {
				Cell cell = resourcesTable.add(createResourceTable(value, count)).width(120f);

				if (++resourceTables % 3 == 0) {
					cell.row();
				}
			}
		}

		resourcesTable.left().top();

		add(resourcesTable).width(360f).minHeight(240f).row();
	}

	private Table createResourceTable(ResourceEnum resource, int amount) {
		Table rootTable = new Table();

		Image image = uiElementFactory.createImage("resource/" + resource.getName().toLowerCase() + ".png");

		rootTable.add(image).width(60f).height(60f);

		Label label = uiElementFactory.createTextLabel();
		label.setText("" + amount);

		rootTable.add(label);

		return rootTable;
	}
}
