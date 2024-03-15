package com.mygdx;

import com.badlogic.gdx.maps.MapGroupLayer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;


public class GameMap {
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer renderer;
    private float timeSinceLastToggle = 0;
    private float scale = 1 / 0.18f;

    public GameMap(String mapPath) {
        tiledMap = new TmxMapLoader().load(mapPath);
        renderer = new OrthogonalTiledMapRenderer(tiledMap, scale); // Adjust the unit scale to match your game
    }

    public void render(OrthographicCamera camera) {
        renderer.setView(camera);
        renderer.render();
    }

    public void update(float delta) {
        timeSinceLastToggle += delta;
        if (timeSinceLastToggle >= .75f) {
            toggleLayerVisibility("terrain", "water2");
            timeSinceLastToggle -= 0.75f; // Reset the timer, subtracting instead of setting to 0 to avoid any overflow
        }
    }

    public void toggleLayerVisibility(String groupName, String layerName) {
        MapLayer groupLayer = tiledMap.getLayers().get(groupName);

        if (groupLayer instanceof MapGroupLayer) {
            MapLayer layer = ((MapGroupLayer) groupLayer).getLayers().get(layerName);
            if (layer != null) {
                layer.setVisible(!layer.isVisible());
            }
        }
    }
    public boolean canPlayerMove(Vector2 newPosition, float width, float height) {
        Rectangle futureBounds = new Rectangle(newPosition.x, newPosition.y, width, height);
        MapLayer collisionObjectLayer = tiledMap.getLayers().get("collision_layer");
        if (collisionObjectLayer != null) {
            MapObjects objects = collisionObjectLayer.getObjects();

            for (MapObject object : objects) {
                if (object instanceof RectangleMapObject) {
                    Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
                    Rectangle scaledRectangle = new Rectangle(rectangle.x * scale, rectangle.y * scale, rectangle.width * scale, rectangle.height * scale);
                    if (Intersector.overlaps(scaledRectangle, futureBounds)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public Vector2 getSpawnPoint() {
        MapLayer layer = tiledMap.getLayers().get("spawn_point");
        if (layer != null) {
            RectangleMapObject object = layer.getObjects().getByType(RectangleMapObject.class).first();
            if (object != null) {
                Rectangle spawn_rectangle = object.getRectangle();
                return new Vector2(spawn_rectangle.x * scale, spawn_rectangle.y * scale);
            }
        }
        return new Vector2(1000, 800);
    }

    public void dispose() {
        tiledMap.dispose();
        renderer.dispose();

    }
}
