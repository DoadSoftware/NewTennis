<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Exhibition Logger</title>
    <link rel="stylesheet" href="<c:url value='/webjars/bootstrap/5.3.3/css/bootstrap.min.css'/>"/>
    <link rel="stylesheet" href="<c:url value='/resources/css/index.css'/>">
    <script src="<c:url value='/resources/javascript/exhibition_index.js'/>" defer></script>
</head>
<body>
<div class="container-fluid py-3">
    <div class="card shadow-sm">
        <div class="card-header">
            <h3 class="mb-1"><c:out value="${exhibitionMatch.matchTitle}"/></h3>
            <h6 class="mb-0 text-muted"><c:out value="${exhibitionMatch.competitionName}"/></h6>
        </div>
        <div class="card-body">
            <div class="d-flex flex-wrap gap-2 mb-3">
                <a href="<c:url value='/exhibition/ready'/>" class="btn btn-outline-secondary btn-sm">← READY / HOME</a>
                <form action="<c:url value='/exhibition/setup'/>" method="GET">
                    <input type="hidden" name="matchId" value="<c:out value='${exhibitionMatch.matchId}'/>"/>
                    <button type="submit" class="btn btn-outline-primary btn-sm" <c:if test="${exhibitionMatch.matchStatus eq 'COMPLETED'}">disabled title="Completed match editing is restricted."</c:if>>SETUP / EDIT</button>
                </form>
            </div>
            <div class="logger-top d-flex flex-wrap justify-content-between align-items-center gap-2 mb-2">
                <div>
                    <span class="badge bg-secondary"><c:out value="${selectedSet.setStatus}"/></span>
                    <c:choose>
                        <c:when test="${selectedSet.setType eq 'SHOOTOUT'}"><span class="badge bg-warning text-dark">SHOOTOUT</span></c:when>
                        <c:otherwise><span class="badge bg-light text-dark border"><c:out value="${selectedSet.matchStyle}"/></span></c:otherwise>
                    </c:choose>
                </div>
                <div class="fw-semibold">Set <c:out value="${selectedSet.setNumber}"/> of <c:out value="${totalSets}"/></div>
            </div>
            <nav class="set-tabs d-flex flex-wrap gap-2 mb-3" aria-label="Navigate sets">
                <c:forEach items="${exhibitionMatch.sets}" var="setItem" varStatus="setTab">
                    <form action="<c:url value='/exhibition/logger/set'/>" method="POST">
                        <input type="hidden" name="matchId" value="<c:out value='${exhibitionMatch.matchId}'/>"/>
                        <input type="hidden" name="setIndex" value="${setTab.index}"/>
                        <button type="submit" class="btn btn-sm ${setTab.index == selectedSetIndex ? 'btn-primary' : 'btn-outline-primary'}"><c:out value="${setItem.setName}"/><c:if test="${setItem.setStatus eq 'COMPLETED'}"> ✓</c:if></button>
                    </form>
                </c:forEach>
            </nav>
            <div class="row text-center">
                <div class="col-md-6 border-end mb-3">
                    <h4 class="group-heading"><c:out value="${exhibitionMatch.group1Name}"/></h4>
                    <div class="my-3">
                        <c:forEach items="${selectedSet.group1PlayerIds}" var="playerId" varStatus="playerStatus">
                            <div class="card mb-2 p-2 text-start">
								<div class="mb-2">
								    <span class="badge bg-light text-dark border">Player ${playerStatus.index + 1}: <strong><c:out value="${group1PlayerNames[playerStatus.index]}"/></strong></span>
								    <c:if test="${selectedSet.group1ServingPlayerId != null and selectedSet.group1ServingPlayerId eq playerId}">
								        <span class="badge bg-success ms-2">SERVING</span>
								    </c:if>
								</div>
								<c:if test="${selectedSet.setStatus eq 'IN_PROGRESS'}">
								    <form action="<c:url value='/exhibition/logger/server'/>" method="POST" class="mb-2">
								        <input type="hidden" name="matchId" value="<c:out value='${exhibitionMatch.matchId}'/>"/>
								        <input type="hidden" name="setIndex" value="${selectedSetIndex}"/>
								        <input type="hidden" name="groupNumber" value="1"/>
								        <input type="hidden" name="playerId" value="${playerId}"/>
								        <button type="submit" class="btn btn-sm btn-outline-success" <c:if test="${selectedSet.group1ServingPlayerId != null and selectedSet.group1ServingPlayerId eq playerId}">disabled</c:if>>
								            <c:choose>
								                <c:when test="${selectedSet.group1ServingPlayerId != null and selectedSet.group1ServingPlayerId eq playerId}">Current Server</c:when>
								                <c:otherwise>Set Serving</c:otherwise>
								            </c:choose>
								        </button>
								    </form>
								</c:if>
                                <form action="<c:url value='/exhibition/logger/player/replace'/>" method="POST" class="row g-2 align-items-center">
                                    <input type="hidden" name="matchId" value="<c:out value='${exhibitionMatch.matchId}'/>"/>
                                    <input type="hidden" name="setIndex" value="${selectedSetIndex}"/>
                                    <input type="hidden" name="groupNumber" value="1"/>
                                    <input type="hidden" name="playerPosition" value="${playerStatus.index}"/>
                                    <div class="col"><select name="newPlayerId" class="form-select" required><option value="">Select replacement player</option><c:forEach items="${players}" var="replacementPlayer"><option value="${replacementPlayer.playerId}"><c:out value="${replacementPlayer.full_name}"/> (ID: <c:out value="${replacementPlayer.playerId}"/>)</option></c:forEach></select></div>
                                    <div class="col-auto"><button type="submit" class="btn btn-outline-warning">Replace</button></div>
                                </form>
                            </div>
                        </c:forEach>
                    </div>
