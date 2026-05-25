package com.tennis.broadcaster;

import com.tennis.containers.ScoreBug;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import jakarta.xml.bind.JAXBException;
import com.tennis.model.Fixture;
import com.tennis.model.LiveMatchStatsAPI;
import com.tennis.model.Match;
import com.tennis.model.NameSuper;
import com.tennis.model.Player;
import com.tennis.model.VariousText;
import com.tennis.service.TennisService;
import com.tennis.util.TennisFunctions;
import com.tennis.util.TennisUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tennis.containers.Scene;

public class ATP_2022 extends Scene {

	public String session_selected_broadcaster = "ATP_2022";
	public ScoreBug scorebug = new ScoreBug();
	public String which_graphics_onscreen = "";
	public boolean is_infobar = false;
	public long last_date = 0;
	public ObjectMapper objectMapper = new ObjectMapper();
	
	private String flag_path = "C:\\\\Images\\\\ATP\\\\Flag\\\\";
	private String left_photo_path = "C:\\\\Images\\\\ATP\\\\Left\\\\";
	private String right_photo_path = "C:\\\\Images\\\\ATP\\\\Right\\\\";
	
	public ATP_2022() {
		super();
	}

	public ScoreBug updateScoreBug(List<Scene> scenes, Match match, PrintWriter print_writer)
			throws InterruptedException, MalformedURLException, IOException {
		if (scorebug.isScorebug_on_screen() == true) {
			scorebug = populateScoreBug(true, scorebug, print_writer, scenes.get(0).getScene_path(), match,session_selected_broadcaster);
			
			if(match.getSets().get(match.getSets().size()-1).getGames().get(match.getSets().get(match.getSets().size()-1).getGames().size()-1).getGame_status().equalsIgnoreCase(TennisUtil.START)
				&& (match.getSets().get(match.getSets().size()-1).getGames().get(match.getSets().get(match.getSets().size()-1).getGames().size()-1).getHome_score().equalsIgnoreCase(TennisUtil.GAME)||
					match.getSets().get(match.getSets().size()-1).getGames().get(match.getSets().get(match.getSets().size()-1).getGames().size()-1).getAway_score().equalsIgnoreCase(TennisUtil.GAME)||
					match.getSets().get(match.getSets().size()-1).getGames().get(match.getSets().get(match.getSets().size()-1).getGames().size()-1).getHome_score().equalsIgnoreCase(TennisUtil.ADVANTAGE)||
					match.getSets().get(match.getSets().size()-1).getGames().get(match.getSets().get(match.getSets().size()-1).getGames().size()-1).getAway_score().equalsIgnoreCase(TennisUtil.ADVANTAGE))){
					
					scorebug = populateGameScore(false, scorebug, print_writer, match, session_selected_broadcaster);
					
			}else if(match.getSets().get(match.getSets().size()-1).getGames().get(match.getSets().get(match.getSets().size()-1).getGames().size()-1).getGame_type().equalsIgnoreCase(TennisUtil.NORMAL)
					&& match.getSets().get(match.getSets().size()-1).getGames().get(match.getSets().get(match.getSets().size()-1).getGames().size()-1).getGame_status().equalsIgnoreCase(TennisUtil.START)
					&& (Integer.valueOf(match.getSets().get(match.getSets().size()-1).getGames().get(match.getSets().get(match.getSets().size()-1).getGames().size()-1).getHome_score()) > 0
					|| Integer.valueOf(match.getSets().get(match.getSets().size()-1).getGames().get(match.getSets().get(match.getSets().size()-1).getGames().size()-1).getAway_score()) > 0)) {
				scorebug = populateGameScore(false, scorebug, print_writer, match, session_selected_broadcaster);
			}else if(match.getSets().get(match.getSets().size()-1).getGames().get(match.getSets().get(match.getSets().size()-1).getGames().size()-1).getGame_type().equalsIgnoreCase(TennisUtil.TIE_BREAK)
					&& match.getSets().get(match.getSets().size()-1).getGames().get(match.getSets().get(match.getSets().size()-1).getGames().size()-1).getGame_status().equalsIgnoreCase(TennisUtil.START)
					&& (Integer.valueOf(match.getSets().get(match.getSets().size()-1).getGames().get(match.getSets().get(match.getSets().size()-1).getGames().size()-1).getHome_score()) > 0
					|| Integer.valueOf(match.getSets().get(match.getSets().size()-1).getGames().get(match.getSets().get(match.getSets().size()-1).getGames().size()-1).getAway_score()) > 0)) {
				scorebug = populateGameScore(false, scorebug, print_writer, match, session_selected_broadcaster);
			}else {
				scorebug = populateGameScore(true, scorebug, print_writer, match, session_selected_broadcaster);
			}
			//scorebug = populateGameScore(true, scorebug, print_writer, match, session_selected_broadcaster);
		}
		return scorebug;
	}

	public Object ProcessGraphicOption(String whatToProcess, Match match, TennisService tennisService,
			PrintWriter print_writer, List<Scene> scenes, String valueToProcess)
			throws InterruptedException, NumberFormatException, MalformedURLException, IOException, JAXBException {
		switch (whatToProcess.toUpperCase()) {
		case "POPULATE-SCOREBUG": case "POPULATE-SCOREBUG_STATS": case "POPULATE-SCOREBUG_SET_STATS": case "POPULATE-SCOREBUG_HEADER": case "POPULATE-SCOREBUG_BAR_STATS":
		case "POPULATE-LT-MATCH_RESULTSINGLES": case "POPULATE-LT-MATCH_RESULTDOUBLES": case "POPULATE-LT-MATCHID": case "POPULATE-LT-MATCHID_DOUBLE": case "POPULATE-NAMESUPERDB":
		case "POPULATE-NAMESUPER-SP": case "POPULATE-NAMESUPER-DP": case "POPULATE-NAMESUPER-SP1": case "POPULATE-NAMESUPER-DP1": case "POPULATE-LT-MATCH_SCORESINGLES":
		case "POPULATE-SINGLE_LT_MATCHPROMO": case "POPULATE-LT_DOUBLE_MATCHPROMO": case "POPULATE-LT-MATCH_SCOREDOUBLES":
		case "POPULATE-MATCHID_DOUBLE": case "POPULATE-MATCHID": case "POPULATE-FF-MATCH_RESULTSINGLES": case "POPULATE-FF-MATCH_RESULTDOUBLES": case "POPULATE-SINGLE_MATCHPROMO":	
		case "POPULATE-DOUBLE_MATCHPROMO": case "POPULATE-MATCH_STATS": case "POPULATE-SPEED":
		case "POPULATE-CROSS":
			switch (whatToProcess.toUpperCase()) {
			
			case "POPULATE-SCOREBUG_STATS": case "POPULATE-SCOREBUG_SET_STATS": case "POPULATE-SCOREBUG_HEADER": case "POPULATE-SCOREBUG_BAR_STATS":
				 break;
			 
			case "POPULATE-SCOREBUG":
				scenes.get(0).scene_load(print_writer, session_selected_broadcaster);
				break;
			default:
				scenes.get(1).setScene_path(valueToProcess.split(",")[0]);
				scenes.get(1).scene_load(print_writer, session_selected_broadcaster);
				break;
			}
			switch (whatToProcess.toUpperCase()) {
			case "POPULATE-SCOREBUG":
				populateScoreBug(false, scorebug, print_writer, valueToProcess.split(",")[0], match,session_selected_broadcaster);
				break;
			case "POPULATE-SCOREBUG_HEADER":
				populateScoreBugHeader(false,scorebug,print_writer,valueToProcess.split(",")[0],match,session_selected_broadcaster);
				break;
			case "POPULATE-SCOREBUG_STATS":
				if(scorebug.getLast_scorebug_stat() != null && !scorebug.getLast_scorebug_stat().trim().isEmpty()) {
					switch(scorebug.getLast_scorebug_stat()) {
					case "doubleFault": case "ace": case "firstServeWon": case "secondServeWon": case "breakPointWon":
					case "setfirstServeWon": case "setsecondServeWon": case "setace": case "setdoubleFault": case "setbreakPointWon":
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*StatType1HeadOut START \0");
						TimeUnit.MICROSECONDS.sleep(800);
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*StatType1Out START \0");
						TimeUnit.SECONDS.sleep(1);
						break;
					case "firstServePoints": case "secondServePoints": case "totalPointsWon": case "returnPointsWon": case "breakPoint":
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*StatType2HeadOut START \0");
						TimeUnit.MICROSECONDS.sleep(800);
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*StatType2Out START \0");
						TimeUnit.SECONDS.sleep(1);
						break;
					}
					TimeUnit.MILLISECONDS.sleep(500);
					scorebug.setScorebug_stat(valueToProcess);
					populateScoreBugStats(false,scorebug,print_writer,match,session_selected_broadcaster);
				}else {
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*SoreAllOut START \0");
					TimeUnit.MILLISECONDS.sleep(500);
					scorebug.setScorebug_stat(valueToProcess);
					populateScoreBugStats(false,scorebug,print_writer,match,session_selected_broadcaster);
				}
				break;
			case "POPULATE-SCOREBUG_SET_STATS":
				if(scorebug.getLast_scorebug_stat() != null && !scorebug.getLast_scorebug_stat().trim().isEmpty()) {
					switch(scorebug.getLast_scorebug_stat()) {
					case "doubleFault": case "ace": case "firstServeWon": case "secondServeWon": case "breakPointWon":
					case "setfirstServeWon": case "setsecondServeWon": case "setace": case "setdoubleFault": case "setbreakPointWon":
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*StatType1HeadOut START \0");
						TimeUnit.MICROSECONDS.sleep(800);
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*StatType1Out START \0");
						TimeUnit.SECONDS.sleep(1);
						break;
					case "firstServePoints": case "secondServePoints": case "totalPointsWon": case "returnPointsWon": case "breakPoint":
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*StatType2HeadOut START \0");
						TimeUnit.MICROSECONDS.sleep(800);
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*StatType2Out START \0");
						TimeUnit.SECONDS.sleep(1);
						break;
					}
					TimeUnit.MILLISECONDS.sleep(500);
					scorebug.setScorebug_stat(valueToProcess);
					populateScoreBugStatsSet(false,scorebug,print_writer,match,session_selected_broadcaster);
				}else {
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*SoreAllOut START \0");
					TimeUnit.MILLISECONDS.sleep(500);
					scorebug.setScorebug_stat(valueToProcess);
					populateScoreBugStatsSet(false,scorebug,print_writer,match,session_selected_broadcaster);
				}
				break;
			case "POPULATE-SCOREBUG_BAR_STATS":
				if(scorebug.getLast_scorebug_stat() != null && !scorebug.getLast_scorebug_stat().trim().isEmpty()) {
					switch(scorebug.getLast_scorebug_stat()) {
					case "doubleFault": case "ace": case "firstServeWon": case "secondServeWon": case "breakPointWon":
					case "setfirstServeWon": case "setsecondServeWon": case "setace": case "setdoubleFault": case "setbreakPointWon":
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*StatType1HeadOut START \0");
						TimeUnit.MICROSECONDS.sleep(800);
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*StatType1Out START \0");
						TimeUnit.SECONDS.sleep(1);
						break;
					case "firstServePoints": case "secondServePoints": case "totalPointsWon": case "returnPointsWon": case "breakPoint":
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*StatType2HeadOut START \0");
						TimeUnit.MICROSECONDS.sleep(800);
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*StatType2Out START \0");
						TimeUnit.SECONDS.sleep(1);
						break;
					}
					TimeUnit.MILLISECONDS.sleep(500);
					scorebug.setScorebug_stat(valueToProcess);
					populateScoreBugStatsBar(false,scorebug,print_writer,match,session_selected_broadcaster);
				}else {
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*SoreAllOut START \0");
					TimeUnit.MILLISECONDS.sleep(500);
					scorebug.setScorebug_stat(valueToProcess);
					populateScoreBugStatsBar(false,scorebug,print_writer,match,session_selected_broadcaster);
				}
				break;
			case "POPULATE-LT-MATCH_SCORESINGLES":
				populateLtMatchScoreSingles(print_writer, valueToProcess.split(",")[0], match,session_selected_broadcaster);
				break;
			case "POPULATE-LT-MATCH_SCOREDOUBLES":
				populateLtMatchScoreDoubles(print_writer, valueToProcess.split(",")[0], match,session_selected_broadcaster);
				break;
			case "POPULATE-LT-MATCH_RESULTSINGLES":
				populateLtMatchResultSingles(print_writer, valueToProcess.split(",")[0], match,session_selected_broadcaster);
				break;
			case "POPULATE-FF-MATCH_RESULTSINGLES":
				populateFFMatchResultSingles(print_writer, valueToProcess.split(",")[0], match,session_selected_broadcaster);
				break;
			case "POPULATE-LT-MATCH_RESULTDOUBLES":
				populateLtMatchResultDoubles(print_writer, valueToProcess.split(",")[0], match,session_selected_broadcaster);
				break;
			case "POPULATE-FF-MATCH_RESULTDOUBLES":
				populateFFMatchResultDoubles(print_writer, valueToProcess.split(",")[0], match,session_selected_broadcaster);
				break;
			case "POPULATE-MATCHID_DOUBLE":
				populateMatchIdDouble(print_writer, valueToProcess.split(",")[0],match, session_selected_broadcaster);
				break;
			case "POPULATE-MATCHID":
				populateMatchId(print_writer, valueToProcess.split(",")[0],match, session_selected_broadcaster);
				break;
			case "POPULATE-SINGLE_MATCHPROMO":
				populateMatchPromo(print_writer, valueToProcess.split(",")[0],Integer.valueOf(valueToProcess.split(",")[1]),tennisService.getFixtures(),
						tennisService.getAllPlayer(),tennisService.getVariousTexts(),match, session_selected_broadcaster);
				break;
			case "POPULATE-SINGLE_LT_MATCHPROMO":
				populateLtMatchPromo(print_writer, valueToProcess.split(",")[0],Integer.valueOf(valueToProcess.split(",")[1]),tennisService.getFixtures(),
						tennisService.getAllPlayer(),tennisService.getVariousTexts(),match, session_selected_broadcaster);
				break;
			case "POPULATE-LT_DOUBLE_MATCHPROMO":
				populateLtMatchDoublePromo(print_writer, valueToProcess.split(",")[0],Integer.valueOf(valueToProcess.split(",")[1]),tennisService.getFixtures(),
						tennisService.getAllPlayer(),tennisService.getVariousTexts(),match, session_selected_broadcaster);
				break;
			case "POPULATE-DOUBLE_MATCHPROMO":
				populateMatchDoublePromo(print_writer, valueToProcess.split(",")[0],Integer.valueOf(valueToProcess.split(",")[1]),tennisService.getFixtures(),
						tennisService.getAllPlayer(),tennisService.getVariousTexts(),match, session_selected_broadcaster);
				break;
			case "POPULATE-LT-MATCHID":
				populateltMatchId(print_writer, valueToProcess.split(",")[0],match, session_selected_broadcaster);
				break;
			case "POPULATE-LT-MATCHID_DOUBLE":
				populateltMatchIdDouble(print_writer, valueToProcess.split(",")[0],match, session_selected_broadcaster);
				break;
			case "POPULATE-NAMESUPERDB":
				populateNameSuperDB(print_writer, valueToProcess.split(",")[0],Integer.valueOf(valueToProcess.split(",")[1]),tennisService.getNameSupers(),
						match, session_selected_broadcaster);
				break;
			case "POPULATE-NAMESUPER-SP":
				populateNameSuperSP(print_writer, valueToProcess.split(",")[0],Integer.valueOf(valueToProcess.split(",")[1]),tennisService.getAllPlayer(),
						tennisService.getVariousTexts(),match, session_selected_broadcaster);
				break;
			case "POPULATE-NAMESUPER-SP1":
				populateNameSuperSP1(print_writer, valueToProcess.split(",")[0],Integer.valueOf(valueToProcess.split(",")[1]),tennisService.getAllPlayer(),
						tennisService.getVariousTexts(),match, session_selected_broadcaster);
				break;
			case "POPULATE-NAMESUPER-DP":
				populateNameSuperDP(print_writer, valueToProcess.split(",")[0],valueToProcess.split(",")[1],tennisService.getAllPlayer(),
						tennisService.getVariousTexts(),match, session_selected_broadcaster);
				break;
			case "POPULATE-NAMESUPER-DP1":
				populateNameSuperDP1(print_writer, valueToProcess.split(",")[0],Integer.valueOf(valueToProcess.split(",")[1]),Integer.valueOf(valueToProcess.split(",")[2]),
						tennisService.getAllPlayer(),tennisService.getVariousTexts(),match, session_selected_broadcaster);
				break;
			case "POPULATE-CROSS":
				populateCross(print_writer, valueToProcess.split(",")[0],valueToProcess.split(",")[1],match, session_selected_broadcaster);
				break;
			case "POPULATE-SPEED":
				populateSpeed(print_writer, valueToProcess.split(",")[0],Integer.valueOf(valueToProcess.split(",")[1]),match, session_selected_broadcaster);
				break;
			case "POPULATE-MATCH_STATS":
				populateMatchStats(print_writer, valueToProcess.split(",")[0],match, session_selected_broadcaster);
				break;
			}
			
		case "NAMESUPER_GRAPHICS-OPTIONS": 
			return objectMapper.writeValueAsString(tennisService.getNameSupers()).toString();
		case "NAMESUPER-SP_GRAPHICS-OPTIONS": case "NAMESUPER-SP1_GRAPHICS-OPTIONS": case "NAMESUPER-DP1_GRAPHICS-OPTIONS":
			return objectMapper.writeValueAsString(tennisService.getAllPlayer()).toString();
		case "SINGLE-MATCHPROMO_GRAPHICS-OPTIONS": case "DOUBLE-MATCHPROMO_GRAPHICS-OPTIONS": case "SINGLE-LT_MATCHPROMO_GRAPHICS-OPTIONS": case "DOUBLE-LT_MATCHPROMO_GRAPHICS-OPTIONS":
			return objectMapper.writeValueAsString(TennisFunctions.processAllFixtures(tennisService)).toString();

		case "ANIMATE-IN-SCOREBUG":
		case "ANIMATE-LT-MATCH_RESULTSINGLES": case "ANIMATE-LT-MATCH_RESULTDOUBLES": case "ANIMATE-IN-LT_MATCHID": case "ANIMATE-IN-LT-MATCHID_DOUBLE": case "ANIMATE-LT-NAMESUPERDB":
		case "ANIMATE-LT-NAMESUPER_SP": case "ANIMATE-LT-NAMESUPER_DP": case "ANIMATE-LT-NAMESUPER_SP1": case "ANIMATE-LT-NAMESUPER_DP1":
		case "ANIMATE-LT-MATCH_SCORESINGLES": case "ANIMATE-LT-MATCH_SCOREDOUBLES": case "ANIMATE-LT-SINGLE_LT_MATCHPROMO": case "ANIMATE-LT-DOUBLE_LT_MATCHPROMO":
		case "ANIMATE-IN-MATCHID_DOUBLE": case "ANIMATE-IN-MATCHID": case "ANIMATE-FF-MATCH_RESULTSINGLES": case "ANIMATE-FF-MATCH_RESULTDOUBLES":
		case "ANIMATE-IN-SINGLE_MATCHPROMO": case "ANIMATE-IN-DOUBLE_MATCHPROMO": case "ANIMATE-MATCH_STATS":
		case "ANIMATE-LT-CROSS": case "ANIMATE-SPEED":
			
		case "CLEAR-ALL":
		case "ANIMATE-OUT-SCOREBUG": case "ANIMATE-OUT-SCOREBUG_STAT": case "ANIMATE-OUT-SCOREBUG_HEADER":
		
		case "ANIMATE-OUT":
			switch (whatToProcess.toUpperCase()) {

			case "ANIMATE-IN-SCOREBUG":
				AnimateInGraphics(print_writer, "SCOREBUG");
				TimeUnit.SECONDS.sleep(1);
				if(match.getSets().get(0).getGames().get(0).getGame_status().equalsIgnoreCase(TennisUtil.END)) {
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*SoreAllIn START \0");
					TimeUnit.SECONDS.sleep(1);
				}
				which_graphics_onscreen = "SCOREBUG";
				is_infobar = true;
				scorebug.setScorebug_on_screen(true);
				break;
			case "ANIMATE-MATCH_STATS":
				AnimateInGraphics(print_writer, "MATCH_STATS");
				which_graphics_onscreen = "MATCH_STATS";
				break;
			case "ANIMATE-LT-MATCH_RESULTSINGLES":
				AnimateInGraphics(print_writer, "LT-MATCH_RESULTSINGLES");
				which_graphics_onscreen = "LT-MATCH_RESULTSINGLES";
				break;
			case "ANIMATE-FF-MATCH_RESULTSINGLES":
				AnimateInGraphics(print_writer, "FF-MATCH_RESULTSINGLES");
				which_graphics_onscreen = "FF-MATCH_RESULTSINGLES";
				break;
			case "ANIMATE-LT-MATCH_RESULTDOUBLES":
				AnimateInGraphics(print_writer, "LT-MATCH_RESULTDOUBLES");
				which_graphics_onscreen = "LT-MATCH_RESULTDOUBLES";
				break;
			case "ANIMATE-FF-MATCH_RESULTDOUBLES":
				AnimateInGraphics(print_writer, "FF-MATCH_RESULTDOUBLES");
				which_graphics_onscreen = "FF-MATCH_RESULTDOUBLES";
				break;
			case "ANIMATE-IN-MATCHID_DOUBLE":
				AnimateInGraphics(print_writer, "MATCHID_DOUBLE");
				which_graphics_onscreen = "MATCHID_DOUBLE";
				break;
			case "ANIMATE-IN-MATCHID":
				AnimateInGraphics(print_writer, "MATCHID");
				which_graphics_onscreen = "MATCHID";
				break;
			case "ANIMATE-IN-LT_MATCHID":
				AnimateInGraphics(print_writer, "LT_MATCHID");
				which_graphics_onscreen = "LT_MATCHID";
				break;
			case "ANIMATE-IN-LT-MATCHID_DOUBLE":
				AnimateInGraphics(print_writer, "LT-MATCHID_DOUBLE");
				which_graphics_onscreen = "LT-MATCHID_DOUBLE";
				break;
			case "ANIMATE-LT-NAMESUPERDB":
				AnimateInGraphics(print_writer, "NAMESUPERDB");
				which_graphics_onscreen = "NAMESUPERDB";
				break;
			case "ANIMATE-LT-NAMESUPER_SP":
				AnimateInGraphics(print_writer, "NAMESUPER_SP");
				which_graphics_onscreen = "NAMESUPER_SP";
				break;
			case "ANIMATE-LT-NAMESUPER_DP":
				AnimateInGraphics(print_writer, "NAMESUPER_DP");
				which_graphics_onscreen = "NAMESUPER_DP";
				break;
			case "ANIMATE-LT-NAMESUPER_SP1":
				AnimateInGraphics(print_writer, "NAMESUPER_SP1");
				which_graphics_onscreen = "NAMESUPER_SP1";
				break;
			case "ANIMATE-LT-NAMESUPER_DP1":
				AnimateInGraphics(print_writer, "NAMESUPER_DP1");
				which_graphics_onscreen = "NAMESUPER_DP1";
				break;
			case "ANIMATE-LT-CROSS":
				AnimateInGraphics(print_writer, "CROSS");
				which_graphics_onscreen = "CROSS";
				break;
			case "ANIMATE-LT-MATCH_SCORESINGLES":
				AnimateInGraphics(print_writer, "MATCH_SCORESINGLES");
				which_graphics_onscreen = "MATCH_SCORESINGLES";
				break;
			case "ANIMATE-IN-SINGLE_MATCHPROMO":
				AnimateInGraphics(print_writer, "SINGLE_MATCHPROMO");
				which_graphics_onscreen = "SINGLE_MATCHPROMO";
				break;
			case "ANIMATE-IN-DOUBLE_MATCHPROMO":
				AnimateInGraphics(print_writer, "DOUBLE_MATCHPROMO");
				which_graphics_onscreen = "DOUBLE_MATCHPROMO";
				break;
			case "ANIMATE-LT-SINGLE_LT_MATCHPROMO":
				AnimateInGraphics(print_writer, "SINGLE_LT_MATCHPROMO");
				which_graphics_onscreen = "SINGLE_LT_MATCHPROMO";
				break;
			case "ANIMATE-LT-DOUBLE_LT_MATCHPROMO":
				AnimateInGraphics(print_writer, "DOUBLE_LT_MATCHPROMO");
				which_graphics_onscreen = "DOUBLE_LT_MATCHPROMO";
				break;
			case "ANIMATE-LT-MATCH_SCOREDOUBLES":
				AnimateInGraphics(print_writer, "MATCH_SCOREDOUBLES");
				which_graphics_onscreen = "MATCH_SCOREDOUBLES";
				break;
			case "ANIMATE-SPEED":
				AnimateInGraphics(print_writer, "SPEED");
				which_graphics_onscreen = "SPEED";
				break;
			case "CLEAR-ALL":
				print_writer.println("-1 SCENE CLEANUP\0");
				print_writer.println("-1 IMAGE CLEANUP\0");
				print_writer.println("-1 GEOM CLEANUP\0");
				print_writer.println("-1 FONT CLEANUP\0");

				print_writer.println("-1 IMAGE INFO\0");
				print_writer.println("-1 RENDERER SET_OBJECT SCENE*" + valueToProcess.split(",")[0] + "\0");

				print_writer.println("-1 RENDERER INITIALIZE\0");
				print_writer.println("-1 RENDERER*SCENE_DATA INITIALIZE\0");
				print_writer.println("-1 RENDERER*UPDATE SET 0\0");
				print_writer.println("-1 RENDERER*STAGE SHOW 0.0\0");

				print_writer.println("-1 RENDERER*UPDATE SET 1\0");

				print_writer.println("-1 RENDERER*FRONT_LAYER SET_OBJECT SCENE*/Default/ScoreBug-Single\0");

				print_writer.println("-1 RENDERER*FRONT_LAYER INITIALIZE\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*SCENE_DATA INITIALIZE\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*UPDATE SET 0\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE SHOW 0.0\0");

				print_writer.println("-1 RENDERER*FRONT_LAYER*UPDATE SET 1\0");

				print_writer.println("-1 SCENE CLEANUP\0");
				print_writer.println("-1 IMAGE CLEANUP\0");
				print_writer.println("-1 GEOM CLEANUP\0");
				print_writer.println("-1 FONT CLEANUP\0");

				which_graphics_onscreen = "";
				is_infobar = false;
				scorebug.setScorebug_on_screen(false);
				scorebug.setGame_in(false);

				break;
			case "ANIMATE-OUT-SCOREBUG_HEADER":
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*StatType1HeadOut START \0");
				break;
			case "ANIMATE-OUT-SCOREBUG_STAT":
				if(scorebug.getLast_scorebug_stat() != null && !scorebug.getLast_scorebug_stat().trim().isEmpty()) {
					//System.out.println(scorebug.getLast_scorebug_stat());
					switch(scorebug.getLast_scorebug_stat()) {
					case "doubleFault": case "ace": case "firstServeWon": case "secondServeWon": case "breakPointWon":
					case "setfirstServeWon": case "setsecondServeWon": case "setace": case "setdoubleFault": case "setbreakPointWon":
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*StatType1HeadOut START \0");
						TimeUnit.MICROSECONDS.sleep(800);
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*StatType1Out START \0");
						TimeUnit.SECONDS.sleep(1);
						break;
					case "firstServePoints": case "secondServePoints": case "totalPointsWon": case "returnPointsWon": case "breakPoint":
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*StatType2HeadOut START \0");
						TimeUnit.MICROSECONDS.sleep(800);
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*StatType2Out START \0");
						TimeUnit.SECONDS.sleep(1);
						break;
					}
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*SoreAllIn START \0");
				}
				scorebug.setLast_scorebug_stat("");
				scorebug.setScorebug_stat("");
				break;
			case "ANIMATE-OUT-SCOREBUG":
				if (is_infobar == true) {
					AnimateOutGraphics(print_writer, "SCOREBUG");
					is_infobar = false;
					scorebug.setScorebug_on_screen(false);
					scorebug.setGame_in(false);
				}
				break;
			
			case "ANIMATE-OUT":
				switch (which_graphics_onscreen) {
				case "LT-MATCH_RESULTSINGLES": case "LT-MATCH_RESULTDOUBLES": case "LT_MATCHID": case "LT-MATCHID_DOUBLE": case "NAMESUPERDB": case "NAMESUPER_SP": 
				case "NAMESUPER_DP":case "NAMESUPER_SP1": case "NAMESUPER_DP1": case "MATCH_SCORESINGLES": case "MATCH_SCOREDOUBLES": case "SINGLE_LT_MATCHPROMO": case "DOUBLE_LT_MATCHPROMO":
				case "MATCHID_DOUBLE": case "MATCHID": case "FF-MATCH_RESULTSINGLES": case "FF-MATCH_RESULTDOUBLES": case "SINGLE_MATCHPROMO": case "DOUBLE_MATCHPROMO":
				case "CROSS": case "MATCH_STATS": case "SPEED":
					AnimateOutGraphics(print_writer, which_graphics_onscreen);
					which_graphics_onscreen = "";
					break;
				}
				break;
			}
			break;
		}
		return null;
	}

