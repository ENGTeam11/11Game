package com.mygdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import java.util.HashMap;
import java.util.Map;

public class GameMap {
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer renderer;
    private float timeSinceLastToggle = 0;
    private Map<String, String[]> doorToRoofMap = new HashMap<>();
    private final float proximityThreshold = 5f; 

    public GameMap(String mapPath) {
        tiledMap = new TmxMapLoader().load(mapPath);
        renderer = new OrthogonalTiledMapRenderer(tiledMap, 1 / 0.18f); 

        // Defining the associations between door layers and roof layers
        doorToRoofMap.put("house_door", new String[]{"roof_house_1", "roof_house_2"});
        doorToRoofMap.put("res_door", new String[]{"roof_restaurant1", "roof_restaurant2"});
        doorToRoofMap.put("cs_door", new String[]{"roof_cs_1", "roof_cs_2"});
    }

    public void render(OrthographicCamera camera) {
        renderer.setView(camera);
        renderer.render();
    }

    public void update(float delta) {
        timeSinceLastToggle += delta;
        if (timeSinceLastToggle >= 1.0f) {
            toggleLayerVisibility("water2");
            timeSinceLastToggle -= 1.0f; // Reset the timer, subtracting instead of setting to 0 to avoid any overflow
        }
    }
    public void toggleRoofVisibilityBasedOnPlayerPosition(Vector2 playerTilePosition) {
        for (Map.Entry<String, String[]> entry : doorToRoofMap.entrySet()) {
            String doorLayerName = entry.getKey();
            String[] roofLayerNames = entry.getValue();

            TiledMapTileLayer doorLayer = (TiledMapTileLayer) tiledMap.getLayers().get(doorLayerName);
            if (doorLayer == null) continue;


            int proximity = Math.max(1, (int)proximityThreshold);
            for (int x = (int) playerTilePosition.x - proximity; x <= (int) playerTilePosition.x + proximity; x++) {
                for (int y = (int) playerTilePosition.y - proximity; y <= (int) playerTilePosition.y + proximity; y++) {
                    TiledMapTileLayer.Cell cell = doorLayer.getCell(x, y);
                    if (cell != null) {
                        for (String roofLayerName : roofLayerNames) {
                            toggleLayerVisibility(roofLayerName);
                        }
                        return;
                    }
                }
            }
        }
    }
    public Array<Rectangle> getCollisionRectangles(String objectLayerName) {
        Array<Rectangle> rectangles = new Array<>();
        MapLayer collision_layer = tiledMap.getLayers().get(objectLayerName);
        if (collision_layer == null) return rectangles;
        for (MapObject object : collision_layer.getObjects()) {
            if (object instanceof RectangleMapObject) {
                rectangles.add(((RectangleMapObject) object).getRectangle());
            }
        }
        return rectangles;
    }
    public void toggleLayerVisibility(String layerName) {
        MapLayer layer = tiledMap.getLayers().get(layerName);
        if (layer != null) {
            layer.setVisible(!layer.isVisible());
        }
    }
    public void dispose() {
        tiledMap.dispose();
        renderer.dispose();
    }
}
