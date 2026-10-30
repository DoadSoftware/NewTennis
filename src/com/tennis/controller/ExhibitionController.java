package com.tennis.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import com.tennis.model.ExhibitionMatch;
import com.tennis.model.ExhibitionSet;
import com.tennis.model.Player;
import com.tennis.service.ExhibitionMatchRepository;
import com.tennis.service.TennisService;
import org.springframework.beans.factory.annotation.Autowired;

@Controller
public class ExhibitionController {
	
	@Autowired
	private ExhibitionMatchRepository exhibitionMatchRepository;
	
    @Autowired
    private TennisService tennisService;

    @RequestMapping(value = "/exhibition/logger", method = RequestMethod.POST)
    public String exhibitionLogger(@RequestParam("matchId") String matchId, ModelMap model) {
        return showExhibitionSet(matchId, 0, model);
    }

    @RequestMapping(value = "/exhibition/logger/set", method = RequestMethod.POST)
    public String exhibitionLoggerSet(@RequestParam("matchId") String matchId, @RequestParam("setIndex") int setIndex, ModelMap model) {
        return showExhibitionSet(matchId, setIndex, model);
    }    
    
    @RequestMapping(value = "/exhibition/setup", method = RequestMethod.POST)
    public String exhibitionSetupPage(ModelMap model) {
        model.addAttribute("players", tennisService.getAllPlayer());
        return "exhibitionSetup";
    }


    
	@RequestMapping(value = "/exhibition/start", method = RequestMethod.POST)
	public String exhibitionStart(@RequestParam Map<String, String> params, HttpSession session, ModelMap model) 
	{
		String competitionName = params.getOrDefault("competitionName", "").trim();
	    String matchTitle = params.getOrDefault("matchTitle", "").trim();
	    String group1Name = params.getOrDefault("group1Name", "").trim();
	    String group2Name = params.getOrDefault("group2Name", "").trim();

	    if (competitionName.isEmpty() || matchTitle.isEmpty() || group1Name.isEmpty() || group2Name.isEmpty() || group1Name.equalsIgnoreCase(group2Name)) {
	        model.addAttribute("error_message", "Please enter valid match details and two different group names.");
	        model.addAttribute("players", tennisService.getAllPlayer());
	        return "exhibitionSetup";
	    }
	
	    String matchFileName = matchTitle.replaceAll("[^a-zA-Z0-9_-]+", "_").replaceAll("^_+|_+$", "");
	    if (matchFileName.isBlank()) matchFileName = "Exhibition_Match";
	    if (matchFileName.length() > 100) matchFileName = matchFileName.substring(0, 100);
	    
	    ExhibitionMatch exhibitionMatch = new ExhibitionMatch();
	    exhibitionMatch.setMatchId(UUID.randomUUID().toString());
	    exhibitionMatch.setCompetitionName(competitionName);
	    exhibitionMatch.setMatchTitle(matchTitle);
	    exhibitionMatch.setGroup1Name(group1Name);
	    exhibitionMatch.setGroup2Name(group2Name);
	    exhibitionMatch.setMatchStatus("NOT_STARTED");
	    exhibitionMatch.setOverallWinnerGroup(null);
	    exhibitionMatch.setMatchFileName(matchFileName.trim());
	    
	    for (int i = 0; params.containsKey("sets[" + i + "].setName"); i++) {
	        String prefix = "sets[" + i + "].";
	        String setName = params.getOrDefault(prefix + "setName", "").trim();
	        String setType = params.getOrDefault(prefix + "setType", "");
	        String matchStyle = params.getOrDefault(prefix + "matchStyle", "");
	
	        try {
	            int p1 = Integer.parseInt(params.getOrDefault(prefix + "group1Player1", ""));
	            int p2 = Integer.parseInt(params.getOrDefault(prefix + "group2Player1", ""));
	            List<Integer> group1 = new ArrayList<>();
	            List<Integer> group2 = new ArrayList<>();
	            group1.add(p1);
	            group2.add(p2);
	
	            if (matchStyle.equals("DOUBLES")) {
	                group1.add(Integer.parseInt(params.getOrDefault(prefix + "group1Player2", "")));
	                group2.add(Integer.parseInt(params.getOrDefault(prefix + "group2Player2", "")));
	            }
	
	            List<Integer> allPlayers = new ArrayList<>();
	            allPlayers.addAll(group1);
	            allPlayers.addAll(group2);
	
	            if (setName.isEmpty() || !(setType.equals("REGULAR") || setType.equals("SHOOTOUT")) || !(matchStyle.equals("SINGLES") || matchStyle.equals("DOUBLES")) || allPlayers.stream().distinct().count() != allPlayers.size()) {
	                model.addAttribute("error_message", "Set " + (i + 1) + " has invalid details or duplicate players.");
	                model.addAttribute("players", tennisService.getAllPlayer());
	                return "exhibitionSetup";
	            }
	
	            for (Integer playerId : allPlayers) {
	                if (tennisService.getPlayer(playerId) == null) {
	                    model.addAttribute("error_message", "Invalid player selected in Set " + (i + 1) + ".");
	                    model.addAttribute("players", tennisService.getAllPlayer());
	                    return "exhibitionSetup";
	                }
	            }

				ExhibitionSet set = new ExhibitionSet();
				set.setSetNumber(i + 1);
				set.setSetName(setName);
				set.setSetType(setType);
				set.setMatchStyle(matchStyle);
				set.setGroup1PlayerIds(group1);
				set.setGroup2PlayerIds(group2);
				set.setGroup1ServingPlayerId(group1.get(0));
				set.setGroup2ServingPlayerId(group2.get(0));
				set.setSetStatus("NOT_STARTED");
				exhibitionMatch.getSets().add(set);

	        } catch (NumberFormatException ex) {
	            model.addAttribute("error_message", "Invalid player selection in Set " + (i + 1) + ".");
	            model.addAttribute("players", tennisService.getAllPlayer());
	            return "exhibitionSetup";
	        }
	    }
	
	    if (exhibitionMatch.getSets().isEmpty()) {
	        model.addAttribute("error_message", "Please configure at least one set.");
	        model.addAttribute("players", tennisService.getAllPlayer());
	        return "exhibitionSetup";
	    }
	
	    try {
	        exhibitionMatchRepository.save(exhibitionMatch);
	        session.setAttribute("exhibitionMatch", exhibitionMatch);
	        model.addAttribute("exhibitionMatch", exhibitionMatch);
	        model.addAttribute("exhibitionMatches", exhibitionMatchRepository.findAll());
	        return "exhibitionReady";
	    } catch (IOException | IllegalArgumentException ex) {
	        model.addAttribute("error_message", "Unable to save Exhibition match. Please check the match details, filename and directory permissions.");
	        model.addAttribute("players", tennisService.getAllPlayer());
	        return "exhibitionSetup";
	    }
	}

