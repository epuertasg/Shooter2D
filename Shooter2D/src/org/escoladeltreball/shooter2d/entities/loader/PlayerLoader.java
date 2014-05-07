package org.escoladeltreball.shooter2d.entities.loader;

import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.escoladeltreball.shooter2d.ResourceManager;
import org.escoladeltreball.shooter2d.entities.Player;

public class PlayerLoader {

	private static Player player;
	
	private PlayerLoader(){};

	/**
	 * La clase PlayerLoader se encarga cargar los recursos del jugador y del propio jugador.
	 * 
	 * @author Carlos Serrano
	 * @author Elvis Puertas
	 * @author Jaume Ribas
	 */
	public static Player loadPlayer(float x, float y, VertexBufferObjectManager vertexBufferObjectManager) {
		if (player == null) {
			TiledTextureRegion pTiledTextureRegion;
			pTiledTextureRegion = ResourceManager.getInstance().playerTextureRegion;

			player = new Player(x, y, pTiledTextureRegion,
					vertexBufferObjectManager);
		}
		return player;

	}
	
	/**
	 * Obtiene la instancia de player.
	 * Puede ser null si todavia no se ha creado.
	 * 
	 * @return
	 */
	public static Player getPlayer() {
		return player;
	}
}
