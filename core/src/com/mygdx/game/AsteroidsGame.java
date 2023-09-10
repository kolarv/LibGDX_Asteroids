package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Iterator;

public class AsteroidsGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	OrthographicCamera camera;
	Array<Sutr> AsteroidArray;
	BitmapFont kolik;
	int dalsi;
	@Override
	public void create () {
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
		AsteroidArray = new Array<Sutr>();
		kolik = new BitmapFont();
		kolik.setColor(Color.RED);
		dalsi = 0;
	}

	@Override
	public void render () {
		ScreenUtils.clear(0, 0, 0, 1);
		camera.update();
		batch.setProjectionMatrix(camera.combined);

		batch.begin();
			for (Sutr astr : AsteroidArray) {
				batch.draw(astr.asteroidImage, astr.pos.x-50, astr.pos.y-50);
				astr.pos.x += astr.smer.x*Gdx.graphics.getDeltaTime();
				astr.pos.y += astr.smer.y*Gdx.graphics.getDeltaTime();
			}
		for (Iterator<Sutr> iter = AsteroidArray.iterator(); iter.hasNext();){
			Sutr sutr = iter.next();
			if(sutr.isOut()){
				iter.remove();
				dalsi--;
			}
		}

		kolik.draw(batch,String.valueOf(dalsi),400,240);
		batch.end();

		if(Gdx.input.isTouched()){
			Vector3 mouse = new Vector3();
			mouse.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			Vector3 smer = new Vector3();
			int randX = MathUtils.random(-10,10)*15;
			int randY = (MathUtils.random(0,20)-10)*15;
			smer.set(randX,randY,0);
			camera.unproject(mouse);
			Sutr sutr = new Sutr(mouse,smer);
			AsteroidArray.add(sutr);
			dalsi++;
		}
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}

class Sutr {
	Texture asteroidImage;
	Vector3 pos;
	Vector3 smer;
	public Sutr(Vector3 pos, Vector3 smer){
		asteroidImage = new Texture("Asteroid.png");
		this.smer = smer;
		this.pos = pos;
	}

	public boolean isOut(){
		if (pos.x>850 || pos.x<-50 ||pos.y<-50 ||pos.y>530){
			return true;
		}
			return false;
	}

	public String getLocation(){
		return "X: "+pos.x+" Y: "+pos.y;
	}
}