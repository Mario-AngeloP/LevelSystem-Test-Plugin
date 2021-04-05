package de.srsuders.levelsystem.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.srsuders.levelsystem.game.LevelPlayer;
import de.srsuders.levelsystem.handler.LevelPlayerHandler;
import de.srsuders.levelsystem.storage.Messages;
import de.srsuders.levelsystem.storage.PlayerCheck;

/**
 * Author: SrSuders aka. Mario-Angelo Date: 05.04.2021 Project: levelsystem
 */
public class LevelCommand implements CommandExecutor, Messages {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("Dieser Befehl steht nur Spielern zur Verfügung.");
			return false;
		}
		final Player p = (Player) sender;
		if (!p.hasPermission("levelsystem.admin")) {
			sender.sendMessage(prefix + "§cDafür hast du keine Berechtigung!");
			return false;
		}
		if (args.length == 3) {
			if (args[1].equalsIgnoreCase("add") || args[1].equalsIgnoreCase("remove")) {
				final PlayerCheck pc = new PlayerCheck(args[0]);
				if (!pc.existsPlayer()) {
					sender.sendMessage(playerNotExists.replaceAll("%player%", args[0]));
					return false;
				}
				int i;
				try {
					i = Integer.valueOf(args[2]);
				} catch (NumberFormatException exc) {
					sender.sendMessage(
							prefix + "§cDie angegebene Zeichenkette: " + args[2] + " ist keine rationale Zahl.");
					return false;
				}
				final LevelPlayer lp = LevelPlayerHandler.getLevelPlayer(pc.getUUID());
				if (args[1].equalsIgnoreCase("add")) {
					lp.addExp(i);
					sender.sendMessage(prefix + "Du hast dem Spieler §e" + args[0] + " " + i + " §aExp hinzugefügt.");
				} else {
					lp.removeExp(i);
					sender.sendMessage(prefix + "Du hast dem Spieler §e" + args[0] + " " + i + " §aExp entfernt.");
				}
				return false;
			}
		}
		sender.sendMessage(prefix
				+ "Syntax Error. Bitte Benutze: /level <Spieler> add/remove <Anzahl> um Exp hinzu zufügen oder zu entfernen.");
		return false;
	}
}
