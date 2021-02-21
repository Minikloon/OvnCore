package net.minikloon.ovn.ovncore.libs.sidebar;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerSidebar {
    private final UUID playerId;
    private final SidebarLinesProvider linesProvider;

    private final Scoreboard bukkitBoard;
    private final Objective objective;

    private final List<Team> teamsCache = new ArrayList<>();

    public PlayerSidebar(UUID playerId, SidebarLinesProvider linesProvider, Scoreboard bukkitBoard, Objective objective) {
        this.playerId = playerId;
        this.linesProvider = linesProvider;
        this.bukkitBoard = bukkitBoard;
        this.objective = objective;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public Scoreboard getBukkitBoard() {
        return bukkitBoard;
    }

    private static final int LINES_LIMIT = 16;

    public void update(Player player) {
        objective.setDisplayName(linesProvider.getDisplayName(player));

        List<String> lines = linesProvider.getLines(player);
        if (lines.size() > LINES_LIMIT) {
            lines = lines.subList(0, LINES_LIMIT);
        }

        int extraTeamsCount = teamsCache.size() - lines.size();
        if (extraTeamsCount > 0) {
            List<Team> extraTeams = teamsCache.subList(teamsCache.size() - extraTeamsCount, teamsCache.size());
            extraTeams.forEach(team -> {
                bukkitBoard.resetScores(team.getName());
                team.unregister();
            });
            extraTeams.clear();
        }

        int missingTeams = lines.size() - teamsCache.size();
        for (int i = 0; i < missingTeams; ++i) {
            int teamIndex = teamsCache.size();
            int score = 15 - teamIndex;

            String teamId = ChatColor.values()[teamIndex].toString();
            Team team = bukkitBoard.registerNewTeam(teamId);
            team.setDisplayName("");
            team.addEntry(teamId);
            teamsCache.add(team);

            objective.getScore(teamId).setScore(score);
        }

        for (int i = 0; i < lines.size(); ++i) {
            String text = lines.get(i);
            teamsCache.get(i).setPrefix(text);
        }
    }
}
