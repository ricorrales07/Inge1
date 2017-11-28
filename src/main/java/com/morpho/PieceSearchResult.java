package com.morpho;

/**
 * Created by irvin on 11/27/17.
 */
public class PieceSearchResult {

    private String _id;
    private String image_binary;

    public PieceSearchResult(){

    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getImage_binary() {
        return image_binary;
    }

    public void setImage_binary(String image_binary) {
        this.image_binary = image_binary;
    }
}
