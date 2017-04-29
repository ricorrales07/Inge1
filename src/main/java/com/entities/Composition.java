package com.entities;

/**
 * Created by Ricardo on 29/4/2017.
 */
public class Composition extends Entity{

    int compID;
    int a1;
    int a2;
    int b1;
    int b2;

    public Composition(){

    }

    public void addPiece(Piece piece, int x1, int x2, int y1, int y2){

    }

    public void deletePiece(Piece piece){

    }

    public int getCompID() {
        return compID;
    }

    public void setCompID(int compID) {
        this.compID = compID;
    }

    public int[] getCompProperties(){
        int[] vector = {a1, a2, b1, b2};
        return vector;
    }

    public void setCompProperties(int x1, int x2, int y1, int y2){

    }
}