	@RequestMapping(value = "/exhibition/logger/score/overwrite", method = RequestMethod.POST)
	public String overwriteExhibitionScore(@RequestParam("matchId") String matchId, @RequestParam("setIndex") int setIndex, @RequestParam("groupNumber") int groupNumber, @RequestParam("score") int score, ModelMap model) {
	    try {
	        ExhibitionMatch exhibitionMatch = exhibitionMatchRepository.load(matchId);
	        if (exhibitionMatch.getSets() == null || setIndex < 0 || setIndex >= exhibitionMatch.getSets().size() || (groupNumber != 1 && groupNumber != 2) || score < 0) {
	            model.addAttribute("error_message", "Invalid Exhibition score overwrite.");
	            return "error";
	        }
			ExhibitionSet selectedSet = exhibitionMatch.getSets().get(setIndex);
			if (!"IN_PROGRESS".equals(selectedSet.getSetStatus())) {
			    model.addAttribute("error_message", "Score overwrite is allowed only while the set is in progress.");
			    return "error";
			}
			if (groupNumber == 1) selectedSet.setGroup1Score(score);
			else selectedSet.setGroup2Score(score);

	        exhibitionMatchRepository.save(exhibitionMatch);
	        return showExhibitionSet(matchId, setIndex, model);

	    } catch (IOException | IllegalArgumentException ex) {
	        model.addAttribute("error_message", "Unable to overwrite Exhibition score. The score may not have been saved.");
	        return "error";
	    }
	}
	
