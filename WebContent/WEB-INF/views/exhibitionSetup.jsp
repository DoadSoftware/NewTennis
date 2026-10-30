<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Exhibition Match Setup</title>
    <link rel="stylesheet" href="<c:url value='/webjars/bootstrap/5.3.3/css/bootstrap.min.css'/>"/>
    <style>
        .setup-section { margin-bottom: 20px; }
        .set-card { border: 1px solid #ddd; padding: 15px; margin-bottom: 15px; border-radius: 6px; }
        .player-row { margin-top: 10px; }
    </style>
</head>
<body>
<div class="container py-4">
    <div class="card">
        <div class="card-header"><h3>Exhibition Match Setup</h3></div>
        <div class="card-body">
            <c:if test="${not empty error_message}"><div class="alert alert-danger"><c:out value="${error_message}"/></div></c:if>
            <form id="exhibitionSetupForm" method="post" action="<c:url value='${editMode ? "/exhibition/update" : "/exhibition/start"}'/>">
                <c:if test="${editMode}"><input type="hidden" name="matchId" value="${fn:escapeXml(exhibitionMatch.matchId)}"></c:if>
                <div class="setup-section">
                    <h5>Match Details</h5>
                    <div class="form-group mb-3"><label for="matchFileName">JSON File Name</label><input type="text" id="matchFileName" name="matchFileName" class="form-control" value="${fn:escapeXml(editMode ? exhibitionMatch.matchFileName : '')}" placeholder="e.g. Alexa_vs_Ulla_Final" maxlength="100"><small class="form-text text-muted">Optional filename without extension. Leave blank to use the match title.</small></div>
                    <div class="row mb-3"><label class="col-sm-3 col-form-label">Competition Name</label><div class="col-sm-9"><input type="text" name="competitionName" class="form-control" value="${fn:escapeXml(editMode ? exhibitionMatch.competitionName : '')}" required></div></div>
                    <div class="row mb-3"><label class="col-sm-3 col-form-label">Match Title</label><div class="col-sm-9"><input type="text" name="matchTitle" class="form-control" value="${fn:escapeXml(editMode ? exhibitionMatch.matchTitle : '')}" required></div></div>
                    <div class="row mb-3"><label class="col-sm-3 col-form-label">First Group Name</label><div class="col-sm-9"><input type="text" name="group1Name" class="form-control" value="${fn:escapeXml(editMode ? exhibitionMatch.group1Name : '')}" placeholder="e.g. Alexa" required></div></div>
                    <div class="row mb-3"><label class="col-sm-3 col-form-label">Second Group Name</label><div class="col-sm-9"><input type="text" name="group2Name" class="form-control" value="${fn:escapeXml(editMode ? exhibitionMatch.group2Name : '')}" placeholder="e.g. Ulla" required></div></div>
                </div>
                <hr>
                <div class="setup-section">
                    <div class="d-flex justify-content-between align-items-center mb-3"><h5>Sets</h5><button type="button" class="btn btn-primary btn-sm" id="addSetBtn">Add Set</button></div>
                    <div id="setsContainer"></div>
                    <div id="savedSetsData" hidden><c:if test="${editMode}"><c:forEach items="${exhibitionMatch.sets}" var="savedSet"><div class="saved-set" data-set-name="${fn:escapeXml(savedSet.setName)}" data-set-type="${fn:escapeXml(savedSet.setType)}" data-match-style="${fn:escapeXml(savedSet.matchStyle)}" data-group1-player1="${fn:escapeXml(savedSet.group1PlayerIds[0])}" data-group1-player2="${fn:escapeXml(savedSet.group1PlayerIds.size() gt 1 ? savedSet.group1PlayerIds[1] : '')}" data-group2-player1="${fn:escapeXml(savedSet.group2PlayerIds[0])}" data-group2-player2="${fn:escapeXml(savedSet.group2PlayerIds.size() gt 1 ? savedSet.group2PlayerIds[1] : '')}"></div></c:forEach></c:if></div>
                </div>
                <div class="d-flex justify-content-between">
                    <a href="<c:url value='/exhibition/ready'/>" class="btn btn-secondary">Back to Ready</a>
                    <button type="submit" class="btn btn-success">${editMode ? "Save Changes" : "Continue"}</button>
                </div>
            </form>
        </div>
    </div>
</div>
<template id="setTemplate">
    <div class="set-card">
        <div class="d-flex justify-content-between align-items-center"><h6 class="set-heading">Set</h6><button type="button" class="btn btn-outline-danger btn-sm remove-set-btn">Remove Set</button></div>
        <div class="row mb-3"><label class="col-sm-3 col-form-label">Set Name</label><div class="col-sm-9"><input type="text" class="form-control set-name" required></div></div>
        <div class="row mb-3"><label class="col-sm-3 col-form-label">Set Type</label><div class="col-sm-9"><select class="form-select set-type"><option value="REGULAR">Regular</option><option value="SHOOTOUT">SHOOTOUT</option></select></div></div>
        <div class="row mb-3"><label class="col-sm-3 col-form-label">Match Style</label><div class="col-sm-9"><select class="form-select match-style"><option value="SINGLES">Singles</option><option value="DOUBLES">Doubles</option></select></div></div>
        <div class="row player-row">
            <div class="col-md-6"><h6 class="group1-heading">Group 1</h6>
                <select class="form-select player1" required><option value="">Select Player</option><c:forEach items="${players}" var="player"><option value="${fn:escapeXml(player.playerId)}"><c:out value="${player.full_name}"/></option></c:forEach></select>
                <select class="form-select player1b mt-2" style="display:none"><option value="">Select Second Player</option><c:forEach items="${players}" var="player"><option value="${fn:escapeXml(player.playerId)}"><c:out value="${player.full_name}"/></option></c:forEach></select>
            </div>
            <div class="col-md-6"><h6 class="group2-heading">Group 2</h6>
                <select class="form-select player2" required><option value="">Select Player</option><c:forEach items="${players}" var="player"><option value="${fn:escapeXml(player.playerId)}"><c:out value="${player.full_name}"/></option></c:forEach></select>
                <select class="form-select player2b mt-2" style="display:none"><option value="">Select Second Player</option><c:forEach items="${players}" var="player"><option value="${fn:escapeXml(player.playerId)}"><c:out value="${player.full_name}"/></option></c:forEach></select>
            </div>
        </div>
    </div>
</template>
<script src="<c:url value='/resources/javascript/exhibition_index.js'/>" defer></script>

</body>
</html>