<div class="score-display d-flex justify-content-center align-items-center gap-2 my-2" data-score-group="1">
                        <div class="logger-score score-value"><c:out value="${selectedSet.group1Score}"/></div>
                        <c:if test="${selectedSet.setStatus eq 'IN_PROGRESS'}"><button type="button" class="btn btn-sm btn-outline-secondary score-edit-btn" aria-label="Edit Group 1 score" title="Edit score"><span aria-hidden="true">✎</span></button></c:if>
                    </div>
                    <c:if test="${selectedSet.setStatus eq 'IN_PROGRESS'}">
                        <form action="<c:url value='/exhibition/logger/score/overwrite'/>" method="POST" class="score-edit-form d-none d-flex justify-content-center align-items-center gap-2 my-2" data-score-group="1">
                            <input type="hidden" name="matchId" value="<c:out value='${exhibitionMatch.matchId}'/>"/>
                            <input type="hidden" name="setIndex" value="${selectedSetIndex}"/>
                            <input type="hidden" name="groupNumber" value="1"/>
                            <input type="number" name="score" class="form-control score-edit-input" min="0" max="2147483647" step="1" value="${selectedSet.group1Score}" required aria-label="New score for Group 1"/>
                            <button type="submit" class="btn btn-sm btn-success" title="Save score" aria-label="Save score">✓</button>
                            <button type="button" class="btn btn-sm btn-outline-danger score-cancel-btn" title="Cancel editing" aria-label="Cancel editing">✕</button>
                        </form>
                    </c:if>
                    <c:if test="${selectedSet.setStatus eq 'IN_PROGRESS'}">

                        <div class="d-flex justify-content-center gap-2 my-3">
                            <form action="<c:url value='/exhibition/logger/score'/>" method="POST">
                                <input type="hidden" name="matchId" value="<c:out value='${exhibitionMatch.matchId}'/>"/>
                                <input type="hidden" name="setIndex" value="${selectedSetIndex}"/>
                                <input type="hidden" name="groupNumber" value="1"/>
                                <input type="hidden" name="action" value="DECREMENT"/>
                                <button type="submit" class="btn btn-outline-danger">−1</button>
                            </form>
                            <form action="<c:url value='/exhibition/logger/score'/>" method="POST">
                                <input type="hidden" name="matchId" value="<c:out value='${exhibitionMatch.matchId}'/>"/>
                                <input type="hidden" name="setIndex" value="${selectedSetIndex}"/>
                                <input type="hidden" name="groupNumber" value="1"/>
                                <input type="hidden" name="action" value="INCREMENT"/>
                                <button type="submit" class="btn btn-success">+1</button>
                            </form>
                        </div>
                    </c:if>
                </div>
                <div class="col-md-6 mb-3">
                    <h4 class="group-heading"><c:out value="${exhibitionMatch.group2Name}"/></h4>
                    <div class="my-3">
                        <c:forEach items="${selectedSet.group2PlayerIds}" var="playerId" varStatus="playerStatus">
                            <div class="card mb-2 p-2 text-start">
								<div class="mb-2">
								    <span class="badge bg-light text-dark border">Player ${playerStatus.index + 1}: <strong><c:out value="${group2PlayerNames[playerStatus.index]}"/></strong></span>
								    <c:if test="${selectedSet.group2ServingPlayerId != null and selectedSet.group2ServingPlayerId eq playerId}">
								        <span class="badge bg-success ms-2">SERVING</span>
								    </c:if>
								</div>
								<c:if test="${selectedSet.setStatus eq 'IN_PROGRESS'}">
								    <form action="<c:url value='/exhibition/logger/server'/>" method="POST" class="mb-2">
								        <input type="hidden" name="matchId" value="<c:out value='${exhibitionMatch.matchId}'/>"/>
								        <input type="hidden" name="setIndex" value="${selectedSetIndex}"/>
								        <input type="hidden" name="groupNumber" value="2"/>
								        <input type="hidden" name="playerId" value="${playerId}"/>
								        <button type="submit" class="btn btn-sm btn-outline-success" <c:if test="${selectedSet.group2ServingPlayerId != null and selectedSet.group2ServingPlayerId eq playerId}">disabled</c:if>>
								            <c:choose>
								                <c:when test="${selectedSet.group2ServingPlayerId != null and selectedSet.group2ServingPlayerId eq playerId}">Current Server</c:when>
								                <c:otherwise>Set Serving</c:otherwise>
								            </c:choose>
								        </button>
								    </form>
								</c:if>
                                <form action="<c:url value='/exhibition/logger/player/replace'/>" method="POST" class="row g-2 align-items-center">
                                    <input type="hidden" name="matchId" value="<c:out value='${exhibitionMatch.matchId}'/>"/>
                                    <input type="hidden" name="setIndex" value="${selectedSetIndex}"/>
                                    <input type="hidden" name="groupNumber" value="2"/>
                                    <input type="hidden" name="playerPosition" value="${playerStatus.index}"/>
                                    <div class="col"><select name="newPlayerId" class="form-select" required><option value="">Select replacement player</option><c:forEach items="${players}" var="replacementPlayer"><option value="${replacementPlayer.playerId}"><c:out value="${replacementPlayer.full_name}"/> (ID: <c:out value="${replacementPlayer.playerId}"/>)</option></c:forEach></select></div>
                                    <div class="col-auto"><button type="submit" class="btn btn-outline-warning">Replace</button></div>
                                </form>
                            </div>
                        </c:forEach>
                    </div>
