package com.albertogomez.ewend.map;

/**
 * Enum for different map types and their files path
 * @author Alberto Gómez
 */
public enum MapType {
    MAP_1("maps/map1/map1.tmx");
    /**
     * Map file path
     */
    private final String filePath;

    /**
     * Constructor that set the filePath
     * @param filePath Map file path
     */
    MapType(String filePath) {this.filePath = filePath;}
    public String getFilePath(){return filePath;}
}
