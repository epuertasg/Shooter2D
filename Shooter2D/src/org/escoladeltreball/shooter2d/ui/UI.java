package org.escoladeltreball.shooter2d.ui;

import java.util.Observable;
import java.util.Observer;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.camera.hud.controls.AnalogOnScreenControl;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.escoladeltreball.shooter2d.MainActivity;
import org.escoladeltreball.shooter2d.ResourceManager;
import org.escoladeltreball.shooter2d.commands.CommandFactory;
import org.escoladeltreball.shooter2d.commands.interfaces.AnalogChangeCommand;
import org.escoladeltreball.shooter2d.commands.interfaces.Command;
import org.escoladeltreball.shooter2d.constants.HPConstants;

/**
 * La clase UI continene variables y metodos relacionados con la interfaz de usuario.
 * Utiliza el patron singleton.
 * 
 * @author Carlos Serrano
 * @author Elvis Puertas
 * @author Jaume Ribas
 */
public class UI implements Observer {
	
	private static final float HEALTH_BAR_WIDTH = 200;
	private static final float HEALTH_BAR_HEIGHT = 7;
	private static final float HEALTH_BAR_ANGLE = 0;
	private static final float HEALTH_BAR_X = MainActivity.CAMERA_WIDTH - HEALTH_BAR_WIDTH - 20;
	private static final float HEALTH_BAR_Y = MainActivity.CAMERA_HEIGHT - 20;
	
	private static final float ANALOG_ALPHA = 0.5f;
	private static final float ANALOG_TIME_BETWEEN_UPDATES = 0.1f;

	private static final float LEFT_ANALOG_X = 0;
	private static final float LEFT_ANALOG_Y = 0;
	private static final float LEFT_ANALOG_OFFSET_CENTER_X = 0;
	private static final float LEFT_ANALOG_OFFSET_CENTER_Y = 0;
	
	private static final float RIGHT_ANALOG_X = MainActivity.CAMERA_WIDTH;
	private static final float RIGHT_ANALOG_Y = 0;
	private static final float RIGHT_ANALOG_OFFSET_CENTER_X = 1;
	private static final float RIGHT_ANALOG_OFFSET_CENTER_Y = 0;

	

	/** instancia unica */
	private static UI instance;
	
	/** instancia unica de HUD */
	private static HUD hud;

	private AnalogOnScreenControl leftAnalogControl;
	private AnalogOnScreenControl rightAnalogControl;
	private HUDBar healthBar;
	
	private UI(){}
	
	/**
	 * Devuelve la unica instancia.
	 * Si no existe la crea.
	 * 
	 * @return la unica instancia de UI
	 */
	public static UI getInstance() {
		if (instance == null) {
			instance = new UI();
		}
		return instance;
	}
	
	/**
	 * Devuelve la unica instancia de {@link HUD}.
	 * Si no existe la crea.
	 * 
	 * @return la unica instancia de {@link HUD}
	 */
	public static HUD getHUD() {
		if (hud == null) {
			hud = new HUD();
		}
		return hud;
	}
	
	/**
	 * Crea los componentes de la interfaz de usuario y los une al HUD.
	 */
	public void createUI(VertexBufferObjectManager vertexBufferObjectManager) {
		//Control analogico izquierda 
		this.leftAnalogControl = createAnalogControl(getHUD().getCamera(), LEFT_ANALOG_X, LEFT_ANALOG_Y, LEFT_ANALOG_OFFSET_CENTER_X, LEFT_ANALOG_OFFSET_CENTER_Y, CommandFactory.getSetPlayerVelocityAndOrientation(), CommandFactory.getDoNothingCommand(), vertexBufferObjectManager);
		// Control analogico derecha
		this.rightAnalogControl = createAnalogControl(getHUD().getCamera(), RIGHT_ANALOG_X, RIGHT_ANALOG_Y, RIGHT_ANALOG_OFFSET_CENTER_X, RIGHT_ANALOG_OFFSET_CENTER_Y, CommandFactory.getSetPlayerOrientationAndShoot(), CommandFactory.getDoNothingCommand(), vertexBufferObjectManager);
		this.leftAnalogControl.setChildScene(this.rightAnalogControl);
		getHUD().setChildScene(this.leftAnalogControl);
		// Barra vida
		this.healthBar = new HUDBar(HEALTH_BAR_X, HEALTH_BAR_Y, HEALTH_BAR_WIDTH, HEALTH_BAR_HEIGHT, HPConstants.HUMAN_HEALTH, HPConstants.HUMAN_HEALTH, HEALTH_BAR_ANGLE, vertexBufferObjectManager);
		getHUD().attachChild(this.healthBar);
	}


