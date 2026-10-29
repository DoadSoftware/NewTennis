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
	        return "exhibitionReady";
	    } catch (IOException | IllegalArgumentException ex) {
	        model.addAttribute("error_message", "Unable to save Exhibition match. Please check the match details, filename and directory permissions.");
	        model.addAttribute("players", tennisService.getAllPlayer());
	        return "exhibitionSetup";
	    }
	}
	
	private String showExhibitionSet(String matchId, int setIndex, ModelMap model) {
	    try {
	        ExhibitionMatch exhibitionMatch = exhibitionMatchRepository.load(matchId);
	        if (exhibitionMatch.getSets() == null || exhibitionMatch.getSets().isEmpty() || setIndex < 0 || setIndex >= exhibitionMatch.getSets().size()) {
	            model.addAttribute("error_message", "Invalid Exhibition set selection.");
	            return "error";
	        }
	        model.addAttribute("exhibitionMatch", exhibitionMatch);
	        model.addAttribute("selectedSet", exhibitionMatch.getSets().get(setIndex));
	        model.addAttribute("selectedSetIndex", setIndex);
	        model.addAttribute("totalSets", exhibitionMatch.getSets().size());
	        return "exhibitionLogger";
	    } catch (IOException | IllegalArgumentException ex) {
	        model.addAttribute("error_message", "Unable to load Exhibition match.");
	        return "error";
	    }
	}
}
