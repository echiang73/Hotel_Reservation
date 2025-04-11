package model;

public interface IRoom {

    // interface methods are by default public and abstract
    String getRoomNumber();
    Double getRoomPrice();
    RoomType getRoomType();
    boolean isFree();
}
