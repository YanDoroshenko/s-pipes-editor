package cz.cvut.kbss.spipes.model.klay;

/**
 * Created by Yan Doroshenko (yandoroshenko@protonmail.com) on 14.04.2017.
 */
public class Child {
    private String id;
    private int width;
    private int height;
    private String type;

    public Child(String id, int width, int height, String type) {
        this.id = id;
        this.width = width;
        this.height = height;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
