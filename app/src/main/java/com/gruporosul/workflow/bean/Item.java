package com.gruporosul.workflow.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by soporte on 11/11/2015.
 */
public class Item {

    private int id;
    private String imagen;
    private String texto;

    public static List<Item> ITEMS = new ArrayList<>();

    public static Item getItem(int id) {
        for (Item item : ITEMS) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }

    public Item() {
    }

    public Item(int id, String imagen, String texto) {
        this.id = id;
        this.imagen = imagen;
        this.texto = texto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }
}
