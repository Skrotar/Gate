package gate.engine;

import gate.stages.Intro;


import com.badlogic.gdx.Game;


public class Engine extends Game {
	
	public static final String NAME = "Gate", VERSION = "0.0.0";


	@Override
	public void create() {
		setScreen(new Intro());
	}
	
	@Override
	public void dispose() {
		super.dispose();
	}

	@Override
	public void render() {	
		super.render();

	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	@Override
	public void pause() {
		super.pause();
	}

	@Override
	public void resume() {
		super.resume();
	}


}
