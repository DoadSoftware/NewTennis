var match_data,home_team_id,away_team_id,match_type;
function processWaitingButtonSpinner(whatToProcess) 
{
	switch (whatToProcess) {
	case 'START_WAIT_TIMER': 
		$('.spinner-border').show();
		$(':button').prop('disabled', true);
		break;	
	case 'END_WAIT_TIMER': 
		$('.spinner-border').hide();
		$(':button').prop('disabled', false);
		break;
	}
}

function afterPageLoad(whichPageHasLoaded)
{
	switch (whichPageHasLoaded) {
	case 'SETUP':
		processTennisProcedures('READ_MATCH_DATA',null);
		$('#homeFirstPlayerId').select2();
		$('#homeSecondPlayerId').select2();
		$('#awayFirstPlayerId').select2();
		$('#awaySecondPlayerId').select2();
		addItemsToList('LOAD_SELECTED_TEAM_PLAYERS', document.getElementById('homeTeamId'));
		addItemsToList('LOAD_SELECTED_TEAM_PLAYERS', document.getElementById('awayTeamId'));
		break;
	case 'MATCH':
		if(window.location.href.toLowerCase().includes('/stat_to_match')) {
			processTennisProcedures('LOAD_MATCH_AFTER_STAT_LOG',null);
		}
		break;
	}
}
function secondsTimeSpanToHMS(s) {
  var h = Math.floor(s / 3600); //Get whole hours
  s -= h * 3600;
  var m = Math.floor(s / 60); //Get remaining minutes
  s -= m * 60;
  return h + ":" + (m < 10 ? '0' + m : m) + ":" + (s < 10 ? '0' + s : s); //zero padding on minutes and seconds
}
function displayMatchTime() {
	processTennisProcedures('READ_CLOCK',null);
}
function initialiseForm(whatToProcess, dataToProcess)
{
	switch (whatToProcess) {
	case 'LOAD_STAT':

		$('input[id*="_txt"]').val('0');
			
		if(match_data) {
			match_data.sets.forEach(function(set,set_index,set_arr){
				if(set.set_status.toLowerCase() == 'start') {
					set.games.forEach(function(game,game_index,game_arr){
						if(game.game_status.toLowerCase() == 'start') {
							game.stats.forEach(function(stat,stat_index,stat_arr){
								switch(stat.playerId) {
								case match_data.homeFirstPlayerId:
									$('#' + stat.statType + '_home_first_txt').val(
										parseInt($('#' + stat.statType + '_home_first_txt').val()) + parseInt(1));
									break;
								case match_data.homeSecondPlayerId:
									$('#' + stat.statType + '_home_second_txt').val(
										parseInt($('#' + stat.statType + '_home_second_txt').val()) + parseInt(1));
									break;
								case match_data.awayFirstPlayerId:
									$('#' + stat.statType + '_away_first_txt').val(
										parseInt($('#' + stat.statType + '_away_first_txt').val()) + parseInt(1));
									break;
								case match_data.awaySecondPlayerId:
									$('#' + stat.statType + '_away_second_txt').val(
										parseInt($('#' + stat.statType + '_away_second_txt').val()) + parseInt(1));
									break;
								}
							});
						}
					});
				}			
			});
		}
		break;
		
	case 'TIME':
		
		if(match_data) {
			if(document.getElementById('match_time_hdr')) {
				document.getElementById('match_time_hdr').innerHTML = 'MATCH TIME : ' + 
					secondsTimeSpanToHMS(match_data.clock.matchTotalSeconds);
			}
		}
		
		break;
	
	case 'SETUP':
		if(dataToProcess) {
			document.getElementById('homeTeamId').value = dataToProcess.homeFirstPlayer.team.teamId;
			document.getElementById('awayTeamId').value = dataToProcess.awayFirstPlayer.team.teamId;
			document.getElementById('matchFileName').value = dataToProcess.matchFileName;
			document.getElementById('tournament').value = dataToProcess.tournament;
			document.getElementById('matchIdent').value = dataToProcess.matchIdent;
			document.getElementById('matchId').value = dataToProcess.matchId;
			document.getElementById('categoryType').value = dataToProcess.categoryType;
			document.getElementById('matchType').value = dataToProcess.matchType;
			document.getElementById('tieBreakerRule').value = dataToProcess.tieBreakerRule;
			document.getElementById('advantageRule').value = dataToProcess.advantageRule;
			document.getElementById('homeFirstPlayerId').value = dataToProcess.homeFirstPlayerId;
			document.getElementById('homeSecondPlayerId').value = dataToProcess.homeSecondPlayerId;
			document.getElementById('awayFirstPlayerId').value = dataToProcess.awayFirstPlayerId;
			document.getElementById('awaySecondPlayerId').value = dataToProcess.awaySecondPlayerId;
			
		} else {
			document.getElementById('homeTeamId').value = 0;
			document.getElementById('awayTeamId').value = 0;
			document.getElementById('matchFileName').value = '';
			document.getElementById('tournament').value = '';
			document.getElementById('matchIdent').value = '';
			document.getElementById('matchId').value = '';
			document.getElementById('categoryType').selectedIndex = 0;
			document.getElementById('matchType').selectedIndex = 0;
			document.getElementById('tieBreakerRule').selectedIndex = 0;
			document.getElementById('advantageRule').selectedIndex = 0;
			document.getElementById('homeFirstPlayerId').selectedIndex = 0;
			document.getElementById('homeSecondPlayerId').selectedIndex = 0;
			document.getElementById('awayFirstPlayerId').selectedIndex = 0;
			document.getElementById('awaySecondPlayerId').selectedIndex = 0;
		}
		processUserSelection($('#matchType'));
		afterPageLoad('SETUP');
		break;
	}
}
function uploadFormDataToSessionObjects(whatToProcess)
{
	var formData = new FormData();
	var url_path;

	$('input, select, textarea').each(
		function(index){  
			if($(this).is("select")) {
				formData.append($(this).attr('id'),$('#' + $(this).attr('id') + ' option:selected').val());  
			} else {
				formData.append($(this).attr('id'),$(this).val());  
			}	
		}
	);
	
	switch(whatToProcess.toUpperCase()) {
	case 'RESET_MATCH':
		url_path = 'reset_and_upload_match_setup_data';
		break;
	case 'SAVE_MATCH':
		url_path = 'upload_match_setup_data';
		break;
	}
	
	$.ajax({    
		headers: {'X-CSRF-TOKEN': $('meta[name="_csrf"]').attr('content')},
        url : url_path,     
        data : formData,
        cache: false,
        contentType: false,
        processData: false,
        type: 'POST',     
        success : function(data) {

        	switch(whatToProcess.toUpperCase()) {
        	case 'RESET_MATCH':
        		alert('Match has been reset');
        		processWaitingButtonSpinner('END_WAIT_TIMER');
        		break;
        	case 'SAVE_MATCH':
        		document.setup_form.method = 'post';
        		document.setup_form.action = 'setup_to_match';
        	   	document.setup_form.submit();
        		break;
        	}
        	
        },    
        error : function(e) {    
       	 	console.log('Error occured in uploadFormDataToSessionObjects with error description = ' + e);     
        }    
    });		
	
}
function processUserSelectionData(whatToProcess,dataToProcess){
	//alert(whatToProcess);
	switch (whatToProcess) {
	case 'LOGGER_FORM_KEYPRESS':
		if($('#log_game_undo_btn').val()) { // Ignore keypress when user is working with UNDO
			return false;
		}
		switch (dataToProcess) {
		case 32:
			processTennisProcedures('CLEAR-ALL');
			break;
		case 189:
			if(confirm('It will Also Delete Your Preview from Directory...\r\n\r\n Are You Sure To Animate Out?') == true){
				processTennisProcedures('ANIMATE-OUT');
			}
			break;	
		case 187:
			processTennisProcedures('ANIMATE-OUT-SCOREBUG');
			break;
		case 79:
			processTennisProcedures('ANIMATE-OUT-SCOREBUG_STAT');
			break;
		case 112:
			processTennisProcedures('POPULATE-SCOREBUG');  //DONE
			break;
		case 113:
			processTennisProcedures('POPULATE-MATCHID');   //DONE
			break;
		case 114:
			processTennisProcedures('POPULATE-MATCHID_DOUBLE');
			break;
		case 115:
			processTennisProcedures('POPULATE-LT-MATCHID');
			break;
		case 116:
			processTennisProcedures('POPULATE-LT-MATCHID_DOUBLE');
			break;
		case 117:
			processTennisProcedures('POPULATE-LT-MATCH_RESULTSINGLES');   //DONE
			break;
		case 118:
			processTennisProcedures('POPULATE-LT-MATCH_RESULTDOUBLES');
			break;
		case 119:
			processTennisProcedures('POPULATE-FF-MATCH_RESULTSINGLES');
			break;
		/*case 120:
			processTennisProcedures('POPULATE-FF-MATCH_RESULTDOUBLES');
			break;*/
		case 120:   
			processTennisProcedures('POPULATE-FIX_AND_RES');   //DONE
			break;
		case 121:
			$("#select_event_div").hide();                 //DONE
			$("#match_configuration").hide();
			$("#tennis_div").hide();
			processTennisProcedures('NAMESUPER_GRAPHICS-OPTIONS');
			break;
		case 122:      										//DONE
			$("#select_event_div").hide();
			$("#match_configuration").hide();
			$("#tennis_div").hide();
			match_type = match_data.matchType;
			if(match_data.matchType == 'singles'){
				home_team_id1 = match_data.homeFirstPlayerId;
				away_team_id1 = match_data.awayFirstPlayerId;
			}else if(match_data.matchType == 'doubles'){
				home_team_id1 = match_data.homeFirstPlayerId;
				home_team_id2 = match_data.homeSecondPlayerId;
				away_team_id1 = match_data.awayFirstPlayerId;
				away_team_id2 = match_data.awaySecondPlayerId;
			}
			processTennisProcedures('NAMESUPER-SP_GRAPHICS-OPTIONS');
			break;
		case 123:
			$("#select_event_div").hide();
			$("#match_configuration").hide();
			$("#tennis_div").hide();
			addItemsToList('NAMESUPER-DP-OPTIONS',null);
			break;
			
		case 84:
			$("#select_event_div").hide();
			$("#match_configuration").hide();
			$("#tennis_div").hide();
			processTennisProcedures('NAMESUPER-SP1_GRAPHICS-OPTIONS');
			break;
		case 89:
			$("#select_event_div").hide();
			$("#match_configuration").hide();
			$("#tennis_div").hide();
			processTennisProcedures('NAMESUPER-DP1_GRAPHICS-OPTIONS');
			break;
			
		case 69:
			$("#select_event_div").hide();
			$("#match_configuration").hide();
			$("#tennis_div").hide();
			processTennisProcedures('SINGLE-LT_MATCHPROMO_GRAPHICS-OPTIONS');
			break;
		case 82:
			$("#select_event_div").hide();
			$("#match_configuration").hide();
			$("#tennis_div").hide();
			processTennisProcedures('DOUBLE-LT_MATCHPROMO_GRAPHICS-OPTIONS');
			break;
			
		case 81:
			$("#select_event_div").hide();
			$("#match_configuration").hide();
			$("#tennis_div").hide();
			processTennisProcedures('SINGLE-MATCHPROMO_GRAPHICS-OPTIONS');
			break;
		case 87:
			$("#select_event_div").hide();
			$("#match_configuration").hide();
			$("#tennis_div").hide();
			processTennisProcedures('DOUBLE-MATCHPROMO_GRAPHICS-OPTIONS');
			break;
		
		case 90:
			$("#select_event_div").hide();
			$("#match_configuration").hide();
			$("#tennis_div").hide();
			addItemsToList('CROSS-OPTIONS',null);
			break;
		case 65:
			processTennisProcedures('POPULATE-LT-MATCH_SCORESINGLES');
			break;
		case 68:
			processTennisProcedures('POPULATE-LT-MATCH_SCOREDOUBLES');
			break;
			
		case 73:
			$("#select_event_div").hide();
			$("#match_configuration").hide();
			$("#tennis_div").hide();
			addItemsToList('SCOREBUG_OPTION',null);
			processTennisProcedures('APIDATA_GRAPHICS-OPTIONS');
			break;
		case 76:
			$("#select_event_div").hide();
			$("#match_configuration").hide();
			$("#tennis_div").hide();
			addItemsToList('SCOREBUG-SET_OPTION',null); 
			break;
		/*case 75:
			$("#select_event_div").hide();
			$("#match_configuration").hide();
			$("#tennis_div").hide();
			addItemsToList('SCOREBUG_STATS_OPTION',null);
			break;*/
		case 72:
			addItemsToList('SCOREBUG-HEADER_OPTION',null);
			//processTennisProcedures('POPULATE-SCOREBUG_HEADER');
			break;
		case 74:
			processTennisProcedures('ANIMATE-OUT-SCOREBUG_HEADER');
			break;
		case 83:
			processTennisProcedures('POPULATE-MATCH_STATS');
			break;
		case 88:
			addItemsToList('SPEED_OPTION',null);
			break;				
		}
		
		break;
	}
}
function processUserSelection(whichInput)
{	
	var error_msg = '';

	switch ($(whichInput).attr('name')) {
	case 'log_game_undo_btn':
		if($('#undo_home_score_txt').val().toUpperCase() != 'GAME' && $('#undo_away_score_txt').val().toUpperCase() != 'GAME') {
			alert('Either the home score or the away score must have the value GAME');
			return false;
		}
		processWaitingButtonSpinner('START_WAIT_TIMER');
		processTennisProcedures('LOG_SET_UNDO',null);
		break;
	case 'select_undo_set_number':
		addItemsToList('LOAD_GAMES', null);
		break;
	case 'select_undo_game_number':
		addItemsToList('LOAD_GAME_SCORES', null);
		break;
	case 'undo_set_btn':
		if(match_data.sets.length <= 0) {
			alert('No set started yet. Undo is not available');
			return false;			
		}
		error_msg = 'No Set/Game found. Undo not available';
		match_data.sets.forEach(function(set,set_index,set_arr){
			if(set.games.length <= 0) {
				alert('No game started yet. Undo is not available');
				return false;			
			}
		});
		addItemsToList('LOAD_UNDO', match_data);
		break;
	case 'exit_stat_page_btn':
		document.tennis_stat_form.method = 'post';
		document.tennis_stat_form.action = 'stat_to_match';
	   	document.tennis_stat_form.submit();
		break;
	case 'load_stat':
		document.tennis_form.method = 'post';
		document.tennis_form.action = 'stats_selection';
	   	document.tennis_form.submit();
		break;
	case 'select_serving_player':
		if(match_data.sets.length <= 0) {
			alert('No set started yet. Serve selection is not available');
			return false;			
		}
		match_data.sets.forEach(function(set,set_index,set_arr){
			if(set.set_status.toLowerCase() == 'start') {
				set.games.forEach(function(game,game_index,game_arr){
					if(game.game_status.toLowerCase() == 'start') {
						error_msg = 'Game number ' + game.game_number + ' already in play. End this game first before starting a new one';
					}
				});
			}
		});
		if(error_msg) {
			processWaitingButtonSpinner('START_WAIT_TIMER');
			processTennisProcedures('LOG_SERVE',null);
		} else {
			alert('No game started yet. Serve selection is not available');
			return false;			
		}
		break;
	case 'matchType':
		if($('#matchType option:selected').val() == 'singles') {
			document.getElementById('select_double_player_row').style.display = 'none';
		} else {
			document.getElementById('select_double_player_row').style.display = '';
		}
		break;
	case 'start_set_btn': 
		match_data.sets.forEach(function(set,set_index,set_arr){
			if(set.set_status.toLowerCase() == 'start') {
				error_msg = 'Set number ' + set.set_number + ' already in play. End this set first before starting a new one';
			}
		});
		if(error_msg) {
			alert(error_msg);
			return false;			
		} else {
			processWaitingButtonSpinner('START_WAIT_TIMER');
			processTennisProcedures('LOG_SET','START');
		}
		break;
	case 'end_set_btn': case 'reset_set_btn':
		error_msg = 'Cannot find any started set. Please start a set first';
		match_data.sets.forEach(function(set,set_index,set_arr){
			if(set.set_status.toLowerCase() == 'start') {
				error_msg = '';
			}
		});
		if(error_msg) {
			alert(error_msg);
			return false;			
		} else {
			switch ($(whichInput).attr('name')) {
			case 'end_set_btn': 
				if(confirm('Confirm ' + $('#select_set_winner option:selected').text() + ' wins this set?')) {
					processWaitingButtonSpinner('START_WAIT_TIMER');
					processTennisProcedures('LOG_SET','END');
				}
				break;
			case 'reset_set_btn':
				if(confirm('Do you really wish to RESET this set?')) {
					processWaitingButtonSpinner('START_WAIT_TIMER');
					processTennisProcedures('LOG_SET','RESET');
				}
				break;
			}
		}
		break;
	case 'start_game_btn':
		if(match_data.sets.length <= 0) {
			alert('No set started yet. Starting a game before starting a set is an invalid selection');
			return false;			
		}
		match_data.sets.forEach(function(set,set_index,set_arr){
			if(set.set_status.toLowerCase() == 'start') {
				set.games.forEach(function(game,game_index,game_arr){
					if(game.game_status.toLowerCase() == 'start') {
						error_msg = 'Game number ' + game.game_number + ' already in play. End this game first before starting a new one';
					}
				});
			}
		});
		if(error_msg) {
			alert(error_msg);
			return false;			
		} else {
			processWaitingButtonSpinner('START_WAIT_TIMER');
			processTennisProcedures('LOG_GAME','START');
		}
		break;
	case 'end_game_btn': case 'reset_game_btn':
		if(match_data.sets.length <= 0) {
			alert('No set started yet. This button is not available');
			return false;			
		}
		error_msg = 'Cannot find any started game. Please start a game first before using this option';
		match_data.sets.forEach(function(set,set_index,set_arr){
			if(set.set_status.toLowerCase() == 'start') {
				set.games.forEach(function(game,game_index,game_arr){
					if(game.game_status.toLowerCase() == 'start') {
						error_msg = '';
					}
				});
			}
		});
		if(error_msg) {
			alert(error_msg);
			return false;			
		} else {
			switch ($(whichInput).attr('name')) {
			case 'end_game_btn': 
			
				if(parseInt($('#homeScore').val()) == parseInt($('#awayScore').val())) {
					alert("It's a TIE");
					processWaitingButtonSpinner('START_WAIT_TIMER');
					processTennisProcedures('LOG_GAME','END');
				}else{
					confirm('Confirm ' + $('#select_game_winner option:selected').text() + ' wins this game?')
					processWaitingButtonSpinner('START_WAIT_TIMER');
					processTennisProcedures('LOG_GAME','END');
				}/*else{
					processUserSelection(document.getElementById('away_decrement_score_btn'));
					processUserSelection(document.getElementById('home_decrement_score_btn'));
					processWaitingButtonSpinner('START_WAIT_TIMER');
					processTennisProcedures('LOG_SCORE',whichInput);
				}*/
				break;
			case 'reset_game_btn':
				if(confirm('Do you really wish to RESET this game?')) {
					processWaitingButtonSpinner('START_WAIT_TIMER');
					processTennisProcedures('LOG_GAME','RESET');
				}
				break;
			}
		}
		break;
	case 'load_scene_btn':
		/*if(checkEmpty($('#vizIPAddress'),'IP Address Blank') == false
			|| checkEmpty($('#vizPortNumber'),'Port Number Blank') == false) {
			return false;
		}*/
	  	document.initialise_form.submit();
		break;
	case 'cancel_match_setup_btn':
		document.setup_form.method = 'post';
		document.setup_form.action = 'setup_to_match';
	   	document.setup_form.submit();
		break;
	case 'matchFileName':
		if(document.getElementById('matchFileName').value) {
			document.getElementById('matchFileName').value = 
				document.getElementById('matchFileName').value.replace('.json','') + '.json';
		}
		break;
	case 'save_match_btn': case 'reset_match_btn':
		switch ($(whichInput).attr('name')) {
		case 'reset_match_btn':
	    	if (confirm('The setup selections of this match will be retained ' +
	    			'but the match data will be deleted permanently. Are you sure, you want to RESET this match?') == false) {
	    		return false;
	    	}
			break;
		}
		if (!checkEmpty(document.getElementById('matchFileName'),'Match Name')) {
			return false;
		} 
		if($('#homeFirstPlayerId option:selected').val() == $('#awayFirstPlayerId option:selected').val()) {
			alert('Player dupication found. Please choose different players');
			return false;
		}
		if($('#homeTeamId option:selected').val() == $('#awayTeamId option:selected').val()) {
			alert('Team dupication found. Please choose different Teams');
			return false;
		}
		if($('#matchType option:selected').val() == 'doubles') {
			if($('#homeSecondPlayerId option:selected').val() == $('#awaySecondPlayerId option:selected').val()
				|| $('#homeFirstPlayerId option:selected').val() == $('#awaySecondPlayerId option:selected').val()
				|| $('#homeSecondPlayerId option:selected').val() == $('#awayFirstPlayerId option:selected').val()
				|| $('#homeFirstPlayerId option:selected').val() == $('#homeSecondPlayerId option:selected').val()
				|| $('#awayFirstPlayerId option:selected').val() == $('#awaySecondPlayerId option:selected').val())
				 {
				alert('Player dupication found. Please choose different players');
				return false;
			}
		}	
		switch ($(whichInput).attr('name')) {
		case 'save_match_btn': 
			uploadFormDataToSessionObjects('SAVE_MATCH');
			break;
		case 'reset_match_btn':
			processWaitingButtonSpinner('START_WAIT_TIMER');
			uploadFormDataToSessionObjects('RESET_MATCH');
			break;
		}
		break;
	case 'setup_match_btn':
		document.tennis_form.method = 'post';
		document.tennis_form.action = 'setup';
	   	document.tennis_form.submit();
		break;
	case 'load_match_btn':
		processWaitingButtonSpinner('START_WAIT_TIMER');
		processTennisProcedures('LOAD_MATCH',$('#select_tennis_matches option:selected'));
		break;
	case 'log_saves_btn':
		processTennisProcedures('SAVES',match_data);
		break;
	case 'cancel_undo_btn':
		addItemsToList('LOAD_EVENTS',match_data);
		document.getElementById('tennis_div').style.display = '';
		document.getElementById('select_event_div').style.display = '';
		processWaitingButtonSpinner('END_WAIT_TIMER');
		break;
	case 'cancel_overwrite_btn': case 'cancel_event_btn': case 'cancel_saves_btn':
		document.getElementById('select_event_div').style.display = 'none';
		addItemsToList('LOAD_EVENTS',match_data); 
		processWaitingButtonSpinner('END_WAIT_TIMER');
		break;
	case 'cancel_graphics_btn':
		$('#select_graphic_options_div').empty();
		document.getElementById('select_graphic_options_div').style.display = 'none';
		$("#select_event_div").show();
		$("#match_configuration").show();
		$("#tennis_div").show();
		break;
	case 'select_existing_tennis_matches':
		if(whichInput.value.toLowerCase().includes('new_match')) {
			initialiseForm('SETUP',null);
		} else {
			processWaitingButtonSpinner('START_WAIT_TIMER');
			processTennisProcedures('LOAD_SETUP',$('#select_existing_tennis_matches option:selected'));
		}
		break;
	case 'populate_stats_btn':
		processTennisProcedures('POPULATE-SCOREBUG_STATS');
		break;
	case 'populate_header_btn':
		processTennisProcedures('POPULATE-SCOREBUG_HEADER');
		break;
	case 'populate_speed_btn':
		processTennisProcedures('POPULATE-SPEED');
		break;
	case 'populate_stats_set_btn':
		processTennisProcedures('POPULATE-SCOREBUG_SET_STATS');
		break;
	case 'populate_stats_bar_btn':
		processTennisProcedures('POPULATE-SCOREBUG_BAR_STATS');
		break;
	case 'populate_namesuper_btn':
		processTennisProcedures('POPULATE-NAMESUPERDB');
		break;
	case 'populate_namesupersp_btn':
		processTennisProcedures('POPULATE-NAMESUPER-SP');
		break;
	case 'populate_namesuperdp_btn':
		processTennisProcedures('POPULATE-NAMESUPER-DP');
		break;
	case 'populate_namesuper_sp1_btn':
		processTennisProcedures('POPULATE-NAMESUPER-SP1');
		break;
	case 'populate_namesuper_dp1_btn':
		processTennisProcedures('POPULATE-NAMESUPER-DP1');
		break;
	case 'populate_cross_btn':
		processTennisProcedures('POPULATE-CROSS');
		break;
	case 'populate_single_match_promo_btn':
		processTennisProcedures('POPULATE-SINGLE_MATCHPROMO');
		break;
	case 'populate_single_ltmatch_promo_btn':
		processTennisProcedures('POPULATE-SINGLE_LT_MATCHPROMO');
		break;
	case 'populate_double_match_promo_btn':
		processTennisProcedures('POPULATE-DOUBLE_MATCHPROMO');
		break;
	case 'populate_lt_double_match_promo_btn':
		processTennisProcedures('POPULATE-LT_DOUBLE_MATCHPROMO');
		break;
	default:
		if(whichInput) {
			if(whichInput.id.includes('_score_btn')) { 
				
				error_msg = 'Cannot find any started set. Please start a set first before logging an event';
				match_data.sets.forEach(function(set,set_index,set_arr){
					if(set.set_status.toLowerCase() == 'start') {
						error_msg = '';
					}
				});
				if(error_msg) {
					alert(error_msg);
					return false;			
				} else {
					error_msg = 'Cannot find any started game. Please start a game first before logging an event';
					match_data.sets.forEach(function(set,set_index,set_arr){
						if(set.set_status.toLowerCase() == 'start') {
							set.games.forEach(function(game,game_index,game_arr){
								if(game.game_status.toLowerCase() == 'start') {
									error_msg = '';
								}
							});
						}
					});
					if(error_msg) {
						alert(error_msg);
						return false;			
					} else {
						processWaitingButtonSpinner('START_WAIT_TIMER');
						processTennisProcedures('LOG_SCORE',whichInput);
					}
				}
				
			} else if(whichInput.id.includes('_increment_') || whichInput.id.includes('_decrement_')) {

				if(whichInput.id.includes('_decrement_')) {
					if(parseInt($('#' + whichInput.id.replace('_decrement_','_').replace('_btn','_txt')).val()) <= 0) {
						alert('Cannot use decrement button when the value is zero');
						return false;
					}
				}
				if(whichInput.id.includes('_increment_')) {
					$('#' + whichInput.id.replace('_increment_','_').replace('_btn','_txt')).val(
						parseInt($('#' + whichInput.id.replace('_increment_','_').replace('_btn','_txt')).val()) + parseInt(1));
				}else if(whichInput.id.includes('_decrement_')) {
					$('#' + whichInput.id.replace('_decrement_','_').replace('_btn','_txt')).val(
						parseInt($('#' + whichInput.id.replace('_increment_','_').replace('_btn','_txt')).val()) - parseInt(1));
				}
				
				processWaitingButtonSpinner('START_WAIT_TIMER');
				processTennisProcedures('LOG_STAT',whichInput);
			}
		}
		break;
	}
}
function processTennisProcedures(whatToProcess, whichInput)
{
	var value_to_process; 
	
	switch(whatToProcess) {
	case 'LOG_SET_UNDO':
		value_to_process = $('#select_undo_set_number option:selected').val() + ',' + $('#select_undo_game_number option:selected').val()
			+ ',' + $('#undo_home_score_txt').val() + ',' + $('#undo_away_score_txt').val();
		break;
	case 'LOG_STAT':
		value_to_process = whichInput.id;
		break;
	case 'LOG_SET': case 'LOG_GAME':
		if(whichInput == 'START' || whichInput == 'RESET') {
			value_to_process = whichInput;
			switch(whatToProcess) {
			case 'LOG_GAME':
				if(whichInput == 'START') {
					value_to_process = value_to_process + ',' + $('#select_scoring_type option:selected').val();
				}
				break;
			}
			
		}else if(whichInput == 'END') {
			switch(whatToProcess) {
			case 'LOG_SET': 
				value_to_process = whichInput + ',' + $('#select_set_winner option:selected').val();
				break;
			case 'LOG_GAME':
				value_to_process = whichInput + ',' + $('#select_game_winner option:selected').val();
				break;
			}
		}
		break;
	case 'LOAD_MATCH': case 'LOAD_SETUP':
		value_to_process = whichInput.val();
		break;
	case 'LOG_SCORE': 
		value_to_process = whichInput.id;
		break;
	case 'LOG_SERVE':
		value_to_process = $('#select_serving_player option:selected').val();
		break;
	case 'POPULATE-SCOREBUG':
		//alert($('#selectedBroadcaster').val());
		switch ($('#selectedBroadcaster').val()) {
		case 'TPL_2023':
			value_to_process = '/Default/ScoreBug';
			break;
		case 'ATP_2022':
			value_to_process = '/Default/ScoreBug-Single';
			break;
		}
		break;
	case 'POPULATE-SCOREBUG_STATS': case 'POPULATE-SCOREBUG_SET_STATS': case 'POPULATE-SCOREBUG_BAR_STATS':
		switch ($('#selectedBroadcaster').val()) {
		case 'ATP_2022':
			value_to_process = $('#selectScorebugstats option:selected').val() ;
			break;
		}
		break
	case 'POPULATE-SCOREBUG_HEADER':
		switch ($('#selectedBroadcaster').val()) {
		case 'ATP_2022':
			value_to_process = $('#selectScorebugHeader option:selected').val() ;
			break;
		}
		break
	case 'POPULATE-FIX_AND_RES':
		//alert($('#selectedBroadcaster').val());
		switch ($('#selectedBroadcaster').val()) {
		case 'TPL_2023':
			value_to_process = '/Default/Fixtures';
			break;
		}
	case 'POPULATE-MATCH_STATS':
		switch ($('#selectedBroadcaster').val()) {
		case 'ATP_2022':
			value_to_process = '/Default/FF_MatchStats';
			break;
		}
		break;
	case 'POPULATE-SPEED':
		switch ($('#selectedBroadcaster').val()) {
		case 'ATP_2022':
			value_to_process = '/Default/LtServeSpeed_Auto' + ',' + $('#selectSpeed').val();
			break;
		}
		break;
	case 'POPULATE-MATCHID':
		//alert($('#selectedBroadcaster').val());
		switch ($('#selectedBroadcaster').val()) {
		case 'TPL_2023':
			value_to_process = '/Default/MatchId';
			break;
		case 'ATP_2022':
			value_to_process = '/Default/FF_MatchIdSingles';
			break;
		}
		break;
	case 'POPULATE-SINGLE_MATCHPROMO':
		switch ($('#selectedBroadcaster').val()) {
		case 'ATP_2022':
			value_to_process = '/Default/FF_MatchIdSingles' + ',' + $('#selectSingleMatchPromo option:selected').val();
			break;
		}
		break;
	case 'POPULATE-LT-MATCHID':
		switch ($('#selectedBroadcaster').val()) {
		case 'ATP_2022':
			value_to_process = '/Default/LtMatchIdentSingles';
			break;
		}
		break;
	case 'POPULATE-SINGLE_LT_MATCHPROMO':
		switch ($('#selectedBroadcaster').val()) {
		case 'ATP_2022':
			value_to_process = '/Default/LtMatchIdentSingles' + ',' + $('#selectSingleltMatchPromo option:selected').val();
			break;
		}
		break;
	case 'POPULATE-DOUBLE_MATCHPROMO':
		switch ($('#selectedBroadcaster').val()) {
		case 'ATP_2022':
			value_to_process = '/Default/FF_MatchIdDoubles'  + ',' + $('#selectDoubleMatchPromo option:selected').val();
			break;
		}
		break;
	case 'POPULATE-LT-MATCHID_DOUBLE':
		switch ($('#selectedBroadcaster').val()) {
		case 'ATP_2022':
			value_to_process = '/Default/LtMatchIdentDoubles';
			break;
		}
		break;
	case 'POPULATE-LT_DOUBLE_MATCHPROMO':
		switch ($('#selectedBroadcaster').val()) {
		case 'ATP_2022':
			value_to_process = '/Default/LtMatchIdentDoubles' + ',' + $('#selectltDoubleMatchPromo option:selected').val();
			break;
		}
		break;
	case 'POPULATE-MATCHID_DOUBLE':
		switch ($('#selectedBroadcaster').val()) {
		case 'ATP_2022':
			value_to_process = '/Default/FF_MatchIdDoubles';
			break;
		}
		break;
	case 'POPULATE-LT-MATCH_RESULTSINGLES': case 'POPULATE-LT-MATCH_SCORESINGLES':
		switch ($('#selectedBroadcaster').val()) {
		case 'TPL_2023':
			value_to_process = '/Default/LT_Score';
			break;
		case 'ATP_2022':
			value_to_process = '/Default/LtMatchResultsSingles';
			break;
		}
		break;
	case 'POPULATE-FF-MATCH_RESULTSINGLES':
		switch ($('#selectedBroadcaster').val()) {
		case 'ATP_2022':
			value_to_process = '/Default/FF_MatchIdSingles';
			break;
		}
		break;
	case 'POPULATE-LT-MATCH_RESULTDOUBLES': case 'POPULATE-LT-MATCH_SCOREDOUBLES':
		switch ($('#selectedBroadcaster').val()) {
		case 'ATP_2022':
			value_to_process = '/Default/LtMatchResultsDoubles';
			break;
		}
		break;
	case 'POPULATE-FF-MATCH_RESULTDOUBLES':
		switch ($('#selectedBroadcaster').val()) {
		case 'ATP_2022':
			value_to_process = '/Default/FF_MatchIdDoubles';
			break;
		}
		break;
	case 'POPULATE-NAMESUPER-DP':
		switch ($('#selectedBroadcaster').val()) {
		case 'TPL_2023':
			value_to_process = '/Default/LowerThird' + ',' + $('#selectNameSuperdp option:selected').val();
			break;
		case 'ATP_2022':
			value_to_process = '/Default/LtNameSuper2' + ',' + $('#selectNameSuperdp option:selected').val(); 
			break;
		}
		break;
	case 'POPULATE-NAMESUPER-SP':
		switch ($('#selectedBroadcaster').val()) {
		case 'TPL_2023':
			//alert($('#selectNameSupersp option:selected').val());
			value_to_process = '/Default/LowerThird' + ',' + $('#selectNameSupersp option:selected').val();
			break;
		case 'ATP_2022':
			value_to_process = '/Default/LtNameSuper1' + ',' + $('#selectNameSupersp option:selected').val();
			break;
		}
		break;
	case 'POPULATE-NAMESUPER-SP1':
		switch ($('#selectedBroadcaster').val()) {
		case 'ATP_2022':
			value_to_process = '/Default/LtNameSuper1' + ',' + $('#selectNameSupersp1 option:selected').val();
			break;
		}
		break;
	case 'POPULATE-NAMESUPER-DP1':
		switch ($('#selectedBroadcaster').val()) {
		case 'ATP_2022':
			value_to_process = '/Default/LtNameSuper2' + ',' + $('#selectNameSuperdphome option:selected').val() + ',' + $('#selectNameSuperdpaway option:selected').val();
			break;
		}
		break;
	case 'POPULATE-CROSS':
		switch ($('#selectedBroadcaster').val()) {
		case 'ATP_2022':
			if(match_data.matchType == 'singles'){
				value_to_process = '/Default/LtCross_Court_Single_Auto' + ',' + $('#selectCross1 option:selected').val();
			}else if(match_data.matchType == 'doubles'){
				value_to_process = '/Default/LtCross_Court_Doubles_Auto' + ',' + $('#selectCross1 option:selected').val();
			}
			break;
		}
		break;
	case 'POPULATE-NAMESUPERDB':
		switch ($('#selectedBroadcaster').val()) {
		case 'TPL_2023':
			value_to_process = '/Default/LowerThird' + ',' + $('#selectNameSuper option:selected').val();
			break;
		case 'ATP_2022':
			value_to_process = '/Default/LtNameSuperCenter' + ',' + $('#selectNameSuper option:selected').val();
			break;
		}
		break;		
	}

	$.ajax({    
        type : 'Get',     
        url : 'processTennisProcedures.html',     
        data : 'whatToProcess=' + whatToProcess + '&valueToProcess=' + value_to_process, 
        dataType : 'json',
        success : function(data) {
			match_data = data;
        	switch(whatToProcess) {
			case 'READ_MATCH_FOR_STATS': case 'LOG_STAT':
				initialiseForm('LOAD_STAT',data);
				break;
			case 'READ_CLOCK':
				if(match_data.clock) {
					if(document.getElementById('match_time_hdr')) {
						document.getElementById('match_time_hdr').innerHTML = 'MATCH TIME : ' + 
							secondsTimeSpanToHMS(match_data.clock.matchTotalSeconds);
					}
				}
				break;
			case 'LOG_SCORE': case 'LOAD_MATCH': case 'LOG_SET': case 'LOG_GAME': case 'LOAD_MATCH_AFTER_STAT_LOG': 
			case 'LOG_SET_UNDO':
        		addItemsToList('LOAD_MATCH',data);
				switch(whatToProcess) {
				case 'LOG_SET_UNDO':
					addItemsToList('LOAD_GAMES', data);
					addItemsToList('LOAD_GAME_SCORES', data);
					break;
				case 'LOG_SCORE':
					if(processVariousStats('CHECK-ADVANTAGE-POINT',whichInput) == false) {
						if(data.sets[data.sets.length - 1].games[data.sets[data.sets.length - 1]
							.games.length - 1].game_type == 'tie_break'){
							processVariousStats('CHECK-TIE-BREAK-WINNER',whichInput);
						}else if(data.sets[data.sets.length - 1].games[data.sets[data.sets.length - 1]
							.games.length - 1].game_type == 'points'){
							processVariousStats('CHECK-POINTS-WINNER',whichInput);
						}else{
							processVariousStats('CHECK-GAME-WINNER',whichInput);
						}
					}
					
					break;
				case 'LOAD_MATCH': case 'LOAD_MATCH_AFTER_STAT_LOG':
					addItemsToList('LOAD_EVENTS',data);
					document.getElementById('tennis_div').style.display = '';
					document.getElementById('select_event_div').style.display = '';
					setInterval(displayMatchTime, 500);
					break;
				case 'LOG_GAME':
					if(whichInput == 'START') {
						processUserSelection($('#select_serving_player'));
					}else if(whichInput == 'END') {
						processVariousStats('CHECK-SET-WINNER',null);
					}
					break;
				}
        		break;
        	case 'LOAD_SETUP':
        		initialiseForm('SETUP',data);
        		break;
        	case 'POPULATE-SCOREBUG': case 'POPULATE-LT-MATCH_RESULTSINGLES': case 'POPULATE-LT-MATCHID': case 'POPULATE-MATCHID': case 'POPULATE-MATCHID_DOUBLE':
        	case 'POPULATE-LT-MATCHID_DOUBLE': case 'POPULATE-NAMESUPERDB': case 'POPULATE-LT-MATCH_RESULTDOUBLES': case 'POPULATE-FF-MATCH_RESULTSINGLES':
        	case 'POPULATE-FF-MATCH_RESULTDOUBLES': case 'POPULATE-NAMESUPER-SP': case 'POPULATE-NAMESUPER-DP': case 'POPULATE-NAMESUPER-SP1': case 'POPULATE-NAMESUPER-DP1':
        	case 'POPULATE-CROSS': case "POPULATE-LT-MATCH_SCORESINGLES": case 'POPULATE-SINGLE_MATCHPROMO': case 'POPULATE-DOUBLE_MATCHPROMO': case 'POPULATE-SINGLE_LT_MATCHPROMO':
        	case 'POPULATE-LT_DOUBLE_MATCHPROMO': case 'POPULATE-LT-MATCH_SCOREDOUBLES': case 'POPULATE-MATCH_STATS': case 'POPULATE-SPEED': case 'POPULATE-FIX_AND_RES':
        		if(confirm('Animate In?') == true){
					switch(whatToProcess){
					case 'POPULATE-SCOREBUG':
						processTennisProcedures('ANIMATE-IN-SCOREBUG');		
						break;
					case 'POPULATE-FIX_AND_RES':
						//alert('ANIMATE IN');
						processTennisProcedures('ANIMATE-IN-FIX_AND_RES');
						break;
					case 'POPULATE-MATCHID':
						processTennisProcedures('ANIMATE-IN-MATCHID');				
						break;
					case 'POPULATE-SINGLE_MATCHPROMO':
						processTennisProcedures('ANIMATE-IN-SINGLE_MATCHPROMO');				
						break;
					case 'POPULATE-DOUBLE_MATCHPROMO':
						processTennisProcedures('ANIMATE-IN-DOUBLE_MATCHPROMO');				
						break;
					case 'POPULATE-MATCHID_DOUBLE':
						processTennisProcedures('ANIMATE-IN-MATCHID_DOUBLE');				
						break;
					case 'POPULATE-LT-MATCHID':
						processTennisProcedures('ANIMATE-IN-LT_MATCHID');				
						break;
					case 'POPULATE-LT-MATCHID_DOUBLE':
						processTennisProcedures('ANIMATE-IN-LT-MATCHID_DOUBLE');				
						break;
					case 'POPULATE-LT-MATCH_RESULTSINGLES':
						processTennisProcedures('ANIMATE-LT-MATCH_RESULTSINGLES');				
						break;
					case 'POPULATE-NAMESUPERDB':
						processTennisProcedures('ANIMATE-LT-NAMESUPERDB');				
						break;
					case 'POPULATE-LT-MATCH_RESULTDOUBLES':
						processTennisProcedures('ANIMATE-LT-MATCH_RESULTDOUBLES');				
						break;
					case 'POPULATE-FF-MATCH_RESULTSINGLES':
						processTennisProcedures('ANIMATE-FF-MATCH_RESULTSINGLES');				
						break;
					case 'POPULATE-FF-MATCH_RESULTDOUBLES':
						processTennisProcedures('ANIMATE-FF-MATCH_RESULTDOUBLES');				
						break;
					case 'POPULATE-NAMESUPER-SP':
						processTennisProcedures('ANIMATE-LT-NAMESUPER_SP');				
						break;
					case 'POPULATE-NAMESUPER-DP':
						processTennisProcedures('ANIMATE-LT-NAMESUPER_DP');				
						break;
					case 'POPULATE-NAMESUPER-SP1':
						processTennisProcedures('ANIMATE-LT-NAMESUPER_SP1');				
						break;
					case 'POPULATE-NAMESUPER-DP1':
						processTennisProcedures('ANIMATE-LT-NAMESUPER_DP1');				
						break;
					case 'POPULATE-CROSS':
						processTennisProcedures('ANIMATE-LT-CROSS');				
						break;
					case "POPULATE-LT-MATCH_SCORESINGLES":
						processTennisProcedures('ANIMATE-LT-MATCH_SCORESINGLES');				
						break;
					case 'POPULATE-LT-MATCH_SCOREDOUBLES':
						processTennisProcedures('ANIMATE-LT-MATCH_SCOREDOUBLES');				
						break;
					case 'POPULATE-SINGLE_LT_MATCHPROMO':
						processTennisProcedures('ANIMATE-LT-SINGLE_LT_MATCHPROMO');				
						break;
					case 'POPULATE-LT_DOUBLE_MATCHPROMO':
						processTennisProcedures('ANIMATE-LT-DOUBLE_LT_MATCHPROMO');				
						break;
					case 'POPULATE-MATCH_STATS':
						processTennisProcedures('ANIMATE-MATCH_STATS');				
						break;
					case 'POPULATE-SPEED':
						processTennisProcedures('ANIMATE-SPEED');				
						break;
					}
				}
				break;
			case 'SINGLE-MATCHPROMO_GRAPHICS-OPTIONS':
				addItemsToList('SINGLE-MATCHPROMO-OPTIONS',data);
				break;
			case 'SINGLE-LT_MATCHPROMO_GRAPHICS-OPTIONS':
				addItemsToList('SINGLE-LT_MATCHPROMO-OPTIONS',data);
				break;
			case 'DOUBLE-LT_MATCHPROMO_GRAPHICS-OPTIONS':
				addItemsToList('DOUBLE-LT_MATCHPROMO-OPTIONS',data);
				break;
			case 'DOUBLE-MATCHPROMO_GRAPHICS-OPTIONS':
				addItemsToList('DOUBLE-MATCHPROMO-OPTIONS',data);
				break;
			case 'NAMESUPER_GRAPHICS-OPTIONS':
				addItemsToList('NAMESUPER-OPTIONS',data);
				break;
			case 'NAMESUPER-SP_GRAPHICS-OPTIONS':
				addItemsToList('NAMESUPER-SP-OPTIONS',data);
				break;
			case 'NAMESUPER-SP1_GRAPHICS-OPTIONS':
				addItemsToList('NAMESUPER-SP1-OPTIONS',data);
				break;
			case 'NAMESUPER-DP1_GRAPHICS-OPTIONS':
				addItemsToList('NAMESUPER-DP1-OPTIONS',data);
				break;
			case 'APIDATA_GRAPHICS-OPTIONS':
				addItemsToList('APIDATA-OPTIONS',data);				
				break;	
        	}
    		processWaitingButtonSpinner('END_WAIT_TIMER');
	    },    
	    error : function(e) {    
	  	 	console.log('Error occured in ' + whatToProcess + ' with error description = ' + e);     
	    }    
	});
}
function processVariousStats(whatToProcess, whichInput)
{
	switch(whatToProcess){
	case 'CHECK-ADVANTAGE-POINT':
		if($('#homeScore').val() == 'ADVANTAGE' && $('#awayScore').val() == 'ADVANTAGE') {
			if(whichInput.id.includes('increment')) {
				processUserSelection(document.getElementById('home_decrement_score_btn'));
				processUserSelection(document.getElementById('away_decrement_score_btn'));
				return true;
			}
		} 
		return false;
	case 'CHECK-SET-WINNER':
		var home_game_wins = 0, away_game_wins = 0;
		match_data.sets.forEach(function(set,set_index,set_arr){
			if(set.set_status.toLowerCase() == 'start') {
				set.games.forEach(function(game,game_index,game_arr){
					if(game.game_winner.toLowerCase() == 'home') {
						home_game_wins = home_game_wins + 1;
					}else if(game.game_winner.toLowerCase() == 'away') {
						away_game_wins = away_game_wins + 1;
					}
				});
			}
		});
		if(home_game_wins >= 6 && (home_game_wins - away_game_wins) >= 2 || home_game_wins == 7 && away_game_wins == 6) {
			$('#select_set_winner').val('home');
			processUserSelection($('#end_set_btn'));
		}else if(away_game_wins >= 6 && (away_game_wins - home_game_wins) >= 2 || home_game_wins == 6 && away_game_wins == 7) {
			$('#select_set_winner').val('away');
			processUserSelection($('#end_set_btn'));
		}
		break;
		
	case 'CHECK-GAME-WINNER':
		if($('#homeScore').val() == 'GAME' || $('#awayScore').val() == 'GAME') {
			if($('#homeScore').val() == 'GAME') {
				$('#select_game_winner').val('home');
			} else if ($('#awayScore').val() == 'GAME') {
				$('#select_game_winner').val('away');
			}
			processUserSelection($('#end_game_btn'));
		}
		break;
		
	case 'CHECK-POINTS-WINNER':
		var home_and_away_score;
		if(match_data.tieBreakerRule == 'race_to_20'){
			home_and_away_score = 20;
		}else if(match_data.tieBreakerRule == 'race_to_21'){
			home_and_away_score = 21;
		}else if(match_data.tieBreakerRule == 'race_to_25'){
			home_and_away_score = 25;
		}
		
		if(parseInt($('#homeScore').val()) + parseInt($('#awayScore').val()) == home_and_away_score) {
			if(parseInt($('#homeScore').val()) > parseInt($('#awayScore').val())) {
				$('#select_game_winner').val('home');
				processUserSelection($('#end_game_btn'));
			} else if (parseInt($('#awayScore').val()) > parseInt($('#homeScore').val())) {
				$('#select_game_winner').val('away');
				processUserSelection($('#end_game_btn'));
			}else{
				alert("It's a tie End the game");
				processUserSelection($('#end_game_btn'));
			}
			
		}
	break;
	case 'CHECK-TIE-BREAK-WINNER':
		var race_number;
		if(match_data.tieBreakerRule == 'race_to_7'){
			race_number = 7;
		}else if(match_data.tieBreakerRule == 'race_to_10'){
			race_number = 10;
		}
		//alert(parseInt($('#homeScore').val()));
		if(parseInt($('#homeScore').val()) >= race_number || parseInt($('#awayScore').val()) >= race_number) {
			if(parseInt($('#homeScore').val()) >= race_number && (parseInt($('#homeScore').val()) - parseInt($('#awayScore').val())) >= 2) {
				$('#select_game_winner').val('home');
				processUserSelection($('#end_game_btn'));
			} else if (parseInt($('#awayScore').val()) >= race_number && (parseInt($('#awayScore').val()) - parseInt($('#homeScore').val())) >= 2) {
				$('#select_game_winner').val('away');
				processUserSelection($('#end_game_btn'));
			}
		}
		break;
	}
}
function addItemsToList(whatToProcess, dataToProcess)
{
	var max_cols,div,anchor,row,header_text,select,option,tr,th,thead,text,table,tbody,cellCount;
	
	cellCount=0;
	
	switch (whatToProcess) {
	case 'LOAD_SELECTED_TEAM_PLAYERS':
	
		/*if(dataToProcess.id.includes('home')) {
			$('#homeFirstPlayerId').empty();
			$('#homeSecondPlayerId').empty();
		} else {
			$('#awayFirstPlayerId').empty();
			$('#awaySecondPlayerId').empty();
		}*/
		
		if (match_data) {
		    match_data.players.forEach(function(plyr) {
		        if (plyr.teamId == $('#' + dataToProcess.id).val()) {
		            option = document.createElement('option');
		            if (dataToProcess.id.includes('home')) {
		                $('#homeFirstPlayerId').append(new Option(plyr.full_name, plyr.playerId));
		                if ($('#matchType').val() == 'doubles') {
		                    $('#homeSecondPlayerId').append(new Option(plyr.full_name, plyr.playerId));
		                } else {
		                    $('#homeSecondPlayerId').val('0');
		                }
		            } else {
		                $('#awayFirstPlayerId').append(new Option(plyr.full_name, plyr.playerId));
		                if ($('#matchType').val() == 'doubles') {
		                    $('#awaySecondPlayerId').append(new Option(plyr.full_name, plyr.playerId));
		                } else {
		                    $('#awaySecondPlayerId').val('0');
		                }
		            }
		        }
		    });
		
		    if (dataToProcess.id.includes('home')) {
		        $('#homeFirstPlayerId').select2();
		        if ($('#matchType').val() == 'doubles') {
		            $('#homeSecondPlayerId').select2();
		        }
		    } else {
		        $('#awayFirstPlayerId').select2();
		        if ($('#matchType').val() == 'doubles') {
		            $('#awaySecondPlayerId').select2();
		        }
		    }
		}
		break;
		
	case 'SCOREBUG_OPTION': case 'SCOREBUG-SET_OPTION': case 'SCOREBUG_STATS_OPTION': case 'SPEED_OPTION': case 'SCOREBUG-HEADER_OPTION':
		switch ($('#selectedBroadcaster').val()) {
		case 'ATP_2022':

			$('#select_graphic_options_div').empty();
	
			header_text = document.createElement('h6');
			header_text.innerHTML = 'Select Graphic Options';
			document.getElementById('select_graphic_options_div').appendChild(header_text);
			
			table = document.createElement('table');
			table.setAttribute('class', 'table table-bordered');
					
			tbody = document.createElement('tbody');
	
			table.appendChild(tbody);
			document.getElementById('select_graphic_options_div').appendChild(table);
			
			row = tbody.insertRow(tbody.rows.length);
			
			switch(whatToProcess){
				case 'SCOREBUG_STATS_OPTION':
					select = document.createElement('select');
					select.style = 'width:130px';
					select.id = 'selectScorebugstats';
					select.name = select.id;
					
					option = document.createElement('option');
					option.value = 'firstServePoints';
					option.text = '1st Serve Points Won';
					select.appendChild(option);
					
					option = document.createElement('option');
					option.value = 'secondServePoints';
					option.text = '2nd Serve Points Won';
					select.appendChild(option);
					
					option = document.createElement('option');
					option.value = 'totalPointsWon';
					option.text = 'Total Points Won';
					select.appendChild(option);
					
					option = document.createElement('option');
					option.value = 'returnPointsWon';
					option.text = 'Return Points Won ';
					select.appendChild(option);
					
					option = document.createElement('option');
					option.value = 'breakPoint';
					option.text = 'Break Points Won';
					select.appendChild(option);

					select.setAttribute('onchange',"processUserSelection(this)");
					row.insertCell(cellCount).appendChild(select);
					cellCount = cellCount + 1;
					break;
				case 'SCOREBUG-HEADER_OPTION':
					select = document.createElement('select');
					select.style = 'width:130px';
					select.id = 'selectScorebugHeader';
					select.name = select.id;
					
					option = document.createElement('option');
					option.value = 'match_point';
					option.text = 'Match Point';
					select.appendChild(option);
					
					option = document.createElement('option');
					option.value = 'set_point';
					option.text = 'Set Point';
					select.appendChild(option);
					
					option = document.createElement('option');
					option.value = 'break_point';
					option.text = 'Break Point';
					select.appendChild(option);
					
					option = document.createElement('option');
					option.value = 'tie_break';
					option.text = 'Tie-Break';
					select.appendChild(option);
					
					option = document.createElement('option');
					option.value = 'match_tie_break';
					option.text = 'Match Tie-Break';
					select.appendChild(option);
					
					option = document.createElement('option');
					option.value = 'deuce';
					option.text = 'Deuce';
					select.appendChild(option);

					select.setAttribute('onchange',"processUserSelection(this)");
					row.insertCell(cellCount).appendChild(select);
					cellCount = cellCount + 1;
					break;
				case 'SPEED_OPTION':
					select = document.createElement('input');
					select.type = "text";
					select.id = 'selectSpeed';
					select.value = '';
					
					row.insertCell(cellCount).appendChild(select);
					cellCount = cellCount + 1;
					break;
				case 'SCOREBUG_OPTION':
					select = document.createElement('select');
					select.style = 'width:130px';
					select.id = 'selectScorebugstats';
					select.name = select.id;
					
					option = document.createElement('option');
					option.value = 'firstServeWon';
					option.text = '1st Serve Points Won';
					select.appendChild(option);
					
					option = document.createElement('option');
					option.value = 'secondServeWon';
					option.text = '2nd Serve Points Won';
					select.appendChild(option);
					
					option = document.createElement('option');
					option.value = 'ace';
					option.text = 'Aces';
					select.appendChild(option);
					
					option = document.createElement('option');
					option.value = 'doubleFault';
					option.text = 'Double Fault';
					select.appendChild(option);
					
					option = document.createElement('option');
					option.value = 'breakPointWon';
					option.text = 'Break Points Won';
					select.appendChild(option);

					select.setAttribute('onchange',"processUserSelection(this)");
					row.insertCell(cellCount).appendChild(select);
					cellCount = cellCount + 1;
					
					break;
					
					case 'SCOREBUG-SET_OPTION':
					select = document.createElement('select');
					select.style = 'width:130px';
					select.id = 'selectScorebugstats';
					select.name = select.id;
					
					option = document.createElement('option');
					option.value = 'setfirstServeWon';
					option.text = '1st Serve Points Won';
					select.appendChild(option);
					
					option = document.createElement('option');
					option.value = 'setsecondServeWon';
					option.text = '2nd Serve Points Won';
					select.appendChild(option);
					
					option = document.createElement('option');
					option.value = 'setace';
					option.text = 'Aces';
					select.appendChild(option);
					
					option = document.createElement('option');
					option.value = 'setdoubleFault';
					option.text = 'Double Fault';
					select.appendChild(option);
					
					option = document.createElement('option');
					option.value = 'setbreakPointWon';
					option.text = 'Break Points Won';
					select.appendChild(option);

					select.setAttribute('onchange',"processUserSelection(this)");
					row.insertCell(cellCount).appendChild(select);
					cellCount = cellCount + 1;
					
					break;
				}
			
			option = document.createElement('input');
		    option.type = 'button';
			switch (whatToProcess) {
			case 'SCOREBUG-HEADER_OPTION':
				option.name = 'populate_header_btn';
			    option.value = 'Populate Header';
				break;
			case 'SPEED_OPTION':
				option.name = 'populate_speed_btn';
			    option.value = 'Populate Speed';
				break;
			case 'SCOREBUG_OPTION':
			    option.name = 'populate_stats_btn';
			    option.value = 'Populate Stats';
				break;
			case 'SCOREBUG_STATS_OPTION':
				option.name = 'populate_stats_bar_btn';
			    option.value = 'Populate Stats Bar';
				break;
			case 'SCOREBUG-SET_OPTION':
				option.name = 'populate_stats_set_btn';
			    option.value = 'Populate Set Stats';
				break;
			}
		    option.id = option.name;
		    option.setAttribute('onclick',"processUserSelection(this)");
		    
		    div = document.createElement('div');
		    div.append(option);

			option = document.createElement('input');
			option.type = 'button';
			option.name = 'cancel_graphics_btn';
			option.id = option.name;
			option.value = 'Cancel';
			option.setAttribute('onclick','processUserSelection(this)');
	
		    div.append(option);
		    
		    row.insertCell(cellCount).appendChild(div);
		    cellCount = cellCount + 1;
		    
			document.getElementById('select_graphic_options_div').style.display = '';

			break;
		}
		break;
	case 'SINGLE-MATCHPROMO-OPTIONS': case 'DOUBLE-MATCHPROMO-OPTIONS': case 'SINGLE-LT_MATCHPROMO-OPTIONS': case 'DOUBLE-LT_MATCHPROMO-OPTIONS':
		switch ($('#selectedBroadcaster').val().toUpperCase()){
		case 'ATP_2022':
			$('#select_graphic_options_div').empty();
	
			header_text = document.createElement('h6');
			header_text.innerHTML = 'Select Graphic Options';
			document.getElementById('select_graphic_options_div').appendChild(header_text);
			
			table = document.createElement('table');
			table.setAttribute('class', 'table table-bordered');
					
			tbody = document.createElement('tbody');
	
			table.appendChild(tbody);
			document.getElementById('select_graphic_options_div').appendChild(table);
			
			row = tbody.insertRow(tbody.rows.length);
			switch(whatToProcess){
				case 'SINGLE-MATCHPROMO-OPTIONS':
					select = document.createElement('select');
					select.style = 'width:300px';
					select.id = 'selectSingleMatchPromo';
					select.name = select.id;
					
					match_data.forEach(function(smp,index,arr1){
						if(smp.homePlayerSecond == 0 && smp.awayPlayerSecond == 0){
							option = document.createElement('option');
							option.value = smp.matchnumber;
							option.text = smp.matchnumber + '. ' + smp.home_FirstPlayer.full_name + ' - ' + smp.away_FirstPlayer.full_name;
							select.appendChild(option);
						}
					});
					
					row.insertCell(0).appendChild(select);
					
					option = document.createElement('input');
			   	 	option.type = 'button';
				    option.name = 'populate_single_match_promo_btn';
				    option.value = 'Populate Matah Promo';
				    option.id = option.name;
				    option.setAttribute('onclick',"processUserSelection(this)");
				    
				    div = document.createElement('div');
				    div.append(option);
		
					option = document.createElement('input');
					option.type = 'button';
					option.name = 'cancel_graphics_btn';
					option.id = option.name;
					option.value = 'Cancel';
					option.setAttribute('onclick','processUserSelection(this)');
			
				    div.append(option);
				    
				    row.insertCell(1).appendChild(div);
				    
					document.getElementById('select_graphic_options_div').style.display = '';
					break;
				case 'SINGLE-LT_MATCHPROMO-OPTIONS':
					select = document.createElement('select');
					select.style = 'width:300px';
					select.id = 'selectSingleltMatchPromo';
					select.name = select.id;
					
					match_data.forEach(function(smp,index,arr1){
						if(smp.homePlayerSecond == 0 && smp.awayPlayerSecond == 0){
							option = document.createElement('option');
							option.value = smp.matchnumber;
							option.text = smp.matchnumber + '. ' +smp.home_FirstPlayer.full_name + ' - ' + smp.away_FirstPlayer.full_name ;
							select.appendChild(option);
						}
					});
					
					row.insertCell(0).appendChild(select);
					
					option = document.createElement('input');
			   	 	option.type = 'button';
				    option.name = 'populate_single_ltmatch_promo_btn';
				    option.value = 'Populate Matah Promo';
				    option.id = option.name;
				    option.setAttribute('onclick',"processUserSelection(this)");
				    
				    div = document.createElement('div');
				    div.append(option);
		
					option = document.createElement('input');
					option.type = 'button';
					option.name = 'cancel_graphics_btn';
					option.id = option.name;
					option.value = 'Cancel';
					option.setAttribute('onclick','processUserSelection(this)');
			
				    div.append(option);
				    
				    row.insertCell(1).appendChild(div);
				    
					document.getElementById('select_graphic_options_div').style.display = '';
					break;
				case 'DOUBLE-MATCHPROMO-OPTIONS':
					select = document.createElement('select');
					select.style = 'width:300px';
					select.id = 'selectDoubleMatchPromo';
					select.name = select.id;
					
					match_data.forEach(function(smp,index,arr1){
						if(smp.homePlayerSecond != 0 && smp.awayPlayerSecond != 0){
							option = document.createElement('option');
							option.value = smp.matchnumber;
							option.text = smp.matchnumber + '. ' +smp.home_FirstPlayer.ticker_name + ' / ' + smp.home_SecondPlayer.ticker_name + ' - ' + 
								smp.away_FirstPlayer.ticker_name + ' / ' + smp.away_SecondPlayer.ticker_name;
							select.appendChild(option);
						}
					});
					
					row.insertCell(0).appendChild(select);
					
					option = document.createElement('input');
			   	 	option.type = 'button';
				    option.name = 'populate_double_match_promo_btn';
				    option.value = 'Populate Matah Promo';
				    option.id = option.name;
				    option.setAttribute('onclick',"processUserSelection(this)");
				    
				    div = document.createElement('div');
				    div.append(option);
		
					option = document.createElement('input');
					option.type = 'button';
					option.name = 'cancel_graphics_btn';
					option.id = option.name;
					option.value = 'Cancel';
					option.setAttribute('onclick','processUserSelection(this)');
			
				    div.append(option);
				    
				    row.insertCell(1).appendChild(div);
				    
					document.getElementById('select_graphic_options_div').style.display = '';
					break;
				case 'DOUBLE-LT_MATCHPROMO-OPTIONS':
					select = document.createElement('select');
					select.style = 'width:300px';
					select.id = 'selectltDoubleMatchPromo';
					select.name = select.id;
					
					match_data.forEach(function(smp,index,arr1){
						if(smp.homePlayerSecond != 0 && smp.awayPlayerSecond != 0){
							option = document.createElement('option');
							option.value = smp.matchnumber;
							option.text = smp.matchnumber + '. ' +smp.home_FirstPlayer.ticker_name + ' / ' + smp.home_SecondPlayer.ticker_name + ' - ' + 
								smp.away_FirstPlayer.ticker_name + ' / ' + smp.away_SecondPlayer.ticker_name;
							select.appendChild(option);
						}
					});
					
					row.insertCell(0).appendChild(select);
					
					option = document.createElement('input');
			   	 	option.type = 'button';
				    option.name = 'populate_lt_double_match_promo_btn';
				    option.value = 'Populate Matah Promo';
				    option.id = option.name;
				    option.setAttribute('onclick',"processUserSelection(this)");
				    
				    div = document.createElement('div');
				    div.append(option);
		
					option = document.createElement('input');
					option.type = 'button';
					option.name = 'cancel_graphics_btn';
					option.id = option.name;
					option.value = 'Cancel';
					option.setAttribute('onclick','processUserSelection(this)');
			
				    div.append(option);
				    
				    row.insertCell(1).appendChild(div);
				    
					document.getElementById('select_graphic_options_div').style.display = '';
					break;
			}
			break;
		}
		break;
	case 'APIDATA-OPTIONS':
		var home_name,away_name;
		api_value_home = '';
		api_value_away = '';
		header_text = document.createElement('h6');
		header_text.innerHTML = 'DOAD API DATA';
		document.getElementById('select_graphic_options_div').appendChild(header_text);
		
		table = document.createElement('table');
		table.setAttribute('class', 'table table-bordered');
				
		tbody = document.createElement('tbody');

		table.appendChild(tbody);
		document.getElementById('select_graphic_options_div').appendChild(table);

		row = tbody.insertRow(tbody.rows.length);
		alert(dataToProcess.playerTeam1.sets[0].stats.servicestats.aces.number);
		header_text = document.createElement('h6');
		if(dataToProcess.playerTeam1.sets.length > 0) {
			for(var i = 0; i <= dataToProcess.apiData.length -1; i++ ) {
				
			}
			//header_text.innerHTML = header_text.innerHTML  + home_name + ' : ' + '[ ' + api_value_home + ' ]' + "<br>" + "<br>" 
									//+ away_name  + ' : ' + '[ ' + api_value_away + ' ]';
			row.insertCell(0).appendChild(header_text);
			
		}
		break;	
		
	case 'NAMESUPER-OPTIONS': case 'NAMESUPER-SP-OPTIONS': case 'NAMESUPER-DP-OPTIONS': case 'NAMESUPER-SP1-OPTIONS': case 'NAMESUPER-DP1-OPTIONS':
	case 'CROSS-OPTIONS': case 'FIXTURE-OPTIONS':
		
		switch ($('#selectedBroadcaster').val().toUpperCase()) {
		case 'ATP_2022': case 'TPL_2023':

			$('#select_graphic_options_div').empty();
	
			header_text = document.createElement('h6');
			header_text.innerHTML = 'Select Graphic Options';
			document.getElementById('select_graphic_options_div').appendChild(header_text);
			
			table = document.createElement('table');
			table.setAttribute('class', 'table table-bordered');
					
			tbody = document.createElement('tbody');
	
			table.appendChild(tbody);
			document.getElementById('select_graphic_options_div').appendChild(table);
			
			row = tbody.insertRow(tbody.rows.length);
			
			switch(whatToProcess){
				
				case 'FIXTURE-OPTIONS':
					select = document.createElement('select');
					select.id = 'selectTeam1';
					select.name = select.id;
				
					dataToProcess.forEach(function(fix,index,arr1){
						
					option = document.createElement('option');
                    option.value = fix.matchnumber;
                    option.text =  fix.matchnumber +" "+ fix.home_Team.fullname + ' Vs ' + fix.away_Team.fullname;
                    select.appendChild(option);
						
                	});
				
					row.insertCell(cellCount).appendChild(select);
					cellCount = cellCount + 1;
				
					select = document.createElement('select');
					select.id = 'selectTeam';
					select.name = select.id;
					
					option = document.createElement('option');
					option.value = 'mens';
                	option.text = 'Mens' ;
                    select.appendChild(option);
                    
                    option = document.createElement('option');
					option.value = 'womens';
                	option.text = 'Womens' ;
                    select.appendChild(option);
					
					row.insertCell(cellCount).appendChild(select);
					cellCount = cellCount + 1;
					break;
					
				case 'CROSS-OPTIONS':
					select = document.createElement('select');
					select.style = 'width:130px';
					select.id = 'selectCross1';
					select.name = select.id;
					
					option = document.createElement('option');
					option.value = 'home';
					option.text = match_data.homeFirstPlayer.full_name ;
					select.appendChild(option);
					
					option = document.createElement('option');
					option.value = 'away';
					option.text = match_data.awayFirstPlayer.full_name ;
					select.appendChild(option);
					
					row.insertCell(0).appendChild(select);
					
					option = document.createElement('input');
			   	 	option.type = 'button';
				    option.name = 'populate_cross_btn';
				    option.value = 'Populate Cross';
				    option.id = option.name;
				    option.setAttribute('onclick',"processUserSelection(this)");
				    
				    div = document.createElement('div');
				    div.append(option);
		
					option = document.createElement('input');
					option.type = 'button';
					option.name = 'cancel_graphics_btn';
					option.id = option.name;
					option.value = 'Cancel';
					option.setAttribute('onclick','processUserSelection(this)');
			
				    div.append(option);
				    
				    row.insertCell(1).appendChild(div);
				    
					document.getElementById('select_graphic_options_div').style.display = '';
					
					break;
				case 'NAMESUPER-DP1-OPTIONS':
					select = document.createElement('select');
					select.style = 'width:130px';
					select.id = 'selectNameSuperdphome';
					select.name = select.id;
					
					dataToProcess.forEach(function(nsp,index,arr1){
						option = document.createElement('option');
						option.value = nsp.playerId;
						option.text = nsp.full_name;
						select.appendChild(option);
					});
					
					row.insertCell(0).appendChild(select);
					
					select = document.createElement('select');
					select.style = 'width:130px';
					select.id = 'selectNameSuperdpaway';
					select.name = select.id;
					
					dataToProcess.forEach(function(nsp,index,arr1){
						option = document.createElement('option');
						option.value = nsp.playerId;
						option.text = nsp.full_name;
						select.appendChild(option);
					});
					
					row.insertCell(1).appendChild(select);
					
					option = document.createElement('input');
			   	 	option.type = 'button';
				    option.name = 'populate_namesuper_dp1_btn';
				    option.value = 'Populate Namesuper';
				    option.id = option.name;
				    option.setAttribute('onclick',"processUserSelection(this)");
				    
				    div = document.createElement('div');
				    div.append(option);
		
					option = document.createElement('input');
					option.type = 'button';
					option.name = 'cancel_graphics_btn';
					option.id = option.name;
					option.value = 'Cancel';
					option.setAttribute('onclick','processUserSelection(this)');
			
				    div.append(option);
				    
				    row.insertCell(2).appendChild(div);
				    
					document.getElementById('select_graphic_options_div').style.display = '';
					
					break;
				case 'NAMESUPER-SP1-OPTIONS':
					select = document.createElement('select');
					select.style = 'width:130px';
					select.id = 'selectNameSupersp1';
					select.name = select.id;
					
					dataToProcess.forEach(function(nsp,index,arr1){
						option = document.createElement('option');
						option.value = nsp.playerId;
						option.text = nsp.full_name;
						select.appendChild(option);
					});
					
					row.insertCell(0).appendChild(select);
					
					option = document.createElement('input');
			   	 	option.type = 'button';
				    option.name = 'populate_namesuper_sp1_btn';
				    option.value = 'Populate Namesuper';
				    option.id = option.name;
				    option.setAttribute('onclick',"processUserSelection(this)");
				    
				    div = document.createElement('div');
				    div.append(option);
		
					option = document.createElement('input');
					option.type = 'button';
					option.name = 'cancel_graphics_btn';
					option.id = option.name;
					option.value = 'Cancel';
					option.setAttribute('onclick','processUserSelection(this)');
			
				    div.append(option);
				    
				    row.insertCell(1).appendChild(div);
				    
					document.getElementById('select_graphic_options_div').style.display = '';
					
					break;
				case 'NAMESUPER-DP-OPTIONS':
					select = document.createElement('select');
					select.style = 'width:130px';
					select.id = 'selectNameSuperdp';
					select.name = select.id;
					
					option = document.createElement('option');
					option.value = 'home';
					option.text = 'Home' ;
					select.appendChild(option);
					
					option = document.createElement('option');
					option.value = 'away';
					option.text = 'Away' ;
					select.appendChild(option);
					
					row.insertCell(0).appendChild(select);
					
					option = document.createElement('input');
			   	 	option.type = 'button';
				    option.name = 'populate_namesuperdp_btn';
				    option.value = 'Populate Namesuper';
				    option.id = option.name;
				    option.setAttribute('onclick',"processUserSelection(this)");
				    
				    div = document.createElement('div');
				    div.append(option);
		
					option = document.createElement('input');
					option.type = 'button';
					option.name = 'cancel_graphics_btn';
					option.id = option.name;
					option.value = 'Cancel';
					option.setAttribute('onclick','processUserSelection(this)');
			
				    div.append(option);
				    
				    row.insertCell(1).appendChild(div);
					document.getElementById('select_graphic_options_div').style.display = '';
					break;
				case 'NAMESUPER-SP-OPTIONS':
					select = document.createElement('select');
					select.style = 'width:130px';
					select.id = 'selectNameSupersp';
					select.name = select.id;
					
					dataToProcess.forEach(function(nsp,index,arr1){
						//alert("PLAYER ID == "+nsp.playerId);
						//alert("file id "+match_data.homeFirstPlayerId);
						//alert("DB ID "+nsp.playerId);
						if(match_type == 'singles'){
							if(home_team_id1 == nsp.playerId){
								option = document.createElement('option');
								option.value = nsp.playerId;
								option.text = nsp.full_name ;
								select.appendChild(option);
							}
							if(away_team_id1 == nsp.playerId){
								option = document.createElement('option');
								option.value = nsp.playerId;
								option.text = nsp.full_name ;
								select.appendChild(option);
							}
						}else if(match_type == 'doubles'){
							if(home_team_id1 == nsp.playerId){
								option = document.createElement('option');
								option.value = nsp.playerId;
								option.text = nsp.full_name ;
								select.appendChild(option);
							}
							if(home_team_id2 == nsp.playerId){
								option = document.createElement('option');
								option.value = nsp.playerId;
								option.text = nsp.full_name ;
								select.appendChild(option);
							}
							if(away_team_id1 == nsp.playerId){
								option = document.createElement('option');
								option.value = nsp.playerId;
								option.text = nsp.full_name ;
								select.appendChild(option);
							}
							if(away_team_id2 == nsp.playerId){
								option = document.createElement('option');
								option.value = nsp.playerId;
								option.text = nsp.full_name ;
								select.appendChild(option);
							}
						}
					});
					
					row.insertCell(0).appendChild(select);
					
					option = document.createElement('input');
			   	 	option.type = 'button';
				    option.name = 'populate_namesupersp_btn';
				    option.value = 'Populate Namesuper';
				    option.id = option.name;
				    option.setAttribute('onclick',"processUserSelection(this)");
				    
				    div = document.createElement('div');
				    div.append(option);
		
					option = document.createElement('input');
					option.type = 'button';
					option.name = 'cancel_graphics_btn';
					option.id = option.name;
					option.value = 'Cancel';
					option.setAttribute('onclick','processUserSelection(this)');
			
				    div.append(option);
				    
				    row.insertCell(1).appendChild(div);
				   
					document.getElementById('select_graphic_options_div').style.display = '';
					break;
				
				case'NAMESUPER-OPTIONS':
					//alert(match_data.namesuperId);
					
					select = document.createElement('select');
					select.style = 'width:130px';
					select.id = 'selectNameSuper';
					select.name = select.id;
					
					dataToProcess.forEach(function(ns,index,arr1){
						option = document.createElement('option');
						option.value = ns.namesuperId;
						option.text = ns.subHeader ;
						select.appendChild(option);
					});
					
					row.insertCell(0).appendChild(select);
					
					option = document.createElement('input');
			   	 	option.type = 'button';
				    option.name = 'populate_namesuper_btn';
				    option.value = 'Populate Namesuper';
				    option.id = option.name;
				    option.setAttribute('onclick',"processUserSelection(this)");
				    
				    div = document.createElement('div');
				    div.append(option);
		
					option = document.createElement('input');
					option.type = 'button';
					option.name = 'cancel_graphics_btn';
					option.id = option.name;
					option.value = 'Cancel';
					option.setAttribute('onclick','processUserSelection(this)');
			
				    div.append(option);
				    
				    row.insertCell(1).appendChild(div);
				    
					document.getElementById('select_graphic_options_div').style.display = '';
					
					break;
			}
			break;
		}
		break;

	case 'LOAD_GAME_SCORES':
		
		match_data.sets.forEach(function(set,set_index,set_arr){
			if(set.set_number == $('#select_undo_set_number option:selected').val()) {
				set.games.forEach(function(game,game_index,game_arr){
					if(game.game_number == $('#select_undo_game_number option:selected').val()) {
						$('#undo_home_score_txt').val(game.home_score);
						$('#undo_away_score_txt').val(game.away_score);
					}
				});
			}
		});
		
		break;
	
	case 'LOAD_GAMES':
		
		$('#select_undo_game_number').empty();
		
		select = document.getElementById('select_undo_game_number');
		match_data.sets.forEach(function(set,set_index,set_arr){
			if(set.set_number == $('#select_undo_set_number option:selected').val()) {
				set.games.forEach(function(game,game_index,game_arr){
					option = document.createElement('option');
					option.value = game.game_number;
				    option.text = 'Game ' + game.game_number + ' [' + game.home_score + ':' + game.away_score + ']';
				    select.appendChild(option);
				});
				
			}
		});
		break;
		
	case 'LOAD_UNDO':

		$('#select_event_div').empty();
		
		table = document.createElement('table');
		table.setAttribute('class', 'table table-bordered');
				
		tbody = document.createElement('tbody');
		row = tbody.insertRow(tbody.rows.length);
		
		select = document.createElement('select');
		select.id = 'select_undo_set_number';
		select.name = select.id;
		match_data.sets.forEach(function(set,set_index,set_arr){
			option = document.createElement('option');
			option.value = set.set_number;
		    option.text = 'Set ' + set.set_number;
		    select.appendChild(option);
		});
		select.setAttribute('onchange','processUserSelection(this);');
		header_text = document.createElement('label');
		header_text.innerHTML = 'Select Set: '
		header_text.htmlFor = select.id;
		row.insertCell(0).appendChild(header_text).appendChild(select);

		select = document.createElement('select');
		select.id = 'select_undo_game_number';
		select.name = select.id;
		match_data.sets[0].games.forEach(function(game,game_index,game_arr){
			option = document.createElement('option');
			option.value = game.game_number;
		    option.text = 'Game ' + game.game_number + ' [' + game.home_score + ':' + game.away_score + ']';
		    select.appendChild(option);
		});
		header_text = document.createElement('label');
		header_text.innerHTML = 'Select Game: '
		header_text.htmlFor = select.id;
		select.setAttribute('onchange','processUserSelection(this);');
		row.insertCell(1).appendChild(header_text).appendChild(select);

	    div = document.createElement('div');

	    option = document.createElement('input');
	    option.type = 'text';
	    option.name = 'undo_home_score_txt';
	    option.id = option.name;
		header_text = document.createElement('label');
		header_text.innerHTML = 'Home Score: '
		header_text.htmlFor = option.id;
	    div.append(header_text);
		div.append(option);

	    option = document.createElement('input');
	    option.type = 'text';
	    option.name = 'undo_away_score_txt';
	    option.id = option.name;
		header_text = document.createElement('label');
		header_text.innerHTML = 'Away Score: '
		header_text.htmlFor = option.id;
	    div.append(header_text);
		div.append(option);

		row.insertCell(2).appendChild(div);
			
	    div = document.createElement('div');

	    option = document.createElement('input');
	    option.type = 'button';
	    option.name = 'log_game_undo_btn';
	    option.id = option.name;
	    option.value = 'Undo Game Score';
	    option.setAttribute('onclick','processUserSelection(this);');
	    
	    div.append(option);

		option = document.createElement('input');
		option.type = 'button';
		option.name = 'cancel_undo_btn';
		option.id = option.name;
		option.value = 'Cancel';
		option.setAttribute('onclick','processUserSelection(this)');

	    div.append(document.createElement('br'));
	    div.append(option);

	    row.insertCell(3).appendChild(div);

		table.appendChild(tbody);
		
		document.getElementById('select_event_div').appendChild(table);
		
		addItemsToList('LOAD_GAME_SCORES', null);
		
		break;
	
	case 'LOAD_EVENTS':
		
		$('#select_event_div').empty();
	
        for(var i=0; i<=2; i++) {

			table = document.createElement('table');
			table.setAttribute('class', 'table table-bordered');
			tbody = document.createElement('tbody');
            row = tbody.insertRow(tbody.rows.length);
			
			switch (i) {
			case 0:
				header_text = document.createElement('h6');
				header_text.id = 'match_time_hdr';
				header_text.innerHTML = 'Match Time: 00:00:00';
				row.insertCell(0).appendChild(header_text);
				
		   		div = document.createElement('div');
				div.style = 'text-align:center;';
				
			    option = document.createElement('input');
			    option.type = 'button';
			    option.name = 'start_set_btn';
			    option.value = 'Start Tie';
			    option.style = 'width:80px;';
			    option.style.backgroundColor = 'Pink';
			    option.id = option.name;
			    option.setAttribute('onclick','processUserSelection(this);');
				div.appendChild(option);
				row.insertCell(1).appendChild(option);
				
				select = document.createElement('select');
				select.id = 'select_set_winner';
				select.style = 'width:175px;';
				option = document.createElement('option');
				option.value = 'home';	
				if(match_data.matchType.toLowerCase() == 'doubles' && match_data.homeSecondPlayerId > 0) {
					option.text = match_data.homeFirstPlayer.full_name + '/' + match_data.homeSecondPlayer.full_name;
				} else {
					option.text = match_data.homeFirstPlayer.full_name;
				}
				select.appendChild(option);
				
				option = document.createElement('option');
				option.value = 'away';	
				if(match_data.matchType.toLowerCase() == 'doubles' && match_data.awaySecondPlayerId > 0) {
					option.text = match_data.awayFirstPlayer.full_name + '/' + match_data.awaySecondPlayer.full_name;
				} else {
					option.text = match_data.awayFirstPlayer.full_name;
				}
				select.appendChild(option);
				
				text = document.createElement('label');
				text.innerHTML = 'Tie Winner: '
				text.htmlFor = select.id;
				text.style = 'display:none;';
				document.getElementById('select_event_div').appendChild(text).appendChild(select);
		
			    option = document.createElement('input');
			    option.type = 'button';
			    option.name = 'end_set_btn';
			    option.value = 'End Tie';
			    option.style = 'width:80px;';
			    option.style.backgroundColor = 'Pink';
			    option.id = option.name;
			    option.setAttribute('onclick','processUserSelection(this);');
				div.appendChild(option);
				
				row.insertCell(2).appendChild(option);
		
			    option = document.createElement('input');
			    option.type = 'button';
			    option.name = 'reset_set_btn';
			    option.value = 'RESET Tie';
			    option.style = 'width:80px;';
			    option.style.backgroundColor = 'Pink';
			    option.id = option.name;
			    option.setAttribute('onclick','processUserSelection(this);');
				div.appendChild(option);

				row.insertCell(3).appendChild(option);

			    option = document.createElement('input');
			    option.type = 'button';
			    option.name = 'undo_set_btn';
			    option.value = 'UNDO Tie';
			    option.style = 'width:80px;';
			    option.style.backgroundColor = 'Pink';
			    option.id = option.name;
			    option.setAttribute('onclick','processUserSelection(this);');
				div.appendChild(option);

				row.insertCell(4).appendChild(option);

				break;

			case 1:
				text = document.createElement('label');
				text.innerHTML = 'GAME'
				text.htmlFor = select.id;
				
		   		//div = document.createElement('div');
				//div.style = 'text-align:center;';
		
				select = document.createElement('select');
				select.id = 'select_scoring_type';
				select.style = 'width:175px;';
				
				option = document.createElement('option');
				option.value = 'normal';	
				option.text = 'Normal';
				select.appendChild(option);
				
				option = document.createElement('option');
				option.value = 'tie_break';	
				option.text = 'Tie Breaker';
				select.appendChild(option);

				option = document.createElement('option');
				option.value = 'points';	
				option.text = 'Points (TPL)';
				select.appendChild(option);

				text = document.createElement('label');
				text.innerHTML = 'Score Type: '
				text.htmlFor = select.id;
				
				switch (document.getElementById('selectedBroadcaster').value){
				case 'TPL_2023':
					$(select).val('points');
					break;
				}
				//div.appendChild(text).appendChild(select);
				
				row.insertCell(0).appendChild(text).appendChild(select);
							
			    option = document.createElement('input');
			    option.type = 'button';
			    option.name = 'start_game_btn';
			    option.value = 'Start Game';
			    option.style = 'width:85px;';
			    option.style.backgroundColor = 'LightGreen';
			    option.id = option.name;
			    option.setAttribute('onclick','processUserSelection(this);');
				div.appendChild(option);
				row.insertCell(1).appendChild(option);
				
				select = document.createElement('select');
				select.id = 'select_game_winner';
				select.style = 'width:175px;display:none;';
				option = document.createElement('option');
				option.value = 'home';	
				if(match_data.matchType.toLowerCase() == 'doubles' && match_data.homeSecondPlayerId > 0) {
					option.text = match_data.homeFirstPlayer.full_name + '/' + match_data.homeSecondPlayer.full_name;
				} else {
					option.text = match_data.homeFirstPlayer.full_name;
				}
				select.appendChild(option);
				option = document.createElement('option');
				option.value = 'away';	
				if(match_data.matchType.toLowerCase() == 'doubles' && match_data.awaySecondPlayerId > 0) {
					option.text = match_data.awayFirstPlayer.full_name + '/' + match_data.awaySecondPlayer.full_name;
				} else {
					option.text = match_data.awayFirstPlayer.full_name;
				}
				select.appendChild(option);
				
				text = document.createElement('label');
				text.innerHTML = 'Game Winner: '
				text.style = 'display:none;';
				text.htmlFor = select.id;
				document.getElementById('select_event_div').appendChild(text).appendChild(select);
		
			    option = document.createElement('input');
			    option.type = 'button';
			    option.name = 'end_game_btn';
			    option.value = 'End Game';
			    option.style = 'width:80px;';
			   	option.style.backgroundColor = 'LightGreen';
			    option.id = option.name;
			    option.setAttribute('onclick','processUserSelection(this);');
				div.appendChild(option);
				
				row.insertCell(2).appendChild(option);
				
			    option = document.createElement('input');
			    option.type = 'button';
			    option.name = 'reset_game_btn';
			    option.value = 'RESET Game';
			    option.style = 'width:100px;';
			    option.style.backgroundColor = 'LightGreen';
			    //option.style.color = 'Red';
			    option.id = option.name;
			    option.setAttribute('onclick','processUserSelection(this);');
				div.appendChild(option);

				row.insertCell(3).appendChild(option);

				break;

			case 2:

				option = document.createElement('select');
				option.id = 'select_serving_player';
				option.name = option.id;
				option.style = 'text-align:center;width:175px;';
				text = document.createElement('option');
				text.value = match_data.homeFirstPlayerId;
				text.text = match_data.homeFirstPlayer.full_name;
				option.appendChild(text);
				if(match_data.matchType.toLowerCase() == 'doubles' && match_data.homeSecondPlayerId > 0) {
					text = document.createElement('option');
					text.value = match_data.homeSecondPlayerId;
					text.text = match_data.homeSecondPlayer.full_name;
					option.appendChild(text);
				}
				text = document.createElement('option');
				text.value = match_data.awayFirstPlayerId;
				text.text = match_data.awayFirstPlayer.full_name;
				option.appendChild(text);
				if(match_data.matchType.toLowerCase() == 'doubles' && match_data.awaySecondPlayerId > 0) {
					text = document.createElement('option');
					text.value = match_data.awaySecondPlayerId;
					text.text = match_data.awaySecondPlayer.full_name;
					option.appendChild(text);
				}
				option.setAttribute('onchange','processUserSelection(this);');
				
				text = document.createElement('label');
				text.innerHTML = 'Select Serve: ';
				text.htmlFor = option.id;
				document.getElementById('select_event_div').appendChild(text).appendChild(option);

	    		for(var j=0; j<=2; j++) {
	        		div = document.createElement('div');
	   				div.style = 'text-align:center;';
					switch (j) {
					case 0: case 2:
						header_text = document.createElement('label');
		    			switch (j) {
		    			case 0: //home
							text = 'home';
							let name ='';
							if(match_data.matchType.toLowerCase() == 'doubles' && match_data.homeSecondPlayerId > 0) {
								name = match_data.homeFirstPlayer.full_name + '/' + match_data.homeSecondPlayer.full_name;
							} else {
								name = match_data.homeFirstPlayer.full_name;
							}
							header_text.innerHTML = name + ' <br>[<span style="color: #FF5733;"> '+match_data.homeFirstPlayer.team.teamName1 + '</span> ]';
		    				break;
		    			case 2: //away
							text = 'away';
							let Away_name ='';
							if(match_data.matchType.toLowerCase() == 'doubles' && match_data.awaySecondPlayerId > 0) {
								Away_name = match_data.awayFirstPlayer.full_name + '/' + match_data.awaySecondPlayer.full_name;
							} else {
								Away_name = match_data.awayFirstPlayer.full_name;
							}
							header_text.innerHTML = Away_name +'<br> [ <span style="color: #FF5733;">' + match_data.awayFirstPlayer.team.teamName1 + '</span> ]';
		    				break;
						}
						div.appendChild(header_text);
		        		link_div = document.createElement('div');
						for(var k=0; k<=2; k++) {
							switch (k) {
							case 0: case 2:
			        			option = document.createElement('input');
			    				option.type = "button";
			    				if (k == 0) {
			        				option.id = text + '_increment_score_btn';
			        				option.value="+";
			        				option.setAttribute('onclick','processUserSelection(this);');
			        				}
			    				else {
									option.id = text + '_decrement_score_btn';
									option.value="-";
									option.setAttribute('onclick','processUserSelection(this);');
									break;
								}
			    				option.style = 'text-align:center;';
								break;
							case 1: 
			        			option = document.createElement('input');
			    				option.type = "text";
			    				option.id = text + 'Score';
			    				option.style = 'width:25%;text-align:center;';
								option.value = '0';
								match_data.sets.forEach(function(set,set_index,set_arr){
									set.games.forEach(function(game,game_index,game_arr){
										switch(j) {
										case 0:
											option.value = game.home_score;
											break;
										case 2:
											option.value = game.away_score;
											break;
										}
									});
								});
			    				break;
							}
							link_div.appendChild(option);
					    }	
						div.appendChild(link_div);
					    break;
					
					case 1:
	
						text = document.createElement('label');
						text.innerHTML = 'Score'

		    			option = document.createElement('input');
						option.type = "button";
						option.id = 'load_stat';
						option.name = option.id;
						option.value="Log Stat";
						option.setAttribute('onclick','processUserSelection(this)');
	
						div.appendChild(text);
						div.appendChild(document.createElement("br"));
						div.appendChild(option);
						
						break;		
				    }
				    row.insertCell(j).appendChild(div);
		     	 }	 
				break;
			}
			table.appendChild(tbody);
			document.getElementById('select_event_div').appendChild(table);
	    }		

		if(match_data) {
			match_data.sets.forEach(function(set,set_index,set_arr){
				set.games.forEach(function(game,game_index,game_arr){
					if(game.game_status.toLowerCase() == 'start') {
						$('#select_scoring_type').val(game.game_type);
					}
				});
			});
		}
		
		break;
				
	case 'LOAD_MATCH':
		
		$('#tennis_div').empty();
		
		if (dataToProcess)
		{
			table = document.createElement('table');
			table.setAttribute('class', 'table table-bordered');
			tbody = document.createElement('tbody');
			row = tbody.insertRow(tbody.rows.length);
			for (var j = 1; j <= (match_data.sets.length + 1); j++) {
				cell = row.insertCell(row.cells.length);
			    th = document.createElement('th'); //column
			    switch (j) {
				case 1:
				    cell.innerHTML = ''; 
					break;
				default:
				    cell.innerHTML = match_data.sets[j-2].set_number; 
					break;
				}
			}
						
			for (var j = 0; j <= 1; j++) {
				row = tbody.insertRow(tbody.rows.length);
				cell = row.insertCell(row.cells.length);
			    switch (j) {
				case 0:
					//cell.innerHTML = match_data.homeFirstPlayer.team.teamName1;
				    cell.innerHTML = match_data.homeFirstPlayer.full_name;
					if(match_data.matchType.toLowerCase() == 'doubles' && match_data.homeSecondPlayerId > 0) {
					    cell.innerHTML = cell.innerHTML + '/' + match_data.homeSecondPlayer.full_name;
					}
					match_data.sets.forEach(function(set,set_index,set_arr){
						text = 0; 
						set.games.forEach(function(game,game_index,game_arr){
							if(game.game_winner.toLowerCase() == 'home') {
								text = text + 1;
							}
							if(game.game_status.toLowerCase() == 'start') {
								$('#homeScore').val(game.home_score);
							}
						});
						cell = row.insertCell(row.cells.length);
						cell.innerHTML = text;
					});
					break;
				case 1:
					//cell.innerHTML = match_data.awayFirstPlayer.team.teamName1;
				    cell.innerHTML = match_data.awayFirstPlayer.full_name;
					if(match_data.matchType.toLowerCase() == 'doubles' && match_data.awaySecondPlayerId > 0) {
					    cell.innerHTML = cell.innerHTML + '/' + match_data.awaySecondPlayer.full_name;
					}
					
					match_data.sets.forEach(function(set,set_index,set_arr){
						text = 0; 
						set.games.forEach(function(game,game_index,game_arr){
							if(game.game_winner.toLowerCase() == 'away') {
								text = text + 1;
							}
							if(game.game_status.toLowerCase() == 'start') {
								$('#awayScore').val(game.away_score);
							}
						});
						cell = row.insertCell(row.cells.length);
						cell.innerHTML = text;
					});
					break;
				}
			}
			table.appendChild(tbody);
			document.getElementById('tennis_div').appendChild(table);
		}
	break;
	}
}
function removeSelectDuplicates(select_id)
{
	var this_list = {};
	$("select[id='" + select_id + "'] > option").each(function () {
	    if(this_list[this.text]) {
	        $(this).remove();
	    } else {
	        this_list[this.text] = this.value;
	    }
	});
}
function checkEmpty(inputBox,textToShow) {

	var name = $(inputBox).attr('id');
	
	document.getElementById(name + '-validation').innerHTML = '';
	document.getElementById(name + '-validation').style.display = 'none';
	$(inputBox).css('border','');
	if(document.getElementById(name).value.trim() == '') {
		$(inputBox).css('border','#E11E26 2px solid');
		document.getElementById(name + '-validation').innerHTML = textToShow + ' required';
		document.getElementById(name + '-validation').style.display = '';
		document.getElementById(name).focus({preventScroll:false});
		return false;
	}
	return true;	
}	
