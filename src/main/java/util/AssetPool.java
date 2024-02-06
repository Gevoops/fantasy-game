package util;

import renderer.Shader;
import renderer.Texture;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class AssetPool {
    private static Map<String, Shader> shaders = new HashMap<>();
    private static  Map<String, Texture> textures =  new HashMap<>();

    public static  Shader getShader(String name) {
        File file  = new File(name);
        if(AssetPool.shaders.containsKey(file.getAbsolutePath())) {
            return AssetPool.shaders.get(file.getAbsolutePath());
        } else {
            Shader shader = new Shader(name);
            shader.compile();
            AssetPool.shaders.put(file.getAbsolutePath(),shader);
            return shader;
        }
    }

    public static Texture getTexture(String name) {
        File file = new File(name);
        if(AssetPool.textures.containsKey(name)){
            return AssetPool.textures.get(file.getAbsolutePath());
        } else {
            Texture texture = new Texture(name);
            AssetPool.textures.put(file.getAbsolutePath(),texture);
            return texture;
        }
    }
}