	@RequestMapping(value = "/exhibition/logger/score", method = RequestMethod.POST)
	public String updateExhibitionScore(@RequestParam("matchId") String matchId, @RequestParam("setIndex") int setIndex, @RequestParam("groupNumber") int groupNumber, @RequestParam("action") String action, ModelMap model) {
	    try {
	        ExhibitionMatch exhibitionMatch = exhibitionMatchRepository.load(matchId);
	        if (exhibitionMatch.getSets() == null || setIndex < 0 || setIndex >= exhibitionMatch.getSets().size() || (groupNumber != 1 && groupNumber != 2) || !("INCREMENT".equals(action) || "DECREMENT".equals(action))) {
	            model.addAttribute("error_message", "Invalid Exhibition score update.");
	            return "error";
	        }
			ExhibitionSet selectedSet = exhibitionMatch.getSets().get(setIndex);
			if (!"IN_PROGRESS".equals(selectedSet.getSetStatus())) {
			    model.addAttribute("error_message", "Scoring is allowed only while the set is in progress.");
			    return "error";
			}
			int score = groupNumber == 1 ? selectedSet.getGroup1Score() : selectedSet.getGroup2Score();
	        if ("INCREMENT".equals(action)) {
	            if (score == Integer.MAX_VALUE) {
	                model.addAttribute("error_message", "Score cannot be increased further.");
	                return "error";
	            }
	            score++;
	        } else {
	            score = Math.max(0, score - 1);
	        }
	        if (groupNumber == 1) selectedSet.setGroup1Score(score);
	        else selectedSet.setGroup2Score(score);
	        exhibitionMatchRepository.save(exhibitionMatch);
	        return showExhibitionSet(matchId, setIndex, model);
	    } catch (IOException | IllegalArgumentException ex) {
	        model.addAttribute("error_message", "Unable to update Exhibition score. The score may not have been saved.");
	        return "error";
	    }
	}
	
	@RequestMapping(value = "/exhibition/logger/set/start", method = RequestMethod.POST)
	public String startExhibitionSet(@RequestParam("matchId") String matchId, @RequestParam("setIndex") int setIndex, ModelMap model) {
	    try {
	        ExhibitionMatch exhibitionMatch = exhibitionMatchRepository.load(matchId);
	        if (exhibitionMatch.getSets() == null || setIndex < 0 || setIndex >= exhibitionMatch.getSets().size()) {
	            model.addAttribute("error_message", "Invalid Exhibition set selection.");
	            return "error";
	        }
	        ExhibitionSet selectedSet = exhibitionMatch.getSets().get(setIndex);
	        if (!"NOT_STARTED".equals(selectedSet.getSetStatus())) {
	            model.addAttribute("error_message", "Only a set that has not started can be started.");
	            return "error";
	        }
	        selectedSet.setSetStatus("IN_PROGRESS");
	        selectedSet.setSetWinnerGroup(null);
	        exhibitionMatchRepository.save(exhibitionMatch);
	        return showExhibitionSet(matchId, setIndex, model);
	    } catch (IOException | IllegalArgumentException ex) {
	        model.addAttribute("error_message", "Unable to start Exhibition set.");
	        return "error";
	    }
	}

	@RequestMapping(value = "/exhibition/logger/set/reopen", method = RequestMethod.POST)
	public String reopenExhibitionSet(@RequestParam("matchId") String matchId, @RequestParam("setIndex") int setIndex, ModelMap model) {
	    try {
	        ExhibitionMatch exhibitionMatch = exhibitionMatchRepository.load(matchId);
	        if (exhibitionMatch.getSets() == null || setIndex < 0 || setIndex >= exhibitionMatch.getSets().size()) {
	            model.addAttribute("error_message", "Invalid Exhibition set selection.");
	            return "error";
	        }
	        ExhibitionSet selectedSet = exhibitionMatch.getSets().get(setIndex);
	        if (!"COMPLETED".equals(selectedSet.getSetStatus())) {
	            model.addAttribute("error_message", "Only a completed set can be reopened.");
	            return "error";
	        }
	        selectedSet.setSetStatus("IN_PROGRESS");
	        selectedSet.setSetWinnerGroup(null);
	        exhibitionMatchRepository.save(exhibitionMatch);
	        return showExhibitionSet(matchId, setIndex, model);
	    } catch (IOException | IllegalArgumentException ex) {
	        model.addAttribute("error_message", "Unable to reopen Exhibition set.");
	        return "error";
	    }
	}

