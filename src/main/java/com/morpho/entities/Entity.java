package com.morpho.entities;

import java.net.URL;
import java.util.List;
import java.util.Map;
import javafx.util.Pair;

/**
 * Created by Ricardo on 29/4/2017.
 */
abstract class Entity {
    private final String ownerId;
    private List<URL> imageList;
    private String scientificName;
    private Boolean isPublic;
    Map<String,Pair<String, Object>> properties;

    Entity(){
        ownerId = null;
    }

    Entity(Entity otherEntity, String ownerId) {
        this.ownerId = ownerId;
        this.imageList = otherEntity.imageList;
        this.scientificName = otherEntity.scientificName;
        this.isPublic = otherEntity.isPublic;
        this.properties = otherEntity.properties;
    }

    public Entity(String ownerId, List<URL> imageList, String scientificName, Boolean isPublic, Map<String,Pair<String, Object>> properties){
        this.ownerId = ownerId;
        this.imageList = imageList;
        this.scientificName = scientificName;
        this.isPublic = isPublic;
        this.properties = properties;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public List<URL> getImageList() {
        return imageList;
    }

    public void setImageList(List<URL> imageList) {
        this.imageList = imageList;
    }

    public String getScientificName() {
        return scientificName;
    }

    public void setScientificName(String scientificName) {
        this.scientificName = scientificName;
    }

    public Boolean getPublic() {
        return isPublic;
    }

    public void setPublic(Boolean aPublic) {
        isPublic = aPublic;
    }

    public Map<String, Pair<String, Object>> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Pair<String, Object>> properties) {
        this.properties = properties;
    }
}
