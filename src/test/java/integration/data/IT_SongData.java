package integration.data;

public class IT_SongData {

    private final String title;
    private final String performer;
    private final String lyrics;

    public IT_SongData(String title, String performer, String lyrics) {
        this.title = title;
        this.performer = performer;
        this.lyrics = lyrics;
    }

    public String title() {
        return title;
    }

    public String performer() {
        return performer;
    }

    public String lyrics() {
        return lyrics;
    }
}
