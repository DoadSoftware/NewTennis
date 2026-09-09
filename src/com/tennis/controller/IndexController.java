package com.tennis.controller;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.tennis.containers.Scene;
import com.tennis.containers.ScoreBug;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tennis.broadcaster.ATP_2022;
import com.tennis.broadcaster.TPL_2023;
import com.tennis.model.Clock;
import com.tennis.model.Configurations;
import com.tennis.model.Event;
import com.tennis.model.EventFile;
import com.tennis.model.Fixture;
import com.tennis.model.Game;
import com.tennis.model.Match;
import com.tennis.model.Set;
import com.tennis.model.Stat;
import com.tennis.service.TennisService;
import com.tennis.util.TennisFunctions;
import com.tennis.util.TennisUtil;

@Controller
public class IndexController 
{
	@Autowired
	TennisService tennisService;
	
	public static String expiry_date = "2026-12-31";
	public static String current_date = "";
	public static String error_message = "";
	public static ATP_2022 this_ATP_2022;
	public static TPL_2023 this_TPL_2023;
	public static Match session_match = new Match();
	public static EventFile session_event = new EventFile();
	public static Clock session_clock = new Clock();
	public static Configurations session_Configurations = new Configurations();
	public List<Scene> session_selected_scenes = new ArrayList<Scene>();
	public static String session_selected_broadcaster="";
	public static Socket session_socket;
	public static PrintWriter print_writer;
	public static List<File> all_match_files;
	public static List<Fixture> all_db_fixture;
	public static File this_file = null;
	public static ObjectMapper objectMapper = new ObjectMapper(); 
	
	@RequestMapping(value = {"/","/initialise"}, method={RequestMethod.GET,RequestMethod.POST}) 
	public String initialisePage(ModelMap model) throws JAXBException, IOException, ParseException 
	{		
		if(current_date == null || current_date.isEmpty()) {
			current_date = TennisFunctions.getOnlineCurrentDate();
		}
		model.addAttribute("session_viz_scenes", new File(TennisUtil.TENNIS_DIRECTORY + TennisUtil.SCENES_DIRECTORY).listFiles(new FileFilter() {
			@Override
		    public boolean accept(File pathname) {
		        String name = pathname.getName().toLowerCase();
		        return name.endsWith(".via") && pathname.isFile();
		    }
		}));
		
		all_match_files = Arrays.asList(new File(TennisUtil.TENNIS_DIRECTORY + TennisUtil.MATCHES_DIRECTORY).listFiles(new FileFilter() {
			@Override
		    public boolean accept(File pathname) {
		        String name = pathname.getName().toLowerCase();
		        return name.endsWith(".json") && pathname.isFile();
		    }
		}));
		model.addAttribute("match_files",all_match_files);
		
		all_db_fixture = tennisService.getFixtures();
		
		if(new File(TennisUtil.TENNIS_DIRECTORY + TennisUtil.CONFIGURATIONS_DIRECTORY + TennisUtil.LOGGER_XML).exists()) {
			session_Configurations = (Configurations)JAXBContext.newInstance(Configurations.class).createUnmarshaller().unmarshal(
					new File(TennisUtil.TENNIS_DIRECTORY + TennisUtil.CONFIGURATIONS_DIRECTORY + TennisUtil.LOGGER_XML));
		} else {
			session_Configurations = new Configurations();
			JAXBContext.newInstance(Configurations.class).createMarshaller().marshal(session_Configurations, 
					new File(TennisUtil.TENNIS_DIRECTORY + TennisUtil.CONFIGURATIONS_DIRECTORY + TennisUtil.LOGGER_XML));
		}
		
		model.addAttribute("session_Configurations",session_Configurations);
	
		return "initialise";
	}
	
	@RequestMapping(value = {"/setup"}, method = RequestMethod.POST)
	public String setupPage(ModelMap model) throws JAXBException, IllegalAccessException, 
		InvocationTargetException, IOException, ParseException  
	{
		model.addAttribute("match_files", new File(TennisUtil.TENNIS_DIRECTORY + 
			TennisUtil.MATCHES_DIRECTORY ).listFiles(new FileFilter() {
			@Override
		    public boolean accept(File pathname) {
		        String name = pathname.getName().toLowerCase();
		        return name.endsWith(".json") && pathname.isFile();
		    }
			}));
		model.addAttribute("match_files", all_match_files);
		model.addAttribute("players", tennisService.getAllPlayer());
		model.addAttribute("teams", tennisService.getAllTeams());
		model.addAttribute("session_selected_broadcaster", session_selected_broadcaster);
		model.addAttribute("licence_expiry_message",
				"Software licence expires on " + new SimpleDateFormat("E, dd MMM yyyy").format(
				new SimpleDateFormat("yyyy-MM-dd").parse(expiry_date)));

		return "setup";
	}

