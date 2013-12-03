package gate.stages;

import gate.entities.Player;

import java.util.Iterator;



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.utils.Array;

public class Test implements Screen {

	private TiledMap map;
	private OrthogonalTiledMapRenderer renderer;
	private OrthographicCamera camera;

	private TextureAtlas playerAtlas;
	private Player player;

	private int[] background = new int[] {0}, foreground = new int[] {1};
	
	int mouseX = Gdx.input.getX();


	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		

	//	if (player.getX() > 50){
			camera.position.set(player.getX() + player.getWidth() / 2, player.getY() + player.getHeight() / 2, 0);
	//	else 
		//	camera.position.set(50, 0, 0);
		camera.update();

		renderer.setView(camera);

		renderer.render(background);

		renderer.getSpriteBatch().begin();
		player.draw(renderer.getSpriteBatch());
	
		renderer.getSpriteBatch().end();

		renderer.render(foreground);

	
	
	}

	@Override
	public void resize(int width, int height) {
		camera.viewportWidth = width / 2.5f;
		camera.viewportHeight = height / 2.5f;
	}

	@Override
	public void show() {
		map = new TmxMapLoader().load("maps/map.tmx");

		renderer = new OrthogonalTiledMapRenderer(map);

		Gdx.gl.glLineWidth(3);

		camera = new OrthographicCamera();

		playerAtlas = new TextureAtlas("img/player/player.pack");
		Animation still, left, right;
		still = new Animation(1 / 2f, playerAtlas.findRegions("still"));
		left = new Animation(1 / 6f, playerAtlas.findRegions("left"));
		right = new Animation(1 / 6f, playerAtlas.findRegions("right"));
		still.setPlayMode(Animation.LOOP);
		left.setPlayMode(Animation.LOOP);
		right.setPlayMode(Animation.LOOP);

		player = new Player(still, left, right, right, (TiledMapTileLayer) map.getLayers().get(0));
		player.setPosition(11 * player.getCollisionLayer().getTileWidth(), (player.getCollisionLayer().getHeight() - 14) * player.getCollisionLayer().getTileHeight());

		Gdx.input.setInputProcessor(player);

		
	}

	@Override
	public void hide() {
		dispose();
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		map.dispose();
		renderer.dispose();

		playerAtlas.dispose();
	}

}