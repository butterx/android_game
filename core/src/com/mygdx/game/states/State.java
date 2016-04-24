package com.mygdx.game.states;


import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

//этот класс будет управлять окнами или состояниями игры
public abstract class State {
    protected PerspectiveCamera cam;
    protected Vector3 mouse;
    protected GameStateManager gsm;

    public State (GameStateManager gsm){
        this.gsm=gsm;
        cam=new PerspectiveCamera();
        mouse=new Vector3();
    }
    //были ли нажаты определенные клавиши
    protected abstract void handleInput();
    //обновляет картинку через определенное время
    public abstract void update(float dt);
    //метод,рисующий экран
    public abstract void render(SpriteBatch spritebatch);
   //освобождает ресурсы, удаляет экземпляры класса
    public abstract void dispose();
}