	@RequestMapping(value = "/exhibition/logger/set/complete", method = RequestMethod.POST)
	public String completeExhibitionSet(@RequestParam("matchId") String matchId, @RequestParam("setIndex") int setIndex, @RequestParam("winnerGroup") String winnerGroup, ModelMap model) {
	    try {
	        ExhibitionMatch exhibitionMatch = exhibitionMatchRepository.load(matchId);
	        if (exhibitionMatch.getSets() == null || setIndex < 0 || setIndex >= exhibitionMatch.getSets().size()) {
	            model.addAttribute("error_message", "Invalid Exhibition set selection.");
	            return "error";
	        }
	        ExhibitionSet selectedSet = exhibitionMatch.getSets().get(setIndex);
	        if (!"IN_PROGRESS".equals(selectedSet.getSetStatus())) {
	            model.addAttribute("error_message", "Only an in-progress set can be completed.");
	            return "error";
	        }
	        String group1Name = exhibitionMatch.getGroup1Name();
	        String group2Name = exhibitionMatch.getGroup2Name();
	        if (winnerGroup == null || !(winnerGroup.equals(group1Name) || winnerGroup.equals(group2Name))) {
	            model.addAttribute("error_message", "Invalid set winner selection.");
	            return "error";
	        }
	        selectedSet.setSetWinnerGroup(winnerGroup);
	        selectedSet.setSetStatus("COMPLETED");
	        exhibitionMatchRepository.save(exhibitionMatch);
	        return showExhibitionSet(matchId, setIndex, model);
	    } catch (IOException | IllegalArgumentException ex) {
	        model.addAttribute("error_message", "Unable to complete Exhibition set.");
	        return "error";
	    }
	}
	
	@RequestMapping(value = "/exhibition/logger/match/complete", method = RequestMethod.POST)
	public String completeExhibitionMatch(@RequestParam("matchId") String matchId, @RequestParam("setIndex") int setIndex, @RequestParam("winnerGroup") String winnerGroup, ModelMap model) {
	    try {
	        ExhibitionMatch exhibitionMatch = exhibitionMatchRepository.load(matchId);
	        if (exhibitionMatch.getSets() == null || exhibitionMatch.getSets().isEmpty() || setIndex < 0 || setIndex >= exhibitionMatch.getSets().size()) {
	            model.addAttribute("error_message", "Invalid Exhibition match or set selection.");
	            return "error";
	        }
	        String group1Name = exhibitionMatch.getGroup1Name();
	        String group2Name = exhibitionMatch.getGroup2Name();
	        if (winnerGroup == null || !(winnerGroup.equals(group1Name) || winnerGroup.equals(group2Name))) {
	            model.addAttribute("error_message", "Invalid overall match winner selection.");
	            return "error";
	        }
	        exhibitionMatch.setOverallWinnerGroup(winnerGroup);
	        exhibitionMatch.setMatchStatus("COMPLETED");
	        exhibitionMatchRepository.save(exhibitionMatch);
	        return showExhibitionSet(matchId, setIndex, model);
	    } catch (IOException | IllegalArgumentException ex) {
	        model.addAttribute("error_message", "Unable to declare the overall Exhibition match winner.");
	        return "error";
	    }
	}
	
