package org.escoladeltreball.shooter2d;

import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.escoladeltreball.shooter2d.constants.NotificationConstants;
import org.escoladeltreball.shooter2d.entities.loader.PlayerLoader;
import org.escoladeltreball.shooter2d.ui.GameObserver;
import org.escoladeltreball.shooter2d.ui.UI;

/**
 * Controla el flujo del juego
 * 
 * @author Carlos Serrano
 * @author Elvis Puertas
 * @author Jaume Ribas
 */
public class GameManager implements GameObserver, IOnMenuItemClickListener {
	
	public static final int MENU_START = 0;
	public static final int MENU_EXIT = 1;
	public static final int MENU_RESUME = 2;
	
	/** instancia unica */
	private static GameManager instance;
	
	private boolean started = false;
	
	public void setStarted(boolean started) {
		this.started = started;
	}

	public boolean isStarted() {
		return started;
	}

	private GameManager() {}
	
	/**
	 * Este metodo devuelve la instancia unica de GameManager.
	 * Si no existe, la crea.
	 * 
	 * @return la instancia unica de GameManager
	 */
	public static GameManager getInstance() {
		if (instance == null) {
			instance = new GameManager();
		}
		return instance;
	}
	
	@Override
	public void notify(Object notifier, Object data) {
		if (data instanceof Short) {
			short notification = ((Short)data).shortValue();
			if(notifier == PlayerLoader.getPlayer()) {
				switch (notification) {
				case NotificationConstants.CHANGE_HEALTH:
					if (PlayerLoader.getPlayer().getHealthpoints() <= 0)  {
						showGameOver();
					}
					break;
				}
			}
		}
	}	
	
	private void showGameOver() {
		MainActivity.getInstance().currentLevel.setIgnoreUpdate(true);
		MainActivity.getInstance().setCurrentHUD(UI.getGameOverHUD());		
	}

	@Override
	public boolean onMenuItemClicked(MenuScene arg0, IMenuItem arg1,
			float arg2, float arg3) {
		switch (arg1.getID()) {
		case MENU_START:
			this.setStarted(true);
			MainActivity.getInstance().setCurrentHUD(UI.getUIHUD());
		case MENU_RESUME:
			MainActivity.getInstance().openGame();
			return true;
		case MENU_EXIT:
			MainActivity.getInstance().closeActivity();
			return true;
		default:
			break;
		}
		return false;
	}
}
