<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Exhibition Match Setup</title>
    <link rel="stylesheet" href="<c:url value='/webjars/bootstrap/5.3.3/css/bootstrap.min.css' />"/>
    <style>
        .setup-section { margin-bottom: 20px; }
        .set-card { border: 1px solid #ddd; padding: 15px; margin-bottom: 15px; border-radius: 6px; }
        .player-row { margin-top: 10px; }
    </style>
</head>
<body>
<div class="container py-4">
    <div class="card">
        <div class="card-header">
            <h3>Exhibition Match Setup</h3>
        </div>
        <div class="card-body">
			<c:if test="${not empty error_message}">
			    <div class="alert alert-danger"><c:out value="${error_message}"/></div>
			</c:if>        
            <form id="exhibitionSetupForm" method="post" action="<c:url value='/exhibition/start'/>">
                <div class="setup-section">
                    <h5>Match Details</h5>
					<div class="form-group">
					    <label for="matchFileName">JSON File Name</label>
					    <input type="text" id="matchFileName" name="matchFileName" class="form-control" placeholder="e.g. Alexa_vs_Ulla_Final" required maxlength="100">
					    <small class="form-text text-muted">Enter the filename without .json</small>
					</div>
                    <div class="row mb-3">
                        <label class="col-sm-3 col-form-label">Competition Name</label>
                        <div class="col-sm-9">
                            <input type="text" name="competitionName" class="form-control" required>
                        </div>
                    </div>
                    <div class="row mb-3">
                        <label class="col-sm-3 col-form-label">Match Title</label>
                        <div class="col-sm-9">
                            <input type="text" name="matchTitle" class="form-control" required>
                        </div>
                    </div>
                    <div class="row mb-3">
                        <label class="col-sm-3 col-form-label">First Group Name</label>
                        <div class="col-sm-9">
                            <input type="text" name="group1Name" class="form-control" placeholder="e.g. Alexa" required>
                        </div>
                    </div>
                    <div class="row mb-3">
                        <label class="col-sm-3 col-form-label">Second Group Name</label>
                        <div class="col-sm-9">
                            <input type="text" name="group2Name" class="form-control" placeholder="e.g. Ulla" required>
                        </div>
                    </div>
                </div>
                <hr>
                <div class="setup-section">
                    <div class="d-flex justify-content-between align-items-center mb-3">
                        <h5>Sets</h5>
                        <button type="button" class="btn btn-primary btn-sm" id="addSetBtn">Add Set</button>
                    </div>
                    <div id="setsContainer"></div>
                </div>
                <div class="d-flex justify-content-between">
                    <button type="button" class="btn btn-secondary" onclick="history.back()">Cancel</button>
                    <button type="submit" class="btn btn-success">Continue</button>
                </div>
            </form>
        </div>
    </div>
</div>

<template id="setTemplate">
    <div class="set-card">
        <div class="d-flex justify-content-between align-items-center">
            <h6 class="set-heading">Set</h6>
            <button type="button" class="btn btn-outline-danger btn-sm remove-set-btn">Remove Set</button>
        </div>
        <div class="row mb-3">
            <label class="col-sm-3 col-form-label">Set Name</label>
            <div class="col-sm-9">
                <input type="text" class="form-control set-name" required>
            </div>
        </div>
        <div class="row mb-3">
            <label class="col-sm-3 col-form-label">Set Type</label>
            <div class="col-sm-9">
                <select class="form-select set-type">
                    <option value="REGULAR">Regular</option>
                    <option value="SHOOTOUT">SHOOTOUT</option>
                </select>
            </div>
        </div>
        <div class="row mb-3">
            <label class="col-sm-3 col-form-label">Match Style</label>
            <div class="col-sm-9">
                <select class="form-select match-style">
                    <option value="SINGLES">Singles</option>
                    <option value="DOUBLES">Doubles</option>
                </select>
            </div>
        </div>
        <div class="row player-row">
            <div class="col-md-6">
                <h6 class="group1-heading">Group 1</h6>
                <select class="form-select player1" required>
                    <option value="">Select Player</option>
                    <c:forEach items="${players}" var="player">
                        <option value="${player.playerId}"><c:out value="${player.full_name}"/></option>
                    </c:forEach>
                </select>
                <select class="form-select player1b mt-2" style="display:none">
                    <option value="">Select Second Player</option>
                    <c:forEach items="${players}" var="player">
                        <option value="${player.playerId}"><c:out value="${player.full_name}"/></option>
                    </c:forEach>
                </select>
            </div>
            <div class="col-md-6">
                <h6 class="group2-heading">Group 2</h6>
                <select class="form-select player2" required>
                    <option value="">Select Player</option>
                    <c:forEach items="${players}" var="player">
                        <option value="${player.playerId}"><c:out value="${player.full_name}"/></option>
                    </c:forEach>
                </select>
                <select class="form-select player2b mt-2" style="display:none">
                    <option value="">Select Second Player</option>
                    <c:forEach items="${players}" var="player">
                        <option value="${player.playerId}"><c:out value="${player.full_name}"/></option>
                    </c:forEach>
                </select>
            </div>
        </div>
    </div>
</template>
<script src="<c:url value='/WEB-INF/resources/javascript/exhibition_index.js'/>"></script>
<script>initialiseExhibitionSetup();</script>
</body>
</html>
