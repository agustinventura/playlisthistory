package dev.agustinventura.playlisthistory.application.model;

public record Album(String id, String name) {

  public static final class AlbumBuilder {

    private String id;

    private String name;

    private AlbumBuilder() {
    }

    public static AlbumBuilder anAlbum() {
      return new AlbumBuilder();
    }

    public AlbumBuilder withId(String id) {
      this.id = id;
      return this;
    }

    public AlbumBuilder withName(String name) {
      this.name = name;
      return this;
    }

    public Album build() {
      return new Album(id, name);
    }
  }
}
