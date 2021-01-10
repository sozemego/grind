package com.soze.grind.core.game.event;

import com.soze.grind.core.game.Dungeon;

/**
 * Event fired when the user selects one of the dungeons in the dungeon selection table.
 * Or clicks outside of the DungeonManagementWindow and deselects it.
 */
public class DungeonSelectedEvent {

	private final Dungeon dungeon;

	public DungeonSelectedEvent(Dungeon dungeon) {
		this.dungeon = dungeon;
	}

	public Dungeon getDungeon() {
		return dungeon;
	}
}