	@RequestMapping(value = {"/stats_selection"}, method = RequestMethod.POST)
	public String statsSelectionPage(ModelMap model) 
	{
		model.addAttribute("session_match", session_match);
		return "stat";
	}
	
	@RequestMapping(value = {"/match"}, method = {RequestMethod.POST,RequestMethod.GET})
	public String footballMatchPage(ModelMap model, 
		@RequestParam(value = "selectedBroadcaster", required = false, defaultValue = "") String selectedBroadcaster,
		@RequestParam(value = "vizIPAddress", required = false, defaultValue = "") String vizIPAddresss,
		@RequestParam(value = "vizPortNumber", required = false, defaultValue = "") String vizPortNumber,
		@RequestParam(value = "vizScene", required = false, defaultValue = "") String vizScene)
			throws IOException, ParseException, JAXBException, InterruptedException  
	{
		if(current_date == null || current_date.isEmpty()) {
		
			model.addAttribute("error_message","You must be connected to the internet online");
			return "error";
		
		} else if(new SimpleDateFormat("yyyy-MM-dd").parse(expiry_date).before(new SimpleDateFormat("yyyy-MM-dd").parse(current_date))) {
			
			model.addAttribute("error_message","This software has expired");
			return "error";
			
		}else {
			
			all_match_files = Arrays.asList(new File(TennisUtil.TENNIS_DIRECTORY + TennisUtil.MATCHES_DIRECTORY).listFiles(new FileFilter() {
				@Override
			    public boolean accept(File pathname) {
			        String name = pathname.getName().toLowerCase();
			        return name.endsWith(".json") && pathname.isFile();
			    }
			}));
			model.addAttribute("match_files",all_match_files);

			model.addAttribute("licence_expiry_message",
				"Software licence expires on " + new SimpleDateFormat("E, dd MMM yyyy").format(
				new SimpleDateFormat("yyyy-MM-dd").parse(expiry_date)));
			
			session_match = new Match();
			session_event = new EventFile();
			if(session_event.getEvents() == null || session_event.getEvents().size() <= 0)
				session_event.setEvents(new ArrayList<Event>());
			if(session_match.getSets() == null || session_match.getSets().size() <= 0)
				session_match.setSets(new ArrayList<Set>());
			
			session_Configurations.setBroadcaster(selectedBroadcaster);
			session_Configurations.setVizscene(vizScene);
			session_Configurations.setIpAddress(vizIPAddresss);
			if(Integer.valueOf(vizPortNumber) != 0) {
				session_Configurations.setPortNumber(Integer.valueOf(vizPortNumber));
				session_socket = new Socket(vizIPAddresss, Integer.valueOf(vizPortNumber));
				print_writer = new PrintWriter(session_socket.getOutputStream(), true);
			}
			session_selected_broadcaster = selectedBroadcaster;
			
			switch (session_selected_broadcaster.toUpperCase()) {
			case TennisUtil.ATP_2022:
				session_selected_scenes.add(new Scene("/Default/ScoreBug-Single","FRONT_LAYER")); // Front layer
				session_selected_scenes.add(new Scene("","MIDDLE_LAYER"));
				//session_selected_scenes.get(0).scene_load(print_writer, session_selected_broadcaster);
				this_ATP_2022 = new ATP_2022();
				this_ATP_2022.scorebug = new ScoreBug();
				break;
			}
			
			JAXBContext.newInstance(Configurations.class).createMarshaller().marshal(session_Configurations, 
					new File(TennisUtil.TENNIS_DIRECTORY + TennisUtil.CONFIGURATIONS_DIRECTORY + TennisUtil.LOGGER_XML));
			
			model.addAttribute("session_selected_broadcaster", session_selected_broadcaster);
			
			return "match";
		}
	}
	
