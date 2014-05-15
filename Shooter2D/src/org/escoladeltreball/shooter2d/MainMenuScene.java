package org.escoladeltreball.shooter2d;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.TextMenuItem;
import org.andengine.ui.activity.BaseActivity;

/**
 * El menu principal
 * 
 * @author Carlos Serrano
 * @author Elvis Puertas
 * @author Jaume Ribas
 */
public class MainMenuScene extends MenuScene implements
		IOnMenuItemClickListener {

	BaseActivity activity;
	private static final int MENU_START = 0;

	public MainMenuScene(Camera camera, Engine engine, BaseActivity activity) {
		super(camera);
		setBackground(new Background(0.09804f, 0.6274f, 0.8784f));
		IMenuItem startButton = new TextMenuItem(MENU_START, ResourceManager.getInstance().hudFont, activity.getString(R.string.start),
				engine.getVertexBufferObjectManager());
		startButton.setPosition(MainActivity.CAMERA_WIDTH / 2 - startButton.getWidth()
				/ 2, MainActivity.CAMERA_HEIGHT / 2 - startButton.getHeight() / 2);

		addMenuItem(startButton);
		setOnMenuItemClickListener(this);
	}

	@Override
	public boolean onMenuItemClicked(MenuScene arg0, IMenuItem arg1,
			float arg2, float arg3) {
		switch (arg1.getID()) {
		case MENU_START:
			MainActivity.getInstance().startGame();
			return true;
		default:
			break;
		}
		return false;
	}

}
