package com.mygdx.game.states;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Stack;

//будет 2 состояния:игра и пауза
public class GameStateManager {
    //создаем стэк состояний
    private Stack<State> states;
    public GameStateManager(){
        //создаем пустой стэк
        states=new Stack<State>();
    }
    //помещаем элемент в вершину стека, т.е состояния игровых экранов
    public void push(State state){
        states.push(state);
    }
    //извлекаем верхний элемент, удаляем его из стэка
    public void pop(){
        states.pop().dispose();
    }
    // этот метод будетудалять верхний экран и помещеать следующий экран в вершину стека?!
    public void set (State state){
        states.pop().dispose();
        states.push(state);
    }
    //обновляет игровое состояние, которое на вершине стека
    public void update(float dt){
        states.peek().update(dt);
    }
    public void render (SpriteBatch spriteBatch){
        states.peek().render(spriteBatch);
    }
}
