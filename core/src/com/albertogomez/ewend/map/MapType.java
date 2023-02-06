package com.albertogomez.ewend.map;

public enum MapType {
    MAP_1("maps/mapa.tmx"),
    MAP_2("maps/mapa2.tmx");


    private final String filePath;

    MapType(String filePath) {this.filePath = filePath;}
    public String getFilePath(){return filePath;}
}
