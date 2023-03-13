package me.mowlcoder.mypetplugin.pet;

public enum PetDataKey {
    COLOR("my_pet_color"),
    NAME("my_pet_name"),
    BLOCK("my_pet_block");

    private String key;
    PetDataKey(String key){
        this.key = key;
    }
    public String getKey(){ return key;}
}