    @RequestMapping(value = "/exhibition/logger/player/replace", method = RequestMethod.POST)
    public String replaceExhibitionPlayer(@RequestParam("matchId") String matchId, @RequestParam("setIndex") int setIndex, @RequestParam("groupNumber") int groupNumber, @RequestParam("playerPosition") int playerPosition, @RequestParam("newPlayerId") int newPlayerId, ModelMap model) {
        try {
            ExhibitionMatch exhibitionMatch = exhibitionMatchRepository.load(matchId);
            if (exhibitionMatch.getSets() == null || setIndex < 0 || setIndex >= exhibitionMatch.getSets().size() || (groupNumber != 1 && groupNumber != 2) || playerPosition < 0) {
                model.addAttribute("error_message", "Invalid Exhibition player replacement request.");
                return "error";
            }
            ExhibitionSet selectedSet = exhibitionMatch.getSets().get(setIndex);
            List<Integer> selectedGroup = groupNumber == 1 ? selectedSet.getGroup1PlayerIds() : selectedSet.getGroup2PlayerIds();
            List<Integer> otherGroup = groupNumber == 1 ? selectedSet.getGroup2PlayerIds() : selectedSet.getGroup1PlayerIds();
            int expectedPlayerCount = "DOUBLES".equals(selectedSet.getMatchStyle()) ? 2 : "SINGLES".equals(selectedSet.getMatchStyle()) ? 1 : -1;
            if (expectedPlayerCount < 1 || selectedGroup == null || otherGroup == null || selectedGroup.size() != expectedPlayerCount || otherGroup.size() != expectedPlayerCount || playerPosition >= selectedGroup.size()) {
                model.addAttribute("error_message", "The selected group or player position is invalid.");
                return "error";
            }
            if (tennisService.getPlayer(newPlayerId) == null) {
                model.addAttribute("error_message", "The selected replacement player does not exist.");
                return "error";
            }
            if (selectedGroup.get(playerPosition).equals(newPlayerId)) {
                model.addAttribute("error_message", "Please select a different replacement player.");
                return "error";
            }
            if (selectedGroup.contains(newPlayerId) || otherGroup.contains(newPlayerId)) {
                model.addAttribute("error_message", "That player is already active in this set.");
                return "error";
            }
			Integer outgoingPlayerId = selectedGroup.get(playerPosition);
			if (groupNumber == 1) {
			    if (outgoingPlayerId.equals(selectedSet.getGroup1ServingPlayerId())) {
			        selectedSet.setGroup1ServingPlayerId(newPlayerId);
			    }
			} else {
			    if (outgoingPlayerId.equals(selectedSet.getGroup2ServingPlayerId())) {
			        selectedSet.setGroup2ServingPlayerId(newPlayerId);
			    }
			}
			selectedGroup.set(playerPosition, newPlayerId);
			exhibitionMatchRepository.save(exhibitionMatch);
			return showExhibitionSet(matchId, setIndex, model);
        } catch (IOException | IllegalArgumentException ex) {
            model.addAttribute("error_message", "Unable to replace Exhibition player. The substitution may not have been saved.");
            return "error";
        }
    }

	@RequestMapping(value = "/exhibition/logger/server", method = RequestMethod.POST)
	public String setExhibitionServer(@RequestParam("matchId") String matchId, @RequestParam("setIndex") int setIndex, @RequestParam("groupNumber") int groupNumber, @RequestParam("playerId") int playerId, ModelMap model) {
	    try {
	        ExhibitionMatch exhibitionMatch = exhibitionMatchRepository.load(matchId);
	        if (exhibitionMatch.getSets() == null || setIndex < 0 || setIndex >= exhibitionMatch.getSets().size() || (groupNumber != 1 && groupNumber != 2)) {
	            model.addAttribute("error_message", "Invalid Exhibition serving player request.");
	            return "error";
	        }
	        ExhibitionSet selectedSet = exhibitionMatch.getSets().get(setIndex);
	        if (!"IN_PROGRESS".equals(selectedSet.getSetStatus())) {
	            model.addAttribute("error_message", "Serving player can only be changed while the set is in progress.");
	            return "error";
	        }
	        List<Integer> selectedGroup = groupNumber == 1 ? selectedSet.getGroup1PlayerIds() : selectedSet.getGroup2PlayerIds();
	        if (selectedGroup == null || !selectedGroup.contains(playerId)) {
	            model.addAttribute("error_message", "The selected serving player is not active in this group.");
	            return "error";
	        }
	        if (tennisService.getPlayer(playerId) == null) {
	            model.addAttribute("error_message", "The selected serving player does not exist.");
	            return "error";
	        }	        
	        if (groupNumber == 1) selectedSet.setGroup1ServingPlayerId(playerId);
	        else selectedSet.setGroup2ServingPlayerId(playerId);
	        exhibitionMatchRepository.save(exhibitionMatch);
	        return showExhibitionSet(matchId, setIndex, model);
	    } catch (IOException | IllegalArgumentException ex) {
	        model.addAttribute("error_message", "Unable to update serving player.");
	        return "error";
	    }
	}
	
