package org.escoladeltreball.shooter2d;

import java.io.IOException;
import java.util.ArrayList;

import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.Engine;
import org.andengine.engine.FixedStepEngine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.camera.hud.controls.AnalogOnScreenControl;
import org.andengine.engine.camera.hud.controls.BaseOnScreenControl;
import org.andengine.engine.camera.hud.controls.AnalogOnScreenControl.IAnalogOnScreenControlListener;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.tmx.TMXLayer;
import org.andengine.extension.tmx.TMXTiledMap;
import org.andengine.input.touch.controller.MultiTouch;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.escoladeltreball.shooter2d.commands.CommandManager;
import org.escoladeltreball.shooter2d.entities.Player;
import org.escoladeltreball.shooter2d.entities.loader.PlayerLoader;
import org.escoladeltreball.shooter2d.ui.UI;

import com.badlogic.gdx.math.Vector2;

import android.widget.Toast;

public class MainActivity extends BaseGameActivity
{
	private BoundCamera camera;
	private MapCreator mapCreator;
	private PlayerLoader playerLoader;
	public static final int CAMERA_WIDTH = 720;
	public static final int CAMERA_HEIGHT = 480;
	public static final int STEPS_PER_SECOND = 60;
	public FixedStepPhysicsWorld mPhysicsWorld;
	private Player player;
	private VertexBufferObjectManager vbo = new VertexBufferObjectManager();

	
	@Override
	public Engine onCreateEngine(final EngineOptions pEngineOptions) {
		return new FixedStepEngine(pEngineOptions, STEPS_PER_SECOND);
	}
	
	@Override
	public EngineOptions onCreateEngineOptions()
	{
		checkCompatibilityMultiTouch();
		camera = new BoundCamera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, 
				new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), camera);
		engineOptions.getRenderOptions().setDithering(true);
		engineOptions.getRenderOptions().getConfigChooserOptions().setRequestedMultiSampling(true);
		engineOptions.setWakeLockOptions(WakeLockOptions.SCREEN_ON);
		engineOptions.getTouchOptions().setNeedsMultiTouch(true);
		engineOptions.getAudioOptions().setNeedsMusic(true);
		engineOptions.getAudioOptions().setNeedsSound(true);
		this.mapCreator = new MapCreator();
		this.playerLoader = new PlayerLoader();
		return engineOptions;
	}

	@Override
	public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback) throws IOException
	{
		ResourceManager.getInstance().loadGameTextures(mEngine, this);
		this.playerLoader.loadResources(this.getTextureManager(), this.getAssets());
		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback)
	{
		Scene scene = new Scene();
		scene.setBackground(new Background(0.09804f, 0.6274f, 0.8784f));
		this.player = playerLoader.loadPlayer(camera,  getTextureManager(), getAssets(), getVertexBufferObjectManager());
		// Añade los controles con commandos a la escena 
		scene.setChildScene(UI.getInstance().createAnalogControls(this.camera, this.getVertexBufferObjectManager(), CommandManager.getSetPlayerVelocity(this.player), CommandManager.getDoNothingCommand(), CommandManager.getDoNothingAnalogCommand(), CommandManager.getDoNothingCommand()));
		// Muestra el mapa en la pantalla
		TMXTiledMap map = this.mapCreator.loadMap(getAssets(), getTextureManager(), getVertexBufferObjectManager());
		scene.attachChild(map);
		// Muentra sobre el mapa rectangulos que son areas de colision
		ArrayList<Rectangle> rectangles = mapCreator.createUnwalkableObjects(map, vbo);
		for (Rectangle rect : rectangles){
			scene.attachChild(rect);
		}
		
		// La camara no execede el tamaño del mapa
        final TMXLayer tmxLayer = map.getTMXLayers().get(0);
        this.camera.setBounds(0, 0, tmxLayer.getWidth(), tmxLayer.getHeight());
        this.camera.setBoundsEnabled(true);
		// La camara sigue al jugador
		this.camera.setChaseEntity(player);
		scene.attachChild(player);
		pOnCreateSceneCallback.onCreateSceneFinished(scene);
	}
	
	@Override
	public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback) throws IOException {
		
		this.mPhysicsWorld = new FixedStepPhysicsWorld(STEPS_PER_SECOND, new Vector2(0f, 0), false, 0, 0);
		
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}

	/**
	 * Comprueba si existe compatibilidad con multitouch
	 */
	public void checkCompatibilityMultiTouch(){
		if(MultiTouch.isSupported(this)) {  
			if(!MultiTouch.isSupportedDistinct(this)) { 
				Toast.makeText(this, "MultiTouch detected, but your device has problems distinguishing between fingers.", Toast.LENGTH_LONG).show();  
			}  
		} else {  
			Toast.makeText(this, "Sorry your device does NOT support MultiTouch!", Toast.LENGTH_LONG).show();  
		}  
	}
}
