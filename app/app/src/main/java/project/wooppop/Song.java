package project.wooppop;

/**
 * Created by Camila Brendel on 29/05/2015.
 */
public class Song {
    private long id;
    private String title;
    private String artist;
    private long duration;

    public Song(long songId, String songTitle, String songArtist, long duration) {
        id = songId;
        title = songTitle;
        artist = songArtist;
        this.duration = duration;
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

    public long getDurantion(){
        return this.duration;
    }

}
