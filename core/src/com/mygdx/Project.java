package com.mygdx;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.pages.MainMenu;

public class Project extends Game{
	public SpriteBatch batch;
	public ShapeRenderer shape;
	public BitmapFont font;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		shape = new ShapeRenderer();
		font = new BitmapFont();
		this.setScreen(new MainMenu(this));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
