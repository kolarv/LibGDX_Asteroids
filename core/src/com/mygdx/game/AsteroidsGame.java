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
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Iterator;

public class AsteroidsGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	OrthographicCamera camera;
	Array<MoovableSutr> AsteroidArray;
	Array<ClickableSutr> ClickableArray;
	BitmapFont kolik;
	int dalsi;
	@Override
	public void create () {
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
		AsteroidArray = new Array<MoovableSutr>();
		ClickableArray = new Array<ClickableSutr>();
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
			for (MoovableSutr astr : AsteroidArray) {
				batch.draw(astr.asteroidImage, astr.pos.x-32, astr.pos.y-32);
				astr.pos.x += astr.smer.x*Gdx.graphics.getDeltaTime();
				astr.pos.y += astr.smer.y*Gdx.graphics.getDeltaTime();
			}
			for (ClickableSutr clik : ClickableArray){
				batch.draw(clik.asteroidImage,clik.pos.x-32, clik.pos.y-32);
			}
		for (Iterator<MoovableSutr> iter = AsteroidArray.iterator(); iter.hasNext();){
			MoovableSutr sutr = iter.next();
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
			MoovableSutr moovablesutr = new MoovableSutr(mouse,smer);
			AsteroidArray.add(moovablesutr);
			dalsi++;
		}

		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
			Vector3 mouse = new Vector3();
			mouse.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(mouse);
			ClickableSutr cks = new ClickableSutr(mouse);
			ClickableArray.add(cks);
		}

		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
			for (Iterator<ClickableSutr> iter = ClickableArray.iterator(); iter.hasNext();){
				ClickableSutr clik = iter.next();
					Vector3 tmp=new Vector3(Gdx.input.getX(),Gdx.input.getY(),0);
					camera.unproject(tmp);
					Rectangle textureBounds=new Rectangle(clik.pos.x-32,clik.pos.y-32,clik.asteroidImage.getWidth(),clik.asteroidImage.getHeight());// textureheight is the height of the texture (you can get it with texture.getHeight() or textureRegion.getRegionhHeight() if you have a texture region
					if(textureBounds.contains(tmp.x,tmp.y))
					{
						clik.swapTextures();
					}
			}
		}
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
class Sutr{
	Texture asteroidImage;
	Vector3 pos;

	public Sutr(Vector3 pos){
		this.pos = pos;
		asteroidImage = new Texture("Asteroid.png");
	}
}

class MoovableSutr extends Sutr{
	Vector3 smer;
	public MoovableSutr(Vector3 pos, Vector3 smer){
		super(pos);
		this.smer = smer;
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

class ClickableSutr extends Sutr{
	boolean selected;
Texture redS = new Texture("Redsteroid.png");
Texture astS = new Texture("Asteroid.png");
	public ClickableSutr(Vector3 pos) {
		super(pos);
		if(MathUtils.random(0,1) == 1){
			asteroidImage = redS;
		}
		else {
			asteroidImage = astS;
		}
	}

	public void swapTextures(){
		if(asteroidImage == redS){
			asteroidImage = astS;
		}
		else{
			asteroidImage = redS;
		}
	}
	public boolean isSelected(){
		return selected;
	}
	public void select(){
		selected = true;
	}
	public void deselect(){
		selected = false;
	}
}