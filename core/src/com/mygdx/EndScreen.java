package com.mygdx;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class EndScreen extends ScreenAdapter {
    private Game game;
    private Skin skin;
    private Stage stage;
    private LabelStyle menuTextStyle;
    private BitmapFont font;

    public EndScreen(Game game, Skin skin){
        this.game = game;
        this.skin = skin;
        font = new BitmapFont();
    }

    @Override
    public void show(){
        stage = new Stage(new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        Gdx.input.setInputProcessor(stage);

        menuTextStyle = new LabelStyle(font, Color.WHITE);
        Table table = new Table();
        table.setFillParent(true);
        table.center();

        Label message = new Label("Game Complete", menuTextStyle);
        TextButton menuButton = new TextButton("Main menu", skin);
        TextButton exitButton = new TextButton("Exit game", skin);
        table.add(message);
        table.row();
        table.add(menuButton);
        table.row();
        table.add(exitButton);

        float buttonWidth = Gdx.graphics.getWidth() * 0.4f;
        float buttonHeight = Gdx.graphics.getHeight() * 0.1f;

        message.setSize(buttonWidth, buttonHeight);
        menuButton.setSize(buttonWidth, buttonHeight);
        exitButton.setSize(buttonWidth, buttonHeight);

        menuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MenuScreen(game, skin));
            }
        });
        
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        stage.addActor(table);
    }

    @Override
    public void render(float delta){
        Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void dispose(){
        stage.dispose();
    }
}