	private boolean repairExhibitionServingPlayers(ExhibitionSet selectedSet, ExhibitionMatch exhibitionMatch) throws IOException {
	    List<Integer> group1 = selectedSet.getGroup1PlayerIds();
	    List<Integer> group2 = selectedSet.getGroup2PlayerIds();
	    if (group1 == null || group1.isEmpty() || group2 == null || group2.isEmpty() || group1.stream().anyMatch(id -> id == null) || group2.stream().anyMatch(id -> id == null)) return false;
	    if (group1.stream().anyMatch(id -> tennisService.getPlayer(id) == null) || group2.stream().anyMatch(id -> tennisService.getPlayer(id) == null)) return false;
	    boolean changed = false;
	    Integer server1 = selectedSet.getGroup1ServingPlayerId();
	    Integer server2 = selectedSet.getGroup2ServingPlayerId();
	    if (server1 == null || !group1.contains(server1)) {
	        selectedSet.setGroup1ServingPlayerId(group1.get(0));
	        changed = true;
	    }
	    if (server2 == null || !group2.contains(server2)) {
	        selectedSet.setGroup2ServingPlayerId(group2.get(0));
	        changed = true;
	    }
	    if (changed) exhibitionMatchRepository.save(exhibitionMatch);
	    return true;
	}
    
	private String showExhibitionSet(String matchId, int setIndex, ModelMap model) {
	    try {
	        ExhibitionMatch exhibitionMatch = exhibitionMatchRepository.load(matchId);
	        if (exhibitionMatch.getSets() == null || exhibitionMatch.getSets().isEmpty() || setIndex < 0 || setIndex >= exhibitionMatch.getSets().size()) {
	            model.addAttribute("error_message", "Invalid Exhibition set selection.");
	            return "error";
	        }
			ExhibitionSet selectedSet = exhibitionMatch.getSets().get(setIndex);
			if (!repairExhibitionServingPlayers(selectedSet, exhibitionMatch)) {
			    model.addAttribute("error_message", "This set has an invalid or empty player roster. Please correct the set players before continuing.");
			    return "error";
			}
			
			List<String> group1PlayerNames = selectedSet.getGroup1PlayerIds().stream().map(this::getExhibitionPlayerName).collect(java.util.stream.Collectors.toList());
			List<String> group2PlayerNames = selectedSet.getGroup2PlayerIds().stream().map(this::getExhibitionPlayerName).collect(java.util.stream.Collectors.toList());
			model.addAttribute("group1PlayerNames", group1PlayerNames);
			model.addAttribute("group2PlayerNames", group2PlayerNames);
			
			model.addAttribute("exhibitionMatch", exhibitionMatch);
			model.addAttribute("selectedSet", selectedSet);
			model.addAttribute("selectedSetIndex", setIndex);
			model.addAttribute("totalSets", exhibitionMatch.getSets().size());
			model.addAttribute("players", tennisService.getAllPlayer());
			return "exhibitionLogger";
	    } catch (IOException | IllegalArgumentException ex) {
	        model.addAttribute("error_message", "Unable to load Exhibition match.");
	        return "error";
	    }
	}
	
	private String getExhibitionPlayerName(Integer playerId) {
	    if (playerId == null) return "Unknown player";
	    Player player = tennisService.getPlayer(playerId);
	    if (player == null) return "Player " + playerId;
	    String fullName = player.getFull_name();
	    if (fullName != null && !fullName.trim().isEmpty()) return fullName.trim();
	    String firstName = player.getFirstname() == null ? "" : player.getFirstname().trim();
	    String surname = player.getSurname() == null ? "" : player.getSurname().trim();
	    String name = (firstName + " " + surname).trim();
	    return name.isEmpty() ? "Player " + playerId : name;
	}	
}