	@RequestMapping(value = {"/setup_to_match","/stat_to_match"}, method = RequestMethod.POST)
	public String backToMatchPage(ModelMap model) throws ParseException
	{
		if(current_date == null || current_date.isEmpty()) {
		
			model.addAttribute("error_message","You must be connected to the internet online");
			return "error";
		
		} else if(new SimpleDateFormat("yyyy-MM-dd").parse(expiry_date).before(new SimpleDateFormat("yyyy-MM-dd").parse(current_date))) {
			
			model.addAttribute("error_message","This software has expired");
			return "error";
			
		}else {
		
			all_match_files = Arrays.asList(new File(TennisUtil.TENNIS_DIRECTORY + TennisUtil.MATCHES_DIRECTORY).listFiles(new FileFilter() {
				@Override
			    public boolean accept(File pathname) {
			        String name = pathname.getName().toLowerCase();
			        return name.endsWith(".json") && pathname.isFile();
			    }
			}));
			model.addAttribute("match_files", all_match_files);
			model.addAttribute("licence_expiry_message",
				"Software licence expires on " + new SimpleDateFormat("E, dd MMM yyyy").format(
				new SimpleDateFormat("yyyy-MM-dd").parse(expiry_date)));
			
			model.addAttribute("session_selected_broadcaster", session_selected_broadcaster);

			return "match";
		
		}
	}	
	
	@RequestMapping(value = {"/upload_match_setup_data", "/reset_and_upload_match_setup_data"}
		,method={RequestMethod.GET,RequestMethod.POST})    
	public @ResponseBody String uploadFormDataToSessionObjects(HttpServletRequest request) 
			throws IllegalAccessException, InvocationTargetException, JAXBException, IOException
	{
		if (request.getRequestURI().contains("upload_match_setup_data") 
				|| request.getRequestURI().contains("reset_and_upload_match_setup_data")) {
			
	   		boolean reset_all_variables = false;
			if(request.getRequestURI().contains("reset_and_upload_match_setup_data")) {
				reset_all_variables = true;
			} else if(request.getRequestURI().contains("upload_match_setup_data")) {
				for (Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
					if(entry.getKey().equalsIgnoreCase("select_existing_tennis_matches") && entry.getValue()[0].equalsIgnoreCase("new_match")) {
						reset_all_variables = true;
						break;
					}
				}
			}
			if(reset_all_variables == true) {
				session_match = new Match(); 
				session_event = new EventFile();
				session_event.setEvents(new ArrayList<Event>());
			}
			
			for (Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
   				BeanUtils.setProperty(session_match, entry.getKey(), entry.getValue()[0]);
	   		}
			
			new File(TennisUtil.TENNIS_DIRECTORY + TennisUtil.MATCHES_DIRECTORY + session_match.getMatchFileName()).createNewFile();
			new File(TennisUtil.TENNIS_DIRECTORY + TennisUtil.EVENT_DIRECTORY + session_match.getMatchFileName()).createNewFile();
			
			new ObjectMapper().writeValue(new File(TennisUtil.TENNIS_DIRECTORY + TennisUtil.MATCHES_DIRECTORY +
				session_match.getMatchFileName()), session_match);
			new ObjectMapper().writeValue(new File(TennisUtil.TENNIS_DIRECTORY + TennisUtil.EVENT_DIRECTORY + 
				session_match.getMatchFileName()), session_event);

		}
		session_match.setEvents(session_event.getEvents());
		return objectMapper.writeValueAsString(session_match).toString();
	}
	
