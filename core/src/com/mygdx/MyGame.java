package com.mygdx;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class MyGame extends Game {
    private OrthographicCamera camera;
    private Viewport viewport;
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;

    private final float mapWidth = 480f;
    private final float mapHeight = 560f;

    @Override
    public void create() {
        camera = new OrthographicCamera();
        // Adjust the viewport size to your map dimensions
        viewport = new StretchViewport(mapWidth, mapHeight, camera);
        map = new TmxMapLoader().load("maps/map1.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map);
    }

    @Override
    public void render() {
        // Clear the screen and update the camera
        camera.update();
        mapRenderer.setView(camera);
        mapRenderer.render();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        map.dispose();
        mapRenderer.dispose();
    }
}
