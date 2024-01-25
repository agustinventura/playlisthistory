package dev.agustinventura.playlisthistory.application.model;

import java.util.Set;

public record Track(String id, String name, Album album, Set<Artist> artists) {

  public static final class TrackBuilder {

    private String id;

    private String name;

    private Album album;

    private Set<Artist> artists;

    private TrackBuilder() {
    }

    public static TrackBuilder aTrack() {
      return new TrackBuilder();
    }

    public TrackBuilder withId(String id) {
      this.id = id;
      return this;
    }

    public TrackBuilder withName(String name) {
      this.name = name;
      return this;
    }

    public TrackBuilder withAlbum(Album album) {
      this.album = album;
      return this;
    }

    public TrackBuilder withArtists(Set<Artist> artists) {
      this.artists = artists;
      return this;
    }

    public Track build() {
      return new Track(id, name, album, artists);
    }
  }
}
