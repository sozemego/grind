package com.soze.grind.core.game.ecs.component;

import com.artemis.Component;

public class PositionComponent extends Component {

	private float x;
	private float y;
	private float width;
	private float height;

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public void setPosition(float x, float y) {
		setX(x);
		setY(y);
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public void setSize(float width, float height) {
		setWidth(width);
		setHeight(height);
	}
}
