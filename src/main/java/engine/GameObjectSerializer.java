package engine;

import components.Component;
import com.google.gson.*;
import components.SpriteSheetList;
import renderer.Sprite;
import renderer.Transform;
import util.AssetPool;

import java.lang.reflect.Type;

public class GameObjectSerializer implements  JsonDeserializer<GameObject> {


    @Override
    public GameObject deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        // notice - must initialize attributes to serialize - outside constructor
        JsonObject jsonObject = json.getAsJsonObject();
        String name = jsonObject.get("name").getAsString();
        JsonArray components = jsonObject.getAsJsonArray("components");
        Transform transform = context.deserialize(jsonObject.get("transform"),Transform.class);
        Sprite sprite = context.deserialize(jsonObject.get("sprite"),Sprite.class);
        int zIndex = context.deserialize(jsonObject.get("zIndex"),int.class);
        int type = context.deserialize(jsonObject.get("type"),int.class);


        GameObject ob1 = new GameObject(name, sprite,transform,zIndex);
        ob1.setType(type);
        for (JsonElement e : components){
            Component c = context.deserialize(e, Component.class);
            if(c.getClass().isAssignableFrom(SpriteSheetList.class)){
                ((SpriteSheetList)c).spriteSheets.replaceAll(spriteSheet -> AssetPool.getSpriteSheet(spriteSheet.getName()));
            }
            ob1.addComponent(c);
        }
        if (sprite != null && sprite.getTexture() != null){
            sprite.setTexture(AssetPool.getTexture(sprite.getTexture().getFilepath()));
        }
        return ob1;
    }
}