	/**
	 * Crea un {@link AnalogOnScreenControl}
	 * 
	 * @param camera
	 * @param x
	 * @param y
	 * @param offsetCenterX
	 * @param offsetCenterY
	 * @param analogChangeCommand
	 * @param analogClickCommand
	 * @param vertexBufferObjectManager
	 * @return un {@link AnalogOnScreenControl}
	 */
	private AnalogOnScreenControl createAnalogControl(Camera camera, float x, float y, float offsetCenterX, float offsetCenterY, AnalogChangeCommand analogChangeCommand, Command analogClickCommand, VertexBufferObjectManager vertexBufferObjectManager) {
		
		ConfigurableAnalogControlListener leftAnalogListener = new ConfigurableAnalogControlListener();
		leftAnalogListener.setAnalogChangeCommand(analogChangeCommand == null? CommandFactory.getDoNothingAnalogCommand(): analogChangeCommand);
		leftAnalogListener.setAnalogClickCommand(analogClickCommand == null? CommandFactory.getDoNothingCommand() : analogClickCommand);
		
		
		AnalogOnScreenControl analogControl = new AnalogOnScreenControl(x, y, camera, ResourceManager.getInstance().analogControlBaseTextureRegion, ResourceManager.getInstance().analogControlKnobTextureRegion, ANALOG_TIME_BETWEEN_UPDATES, vertexBufferObjectManager, leftAnalogListener);

		{
			final Sprite controlBase = analogControl.getControlBase();
			controlBase.setAlpha(ANALOG_ALPHA);
			controlBase.setOffsetCenter(offsetCenterX, offsetCenterY);
		}
		return analogControl;
	}
	
	@Override
	public void update(Observable observable, Object data) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Devuelve el comando que se ejecuta al mover
	 * el analog izquierdo
	 * 
	 * @return un objeto {@link AnalogChangeCommand}
	 */
	public AnalogChangeCommand getLeftAnalogChangeCommand(){
		return ((ConfigurableAnalogControlListener)this.leftAnalogControl.getOnScreenControlListener()).getAnalogChangeCommand();
	}
	
	/**
	 * Setea un comando que se ejecutará al mover
	 * el analog izquierdo
	 * 
	 * @param analogChangeCommand un objeto {@link AnalogChangeCommand}
	 */
	public void setLeftAnalogChangeCommand(AnalogChangeCommand analogChangeCommand) {
		((ConfigurableAnalogControlListener)this.leftAnalogControl.getOnScreenControlListener()).setAnalogChangeCommand(analogChangeCommand);
	}
	
	/**
	 * Devuelve el comando que se ejecuta al mover
	 * el analog derecho
	 * 
	 * @return un objeto {@link AnalogChangeCommand}
	 */
	public AnalogChangeCommand getRightAnalogChangeCommand(){
		return ((ConfigurableAnalogControlListener)this.rightAnalogControl.getOnScreenControlListener()).getAnalogChangeCommand();
	}
	
	/**
	 * Setea un comando que se ejecutará al mover
	 * el analog derecho
	 * 
	 * @param analogChangeCommand un objeto {@link AnalogChangeCommand}
	 */
	public void setRightAnalogChangeCommand() {
		((ConfigurableAnalogControlListener)this.rightAnalogControl.getOnScreenControlListener()).getAnalogChangeCommand();
	}
	
	/**
	 * Devuelve el comando que se ejecuta al clicar
	 * el analog izquierdo
	 * 
	 * @return un objeto {@link Command}
	 */
	public Command getLeftAnalogClickCommand(){
		return ((ConfigurableAnalogControlListener)this.leftAnalogControl.getOnScreenControlListener()).getAnalogClickCommand();
	}
	
	/**
	 * Setea un comando que se ejecutará al clicar
	 * el analog izquierdo
	 * 
	 * @param analogClickCommand un objeto {@link Command}
	 */
	public void setLeftAnalogClickCommand(Command analogClickCommand) {
		((ConfigurableAnalogControlListener)this.leftAnalogControl.getOnScreenControlListener()).setAnalogClickCommand(analogClickCommand);
	}
	
	/**
	 * Devuelve el comando que se ejecuta al clicar
	 * el analog derecho
	 * 
	 * @return un objeto {@link Command}
	 */
	public Command getRightAnalogClickCommand(){
		return ((ConfigurableAnalogControlListener)this.rightAnalogControl.getOnScreenControlListener()).getAnalogClickCommand();
	}
	
	/**
	 * Setea un comando que se ejecutará al clicar
	 * el analog derecho
	 * 
	 * @param analogClickCommand un objeto {@link Command}
	 */
	public void setRightAnalogClickCommand() {
		((ConfigurableAnalogControlListener)this.rightAnalogControl.getOnScreenControlListener()).getAnalogClickCommand();
	}
}