	public void AnimateInGraphics(PrintWriter print_writer, String whichGraphic) throws InterruptedException {

		switch (whichGraphic) {
		case "SCOREBUG":
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*In START \0");
			break;
		case "LT-MATCH_RESULTSINGLES": case "LT-MATCH_RESULTDOUBLES": case "LT_MATCHID": case "LT-MATCHID_DOUBLE": case "NAMESUPERDB": case "NAMESUPER_SP": 
		case "NAMESUPER_DP": case "NAMESUPER_SP1": case "NAMESUPER_DP1": case "MATCH_SCORESINGLES": case "MATCH_SCOREDOUBLES":
		case "MATCHID_DOUBLE": case "MATCHID": case "FF-MATCH_RESULTSINGLES": case "FF-MATCH_RESULTDOUBLES": case "SINGLE_MATCHPROMO": case "DOUBLE_MATCHPROMO": case "SINGLE_LT_MATCHPROMO":
		case "DOUBLE_LT_MATCHPROMO": case "MATCH_STATS": case "SPEED":
		case "CROSS":
			print_writer.println("-1 RENDERER*STAGE*DIRECTOR*In START \0");
			break;
		}
	}

	public void AnimateOutGraphics(PrintWriter print_writer, String whichGraphic) {
		switch (whichGraphic.toUpperCase()) {
		case "SCOREBUG":
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*Out START \0");
			break;
		case "LT-MATCH_RESULTSINGLES": case "LT-MATCH_RESULTDOUBLES": case "LT_MATCHID": case "LT-MATCHID_DOUBLE": case "NAMESUPERDB": case "NAMESUPER_SP": 
		case "NAMESUPER_DP": case "NAMESUPER_SP1": case "NAMESUPER_DP1": case "MATCH_SCORESINGLES": case "MATCH_SCOREDOUBLES":
		case "MATCHID_DOUBLE": case "MATCHID": case "FF-MATCH_RESULTSINGLES": case "FF-MATCH_RESULTDOUBLES": case "SINGLE_MATCHPROMO": case "DOUBLE_MATCHPROMO": case "SINGLE_LT_MATCHPROMO":
		case "DOUBLE_LT_MATCHPROMO": case "MATCH_STATS": case "SPEED":
		case "CROSS":
			print_writer.println("-1 RENDERER*STAGE*DIRECTOR*Out START \0");
			break;
		}
	}
	
	public static List<String> getSpeed(String FileName) throws IOException {
		long last_date = 0;
		String filePath = TennisUtil.TENNIS_DIRECTORY + "/Speeds/" + FileName + ".txt";
        File file = new File(filePath);
        long lastModified = file.lastModified();
		
		List<String> allLines = new ArrayList<String>();
		if(new File(TennisUtil.TENNIS_DIRECTORY + "/Speeds/" + FileName + ".txt").exists()) {
			if(last_date == 0) {
				last_date = lastModified;
				allLines = Files.readAllLines(Paths.get(TennisUtil.TENNIS_DIRECTORY + "/Speeds/" + FileName + ".txt"));
				System.out.println("1 = " + allLines.get(1));
			}else if(last_date != 0 && last_date != lastModified) {
				last_date = lastModified;
				allLines = Files.readAllLines(Paths.get(TennisUtil.TENNIS_DIRECTORY + "/Speeds/" + FileName + ".txt"));
				System.out.println("2 = " + allLines.get(1));
			}
		}
		return allLines;
	}
	
