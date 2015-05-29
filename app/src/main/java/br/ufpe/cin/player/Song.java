package br.ufpe.cin.player;

/**
 * Created by Camila Brendel on 29/05/2015.
 */
public class Song {
    private long id;
    private String title;
    private String artist;

    public Song(long songId, String songTitle, String songArtist) {
        id = songId;
        title = songTitle;
        artist = songArtist;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

}
