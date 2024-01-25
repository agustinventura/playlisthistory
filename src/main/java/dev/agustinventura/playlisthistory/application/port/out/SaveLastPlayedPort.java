package dev.agustinventura.playlisthistory.application.port.out;

import dev.agustinventura.playlisthistory.application.model.PlayListHistoryItem;

public interface SaveLastPlayedPort {

  PlayListHistoryItem save (PlayListHistoryItem item);
}
