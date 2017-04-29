package com.entities;

/**
 * Created by Ricardo on 29/4/2017.
 */
public class Animal extends Entity {

    Composition composition;
    int animalId;

    public Animal(){

    }

    public Composition getComposition() {
        return composition;
    }

    public void addComposition(Composition composition) {
        this.composition = composition;
    }

    public void deleteComposition(){
        this.composition = null;
    }

    public int getAnimalId() {
        return animalId;
    }

    public void setAnimalId(int animalId) {
        this.animalId = animalId;
    }
}
