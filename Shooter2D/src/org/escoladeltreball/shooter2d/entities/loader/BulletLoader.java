package org.escoladeltreball.shooter2d.entities.loader;

import java.io.IOException;

import org.andengine.engine.camera.Camera;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.escoladeltreball.shooter2d.constants.SpriteConstants;
import org.escoladeltreball.shooter2d.entities.Bullet;
import org.escoladeltreball.shooter2d.entities.Player;
import org.escoladeltreball.shooter2d.entities.Zombie;

import android.content.res.AssetManager;

public class BulletLoader extends EntityLoader {

	/**
	 * La clase PlayerLoader se encarga cargar los recursos del jugador y del propio jugador.
	 * 
	 * @author Carlos Serrano
	 * @author Elvis Puertas
	 * @author Jaume Ribas
	 */
	public Bullet loadBullet(Camera camera, int x, int y, TextureManager textureManger,
			AssetManager assets,
			VertexBufferObjectManager vertexBufferObjectManager, float angle, int strengh) {
		
			Bullet bullet = null;
		
			try {
				TiledTextureRegion pTiledTextureRegion;
				pTiledTextureRegion = this.loadResources(textureManger, assets);

				bullet = new Bullet(x, y, pTiledTextureRegion, vertexBufferObjectManager, angle, strengh);
			} catch (IOException e) {
				e.printStackTrace();
			}
		return bullet;


	}
	/**
	 * Carga recursos relacionados con el jugador.
	 * 
	 * @param textureManger un TextureManager
	 * @param assets un AssetManager
	 * @throws IOException
	 */
	public TiledTextureRegion loadResources(TextureManager textureManger,
			AssetManager assets) throws IOException {
		return super.loadResources(textureManger, assets,
				SpriteConstants.BULLET_SPRITE,
				SpriteConstants.BULLET_SPRITE_COLUMNS,
				SpriteConstants.BULLET_SPRITE_ROWS);
	}
}