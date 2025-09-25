package Models_2D;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

import java.io.File;
import java.io.IOException;

public class TextureLoader {

    public static int loadTexture(String filePath) {
        Texture texture = null;
        try {
            File textureFile = new File(filePath);
            texture = TextureIO.newTexture(textureFile, true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return texture != null ? texture.getTextureObject() : 0;
    }

    public static void bindTexture(GL2 gl, int textureId) {
        gl.glBindTexture(GL2.GL_TEXTURE_2D, textureId);
    }
}