	@RequestMapping(value = {"/processTennisProcedures.html"}, method={RequestMethod.GET,RequestMethod.POST})    
	public @ResponseBody String processTennisProcedures(
			@RequestParam(value = "whatToProcess", required = false, defaultValue = "") String whatToProcess,
			@RequestParam(value = "valueToProcess", required = false, defaultValue = "") String valueToProcess)
					throws JAXBException, IllegalAccessException, InvocationTargetException, IOException, NumberFormatException, InterruptedException
	{	
		
		
		
		switch (whatToProcess.toUpperCase()) {
		case "READ_MATCH_DATA":
			
			session_match = TennisFunctions.populateMatchVariables(tennisService, session_match);
			return objectMapper.writeValueAsString(session_match).toString();

		case "LOG_SET_UNDO":
			
			for(Set set : session_match.getSets()) {
				if(set.getSet_number() == Integer.valueOf(valueToProcess.split(",")[0])) {
					for(Game game : set.getGames()) {
						if(game.getGame_number() == Integer.valueOf(valueToProcess.split(",")[1])) {
							if(valueToProcess.split(",")[2].equalsIgnoreCase(TennisUtil.GAME)) {
								game.setGame_winner(TennisUtil.HOME);
							}else if(valueToProcess.split(",")[3].equalsIgnoreCase(TennisUtil.GAME)) {
								game.setGame_winner(TennisUtil.AWAY);
							}
							game.setHome_score(valueToProcess.split(",")[2].toUpperCase());
							game.setAway_score(valueToProcess.split(",")[3].toUpperCase());
						}
					}
				}
			}
			
			new ObjectMapper().writeValue(new File(TennisUtil.TENNIS_DIRECTORY + TennisUtil.MATCHES_DIRECTORY +
					session_match.getMatchFileName()), session_match);

			return objectMapper.writeValueAsString(session_match).toString();
		case "NAMESUPER_GRAPHICS-OPTIONS": case "NAMESUPER-SP_GRAPHICS-OPTIONS": case "NAMESUPER-SP1_GRAPHICS-OPTIONS": case "NAMESUPER-DP1_GRAPHICS-OPTIONS":
		case "SINGLE-MATCHPROMO_GRAPHICS-OPTIONS": case "DOUBLE-MATCHPROMO_GRAPHICS-OPTIONS": case "SINGLE-LT_MATCHPROMO_GRAPHICS-OPTIONS": case "DOUBLE-LT_MATCHPROMO_GRAPHICS-OPTIONS":
			switch(session_selected_broadcaster) {
			case TennisUtil.ATP_2022:
				return (String) this_ATP_2022.ProcessGraphicOption(whatToProcess, session_match, tennisService, print_writer, session_selected_scenes, valueToProcess);
			case TennisUtil.TPL_2023:
				return (String) this_TPL_2023.ProcessGraphicOption(whatToProcess, session_match, tennisService, print_writer, session_selected_scenes, valueToProcess);
			}
			
		case "APIDATA_GRAPHICS-OPTIONS":
			String link = "https://api.protennislive.com/feeds/MatchStats/" + session_match.getMatchId();
			
			return objectMapper.writeValueAsString(TennisFunctions.getMatchStatsApi(link)).toString();
		case TennisUtil.READ_CLOCK:
			
			if(new File(TennisUtil.TENNIS_DIRECTORY + TennisUtil.CLOCK_XML).exists()) {
				session_match.setClock((Clock) JAXBContext.newInstance(Clock.class).createUnmarshaller().unmarshal(
					new File(TennisUtil.TENNIS_DIRECTORY + TennisUtil.CLOCK_XML)));
			}
			switch (session_selected_broadcaster) {
//			case TennisUtil.TPL_2023:
//				this_TPL_2023.updateScoreBug(session_selected_scenes, session_match, tennisService, print_writer);
//				break;
			case TennisUtil.ATP_2022:
				this_ATP_2022.updateScoreBug(session_selected_scenes, session_match, print_writer);
				break;
			}
			return objectMapper.writeValueAsString(session_match).toString();
			
		case TennisUtil.LOAD_MATCH: case TennisUtil.LOAD_SETUP: case "LOAD_MATCH_AFTER_STAT_LOG": case "READ_MATCH_FOR_STATS":
			
			switch (whatToProcess.toUpperCase()) {
			case TennisUtil.LOAD_MATCH: case TennisUtil.LOAD_SETUP: 
				session_match = TennisFunctions.populateMatchVariables(tennisService, new ObjectMapper().readValue(
						new File(TennisUtil.TENNIS_DIRECTORY + TennisUtil.MATCHES_DIRECTORY + valueToProcess), Match.class));					
				
				if(session_match.getSets() == null) {
					session_match.setSets(new ArrayList<Set>());
				}
				
				switch(whatToProcess.toUpperCase()) {
				case TennisUtil.LOAD_MATCH:
					
					switch (session_selected_broadcaster) {
					case TennisUtil.TPL_2023:
						if(all_db_fixture != null) {
							Fixture curr_fixture = all_db_fixture.stream().filter(fix -> 
							fix.getMatchfilename().equalsIgnoreCase(session_match.getMatchFileName())).findAny().orElse(null);						
							Match this_match = new Match();
							session_match.setHome_total_score(0);
							session_match.setAway_total_score(0);
							
							if(curr_fixture != null) {
								for (Fixture fixture : all_db_fixture.stream().filter(fix -> fix.getMatchNumber()==curr_fixture.getMatchNumber()).collect(Collectors.toList())) {
									this_file = all_match_files.stream().filter(fil -> fil.getName().equalsIgnoreCase(fixture.getMatchfilename())).findAny().orElse(null);
									if(this_file != null) {
										if(!this_file.getName().equalsIgnoreCase(session_match.getMatchFileName())) {
											this_match = TennisFunctions.populateMatchVariables(tennisService, new ObjectMapper().readValue(
													new File(TennisUtil.TENNIS_DIRECTORY + TennisUtil.MATCHES_DIRECTORY + this_file.getName()), Match.class));
											if(session_match.getHomeFirstPlayer().getTeamId()==this_match.getHomeFirstPlayer().getTeamId()
													|| session_match.getHomeFirstPlayer().getTeamId()==this_match.getAwayFirstPlayer().getTeamId()
													|| session_match.getAwayFirstPlayer().getTeamId()==this_match.getAwayFirstPlayer().getTeamId()
													|| session_match.getAwayFirstPlayer().getTeamId()==this_match.getAwayFirstPlayer().getTeamId())
												{
												if(this_match.getSets() != null) {
														for (Set set : this_match.getSets()) {
															for (Game game : set.getGames()) {
																if(session_match.getHomeFirstPlayer().getTeamId()==this_match.getHomeFirstPlayer().getTeamId()) {
																	session_match.setHome_total_score(session_match.getHome_total_score()+Integer.valueOf(game.getHome_score()));
																}else if(session_match.getHomeFirstPlayer().getTeamId()==this_match.getAwayFirstPlayer().getTeamId()) {												
																	session_match.setHome_total_score(session_match.getHome_total_score()+Integer.valueOf(game.getAway_score()));
																}
																
																if(session_match.getAwayFirstPlayer().getTeamId()==this_match.getAwayFirstPlayer().getTeamId()) {
																	session_match.setAway_total_score(session_match.getAway_total_score()+Integer.valueOf(game.getAway_score()));
																}else if(session_match.getAwayFirstPlayer().getTeamId()==this_match.getHomeFirstPlayer().getTeamId()) {
																	session_match.setAway_total_score(session_match.getAway_total_score()+Integer.valueOf(game.getHome_score()));
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						break;
						}
					break;
					}
				break;
				}
				return objectMapper.writeValueAsString(session_match).toString();
			
		case TennisUtil.LOG_SET:
			
			if(session_match.getSets() == null) {
				session_match.setSets(new ArrayList<Set>());
			}
			if(valueToProcess.toUpperCase().contains(TennisUtil.START)) {
				session_match.getSets().add(new Set(session_match.getSets().size() + 1, TennisUtil.START, new ArrayList<Game>()));
			} else if(valueToProcess.toUpperCase().contains(TennisUtil.RESET)) {
				session_match.getSets().remove(session_match.getSets().size()-1);
			} else if(valueToProcess.toUpperCase().contains(TennisUtil.END)) {
				session_match.getSets().get(session_match.getSets().size()-1).setSet_status(TennisUtil.END);
				session_match.getSets().get(session_match.getSets().size()-1).setSet_winner(valueToProcess.split(",")[1]);
			}
			new ObjectMapper().writeValue(new File(TennisUtil.TENNIS_DIRECTORY + TennisUtil.MATCHES_DIRECTORY +
					session_match.getMatchFileName()), session_match);
			return objectMapper.writeValueAsString(session_match).toString();
			
		case TennisUtil.LOG_GAME: case TennisUtil.LOG_SERVE:
			
			if(session_match.getSets() == null) {
				session_match.setSets(new ArrayList<Set>());
			}
			if(session_match.getSets().get(session_match.getSets().size()-1).getGames() == null) {
				session_match.getSets().get(session_match.getSets().size()-1).setGames(new ArrayList<Game>());
			}
			switch (whatToProcess.toUpperCase()) {
			case TennisUtil.LOG_SERVE:
				
				session_match.getSets().get(session_match.getSets().size()-1).getGames().get(
					session_match.getSets().get(session_match.getSets().size()-1).getGames().size()-1)
					.setServing_player(Integer.valueOf(valueToProcess));
				
				break;
				
			case TennisUtil.LOG_GAME: 
				
				if(valueToProcess.toUpperCase().contains(TennisUtil.START)) {
					session_match.getSets().get(session_match.getSets().size()-1).getGames().add(
						new Game(session_match.getSets().get(session_match.getSets().size()-1).getGames().size() + 1, 
						TennisUtil.LOVE, TennisUtil.LOVE, TennisUtil.START, valueToProcess.split(",")[1]));
				} else if(valueToProcess.toUpperCase().contains(TennisUtil.RESET)) {
					session_match.getSets().get(session_match.getSets().size()-1).getGames().remove(
							session_match.getSets().get(session_match.getSets().size()-1).getGames().size()-1);
				} else if(valueToProcess.toUpperCase().contains(TennisUtil.END)) {
					
					switch (session_Configurations.getBroadcaster().toUpperCase()) {
					case TennisUtil.TPL_2023:
						if(Integer.valueOf(session_match.getSets().get(session_match.getSets().size()-1).getGames().get(
									session_match.getSets().get(session_match.getSets().size()-1)
									.getGames().size()-1).getHome_score()) == Integer.valueOf(session_match.getSets().get(session_match.getSets().size()-1).getGames().get(
									session_match.getSets().get(session_match.getSets().size()-1)
									.getGames().size()-1).getAway_score())) {
									
							session_match.getSets().get(session_match.getSets().size()-1).getGames().get(
								session_match.getSets().get(session_match.getSets().size()-1)
								.getGames().size()-1).setGame_status(TennisUtil.END);
							session_match.getSets().get(session_match.getSets().size()-1).getGames().get(
									session_match.getSets().get(session_match.getSets().size()-1)
									.getGames().size()-1).setGame_winner("TIE");
						}else {
							session_match.getSets().get(session_match.getSets().size()-1).getGames().get(
								session_match.getSets().get(session_match.getSets().size()-1)
								.getGames().size()-1).setGame_status(TennisUtil.END);
							session_match.getSets().get(session_match.getSets().size()-1).getGames().get(
									session_match.getSets().get(session_match.getSets().size()-1)
									.getGames().size()-1).setGame_winner(valueToProcess.split(",")[1]);
						}
						break;

					default:
						session_match.getSets().get(session_match.getSets().size()-1).getGames().get(
								session_match.getSets().get(session_match.getSets().size()-1)
								.getGames().size()-1).setGame_status(TennisUtil.END);
							session_match.getSets().get(session_match.getSets().size()-1).getGames().get(
									session_match.getSets().get(session_match.getSets().size()-1)
									.getGames().size()-1).setGame_winner(valueToProcess.split(",")[1]);
						break;
					}
				}
				
				
				int curr_game_home_score = Integer.valueOf(session_match.getSets().get(session_match.getSets().size()-1).getGames().get(
						session_match.getSets().get(session_match.getSets().size()-1)
						.getGames().size()-1).getHome_score());
				
				int curr_game_away_score = Integer.valueOf(session_match.getSets().get(session_match.getSets().size()-1).getGames().get(
						session_match.getSets().get(session_match.getSets().size()-1)
						.getGames().size()-1).getAway_score());
				
				int home_past_total_score = session_match.getHome_total_score();
				int away_past_total_score = session_match.getAway_total_score();
				
				String home_team = session_match.getHomeFirstPlayer().getTeam() != null ? session_match.getHomeFirstPlayer().getTeam().getTeamName1() : "";
				String away_team = session_match.getAwayFirstPlayer().getTeam() != null ? session_match.getAwayFirstPlayer().getTeam().getTeamName1() : "";
				
//				System.out.println(home_past_total_score+curr_game_home_score);
//				System.out.println(away_past_total_score+curr_game_away_score);
				
				System.out.println(home_team +" : "+(home_past_total_score+curr_game_home_score));
				System.out.println(away_team+" : "+(away_past_total_score+curr_game_away_score));
				break;
			}

			new ObjectMapper().writeValue(new File(TennisUtil.TENNIS_DIRECTORY + TennisUtil.MATCHES_DIRECTORY +
				session_match.getMatchFileName()), session_match);
			return objectMapper.writeValueAsString(session_match).toString();

		case TennisUtil.LOG_STAT:

			if(session_match.getSets().get(session_match.getSets().size()-1)
				.getGames().get(session_match.getSets().get(
				session_match.getSets().size()-1).getGames().size()-1).getStats() == null) {
				
				session_match.getSets().get(session_match.getSets().size()-1)
				.getGames().get(session_match.getSets().get(
				session_match.getSets().size()-1).getGames().size()-1).setStats(new ArrayList<Stat>());
			
			}
			
			session_match = TennisFunctions.processStats(session_match, valueToProcess);
			
			new ObjectMapper().writeValue(new File(TennisUtil.TENNIS_DIRECTORY + TennisUtil.MATCHES_DIRECTORY +
				session_match.getMatchFileName()), session_match);
			return objectMapper.writeValueAsString(session_match).toString();

		case TennisUtil.LOG_SCORE:
			
			if (valueToProcess.toUpperCase().contains(TennisUtil.SCORE)) {
				if (valueToProcess.toUpperCase().contains(TennisUtil.HOME)) {
					if(valueToProcess.toUpperCase().contains(TennisUtil.INCREMENT)) {
						
						session_match.getSets().get(session_match.getSets().size()-1)
							.getGames().get(session_match.getSets().get(
							session_match.getSets().size()-1).getGames().size()-1).setHome_score(
							TennisFunctions.processScore(session_match, session_match.getSets().size() - 1, 
							session_match.getSets().get(session_match.getSets().size()-1).getGames().size()-1, 
							TennisUtil.HOME, TennisUtil.INCREMENT));
						
					} else if(valueToProcess.toUpperCase().contains(TennisUtil.DECREMENT)) {

						session_match.getSets().get(session_match.getSets().size()-1)
							.getGames().get(session_match.getSets().get(
							session_match.getSets().size()-1).getGames().size()-1).setHome_score(
							TennisFunctions.processScore(session_match, session_match.getSets().size() - 1, 
							session_match.getSets().get(session_match.getSets().size()-1).getGames().size()-1, 
							TennisUtil.HOME, TennisUtil.DECREMENT));

					}
					
				} else if (valueToProcess.toUpperCase().contains(TennisUtil.AWAY)) {
					if(valueToProcess.toUpperCase().contains(TennisUtil.INCREMENT)) {

						session_match.getSets().get(session_match.getSets().size()-1)
							.getGames().get(session_match.getSets().get(
							session_match.getSets().size()-1).getGames().size()-1).setAway_score(
							TennisFunctions.processScore(session_match, session_match.getSets().size() - 1, 
							session_match.getSets().get(session_match.getSets().size()-1).getGames().size()-1, 
							TennisUtil.AWAY, TennisUtil.INCREMENT));
						
					} else if(valueToProcess.toUpperCase().contains(TennisUtil.DECREMENT)) {

						session_match.getSets().get(session_match.getSets().size()-1)
							.getGames().get(session_match.getSets().get(
							session_match.getSets().size()-1).getGames().size()-1).setAway_score(
							TennisFunctions.processScore(session_match, session_match.getSets().size() - 1, 
							session_match.getSets().get(session_match.getSets().size()-1).getGames().size()-1, 
							TennisUtil.AWAY, TennisUtil.DECREMENT));

					}
				}
			}
			new ObjectMapper().writeValue(new File(TennisUtil.TENNIS_DIRECTORY + TennisUtil.MATCHES_DIRECTORY +
				session_match.getMatchFileName()), session_match);
			return objectMapper.writeValueAsString(session_match).toString();
			
		default:
			
			switch (session_selected_broadcaster) {
			case TennisUtil.TPL_2023:
				this_TPL_2023.ProcessGraphicOption(whatToProcess, session_match, tennisService, print_writer, session_selected_scenes, valueToProcess);
				break;
			case TennisUtil.ATP_2022:
				this_ATP_2022.ProcessGraphicOption(whatToProcess, session_match, tennisService, print_writer, session_selected_scenes, valueToProcess);
				break;
			}
			return objectMapper.writeValueAsString(session_match).toString();
		}
	}
}