	public ScoreBug populateScoreBug(boolean is_this_updating, ScoreBug scorebug, PrintWriter print_writer,String viz_sence_path, Match match, String selectedbroadcaster) throws IOException {
		if (match == null) {
			System.out.println("ERROR: ScoreBug -> Match is null");
		} else {
			getSpeed("SPEED");
			
			if (is_this_updating == false) {
				if (match.getMatchType().toUpperCase().equalsIgnoreCase(TennisUtil.SINGLES)) {
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tPlayerName1"+ " SET " + 
							match.getHomeFirstPlayer().getTicker_name() + "\0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tPlayerName2"+ " SET " + 
							match.getAwayFirstPlayer().getTicker_name() + "\0");
				} else if (match.getMatchType().toUpperCase().equalsIgnoreCase(TennisUtil.DOUBLES)) {
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tPlayerName1" + " SET " + 
							match.getHomeFirstPlayer().getTicker_name()+ " / " + match.getHomeSecondPlayer().getTicker_name() + "\0");
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tPlayerName2" + " SET " + 
							match.getAwayFirstPlayer().getTicker_name() + " / " + match.getAwaySecondPlayer().getTicker_name() + "\0");
				}
			}
			
			List<String> home_data = new ArrayList<String>();
			List<String> away_data = new ArrayList<String>();

			if (match.getSets() != null) {
				for (int i = 0; i <= match.getSets().size() - 1; i++) {
					int home = 0, away = 0;
					for (int j = 0; j <= match.getSets().get(i).getGames().size() - 1; j++) {
						if (match.getSets().get(i).getGames().get(j).getGame_winner() != null) {
							if (match.getSets().get(i).getGames().get(j).getGame_winner().toUpperCase()
									.equalsIgnoreCase("HOME")) {
								home = home + 1;
							} else if (match.getSets().get(i).getGames().get(j).getGame_winner().toUpperCase()
									.equalsIgnoreCase("AWAY")) {
								away = away + 1;
							}
						}
					}
					home_data.add(i, String.valueOf(home));
					away_data.add(i, String.valueOf(away));
				}
			}

			if (match.getSets() != null) {
				if(match.getSets().get(match.getSets().size()-1).getGames().get(match.getSets().get(match.getSets().size()-1).getGames().size()-1).getServing_player() == match.getHomeFirstPlayer().getPlayerId() ||
						match.getSets().get(match.getSets().size()-1).getGames().get(match.getSets().get(match.getSets().size()-1).getGames().size()-1).getServing_player() == match.getHomeSecondPlayer().getPlayerId()) {
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$MainScoreBug$ServiceDot*FUNCTION*Omo*vis_con SET 1 \0");
				}else if(match.getSets().get(match.getSets().size()-1).getGames().get(match.getSets().get(match.getSets().size()-1).getGames().size()-1).getServing_player() == match.getAwayFirstPlayer().getPlayerId() || 
						match.getSets().get(match.getSets().size()-1).getGames().get(match.getSets().get(match.getSets().size()-1).getGames().size()-1).getServing_player() == match.getAwaySecondPlayer().getPlayerId()) {
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$MainScoreBug$ServiceDot*FUNCTION*Omo*vis_con SET 2 \0");
				}else {
					print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$MainScoreBug$ServiceDot*FUNCTION*Omo*vis_con SET 0 \0");
				}
				for (int i = 0; i <= match.getSets().size() - 1; i++) {
					if (match.getSets().get(i).getSet_status().toUpperCase().equalsIgnoreCase("START")||match.getSets().get(i).getSet_status().toUpperCase().equalsIgnoreCase("END")) {
						if(i==0) {
							if(is_this_updating == false) {
								print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$TopGrp$ScoreGrp$TopScore$SetOmo*FUNCTION*Omo*vis_con SET 0 \0");
								print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$BottomGrp$ScoreGrp$BottomScore$SetOmo*FUNCTION*Omo*vis_con SET 0 \0");
							}
							if (home_data.get(0).equalsIgnoreCase("0") && away_data.get(0).equalsIgnoreCase("0")) {
								print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON "	+ "tTopRunningSet" + " SET " + "0" + "\0");
								print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomRunningSet" + " SET " + "0" + "\0");
							} else {
								print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON "+ "tTopRunningSet" + " SET " + home_data.get(0) + "\0");
								print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON "+ "tBottomRunningSet" + " SET " + away_data.get(0) + "\0");
							}
						}else if(i==1) {
							if(is_this_updating == false) {
								print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$TopGrp$ScoreGrp$TopScore$SetOmo*FUNCTION*Omo*vis_con SET 1 \0");
								print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$BottomGrp$ScoreGrp$BottomScore$SetOmo*FUNCTION*Omo*vis_con SET 1 \0");
								print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON "+ "tTopSetScore1" + " SET " + home_data.get(0) + "\0");
								print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON "+ "tBottomSetScore1" + " SET " + away_data.get(0) + "\0");
								
								print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopTieBreakScore1" + " SET " + "" + "\0");
								print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomTieBreakScore1" + " SET " + "" + "\0");
								
								if(Integer.valueOf(home_data.get(0)) > Integer.valueOf(away_data.get(0))) {
									print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON "+ "tTopSetStatus1" + " SET " + "1" + "\0");
									print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON "+ "tBottomSetStatus1" + " SET " + "0" + "\0");
								}else if(Integer.valueOf(home_data.get(0))  < Integer.valueOf(away_data.get(0))) {
									print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON "+ "tTopSetStatus1" + " SET " + "0" + "\0");
									print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON "+ "tBottomSetStatus1" + " SET " + "1" + "\0");
								}
								
								if(match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getGame_type().equalsIgnoreCase(TennisUtil.TIE_BREAK)&&
										match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getGame_status().equalsIgnoreCase(TennisUtil.END)) {
									//if(match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getGame_winner().equalsIgnoreCase(TennisUtil.HOME)) {
										print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomTieBreakScore1" + " SET " + 
												match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getAway_score() + "\0");
									//}else if(match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getGame_winner().equalsIgnoreCase(TennisUtil.AWAY)) {
										print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopTieBreakScore1" + " SET " + 
												match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getHome_score() + "\0");
									//}
								}
								
							}
							
							if (home_data.get(0).equalsIgnoreCase("0") && away_data.get(0).equalsIgnoreCase("0")) {
								print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON "	+ "tTopRunningSet" + " SET " + "0" + "\0");
								print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomRunningSet" + " SET " + "0" + "\0");
							} else {
								print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON "+ "tTopRunningSet" + " SET " + home_data.get(1) + "\0");
								print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON "+ "tBottomRunningSet" + " SET " + away_data.get(1) + "\0");
							}
						}else if(i==2) {
							if(is_this_updating == false) {
								print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$TopGrp$ScoreGrp$TopScore$SetOmo*FUNCTION*Omo*vis_con SET 2 \0");
								print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$BottomGrp$ScoreGrp$BottomScore$SetOmo*FUNCTION*Omo*vis_con SET 2 \0");
								print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON "+ "tTopSetScore1" + " SET " + home_data.get(0) + "\0");
								print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON "+ "tBottomSetScore1" + " SET " + away_data.get(0) + "\0");
								print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON "+ "tTopSetScore2" + " SET " + home_data.get(1) + "\0");
								print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON "+ "tBottomSetScore2" + " SET " + away_data.get(1) + "\0");
								
								print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopTieBreakScore1" + " SET " + "" + "\0");
								print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomTieBreakScore1" + " SET " + "" + "\0");
								print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopTieBreakScore2" + " SET " + "" + "\0");
								print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomTieBreakScore2" + " SET " + "" + "\0");
								
								if(Integer.valueOf(home_data.get(0)) > Integer.valueOf(away_data.get(0))) {
									print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON "+ "tTopSetStatus1" + " SET " + "1" + "\0");
									print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON "+ "tBottomSetStatus1" + " SET " + "0" + "\0");
								}else if(Integer.valueOf(home_data.get(0))  < Integer.valueOf(away_data.get(0))) {
									print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON "+ "tTopSetStatus1" + " SET " + "0" + "\0");
									print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON "+ "tBottomSetStatus1" + " SET " + "1" + "\0");
								}
								
								if(Integer.valueOf(home_data.get(1))  > Integer.valueOf(away_data.get(1))) {
									print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON "+ "tTopSetStatus2" + " SET " + "1" + "\0");
									print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON "+ "tBottomSetStatus2" + " SET " + "0" + "\0");
								}else if(Integer.valueOf(home_data.get(1))  < Integer.valueOf(away_data.get(1))) {
									print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON "+ "tTopSetStatus2" + " SET " + "0" + "\0");
									print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON "+ "tBottomSetStatus2" + " SET " + "1" + "\0");
								}
								
								if(match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getGame_type().equalsIgnoreCase(TennisUtil.TIE_BREAK)&&
										match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getGame_status().equalsIgnoreCase(TennisUtil.END)) {
									//if(match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getGame_winner().equalsIgnoreCase(TennisUtil.HOME)) {
										print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomTieBreakScore1" + " SET " + 
												match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getAway_score() + "\0");
									//}else if(match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getGame_winner().equalsIgnoreCase(TennisUtil.AWAY)) {
										print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopTieBreakScore1" + " SET " + 
												match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getHome_score() + "\0");
									//}
								}
								
								if(match.getSets().get(1).getGames().get(match.getSets().get(1).getGames().size()-1).getGame_type().equalsIgnoreCase(TennisUtil.TIE_BREAK)&&
										match.getSets().get(1).getGames().get(match.getSets().get(1).getGames().size()-1).getGame_status().equalsIgnoreCase(TennisUtil.END)) {
									//if(match.getSets().get(1).getGames().get(match.getSets().get(1).getGames().size()-1).getGame_winner().equalsIgnoreCase(TennisUtil.HOME)) {
										print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomTieBreakScore2" + " SET " + 
												match.getSets().get(1).getGames().get(match.getSets().get(1).getGames().size()-1).getAway_score() + "\0");
									//}else if(match.getSets().get(1).getGames().get(match.getSets().get(1).getGames().size()-1).getGame_winner().equalsIgnoreCase(TennisUtil.AWAY)) {
										print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopTieBreakScore2" + " SET " + 
												match.getSets().get(1).getGames().get(match.getSets().get(1).getGames().size()-1).getHome_score() + "\0");
									//}
								}
							}
							
							
							if(match.getMatchType().equalsIgnoreCase(TennisUtil.DOUBLES) && 
									match.getSets().get(2).getGames().get(0).getGame_type().equalsIgnoreCase(TennisUtil.TIE_BREAK)) {
								
								if(match.getSets().get(2).getGames().get(0).getHome_score().equalsIgnoreCase("0") && 
										match.getSets().get(2).getGames().get(0).getAway_score().equalsIgnoreCase("0")) {
									print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON "	+ "tTopRunningSet" + " SET " + "0" + "\0");
									print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomRunningSet" + " SET " + "0" + "\0");
								}else {
									print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON "+ "tTopRunningSet" + " SET " + match.getSets().get(2).getGames().get(0).getHome_score() + "\0");
									print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON "+ "tBottomRunningSet" + " SET " + match.getSets().get(2).getGames().get(0).getAway_score() + "\0");
								}
								
							}else {
								if (home_data.get(0).equalsIgnoreCase("0") && away_data.get(0).equalsIgnoreCase("0")) {
									print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON "	+ "tTopRunningSet" + " SET " + "0" + "\0");
									print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomRunningSet" + " SET " + "0" + "\0");
								} else {
									print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON "+ "tTopRunningSet" + " SET " + home_data.get(2) + "\0");
									print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON "+ "tBottomRunningSet" + " SET " + away_data.get(2) + "\0");
								}
							}
							
						}
					}
				}
			}
		}
		return scorebug;
	}
	public ScoreBug populateGameScore(boolean is_this_updating,ScoreBug scorebug, PrintWriter print_writer, Match match, String selectedbroadcaster) {
		
		if(is_this_updating == false && scorebug.isGame_in() == false) {
			if(match.getSets().get(0).getGames().get(0).getGame_status().equalsIgnoreCase(TennisUtil.START)) {
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*SoreAllIn START \0");
			}
			if(match.getSets().size() < 3) {
				print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*GameIn START \0");
			}else if(match.getSets().size() == 3) {
				if(!match.getSets().get(2).getGames().get(0).getGame_type().equalsIgnoreCase(TennisUtil.TIE_BREAK)) {
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*GameIn START \0");
				}
			}
			scorebug.setGame_in(true);
		}
		
		if (match.getSets().get(match.getSets().size() - 1).getGames().get(match.getSets().get(match.getSets().size() - 1).getGames().size()-1).getGame_status().
				equalsIgnoreCase(TennisUtil.START)) {
			
			if (match.getSets().get(match.getSets().size() - 1).getGames().get(match.getSets().get(match.getSets().size() - 1).getGames().size()-1).getHome_score().
					toUpperCase().equalsIgnoreCase(TennisUtil.GAME)) {
				
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON "+ "tTopGameScore" + " SET " + "40" + "\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON "+ "tBottomGameScore" + " SET " + 
						match.getSets().get(match.getSets().size() - 1).getGames().get(match.getSets().get(match.getSets().size() - 1).getGames().size()-1).getAway_score() + "\0");
				
			}else if (match.getSets().get(match.getSets().size() - 1).getGames().get(match.getSets().get(match.getSets().size() - 1).getGames().size()-1).
					getAway_score().toUpperCase().equalsIgnoreCase(TennisUtil.GAME)) {
				
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON "+ "tTopGameScore" + " SET "+ 
						match.getSets().get(match.getSets().size() - 1).getGames().get(match.getSets().get(match.getSets().size() - 1).getGames().size()-1).getHome_score() + "\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomGameScore" + " SET " + "40" + "\0");
				
			}else if (match.getSets().get(match.getSets().size() - 1).getGames().get(match.getSets().get(match.getSets().size() - 1).getGames().size()-1).getHome_score().
					toUpperCase().equalsIgnoreCase(TennisUtil.ADVANTAGE)) {
				
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON "+ "tTopGameScore" + " SET " + "AD" + "\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON "+ "tBottomGameScore" + " SET " + "" + "\0");
				
			}else if (match.getSets().get(match.getSets().size() - 1).getGames().get(match.getSets().get(match.getSets().size() - 1).getGames().size()-1).
					getAway_score().toUpperCase().equalsIgnoreCase(TennisUtil.ADVANTAGE)) {
				
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON "+ "tTopGameScore" + " SET "+ "" + "\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomGameScore" + " SET " + "AD" + "\0");
				
			}else {
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON "+ "tTopGameScore" + " SET " + 
						match.getSets().get(match.getSets().size() - 1).getGames().get(match.getSets().get(match.getSets().size() - 1).getGames().size()-1).getHome_score() + "\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON "+ "tBottomGameScore" + " SET "+ 
						match.getSets().get(match.getSets().size() - 1).getGames().get(match.getSets().get(match.getSets().size() - 1).getGames().size()-1).getAway_score() + "\0");
			}

		}else if(match.getSets().get(match.getSets().size() - 1).getGames().get(match.getSets().get(match.getSets().size() - 1).getGames().size()-1).getGame_status().
				equalsIgnoreCase(TennisUtil.END)) {
			if(scorebug.isGame_in() == true) {
				if(match.getSets().size() < 3) {
					print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*GameOut START \0");
				}else if(match.getSets().size() == 3) {
					if(!match.getSets().get(2).getGames().get(0).getGame_type().equalsIgnoreCase(TennisUtil.TIE_BREAK)) {
						print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*GameOut START \0");
					}
				}
				scorebug.setGame_in(false);
			}
			
		}
		
		return scorebug;
	}
	public ScoreBug populateScoreBugStats(boolean is_this_updating,ScoreBug scorebug, PrintWriter print_writer, Match match, String selectedbroadcaster) 
			throws MalformedURLException, IOException, InterruptedException {
		
		String link = "https://api.protennislive.com/feeds/MatchStats/" + match.getMatchId();
		LiveMatchStatsAPI ApiMatch = TennisFunctions.getMatchStatsApi(link);
		
		if(scorebug.getScorebug_stat().equalsIgnoreCase("firstServeWon")) {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tStatHeadType1"+ " SET " + "1st SERVE POINTS" + "\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopStatValueType1"+ " SET " + 
					ApiMatch.getPlayerTeam1().getSets().get(0).getStats().getServiceStats().getFirstServePointsWon().getDividend() + "\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomStatValueType1"+ " SET " + 
					ApiMatch.getPlayerTeam2().getSets().get(0).getStats().getServiceStats().getFirstServePointsWon().getDividend() + "\0");
		}else if(scorebug.getScorebug_stat().equalsIgnoreCase("secondServeWon")) {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tStatHeadType1"+ " SET " + "2nd SERVE POINTS" + "\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopStatValueType1"+ " SET " + 
					ApiMatch.getPlayerTeam1().getSets().get(0).getStats().getServiceStats().getSecondServePointsWon().getDividend() + "\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomStatValueType1"+ " SET " + 
					ApiMatch.getPlayerTeam2().getSets().get(0).getStats().getServiceStats().getSecondServePointsWon().getDividend() + "\0");
		}else if(scorebug.getScorebug_stat().equalsIgnoreCase(TennisUtil.ACE)) {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tStatHeadType1"+ " SET " + "ACES" + "\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopStatValueType1"+ " SET " + 
					ApiMatch.getPlayerTeam1().getSets().get(0).getStats().getServiceStats().getAces().getNumber() + "\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomStatValueType1"+ " SET " + 
					ApiMatch.getPlayerTeam2().getSets().get(0).getStats().getServiceStats().getAces().getNumber() + "\0");
		}else if(scorebug.getScorebug_stat().equalsIgnoreCase(TennisUtil.DOUBLE_FAULT)) {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tStatHeadType1"+ " SET " + "DOUBLE FAULTS" + "\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopStatValueType1"+ " SET " + 
					ApiMatch.getPlayerTeam1().getSets().get(0).getStats().getServiceStats().getDoubleFaults().getNumber() + "\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomStatValueType1"+ " SET " + 
					ApiMatch.getPlayerTeam2().getSets().get(0).getStats().getServiceStats().getDoubleFaults().getNumber() + "\0");
		}else if(scorebug.getScorebug_stat().equalsIgnoreCase("breakPointWon")) {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tStatHeadType1"+ " SET " + "BREAK POINTS WON" + "\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopStatValueType1"+ " SET " + 
					ApiMatch.getPlayerTeam1().getSets().get(0).getStats().getReturnStats().getBreakPointsConverted().getDividend() + "\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomStatValueType1"+ " SET " + 
					ApiMatch.getPlayerTeam2().getSets().get(0).getStats().getReturnStats().getBreakPointsConverted().getDividend() + "\0");
		}
		
		if(is_this_updating == false) {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$StatGrpAll$StatType1$StatValueGrp$Top$ScoreBg$LoseBg*ACTIVE SET 0 \0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$StatGrpAll$StatType1$StatValueGrp$Bottom$ScoreBg$LoseBg*ACTIVE SET 0 \0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$StatGrpAll$StatType1$StatValueGrp$Top$ScoreBg$WinBg*ACTIVE SET 1 \0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$StatGrpAll$StatType1$StatValueGrp$Bottom$ScoreBg$WinBg*ACTIVE SET 1 \0");
			
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*StatType1HeadIn START \0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*StatType1In START \0");
			TimeUnit.MILLISECONDS.sleep(4);
		}
		scorebug.setLast_scorebug_stat(scorebug.getScorebug_stat());
		return scorebug;
	}
	public ScoreBug populateScoreBugStatsSet(boolean is_this_updating,ScoreBug scorebug, PrintWriter print_writer, Match match, String selectedbroadcaster) 
			throws MalformedURLException, IOException, InterruptedException {
		
		String link = "https://api.protennislive.com/feeds/MatchStats/" + match.getMatchId();
		LiveMatchStatsAPI ApiMatch = TennisFunctions.getMatchStatsApi(link);
		
		if(scorebug.getScorebug_stat().equalsIgnoreCase("setfirstServeWon")) {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tStatHeadType1"+ " SET " + "1st SERVE POINTS" + "\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopStatValueType1"+ " SET " + 
					ApiMatch.getPlayerTeam1().getSets().get(ApiMatch.getPlayerTeam1().getSets().size()-1).getStats().getServiceStats().getFirstServePointsWon().getDividend() + "\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomStatValueType1"+ " SET " + 
					ApiMatch.getPlayerTeam2().getSets().get(ApiMatch.getPlayerTeam2().getSets().size()-1).getStats().getServiceStats().getFirstServePointsWon().getDividend() + "\0");
		}else if(scorebug.getScorebug_stat().equalsIgnoreCase("setsecondServeWon")) {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tStatHeadType1"+ " SET " + "2nd SERVE POINTS" + "\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopStatValueType1"+ " SET " + 
					ApiMatch.getPlayerTeam1().getSets().get(ApiMatch.getPlayerTeam1().getSets().size()-1).getStats().getServiceStats().getSecondServePointsWon().getDividend() + "\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomStatValueType1"+ " SET " + 
					ApiMatch.getPlayerTeam2().getSets().get(ApiMatch.getPlayerTeam2().getSets().size()-1).getStats().getServiceStats().getSecondServePointsWon().getDividend() + "\0");
		}else if(scorebug.getScorebug_stat().equalsIgnoreCase("setace")) {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tStatHeadType1"+ " SET " + "ACES" + "\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopStatValueType1"+ " SET " + 
					ApiMatch.getPlayerTeam1().getSets().get(ApiMatch.getPlayerTeam1().getSets().size()-1).getStats().getServiceStats().getAces().getNumber() + "\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomStatValueType1"+ " SET " + 
					ApiMatch.getPlayerTeam2().getSets().get(ApiMatch.getPlayerTeam2().getSets().size()-1).getStats().getServiceStats().getAces().getNumber() + "\0");
		}else if(scorebug.getScorebug_stat().equalsIgnoreCase("setdoubleFault")) {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tStatHeadType1"+ " SET " + "DOUBLE FAULTS" + "\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopStatValueType1"+ " SET " + 
					ApiMatch.getPlayerTeam1().getSets().get(ApiMatch.getPlayerTeam1().getSets().size()-1).getStats().getServiceStats().getDoubleFaults().getNumber() + "\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomStatValueType1"+ " SET " + 
					ApiMatch.getPlayerTeam2().getSets().get(ApiMatch.getPlayerTeam2().getSets().size()-1).getStats().getServiceStats().getDoubleFaults().getNumber() + "\0");
		}else if(scorebug.getScorebug_stat().equalsIgnoreCase("setbreakPointWon")) {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tStatHeadType1"+ " SET " + "BREAK POINTS WON" + "\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopStatValueType1"+ " SET " + 
					ApiMatch.getPlayerTeam1().getSets().get(ApiMatch.getPlayerTeam1().getSets().size()-1).getStats().getReturnStats().getBreakPointsConverted().getDividend() + "\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomStatValueType1"+ " SET " + 
					ApiMatch.getPlayerTeam2().getSets().get(ApiMatch.getPlayerTeam2().getSets().size()-1).getStats().getReturnStats().getBreakPointsConverted().getDividend() + "\0");
		}
		
		if(is_this_updating == false) {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$StatGrpAll$StatType1$StatValueGrp$Top$ScoreBg$LoseBg*ACTIVE SET 0 \0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$StatGrpAll$StatType1$StatValueGrp$Bottom$ScoreBg$LoseBg*ACTIVE SET 0 \0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$StatGrpAll$StatType1$StatValueGrp$Top$ScoreBg$WinBg*ACTIVE SET 1 \0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$StatGrpAll$StatType1$StatValueGrp$Bottom$ScoreBg$WinBg*ACTIVE SET 1 \0");
			
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*StatType1HeadIn START \0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*StatType1In START \0");
			TimeUnit.MILLISECONDS.sleep(4);
		}
		scorebug.setLast_scorebug_stat(scorebug.getScorebug_stat());
		return scorebug;
	}
	
	public ScoreBug populateScoreBugStatsBar(boolean is_this_updating,ScoreBug scorebug, PrintWriter print_writer, Match match, String selectedbroadcaster) throws JsonMappingException, JsonProcessingException, InterruptedException {
		String link = "https://api.protennislive.com/feeds/MatchStats/" + match.getMatchId();
		LiveMatchStatsAPI ApiMatch = TennisFunctions.getMatchStatsApi(link);
		
		if(ApiMatch.getPlayerTeam1().getSets().size() == 2) {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON "+ "vSetNumber" + " SET " + "1" + "\0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON "+ "tSetNumber1" + " SET " + "SET 1" + "\0");
		}else if(ApiMatch.getPlayerTeam1().getSets().size() == 3) {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON "+ "vSetNumber" + " SET " + "2" + "\0");
		}else if(ApiMatch.getPlayerTeam1().getSets().size() == 4) {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON "+ "vSetNumber" + " SET " + "3" + "\0");
			
			if(scorebug.getScorebug_stat().equalsIgnoreCase("firstServePoints")) {
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$StatGrpAll$StatType2$StatValueGrp$Set3$SetGrp1$BarGrp$noname*FUNCTION*BarValues*Bar_Value__1 SET " + 
						Math.round(((Double.valueOf(ApiMatch.getPlayerTeam1().getSets().get(1).getStats().getServiceStats().getFirstServePointsWon().getDividend())/
								Double.valueOf(ApiMatch.getPlayerTeam1().getSets().get(1).getStats().getServiceStats().getFirstServePointsWon().getDivisor()))*100)) + "\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$StatGrpAll$StatType2$StatValueGrp$Set3$SetGrp1$BarGrp$group*FUNCTION*BarValues*Bar_Value__1 SET " + 
								Math.round(((Double.valueOf(ApiMatch.getPlayerTeam1().getSets().get(1).getStats().getServiceStats().getFirstServePointsWon().getDividend())/
										Double.valueOf(ApiMatch.getPlayerTeam1().getSets().get(1).getStats().getServiceStats().getFirstServePointsWon().getDivisor()))*100)) + "\0");
				
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$StatGrpAll$StatType2$StatValueGrp$Set3$SetGrp1$BarGrp$noname*FUNCTION*BarValues*Bar_Value__2 SET " + 
						Math.round(((Double.valueOf(ApiMatch.getPlayerTeam2().getSets().get(1).getStats().getServiceStats().getFirstServePointsWon().getDividend())/
								Double.valueOf(ApiMatch.getPlayerTeam2().getSets().get(1).getStats().getServiceStats().getFirstServePointsWon().getDivisor()))*100)) + "\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$StatGrpAll$StatType2$StatValueGrp$Set3$SetGrp1$BarGrp$group*FUNCTION*BarValues*Bar_Value__2 SET " + 
								Math.round(((Double.valueOf(ApiMatch.getPlayerTeam2().getSets().get(1).getStats().getServiceStats().getFirstServePointsWon().getDividend())/
						Double.valueOf(ApiMatch.getPlayerTeam2().getSets().get(1).getStats().getServiceStats().getFirstServePointsWon().getDivisor()))*100)) + "\0");
				
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$StatGrpAll$StatType2$StatValueGrp$Set3$SetGrp2$BarGrp$noname*FUNCTION*BarValues*Bar_Value__1 SET " + 
						Math.round(((Double.valueOf(ApiMatch.getPlayerTeam1().getSets().get(2).getStats().getServiceStats().getFirstServePointsWon().getDividend())/
								Double.valueOf(ApiMatch.getPlayerTeam1().getSets().get(2).getStats().getServiceStats().getFirstServePointsWon().getDivisor()))*100)) + "\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$StatGrpAll$StatType2$StatValueGrp$Set3$SetGrp2$BarGrp$group*FUNCTION*BarValues*Bar_Value__1 SET " + 
								Math.round(((Double.valueOf(ApiMatch.getPlayerTeam1().getSets().get(2).getStats().getServiceStats().getFirstServePointsWon().getDividend())/
										Double.valueOf(ApiMatch.getPlayerTeam1().getSets().get(2).getStats().getServiceStats().getFirstServePointsWon().getDivisor()))*100)) + "\0");
				
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$StatGrpAll$StatType2$StatValueGrp$Set3$SetGrp2$BarGrp$noname*FUNCTION*BarValues*Bar_Value__2 SET " + 
						Math.round(((Double.valueOf(ApiMatch.getPlayerTeam2().getSets().get(2).getStats().getServiceStats().getFirstServePointsWon().getDividend())/
								Double.valueOf(ApiMatch.getPlayerTeam2().getSets().get(2).getStats().getServiceStats().getFirstServePointsWon().getDivisor()))*100)) + "\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$StatGrpAll$StatType2$StatValueGrp$Set3$SetGrp2$BarGrp$group*FUNCTION*BarValues*Bar_Value__2 SET " + 
								Math.round(((Double.valueOf(ApiMatch.getPlayerTeam2().getSets().get(2).getStats().getServiceStats().getFirstServePointsWon().getDividend())/
						Double.valueOf(ApiMatch.getPlayerTeam2().getSets().get(2).getStats().getServiceStats().getFirstServePointsWon().getDivisor()))*100)) + "\0");
				
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$StatGrpAll$StatType2$StatValueGrp$Set3$SetGrp3$BarGrp$noname*FUNCTION*BarValues*Bar_Value__1 SET " + 
						Math.round(((Double.valueOf(ApiMatch.getPlayerTeam1().getSets().get(3).getStats().getServiceStats().getFirstServePointsWon().getDividend())/
								Double.valueOf(ApiMatch.getPlayerTeam1().getSets().get(3).getStats().getServiceStats().getFirstServePointsWon().getDivisor()))*100)) + "\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$StatGrpAll$StatType2$StatValueGrp$Set3$SetGrp3$BarGrp$group*FUNCTION*BarValues*Bar_Value__1 SET " + 
								Math.round(((Double.valueOf(ApiMatch.getPlayerTeam1().getSets().get(3).getStats().getServiceStats().getFirstServePointsWon().getDividend())/
										Double.valueOf(ApiMatch.getPlayerTeam1().getSets().get(3).getStats().getServiceStats().getFirstServePointsWon().getDivisor()))*100)) + "\0");
				
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$StatGrpAll$StatType2$StatValueGrp$Set3$SetGrp3$BarGrp$noname*FUNCTION*BarValues*Bar_Value__2 SET " + 
						Math.round(((Double.valueOf(ApiMatch.getPlayerTeam2().getSets().get(3).getStats().getServiceStats().getFirstServePointsWon().getDividend())/
								Double.valueOf(ApiMatch.getPlayerTeam2().getSets().get(3).getStats().getServiceStats().getFirstServePointsWon().getDivisor()))*100)) + "\0");
				print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main$All$StatGrpAll$StatType2$StatValueGrp$Set3$SetGrp3$BarGrp$group*FUNCTION*BarValues*Bar_Value__2 SET " + 
								Math.round(((Double.valueOf(ApiMatch.getPlayerTeam2().getSets().get(3).getStats().getServiceStats().getFirstServePointsWon().getDividend())/
						Double.valueOf(ApiMatch.getPlayerTeam2().getSets().get(3).getStats().getServiceStats().getFirstServePointsWon().getDivisor()))*100)) + "\0");
			}
			
			
		}
		
		if(is_this_updating == false) {
			//print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*StatType2HeadIn START \0");
			print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*StatType2In START \0");
			TimeUnit.MILLISECONDS.sleep(4);
		}
		scorebug.setLast_scorebug_stat(scorebug.getScorebug_stat());
		return scorebug;
		
	}
	
	public void populateScoreBugHeader(boolean is_this_updating,ScoreBug scorebug,PrintWriter print_writer,String value,Match match,String selectedbroadcaster) {
		
		if(value.equalsIgnoreCase("match_point")) {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tStatHeadType1"+ " SET " + "MATCH POINT" + "\0");
		}else if(value.equalsIgnoreCase("set_point")) {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tStatHeadType1"+ " SET " + "SET POINT" + "\0");
		}else if(value.equalsIgnoreCase("break_point")) {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tStatHeadType1"+ " SET " + "BREAK POINT" + "\0");
		}else if(value.equalsIgnoreCase("tie_break")) {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tStatHeadType1"+ " SET " + "TIE-BREAK" + "\0");
		}else if(value.equalsIgnoreCase("deuce")) {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tStatHeadType1"+ " SET " + "DEUCE" + "\0");
		}else if(value.equalsIgnoreCase("match_tie_break")) {
			print_writer.println("-1 RENDERER*FRONT_LAYER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tStatHeadType1"+ " SET " + "MATCH TIE-BREAK" + "\0");
		}
		print_writer.println("-1 RENDERER*FRONT_LAYER*STAGE*DIRECTOR*StatType1HeadIn START \0");
	}	
	
	public void populateLtMatchScoreSingles(PrintWriter print_writer, String viz_sence_path, Match match,String selectedbroadcaster) {
		if (match == null) {
			System.out.println("ERROR: MATCH RESULT -> Match is null");
		} else {
			
			print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tHeader" + " SET " + match.getMatchIdent() + "\0");
			
			if(match.getHomeFirstPlayer().getSurname() == null) {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFirstName1" + " SET " + "" + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLastName1" + " SET " + match.getHomeFirstPlayer().getFirstname() + "\0");
			}else {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFirstName1" + " SET " + match.getHomeFirstPlayer().getFirstname() + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLastName1" + " SET " + match.getHomeFirstPlayer().getSurname() + "\0");
			}
			if(match.getHomeFirstPlayer().getNationality() == null) {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountry1" + " SET " + "" + "\0");
			}else {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountry1" + " SET " + match.getHomeFirstPlayer().getNationality() + "\0");
			}
			if(match.getHomeFirstPlayer().getRankingSingle() == 0) {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRank1" + " SET " + "" + "\0");
			}else {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRank1" + " SET " + match.getHomeFirstPlayer().getRankingSingle() + "\0");
			}
			
			if(match.getAwayFirstPlayer().getSurname() == null) {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFirstName2" + " SET " + "" + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLastName2" + " SET " + match.getAwayFirstPlayer().getFirstname() + "\0");
			}else {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFirstName2" + " SET " + match.getAwayFirstPlayer().getFirstname() + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLastName2" + " SET " + match.getAwayFirstPlayer().getSurname() + "\0");
			}
			if(match.getAwayFirstPlayer().getNationality() == null) {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountry2" + " SET " + "" + "\0");
			}else {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountry2" + " SET " + match.getAwayFirstPlayer().getNationality() + "\0");
			}
			if(match.getAwayFirstPlayer().getRankingSingle() == 0) {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRank2" + " SET " + "" + "\0");
			}else {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRank2" + " SET " + match.getAwayFirstPlayer().getRankingSingle() + "\0");
			}
			
			List<String> home_data = new ArrayList<String>();
			List<String> away_data = new ArrayList<String>();
			
			if(match.getSets() != null) {
				
				for (int i = 0; i <= match.getSets().size() - 1; i++) {
					int home = 0, away = 0;
					for (int j = 0; j <= match.getSets().get(i).getGames().size() - 1; j++) {
						if (match.getSets().get(i).getGames().get(j).getGame_winner() != null) {
							if (match.getSets().get(i).getGames().get(j).getGame_winner().equalsIgnoreCase(TennisUtil.HOME)) {
								home = home + 1;
							} else if (match.getSets().get(i).getGames().get(j).getGame_winner().equalsIgnoreCase(TennisUtil.AWAY)) {
								away = away + 1;
							}
						}
					}
					home_data.add(i, String.valueOf(home));
					away_data.add(i, String.valueOf(away));
				}		
			}
			
			print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopTieBreakScore1" + " SET " + "" + "\0");
			print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomTieBreakScore1" + " SET " + "" + "\0");
			print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopTieBreakScore2" + " SET " + "" + "\0");
			print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomTieBreakScore2" + " SET " + "" + "\0");
			print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopTieBreakScore3" + " SET " + "" + "\0");
			print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomTieBreakScore3" + " SET " + "" + "\0");
			
			if(match.getSets().size() <= 3) {
				if(match.getSets().size()==1) {
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vSetStatus" + " SET " + "6" + "\0");
					
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopSetScore1" + " SET " + home_data.get(0) + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomSetScore1" + " SET " + away_data.get(0) + "\0");
					
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopSetScore2" + " SET " + "" + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomSetScore2" + " SET " + "" + "\0");
				}else if(match.getSets().size() == 2) {
					
					if(Integer.valueOf(home_data.get(0)) > Integer.valueOf(away_data.get(0))) {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vSetStatus" + " SET " + "7" + "\0");
					}else if(Integer.valueOf(home_data.get(0)) < Integer.valueOf(away_data.get(0))) {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vSetStatus" + " SET " + "9" + "\0");
					}
					
					if(match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getGame_type().equalsIgnoreCase(TennisUtil.TIE_BREAK)&&
							match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getGame_status().equalsIgnoreCase(TennisUtil.END)) {
						//if(match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getGame_winner().equalsIgnoreCase(TennisUtil.HOME)) {
							print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomTieBreakScore1" + " SET " + 
									match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getAway_score() + "\0");
						//}else if(match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getGame_winner().equalsIgnoreCase(TennisUtil.AWAY)) {
							print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopTieBreakScore1" + " SET " + 
									match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getHome_score() + "\0");
						//}
					}
					
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopSetScore1" + " SET " + home_data.get(0) + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomSetScore1" + " SET " + away_data.get(0) + "\0");
					
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopSetScore2" + " SET " + home_data.get(1) + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomSetScore2" + " SET " + away_data.get(1)+ "\0");
				}else if(match.getSets().size() == 3) {
					if(Integer.valueOf(home_data.get(0)) > Integer.valueOf(away_data.get(0)) && Integer.valueOf(home_data.get(1)) < Integer.valueOf(away_data.get(1))) {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vSetStatus" + " SET " + "8" + "\0");
					}else if(Integer.valueOf(home_data.get(0)) < Integer.valueOf(away_data.get(0)) && Integer.valueOf(home_data.get(1)) > Integer.valueOf(away_data.get(1))) {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vSetStatus" + " SET " + "10" + "\0");
					}
					
					if(match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getGame_type().equalsIgnoreCase(TennisUtil.TIE_BREAK)&&
							match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getGame_status().equalsIgnoreCase(TennisUtil.END)) {
						//if(match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getGame_winner().equalsIgnoreCase(TennisUtil.HOME)) {
							print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomTieBreakScore1" + " SET " + 
									match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getAway_score() + "\0");
						//}else if(match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getGame_winner().equalsIgnoreCase(TennisUtil.AWAY)) {
							print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopTieBreakScore1" + " SET " + 
									match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getHome_score() + "\0");
						//}
					}
					
					if(match.getSets().get(1).getGames().get(match.getSets().get(1).getGames().size()-1).getGame_type().equalsIgnoreCase(TennisUtil.TIE_BREAK)&&
							match.getSets().get(1).getGames().get(match.getSets().get(1).getGames().size()-1).getGame_status().equalsIgnoreCase(TennisUtil.END)) {
						//if(match.getSets().get(1).getGames().get(match.getSets().get(1).getGames().size()-1).getGame_winner().equalsIgnoreCase(TennisUtil.HOME)) {
							print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomTieBreakScore2" + " SET " + 
									match.getSets().get(1).getGames().get(match.getSets().get(1).getGames().size()-1).getAway_score() + "\0");
						//}else if(match.getSets().get(1).getGames().get(match.getSets().get(1).getGames().size()-1).getGame_winner().equalsIgnoreCase(TennisUtil.AWAY)) {
							print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopTieBreakScore2" + " SET " + 
									match.getSets().get(1).getGames().get(match.getSets().get(1).getGames().size()-1).getHome_score() + "\0");
						//}
					}
					
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopSetScore1" + " SET " + home_data.get(0) + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomSetScore1" + " SET " + away_data.get(0) + "\0");
					
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopSetScore2" + " SET " + home_data.get(1) + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomSetScore2" + " SET " + away_data.get(1)+ "\0");
					
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopSetScore3" + " SET " + home_data.get(2) + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomSetScore3" + " SET " + away_data.get(2)+ "\0");
				}
			}
			
			
			print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_sence_path + " C:/Temp/Preview.png In 1.220 \0");
		}
	}
	public void populateLtMatchScoreDoubles(PrintWriter print_writer, String viz_sence_path, Match match,String selectedbroadcaster) {
		if (match == null) {
			System.out.println("ERROR: MATCH RESULT -> Match is null");
		} else {
			
			print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tHeader" + " SET " + match.getMatchIdent() + "\0");
			
			print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tNameA1" + " SET " + match.getHomeFirstPlayer().getTicker_name() + "\0");
			if(match.getHomeFirstPlayer().getNationality() == null) {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountryA1" + " SET " + "" + "\0");
			}else {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountryA1" + " SET " + match.getHomeFirstPlayer().getNationality() + "\0");
			}
			
			if(match.getHomeFirstPlayer().getRankingDouble() == 0) {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRankA1" + " SET " + "" + "\0");
			}else {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRankA1" + " SET " + match.getHomeFirstPlayer().getRankingDouble() + "\0");
			}
			
			print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tNameA2" + " SET " + match.getHomeSecondPlayer().getTicker_name() + "\0");
			if(match.getHomeSecondPlayer().getNationality() == null) {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountryA2" + " SET " + "" + "\0");
			}else {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountryA2" + " SET " + match.getHomeSecondPlayer().getNationality() + "\0");
			}
			if(match.getHomeSecondPlayer().getRankingDouble() == 0) {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRankA2" + " SET " + "" + "\0");
			}else {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRankA2" + " SET " + match.getHomeSecondPlayer().getRankingDouble() + "\0");
			}
			
			print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tNameB1" + " SET " + match.getAwayFirstPlayer().getTicker_name() + "\0");
			if(match.getAwayFirstPlayer().getNationality() == null) {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountryB1" + " SET " + "" + "\0");
			}else {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountryB1" + " SET " + match.getAwayFirstPlayer().getNationality() + "\0");
			}
			if(match.getAwayFirstPlayer().getRankingDouble() == 0) {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRankB1" + " SET " + "" + "\0");
			}else {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRankB1" + " SET " + match.getAwayFirstPlayer().getRankingDouble() + "\0");
			}
			
			print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tNameB2" + " SET " + match.getAwaySecondPlayer().getTicker_name() + "\0");
			if(match.getAwaySecondPlayer().getNationality() == null) {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountryB2" + " SET " + "" + "\0");
			}else {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountryB2" + " SET " + match.getAwaySecondPlayer().getNationality() + "\0");
			}
			if(match.getAwaySecondPlayer().getRankingDouble() == 0) {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRankB2" + " SET " + "" + "\0");
			}else {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRankB2" + " SET " + match.getAwaySecondPlayer().getRankingDouble() + "\0");
			}
			
			List<String> home_data = new ArrayList<String>();
			List<String> away_data = new ArrayList<String>();
			
			if(match.getSets() != null) {
				
				for (int i = 0; i <= match.getSets().size() - 1; i++) {
					int home = 0, away = 0;
					for (int j = 0; j <= match.getSets().get(i).getGames().size() - 1; j++) {
						if (match.getSets().get(i).getGames().get(j).getGame_winner() != null) {
							if (match.getSets().get(i).getGames().get(j).getGame_winner().equalsIgnoreCase(TennisUtil.HOME)) {
								home = home + 1;
							} else if (match.getSets().get(i).getGames().get(j).getGame_winner().equalsIgnoreCase(TennisUtil.AWAY)) {
								away = away + 1;
							}
						}
					}
					home_data.add(i, String.valueOf(home));
					away_data.add(i, String.valueOf(away));
				}		
			}
			
			print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopTieBreakScore1" + " SET " + "" + "\0");
			print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomTieBreakScore1" + " SET " + "" + "\0");
			print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopTieBreakScore2" + " SET " + "" + "\0");
			print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomTieBreakScore2" + " SET " + "" + "\0");
			print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopTieBreakScore3" + " SET " + "" + "\0");
			print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomTieBreakScore3" + " SET " + "" + "\0");
			
			if(match.getSets().size() <= 3) {
				if(match.getSets().size()==1) {
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vSetStatus" + " SET " + "6" + "\0");
					
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopSetScore1" + " SET " + home_data.get(0) + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomSetScore1" + " SET " + away_data.get(0) + "\0");
					
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopSetScore2" + " SET " + "" + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomSetScore2" + " SET " + "" + "\0");
				}else if(match.getSets().size() == 2) {
					
					if(Integer.valueOf(home_data.get(0)) > Integer.valueOf(away_data.get(0))) {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vSetStatus" + " SET " + "7" + "\0");
					}else if(Integer.valueOf(home_data.get(0)) < Integer.valueOf(away_data.get(0))) {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vSetStatus" + " SET " + "9" + "\0");
					}
					
					if(match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getGame_type().equalsIgnoreCase(TennisUtil.TIE_BREAK)&&
							match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getGame_status().equalsIgnoreCase(TennisUtil.END)) {
						//if(match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getGame_winner().equalsIgnoreCase(TennisUtil.HOME)) {
							print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomTieBreakScore1" + " SET " + 
									match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getAway_score() + "\0");
						//}else if(match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getGame_winner().equalsIgnoreCase(TennisUtil.AWAY)) {
							print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopTieBreakScore1" + " SET " + 
									match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getHome_score() + "\0");
						//}
					}
					
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopSetScore1" + " SET " + home_data.get(0) + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomSetScore1" + " SET " + away_data.get(0) + "\0");
					
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopSetScore2" + " SET " + home_data.get(1) + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomSetScore2" + " SET " + away_data.get(1)+ "\0");
				}else if(match.getSets().size() == 3) {
					if(Integer.valueOf(home_data.get(0)) > Integer.valueOf(away_data.get(0)) && Integer.valueOf(home_data.get(1)) < Integer.valueOf(away_data.get(1))) {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vSetStatus" + " SET " + "8" + "\0");
					}else if(Integer.valueOf(home_data.get(0)) < Integer.valueOf(away_data.get(0)) && Integer.valueOf(home_data.get(1)) > Integer.valueOf(away_data.get(1))) {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vSetStatus" + " SET " + "10" + "\0");
					}
					
					if(match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getGame_type().equalsIgnoreCase(TennisUtil.TIE_BREAK)&&
							match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getGame_status().equalsIgnoreCase(TennisUtil.END)) {
						//if(match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getGame_winner().equalsIgnoreCase(TennisUtil.HOME)) {
							print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomTieBreakScore1" + " SET " + 
									match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getAway_score() + "\0");
						//}else if(match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getGame_winner().equalsIgnoreCase(TennisUtil.AWAY)) {
							print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopTieBreakScore1" + " SET " + 
									match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getHome_score() + "\0");
						//}
					}
					
					if(match.getSets().get(1).getGames().get(match.getSets().get(1).getGames().size()-1).getGame_type().equalsIgnoreCase(TennisUtil.TIE_BREAK)&&
							match.getSets().get(1).getGames().get(match.getSets().get(1).getGames().size()-1).getGame_status().equalsIgnoreCase(TennisUtil.END)) {
						//if(match.getSets().get(1).getGames().get(match.getSets().get(1).getGames().size()-1).getGame_winner().equalsIgnoreCase(TennisUtil.HOME)) {
							print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomTieBreakScore2" + " SET " + 
									match.getSets().get(1).getGames().get(match.getSets().get(1).getGames().size()-1).getAway_score() + "\0");
						//}else if(match.getSets().get(1).getGames().get(match.getSets().get(1).getGames().size()-1).getGame_winner().equalsIgnoreCase(TennisUtil.AWAY)) {
							print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopTieBreakScore2" + " SET " + 
									match.getSets().get(1).getGames().get(match.getSets().get(1).getGames().size()-1).getHome_score() + "\0");
						//}
					}
					
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopSetScore1" + " SET " + home_data.get(0) + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomSetScore1" + " SET " + away_data.get(0) + "\0");
					
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopSetScore2" + " SET " + home_data.get(1) + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomSetScore2" + " SET " + away_data.get(1)+ "\0");
					
					
					if(match.getMatchType().equalsIgnoreCase(TennisUtil.DOUBLES) && 
							match.getSets().get(2).getGames().get(0).getGame_type().equalsIgnoreCase(TennisUtil.TIE_BREAK)) {
						
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopSetScore3" + " SET " + match.getSets().get(2).getGames().get(0).getHome_score() + "\0");
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomSetScore3" + " SET " + match.getSets().get(2).getGames().get(0).getAway_score() + "\0");
						
					}else {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopSetScore3" + " SET " + home_data.get(2) + "\0");
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomSetScore3" + " SET " + away_data.get(2)+ "\0");
					}
	
				}
			}
			
			
			print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_sence_path + " C:/Temp/Preview.png In 1.220 \0");
		}
	}
	
	public void populateLtMatchResultSingles(PrintWriter print_writer, String viz_sence_path, Match match,String selectedbroadcaster) {
		if (match == null) {
			System.out.println("ERROR: MATCH RESULT -> Match is null");
		} else {
			
			print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tHeader" + " SET " + match.getMatchIdent() + "\0");
			
			
			if(match.getHomeFirstPlayer().getSurname() == null) {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFirstName1" + " SET " + "" + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLastName1" + " SET " + match.getHomeFirstPlayer().getFirstname() + "\0");
			}else {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFirstName1" + " SET " + match.getHomeFirstPlayer().getFirstname() + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLastName1" + " SET " + match.getHomeFirstPlayer().getSurname() + "\0");
			}
			if(match.getHomeFirstPlayer().getNationality() == null) {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountry1" + " SET " + "" + "\0");
			}else {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountry1" + " SET " + match.getHomeFirstPlayer().getNationality() + "\0");
			}
			if(match.getHomeFirstPlayer().getRankingSingle() == 0) {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRank1" + " SET " + "" + "\0");
			}else {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRank1" + " SET " + match.getHomeFirstPlayer().getRankingSingle() + "\0");
			}
			
			if(match.getAwayFirstPlayer().getSurname() == null) {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFirstName2" + " SET " + "" + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLastName2" + " SET " + match.getAwayFirstPlayer().getFirstname() + "\0");
			}else {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFirstName2" + " SET " + match.getAwayFirstPlayer().getFirstname() + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLastName2" + " SET " + match.getAwayFirstPlayer().getSurname() + "\0");
			}
			if(match.getAwayFirstPlayer().getNationality() == null) {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountry2" + " SET " + "" + "\0");
			}else {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountry2" + " SET " + match.getAwayFirstPlayer().getNationality() + "\0");
			}
			if(match.getAwayFirstPlayer().getRankingSingle() == 0) {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRank2" + " SET " + "" + "\0");
			}else {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRank2" + " SET " + match.getAwayFirstPlayer().getRankingSingle() + "\0");
			}
			
			List<String> home_data = new ArrayList<String>();
			List<String> away_data = new ArrayList<String>();
			
			if(match.getSets() != null) {
				
				for (int i = 0; i <= match.getSets().size() - 1; i++) {
					int home = 0, away = 0;
					for (int j = 0; j <= match.getSets().get(i).getGames().size() - 1; j++) {
						if (match.getSets().get(i).getGames().get(j).getGame_winner() != null) {
							if (match.getSets().get(i).getGames().get(j).getGame_winner().equalsIgnoreCase(TennisUtil.HOME)) {
								home = home + 1;
							} else if (match.getSets().get(i).getGames().get(j).getGame_winner().equalsIgnoreCase(TennisUtil.AWAY)) {
								away = away + 1;
							}
						}
					}
					home_data.add(i, String.valueOf(home));
					away_data.add(i, String.valueOf(away));
				}
				
				if(match.getSets().size() == 2) {
					if(match.getSets().get(0).getSet_winner().equalsIgnoreCase(TennisUtil.HOME) && match.getSets().get(1).getSet_winner().equalsIgnoreCase(TennisUtil.HOME)) {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vSetStatus" + " SET " + "0" + "\0");
					}else if(match.getSets().get(0).getSet_winner().equalsIgnoreCase(TennisUtil.AWAY) && match.getSets().get(1).getSet_winner().equalsIgnoreCase(TennisUtil.AWAY)) {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vSetStatus" + " SET " + "5" + "\0");
					}
					
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopTieBreakScore1" + " SET " + "" + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomTieBreakScore1" + " SET " + "" + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopTieBreakScore2" + " SET " + "" + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomTieBreakScore2" + " SET " + "" + "\0");
					
					
					if(match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getGame_type().equalsIgnoreCase(TennisUtil.TIE_BREAK)&&
							match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getGame_status().equalsIgnoreCase(TennisUtil.END)) {
						//if(match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getGame_winner().equalsIgnoreCase(TennisUtil.HOME)) {
							print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomTieBreakScore1" + " SET " + 
									match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getAway_score() + "\0");
						//}else if(match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getGame_winner().equalsIgnoreCase(TennisUtil.AWAY)) {
							print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopTieBreakScore1" + " SET " + 
									match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getHome_score() + "\0");
						//}
					}
					
					if(match.getSets().get(1).getGames().get(match.getSets().get(1).getGames().size()-1).getGame_type().equalsIgnoreCase(TennisUtil.TIE_BREAK)&&
							match.getSets().get(1).getGames().get(match.getSets().get(1).getGames().size()-1).getGame_status().equalsIgnoreCase(TennisUtil.END)) {
						//if(match.getSets().get(1).getGames().get(match.getSets().get(1).getGames().size()-1).getGame_winner().equalsIgnoreCase(TennisUtil.HOME)) {
							print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomTieBreakScore2" + " SET " + 
									match.getSets().get(1).getGames().get(match.getSets().get(1).getGames().size()-1).getAway_score() + "\0");
						//}else if(match.getSets().get(1).getGames().get(match.getSets().get(1).getGames().size()-1).getGame_winner().equalsIgnoreCase(TennisUtil.AWAY)) {
							print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopTieBreakScore2" + " SET " + 
									match.getSets().get(1).getGames().get(match.getSets().get(1).getGames().size()-1).getHome_score() + "\0");
						//}
					}
					
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopSetScore1" + " SET " + home_data.get(0) + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopSetScore2" + " SET " + home_data.get(1) + "\0");
					
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomSetScore1" + " SET " + away_data.get(0) + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomSetScore2" + " SET " + away_data.get(1)+ "\0");
					
				}else if(match.getSets().size() == 3) {
					if(match.getSets().get(0).getSet_winner().equalsIgnoreCase(TennisUtil.HOME) && match.getSets().get(1).getSet_winner().equalsIgnoreCase(TennisUtil.AWAY)
							&& match.getSets().get(2).getSet_winner().equalsIgnoreCase(TennisUtil.HOME)) {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vSetStatus" + " SET " + "1" + "\0");
					}else if(match.getSets().get(0).getSet_winner().equalsIgnoreCase(TennisUtil.AWAY) && match.getSets().get(1).getSet_winner().equalsIgnoreCase(TennisUtil.HOME)
							&& match.getSets().get(2).getSet_winner().equalsIgnoreCase(TennisUtil.HOME)) {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vSetStatus" + " SET " + "2" + "\0");
					}else if(match.getSets().get(0).getSet_winner().equalsIgnoreCase(TennisUtil.HOME) && match.getSets().get(1).getSet_winner().equalsIgnoreCase(TennisUtil.AWAY)
							&& match.getSets().get(2).getSet_winner().equalsIgnoreCase(TennisUtil.AWAY)) {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vSetStatus" + " SET " + "3" + "\0");
					}else if(match.getSets().get(0).getSet_winner().equalsIgnoreCase(TennisUtil.AWAY) && match.getSets().get(1).getSet_winner().equalsIgnoreCase(TennisUtil.HOME)
							&& match.getSets().get(2).getSet_winner().equalsIgnoreCase(TennisUtil.AWAY)) {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vSetStatus" + " SET " + "4" + "\0");
					}
					
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopTieBreakScore1" + " SET " + "" + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomTieBreakScore1" + " SET " + "" + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopTieBreakScore2" + " SET " + "" + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomTieBreakScore2" + " SET " + "" + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopTieBreakScore3" + " SET " + "" + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomTieBreakScore3" + " SET " + "" + "\0");
					
					if(match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getGame_type().equalsIgnoreCase(TennisUtil.TIE_BREAK)&&
							match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getGame_status().equalsIgnoreCase(TennisUtil.END)) {
						//if(match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getGame_winner().equalsIgnoreCase(TennisUtil.HOME)) {
							print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomTieBreakScore1" + " SET " + 
									match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getAway_score() + "\0");
						//}else if(match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getGame_winner().equalsIgnoreCase(TennisUtil.AWAY)) {
							print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopTieBreakScore1" + " SET " + 
									match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getHome_score() + "\0");
						//}
					}
					
					if(match.getSets().get(1).getGames().get(match.getSets().get(1).getGames().size()-1).getGame_type().equalsIgnoreCase(TennisUtil.TIE_BREAK)&&
							match.getSets().get(1).getGames().get(match.getSets().get(1).getGames().size()-1).getGame_status().equalsIgnoreCase(TennisUtil.END)) {
						//if(match.getSets().get(1).getGames().get(match.getSets().get(1).getGames().size()-1).getGame_winner().equalsIgnoreCase(TennisUtil.HOME)) {
							print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomTieBreakScore2" + " SET " + 
									match.getSets().get(1).getGames().get(match.getSets().get(1).getGames().size()-1).getAway_score() + "\0");
						//}else if(match.getSets().get(1).getGames().get(match.getSets().get(1).getGames().size()-1).getGame_winner().equalsIgnoreCase(TennisUtil.AWAY)) {
							print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopTieBreakScore2" + " SET " + 
									match.getSets().get(1).getGames().get(match.getSets().get(1).getGames().size()-1).getHome_score() + "\0");
						//}
					}
					
					if(match.getSets().get(2).getGames().get(match.getSets().get(2).getGames().size()-1).getGame_type().equalsIgnoreCase(TennisUtil.TIE_BREAK)&&
							match.getSets().get(2).getGames().get(match.getSets().get(2).getGames().size()-1).getGame_status().equalsIgnoreCase(TennisUtil.END)) {
						//if(match.getSets().get(2).getGames().get(match.getSets().get(2).getGames().size()-1).getGame_winner().equalsIgnoreCase(TennisUtil.HOME)) {
							print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomTieBreakScore3" + " SET " + 
									match.getSets().get(2).getGames().get(match.getSets().get(2).getGames().size()-1).getAway_score() + "\0");
						//}else if(match.getSets().get(2).getGames().get(match.getSets().get(2).getGames().size()-1).getGame_winner().equalsIgnoreCase(TennisUtil.AWAY)) {
							print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopTieBreakScore3" + " SET " + 
									match.getSets().get(2).getGames().get(match.getSets().get(2).getGames().size()-1).getHome_score() + "\0");
						//}
					}
					
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopSetScore1" + " SET " + home_data.get(0) + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopSetScore2" + " SET " + home_data.get(1) + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopSetScore3" + " SET " + home_data.get(2) + "\0");
					
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomSetScore1" + " SET " + away_data.get(0) + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomSetScore2" + " SET " + away_data.get(1)+ "\0");
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomSetScore3" + " SET " + away_data.get(2)+ "\0");
				}
			}
			print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_sence_path + " C:/Temp/Preview.png In 1.220 \0");
		}
	}
	public void populateLtMatchResultDoubles(PrintWriter print_writer, String viz_sence_path, Match match,String selectedbroadcaster) {
		if (match == null) {
			System.out.println("ERROR: MATCH RESULT -> Match is null");
		} else {
			
			print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tHeader" + " SET " + match.getMatchIdent() + "\0");
			
			print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tNameA1" + " SET " + match.getHomeFirstPlayer().getTicker_name() + "\0");
			if(match.getHomeFirstPlayer().getNationality() == null) {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountryA1" + " SET " + "" + "\0");
			}else {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountryA1" + " SET " + match.getHomeFirstPlayer().getNationality() + "\0");
			}
			
			if(match.getHomeFirstPlayer().getRankingDouble() == 0) {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRankA1" + " SET " + "" + "\0");
			}else {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRankA1" + " SET " + match.getHomeFirstPlayer().getRankingDouble() + "\0");
			}
			
			print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tNameA2" + " SET " + match.getHomeSecondPlayer().getTicker_name() + "\0");
			if(match.getHomeSecondPlayer().getNationality() == null) {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountryA2" + " SET " + "" + "\0");
			}else {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountryA2" + " SET " + match.getHomeSecondPlayer().getNationality() + "\0");
			}
			if(match.getHomeSecondPlayer().getRankingDouble() == 0) {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRankA2" + " SET " + "" + "\0");
			}else {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRankA2" + " SET " + match.getHomeSecondPlayer().getRankingDouble() + "\0");
			}
			
			print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tNameB1" + " SET " + match.getAwayFirstPlayer().getTicker_name() + "\0");
			if(match.getAwayFirstPlayer().getNationality() == null) {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountryB1" + " SET " + "" + "\0");
			}else {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountryB1" + " SET " + match.getAwayFirstPlayer().getNationality() + "\0");
			}
			if(match.getAwayFirstPlayer().getRankingDouble() == 0) {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRankB1" + " SET " + "" + "\0");
			}else {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRankB1" + " SET " + match.getAwayFirstPlayer().getRankingDouble() + "\0");
			}
			
			print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tNameB2" + " SET " + match.getAwaySecondPlayer().getTicker_name() + "\0");
			if(match.getAwaySecondPlayer().getNationality() == null) {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountryB2" + " SET " + "" + "\0");
			}else {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountryB2" + " SET " + match.getAwaySecondPlayer().getNationality() + "\0");
			}
			if(match.getAwaySecondPlayer().getRankingDouble() == 0) {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRankB2" + " SET " + "" + "\0");
			}else {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRankB2" + " SET " + match.getAwaySecondPlayer().getRankingDouble() + "\0");
			}
			
			
			List<String> home_data = new ArrayList<String>();
			List<String> away_data = new ArrayList<String>();
			
			if(match.getSets() != null) {
				
				for (int i = 0; i <= match.getSets().size() - 1; i++) {
					int home = 0, away = 0;
					for (int j = 0; j <= match.getSets().get(i).getGames().size() - 1; j++) {
						if (match.getSets().get(i).getGames().get(j).getGame_winner() != null) {
							if (match.getSets().get(i).getGames().get(j).getGame_winner().equalsIgnoreCase(TennisUtil.HOME)) {
								home = home + 1;
							} else if (match.getSets().get(i).getGames().get(j).getGame_winner().equalsIgnoreCase(TennisUtil.AWAY)) {
								away = away + 1;
							}
						}
					}
					home_data.add(i, String.valueOf(home));
					away_data.add(i, String.valueOf(away));
				}
				
				if(match.getSets().size() == 2) {
					if(match.getSets().get(0).getSet_winner().equalsIgnoreCase(TennisUtil.HOME) && match.getSets().get(1).getSet_winner().equalsIgnoreCase(TennisUtil.HOME)) {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vSetStatus" + " SET " + "0" + "\0");
					}else if(match.getSets().get(0).getSet_winner().equalsIgnoreCase(TennisUtil.AWAY) && match.getSets().get(1).getSet_winner().equalsIgnoreCase(TennisUtil.AWAY)) {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vSetStatus" + " SET " + "5" + "\0");
					}
					
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopTieBreakScore1" + " SET " + "" + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomTieBreakScore1" + " SET " + "" + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopTieBreakScore2" + " SET " + "" + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomTieBreakScore2" + " SET " + "" + "\0");
					
					
					if(match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getGame_type().equalsIgnoreCase(TennisUtil.TIE_BREAK)&&
							match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getGame_status().equalsIgnoreCase(TennisUtil.END)) {
						//if(match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getGame_winner().equalsIgnoreCase(TennisUtil.HOME)) {
							print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomTieBreakScore1" + " SET " + 
									match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getAway_score() + "\0");
						//}else if(match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getGame_winner().equalsIgnoreCase(TennisUtil.AWAY)) {
							print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopTieBreakScore1" + " SET " + 
									match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getHome_score() + "\0");
						//}
					}
					
					if(match.getSets().get(1).getGames().get(match.getSets().get(1).getGames().size()-1).getGame_type().equalsIgnoreCase(TennisUtil.TIE_BREAK)&&
							match.getSets().get(1).getGames().get(match.getSets().get(1).getGames().size()-1).getGame_status().equalsIgnoreCase(TennisUtil.END)) {
						//if(match.getSets().get(1).getGames().get(match.getSets().get(1).getGames().size()-1).getGame_winner().equalsIgnoreCase(TennisUtil.HOME)) {
							print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomTieBreakScore2" + " SET " + 
									match.getSets().get(1).getGames().get(match.getSets().get(1).getGames().size()-1).getAway_score() + "\0");
						//}else if(match.getSets().get(1).getGames().get(match.getSets().get(1).getGames().size()-1).getGame_winner().equalsIgnoreCase(TennisUtil.AWAY)) {
							print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopTieBreakScore2" + " SET " + 
									match.getSets().get(1).getGames().get(match.getSets().get(1).getGames().size()-1).getHome_score() + "\0");
						//}
					}
					
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopSetScore1" + " SET " + home_data.get(0) + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopSetScore2" + " SET " + home_data.get(1) + "\0");
					
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomSetScore1" + " SET " + away_data.get(0) + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomSetScore2" + " SET " + away_data.get(1)+ "\0");
					
				}else if(match.getSets().size() == 3) {
					if(match.getSets().get(0).getSet_winner().equalsIgnoreCase(TennisUtil.HOME) && match.getSets().get(1).getSet_winner().equalsIgnoreCase(TennisUtil.AWAY)
							&& match.getSets().get(2).getGames().get(0).getGame_winner().equalsIgnoreCase(TennisUtil.HOME)) {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vSetStatus" + " SET " + "1" + "\0");
					}else if(match.getSets().get(0).getSet_winner().equalsIgnoreCase(TennisUtil.AWAY) && match.getSets().get(1).getSet_winner().equalsIgnoreCase(TennisUtil.HOME)
							&& match.getSets().get(2).getGames().get(0).getGame_winner().equalsIgnoreCase(TennisUtil.HOME)) {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vSetStatus" + " SET " + "2" + "\0");
					}else if(match.getSets().get(0).getSet_winner().equalsIgnoreCase(TennisUtil.HOME) && match.getSets().get(1).getSet_winner().equalsIgnoreCase(TennisUtil.AWAY)
							&& match.getSets().get(2).getGames().get(0).getGame_winner().equalsIgnoreCase(TennisUtil.AWAY)) {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vSetStatus" + " SET " + "3" + "\0");
					}else if(match.getSets().get(0).getSet_winner().equalsIgnoreCase(TennisUtil.AWAY) && match.getSets().get(1).getSet_winner().equalsIgnoreCase(TennisUtil.HOME)
							&& match.getSets().get(2).getGames().get(0).getGame_winner().equalsIgnoreCase(TennisUtil.AWAY)) {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vSetStatus" + " SET " + "4" + "\0");
					}
					
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopTieBreakScore1" + " SET " + "" + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomTieBreakScore1" + " SET " + "" + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopTieBreakScore2" + " SET " + "" + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomTieBreakScore2" + " SET " + "" + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopTieBreakScore3" + " SET " + "" + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomTieBreakScore3" + " SET " + "" + "\0");
					
					if(match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getGame_type().equalsIgnoreCase(TennisUtil.TIE_BREAK)&&
							match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getGame_status().equalsIgnoreCase(TennisUtil.END)) {
						//if(match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getGame_winner().equalsIgnoreCase(TennisUtil.HOME)) {
							print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomTieBreakScore1" + " SET " + 
									match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getAway_score() + "\0");
						//}else if(match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getGame_winner().equalsIgnoreCase(TennisUtil.AWAY)) {
							print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopTieBreakScore1" + " SET " + 
									match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getHome_score() + "\0");
						//}
					}
					
					if(match.getSets().get(1).getGames().get(match.getSets().get(1).getGames().size()-1).getGame_type().equalsIgnoreCase(TennisUtil.TIE_BREAK)&&
							match.getSets().get(1).getGames().get(match.getSets().get(1).getGames().size()-1).getGame_status().equalsIgnoreCase(TennisUtil.END)) {
						//if(match.getSets().get(1).getGames().get(match.getSets().get(1).getGames().size()-1).getGame_winner().equalsIgnoreCase(TennisUtil.HOME)) {
							print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomTieBreakScore2" + " SET " + 
									match.getSets().get(1).getGames().get(match.getSets().get(1).getGames().size()-1).getAway_score() + "\0");
						//}else if(match.getSets().get(1).getGames().get(match.getSets().get(1).getGames().size()-1).getGame_winner().equalsIgnoreCase(TennisUtil.AWAY)) {
							print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopTieBreakScore2" + " SET " + 
									match.getSets().get(1).getGames().get(match.getSets().get(1).getGames().size()-1).getHome_score() + "\0");
						//}
					}
					
					/*if(match.getSets().get(2).getGames().get(match.getSets().get(2).getGames().size()-1).getGame_type().equalsIgnoreCase(TennisUtil.TIE_BREAK)) {
						if(match.getSets().get(2).getGames().get(match.getSets().get(2).getGames().size()-1).getGame_winner().equalsIgnoreCase(TennisUtil.HOME)) {
							print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomTieBreakScore3" + " SET " + 
									match.getSets().get(2).getGames().get(match.getSets().get(2).getGames().size()-1).getAway_score() + "\0");
						}else if(match.getSets().get(2).getGames().get(match.getSets().get(2).getGames().size()-1).getGame_winner().equalsIgnoreCase(TennisUtil.AWAY)) {
							print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopTieBreakScore3" + " SET " + 
									match.getSets().get(2).getGames().get(match.getSets().get(2).getGames().size()-1).getAway_score() + "\0");
						}
					}*/
					
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopSetScore1" + " SET " + home_data.get(0) + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomSetScore1" + " SET " + away_data.get(0) + "\0");
					
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopSetScore2" + " SET " + home_data.get(1) + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomSetScore2" + " SET " + away_data.get(1)+ "\0");
					
					if(match.getMatchType().equalsIgnoreCase(TennisUtil.DOUBLES) && 
							match.getSets().get(2).getGames().get(0).getGame_type().equalsIgnoreCase(TennisUtil.TIE_BREAK)) {
						
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopSetScore3" + " SET " + match.getSets().get(2).getGames().get(0).getHome_score() + "\0");
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomSetScore3" + " SET " + match.getSets().get(2).getGames().get(0).getAway_score() + "\0");
						
					}else {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopSetScore3" + " SET " + home_data.get(2) + "\0");
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomSetScore3" + " SET " + away_data.get(2)+ "\0");
					}
				}
			}
			print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_sence_path + " C:/Temp/Preview.png In 1.220 \0");
		}
	}
	
	public void populateFFMatchResultSingles(PrintWriter print_writer, String viz_sence_path, Match match,String selectedbroadcaster) throws InterruptedException {
		if (match == null) {
			System.out.println("ERROR: Lt-Match -> Match is null");
		} else {
			
			print_writer.println("-1 RENDERER*TREE*$Main$All$TopGrp$HeaderGrp$Header*GEOM*TEXT SET " + match.getMatchIdent() + "\0");
			
			print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vResults" + " SET " + "1" + "\0");
			print_writer.println("-1 RENDERER*TREE*$Main$All$TopWithMask$TopGrp$HeaderGrp$Duration*ACTIVE SET 0 \0");
			
			if(match.getHomeFirstPlayer().getSurname() == null) {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFirstName1" + " SET " + "" + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLastName1" + " SET " + match.getHomeFirstPlayer().getFirstname() + "\0");
			}else {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFirstName1" + " SET " + match.getHomeFirstPlayer().getFirstname() + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLastName1" + " SET " + match.getHomeFirstPlayer().getSurname() + "\0");
			}
			if(match.getHomeFirstPlayer().getNationality() == null) {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountry1" + " SET " + "" + "\0");
			}else {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountry1" + " SET " + match.getHomeFirstPlayer().getNationality() + "\0");
			}
			
			print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgFlag1" + " SET " + flag_path + 
					match.getHomeFirstPlayer().getNationality() + TennisUtil.PNG_EXTENSION + "\0");
			
			print_writer.println("-1 RENDERER*TREE*$Main$All$BottomGrp$LeftPlayerGrp$Noggi*TEXTURE*IMAGE SET " + left_photo_path + 
					match.getHomeFirstPlayer().getPhoto() + TennisUtil.PNG_EXTENSION + "\0");
			
			print_writer.println("-1 RENDERER*TREE*$Main$All$BottomGrp$RightPlayerGrp$Noggi*TEXTURE*IMAGE SET " + right_photo_path + 
					match.getAwayFirstPlayer().getPhoto() + TennisUtil.PNG_EXTENSION + "\0");
			
			if(match.getAwayFirstPlayer().getSurname() == null) {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFirstName2" + " SET " + "" + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLastName2" + " SET " + match.getAwayFirstPlayer().getFirstname() + "\0");
			}else {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFirstName2" + " SET " + match.getAwayFirstPlayer().getFirstname() + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLastName2" + " SET " + match.getAwayFirstPlayer().getSurname() + "\0");
			}
			if(match.getAwayFirstPlayer().getNationality() == null) {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountry2" + " SET " + "" + "\0");
			}else {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountry2" + " SET " + match.getAwayFirstPlayer().getNationality() + "\0");
			}
			
			print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgFlag2" + " SET " + flag_path + 
					match.getAwayFirstPlayer().getNationality() + TennisUtil.PNG_EXTENSION + "\0");
			
			List<String> home_data = new ArrayList<String>();
			List<String> away_data = new ArrayList<String>();
			
			if(match.getSets() != null) {
				
				for (int i = 0; i <= match.getSets().size() - 1; i++) {
					int home = 0, away = 0;
					for (int j = 0; j <= match.getSets().get(i).getGames().size() - 1; j++) {
						if (match.getSets().get(i).getGames().get(j).getGame_winner() != null) {
							if (match.getSets().get(i).getGames().get(j).getGame_winner().equalsIgnoreCase(TennisUtil.HOME)) {
								home = home + 1;
							} else if (match.getSets().get(i).getGames().get(j).getGame_winner().equalsIgnoreCase(TennisUtil.AWAY)) {
								away = away + 1;
							}
						}
					}
					home_data.add(i, String.valueOf(home));
					away_data.add(i, String.valueOf(away));
				}
				
				if(match.getSets().size() == 2) {
					if(match.getSets().get(0).getSet_winner().equalsIgnoreCase(TennisUtil.HOME) && match.getSets().get(1).getSet_winner().equalsIgnoreCase(TennisUtil.HOME)) {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vSetStatus" + " SET " + "0" + "\0");
					}else if(match.getSets().get(0).getSet_winner().equalsIgnoreCase(TennisUtil.AWAY) && match.getSets().get(1).getSet_winner().equalsIgnoreCase(TennisUtil.AWAY)) {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vSetStatus" + " SET " + "5" + "\0");
					}
					
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopTieBreakScore1" + " SET " + "" + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomTieBreakScore1" + " SET " + "" + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopTieBreakScore2" + " SET " + "" + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomTieBreakScore2" + " SET " + "" + "\0");
					
					
					if(match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getGame_type().equalsIgnoreCase(TennisUtil.TIE_BREAK)&&
							match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getGame_status().equalsIgnoreCase(TennisUtil.END)) {
						//if(match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getGame_winner().equalsIgnoreCase(TennisUtil.HOME)) {
							print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomTieBreakScore1" + " SET " + 
									match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getAway_score() + "\0");
						//}else if(match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getGame_winner().equalsIgnoreCase(TennisUtil.AWAY)) {
							print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopTieBreakScore1" + " SET " + 
									match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getHome_score() + "\0");
						//}
					}
					
					if(match.getSets().get(1).getGames().get(match.getSets().get(1).getGames().size()-1).getGame_type().equalsIgnoreCase(TennisUtil.TIE_BREAK)&&
							match.getSets().get(1).getGames().get(match.getSets().get(1).getGames().size()-1).getGame_status().equalsIgnoreCase(TennisUtil.END)) {
						//if(match.getSets().get(1).getGames().get(match.getSets().get(1).getGames().size()-1).getGame_winner().equalsIgnoreCase(TennisUtil.HOME)) {
							print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomTieBreakScore2" + " SET " + 
									match.getSets().get(1).getGames().get(match.getSets().get(1).getGames().size()-1).getAway_score() + "\0");
						//}else if(match.getSets().get(1).getGames().get(match.getSets().get(1).getGames().size()-1).getGame_winner().equalsIgnoreCase(TennisUtil.AWAY)) {
							print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopTieBreakScore2" + " SET " + 
									match.getSets().get(1).getGames().get(match.getSets().get(1).getGames().size()-1).getHome_score() + "\0");
						//}
					}
					
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopSetScore1" + " SET " + home_data.get(0) + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopSetScore2" + " SET " + home_data.get(1) + "\0");
					
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomSetScore1" + " SET " + away_data.get(0) + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomSetScore2" + " SET " + away_data.get(1)+ "\0");
					
				}else if(match.getSets().size() == 3) {
					if(match.getSets().get(0).getSet_winner().equalsIgnoreCase(TennisUtil.HOME) && match.getSets().get(1).getSet_winner().equalsIgnoreCase(TennisUtil.AWAY)
							&& match.getSets().get(2).getSet_winner().equalsIgnoreCase(TennisUtil.HOME)) {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vSetStatus" + " SET " + "1" + "\0");
					}else if(match.getSets().get(0).getSet_winner().equalsIgnoreCase(TennisUtil.AWAY) && match.getSets().get(1).getSet_winner().equalsIgnoreCase(TennisUtil.HOME)
							&& match.getSets().get(2).getSet_winner().equalsIgnoreCase(TennisUtil.HOME)) {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vSetStatus" + " SET " + "2" + "\0");
					}else if(match.getSets().get(0).getSet_winner().equalsIgnoreCase(TennisUtil.HOME) && match.getSets().get(1).getSet_winner().equalsIgnoreCase(TennisUtil.AWAY)
							&& match.getSets().get(2).getSet_winner().equalsIgnoreCase(TennisUtil.AWAY)) {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vSetStatus" + " SET " + "3" + "\0");
					}else if(match.getSets().get(0).getSet_winner().equalsIgnoreCase(TennisUtil.AWAY) && match.getSets().get(1).getSet_winner().equalsIgnoreCase(TennisUtil.HOME)
							&& match.getSets().get(2).getSet_winner().equalsIgnoreCase(TennisUtil.AWAY)) {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vSetStatus" + " SET " + "4" + "\0");
					}
					
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopTieBreakScore1" + " SET " + "" + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomTieBreakScore1" + " SET " + "" + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopTieBreakScore2" + " SET " + "" + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomTieBreakScore2" + " SET " + "" + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopTieBreakScore3" + " SET " + "" + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomTieBreakScore3" + " SET " + "" + "\0");
					
					if(match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getGame_type().equalsIgnoreCase(TennisUtil.TIE_BREAK)&&
							match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getGame_status().equalsIgnoreCase(TennisUtil.END)) {
						//if(match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getGame_winner().equalsIgnoreCase(TennisUtil.HOME)) {
							print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomTieBreakScore1" + " SET " + 
									match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getAway_score() + "\0");
						//}else if(match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getGame_winner().equalsIgnoreCase(TennisUtil.AWAY)) {
							print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopTieBreakScore1" + " SET " + 
									match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getHome_score() + "\0");
						//}
					}
					
					if(match.getSets().get(1).getGames().get(match.getSets().get(1).getGames().size()-1).getGame_type().equalsIgnoreCase(TennisUtil.TIE_BREAK)&&
							match.getSets().get(1).getGames().get(match.getSets().get(1).getGames().size()-1).getGame_status().equalsIgnoreCase(TennisUtil.END)) {
						//if(match.getSets().get(1).getGames().get(match.getSets().get(1).getGames().size()-1).getGame_winner().equalsIgnoreCase(TennisUtil.HOME)) {
							print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomTieBreakScore2" + " SET " + 
									match.getSets().get(1).getGames().get(match.getSets().get(1).getGames().size()-1).getAway_score() + "\0");
						//}else if(match.getSets().get(1).getGames().get(match.getSets().get(1).getGames().size()-1).getGame_winner().equalsIgnoreCase(TennisUtil.AWAY)) {
							print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopTieBreakScore2" + " SET " + 
									match.getSets().get(1).getGames().get(match.getSets().get(1).getGames().size()-1).getHome_score() + "\0");
						//}
					}
					
					if(match.getSets().get(2).getGames().get(match.getSets().get(2).getGames().size()-1).getGame_type().equalsIgnoreCase(TennisUtil.TIE_BREAK)&&
							match.getSets().get(2).getGames().get(match.getSets().get(2).getGames().size()-1).getGame_status().equalsIgnoreCase(TennisUtil.END)) {
						//if(match.getSets().get(2).getGames().get(match.getSets().get(2).getGames().size()-1).getGame_winner().equalsIgnoreCase(TennisUtil.HOME)) {
							print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomTieBreakScore3" + " SET " + 
									match.getSets().get(2).getGames().get(match.getSets().get(2).getGames().size()-1).getAway_score() + "\0");
						//}else if(match.getSets().get(2).getGames().get(match.getSets().get(2).getGames().size()-1).getGame_winner().equalsIgnoreCase(TennisUtil.AWAY)) {
							print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopTieBreakScore3" + " SET " + 
									match.getSets().get(2).getGames().get(match.getSets().get(2).getGames().size()-1).getHome_score() + "\0");
						//}
					}
					
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopSetScore1" + " SET " + home_data.get(0) + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopSetScore2" + " SET " + home_data.get(1) + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopSetScore3" + " SET " + home_data.get(2) + "\0");
					
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomSetScore1" + " SET " + away_data.get(0) + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomSetScore2" + " SET " + away_data.get(1)+ "\0");
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomSetScore3" + " SET " + away_data.get(2)+ "\0");
				}
			}
			
			
			print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_sence_path + " C:/Temp/Preview.png In 2.520 \0");
		}
	}
	public void populateFFMatchResultDoubles(PrintWriter print_writer, String viz_sence_path, Match match,String selectedbroadcaster) {
		if (match == null) {
			System.out.println("ERROR: H2H -> Match is null");
		} else {
			
			print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tHeader" + " SET " + match.getTournament() + "\0");
			print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tSubHeader" + " SET " + match.getMatchIdent() + "\0");
			print_writer.println("-1 RENDERER*TREE*$Main$All$TopWithMask$TopGrp$HeaderGrp$Duration*ACTIVE SET 0 \0");
			
			print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vResults" + " SET " + "1" + "\0");
			//print_writer.println("-1 RENDERER*TREE*$Main$All$BottomOut$BottomGrp$BottttomGrp$NameGrpAll$PlayerAll1$TopPlayerGrp1$SlashIn*ACTIVE SET 0 \0");
			//print_writer.println("-1 RENDERER*TREE*$Main$All$BottomOut$BottomGrp$BottttomGrp$NameGrpAll$PlayerAll1$TopPlayerGrp1$SlashIn*ACTIVE SET 0 \0");
			
			if(match.getHomeFirstPlayer().getSurname() == null) {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLeftFirstName1" + " SET " + "" + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLeftLastName1" + " SET " + match.getHomeFirstPlayer().getFirstname() + "\0");
			}else {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLeftFirstName1" + " SET " + match.getHomeFirstPlayer().getFirstname() + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLeftLastName1" + " SET " + match.getHomeFirstPlayer().getSurname() + "\0");
			}
			if(match.getHomeFirstPlayer().getNationality() == null) {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLeftCountry1" + " SET " + "" + "\0");
			}else {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLeftCountry1" + " SET " + match.getHomeFirstPlayer().getNationality() + "\0");
			}
			
			print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgLeftFlag1" + " SET " + flag_path + 
					match.getHomeFirstPlayer().getNationality() + TennisUtil.PNG_EXTENSION + "\0");
			print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgLeftPlayerImage1" + " SET " + left_photo_path + 
					match.getHomeFirstPlayer().getPhoto() + TennisUtil.PNG_EXTENSION + "\0");
			
			if(match.getHomeSecondPlayer().getSurname() == null) {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLeftFirstName2" + " SET " + "" + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLeftLastName2" + " SET " + match.getHomeSecondPlayer().getFirstname() + "\0");
			}else {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLeftFirstName2" + " SET " + match.getHomeSecondPlayer().getFirstname() + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLeftLastName2" + " SET " + match.getHomeSecondPlayer().getSurname() + "\0");
			}
			
			if(match.getHomeSecondPlayer().getNationality() == null) {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLeftCountry2" + " SET " + "" + "\0");
			}else {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLeftCountry2" + " SET " + match.getHomeSecondPlayer().getNationality() + "\0");
			}
			print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgLeftFlag2" + " SET " + flag_path + 
					match.getHomeSecondPlayer().getNationality() + TennisUtil.PNG_EXTENSION + "\0");
			print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgLeftPlayerImage2" + " SET " + left_photo_path + 
					match.getHomeSecondPlayer().getPhoto() + TennisUtil.PNG_EXTENSION + "\0");
			
			if(match.getAwayFirstPlayer().getSurname() == null) {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRightFirstName1" + " SET " + "" + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRightLastName1" + " SET " + match.getAwayFirstPlayer().getFirstname() + "\0");
			}else {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRightFirstName1" + " SET " + "" + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRightLastName1" + " SET " + "" + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRightFirstName1" + " SET " + match.getAwayFirstPlayer().getFirstname() + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRightLastName1" + " SET " + match.getAwayFirstPlayer().getSurname() + "\0");
			}
			if(match.getAwayFirstPlayer().getNationality() == null) {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRightCountry1" + " SET " + "" + "\0");
			}else {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRightCountry1" + " SET " + match.getAwayFirstPlayer().getNationality() + "\0");
			}
			
			print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgRightFlag1" + " SET " + flag_path + 
					match.getAwayFirstPlayer().getNationality() + TennisUtil.PNG_EXTENSION + "\0");
			print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgRightPlayerImage2" + " SET " + right_photo_path + 
					match.getAwayFirstPlayer().getPhoto() + TennisUtil.PNG_EXTENSION + "\0");
			
			if(match.getAwaySecondPlayer().getSurname() == null) {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRightFirstName2" + " SET " + "" + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRightLastName2" + " SET " + match.getAwaySecondPlayer().getFirstname() + "\0");
			}else {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRightFirstName2" + " SET " + match.getAwaySecondPlayer().getFirstname() + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRightLastName2" + " SET " + match.getAwaySecondPlayer().getSurname() + "\0");
			}
			if(match.getAwaySecondPlayer().getNationality() == null) {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRightCountry2" + " SET " + "" + "\0");
			}else {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRightCountry2" + " SET " + match.getAwaySecondPlayer().getNationality() + "\0");
			}
			print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgRightFlag2" + " SET " + flag_path + 
					match.getAwaySecondPlayer().getNationality() + TennisUtil.PNG_EXTENSION + "\0");
			print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgRightPlayerImage1" + " SET " + right_photo_path + 
					match.getAwaySecondPlayer().getPhoto() + TennisUtil.PNG_EXTENSION + "\0");
			
			List<String> home_data = new ArrayList<String>();
			List<String> away_data = new ArrayList<String>();
			
			if(match.getSets() != null) {
				
				for (int i = 0; i <= match.getSets().size() - 1; i++) {
					int home = 0, away = 0;
					for (int j = 0; j <= match.getSets().get(i).getGames().size() - 1; j++) {
						if (match.getSets().get(i).getGames().get(j).getGame_winner() != null) {
							if (match.getSets().get(i).getGames().get(j).getGame_winner().equalsIgnoreCase(TennisUtil.HOME)) {
								home = home + 1;
							} else if (match.getSets().get(i).getGames().get(j).getGame_winner().equalsIgnoreCase(TennisUtil.AWAY)) {
								away = away + 1;
							}
						}
					}
					home_data.add(i, String.valueOf(home));
					away_data.add(i, String.valueOf(away));
				}
				
				if(match.getSets().size() == 2) {
					if(match.getSets().get(0).getSet_winner().equalsIgnoreCase(TennisUtil.HOME) && match.getSets().get(1).getSet_winner().equalsIgnoreCase(TennisUtil.HOME)) {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vSetStatus" + " SET " + "0" + "\0");
					}else if(match.getSets().get(0).getSet_winner().equalsIgnoreCase(TennisUtil.AWAY) && match.getSets().get(1).getSet_winner().equalsIgnoreCase(TennisUtil.AWAY)) {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vSetStatus" + " SET " + "5" + "\0");
					}
					
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopTieBreakScore1" + " SET " + "" + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomTieBreakScore1" + " SET " + "" + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopTieBreakScore2" + " SET " + "" + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomTieBreakScore2" + " SET " + "" + "\0");
					
					
					if(match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getGame_type().equalsIgnoreCase(TennisUtil.TIE_BREAK)&&
							match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getGame_status().equalsIgnoreCase(TennisUtil.END)) {
						//if(match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getGame_winner().equalsIgnoreCase(TennisUtil.HOME)) {
							print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomTieBreakScore1" + " SET " + 
									match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getAway_score() + "\0");
						//}else if(match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getGame_winner().equalsIgnoreCase(TennisUtil.AWAY)) {
							print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopTieBreakScore1" + " SET " + 
									match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getHome_score() + "\0");
						//}
					}
					
					if(match.getSets().get(1).getGames().get(match.getSets().get(1).getGames().size()-1).getGame_type().equalsIgnoreCase(TennisUtil.TIE_BREAK)&&
							match.getSets().get(1).getGames().get(match.getSets().get(1).getGames().size()-1).getGame_status().equalsIgnoreCase(TennisUtil.END)) {
						//if(match.getSets().get(1).getGames().get(match.getSets().get(1).getGames().size()-1).getGame_winner().equalsIgnoreCase(TennisUtil.HOME)) {
							print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomTieBreakScore2" + " SET " + 
									match.getSets().get(1).getGames().get(match.getSets().get(1).getGames().size()-1).getAway_score() + "\0");
						//}else if(match.getSets().get(1).getGames().get(match.getSets().get(1).getGames().size()-1).getGame_winner().equalsIgnoreCase(TennisUtil.AWAY)) {
							print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopTieBreakScore2" + " SET " + 
									match.getSets().get(1).getGames().get(match.getSets().get(1).getGames().size()-1).getHome_score() + "\0");
						//}
					}
					
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopSetScore1" + " SET " + home_data.get(0) + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopSetScore2" + " SET " + home_data.get(1) + "\0");
					
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomSetScore1" + " SET " + away_data.get(0) + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomSetScore2" + " SET " + away_data.get(1)+ "\0");
					
				}else if(match.getSets().size() == 3) {
					if(match.getSets().get(0).getSet_winner().equalsIgnoreCase(TennisUtil.HOME) && match.getSets().get(1).getSet_winner().equalsIgnoreCase(TennisUtil.AWAY)
							&& match.getSets().get(2).getGames().get(0).getGame_winner().equalsIgnoreCase(TennisUtil.HOME)) {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vSetStatus" + " SET " + "1" + "\0");
					}else if(match.getSets().get(0).getSet_winner().equalsIgnoreCase(TennisUtil.AWAY) && match.getSets().get(1).getSet_winner().equalsIgnoreCase(TennisUtil.HOME)
							&& match.getSets().get(2).getGames().get(0).getGame_winner().equalsIgnoreCase(TennisUtil.HOME)) {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vSetStatus" + " SET " + "2" + "\0");
					}else if(match.getSets().get(0).getSet_winner().equalsIgnoreCase(TennisUtil.HOME) && match.getSets().get(1).getSet_winner().equalsIgnoreCase(TennisUtil.AWAY)
							&& match.getSets().get(2).getGames().get(0).getGame_winner().equalsIgnoreCase(TennisUtil.AWAY)) {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vSetStatus" + " SET " + "3" + "\0");
					}else if(match.getSets().get(0).getSet_winner().equalsIgnoreCase(TennisUtil.AWAY) && match.getSets().get(1).getSet_winner().equalsIgnoreCase(TennisUtil.HOME)
							&& match.getSets().get(2).getGames().get(0).getGame_winner().equalsIgnoreCase(TennisUtil.AWAY)) {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vSetStatus" + " SET " + "4" + "\0");
					}
					
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopTieBreakScore1" + " SET " + "" + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomTieBreakScore1" + " SET " + "" + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopTieBreakScore2" + " SET " + "" + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomTieBreakScore2" + " SET " + "" + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopTieBreakScore3" + " SET " + "" + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomTieBreakScore3" + " SET " + "" + "\0");
					
					if(match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getGame_type().equalsIgnoreCase(TennisUtil.TIE_BREAK)&&
							match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getGame_status().equalsIgnoreCase(TennisUtil.END)) {
						//if(match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getGame_winner().equalsIgnoreCase(TennisUtil.HOME)) {
							print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomTieBreakScore1" + " SET " + 
									match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getAway_score() + "\0");
						//}else if(match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getGame_winner().equalsIgnoreCase(TennisUtil.AWAY)) {
							print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopTieBreakScore1" + " SET " + 
									match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getHome_score() + "\0");
						//}
					}
					
					if(match.getSets().get(1).getGames().get(match.getSets().get(1).getGames().size()-1).getGame_type().equalsIgnoreCase(TennisUtil.TIE_BREAK)&&
							match.getSets().get(1).getGames().get(match.getSets().get(1).getGames().size()-1).getGame_status().equalsIgnoreCase(TennisUtil.END)) {
						//if(match.getSets().get(1).getGames().get(match.getSets().get(1).getGames().size()-1).getGame_winner().equalsIgnoreCase(TennisUtil.HOME)) {
							print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomTieBreakScore2" + " SET " + 
									match.getSets().get(1).getGames().get(match.getSets().get(1).getGames().size()-1).getAway_score() + "\0");
						//}else if(match.getSets().get(1).getGames().get(match.getSets().get(1).getGames().size()-1).getGame_winner().equalsIgnoreCase(TennisUtil.AWAY)) {
							print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopTieBreakScore2" + " SET " + 
									match.getSets().get(1).getGames().get(match.getSets().get(1).getGames().size()-1).getHome_score() + "\0");
						//}
					}
					
					/*if(match.getSets().get(2).getGames().get(match.getSets().get(2).getGames().size()-1).getGame_type().equalsIgnoreCase(TennisUtil.TIE_BREAK)) {
						if(match.getSets().get(2).getGames().get(match.getSets().get(2).getGames().size()-1).getGame_winner().equalsIgnoreCase(TennisUtil.HOME)) {
							print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomTieBreakScore3" + " SET " + 
									match.getSets().get(2).getGames().get(match.getSets().get(2).getGames().size()-1).getAway_score() + "\0");
						}else if(match.getSets().get(2).getGames().get(match.getSets().get(2).getGames().size()-1).getGame_winner().equalsIgnoreCase(TennisUtil.AWAY)) {
							print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopTieBreakScore3" + " SET " + 
									match.getSets().get(2).getGames().get(match.getSets().get(2).getGames().size()-1).getAway_score() + "\0");
						}
					}*/
					
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopSetScore1" + " SET " + home_data.get(0) + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomSetScore1" + " SET " + away_data.get(0) + "\0");
					
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopSetScore2" + " SET " + home_data.get(1) + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomSetScore2" + " SET " + away_data.get(1)+ "\0");
					
					if(match.getMatchType().equalsIgnoreCase(TennisUtil.DOUBLES) && 
							match.getSets().get(2).getGames().get(0).getGame_type().equalsIgnoreCase(TennisUtil.TIE_BREAK)) {
						
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopSetScore3" + " SET " + 
								match.getSets().get(2).getGames().get(0).getHome_score() + "\0");
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomSetScore3" + " SET " + 
								match.getSets().get(2).getGames().get(0).getAway_score() + "\0");
						
					}else {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopSetScore3" + " SET " + home_data.get(2) + "\0");
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomSetScore3" + " SET " + away_data.get(2)+ "\0");
					}
					
				}
			}
			
			print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_sence_path + " C:/Temp/Preview.png In 2.000 \0");
		}
	}
	
	public void populateNameSuperDB(PrintWriter print_writer, String viz_sence_path,int namesuperId,List<NameSuper> NameSuper,Match match,String selectedbroadcaster) {
		
		print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFirstName1" + " SET " + NameSuper.get(namesuperId-1).getFirstname() + "\0");
		print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLastName1" + " SET " + NameSuper.get(namesuperId-1).getSurname() + "\0");
		print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tInfo" + " SET " + NameSuper.get(namesuperId-1).getSubLine() + "\0");
		
		print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_sence_path + " C:/Temp/Preview.png In 0.980 \0");
	}
	public void populateNameSuperSP(PrintWriter print_writer, String viz_sence_path,int playerid,List<Player> Plyr,List<VariousText>vt,Match match,String selectedbroadcaster) {
		
		print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFirstName" + " SET " + Plyr.get(playerid-1).getFirstname() + "\0");
		print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLastName" + " SET " + Plyr.get(playerid-1).getSurname() + "\0");
		
		print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgFlag" + " SET " + flag_path + 
			Plyr.get(playerid-1).getNationality() + TennisUtil.PNG_EXTENSION + "\0");
		
		
		if(Plyr.get(playerid-1).getRankingSingle() == 0) {
			print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tSeed" + " SET " + "" + "\0");
		}else {
			print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tSeed" + " SET " + Plyr.get(playerid-1).getRankingSingle() + "\0");
		}
		
		for(VariousText vtext : vt) {
			if(vtext.getVariousType().equalsIgnoreCase("NameSuperSingle") && vtext.getUseThis().equalsIgnoreCase("Yes")) {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tInfo" + " SET " + vtext.getVariousText().toUpperCase() + "\0");
				break;
			}else {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tInfo" + " SET " + "TATA OPEN MAHARASHTRA 2023" + "\0");
			}
		}
		print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_sence_path + " C:/Temp/Preview.png In 0.980 \0");
	}
	public void populateNameSuperSP1(PrintWriter print_writer, String viz_sence_path,int playerid,List<Player> Plyr,List<VariousText>vt,Match match,String selectedbroadcaster) {
		
		print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFirstName" + " SET " + Plyr.get(playerid-1).getFirstname() + "\0");
		print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLastName" + " SET " + Plyr.get(playerid-1).getSurname() + "\0");
		print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgFlag" + " SET " + flag_path + 
				Plyr.get(playerid-1).getNationality() + TennisUtil.PNG_EXTENSION + "\0");
		
		if(Plyr.get(playerid-1).getRankingSingle() == 0) {
			print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tSeed" + " SET " + "" + "\0");
		}else {
			print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tSeed" + " SET " + Plyr.get(playerid-1).getRankingSingle() + "\0");
		}
		for(VariousText vtext : vt) {
			if(vtext.getVariousType().equalsIgnoreCase("NameSuperSingle") && vtext.getUseThis().equalsIgnoreCase("Yes")) {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tInfo" + " SET " + vtext.getVariousText().toUpperCase() + "\0");
				break;
			}else {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tInfo" + " SET " + "TATA OPEN MAHARASHTRA 2023" + "\0");
			}
		}
		
		print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_sence_path + " C:/Temp/Preview.png In 0.980 \0");
	}
	public void populateNameSuperDP(PrintWriter print_writer, String viz_sence_path,String teamtype,List<Player> Plyr,List<VariousText>vt,Match match,String selectedbroadcaster) {
		
		switch(teamtype.toUpperCase()) {
		case TennisUtil.HOME:
			print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFirstName1" + " SET " + Plyr.get(match.getHomeFirstPlayerId()-1).getFirstname() + "\0");
			print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLastName1" + " SET " + Plyr.get(match.getHomeFirstPlayerId()-1).getSurname() + "\0");
			print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgFlag1" + " SET " + flag_path + 
					Plyr.get(match.getHomeFirstPlayerId()-1).getNationality() + TennisUtil.PNG_EXTENSION + "\0");
			
			if(Plyr.get(match.getHomeFirstPlayerId()-1).getRankingDouble() == 0) {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tSeed1" + " SET " + "" + "\0");
			}else {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tSeed1" + " SET " + Plyr.get(match.getHomeFirstPlayerId()-1).getRankingDouble() + "\0");
			}
			
			print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFirstName2" + " SET " + Plyr.get(match.getHomeSecondPlayerId()-1).getFirstname() + "\0");
			print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLastName2" + " SET " + Plyr.get(match.getHomeSecondPlayerId()-1).getSurname() + "\0");
			print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgFlag2" + " SET " + flag_path + 
					Plyr.get(match.getHomeSecondPlayerId()-1).getNationality() + TennisUtil.PNG_EXTENSION + "\0");
			
			if(Plyr.get(match.getHomeSecondPlayerId()-1).getRankingDouble() == 0) {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tSeed2" + " SET " + "" + "\0");
			}else {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tSeed2" + " SET " + Plyr.get(match.getHomeSecondPlayerId()-1).getRankingDouble() + "\0");
			}
			break;
		case TennisUtil.AWAY:
			print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFirstName1" + " SET " + Plyr.get(match.getAwayFirstPlayerId()-1).getFirstname() + "\0");
			print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLastName1" + " SET " + Plyr.get(match.getAwayFirstPlayerId()-1).getSurname() + "\0");
			print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgFlag1" + " SET " + flag_path + 
					Plyr.get(match.getAwayFirstPlayerId()-1).getNationality() + TennisUtil.PNG_EXTENSION + "\0");
			
			if(Plyr.get(match.getAwayFirstPlayerId()-1).getRankingDouble() == 0) {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tSeed1" + " SET " + "" + "\0");
			}else {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tSeed1" + " SET " + Plyr.get(match.getAwayFirstPlayerId()-1).getRankingDouble() + "\0");
			}
			
			print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFirstName2" + " SET " + Plyr.get(match.getAwaySecondPlayerId()-1).getFirstname() + "\0");
			print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLastName2" + " SET " + Plyr.get(match.getAwaySecondPlayerId()-1).getSurname() + "\0");
			print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgFlag2" + " SET " + flag_path + 
					Plyr.get(match.getAwaySecondPlayerId()-1).getNationality() + TennisUtil.PNG_EXTENSION + "\0");
			
			if(Plyr.get(match.getAwaySecondPlayerId()-1).getRankingDouble() == 0) {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tSeed2" + " SET " + "" + "\0");
			}else {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tSeed2" + " SET " + Plyr.get(match.getAwaySecondPlayerId()-1).getRankingDouble() + "\0");
			}
			break;
		}
		
		for(VariousText vtext : vt) {
			if(vtext.getVariousType().equalsIgnoreCase("NameSuperDouble") && vtext.getUseThis().equalsIgnoreCase("Yes")) {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tInfo" + " SET " + vtext.getVariousText().toUpperCase() + "\0");
				break;
			}else {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tInfo" + " SET " + "TATA OPEN MAHARASHTRA 2023" + "\0");
			}
		}
		
		print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_sence_path + " C:/Temp/Preview.png In 0.980 \0");
	}
	public void populateNameSuperDP1(PrintWriter print_writer, String viz_sence_path,int firstPlayerId,int secondPlayerId,List<Player> Plyr,List<VariousText>vt,Match match,String selectedbroadcaster) {
		
		print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFirstName1" + " SET " + Plyr.get(firstPlayerId-1).getFirstname() + "\0");
		print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLastName1" + " SET " + Plyr.get(firstPlayerId-1).getSurname() + "\0");
		print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgFlag1" + " SET " + flag_path + 
				Plyr.get(firstPlayerId-1).getNationality() + TennisUtil.PNG_EXTENSION + "\0");
		
		if(Plyr.get(firstPlayerId-1).getRankingDouble() == 0) {
			print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tSeed1" + " SET " + "" + "\0");
		}else {
			print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tSeed1" + " SET " + Plyr.get(firstPlayerId-1).getRankingDouble() + "\0");
		}
		
		print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFirstName2" + " SET " + Plyr.get(secondPlayerId-1).getFirstname() + "\0");
		print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLastName2" + " SET " + Plyr.get(secondPlayerId-1).getSurname() + "\0");
		print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgFlag2" + " SET " + flag_path + 
				Plyr.get(secondPlayerId-1).getNationality() + TennisUtil.PNG_EXTENSION + "\0");
		
		if(Plyr.get(secondPlayerId-1).getRankingDouble() == 0) {
			print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tSeed2" + " SET " + "" + "\0");
		}else {
			print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tSeed2" + " SET " + Plyr.get(secondPlayerId-1).getRankingDouble() + "\0");
		}
		
		for(VariousText vtext : vt) {
			if(vtext.getVariousType().equalsIgnoreCase("NameSuperDouble") && vtext.getUseThis().equalsIgnoreCase("Yes")) {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tInfo" + " SET " + vtext.getVariousText().toUpperCase() + "\0");
				break;
			}else {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tInfo" + " SET " + "TATA OPEN MAHARASHTRA 2023" + "\0");
			}
		}
		
		print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_sence_path + " C:/Temp/Preview.png In 0.980 \0");
	}
	
	public void populateSpeed(PrintWriter print_writer, String viz_sence_path,int speed,Match match,String selectedbroadcaster) {
		
		print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "001-SPEEDHEAD" + " SET " + "SERVE SPEED" + "\0");
		print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "001-SPEED" + " SET " + speed + " KPH" + "\0");
		print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_sence_path + " C:/Temp/Preview.png In 0.980 \0");
	}

	public void populateCross(PrintWriter print_writer, String viz_sence_path,String type,Match match,String selectedbroadcaster) throws InterruptedException {
		
		switch(match.getMatchType().toUpperCase()) {
		case TennisUtil.SINGLES:
			switch(type.toUpperCase()) {
			case TennisUtil.HOME:
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "000-FIRST-NAME01" + " SET " + match.getHomeFirstPlayer().getFirstname() + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "000-LAST-NAME01" + " SET " + match.getHomeFirstPlayer().getSurname() + "\0");
				
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "000-FLAG-01" + " SET " + flag_path + 
						match.getHomeFirstPlayer().getNationality() + TennisUtil.PNG_EXTENSION + "\0");
				
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "001-FIRST-NAME-01" + " SET " + match.getAwayFirstPlayer().getFirstname() + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "001-LAST-NAME-01" + " SET " + match.getAwayFirstPlayer().getSurname() + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "00-FLAG-01" + " SET " + flag_path + 
						match.getAwayFirstPlayer().getNationality() + TennisUtil.PNG_EXTENSION + "\0");
				break;
			case TennisUtil.AWAY:
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "000-FIRST-NAME01" + " SET " + match.getAwayFirstPlayer().getFirstname() + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "000-LAST-NAME01" + " SET " + match.getAwayFirstPlayer().getSurname() + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "000-FLAG-01" + " SET " + flag_path + 
						match.getAwayFirstPlayer().getNationality() + TennisUtil.PNG_EXTENSION + "\0");
				
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "001-FIRST-NAME-01" + " SET " + match.getHomeFirstPlayer().getFirstname() + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "001-LAST-NAME-01" + " SET " + match.getHomeFirstPlayer().getSurname() + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "00-FLAG-01" + " SET " + flag_path + 
						match.getHomeFirstPlayer().getNationality() + TennisUtil.PNG_EXTENSION + "\0");
				break;
			}
			break;
		case TennisUtil.DOUBLES:
			switch(type.toUpperCase()) {
			case TennisUtil.HOME:
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "000-FIRST-NAME01" + " SET " + match.getHomeFirstPlayer().getFirstname() + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "000-LAST-NAME01" + " SET " + match.getHomeFirstPlayer().getSurname() + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "000-FLAG-01" + " SET " + flag_path + 
						match.getHomeFirstPlayer().getNationality() + TennisUtil.PNG_EXTENSION + "\0");
				
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "000-FIRST-NAME02" + " SET " + match.getHomeSecondPlayer().getFirstname() + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "000-LAST-NAME02" + " SET " + match.getHomeSecondPlayer().getSurname() + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "000-FLAG-02" + " SET " + flag_path + 
						match.getHomeSecondPlayer().getNationality() + TennisUtil.PNG_EXTENSION + "\0");
				
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "001-FIRST-NAME-01" + " SET " + match.getAwayFirstPlayer().getFirstname() + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "001-LAST-NAME-01" + " SET " + match.getAwayFirstPlayer().getSurname() + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "00-FLAG-01" + " SET " + flag_path + 
						match.getAwayFirstPlayer().getNationality() + TennisUtil.PNG_EXTENSION + "\0");
				
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "001-FIRST-NAME02" + " SET " + match.getAwaySecondPlayer().getFirstname() + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "001-LAST-NAME02" + " SET " + match.getAwaySecondPlayer().getSurname() + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "001-FLAG-02" + " SET " + flag_path + 
						match.getAwaySecondPlayer().getNationality() + TennisUtil.PNG_EXTENSION + "\0");
				
				break;
			case TennisUtil.AWAY:
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "000-FIRST-NAME01" + " SET " + match.getAwayFirstPlayer().getFirstname() + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "000-LAST-NAME01" + " SET " + match.getAwayFirstPlayer().getSurname() + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "000-FLAG-01" + " SET " + flag_path + 
						match.getAwayFirstPlayer().getNationality() + TennisUtil.PNG_EXTENSION + "\0");
				
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "000-FIRST-NAME02" + " SET " + match.getAwaySecondPlayer().getFirstname() + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "000-LAST-NAME02" + " SET " + match.getAwaySecondPlayer().getSurname() + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "000-FLAG-02" + " SET " + flag_path + 
						match.getAwaySecondPlayer().getNationality() + TennisUtil.PNG_EXTENSION + "\0");
				
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "001-FIRST-NAME-01" + " SET " + match.getHomeFirstPlayer().getFirstname() + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "001-LAST-NAME-01" + " SET " + match.getHomeFirstPlayer().getSurname() + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "00-FLAG-01" + " SET " + flag_path + 
						match.getHomeFirstPlayer().getNationality() + TennisUtil.PNG_EXTENSION + "\0");
				
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "001-FIRST-NAME02" + " SET " + match.getHomeSecondPlayer().getFirstname() + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "001-LAST-NAME02" + " SET " + match.getHomeSecondPlayer().getSurname() + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "001-FLAG-02" + " SET " + flag_path + 
						match.getHomeSecondPlayer().getNationality() + TennisUtil.PNG_EXTENSION + "\0");
				
				break;
			}
			break;
		}
		
		
		print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_sence_path + " C:/Temp/Preview.png In 0.980 \0");
		TimeUnit.SECONDS.sleep(1);
	}
	
	public void populateMatchIdDouble(PrintWriter print_writer, String viz_sence_path, Match match,String selectedbroadcaster) throws InterruptedException {
		if (match == null) {
			System.out.println("ERROR: H2H -> Match is null");
		} else {
			
			print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tHeader" + " SET " + match.getTournament() + "\0");
			print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tSubHeader" + " SET " + match.getMatchIdent() + "\0");
			
			print_writer.println("-1 RENDERER*TREE*$Main$All$TopWithMask$TopGrp$HeaderGrp$Duration*ACTIVE SET 0 \0");
			
			print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vResults" + " SET " + "0" + "\0");
			//print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vDiscipline" + " SET " + "2" + "\0");
			
			if(match.getHomeFirstPlayer().getSurname() == null) {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLeftFirstName1" + " SET " + "" + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLeftLastName1" + " SET " + match.getHomeFirstPlayer().getFirstname() + "\0");
			}else {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLeftFirstName1" + " SET " + match.getHomeFirstPlayer().getFirstname() + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLeftLastName1" + " SET " + match.getHomeFirstPlayer().getSurname() + "\0");
			}
			if(match.getHomeFirstPlayer().getNationality() == null) {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLeftCountry1" + " SET " + "" + "\0");
			}else {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLeftCountry1" + " SET " + match.getHomeFirstPlayer().getNationality() + "\0");
			}
			
			print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgLeftFlag1" + " SET " + flag_path + 
					match.getHomeFirstPlayer().getNationality() + TennisUtil.PNG_EXTENSION + "\0");
			print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgLeftPlayerImage1" + " SET " + left_photo_path + 
					match.getHomeFirstPlayer().getPhoto() + TennisUtil.PNG_EXTENSION + "\0");
			
			if(match.getHomeSecondPlayer().getSurname() == null) {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLeftFirstName2" + " SET " + "" + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLeftLastName2" + " SET " + match.getHomeSecondPlayer().getFirstname() + "\0");
			}else {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLeftFirstName2" + " SET " + match.getHomeSecondPlayer().getFirstname() + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLeftLastName2" + " SET " + match.getHomeSecondPlayer().getSurname() + "\0");
			}
			
			if(match.getHomeSecondPlayer().getNationality() == null) {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLeftCountry2" + " SET " + "" + "\0");
			}else {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLeftCountry2" + " SET " + match.getHomeSecondPlayer().getNationality() + "\0");
			}
			print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgLeftFlag2" + " SET " + flag_path + 
					match.getHomeSecondPlayer().getNationality() + TennisUtil.PNG_EXTENSION + "\0");
			print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgLeftPlayerImage2" + " SET " + left_photo_path + 
					match.getHomeSecondPlayer().getPhoto() + TennisUtil.PNG_EXTENSION + "\0");
			
			if(match.getAwayFirstPlayer().getSurname() == null) {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRightFirstName1" + " SET " + "" + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRightLastName1" + " SET " + match.getAwayFirstPlayer().getFirstname() + "\0");
			}else {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRightFirstName1" + " SET " + "" + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRightLastName1" + " SET " + "" + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRightFirstName1" + " SET " + match.getAwayFirstPlayer().getFirstname() + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRightLastName1" + " SET " + match.getAwayFirstPlayer().getSurname() + "\0");
			}
			if(match.getAwayFirstPlayer().getNationality() == null) {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRightCountry1" + " SET " + "" + "\0");
			}else {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRightCountry1" + " SET " + match.getAwayFirstPlayer().getNationality() + "\0");
			}
			
			print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgRightFlag1" + " SET " + flag_path + 
					match.getAwayFirstPlayer().getNationality() + TennisUtil.PNG_EXTENSION + "\0");
			print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgRightPlayerImage2" + " SET " + right_photo_path + 
					match.getAwayFirstPlayer().getPhoto() + TennisUtil.PNG_EXTENSION + "\0");
			
			if(match.getAwaySecondPlayer().getSurname() == null) {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRightFirstName2" + " SET " + "" + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRightLastName2" + " SET " + match.getAwaySecondPlayer().getFirstname() + "\0");
			}else {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRightFirstName2" + " SET " + match.getAwaySecondPlayer().getFirstname() + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRightLastName2" + " SET " + match.getAwaySecondPlayer().getSurname() + "\0");
			}
			if(match.getAwaySecondPlayer().getNationality() == null) {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRightCountry2" + " SET " + "" + "\0");
			}else {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRightCountry2" + " SET " + match.getAwaySecondPlayer().getNationality() + "\0");
			}
			print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgRightFlag2" + " SET " + flag_path + 
					match.getAwaySecondPlayer().getNationality() + TennisUtil.PNG_EXTENSION + "\0");
			print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgRightPlayerImage1" + " SET " + right_photo_path + 
					match.getAwaySecondPlayer().getPhoto() + TennisUtil.PNG_EXTENSION + "\0");
			
			print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_sence_path + " C:/Temp/Preview.png In 2.000 \0");
		}
	}
	public void populateMatchId(PrintWriter print_writer, String viz_sence_path, Match match,String selectedbroadcaster) throws InterruptedException {
		if (match == null) {
			System.out.println("ERROR: H2H -> Match is null");
		} else {
			
			//print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tHeader" + " SET " + match.getTournament() + "\0");
			//print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tSubHeader" + " SET " + match.getCategoryType().toUpperCase() + " " + match.getMatchType().toUpperCase()
					//+ " - " + match.getMatchIdent() + "\0");
			//print_writer.println("-1 RENDERER*TREE*$Main$All$BottomOut$BottomGrp$BottttomGrp*FUNCTION*Omo*vis_con SET 0 \0");
			print_writer.println("-1 RENDERER*TREE*$Main$All$TopGrp$HeaderGrp$Header*GEOM*TEXT SET " + match.getMatchIdent() + "\0");
			print_writer.println("-1 RENDERER*TREE*$Main$All$TopWithMask$TopGrp$HeaderGrp$Duration*ACTIVE SET 0 \0");
			
			print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vResults" + " SET " + "0" + "\0");
			
			if(match.getHomeFirstPlayer().getSurname() == null) {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFirstName1" + " SET " + "" + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLastName1" + " SET " + match.getHomeFirstPlayer().getFirstname() + "\0");
			}else {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFirstName1" + " SET " + match.getHomeFirstPlayer().getFirstname() + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLastName1" + " SET " + match.getHomeFirstPlayer().getSurname() + "\0");
			}
			if(match.getHomeFirstPlayer().getNationality() == null) {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountry1" + " SET " + "" + "\0");
			}else {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountry1" + " SET " + match.getHomeFirstPlayer().getNationality() + "\0");
			}
			
			print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgFlag1" + " SET " + flag_path + 
					match.getHomeFirstPlayer().getNationality() + TennisUtil.PNG_EXTENSION + "\0");
			
			print_writer.println("-1 RENDERER*TREE*$Main$All$BottomGrp$LeftPlayerGrp$Noggi*TEXTURE*IMAGE SET " + left_photo_path + 
					match.getHomeFirstPlayer().getPhoto() + TennisUtil.PNG_EXTENSION + "\0");
			
			print_writer.println("-1 RENDERER*TREE*$Main$All$BottomGrp$RightPlayerGrp$Noggi*TEXTURE*IMAGE SET " + right_photo_path + 
					match.getAwayFirstPlayer().getPhoto() + TennisUtil.PNG_EXTENSION + "\0");
			
			if(match.getAwayFirstPlayer().getSurname() == null) {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFirstName2" + " SET " + "" + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLastName2" + " SET " + match.getAwayFirstPlayer().getFirstname() + "\0");
			}else {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFirstName2" + " SET " + match.getAwayFirstPlayer().getFirstname() + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLastName2" + " SET " + match.getAwayFirstPlayer().getSurname() + "\0");
			}
			if(match.getAwayFirstPlayer().getNationality() == null) {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountry2" + " SET " + "" + "\0");
			}else {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountry2" + " SET " + match.getAwayFirstPlayer().getNationality() + "\0");
			}
			
			print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgFlag2" + " SET " + flag_path + 
					match.getAwayFirstPlayer().getNationality() + TennisUtil.PNG_EXTENSION + "\0");
			
			print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_sence_path + " C:/Temp/Preview.png In 2.520 \0");
		}
	}
	
	public void populateMatchPromo(PrintWriter print_writer, String viz_sence_path,int fixid,List<Fixture> fixture,List<Player> Plyr,List<VariousText>vt,Match match,String selectedbroadcaster) {
		if (match == null) {
			System.out.println("ERROR: H2H -> Match is null");
		} else {
			
			for(VariousText vtext : vt) {
				if(vtext.getVariousType().equalsIgnoreCase("AllMatchPromo") && vtext.getUseThis().equalsIgnoreCase("Yes")) {
					print_writer.println("-1 RENDERER*TREE*$Main$All$TopGrp$HeaderGrp$Header*GEOM*TEXT SET " + 
							vtext.getVariousText().toUpperCase() + "\0");
					break;
				}else {
					print_writer.println("-1 RENDERER*TREE*$Main$All$TopGrp$HeaderGrp$Header*GEOM*TEXT SET " + "COMING UP NEXT - " + 
							fixture.get(fixid-1).getCategary().toUpperCase() + "\0");
				}
			}
			
			print_writer.println("-1 RENDERER*TREE*$Main$All$TopWithMask$TopGrp$HeaderGrp$Duration*ACTIVE SET 0 \0");
			print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vResults" + " SET " + "0" + "\0");
			
			for(Fixture fix : fixture) {
				if(fix.getMatchId() == fixid) {
					if(Plyr.get(fix.getHomePlayerFirst()-1).getSurname() == null) {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFirstName1" + " SET " + "" + "\0");
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLastName1" + " SET " + Plyr.get(fix.getHomePlayerFirst()-1).getFirstname() + "\0");
					}else {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFirstName1" + " SET " + Plyr.get(fix.getHomePlayerFirst()-1).getFirstname() + "\0");
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLastName1" + " SET " + Plyr.get(fix.getHomePlayerFirst()-1).getSurname() + "\0");
					}
					if(Plyr.get(fix.getHomePlayerFirst()-1).getNationality() == null) {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountry1" + " SET " + "" + "\0");
					}else {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountry1" + " SET " + Plyr.get(fix.getHomePlayerFirst()-1).getNationality() + "\0");
					}
					
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgFlag1" + " SET " + flag_path + 
							Plyr.get(fix.getHomePlayerFirst()-1).getNationality() + TennisUtil.PNG_EXTENSION + "\0");
					
					print_writer.println("-1 RENDERER*TREE*$Main$All$BottomGrp$LeftPlayerGrp$Noggi*TEXTURE*IMAGE SET " + left_photo_path + 
							Plyr.get(fix.getHomePlayerFirst()-1).getPhoto() + TennisUtil.PNG_EXTENSION + "\0");
					
					print_writer.println("-1 RENDERER*TREE*$Main$All$BottomGrp$RightPlayerGrp$Noggi*TEXTURE*IMAGE SET " + right_photo_path + 
							Plyr.get(fix.getAwayPlayerFirst()-1).getPhoto() + TennisUtil.PNG_EXTENSION + "\0");
					
					if(Plyr.get(fix.getAwayPlayerFirst()-1).getSurname() == null) {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFirstName2" + " SET " + "" + "\0");
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLastName2" + " SET " + Plyr.get(fix.getAwayPlayerFirst()-1).getFirstname() + "\0");
					}else {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFirstName2" + " SET " + Plyr.get(fix.getAwayPlayerFirst()-1).getFirstname() + "\0");
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLastName2" + " SET " + Plyr.get(fix.getAwayPlayerFirst()-1).getSurname() + "\0");
					}
					if(Plyr.get(fix.getAwayPlayerFirst()-1).getNationality() == null) {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountry2" + " SET " + "" + "\0");
					}else {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountry2" + " SET " + Plyr.get(fix.getAwayPlayerFirst()-1).getNationality() + "\0");
					}
					
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgFlag2" + " SET " + flag_path + 
							Plyr.get(fix.getAwayPlayerFirst()-1).getNationality() + TennisUtil.PNG_EXTENSION + "\0");
				}
			}
			
			print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_sence_path + " C:/Temp/Preview.png In 2.520 \0");
		}
	}
	public void populateMatchDoublePromo(PrintWriter print_writer, String viz_sence_path,int fixid,List<Fixture> fixture,List<Player> Plyr,List<VariousText>vt,Match match,String selectedbroadcaster) {
		if (match == null) {
			System.out.println("ERROR: H2H -> Match is null");
		} else {
			
			for(VariousText vtext : vt) {
				if(vtext.getVariousType().equalsIgnoreCase("AllMatchPromo") && vtext.getUseThis().equalsIgnoreCase("Yes")) {
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tHeader" + " SET " + 
							vtext.getVariousText().toUpperCase() + "\0");
					break;
				}else {
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tHeader" + " SET " + "COMING UP NEXT - " + 
							fixture.get(fixid-1).getCategary().toUpperCase() + "\0");
				}
			}
			
			print_writer.println("-1 RENDERER*TREE*$Main$All$TopWithMask$TopGrp$HeaderGrp$Duration*ACTIVE SET 0 \0");
			print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vResults" + " SET " + "0" + "\0");
			
			for(Fixture fix : fixture) {
				if(fix.getMatchId() == fixid) {
					if(Plyr.get(fix.getHomePlayerFirst()-1).getSurname() == null) {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLeftFirstName1" + " SET " + "" + "\0");
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLeftLastName1" + " SET " + Plyr.get(fix.getHomePlayerFirst()-1).getFirstname() + "\0");
					}else {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLeftFirstName1" + " SET " + Plyr.get(fix.getHomePlayerFirst()-1).getFirstname() + "\0");
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLeftLastName1" + " SET " + Plyr.get(fix.getHomePlayerFirst()-1).getSurname() + "\0");
					}
					if(Plyr.get(fix.getHomePlayerFirst()-1).getNationality() == null) {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLeftCountry1" + " SET " + "" + "\0");
					}else {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLeftCountry1" + " SET " + Plyr.get(fix.getHomePlayerFirst()-1).getNationality() + "\0");
					}
					
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgLeftFlag1" + " SET " + flag_path + 
							Plyr.get(fix.getHomePlayerFirst()-1).getNationality() + TennisUtil.PNG_EXTENSION + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgLeftPlayerImage1" + " SET " + left_photo_path + 
							Plyr.get(fix.getHomePlayerFirst()-1).getPhoto() + TennisUtil.PNG_EXTENSION + "\0");
					
					if(Plyr.get(fix.getHomePlayerSecond()-1).getSurname() == null) {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLeftFirstName2" + " SET " + "" + "\0");
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLeftLastName2" + " SET " + Plyr.get(fix.getHomePlayerSecond()-1).getFirstname() + "\0");
					}else {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLeftFirstName2" + " SET " + Plyr.get(fix.getHomePlayerSecond()-1).getFirstname() + "\0");
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLeftLastName2" + " SET " + Plyr.get(fix.getHomePlayerSecond()-1).getSurname() + "\0");
					}
					if(Plyr.get(fix.getHomePlayerSecond()-1).getNationality() == null) {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLeftCountry2" + " SET " + "" + "\0");
					}else {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLeftCountry2" + " SET " + Plyr.get(fix.getHomePlayerSecond()-1).getNationality() + "\0");
					}
					
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgLeftFlag2" + " SET " + flag_path + 
							Plyr.get(fix.getHomePlayerSecond()-1).getNationality() + TennisUtil.PNG_EXTENSION + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgLeftPlayerImage2" + " SET " + left_photo_path + 
							Plyr.get(fix.getHomePlayerSecond()-1).getPhoto() + TennisUtil.PNG_EXTENSION + "\0");
					
					if(Plyr.get(fix.getAwayPlayerFirst()-1).getSurname() == null) {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRightFirstName1" + " SET " + "" + "\0");
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRightLastName1" + " SET " + Plyr.get(fix.getAwayPlayerFirst()-1).getFirstname() + "\0");
					}else {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRightFirstName1" + " SET " + Plyr.get(fix.getAwayPlayerFirst()-1).getFirstname() + "\0");
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRightLastName1" + " SET " + Plyr.get(fix.getAwayPlayerFirst()-1).getSurname() + "\0");
					}
					if(Plyr.get(fix.getAwayPlayerFirst()-1).getNationality() == null) {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRightCountry1" + " SET " + "" + "\0");
					}else {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRightCountry1" + " SET " + Plyr.get(fix.getAwayPlayerFirst()-1).getNationality() + "\0");
					}
					
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgRightFlag1" + " SET " + flag_path + 
							Plyr.get(fix.getAwayPlayerFirst()-1).getNationality() + TennisUtil.PNG_EXTENSION + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgRightPlayerImage2" + " SET " + right_photo_path + 
							Plyr.get(fix.getAwayPlayerFirst()-1).getPhoto() + TennisUtil.PNG_EXTENSION + "\0");
					
					if(Plyr.get(fix.getAwayPlayerSecond()-1).getSurname() == null) {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRightFirstName2" + " SET " + "" + "\0");
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRightLastName2" + " SET " + Plyr.get(fix.getAwayPlayerSecond()-1).getFirstname() + "\0");
					}else {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRightFirstName2" + " SET " + Plyr.get(fix.getAwayPlayerSecond()-1).getFirstname() + "\0");
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRightLastName2" + " SET " + Plyr.get(fix.getAwayPlayerSecond()-1).getSurname() + "\0");
					}
					if(Plyr.get(fix.getAwayPlayerSecond()-1).getNationality() == null) {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRightCountry2" + " SET " + "" + "\0");
					}else {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRightCountry2" + " SET " + Plyr.get(fix.getAwayPlayerSecond()-1).getNationality() + "\0");
					}
					
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgRightFlag2" + " SET " + flag_path + 
							Plyr.get(fix.getAwayPlayerSecond()-1).getNationality() + TennisUtil.PNG_EXTENSION + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgRightPlayerImage1" + " SET " + right_photo_path + 
							Plyr.get(fix.getAwayPlayerSecond()-1).getPhoto() + TennisUtil.PNG_EXTENSION + "\0");
				}
			}
			
			print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_sence_path + " C:/Temp/Preview.png In 2.520 \0");
		}
	}
	public void populateLtMatchPromo(PrintWriter print_writer, String viz_sence_path,int fixid,List<Fixture> fixture,List<Player> Plyr,List<VariousText>vt,Match match,String selectedbroadcaster) {
		if (match == null) {
			System.out.println("ERROR: H2H -> Match is null");
		} else {
			
			for(VariousText vtext : vt) {
				if(vtext.getVariousType().equalsIgnoreCase("AllMatchPromo") && vtext.getUseThis().equalsIgnoreCase("Yes")) {
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tHeader" + " SET " + 
							vtext.getVariousText().toUpperCase() + "\0");
					break;
				}else {
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tHeader" + " SET " + "COMING UP NEXT - " + 
							fixture.get(fixid-1).getCategary().toUpperCase() + "\0");
				}
			}
			print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vResults" + " SET " + "0" + "\0");
			
			for(Fixture fix : fixture) {
				if(fix.getMatchId() == fixid) {
					if(Plyr.get(fix.getHomePlayerFirst()-1).getSurname() == null) {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFirstName1" + " SET " + "" + "\0");
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLastName1" + " SET " + Plyr.get(fix.getHomePlayerFirst()-1).getFirstname() + "\0");
					}else {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFirstName1" + " SET " + Plyr.get(fix.getHomePlayerFirst()-1).getFirstname() + "\0");
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLastName1" + " SET " + Plyr.get(fix.getHomePlayerFirst()-1).getSurname() + "\0");
					}
					if(Plyr.get(fix.getHomePlayerFirst()-1).getNationality() == null) {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountry1" + " SET " + "" + "\0");
					}else {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountry1" + " SET " + Plyr.get(fix.getHomePlayerFirst()-1).getNationality() + "\0");
					}
					
					if(Plyr.get(fix.getHomePlayerFirst()-1).getRankingSingle() == 0) {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRank1" + " SET " + "" + "\0");
					}else {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRank1" + " SET " + Plyr.get(fix.getHomePlayerFirst()-1).getRankingSingle() + "\0");
					}
					
					if(Plyr.get(fix.getAwayPlayerFirst()-1).getSurname() == null) {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFirstName2" + " SET " + "" + "\0");
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLastName2" + " SET " + Plyr.get(fix.getAwayPlayerFirst()-1).getFirstname() + "\0");
					}else {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFirstName2" + " SET " + Plyr.get(fix.getAwayPlayerFirst()-1).getFirstname() + "\0");
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLastName2" + " SET " + Plyr.get(fix.getAwayPlayerFirst()-1).getSurname() + "\0");
					}
					if(Plyr.get(fix.getAwayPlayerFirst()-1).getNationality() == null) {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountry2" + " SET " + "" + "\0");
					}else {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountry2" + " SET " + Plyr.get(fix.getAwayPlayerFirst()-1).getNationality() + "\0");
					}
					if(Plyr.get(fix.getAwayPlayerFirst()-1).getRankingSingle() == 0) {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRank2" + " SET " + "" + "\0");
					}else {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRank2" + " SET " + Plyr.get(fix.getAwayPlayerFirst()-1).getRankingSingle() + "\0");
					}
				}
			}
			
			print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_sence_path + " C:/Temp/Preview.png In 1.000 \0");
		}
	}
	public void populateLtMatchDoublePromo(PrintWriter print_writer, String viz_sence_path,int fixid,List<Fixture> fixture,List<Player> Plyr,List<VariousText>vt,Match match,String selectedbroadcaster) {
		if (match == null) {
			System.out.println("ERROR: H2H -> Match is null");
		} else {
			
			for(VariousText vtext : vt) {
				if(vtext.getVariousType().equalsIgnoreCase("AllMatchPromo") && vtext.getUseThis().equalsIgnoreCase("Yes")) {
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tHeader" + " SET " + 
							vtext.getVariousText().toUpperCase() + "\0");
					break;
				}else {
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tHeader" + " SET " + "COMING UP NEXT - " + 
							fixture.get(fixid-1).getCategary().toUpperCase() + "\0");
				}
			}
			
			for(Fixture fix : fixture) {
				if(fix.getMatchId() == fixid) {
					if(Plyr.get(fix.getHomePlayerFirst()-1).getSurname() == null) {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFirstNameA1" + " SET " + "" + "\0");
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLastNameA1" + " SET " + Plyr.get(fix.getHomePlayerFirst()-1).getFirstname() + "\0");
					}else {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFirstNameA1" + " SET " + Plyr.get(fix.getHomePlayerFirst()-1).getFirstname() + "\0");
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLastNameA1" + " SET " + Plyr.get(fix.getHomePlayerFirst()-1).getSurname() + "\0");
					}
					if(Plyr.get(fix.getHomePlayerFirst()-1).getNationality() == null) {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountryA1" + " SET " + "" + "\0");
					}else {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountryA1" + " SET " + Plyr.get(fix.getHomePlayerFirst()-1).getNationality() + "\0");
					}
					
					if(Plyr.get(fix.getHomePlayerFirst()-1).getRankingDouble() == 0) {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRankA1" + " SET " + "" + "\0");
					}else {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRankA1" + " SET " + Plyr.get(fix.getHomePlayerFirst()-1).getRankingDouble() + "\0");
					}
					
					if(Plyr.get(fix.getHomePlayerSecond()-1).getSurname() == null) {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFirstNameA2" + " SET " + "" + "\0");
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLastNameA2" + " SET " + Plyr.get(fix.getHomePlayerSecond()-1).getFirstname() + "\0");
					}else {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFirstNameA2" + " SET " + Plyr.get(fix.getHomePlayerSecond()-1).getFirstname() + "\0");
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLastNameA2" + " SET " + Plyr.get(fix.getHomePlayerSecond()-1).getSurname() + "\0");
					}
					if(Plyr.get(fix.getHomePlayerSecond()-1).getNationality() == null) {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountryA2" + " SET " + "" + "\0");
					}else {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountryA2" + " SET " + Plyr.get(fix.getHomePlayerSecond()-1).getNationality() + "\0");
					}
					
					if(Plyr.get(fix.getHomePlayerSecond()-1).getRankingDouble() == 0) {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRankA2" + " SET " + "" + "\0");
					}else {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRankA2" + " SET " + Plyr.get(fix.getHomePlayerSecond()-1).getRankingDouble() + "\0");
					}
					
					if(Plyr.get(fix.getAwayPlayerFirst()-1).getSurname() == null) {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFirstNameB1" + " SET " + "" + "\0");
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLastNameB1" + " SET " + Plyr.get(fix.getAwayPlayerFirst()-1).getFirstname() + "\0");
					}else {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFirstNameB1" + " SET " + Plyr.get(fix.getAwayPlayerFirst()-1).getFirstname() + "\0");
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLastNameB1" + " SET " + Plyr.get(fix.getAwayPlayerFirst()-1).getSurname() + "\0");
					}
					if(Plyr.get(fix.getAwayPlayerFirst()-1).getNationality() == null) {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountryB1" + " SET " + "" + "\0");
					}else {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountryB1" + " SET " + Plyr.get(fix.getAwayPlayerFirst()-1).getNationality() + "\0");
					}
					
					if(Plyr.get(fix.getAwayPlayerFirst()-1).getRankingDouble() == 0) {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRankB1" + " SET " + "" + "\0");
					}else {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRankB1" + " SET " + Plyr.get(fix.getAwayPlayerFirst()-1).getRankingDouble() + "\0");
					}
					
					if(Plyr.get(fix.getAwayPlayerSecond()-1).getSurname() == null) {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFirstNameB2" + " SET " + "" + "\0");
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLastNameB2" + " SET " + Plyr.get(fix.getAwayPlayerSecond()-1).getFirstname() + "\0");
					}else {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFirstNameB2" + " SET " + Plyr.get(fix.getAwayPlayerSecond()-1).getFirstname() + "\0");
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLastNameB2" + " SET " + Plyr.get(fix.getAwayPlayerSecond()-1).getSurname() + "\0");
					}
					if(Plyr.get(fix.getAwayPlayerSecond()-1).getNationality() == null) {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountryB2" + " SET " + "" + "\0");
					}else {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountryB2" + " SET " + Plyr.get(fix.getAwayPlayerSecond()-1).getNationality() + "\0");
					}
					
					if(Plyr.get(fix.getAwayPlayerSecond()-1).getRankingDouble() == 0) {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRankB2" + " SET " + "" + "\0");
					}else {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRankB2" + " SET " + Plyr.get(fix.getAwayPlayerSecond()-1).getRankingDouble() + "\0");
					}
				}
			}
			
			print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_sence_path + " C:/Temp/Preview.png In 1.000 \0");
		}
	}
	
	public void populateltMatchId(PrintWriter print_writer, String viz_sence_path, Match match,String selectedbroadcaster) {
		if (match == null) {
			System.out.println("ERROR: Lt-Match -> Match is null");
		} else {
			
			print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tHeader" + " SET " + match.getMatchIdent().toUpperCase() + "\0");
			print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vResults" + " SET " + "0" + "\0");
			
			if(match.getHomeFirstPlayer().getSurname() == null) {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFirstName1" + " SET " + "" + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLastName1" + " SET " + match.getHomeFirstPlayer().getFirstname() + "\0");
			}else {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFirstName1" + " SET " + match.getHomeFirstPlayer().getFirstname() + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLastName1" + " SET " + match.getHomeFirstPlayer().getSurname() + "\0");
			}
			
			if(match.getHomeFirstPlayer().getNationality() == null) {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountry1" + " SET " + "" + "\0");
			}else {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountry1" + " SET " + match.getHomeFirstPlayer().getNationality() + "\0");
			}
			
			if(match.getHomeFirstPlayer().getRankingSingle() == 0) {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRank1" + " SET " + "" + "\0");
			}else {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRank1" + " SET " + match.getHomeFirstPlayer().getRankingSingle() + "\0");
			}
			
			if(match.getAwayFirstPlayer().getSurname() == null) {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFirstName2" + " SET " + "" + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLastName2" + " SET " + match.getAwayFirstPlayer().getFirstname() + "\0");
			}else {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFirstName2" + " SET " + match.getAwayFirstPlayer().getFirstname() + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLastName2" + " SET " + match.getAwayFirstPlayer().getSurname() + "\0");
			}
			
			if(match.getAwayFirstPlayer().getNationality() == null) {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountry2" + " SET " + "" + "\0");
			}else {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountry2" + " SET " + match.getAwayFirstPlayer().getNationality() + "\0");
			}
			if(match.getAwayFirstPlayer().getRankingSingle() == 0) {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRank2" + " SET " + "" + "\0");
			}else {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRank2" + " SET " + match.getAwayFirstPlayer().getRankingSingle() + "\0");
			}
			print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_sence_path + " C:/Temp/Preview.png In 1.000 \0");
		}
	}
	public void populateltMatchIdDouble(PrintWriter print_writer, String viz_sence_path, Match match,String selectedbroadcaster) {
		if (match == null) {
			System.out.println("ERROR: Lt-Match -> Match is null");
		} else {
			
			print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tHeader" + " SET " + match.getMatchIdent() + "\0");
			
			if(match.getHomeFirstPlayer().getSurname() == null) {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFirstNameA1" + " SET " + "" + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLastNameA1" + " SET " + match.getHomeFirstPlayer().getFirstname() + "\0");
			}else {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFirstNameA1" + " SET " + match.getHomeFirstPlayer().getFirstname() + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLastNameA1" + " SET " + match.getHomeFirstPlayer().getSurname() + "\0");
			}
			if(match.getHomeFirstPlayer().getNationality() == null) {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountryA1" + " SET " + "" + "\0");
			}else {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountryA1" + " SET " + match.getHomeFirstPlayer().getNationality() + "\0");
			}
			
			if(match.getHomeFirstPlayer().getRankingDouble() == 0) {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRankA1" + " SET " + "" + "\0");
			}else {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRankA1" + " SET " + match.getHomeFirstPlayer().getRankingDouble() + "\0");
			}
			
			if(match.getHomeSecondPlayer().getSurname() == null) {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFirstNameA2" + " SET " + "" + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLastNameA2" + " SET " + match.getHomeSecondPlayer().getFirstname() + "\0");
			}else {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFirstNameA2" + " SET " + match.getHomeSecondPlayer().getFirstname() + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLastNameA2" + " SET " + match.getHomeSecondPlayer().getSurname() + "\0");
			}
			if(match.getHomeSecondPlayer().getNationality() == null) {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountryA2" + " SET " + "" + "\0");
			}else {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountryA2" + " SET " + match.getHomeSecondPlayer().getNationality() + "\0");
			}
			
			if(match.getHomeSecondPlayer().getRankingDouble() == 0) {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRankA2" + " SET " + "" + "\0");
			}else {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRankA2" + " SET " + match.getHomeSecondPlayer().getRankingDouble() + "\0");
			}
			
			if(match.getAwayFirstPlayer().getSurname() == null) {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFirstNameB1" + " SET " + "" + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLastNameB1" + " SET " + match.getAwayFirstPlayer().getFirstname() + "\0");
			}else {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFirstNameB1" + " SET " + match.getAwayFirstPlayer().getFirstname() + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLastNameB1" + " SET " + match.getAwayFirstPlayer().getSurname() + "\0");
			}
			if(match.getAwayFirstPlayer().getNationality() == null) {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountryB1" + " SET " + "" + "\0");
			}else {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountryB1" + " SET " + match.getAwayFirstPlayer().getNationality() + "\0");
			}
			
			if(match.getAwayFirstPlayer().getRankingDouble() == 0) {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRankB1" + " SET " + "" + "\0");
			}else {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRankB1" + " SET " + match.getAwayFirstPlayer().getRankingDouble() + "\0");
			}
			
			if(match.getAwaySecondPlayer().getSurname() == null) {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFirstNameB2" + " SET " + "" + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLastNameAB2" + " SET " + match.getAwaySecondPlayer().getFirstname() + "\0");
			}else {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tFirstNameB2" + " SET " + match.getAwaySecondPlayer().getFirstname() + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tLastNameB2" + " SET " + match.getAwaySecondPlayer().getSurname() + "\0");
			}
			if(match.getAwaySecondPlayer().getNationality() == null) {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountryB2" + " SET " + "" + "\0");
			}else {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tCountryB2" + " SET " + match.getAwaySecondPlayer().getNationality() + "\0");
			}
			
			if(match.getAwaySecondPlayer().getRankingDouble() == 0) {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRankB2" + " SET " + "" + "\0");
			}else {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tRankB2" + " SET " + match.getAwaySecondPlayer().getRankingDouble() + "\0");
			}
			
			print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_sence_path + " C:/Temp/Preview.png In 1.000 \0");
		}
	}
	
	public void populateMatchStats(PrintWriter print_writer, String viz_sence_path,  Match match,String selectedbroadcaster) throws JsonMappingException, JsonProcessingException, InterruptedException {
		if (match == null) {
			System.out.println("ERROR: Match-Stats -> Match is null");
		} else {
			
			print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tHeader" + " SET " + match.getMatchIdent() + "\0");
			print_writer.println("-1 RENDERER*TREE*$Main$All$TopWithMask$TopGrp$HeaderGrp$TimerGrp*ACTIVE SET 0 \0");
			
			switch(match.getMatchType().toUpperCase()) {
			case TennisUtil.SINGLES:
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vDiscipline" + " SET " + "1" + "\0");
				
				if(match.getHomeFirstPlayer().getSurname() == null) {
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopFirstName1" + " SET " + "" + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopLastName1" + " SET " + match.getHomeFirstPlayer().getFirstname() + "\0");
					
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tPlayerFirstNameA1" + " SET " + "" + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tPlayerLasstNameA1" + " SET " + match.getHomeFirstPlayer().getFirstname() + "\0");
				}else {
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopFirstName1" + " SET " + match.getHomeFirstPlayer().getFirstname() + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopLastName1" + " SET " + match.getHomeFirstPlayer().getSurname() + "\0");
					
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tPlayerFirstNameA1" + " SET " + "" + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tPlayerLasstNameA1" + " SET " + match.getHomeFirstPlayer().getSurname() + "\0");
				}
				if(match.getHomeFirstPlayer().getNationality() == null) {
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopCountry1" + " SET " + "" + "\0");
				}else {
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopCountry1" + " SET " + match.getHomeFirstPlayer().getNationality() + "\0");
				}
				
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgTopFlag1" + " SET " + flag_path + 
						match.getHomeFirstPlayer().getNationality() + TennisUtil.PNG_EXTENSION + "\0");
				
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgTopPlayerImage" + " SET " + left_photo_path + 
						match.getHomeFirstPlayer().getPhoto() + TennisUtil.PNG_EXTENSION + "\0");
				
				if(match.getAwayFirstPlayer().getSurname() == null) {
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomFirstName1" + " SET " + "" + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomLastName1" + " SET " + match.getAwayFirstPlayer().getFirstname() + "\0");
					
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tPlayerFirstNameB1" + " SET " + "" + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tPlayerLasstNameB1" + " SET " + match.getAwayFirstPlayer().getFirstname() + "\0");
				}else {
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomFirstName1" + " SET " + match.getAwayFirstPlayer().getFirstname() + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomLastName1" + " SET " + match.getAwayFirstPlayer().getSurname() + "\0");
					
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tPlayerFirstNameB1" + " SET " + ""+ "\0");
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tPlayerLasstNameB1" + " SET " + match.getAwayFirstPlayer().getSurname() + "\0");
				}
				
				if(match.getAwayFirstPlayer().getNationality() == null) {
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomCountry1" + " SET " + "" + "\0");
				}else {
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomCountry1" + " SET " + match.getAwayFirstPlayer().getNationality() + "\0");
				}
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgBottomFlag1" + " SET " + flag_path + 
						match.getAwayFirstPlayer().getNationality() + TennisUtil.PNG_EXTENSION + "\0");
				
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgBottomPlayerImage" + " SET " + right_photo_path + 
						match.getAwayFirstPlayer().getPhoto() + TennisUtil.PNG_EXTENSION + "\0");
				
				
				print_writer.println("-1 RENDERER*TREE*$Main$All$BottomOut$BottomGrp3$PlayerGrpNameA$Slash*ACTIVE SET 0 \0");
				print_writer.println("-1 RENDERER*TREE*$Main$All$BottomOut$BottomGrp3$PlayerGrpNameA$PlayerGrpA2*ACTIVE SET 0 \0");
				print_writer.println("-1 RENDERER*TREE*$Main$All$BottomOut$BottomGrp3$PlayerGrpNameB$Slash*ACTIVE SET 0 \0");
				print_writer.println("-1 RENDERER*TREE*$Main$All$BottomOut$BottomGrp3$PlayerGrpNameB$PlayerGrpB2*ACTIVE SET 0 \0");
				
				break;
				
			case TennisUtil.DOUBLES:
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vDiscipline" + " SET " + "2" + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main$All$BottomOut$BottomGrp1$PLayerImageGrp*ACTIVE SET 0 \0");
				print_writer.println("-1 RENDERER*TREE*$Main$All$BottomOut$BottomGrp2$PLayerImageGrp*ACTIVE SET 0 \0");
				
				if(match.getHomeFirstPlayer().getSurname() == null) {
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopFirstName1" + " SET " + "" + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopLastName1" + " SET " + match.getHomeFirstPlayer().getFirstname() + "\0");
					
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tPlayerFirstNameA1" + " SET " + "" + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tPlayerLasstNameA1" + " SET " + match.getHomeFirstPlayer().getFirstname() + "\0");
				}else {
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopFirstName1" + " SET " + match.getHomeFirstPlayer().getFirstname() + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopLastName1" + " SET " + match.getHomeFirstPlayer().getSurname() + "\0");
					
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tPlayerFirstNameA1" + " SET " + "" + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tPlayerLasstNameA1" + " SET " + match.getHomeFirstPlayer().getSurname() + "\0");
				}
				if(match.getHomeFirstPlayer().getNationality() == null) {
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopCountry1" + " SET " + "" + "\0");
				}else {
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopCountry1" + " SET " + match.getHomeFirstPlayer().getNationality() + "\0");
				}
				
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgTopFlag1" + " SET " + flag_path + 
						match.getHomeFirstPlayer().getNationality() + TennisUtil.PNG_EXTENSION + "\0");
				
				if(match.getHomeSecondPlayer().getSurname() == null) {
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopFirstName2" + " SET " + "" + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopLastName2" + " SET " + match.getHomeSecondPlayer().getFirstname() + "\0");
					
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tPlayerFirstNameA2" + " SET " + "" + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tPlayerLasstNameA2" + " SET " + match.getHomeSecondPlayer().getFirstname() + "\0");
				}else {
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopFirstName2" + " SET " + match.getHomeSecondPlayer().getFirstname() + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopLastName2" + " SET " + match.getHomeSecondPlayer().getSurname() + "\0");
					
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tPlayerFirstNameA2" + " SET " + "" + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tPlayerLasstNameA2" + " SET " + match.getHomeSecondPlayer().getSurname() + "\0");
				}
				if(match.getHomeSecondPlayer().getNationality() == null) {
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopCountry2" + " SET " + "" + "\0");
				}else {
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopCountry2" + " SET " + match.getHomeSecondPlayer().getNationality() + "\0");
				}
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgTopFlag2" + " SET " + flag_path + 
						match.getHomeSecondPlayer().getNationality() + TennisUtil.PNG_EXTENSION + "\0");
				
				if(match.getAwayFirstPlayer().getSurname() == null) {
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomFirstName1" + " SET " + "" + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomLastName1" + " SET " + match.getAwayFirstPlayer().getFirstname() + "\0");
					
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tPlayerFirstNameB1" + " SET " + "" + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tPlayerLasstNameB1" + " SET " + match.getAwayFirstPlayer().getFirstname() + "\0");
				}else {
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomFirstName1" + " SET " + match.getAwayFirstPlayer().getFirstname() + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomLastName1" + " SET " + match.getAwayFirstPlayer().getSurname() + "\0");
					
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tPlayerFirstNameB1" + " SET " + "" + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tPlayerLasstNameB1" + " SET " + match.getAwayFirstPlayer().getSurname() + "\0");
				}
				if(match.getAwayFirstPlayer().getNationality() == null) {
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomCountry1" + " SET " + "" + "\0");
				}else {
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomCountry1" + " SET " + match.getAwayFirstPlayer().getNationality() + "\0");
				}
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgBottomFlag1" + " SET " + flag_path + 
						match.getAwayFirstPlayer().getNationality() + TennisUtil.PNG_EXTENSION + "\0");
				
				if(match.getAwaySecondPlayer().getSurname() == null) {
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomFirstName2" + " SET " + "" + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomLastName2" + " SET " + match.getAwaySecondPlayer().getFirstname() + "\0");
					
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tPlayerFirstNameB2" + " SET " + "" + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tPlayerLasstNameB2" + " SET " + match.getAwaySecondPlayer().getFirstname() + "\0");
				}else {
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomFirstName2" + " SET " + match.getAwaySecondPlayer().getFirstname() + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomLastName2" + " SET " + match.getAwaySecondPlayer().getSurname() + "\0");
					
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tPlayerFirstNameB2" + " SET " + "" + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tPlayerLasstNameB2" + " SET " + match.getAwaySecondPlayer().getSurname() + "\0");
				}
				if(match.getAwaySecondPlayer().getNationality() == null) {
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomCountry2" + " SET " + "" + "\0");
				}else {
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomCountry2" + " SET " + match.getAwaySecondPlayer().getNationality() + "\0");
				}
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "lgBottomFlag2" + " SET " + flag_path + 
						match.getAwaySecondPlayer().getNationality() + TennisUtil.PNG_EXTENSION + "\0");
				
				print_writer.println("-1 RENDERER*TREE*$Main$All$BottomOut$BottomGrp1$TopPlayerAll$TopPlayerGrp1$SlashIn$Slash*GEOM*TEXT SET" + "" +"\0");
				print_writer.println("-1 RENDERER*TREE*$Main$All$BottomOut$BottomGrp2$BottomPlayerAll$BottomPlayerGrp1$SlashIn$Slash*GEOM*TEXT SET" + "" +"\0");
				break;
			}
			MatchStatsSets(print_writer, match);
			TimeUnit.MICROSECONDS.sleep(800);
			MatchStatsValue(print_writer, match);
			TimeUnit.MICROSECONDS.sleep(800);
			print_writer.println("-1 RENDERER PREVIEW SCENE*" + viz_sence_path + " C:/Temp/Preview.png In 2.500 \0");
		}
	}
	public void MatchStatsSets(PrintWriter print_writer,Match match) {
		
		List<String> home_data = new ArrayList<String>();
		List<String> away_data = new ArrayList<String>();
		
		if(match.getSets() != null) {
			
			for (int i = 0; i <= match.getSets().size() - 1; i++) {
				int home = 0, away = 0;
				for (int j = 0; j <= match.getSets().get(i).getGames().size() - 1; j++) {
					if (match.getSets().get(i).getGames().get(j).getGame_winner() != null) {
						if (match.getSets().get(i).getGames().get(j).getGame_winner().equalsIgnoreCase(TennisUtil.HOME)) {
							home = home + 1;
						} else if (match.getSets().get(i).getGames().get(j).getGame_winner().equalsIgnoreCase(TennisUtil.AWAY)) {
							away = away + 1;
						}
					}
				}
				home_data.add(i, String.valueOf(home));
				away_data.add(i, String.valueOf(away));
			}
			
			print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopTieBreakScore1" + " SET " + "" + "\0");
			print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomTieBreakScore1" + " SET " + "" + "\0");
			print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopTieBreakScore2" + " SET " + "" + "\0");
			print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomTieBreakScore2" + " SET " + "" + "\0");
			print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopTieBreakScore3" + " SET " + "" + "\0");
			print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomTieBreakScore3" + " SET " + "" + "\0");

			if(match.getSets().size() <= 3) {
				if(match.getSets().size()==1) {
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vSetStatus" + " SET " + "6" + "\0");
					
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopSetScore1" + " SET " + home_data.get(0) + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomSetScore1" + " SET " + away_data.get(0) + "\0");
					
				}else if(match.getSets().size() == 2) {
					if(match.getSets().get(0).getSet_status().equalsIgnoreCase(TennisUtil.END) && match.getSets().get(1).getSet_status().equalsIgnoreCase(TennisUtil.START)) {
						if(Integer.valueOf(home_data.get(0)) > Integer.valueOf(away_data.get(0))) {
							print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vSetStatus" + " SET " + "7" + "\0");
						}else if(Integer.valueOf(home_data.get(0)) < Integer.valueOf(away_data.get(0))) {
							print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vSetStatus" + " SET " + "9" + "\0");
						}
					}else if(match.getSets().get(0).getSet_status().equalsIgnoreCase(TennisUtil.END) && match.getSets().get(1).getSet_status().equalsIgnoreCase(TennisUtil.END)){
						if(match.getSets().get(0).getSet_winner().equalsIgnoreCase(TennisUtil.HOME) && match.getSets().get(1).getSet_winner().equalsIgnoreCase(TennisUtil.HOME)) {
							print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vSetStatus" + " SET " + "0" + "\0");
						}else if(match.getSets().get(0).getSet_winner().equalsIgnoreCase(TennisUtil.AWAY) && match.getSets().get(1).getSet_winner().equalsIgnoreCase(TennisUtil.AWAY)) {
							print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vSetStatus" + " SET " + "5" + "\0");
						}
					}
					
					if(match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getGame_type().equalsIgnoreCase(TennisUtil.TIE_BREAK)&&
							match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getGame_status().equalsIgnoreCase(TennisUtil.END)) {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomTieBreakScore1" + " SET " + 
								match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getAway_score() + "\0");
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopTieBreakScore1" + " SET " + 
								match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getHome_score() + "\0");
					}
					
					if(match.getSets().get(1).getGames().get(match.getSets().get(1).getGames().size()-1).getGame_type().equalsIgnoreCase(TennisUtil.TIE_BREAK)&&
							match.getSets().get(1).getGames().get(match.getSets().get(1).getGames().size()-1).getGame_status().equalsIgnoreCase(TennisUtil.END)) {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomTieBreakScore2" + " SET " + 
								match.getSets().get(1).getGames().get(match.getSets().get(1).getGames().size()-1).getAway_score() + "\0");
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopTieBreakScore2" + " SET " + 
								match.getSets().get(1).getGames().get(match.getSets().get(1).getGames().size()-1).getHome_score() + "\0");
					}
					
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopSetScore1" + " SET " + home_data.get(0) + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopSetScore2" + " SET " + home_data.get(1) + "\0");
					
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomSetScore1" + " SET " + away_data.get(0) + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomSetScore2" + " SET " + away_data.get(1)+ "\0");
					
				}else if(match.getSets().size() == 3) {
					
					if(match.getMatchType().equalsIgnoreCase(TennisUtil.SINGLES)) {
						if(match.getSets().get(0).getSet_status().equalsIgnoreCase(TennisUtil.END) && match.getSets().get(1).getSet_status().equalsIgnoreCase(TennisUtil.END) &&
								match.getSets().get(2).getSet_status().equalsIgnoreCase(TennisUtil.START)) {
							
							if(Integer.valueOf(home_data.get(0)) > Integer.valueOf(away_data.get(0)) && Integer.valueOf(home_data.get(1)) < Integer.valueOf(away_data.get(1))) {
								print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vSetStatus" + " SET " + "8" + "\0");
							}else if(Integer.valueOf(home_data.get(0)) < Integer.valueOf(away_data.get(0)) && Integer.valueOf(home_data.get(1)) > Integer.valueOf(away_data.get(1))) {
								print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vSetStatus" + " SET " + "10" + "\0");
							}
							
						}else if(match.getSets().get(0).getSet_status().equalsIgnoreCase(TennisUtil.END) && match.getSets().get(1).getSet_status().equalsIgnoreCase(TennisUtil.END) &&
								match.getSets().get(2).getSet_status().equalsIgnoreCase(TennisUtil.END)){
							
							if(match.getSets().get(0).getSet_winner().equalsIgnoreCase(TennisUtil.HOME) && match.getSets().get(1).getSet_winner().equalsIgnoreCase(TennisUtil.AWAY)
									&& match.getSets().get(2).getSet_winner().equalsIgnoreCase(TennisUtil.HOME)) {
								print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vSetStatus" + " SET " + "1" + "\0");
							}else if(match.getSets().get(0).getSet_winner().equalsIgnoreCase(TennisUtil.AWAY) && match.getSets().get(1).getSet_winner().equalsIgnoreCase(TennisUtil.HOME)
									&& match.getSets().get(2).getSet_winner().equalsIgnoreCase(TennisUtil.HOME)) {
								print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vSetStatus" + " SET " + "2" + "\0");
							}else if(match.getSets().get(0).getSet_winner().equalsIgnoreCase(TennisUtil.HOME) && match.getSets().get(1).getSet_winner().equalsIgnoreCase(TennisUtil.AWAY)
									&& match.getSets().get(2).getSet_winner().equalsIgnoreCase(TennisUtil.AWAY)) {
								print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vSetStatus" + " SET " + "3" + "\0");
							}else if(match.getSets().get(0).getSet_winner().equalsIgnoreCase(TennisUtil.AWAY) && match.getSets().get(1).getSet_winner().equalsIgnoreCase(TennisUtil.HOME)
									&& match.getSets().get(2).getSet_winner().equalsIgnoreCase(TennisUtil.AWAY)) {
								print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vSetStatus" + " SET " + "4" + "\0");
							}
							
						}
					}else if(match.getMatchType().equalsIgnoreCase(TennisUtil.DOUBLES)) {
						if(match.getSets().get(0).getSet_status().equalsIgnoreCase(TennisUtil.END) && match.getSets().get(1).getSet_status().equalsIgnoreCase(TennisUtil.END) &&
								match.getSets().get(2).getGames().get(0).getGame_status().equalsIgnoreCase(TennisUtil.START)) {
							
							if(Integer.valueOf(home_data.get(0)) > Integer.valueOf(away_data.get(0)) && Integer.valueOf(home_data.get(1)) < Integer.valueOf(away_data.get(1))) {
								print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vSetStatus" + " SET " + "8" + "\0");
							}else if(Integer.valueOf(home_data.get(0)) < Integer.valueOf(away_data.get(0)) && Integer.valueOf(home_data.get(1)) > Integer.valueOf(away_data.get(1))) {
								print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vSetStatus" + " SET " + "10" + "\0");
							}
							
						}else if(match.getSets().get(0).getSet_status().equalsIgnoreCase(TennisUtil.END) && match.getSets().get(1).getSet_status().equalsIgnoreCase(TennisUtil.END) &&
								match.getSets().get(2).getGames().get(0).getGame_status().equalsIgnoreCase(TennisUtil.END)){
							
							if(match.getSets().get(0).getSet_winner().equalsIgnoreCase(TennisUtil.HOME) && match.getSets().get(1).getSet_winner().equalsIgnoreCase(TennisUtil.AWAY)
									&& match.getSets().get(2).getGames().get(0).getGame_winner().equalsIgnoreCase(TennisUtil.HOME)) {
								print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vSetStatus" + " SET " + "1" + "\0");
							}else if(match.getSets().get(0).getSet_winner().equalsIgnoreCase(TennisUtil.AWAY) && match.getSets().get(1).getSet_winner().equalsIgnoreCase(TennisUtil.HOME)
									&& match.getSets().get(2).getGames().get(0).getGame_winner().equalsIgnoreCase(TennisUtil.HOME)) {
								print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vSetStatus" + " SET " + "2" + "\0");
							}else if(match.getSets().get(0).getSet_winner().equalsIgnoreCase(TennisUtil.HOME) && match.getSets().get(1).getSet_winner().equalsIgnoreCase(TennisUtil.AWAY)
									&& match.getSets().get(2).getGames().get(0).getGame_winner().equalsIgnoreCase(TennisUtil.AWAY)) {
								print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vSetStatus" + " SET " + "3" + "\0");
							}else if(match.getSets().get(0).getSet_winner().equalsIgnoreCase(TennisUtil.AWAY) && match.getSets().get(1).getSet_winner().equalsIgnoreCase(TennisUtil.HOME)
									&& match.getSets().get(2).getGames().get(0).getGame_winner().equalsIgnoreCase(TennisUtil.AWAY)) {
								print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vSetStatus" + " SET " + "4" + "\0");
							}
							
						}
					}
					
					if(match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getGame_type().equalsIgnoreCase(TennisUtil.TIE_BREAK)&&
							match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getGame_status().equalsIgnoreCase(TennisUtil.END)) {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomTieBreakScore1" + " SET " + 
								match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getAway_score() + "\0");
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopTieBreakScore1" + " SET " + 
								match.getSets().get(0).getGames().get(match.getSets().get(0).getGames().size()-1).getHome_score() + "\0");
					}
					
					if(match.getSets().get(1).getGames().get(match.getSets().get(1).getGames().size()-1).getGame_type().equalsIgnoreCase(TennisUtil.TIE_BREAK)&&
							match.getSets().get(1).getGames().get(match.getSets().get(1).getGames().size()-1).getGame_status().equalsIgnoreCase(TennisUtil.END)) {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomTieBreakScore2" + " SET " + 
								match.getSets().get(1).getGames().get(match.getSets().get(1).getGames().size()-1).getAway_score() + "\0");
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopTieBreakScore2" + " SET " + 
								match.getSets().get(1).getGames().get(match.getSets().get(1).getGames().size()-1).getHome_score() + "\0");
					}
					
					if(match.getMatchType().toUpperCase().equalsIgnoreCase(TennisUtil.SINGLES) && 
							match.getSets().get(2).getGames().get(match.getSets().get(2).getGames().size()-1).getGame_type().equalsIgnoreCase(TennisUtil.TIE_BREAK)&&
							match.getSets().get(2).getGames().get(match.getSets().get(2).getGames().size()-1).getGame_status().equalsIgnoreCase(TennisUtil.END)) {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomTieBreakScore3" + " SET " + 
								match.getSets().get(2).getGames().get(match.getSets().get(2).getGames().size()-1).getAway_score() + "\0");
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopTieBreakScore3" + " SET " + 
								match.getSets().get(2).getGames().get(match.getSets().get(2).getGames().size()-1).getHome_score() + "\0");
					}
					
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopSetScore1" + " SET " + home_data.get(0) + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopSetScore2" + " SET " + home_data.get(1) + "\0");
					
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomSetScore1" + " SET " + away_data.get(0) + "\0");
					print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomSetScore2" + " SET " + away_data.get(1)+ "\0");
					
					if(match.getMatchType().equalsIgnoreCase(TennisUtil.DOUBLES) && 
							match.getSets().get(2).getGames().get(0).getGame_type().equalsIgnoreCase(TennisUtil.TIE_BREAK)) {
						
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopSetScore3" + " SET " + match.getSets().get(2).getGames().get(0).getHome_score() + "\0");
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomSetScore3" + " SET " + match.getSets().get(2).getGames().get(0).getAway_score() + "\0");
						
					}else {
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tTopSetScore3" + " SET " + home_data.get(2) + "\0");
						print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBottomSetScore3" + " SET " + away_data.get(2)+ "\0");
					}
					
				}
			}
		}
	}
	public void MatchStatsValue(PrintWriter print_writer,Match match) throws JsonMappingException, JsonProcessingException {
		
		Double home_serve = 0.0,away_serve= 0.0,home_serve_won= 0.0,away_serve_won= 0.0,home_serve_won_second= 0.0,away_serve_won_second= 0.0;
		String link = "https://api.protennislive.com/feeds/MatchStats/" + match.getMatchId();
		LiveMatchStatsAPI ApiMatch = TennisFunctions.getMatchStatsApi(link);
		
		print_writer.println("-1 RENDERER*TREE*$Main$All$BottomOut$BottomGrp3$AllHeader$AcesHeaderGrp$AcesHeadText*GEOM*TEXT SET " + "ACES" + "\0");
		print_writer.println("-1 RENDERER*TREE*$Main$All$BottomOut$BottomGrp3$AllHeader$DoubleFaultsrGrp$AcesHeadText*GEOM*TEXT SET " + "DOUBLE\n" + "FAULTS" + "\0");
		print_writer.println("-1 RENDERER*TREE*$Main$All$BottomOut$BottomGrp3$AllHeader$BreakPointsrGrp$AcesHeadText*GEOM*TEXT SET " + "BREAK\n" + "POINTS" + "\0");
		
		if(ApiMatch.getStatus().equalsIgnoreCase("F")) {
			home_serve = ((Double.valueOf(ApiMatch.getPlayerTeam1().getSets().get(0).getStats().getServiceStats().getFirstServe().getDividend())/
					Double.valueOf(ApiMatch.getPlayerTeam1().getSets().get(0).getStats().getServiceStats().getFirstServe().getDivisor()))*100);
			
			home_serve_won = ((Double.valueOf(ApiMatch.getPlayerTeam1().getSets().get(0).getStats().getServiceStats().getFirstServePointsWon().getDividend())/
					Double.valueOf(ApiMatch.getPlayerTeam1().getSets().get(0).getStats().getServiceStats().getFirstServePointsWon().getDivisor()))*100);
			
			home_serve_won_second = ((Double.valueOf(ApiMatch.getPlayerTeam1().getSets().get(0).getStats().getServiceStats().getSecondServePointsWon().getDividend())/
					Double.valueOf(ApiMatch.getPlayerTeam1().getSets().get(0).getStats().getServiceStats().getSecondServePointsWon().getDivisor()))*100);
			
			away_serve = ((Double.valueOf(ApiMatch.getPlayerTeam2().getSets().get(0).getStats().getServiceStats().getFirstServe().getDividend())/
					Double.valueOf(ApiMatch.getPlayerTeam2().getSets().get(0).getStats().getServiceStats().getFirstServe().getDivisor()))*100);
			
			away_serve_won = ((Double.valueOf(ApiMatch.getPlayerTeam2().getSets().get(0).getStats().getServiceStats().getFirstServePointsWon().getDividend())/
					Double.valueOf(ApiMatch.getPlayerTeam2().getSets().get(0).getStats().getServiceStats().getFirstServePointsWon().getDivisor()))*100);
			
			away_serve_won_second = ((Double.valueOf(ApiMatch.getPlayerTeam2().getSets().get(0).getStats().getServiceStats().getSecondServePointsWon().getDividend())/
					Double.valueOf(ApiMatch.getPlayerTeam2().getSets().get(0).getStats().getServiceStats().getSecondServePointsWon().getDivisor()))*100);
			
			print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tAcesA" + " SET " + 
					ApiMatch.getPlayerTeam1().getSets().get(0).getStats().getServiceStats().getAces().getNumber() + "\0");
			print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tAcesB" + " SET " + 
					ApiMatch.getPlayerTeam2().getSets().get(0).getStats().getServiceStats().getAces().getNumber() + "\0");
			
			print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tDoubleFaultsA" + " SET " + 
					ApiMatch.getPlayerTeam1().getSets().get(0).getStats().getServiceStats().getDoubleFaults().getNumber() + "\0");
			print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tDoubleFaultsB" + " SET " + 
					ApiMatch.getPlayerTeam2().getSets().get(0).getStats().getServiceStats().getDoubleFaults().getNumber() + "\0");
			
			
			print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBreakingPointsA1" + " SET " + 
					ApiMatch.getPlayerTeam1().getSets().get(0).getStats().getReturnStats().getBreakPointsConverted().getDividend() + "\0");
			print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBreakingPointsA2" + " SET " + 
					ApiMatch.getPlayerTeam1().getSets().get(0).getStats().getReturnStats().getBreakPointsConverted().getDivisor() + "\0");
			
			print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBreakingPointsB1" + " SET " + 
					ApiMatch.getPlayerTeam2().getSets().get(0).getStats().getReturnStats().getBreakPointsConverted().getDividend() + "\0");
			print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBreakingPointsB2" + " SET " + 
					ApiMatch.getPlayerTeam2().getSets().get(0).getStats().getReturnStats().getBreakPointsConverted().getDivisor() + "\0");
			
			if(ApiMatch.getPlayerTeam1().getSets().size() == 3) {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vBreakingPointSetNumber" + " SET " + "2" + "\0");
				
				print_writer.println("-1 RENDERER*TREE*$Main$All$BottomOut$BottomGrp3$AllHeader$SetNumberGrp$2$SetText1*GEOM*TEXT SET " + "1" + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main$All$BottomOut$BottomGrp3$AllHeader$SetNumberGrp$2$SetText2*GEOM*TEXT SET " + "2" + "\0");
				
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tSet1BreakingPointsA1" + " SET " + 
						ApiMatch.getPlayerTeam1().getSets().get(1).getStats().getReturnStats().getBreakPointsConverted().getDividend() + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tSet1BreakingPointsA2" + " SET " + 
						ApiMatch.getPlayerTeam1().getSets().get(1).getStats().getReturnStats().getBreakPointsConverted().getDivisor() + "\0");
				
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tSet1BreakingPointsB1" + " SET " + 
						ApiMatch.getPlayerTeam2().getSets().get(1).getStats().getReturnStats().getBreakPointsConverted().getDividend() + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tSet1BreakingPointsB2" + " SET " + 
						ApiMatch.getPlayerTeam2().getSets().get(1).getStats().getReturnStats().getBreakPointsConverted().getDivisor() + "\0");
				
				
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tSet2BreakingPointsA1" + " SET " + 
						ApiMatch.getPlayerTeam1().getSets().get(2).getStats().getReturnStats().getBreakPointsConverted().getDividend() + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tSet2BreakingPointsA2" + " SET " + 
						ApiMatch.getPlayerTeam1().getSets().get(2).getStats().getReturnStats().getBreakPointsConverted().getDivisor() + "\0");
				
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tSet2BreakingPointsB1" + " SET " + 
						ApiMatch.getPlayerTeam2().getSets().get(2).getStats().getReturnStats().getBreakPointsConverted().getDividend() + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tSet2BreakingPointsB2" + " SET " + 
						ApiMatch.getPlayerTeam2().getSets().get(2).getStats().getReturnStats().getBreakPointsConverted().getDivisor() + "\0");
				
				
			}else if(ApiMatch.getPlayerTeam1().getSets().size() == 4) {
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vBreakingPointSetNumber" + " SET " + "3" + "\0");
				
				print_writer.println("-1 RENDERER*TREE*$Main$All$BottomOut$BottomGrp3$AllHeader$SetNumberGrp$3$SetText1*GEOM*TEXT SET " + "1" + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main$All$BottomOut$BottomGrp3$AllHeader$SetNumberGrp$3$SetText2*GEOM*TEXT SET " + "2" + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main$All$BottomOut$BottomGrp3$AllHeader$SetNumberGrp$3$SetText3*GEOM*TEXT SET " + "3" + "\0");
				
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tSet1BreakingPointsA1" + " SET " + 
						ApiMatch.getPlayerTeam1().getSets().get(1).getStats().getReturnStats().getBreakPointsConverted().getDividend() + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tSet1BreakingPointsA2" + " SET " + 
						ApiMatch.getPlayerTeam1().getSets().get(1).getStats().getReturnStats().getBreakPointsConverted().getDivisor() + "\0");
				
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tSet1BreakingPointsB1" + " SET " + 
						ApiMatch.getPlayerTeam2().getSets().get(1).getStats().getReturnStats().getBreakPointsConverted().getDividend() + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tSet1BreakingPointsB2" + " SET " + 
						ApiMatch.getPlayerTeam2().getSets().get(1).getStats().getReturnStats().getBreakPointsConverted().getDivisor() + "\0");
				
				
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tSet2BreakingPointsA1" + " SET " + 
						ApiMatch.getPlayerTeam1().getSets().get(2).getStats().getReturnStats().getBreakPointsConverted().getDividend() + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tSet2BreakingPointsA2" + " SET " + 
						ApiMatch.getPlayerTeam1().getSets().get(2).getStats().getReturnStats().getBreakPointsConverted().getDivisor() + "\0");
				
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tSet2BreakingPointsB1" + " SET " + 
						ApiMatch.getPlayerTeam2().getSets().get(2).getStats().getReturnStats().getBreakPointsConverted().getDividend() + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tSet2BreakingPointsB2" + " SET " + 
						ApiMatch.getPlayerTeam2().getSets().get(2).getStats().getReturnStats().getBreakPointsConverted().getDivisor() + "\0");
				
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tSet3BreakingPointsA1" + " SET " + 
						ApiMatch.getPlayerTeam1().getSets().get(3).getStats().getReturnStats().getBreakPointsConverted().getDividend() + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tSet3BreakingPointsA2" + " SET " + 
						ApiMatch.getPlayerTeam1().getSets().get(3).getStats().getReturnStats().getBreakPointsConverted().getDivisor() + "\0");
				
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tSet3BreakingPointsB1" + " SET " + 
						ApiMatch.getPlayerTeam2().getSets().get(3).getStats().getReturnStats().getBreakPointsConverted().getDividend() + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tSet3BreakingPointsB2" + " SET " + 
						ApiMatch.getPlayerTeam2().getSets().get(3).getStats().getReturnStats().getBreakPointsConverted().getDivisor() + "\0");
			}	
		}else if(ApiMatch.getStatus().equalsIgnoreCase("P")) {
			if(ApiMatch.getPlayerTeam1().getSets().size() == 2) {
				
				home_serve = ((Double.valueOf(ApiMatch.getPlayerTeam1().getSets().get(1).getStats().getServiceStats().getFirstServe().getDividend())/
						Double.valueOf(ApiMatch.getPlayerTeam1().getSets().get(1).getStats().getServiceStats().getFirstServe().getDivisor()))*100);
				
				home_serve_won = ((Double.valueOf(ApiMatch.getPlayerTeam1().getSets().get(1).getStats().getServiceStats().getFirstServePointsWon().getDividend())/
						Double.valueOf(ApiMatch.getPlayerTeam1().getSets().get(1).getStats().getServiceStats().getFirstServePointsWon().getDivisor()))*100);
				
				home_serve_won_second = ((Double.valueOf(ApiMatch.getPlayerTeam1().getSets().get(1).getStats().getServiceStats().getSecondServePointsWon().getDividend())/
						Double.valueOf(ApiMatch.getPlayerTeam1().getSets().get(1).getStats().getServiceStats().getSecondServePointsWon().getDivisor()))*100);
				
				away_serve = ((Double.valueOf(ApiMatch.getPlayerTeam2().getSets().get(1).getStats().getServiceStats().getFirstServe().getDividend())/
						Double.valueOf(ApiMatch.getPlayerTeam2().getSets().get(1).getStats().getServiceStats().getFirstServe().getDivisor()))*100);
				
				away_serve_won = ((Double.valueOf(ApiMatch.getPlayerTeam2().getSets().get(1).getStats().getServiceStats().getFirstServePointsWon().getDividend())/
						Double.valueOf(ApiMatch.getPlayerTeam2().getSets().get(1).getStats().getServiceStats().getFirstServePointsWon().getDivisor()))*100);
				
				away_serve_won_second = ((Double.valueOf(ApiMatch.getPlayerTeam2().getSets().get(1).getStats().getServiceStats().getSecondServePointsWon().getDividend())/
						Double.valueOf(ApiMatch.getPlayerTeam2().getSets().get(1).getStats().getServiceStats().getSecondServePointsWon().getDivisor()))*100);
				
				
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tAcesA" + " SET " + 
						ApiMatch.getPlayerTeam1().getSets().get(1).getStats().getServiceStats().getAces().getNumber() + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tAcesB" + " SET " + 
						ApiMatch.getPlayerTeam2().getSets().get(1).getStats().getServiceStats().getAces().getNumber() + "\0");
				
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tDoubleFaultsA" + " SET " + 
						ApiMatch.getPlayerTeam1().getSets().get(1).getStats().getServiceStats().getDoubleFaults().getNumber() + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tDoubleFaultsB" + " SET " + 
						ApiMatch.getPlayerTeam2().getSets().get(1).getStats().getServiceStats().getDoubleFaults().getNumber() + "\0");
				
				
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBreakingPointsA1" + " SET " + 
						ApiMatch.getPlayerTeam1().getSets().get(0).getStats().getReturnStats().getBreakPointsConverted().getDividend() + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBreakingPointsA2" + " SET " + 
						ApiMatch.getPlayerTeam1().getSets().get(0).getStats().getReturnStats().getBreakPointsConverted().getDivisor() + "\0");
				
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBreakingPointsB1" + " SET " + 
						ApiMatch.getPlayerTeam2().getSets().get(0).getStats().getReturnStats().getBreakPointsConverted().getDividend() + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBreakingPointsB2" + " SET " + 
						ApiMatch.getPlayerTeam2().getSets().get(0).getStats().getReturnStats().getBreakPointsConverted().getDivisor() + "\0");
				
				
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vBreakingPointSetNumber" + " SET " + "1" + "\0");
				
				print_writer.println("-1 RENDERER*TREE*$Main$All$BottomOut$BottomGrp3$AllHeader$SetNumberGrp$2$SetText1*GEOM*TEXT SET " + "1" + "\0");
				
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tSet1BreakingPointsA1" + " SET " + 
						ApiMatch.getPlayerTeam1().getSets().get(1).getStats().getReturnStats().getBreakPointsConverted().getDividend() + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tSet1BreakingPointsA2" + " SET " + 
						ApiMatch.getPlayerTeam1().getSets().get(1).getStats().getReturnStats().getBreakPointsConverted().getDivisor() + "\0");
				
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tSet1BreakingPointsB1" + " SET " + 
						ApiMatch.getPlayerTeam2().getSets().get(1).getStats().getReturnStats().getBreakPointsConverted().getDividend() + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tSet1BreakingPointsB2" + " SET " + 
						ApiMatch.getPlayerTeam2().getSets().get(1).getStats().getReturnStats().getBreakPointsConverted().getDivisor() + "\0");
				
			}else if(ApiMatch.getPlayerTeam1().getSets().size() == 3) {
				
				home_serve = ((Double.valueOf(ApiMatch.getPlayerTeam1().getSets().get(2).getStats().getServiceStats().getFirstServe().getDividend())/
						Double.valueOf(ApiMatch.getPlayerTeam1().getSets().get(2).getStats().getServiceStats().getFirstServe().getDivisor()))*100);
				
				home_serve_won = ((Double.valueOf(ApiMatch.getPlayerTeam1().getSets().get(2).getStats().getServiceStats().getFirstServePointsWon().getDividend())/
						Double.valueOf(ApiMatch.getPlayerTeam1().getSets().get(2).getStats().getServiceStats().getFirstServePointsWon().getDivisor()))*100);
				
				home_serve_won_second = ((Double.valueOf(ApiMatch.getPlayerTeam1().getSets().get(2).getStats().getServiceStats().getSecondServePointsWon().getDividend())/
						Double.valueOf(ApiMatch.getPlayerTeam1().getSets().get(2).getStats().getServiceStats().getSecondServePointsWon().getDivisor()))*100);
				
				away_serve = ((Double.valueOf(ApiMatch.getPlayerTeam2().getSets().get(2).getStats().getServiceStats().getFirstServe().getDividend())/
						Double.valueOf(ApiMatch.getPlayerTeam2().getSets().get(2).getStats().getServiceStats().getFirstServe().getDivisor()))*100);
				
				away_serve_won = ((Double.valueOf(ApiMatch.getPlayerTeam2().getSets().get(2).getStats().getServiceStats().getFirstServePointsWon().getDividend())/
						Double.valueOf(ApiMatch.getPlayerTeam2().getSets().get(2).getStats().getServiceStats().getFirstServePointsWon().getDivisor()))*100);
				
				away_serve_won_second = ((Double.valueOf(ApiMatch.getPlayerTeam2().getSets().get(2).getStats().getServiceStats().getSecondServePointsWon().getDividend())/
						Double.valueOf(ApiMatch.getPlayerTeam2().getSets().get(2).getStats().getServiceStats().getSecondServePointsWon().getDivisor()))*100);
				
				
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tAcesA" + " SET " + 
						ApiMatch.getPlayerTeam1().getSets().get(2).getStats().getServiceStats().getAces().getNumber() + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tAcesB" + " SET " + 
						ApiMatch.getPlayerTeam2().getSets().get(2).getStats().getServiceStats().getAces().getNumber() + "\0");
				
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tDoubleFaultsA" + " SET " + 
						ApiMatch.getPlayerTeam1().getSets().get(2).getStats().getServiceStats().getDoubleFaults().getNumber() + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tDoubleFaultsB" + " SET " + 
						ApiMatch.getPlayerTeam2().getSets().get(2).getStats().getServiceStats().getDoubleFaults().getNumber() + "\0");
				
				
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBreakingPointsA1" + " SET " + 
						ApiMatch.getPlayerTeam1().getSets().get(0).getStats().getReturnStats().getBreakPointsConverted().getDividend() + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBreakingPointsA2" + " SET " + 
						ApiMatch.getPlayerTeam1().getSets().get(0).getStats().getReturnStats().getBreakPointsConverted().getDivisor() + "\0");
				
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBreakingPointsB1" + " SET " + 
						ApiMatch.getPlayerTeam2().getSets().get(0).getStats().getReturnStats().getBreakPointsConverted().getDividend() + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tBreakingPointsB2" + " SET " + 
						ApiMatch.getPlayerTeam2().getSets().get(0).getStats().getReturnStats().getBreakPointsConverted().getDivisor() + "\0");
				
				
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "vBreakingPointSetNumber" + " SET " + "2" + "\0");
				
				print_writer.println("-1 RENDERER*TREE*$Main$All$BottomOut$BottomGrp3$AllHeader$SetNumberGrp$2$SetText1*GEOM*TEXT SET " + "1" + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main$All$BottomOut$BottomGrp3$AllHeader$SetNumberGrp$2$SetText2*GEOM*TEXT SET " + "2" + "\0");
				
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tSet1BreakingPointsA1" + " SET " + 
						ApiMatch.getPlayerTeam1().getSets().get(1).getStats().getReturnStats().getBreakPointsConverted().getDividend() + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tSet1BreakingPointsA2" + " SET " + 
						ApiMatch.getPlayerTeam1().getSets().get(1).getStats().getReturnStats().getBreakPointsConverted().getDivisor() + "\0");
				
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tSet1BreakingPointsB1" + " SET " + 
						ApiMatch.getPlayerTeam2().getSets().get(1).getStats().getReturnStats().getBreakPointsConverted().getDividend() + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tSet1BreakingPointsB2" + " SET " + 
						ApiMatch.getPlayerTeam2().getSets().get(1).getStats().getReturnStats().getBreakPointsConverted().getDivisor() + "\0");
				
				
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tSet2BreakingPointsA1" + " SET " + 
						ApiMatch.getPlayerTeam1().getSets().get(2).getStats().getReturnStats().getBreakPointsConverted().getDividend() + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tSet2BreakingPointsA2" + " SET " + 
						ApiMatch.getPlayerTeam1().getSets().get(2).getStats().getReturnStats().getBreakPointsConverted().getDivisor() + "\0");
				
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tSet2BreakingPointsB1" + " SET " + 
						ApiMatch.getPlayerTeam2().getSets().get(2).getStats().getReturnStats().getBreakPointsConverted().getDividend() + "\0");
				print_writer.println("-1 RENDERER*TREE*$Main*FUNCTION*ControlObject*in SET ON " + "tSet2BreakingPointsB2" + " SET " + 
						ApiMatch.getPlayerTeam2().getSets().get(2).getStats().getReturnStats().getBreakPointsConverted().getDivisor() + "\0");
			}
		}
		print_writer.println("-1 RENDERER*TREE*$Main$All$BottomOut$RightDataGrp$TopServeStatus$WWW$ServiceAll$ServiceGrp1$$ServiceDataGrp1*FUNCTION*BarValues*Bar_Value__1 SET " + Math.round(home_serve) + "\0");
		print_writer.println("-1 RENDERER*TREE*$Main$All$BottomOut$RightDataGrp$TopServeStatus$WWW$ServiceAll$ServiceGrp1$TopBar1*FUNCTION*BarValues*Bar_Value__1 SET " + Math.round(home_serve) + "\0");
		
		print_writer.println("-1 RENDERER*TREE*$Main$All$BottomOut$RightDataGrp$TopServeStatus$WWW$ServiceAll$ServiceGrp2$$ServiceDataGrp2*FUNCTION*BarValues*Bar_Value__1 SET " + Math.round(home_serve_won) + "\0");
		print_writer.println("-1 RENDERER*TREE*$Main$All$BottomOut$RightDataGrp$TopServeStatus$WWW$ServiceAll$ServiceGrp2$TopBar2*FUNCTION*BarValues*Bar_Value__1 SET " + Math.round(home_serve_won) + "\0");
		
		print_writer.println("-1 RENDERER*TREE*$Main$All$BottomOut$RightDataGrp$TopServeStatus$WWW$ServiceAll$ServiceGrp3$$ServiceDataGrp3*FUNCTION*BarValues*Bar_Value__1 SET " + Math.round(home_serve_won_second) + "\0");
		print_writer.println("-1 RENDERER*TREE*$Main$All$BottomOut$RightDataGrp$TopServeStatus$WWW$ServiceAll$ServiceGrp3$TopBar3*FUNCTION*BarValues*Bar_Value__1 SET " + Math.round(home_serve_won_second) + "\0");
		
		print_writer.println("-1 RENDERER*TREE*$Main$All$BottomOut$RightDataGrp$BottomServeStatus$WWW$ServiceAll$ServiceGrp1$ServiceDataGrp1*FUNCTION*BarValues*Bar_Value__1 SET " + Math.round(away_serve) + "\0");
		print_writer.println("-1 RENDERER*TREE*$Main$All$BottomOut$RightDataGrp$BottomServeStatus$WWW$ServiceAll$ServiceGrp1$BottomBar1*FUNCTION*BarValues*Bar_Value__1 SET " + Math.round(away_serve) + "\0");
		
		print_writer.println("-1 RENDERER*TREE*$Main$All$BottomOut$RightDataGrp$BottomServeStatus$WWW$ServiceAll$ServiceGrp2$ServiceDataGrp2*FUNCTION*BarValues*Bar_Value__1 SET " + Math.round(away_serve_won) + "\0");
		print_writer.println("-1 RENDERER*TREE*$Main$All$BottomOut$RightDataGrp$BottomServeStatus$WWW$ServiceAll$ServiceGrp2$BottomBar2*FUNCTION*BarValues*Bar_Value__1 SET " + Math.round(away_serve_won) + "\0");
		
		print_writer.println("-1 RENDERER*TREE*$Main$All$BottomOut$RightDataGrp$BottomServeStatus$WWW$ServiceAll$ServiceGrp3$ServiceDataGrp3*FUNCTION*BarValues*Bar_Value__1 SET " + Math.round(away_serve_won_second) + "\0");
		print_writer.println("-1 RENDERER*TREE*$Main$All$BottomOut$RightDataGrp$BottomServeStatus$WWW$ServiceAll$ServiceGrp3$BottomBar3*FUNCTION*BarValues*Bar_Value__1 SET " + Math.round(away_serve_won_second) + "\0");
	}
}
