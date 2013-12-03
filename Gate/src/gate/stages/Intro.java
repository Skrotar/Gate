package gate.stages;


import gate.entities.Player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;


public class Intro  implements Screen {
	
	private TiledMap map;
	private OrthogonalTiledMapRenderer renderer;
	private OrthographicCamera camera;
	
	private SpriteBatch batch;
	private Sprite backimage;

	private TextureAtlas playerAtlas;
	private Player player;

	int cwidth;
	int cheight;

	
	private int[] background = new int[] {0}, foreground = new int[] {1},  collision = new int[] {2};

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


		if (player.getX() < ((cwidth / 2) - (player.getWidth() / 2))){
			camera.position.set(cwidth / 2, player.getY() + player.getHeight() / 2, 0);}
			else {
				camera.position.set(player.getX() + player.getWidth() / 2, player.getY() + player.getHeight() / 2, 0);}


		camera.update();
		renderer = new OrthogonalTiledMapRenderer(map);
		renderer.setView(camera);
		
		batch.begin();
		backimage.draw(batch);
		batch.end();
		
		renderer.render(background);
		
		renderer.getSpriteBatch().begin();
		player.draw(renderer.getSpriteBatch());
		renderer.getSpriteBatch().end();
		
		

		renderer.render(foreground);
		renderer.render(collision);
	
	
	
	}

	@Override
	public void resize(int width, int height) {

	   
		//changes the map viewport 
		camera.viewportWidth = width; /// 2.5f;
		camera.viewportHeight = height; /// 2.5f;
		cwidth = width;
		cheight = height;
		
	}

	@Override
	public void show() {
		map = new TmxMapLoader().load("maps/map.tmx");



		camera = new OrthographicCamera();
		batch = new SpriteBatch();
		backimage = new Sprite(new Texture("images/backgrounds/background.png"));

		playerAtlas = new TextureAtlas("img/player/player.pack");
		Animation still, left, right, attack;
		still = new Animation(2 / 2f, playerAtlas.findRegions("still"));
		left = new Animation(1 / 6f, playerAtlas.findRegions("left"));
		right = new Animation(1 / 6f, playerAtlas.findRegions("right"));
		attack = new Animation(1 / 6f, playerAtlas.findRegions("attack"));
		still.setPlayMode(Animation.LOOP);
		left.setPlayMode(Animation.LOOP);
		right.setPlayMode(Animation.LOOP);
		attack.setPlayMode(Animation.NORMAL);

		player = new Player(still, left, right, attack, (TiledMapTileLayer) map.getLayers().get(0));
	
		MapLayer layer = map.getLayers().get("objects");
			for(MapObject object : layer.getObjects())
				if(object.getName().equals("playerstart"))
            	 player.setPosition(object.getProperties().get("x", Integer.class), object.getProperties().get("y", Integer.class));
        
     //   player = new Player(still, left, right, attack, (TiledMapTileLayer) map.getLayers().get(0));
       
        
	//	player.setPosition(150, 290);
		//player.setPosition(11 * player.getCollisionLayer().getTileWidth(), (player.getCollisionLayer().getHeight() - 14) * player.getCollisionLayer().getTileHeight());
	//	player.setScale(2);
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
		backimage.getTexture().dispose();
		playerAtlas.dispose();
	}


}
