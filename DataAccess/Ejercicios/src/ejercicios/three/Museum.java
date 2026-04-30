package ejercicios.three;

import java.util.HashSet;

public class Museum {

    private String _name;

    public String getName() {
        return _name;
    }
    public void setName(String name) {
        _name = name;
    }



    private String _address;

    public String getAddress() {
        return _address;
    }
    public void setAddress(String _address) {
        this._address = _address;
    }


    private String _city;
    public String getCity() {
        return _city;
    }
    public void setCity(String city) {
        this._city = city;
    }
    private String _country;
    public String getCountry() {
        return _country;
    }
    public void setCountry(String country) {
        _country = country;
    }

    private HashSet<Art> _art;

    public HashSet<Art> getArt() {
        return _art;
    }
    public void setArt(HashSet<Art> art) {
        _art = art;
    }
    public void addArt(Art art){
        _art.add(art);
    }
    public void addArt(HashSet<Art> art){
        _art.addAll(art);
    }
    public void removeArt(Art art){
        _art.remove(art);
    }

    private HashSet<Room> _room = new HashSet<>();
    public HashSet<Room> getRooms() {
        return _room;
    }
    public void setRooms(HashSet<Room> room) {
        _room = room;
    }
    public void addRoom(Room room){
        _room.add(room);
    }
    public void removeRoom(Room room){
        _room.remove(room);
    }




}
