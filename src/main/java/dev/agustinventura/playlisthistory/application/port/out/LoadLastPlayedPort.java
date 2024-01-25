package dev.agustinventura.playlisthistory.application.port.out;

import dev.agustinventura.playlisthistory.application.model.PlayListHistoryItem;
import java.util.List;
import java.util.Optional;

public interface LoadLastPlayedPort {

  List<PlayListHistoryItem> loadLastPlayedSongs();

  Optional<PlayListHistoryItem> loadPlayedSong(String id);
}
