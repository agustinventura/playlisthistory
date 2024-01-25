package dev.agustinventura.playlisthistory.application.model;

public record Artist(String id, String name) {

  public static final class ArtistBuilder {

    private String id;

    private String name;

    private ArtistBuilder() {
    }

    public static ArtistBuilder anArtist() {
      return new ArtistBuilder();
    }

    public ArtistBuilder withId(String id) {
      this.id = id;
      return this;
    }

    public ArtistBuilder withName(String name) {
      this.name = name;
      return this;
    }

    public Artist build() {
      return new Artist(id, name);
    }
  }
}
