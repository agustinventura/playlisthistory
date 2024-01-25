package dev.agustinventura.playlisthistory.application.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

public record PlayListHistoryItem(String id, Track track, @JsonProperty("played_at") LocalDateTime playedAt) {

  public static final class PlayListHistoryItemBuilder {

    private String id;

    private Track track;

    private LocalDateTime playedAt;

    private PlayListHistoryItemBuilder() {
    }

    public static PlayListHistoryItemBuilder aPlayListHistoryItem() {
      return new PlayListHistoryItemBuilder();
    }

    public PlayListHistoryItemBuilder withId(String id) {
      this.id = id;
      return this;
    }

    public PlayListHistoryItemBuilder withTrack(Track track) {
      this.track = track;
      return this;
    }

    public PlayListHistoryItemBuilder withPlayedAt(LocalDateTime playedAt) {
      this.playedAt = playedAt;
      return this;
    }

    public PlayListHistoryItem build() {
      return new PlayListHistoryItem(id, track, playedAt);
    }
  }
}
