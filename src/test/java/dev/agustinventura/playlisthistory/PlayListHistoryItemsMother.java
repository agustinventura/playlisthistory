package dev.agustinventura.playlisthistory;

import dev.agustinventura.playlisthistory.application.model.Album;
import dev.agustinventura.playlisthistory.application.model.Album.AlbumBuilder;
import dev.agustinventura.playlisthistory.application.model.Artist;
import dev.agustinventura.playlisthistory.application.model.Artist.ArtistBuilder;
import dev.agustinventura.playlisthistory.application.model.PlayListHistoryItem;
import dev.agustinventura.playlisthistory.application.model.PlayListHistoryItem.PlayListHistoryItemBuilder;
import dev.agustinventura.playlisthistory.application.model.Track;
import dev.agustinventura.playlisthistory.application.model.Track.TrackBuilder;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Set;
import net.datafaker.Faker;

public class PlayListHistoryItemsMother {

  public static PlayListHistoryItem getRecentlyPlayed() {
    return PlayListHistoryItemBuilder.aPlayListHistoryItem().withId(getId()).withTrack(getTrack()).withPlayedAt(LocalDateTime.now()).build();
  }

  private static String getId() {
    String charSet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    SecureRandom random = new SecureRandom();
    StringBuilder sb = new StringBuilder(22);
    for (int i = 0; i < 22; i++) {
      int randomIndex = random.nextInt(charSet.length());
      sb.append(charSet.charAt(randomIndex));
    }
    return sb.toString();
  }

  private static Track getTrack() {
    Faker faker = new Faker();
    return TrackBuilder.aTrack().withId(faker.code().ean13()).withName(faker.bossaNova().song()).withAlbum(getAlbum()).withArtists(getArtists()).build();
  }

  private static Set<Artist> getArtists() {
    Faker faker = new Faker();
    return Set.of(ArtistBuilder.anArtist().withId(faker.code().ean13()).withName(faker.bossaNova().artist()).build());
  }

  private static Album getAlbum() {
    Faker faker = new Faker();
    return AlbumBuilder.anAlbum().withId(faker.code().ean13()).withName(faker.bossaNova().artist()).build();
  }
}