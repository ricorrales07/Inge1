package com.morpho.entities;

/**
 * Created by Ricardo on 29/4/2017.
 */
public class Composition extends Entity{

    private int compID;
    private int a1;
    private int a2;
    private int b1;
    private int b2;

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
        return new int[]{a1, a2, b1, b2};
    }

    public void setCompProperties(int x1, int x2, int y1, int y2){

    }
}
