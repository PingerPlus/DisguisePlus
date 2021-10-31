package net.pinger.disguise.user;

import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public interface UserManager {

    User getUser(UUID id);

    User getUser(Player player);

    List<? extends User> getOnlinePlayers();

}
