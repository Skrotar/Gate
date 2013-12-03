package gate.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Vector2;

public class Player extends Sprite implements InputProcessor {

	/** the movement velocity */
	private Vector2 velocity = new Vector2();
	private float speed = 60 * 2, gravity = 60 * 2.2f, animationTime = 0;

	private boolean canJump;
	private boolean attacking;
	private boolean rsloped = false;


	private Animation still, left, right, attack;
	private TiledMapTileLayer collisionLayer;

	private String blockedKey = "blocked";
	

	public Player(Animation still, Animation left, Animation right, Animation attack, TiledMapTileLayer collisionLayer) {
		super(still.getKeyFrame(0));
		this.still = still;
		this.left = left;
		this.right = right;
		this.attack = attack;
		this.collisionLayer = collisionLayer;
		setSize(getWidth(), getHeight() /* * 1.5f*/);
		//setScale((float) 1.4);
	}

	@Override
	public void draw(SpriteBatch spriteBatch) {
		update(Gdx.graphics.getDeltaTime());
		super.draw(spriteBatch);
	}

	public void update(float delta) {
		// apply gravity
		velocity.y -= gravity * delta;
		
		// clamp velocity
		if(velocity.y > speed) 
			velocity.y = speed;
		else if(velocity.y < -speed)
			velocity.y = -speed;

		// save old position
		float oldX = getX(), oldY = getY();
		boolean collisionX = false, collisionY = false;
		

		// move on x
		setX(getX() + velocity.x * delta);

	
		if(velocity.x < 0) // going left
		   collisionX = collidesLeft();
		else if(velocity.x > 0) // going right
			collisionX = collidesRight();

		// react to x collision
		if(collisionX) {
			setX(oldX);
			velocity.x = 0;
		}


		// move on y
		setY(getY() + velocity.y * delta * 5f);

		if(velocity.y < 0) // going down
			canJump = collisionY = collidesBottom();

		else if(velocity.y > 0) // going up
			collisionY = collidesTop();
		

		// react to y collision
		if(collisionY) {
			setY(oldY);
			velocity.y = 0;
		}

		// update animation
		animationTime += delta;
		//setRegion(velocity.x < 0 ? left.getKeyFrame(animationTime) : velocity.x > 0 ? right.getKeyFrame(animationTime) : still.getKeyFrame(animationTime));
		//setRegion(attack.getKeyFrame(animationTime)); 
		setRegion(velocity.x < 0 ? left.getKeyFrame(animationTime) : velocity.x > 0 ? right.getKeyFrame(animationTime) : attacking == true ? attack.getKeyFrame(animationTime) :still.getKeyFrame(animationTime));
	}

	private boolean isCellBlocked(float x, float y) {
		Cell cell = collisionLayer.getCell((int) (x / collisionLayer.getTileWidth()), (int) (y / collisionLayer.getTileHeight()));
		return cell != null && cell.getTile() != null && cell.getTile().getProperties().containsKey(blockedKey);
	}

	public boolean collidesRight() {
		for(float step = 0; step < getHeight(); step += collisionLayer.getTileHeight() / 2)
			if(isCellBlocked(getX() + getWidth(), getY() + step))
				return true;
		return false;
	}
	

	public boolean collidesLeft() {
		for(float step = 0; step < getHeight(); step += collisionLayer.getTileHeight() / 2)
			if(isCellBlocked(getX(), getY() + step))
				return true;
		return false;
	}

	public boolean collidesTop() {
		for(float step = 0; step < getWidth(); step += collisionLayer.getTileWidth() / 2)
			if(isCellBlocked(getX() + step, getY() + getHeight()))
				return true;
		return false;

	}

	public boolean collidesBottom() {
		for(float step = 0; step < getWidth(); step += collisionLayer.getTileWidth() / 2)
			if(isCellBlocked(getX() + step, getY()))
				return true;
		return false;
	}

	public Vector2 getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector2 velocity) {
		this.velocity = velocity;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public float getGravity() {
		return gravity;
	}

	public void setGravity(float gravity) {
		this.gravity = gravity;
	}

	public TiledMapTileLayer getCollisionLayer() {
		return collisionLayer;
	}

	public void setCollisionLayer(TiledMapTileLayer collisionLayer) {
		this.collisionLayer = collisionLayer;
	}

	@Override
	public boolean keyDown(int keycode) {
		switch(keycode) {
		case Keys.W:
			if(canJump) {
				velocity.y = speed / 1.8f;
				canJump = false;
			}
			break;
		case Keys.A:
			velocity.x = -speed;
			animationTime = 0;
			break;
		case Keys.D:
			velocity.x = speed;
			//react to x sloping
			if (rsloped){
				setY(+ 16);
			}
			animationTime = 0;
		
	case Keys.I:
		attacking = true;
		animationTime = 0.15f;
	}
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		switch(keycode) {
		case Keys.A:
		case Keys.D:
			velocity.x = 0;
			animationTime = 0;
		case Keys.I:
			animationTime = 0;
			attacking = false;
		}
		return true;
	}


	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

}