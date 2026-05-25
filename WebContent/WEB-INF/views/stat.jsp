<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8" name="viewport" content="width=device-width, initial-scale=1">
  <title>Tennis Stats</title>
  <script type="text/javascript" src="<c:url value='/webjars/jquery/3.7.1/jquery.min.js' />"></script>
  <script type="text/javascript" src="<c:url value='/webjars/bootstrap/5.3.3/js/bootstrap.bundle.min.js' />"></script>
  <script type="text/javascript" src="<c:url value='/resources/javascript/index.js' />"></script>
  <link rel="stylesheet" href="<c:url value='/webjars/bootstrap/5.3.3/css/bootstrap.min.css' />" />
</head>
<body onload="processTennisProcedures('READ_MATCH_FOR_STATS',null)">
<form:form name="tennis_stat_form" autocomplete="off">
<div class="content py-5" style="background-color: #EAE8FF; color: #2E008B">
  <div class="container">
	<div class="row">
	 <div class="col-md-12 offset-md-12">
       <span class="anchor"></span>
         <div class="card card-outline-secondary">
          <div class="card-body">
			<div class="row">
			    <h6>Tennis Stats</h6>
			    <div class="col-sm-8 col-md-8">
				    <button style="background-color:#2E008B;color:#FEFEFE;" class="btn btn-sm" type="button"
				  		name="exit_stat_page_btn" id="exit_stat_page_btn" onclick="processUserSelection(this)">Exit</button>
			    </div>			    
 				<table class="table table-bordered table-responsive">
				  <thead>
				    <tr>
				      <th scope="col"></th>
				      <th scope="col">${session_match.homeFirstPlayer.full_name}</th>
					  <c:if test = "${session_match.matchType == 'doubles'}">
					      <th scope="col">${session_match.homeSecondPlayer.full_name}</th>
					  </c:if>
				      <th scope="col">${session_match.awayFirstPlayer.full_name}</th>
					  <c:if test = "${session_match.matchType == 'doubles'}">
					      <th scope="col">${session_match.awaySecondPlayer.full_name}</th>
					  </c:if>
				    </tr>
				  </thead>
				  <tbody>
				    <tr>
				      <td>
					    <label>Ace</label>
					  </td>
				      <td>
					    <input id="ace_increment_home_first_btn" type="button" onclick="processUserSelection(this)" value="+"></input>
					    <input id="ace_home_first_txt" type="text" style="width:25%" value="0"></input>
					    <input id="ace_decrement_home_first_btn" type="button" onclick="processUserSelection(this)" value="-"></input>
					  </td>
					  <c:if test = "${session_match.matchType == 'doubles'}">
					      <td>
						    <input id="ace_increment_home_second_btn" type="button" onclick="processUserSelection(this)" value="+"></input>
						    <input id="ace_home_second_txt" type="text" style="width:25%" value="0"></input>
						    <input id="ace_decrement_home_second_btn" type="button" onclick="processUserSelection(this)" value="-"></input>
						  </td>
					  </c:if>
				      <td>
					    <input id="ace_increment_away_first_btn" type="button" onclick="processUserSelection(this)" value="+"></input>
					    <input id="ace_away_first_txt" type="text" style="width:25%" value="0"></input>
					    <input id="ace_decrement_away_first_btn" type="button" onclick="processUserSelection(this)" value="-"></input>
					  </td>
					  <c:if test = "${session_match.matchType == 'doubles'}">
					      <td>
						    <input id="ace_increment_away_second_btn" type="button" onclick="processUserSelection(this)" value="+"></input>
						    <input id="ace_away_second_txt" type="text" style="width:25%" value="0"></input>
						    <input id="ace_decrement_away_second_btn" type="button" onclick="processUserSelection(this)" value="-"></input>
						  </td>
					  </c:if>
				    </tr>
				    <tr>
				      <td>
					    <label>1st Serve</label>
					  </td>
				      <td>
					    <input id="firstServe_increment_home_first_btn" type="button" onclick="processUserSelection(this)" value="+"></input>
					    <input id="firstServe_home_first_txt" type="text" style="width:25%" value="0"></input>
					    <input id="firstServe_decrement_home_first_btn" type="button" onclick="processUserSelection(this)" value="-"></input>
					  </td>
					  <c:if test = "${session_match.matchType == 'doubles'}">
					      <td>
						    <input id="firstServe_increment_home_second_btn" type="button" onclick="processUserSelection(this)" value="+"></input>
						    <input id="firstServe_home_second_txt" type="text" style="width:25%" value="0"></input>
						    <input id="firstServe_decrement_home_second_btn" type="button" onclick="processUserSelection(this)" value="-"></input>
						  </td>
					  </c:if>
				      <td>
					    <input id="firstServe_increment_away_first_btn" type="button" onclick="processUserSelection(this)" value="+"></input>
					    <input id="firstServe_away_first_txt" type="text" style="width:25%" value="0"></input>
					    <input id="firstServe_decrement_away_first_btn" type="button" onclick="processUserSelection(this)" value="-"></input>
					  </td>
					  <c:if test = "${session_match.matchType == 'doubles'}">
					      <td>
						    <input id="firstServe_increment_away_second_btn" type="button" onclick="processUserSelection(this)" value="+"></input>
						    <input id="firstServe_away_second_txt" type="text" style="width:25%" value="0"></input>
						    <input id="firstServe_decrement_away_second_btn" type="button" onclick="processUserSelection(this)" value="-"></input>
						  </td>
					  </c:if>
				    </tr>
				    <tr>
				      <td>
					    <label>1st Serve Won</label>
					  </td>
				      <td>
					    <input id="firstServeWon_increment_home_first_btn" type="button" onclick="processUserSelection(this)" value="+"></input>
					    <input id="firstServeWon_home_first_txt" type="text" style="width:25%" value="0"></input>
					    <input id="firstServeWon_decrement_home_first_btn" type="button" onclick="processUserSelection(this)" value="-"></input>
					  </td>
					  <c:if test = "${session_match.matchType == 'doubles'}">
					      <td>
						    <input id="firstServeWon_increment_home_second_btn" type="button" onclick="processUserSelection(this)" value="+"></input>
						    <input id="firstServeWon_home_second_txt" type="text" style="width:25%" value="0"></input>
						    <input id="firstServeWon_decrement_home_second_btn" type="button" onclick="processUserSelection(this)" value="-"></input>
						  </td>
					  </c:if>
				      <td>
					    <input id="firstServeWon_increment_away_first_btn" type="button" onclick="processUserSelection(this)" value="+"></input>
					    <input id="firstServeWon_away_first_txt" type="text" style="width:25%" value="0"></input>
					    <input id="firstServeWon_decrement_away_first_btn" type="button" onclick="processUserSelection(this)" value="-"></input>
					  </td>
					  <c:if test = "${session_match.matchType == 'doubles'}">
					      <td>
						    <input id="firstServeWon_increment_away_second_btn" type="button" onclick="processUserSelection(this)" value="+"></input>
						    <input id="firstServeWon_away_second_txt" type="text" style="width:25%" value="0"></input>
						    <input id="firstServeWon_decrement_away_second_btn" type="button" onclick="processUserSelection(this)" value="-"></input>
						  </td>
					  </c:if>
				    </tr>
				    <tr>
				      <td>
					    <label>2nd Serve</label>
					  </td>
				      <td>
					    <input id="secondServe_increment_home_first_btn" type="button" onclick="processUserSelection(this)" value="+"></input>
					    <input id="secondServe_home_first_txt" type="text" style="width:25%" value="0"></input>
					    <input id="secondServe_decrement_home_first_btn" type="button" onclick="processUserSelection(this)" value="-"></input>
					  </td>
					  <c:if test = "${session_match.matchType == 'doubles'}">
					      <td>
						    <input id="secondServe_increment_home_second_btn" type="button" onclick="processUserSelection(this)" value="+"></input>
						    <input id="secondServe_home_second_txt" type="text" style="width:25%" value="0"></input>
						    <input id="secondServe_decrement_home_second_btn" type="button" onclick="processUserSelection(this)" value="-"></input>
						  </td>
					  </c:if>
				      <td>
					    <input id="secondServe_increment_away_first_btn" type="button" onclick="processUserSelection(this)" value="+"></input>
					    <input id="secondServe_away_first_txt" type="text" style="width:25%" value="0"></input>
					    <input id="secondServe_decrement_away_first_btn" type="button" onclick="processUserSelection(this)" value="-"></input>
					  </td>
					  <c:if test = "${session_match.matchType == 'doubles'}">
					      <td>
						    <input id="secondServe_increment_away_second_btn" type="button" onclick="processUserSelection(this)" value="+"></input>
						    <input id="secondServe_away_second_txt" type="text" style="width:25%" value="0"></input>
						    <input id="secondServe_decrement_away_second_btn" type="button" onclick="processUserSelection(this)" value="-"></input>
						  </td>
					  </c:if>
				    </tr>
				    <tr>
				      <td>
					    <label>2nd Serve Won</label>
					  </td>
				      <td>
					    <input id="secondServeWon_increment_home_first_btn" type="button" onclick="processUserSelection(this)" value="+"></input>
					    <input id="secondServeWon_home_first_txt" type="text" style="width:25%" value="0"></input>
					    <input id="secondServeWon_decrement_home_first_btn" type="button" onclick="processUserSelection(this)" value="-"></input>
					  </td>
					  <c:if test = "${session_match.matchType == 'doubles'}">
					      <td>
						    <input id="secondServeWon_increment_home_second_btn" type="button" onclick="processUserSelection(this)" value="+"></input>
						    <input id="secondServeWon_home_second_txt" type="text" style="width:25%" value="0"></input>
						    <input id="secondServeWon_decrement_home_second_btn" type="button" onclick="processUserSelection(this)" value="-"></input>
						  </td>
					  </c:if>
				      <td>
					    <input id="secondServeWon_increment_away_first_btn" type="button" onclick="processUserSelection(this)" value="+"></input>
					    <input id="secondServeWon_away_first_txt" type="text" style="width:25%" value="0"></input>
					    <input id="secondServeWon_decrement_away_first_btn" type="button" onclick="processUserSelection(this)" value="-"></input>
					  </td>
					  <c:if test = "${session_match.matchType == 'doubles'}">
					      <td>
						    <input id="secondServeWon_increment_away_second_btn" type="button" onclick="processUserSelection(this)" value="+"></input>
						    <input id="secondServeWon_away_second_txt" type="text" style="width:25%" value="0"></input>
						    <input id="secondServeWon_decrement_away_second_btn" type="button" onclick="processUserSelection(this)" value="-"></input>
						  </td>
					  </c:if>
				    </tr>
				    <tr>
				      <td>
					    <label>Forehand Winner</label>
					  </td>
				      <td>
					    <input id="forehandWinner_increment_home_first_btn" type="button" onclick="processUserSelection(this)" value="+"></input>
					    <input id="forehandWinner_home_first_txt" type="text" style="width:25%" value="0"></input>
					    <input id="forehandWinner_decrement_home_first_btn" type="button" onclick="processUserSelection(this)" value="-"></input>
					  </td>
					  <c:if test = "${session_match.matchType == 'doubles'}">
					      <td>
						    <input id="forehandWinner_increment_home_second_btn" type="button" onclick="processUserSelection(this)" value="+"></input>
						    <input id="forehandWinner_home_second_txt" type="text" style="width:25%" value="0"></input>
						    <input id="forehandWinner_decrement_home_second_btn" type="button" onclick="processUserSelection(this)" value="-"></input>
						  </td>
					  </c:if>
				      <td>
					    <input id="forehandWinner_increment_away_first_btn" type="button" onclick="processUserSelection(this)" value="+"></input>
					    <input id="forehandWinner_away_first_txt" type="text" style="width:25%" value="0"></input>
					    <input id="forehandWinner_decrement_away_first_btn" type="button" onclick="processUserSelection(this)" value="-"></input>
					  </td>
					  <c:if test = "${session_match.matchType == 'doubles'}">
					      <td>
						    <input id="forehandWinner_increment_away_second_btn" type="button" onclick="processUserSelection(this)" value="+"></input>
						    <input id="forehandWinner_away_second_txt" type="text" style="width:25%" value="0"></input>
						    <input id="forehandWinner_decrement_away_second_btn" type="button" onclick="processUserSelection(this)" value="-"></input>
						  </td>
					  </c:if>
				    </tr>
				    <tr>
				      <td>
					    <label>Back Hand Winner</label>
					  </td>
				      <td>
					    <input id="backhandWinner_increment_home_first_btn" type="button" onclick="processUserSelection(this)" value="+"></input>
					    <input id="backhandWinner_home_first_txt" type="text" style="width:25%" value="0"></input>
					    <input id="backhandWinner_decrement_home_first_btn" type="button" onclick="processUserSelection(this)" value="-"></input>
					  </td>
					  <c:if test = "${session_match.matchType == 'doubles'}">
					      <td>
						    <input id="backhandWinner_increment_home_second_btn" type="button" onclick="processUserSelection(this)" value="+"></input>
						    <input id="backhandWinner_home_second_txt" type="text" style="width:25%" value="0"></input>
						    <input id="backhandWinner_decrement_home_second_btn" type="button" onclick="processUserSelection(this)" value="-"></input>
						  </td>
					  </c:if>
				      <td>
					    <input id="backhandWinner_increment_away_first_btn" type="button" onclick="processUserSelection(this)" value="+"></input>
					    <input id="backhandWinner_away_first_txt" type="text" style="width:25%" value="0"></input>
					    <input id="backhandWinner_decrement_away_first_btn" type="button" onclick="processUserSelection(this)" value="-"></input>
					  </td>
					  <c:if test = "${session_match.matchType == 'doubles'}">
					      <td>
						    <input id="backhandWinner_increment_away_second_btn" type="button" onclick="processUserSelection(this)" value="+"></input>
						    <input id="backhandWinner_away_second_txt" type="text" style="width:25%" value="0"></input>
						    <input id="backhandWinner_decrement_away_second_btn" type="button" onclick="processUserSelection(this)" value="-"></input>
						  </td>
					  </c:if>
				    </tr>
				    <tr>
				      <td>
					    <label>Serve Winner</label>
					  </td>
				      <td>
					    <input id="serveWinner_increment_home_first_btn" type="button" onclick="processUserSelection(this)" value="+"></input>
					    <input id="serveWinner_home_first_txt" type="text" style="width:25%" value="0"></input>
					    <input id="serveWinner_decrement_home_first_btn" type="button" onclick="processUserSelection(this)" value="-"></input>
					  </td>
					  <c:if test = "${session_match.matchType == 'doubles'}">
					      <td>
						    <input id="serveWinner_increment_home_second_btn" type="button" onclick="processUserSelection(this)" value="+"></input>
						    <input id="serveWinner_home_second_txt" type="text" style="width:25%" value="0"></input>
						    <input id="serveWinner_decrement_home_second_btn" type="button" onclick="processUserSelection(this)" value="-"></input>
						  </td>
					  </c:if>
				      <td>
					    <input id="serveWinner_increment_away_first_btn" type="button" onclick="processUserSelection(this)" value="+"></input>
					    <input id="serveWinner_away_first_txt" type="text" style="width:25%" value="0"></input>
					    <input id="serveWinner_decrement_away_first_btn" type="button" onclick="processUserSelection(this)" value="-"></input>
					  </td>
					  <c:if test = "${session_match.matchType == 'doubles'}">
					      <td>
						    <input id="serveWinner_increment_away_second_btn" type="button" onclick="processUserSelection(this)" value="+"></input>
						    <input id="serveWinner_away_second_txt" type="text" style="width:25%" value="0"></input>
						    <input id="serveWinner_decrement_away_second_btn" type="button" onclick="processUserSelection(this)" value="-"></input>
						  </td>
					  </c:if>
				    </tr>
				    <tr>
				      <td>
					    <label>Fault 1st Serve</label>
					  </td>
				      <td>
					    <input id="firstServeFault_increment_home_first_btn" type="button" onclick="processUserSelection(this)" value="+"></input>
					    <input id="firstServeFault_home_first_txt" type="text" style="width:25%" value="0"></input>
					    <input id="firstServeFault_decrement_home_first_btn" type="button" onclick="processUserSelection(this)" value="-"></input>
					  </td>
					  <c:if test = "${session_match.matchType == 'doubles'}">
					      <td>
						    <input id="firstServeFault_increment_home_second_btn" type="button" onclick="processUserSelection(this)" value="+"></input>
						    <input id="firstServeFault_home_second_txt" type="text" style="width:25%" value="0"></input>
						    <input id="firstServeFault_decrement_home_second_btn" type="button" onclick="processUserSelection(this)" value="-"></input>
						  </td>
					  </c:if>
				      <td>
					    <input id="firstServeFault_increment_away_first_btn" type="button" onclick="processUserSelection(this)" value="+"></input>
					    <input id="firstServeFault_away_first_txt" type="text" style="width:25%" value="0"></input>
					    <input id="firstServeFault_decrement_away_first_btn" type="button" onclick="processUserSelection(this)" value="-"></input>
					  </td>
					  <c:if test = "${session_match.matchType == 'doubles'}">
					      <td>
						    <input id="firstServeFault_increment_away_second_btn" type="button" onclick="processUserSelection(this)" value="+"></input>
						    <input id="firstServeFault_away_second_txt" type="text" style="width:25%" value="0"></input>
						    <input id="firstServeFault_decrement_away_second_btn" type="button" onclick="processUserSelection(this)" value="-"></input>
						  </td>
					  </c:if>
				    </tr>
				    <tr>
				      <td>
					    <label>Double Fault</label>
					  </td>
				      <td>
					    <input id="doubleFault_increment_home_first_btn" type="button" onclick="processUserSelection(this)" value="+"></input>
					    <input id="doubleFault_home_first_txt" type="text" style="width:25%" value="0"></input>
					    <input id="doubleFault_decrement_home_first_btn" type="button" onclick="processUserSelection(this)" value="-"></input>
					  </td>
					  <c:if test = "${session_match.matchType == 'doubles'}">
					      <td>
						    <input id="doubleFault_increment_home_second_btn" type="button" onclick="processUserSelection(this)" value="+"></input>
						    <input id="doubleFault_home_second_txt" type="text" style="width:25%" value="0"></input>
						    <input id="doubleFault_decrement_home_second_btn" type="button" onclick="processUserSelection(this)" value="-"></input>
						  </td>
					  </c:if>
				      <td>
					    <input id="doubleFault_increment_away_first_btn" type="button" onclick="processUserSelection(this)" value="+"></input>
					    <input id="doubleFault_away_first_txt" type="text" style="width:25%" value="0"></input>
					    <input id="doubleFault_decrement_away_first_btn" type="button" onclick="processUserSelection(this)" value="-"></input>
					  </td>
					  <c:if test = "${session_match.matchType == 'doubles'}">
					      <td>
						    <input id="doubleFault_increment_away_second_btn" type="button" onclick="processUserSelection(this)" value="+"></input>
						    <input id="doubleFault_away_second_txt" type="text" style="width:25%" value="0"></input>
						    <input id="doubleFault_decrement_away_second_btn" type="button" onclick="processUserSelection(this)" value="-"></input>
						  </td>
					  </c:if>
				    </tr>
				    <tr>
				      <td>
					    <label>Unreturned Serves</label>
					  </td>
				      <td>
					    <input id="unreturnedServe_increment_home_first_btn" type="button" onclick="processUserSelection(this)" value="+"></input>
					    <input id="unreturnedServe_home_first_txt" type="text" style="width:25%" value="0"></input>
					    <input id="unreturnedServe_decrement_home_first_btn" type="button" onclick="processUserSelection(this)" value="-"></input>
					  </td>
					  <c:if test = "${session_match.matchType == 'doubles'}">
					      <td>
						    <input id="unreturnedServe_increment_home_second_btn" type="button" onclick="processUserSelection(this)" value="+"></input>
						    <input id="unreturnedServe_home_second_txt" type="text" style="width:25%" value="0"></input>
						    <input id="unreturnedServe_decrement_home_second_btn" type="button" onclick="processUserSelection(this)" value="-"></input>
						  </td>
					  </c:if>
				      <td>
					    <input id="unreturnedServe_increment_away_first_btn" type="button" onclick="processUserSelection(this)" value="+"></input>
					    <input id="unreturnedServe_away_first_txt" type="text" style="width:25%" value="0"></input>
					    <input id="unreturnedServe_decrement_away_first_btn" type="button" onclick="processUserSelection(this)" value="-"></input>
					  </td>
					  <c:if test = "${session_match.matchType == 'doubles'}">
					      <td>
						    <input id="unreturnedServe_increment_away_second_btn" type="button" onclick="processUserSelection(this)" value="+"></input>
						    <input id="unreturnedServe_away_second_txt" type="text" style="width:25%" value="0"></input>
						    <input id="unreturnedServe_decrement_away_second_btn" type="button" onclick="processUserSelection(this)" value="-"></input>
						  </td>
					  </c:if>
				    </tr>
				    <tr>
				      <td>
					    <label>Unforced Error</label>
					  </td>
				      <td>
					    <input id="unforcedError_increment_home_first_btn" type="button" onclick="processUserSelection(this)" value="+"></input>
					    <input id="unforcedError_home_first_txt" type="text" style="width:25%" value="0"></input>
					    <input id="unforcedError_decrement_home_first_btn" type="button" onclick="processUserSelection(this)" value="-"></input>
					  </td>
					  <c:if test = "${session_match.matchType == 'doubles'}">
					      <td>
						    <input id="unforcedError_increment_home_second_btn" type="button" onclick="processUserSelection(this)" value="+"></input>
						    <input id="unforcedError_home_second_txt" type="text" style="width:25%" value="0"></input>
						    <input id="unforcedError_decrement_home_second_btn" type="button" onclick="processUserSelection(this)" value="-"></input>
						  </td>
					  </c:if>
				      <td>
					    <input id="unforcedError_increment_away_first_btn" type="button" onclick="processUserSelection(this)" value="+"></input>
					    <input id="unforcedError_away_first_txt" type="text" style="width:25%" value="0"></input>
					    <input id="unforcedError_decrement_away_first_btn" type="button" onclick="processUserSelection(this)" value="-"></input>
					  </td>
					  <c:if test = "${session_match.matchType == 'doubles'}">
					      <td>
						    <input id="unforcedError_increment_away_second_btn" type="button" onclick="processUserSelection(this)" value="+"></input>
						    <input id="unforcedError_away_second_txt" type="text" style="width:25%" value="0"></input>
						    <input id="unforcedError_decrement_away_second_btn" type="button" onclick="processUserSelection(this)" value="-"></input>
						  </td>
					  </c:if>
				    </tr>
				    <tr>
				      <td>
					    <label>Baseline Points</label>
					  </td>
				      <td>
					    <input id="baselinePoint_increment_home_first_btn" type="button" onclick="processUserSelection(this)" value="+"></input>
					    <input id="baselinePoint_home_first_txt" type="text" style="width:25%" value="0"></input>
					    <input id="baselinePoint_decrement_home_first_btn" type="button" onclick="processUserSelection(this)" value="-"></input>
					  </td>
					  <c:if test = "${session_match.matchType == 'doubles'}">
					      <td>
						    <input id="baselinePoint_increment_home_second_btn" type="button" onclick="processUserSelection(this)" value="+"></input>
						    <input id="baselinePoint_home_second_txt" type="text" style="width:25%" value="0"></input>
						    <input id="baselinePoint_decrement_home_second_btn" type="button" onclick="processUserSelection(this)" value="-"></input>
						  </td>
					  </c:if>
				      <td>
					    <input id="baselinePoint_increment_away_first_btn" type="button" onclick="processUserSelection(this)" value="+"></input>
					    <input id="baselinePoint_away_first_txt" type="text" style="width:25%" value="0"></input>
					    <input id="baselinePoint_decrement_away_first_btn" type="button" onclick="processUserSelection(this)" value="-"></input>
					  </td>
					  <c:if test = "${session_match.matchType == 'doubles'}">
					      <td>
						    <input id="baselinePoint_increment_away_second_btn" type="button" onclick="processUserSelection(this)" value="+"></input>
						    <input id="baselinePoint_away_second_txt" type="text" style="width:25%" value="0"></input>
						    <input id="baselinePoint_decrement_away_second_btn" type="button" onclick="processUserSelection(this)" value="-"></input>
						  </td>
					  </c:if>
				    </tr>
				    <tr>
				      <td>
					    <label>Net Points</label>
					  </td>
				      <td>
					    <input id="netPoint_increment_home_first_btn" type="button" onclick="processUserSelection(this)" value="+"></input>
					    <input id="netPoint_home_first_txt" type="text" style="width:25%" value="0"></input>
					    <input id="netPoint_decrement_home_first_btn" type="button" onclick="processUserSelection(this)" value="-"></input>
					  </td>
					  <c:if test = "${session_match.matchType == 'doubles'}">
					      <td>
						    <input id="netPoint_increment_home_second_btn" type="button" onclick="processUserSelection(this)" value="+"></input>
						    <input id="netPoint_home_second_txt" type="text" style="width:25%" value="0"></input>
						    <input id="netPoint_decrement_home_second_btn" type="button" onclick="processUserSelection(this)" value="-"></input>
						  </td>
					  </c:if>
				      <td>
					    <input id="netPoint_increment_away_first_btn" type="button" onclick="processUserSelection(this)" value="+"></input>
					    <input id="netPoint_away_first_txt" type="text" style="width:25%" value="0"></input>
					    <input id="netPoint_decrement_away_first_btn" type="button" onclick="processUserSelection(this)" value="-"></input>
					  </td>
					  <c:if test = "${session_match.matchType == 'doubles'}">
					      <td>
						    <input id="netPoint_increment_away_second_btn" type="button" onclick="processUserSelection(this)" value="+"></input>
						    <input id="netPoint_away_second_txt" type="text" style="width:25%" value="0"></input>
						    <input id="netPoint_decrement_away_second_btn" type="button" onclick="processUserSelection(this)" value="-"></input>
						  </td>
					  </c:if>
				    </tr>
				    <tr>
				      <td>
					    <label>Front Hand Error</label>
					  </td>
				      <td>
					    <input id="frontHandError_increment_home_first_btn" type="button" onclick="processUserSelection(this)" value="+"></input>
					    <input id="frontHandError_home_first_txt" type="text" style="width:25%" value="0"></input>
					    <input id="frontHandError_decrement_home_first_btn" type="button" onclick="processUserSelection(this)" value="-"></input>
					  </td>
					  <c:if test = "${session_match.matchType == 'doubles'}">
					      <td>
						    <input id="frontHandError_increment_home_second_btn" type="button" onclick="processUserSelection(this)" value="+"></input>
						    <input id="frontHandError_home_second_txt" type="text" style="width:25%" value="0"></input>
						    <input id="frontHandError_decrement_home_second_btn" type="button" onclick="processUserSelection(this)" value="-"></input>
						  </td>
					  </c:if>
				      <td>
					    <input id="frontHandError_increment_away_first_btn" type="button" onclick="processUserSelection(this)" value="+"></input>
					    <input id="frontHandError_away_first_txt" type="text" style="width:25%" value="0"></input>
					    <input id="frontHandError_decrement_away_first_btn" type="button" onclick="processUserSelection(this)" value="-"></input>
					  </td>
					  <c:if test = "${session_match.matchType == 'doubles'}">
					      <td>
						    <input id="frontHandError_increment_away_second_btn" type="button" onclick="processUserSelection(this)" value="+"></input>
						    <input id="frontHandError_away_second_txt" type="text" style="width:25%" value="0"></input>
						    <input id="frontHandError_decrement_away_second_btn" type="button" onclick="processUserSelection(this)" value="-"></input>
						  </td>
					  </c:if>
				    </tr>				    
				    <tr>
				      <td>
					    <label>Back Hand Error</label>
					  </td>
				      <td>
					    <input id="backHandError_increment_home_first_btn" type="button" onclick="processUserSelection(this)" value="+"></input>
					    <input id="backHandError_home_first_txt" type="text" style="width:25%" value="0"></input>
					    <input id="backHandError_decrement_home_first_btn" type="button" onclick="processUserSelection(this)" value="-"></input>
					  </td>
					  <c:if test = "${session_match.matchType == 'doubles'}">
					      <td>
						    <input id="backHandError_increment_home_second_btn" type="button" onclick="processUserSelection(this)" value="+"></input>
						    <input id="backHandError_home_second_txt" type="text" style="width:25%" value="0"></input>
						    <input id="backHandError_decrement_home_second_btn" type="button" onclick="processUserSelection(this)" value="-"></input>
						  </td>
					  </c:if>
				      <td>
					    <input id="backHandError_increment_away_first_btn" type="button" onclick="processUserSelection(this)" value="+"></input>
					    <input id="backHandError_away_first_txt" type="text" style="width:25%" value="0"></input>
					    <input id="backHandError_decrement_away_first_btn" type="button" onclick="processUserSelection(this)" value="-"></input>
					  </td>
					  <c:if test = "${session_match.matchType == 'doubles'}">
					      <td>
						    <input id="backHandError_increment_away_second_btn" type="button" onclick="processUserSelection(this)" value="+"></input>
						    <input id="backHandError_away_second_txt" type="text" style="width:25%" value="0"></input>
						    <input id="backHandError_decrement_away_second_btn" type="button" onclick="processUserSelection(this)" value="-"></input>
						  </td>
					  </c:if>
				    </tr>				    
				    <tr>
				      <td>
					    <label>Serve Error</label>
					  </td>
				      <td>
					    <input id="serveError_increment_home_first_btn" type="button" onclick="processUserSelection(this)" value="+"></input>
					    <input id="serveError_home_first_txt" type="text" style="width:25%" value="0"></input>
					    <input id="serveError_decrement_home_first_btn" type="button" onclick="processUserSelection(this)" value="-"></input>
					  </td>
					  <c:if test = "${session_match.matchType == 'doubles'}">
					      <td>
						    <input id="serveError_increment_home_second_btn" type="button" onclick="processUserSelection(this)" value="+"></input>
						    <input id="serveError_home_second_txt" type="text" style="width:25%" value="0"></input>
						    <input id="serveError_decrement_home_second_btn" type="button" onclick="processUserSelection(this)" value="-"></input>
						  </td>
					  </c:if>
				      <td>
					    <input id="serveError_increment_away_first_btn" type="button" onclick="processUserSelection(this)" value="+"></input>
					    <input id="serveError_away_first_txt" type="text" style="width:25%" value="0"></input>
					    <input id="serveError_decrement_away_first_btn" type="button" onclick="processUserSelection(this)" value="-"></input>
					  </td>
					  <c:if test = "${session_match.matchType == 'doubles'}">
					      <td>
						    <input id="serveError_increment_away_second_btn" type="button" onclick="processUserSelection(this)" value="+"></input>
						    <input id="serveError_away_second_txt" type="text" style="width:25%" value="0"></input>
						    <input id="serveError_decrement_away_second_btn" type="button" onclick="processUserSelection(this)" value="-"></input>
						  </td>
					  </c:if>
				    </tr>
				    <tr>
				      <td>
					    <label>Break Point Won</label>
					  </td>
				      <td>
					    <input id="breakPointWon_increment_home_first_btn" type="button" onclick="processUserSelection(this)" value="+"></input>
					    <input id="breakPointWon_home_first_txt" type="text" style="width:25%" value="0"></input>
					    <input id="breakPointWon_decrement_home_first_btn" type="button" onclick="processUserSelection(this)" value="-"></input>
					  </td>
					  <c:if test = "${session_match.matchType == 'doubles'}">
					      <td>
						    <input id="breakPointWon_increment_home_second_btn" type="button" onclick="processUserSelection(this)" value="+"></input>
						    <input id="breakPointWon_home_second_txt" type="text" style="width:25%" value="0"></input>
						    <input id="breakPointWon_decrement_home_second_btn" type="button" onclick="processUserSelection(this)" value="-"></input>
						  </td>
					  </c:if>
				      <td>
					    <input id="breakPointWon_increment_away_first_btn" type="button" onclick="processUserSelection(this)" value="+"></input>
					    <input id="breakPointWon_away_first_txt" type="text" style="width:25%" value="0"></input>
					    <input id="breakPointWon_decrement_away_first_btn" type="button" onclick="processUserSelection(this)" value="-"></input>
					  </td>
					  <c:if test = "${session_match.matchType == 'doubles'}">
					      <td>
						    <input id="breakPointWon_increment_away_second_btn" type="button" onclick="processUserSelection(this)" value="+"></input>
						    <input id="breakPointWon_away_second_txt" type="text" style="width:25%" value="0"></input>
						    <input id="breakPointWon_decrement_away_second_btn" type="button" onclick="processUserSelection(this)" value="-"></input>
						  </td>
					  </c:if>
				    </tr>				    
				  </tbody>
				</table> 
			  </div>
            </div>
          </div>
         </div>
       </div>
    </div>
  </div>
</form:form>
</body>
</html>