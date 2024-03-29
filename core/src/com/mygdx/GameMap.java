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

/**
 * GameMap is a class designed to manage the game's map using Tiled maps.
 * It facilitates rendering the map, handling layer visibility, player movement validations,
 * and providing spawn points based on the Tiled map properties.
 */
public class GameMap {
    private TiledMap tiledMap; //Holds the tiled map structure
    private OrthogonalTiledMapRenderer renderer; //Renderer fot the tiled map
    private float timeSinceLastToggle = 0; //Timer for toggling layer "water2" visibility
    private float scale = 1 / 0.18f; //Map scale
    private boolean inHouse, inCS, inRestaurant; // checks for whether the roofs have already been toggled 

    /**
     * Constructor that initializes the GameMap with a specified map path.
     * @param mapPath The file path to the Tiled map file (.tmx).
     */
    public GameMap(String mapPath) {
        tiledMap = new TmxMapLoader().load(mapPath);
        renderer = new OrthogonalTiledMapRenderer(tiledMap, scale);
        inHouse = false;
        inRestaurant = false;
        inCS = false;
    }

    /**
     * Renders the map onto the provided camera's view.
     * @param camera The camera through which the map is viewed.
     */
    public void render(OrthographicCamera camera) {
        renderer.setView(camera);
        renderer.render();
    }

    /**
     * Updates the map state
     * including layer visibility toggling based on a timer for water animation
     * @param delta The time elapsed since the last frame in seconds.
     */
    public void update(float delta) {
        timeSinceLastToggle += delta;
        if (timeSinceLastToggle >= .75f) {
            toggleLayerVisibility("terrain", "water2");
            timeSinceLastToggle -= 0.75f; // Reset the timer, subtracting instead of setting to 0 to avoid any overflow
        }
    }

    /**
     * Toggles the visibility of a specific layer within a group layer.
     * @param groupName The name of the group layer containing the target layer.
     * @param layerName The name of the layer whose visibility is to be toggled.
     */
    public void toggleLayerVisibility(String groupName, String layerName) {
        MapLayer groupLayer = tiledMap.getLayers().get(groupName);

        if (groupLayer instanceof MapGroupLayer) {
            MapLayer layer = ((MapGroupLayer) groupLayer).getLayers().get(layerName);
            if (layer != null) {
                layer.setVisible(!layer.isVisible());
            }
        }
    }

    /**
     * Determines if a player can move to a specified position based on collision layers.
     * @param Position The proposed position of the player.
     * @param width The width of the player's bounds.
     * @param height The height of the player's bounds.
     * @param layerName the name of the object layer in the map
     * @return true if the player is within the boundary of the object layer; false otherwise.
     */
    public boolean isInArea(Vector2 Position, float width, float height, String layerName) {
        Rectangle playerBounds = new Rectangle(Position.x, Position.y, width, height);
        MapLayer ObjectLayer = tiledMap.getLayers().get(layerName);
        if (ObjectLayer != null) {
            MapObjects objects = ObjectLayer.getObjects();

            for (MapObject object : objects) {
                if (object instanceof RectangleMapObject) {
                    Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
                    Rectangle scaledRectangle = new Rectangle(rectangle.x * scale, rectangle.y * scale, rectangle.width * scale, rectangle.height * scale);
                    if (Intersector.overlaps(scaledRectangle, playerBounds)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Retrieves the spawn point from the map based on a defined layer.
     * @return A Vector2 representing the spawn point coordinates. Returns a default value if not found.
     */
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

    /**
     * Checks whether the player is within any of the buildings and toggles the visibility of the roof of any building the player is in 
     * @param Position position of the player
     * @param width the width of the player sprite
     * @param height the height of the players sprite
     */
    public void insideCheck(Vector2 Position, float width, float height){
        //house
        if((isInArea(Position, width, height, "house_layer") && !inHouse) || (!isInArea(Position, width, height, "house_layer") && inHouse)){
            inHouse = !inHouse;
            toggleLayerVisibility("house", "house_roof1");
            toggleLayerVisibility("house", "house_roof2");
            toggleLayerVisibility("house", "house_outside");
            toggleLayerVisibility("house", "house_door_open");           
        }
        
        //Restaurant
        if((isInArea(Position, width, height, "restaurant_layer") && !inRestaurant) || (!isInArea(Position, width, height, "restaurant_layer") && inRestaurant)){
            inRestaurant = !inRestaurant;
            toggleLayerVisibility("restaurant", "res_roof1");
            toggleLayerVisibility("restaurant", "res_roof2");
            toggleLayerVisibility("restaurant", "res_outdoor");
            toggleLayerVisibility("restaurant", "res_door_open");
        }

        //computer science building
        if((isInArea(Position, width, height, "cs_layer") && !inCS) || (!isInArea(Position, width, height, "cs_layer") && inCS)){
            inCS = !inCS;
            toggleLayerVisibility("cs_building", "cs_roof1");
            toggleLayerVisibility("cs_building", "cs_roof2");
        }
    }

    /**
     * Disposes of the resources used by the GameMap, the TiledMap and its renderer.
     */
    public void dispose() {
        tiledMap.dispose();
        renderer.dispose();

    }

    
}