<div class="score-display d-flex justify-content-center align-items-center gap-2 my-2" data-score-group="2">
                        <div class="logger-score score-value"><c:out value="${selectedSet.group2Score}"/></div>
                        <c:if test="${selectedSet.setStatus eq 'IN_PROGRESS'}"><button type="button" class="btn btn-sm btn-outline-secondary score-edit-btn" aria-label="Edit Group 2 score" title="Edit score"><span aria-hidden="true">✎</span></button></c:if>
                    </div>
                    <c:if test="${selectedSet.setStatus eq 'IN_PROGRESS'}">
                        <form action="<c:url value='/exhibition/logger/score/overwrite'/>" method="POST" class="score-edit-form d-none d-flex justify-content-center align-items-center gap-2 my-2" data-score-group="2">
                            <input type="hidden" name="matchId" value="<c:out value='${exhibitionMatch.matchId}'/>"/>
                            <input type="hidden" name="setIndex" value="${selectedSetIndex}"/>
                            <input type="hidden" name="groupNumber" value="2"/>
                            <input type="number" name="score" class="form-control score-edit-input" min="0" max="2147483647" step="1" value="${selectedSet.group2Score}" required aria-label="New score for Group 2"/>
                            <button type="submit" class="btn btn-sm btn-success" title="Save score" aria-label="Save score">✓</button>
                            <button type="button" class="btn btn-sm btn-outline-danger score-cancel-btn" title="Cancel editing" aria-label="Cancel editing">✕</button>
                        </form>
                    </c:if>
                    <c:if test="${selectedSet.setStatus eq 'IN_PROGRESS'}">

                        <div class="d-flex justify-content-center gap-2 my-3">
                            <form action="<c:url value='/exhibition/logger/score'/>" method="POST">
                                <input type="hidden" name="matchId" value="<c:out value='${exhibitionMatch.matchId}'/>"/>
                                <input type="hidden" name="setIndex" value="${selectedSetIndex}"/>
                                <input type="hidden" name="groupNumber" value="2"/>
                                <input type="hidden" name="action" value="DECREMENT"/>
                                <button type="submit" class="btn btn-outline-danger">−1</button>
                            </form>
                            <form action="<c:url value='/exhibition/logger/score'/>" method="POST">
                                <input type="hidden" name="matchId" value="<c:out value='${exhibitionMatch.matchId}'/>"/>
                                <input type="hidden" name="setIndex" value="${selectedSetIndex}"/>
                                <input type="hidden" name="groupNumber" value="2"/>
                                <input type="hidden" name="action" value="INCREMENT"/>
                                <button type="submit" class="btn btn-success">+1</button>
                            </form>
                        </div>
                    </c:if>
                </div>
            </div>
            <hr>
            <div class="text-center my-3">
                <c:choose>
                    <c:when test="${selectedSet.setStatus eq 'NOT_STARTED'}">
                        <p class="text-muted">This set has not started yet.</p>
                        <form action="<c:url value='/exhibition/logger/set/start'/>" method="POST">
                            <input type="hidden" name="matchId" value="<c:out value='${exhibitionMatch.matchId}'/>"/>
                            <input type="hidden" name="setIndex" value="${selectedSetIndex}"/>
                            <button type="submit" class="btn btn-success btn-lg">Start Set</button>
                        </form>
                    </c:when>
                    <c:when test="${selectedSet.setStatus eq 'IN_PROGRESS'}">
                        <p class="text-muted">Set is in progress. Select the winner when this set is finished.</p>
                        <form action="<c:url value='/exhibition/logger/set/complete'/>" method="POST" class="row g-2 justify-content-center align-items-center">
                            <input type="hidden" name="matchId" value="<c:out value='${exhibitionMatch.matchId}'/>"/>
                            <input type="hidden" name="setIndex" value="${selectedSetIndex}"/>
                            <div class="col-auto">
                                <select name="winnerGroup" class="form-select" required>
                                    <option value="">Select set winner</option>
                                    <option value="<c:out value='${exhibitionMatch.group1Name}'/>"><c:out value="${exhibitionMatch.group1Name}"/></option>
                                    <option value="<c:out value='${exhibitionMatch.group2Name}'/>"><c:out value="${exhibitionMatch.group2Name}"/></option>
                                </select>
                            </div>
                            <div class="col-auto">
                                <button type="submit" class="btn btn-primary">Complete Set</button>
                            </div>
                        </form>
                    </c:when>
                    <c:when test="${selectedSet.setStatus eq 'COMPLETED'}">
                        <div class="alert alert-success d-inline-block">
                            <strong>Set completed</strong><br>
                            Winner: <c:out value="${selectedSet.setWinnerGroup}"/><br>
                            Final score: <c:out value="${selectedSet.group1Score}"/> - <c:out value="${selectedSet.group2Score}"/>
                        </div>
                        <form action="<c:url value='/exhibition/logger/set/reopen'/>" method="POST">
                            <input type="hidden" name="matchId" value="<c:out value='${exhibitionMatch.matchId}'/>"/>
                            <input type="hidden" name="setIndex" value="${selectedSetIndex}"/>
                            <button type="submit" class="btn btn-warning">Reopen Set</button>
                        </form>
                    </c:when>
                    <c:otherwise>
                        <div class="alert alert-warning">Unrecognized set status: <c:out value="${selectedSet.setStatus}"/></div>
                    </c:otherwise>
                </c:choose>
            </div>
            <div class="text-center my-3">
                <button type="button" id="finishMatchBtn" class="btn btn-danger" style="${exhibitionMatch.matchStatus eq 'COMPLETED' ? 'display:none;' : ''}">FINISH MATCH</button>
            </div>
            <div id="overallMatchResult" class="card my-3" style="${exhibitionMatch.matchStatus eq 'COMPLETED' ? '' : 'display:none;'}">
			    <div class="card-header">
			        <h5 class="mb-0">Overall Match Result</h5>
			    </div>
			    <div class="card-body text-center">
			        <div class="mb-3">
			            <strong>Match Status:</strong>
			            <span class="badge ${exhibitionMatch.matchStatus eq 'COMPLETED' ? 'bg-success' : 'bg-secondary'}"><c:out value="${exhibitionMatch.matchStatus}"/></span>
			        </div>
			        <c:choose>
			            <c:when test="${not empty exhibitionMatch.overallWinnerGroup}">
			                <div class="alert alert-success">
			                    <h5 class="mb-1">Overall Match Winner</h5>
			                    <h3 class="mb-0"><c:out value="${exhibitionMatch.overallWinnerGroup}"/></h3>
			                </div>
			                <p class="text-muted">The overall winner was manually declared by the logger.</p>
			            </c:when>
			            <c:otherwise>
			                <div class="alert alert-light border">
			                    <strong>Overall match winner has not been declared.</strong>
			                </div>
			            </c:otherwise>
			        </c:choose>
			        <form action="<c:url value='/exhibition/logger/match/complete'/>" method="POST" class="row g-2 justify-content-center align-items-center">
			            <input type="hidden" name="matchId" value="<c:out value='${exhibitionMatch.matchId}'/>"/>
			            <input type="hidden" name="setIndex" value="${selectedSetIndex}"/>
			            <div class="col-auto">
			                <select name="winnerGroup" class="form-select" required>
			                    <option value="">Select overall match winner</option>
			                    <option value="<c:out value='${exhibitionMatch.group1Name}'/>"><c:out value="${exhibitionMatch.group1Name}"/></option>
			                    <option value="<c:out value='${exhibitionMatch.group2Name}'/>"><c:out value="${exhibitionMatch.group2Name}"/></option>
			                </select>
			            </div>
			            <div class="col-auto">
			                <button type="submit" class="btn btn-success"><c:choose><c:when test="${not empty exhibitionMatch.overallWinnerGroup}">Update Match Winner</c:when><c:otherwise>Declare Match Winner</c:otherwise></c:choose></button>
			            </div>
			        </form>
			        <hr>
			        <div><strong>Current Set Status:</strong> <c:out value="${selectedSet.setStatus}"/></div>
			    </div>
			</div>
        </div>
    </div>
</div>
</body>
</html>