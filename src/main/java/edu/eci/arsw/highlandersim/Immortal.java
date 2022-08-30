package edu.eci.arsw.highlandersim;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Immortal extends Thread {

    private ImmortalUpdateReportCallback updateCallback=null;
    
    private AtomicInteger health;
    
    private int defaultDamageValue;

    private final List<Immortal> immortalsPopulation;

    private final String name;

    private final Random r = new Random(System.currentTimeMillis());

    private boolean paused = false;

    private boolean live = true;


    public Immortal(String name, List<Immortal> immortalsPopulation, AtomicInteger health, int defaultDamageValue, ImmortalUpdateReportCallback ucb) {
        super(name);
        this.updateCallback=ucb;
        this.name = name;
        this.immortalsPopulation = immortalsPopulation;
        this.health = health;
        this.defaultDamageValue=defaultDamageValue;
    }

    public void run() {

        while (live) {
            Immortal im;
            synchronized(immortalsPopulation){
                try {
                    if(paused){
                        immortalsPopulation.wait();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            int myIndex = immortalsPopulation.indexOf(this);

            int nextFighterIndex = r.nextInt(immortalsPopulation.size());

            //avoid self-fight
            if (nextFighterIndex == myIndex) {
                nextFighterIndex = ((nextFighterIndex + 1) % immortalsPopulation.size());
            }

            im = immortalsPopulation.get(nextFighterIndex);

            if(im.getLive()){
                this.fight(im);
            }
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        
    }

    public void pause(){
        this.paused = true;
    }

    public void resumee(){
        this.paused = false;
    }

    public void kill(){
        this.live = false;
    }

    public boolean getLive(){
        return this.live;
    }

    public void fight(Immortal i2) {
        synchronized(immortalsPopulation){
            synchronized(i2.getHealth()){
                if (i2.getHealth().get() > 0) {
                    updateCallback.processReport("Fight: " + this + " vs " + i2+"\n");
                    i2.changeHealth(i2.getHealth().get() - defaultDamageValue);
                    this.health.getAndAdd(defaultDamageValue);
                    
                } else {
                    updateCallback.processReport(this + " says:" + i2 + " is already dead!\n");
                }
            }
        }
    }

    public void changeHealth(int v) {
        health.getAndSet(v);
        if(health.get() == 0){
            this.live = false;
        }
    }

    public AtomicInteger getHealth() {
        return health;
    }

    @Override
    public String toString() {

        return name + "[" + health + "]";
    }

}
