<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Exhibition Logger</title>
    <link rel="stylesheet" href="<c:url value='/webjars/bootstrap/5.3.3/css/bootstrap.min.css'/>"/>
</head>
<body>
<div class="container-fluid py-3">
    <div class="card">
        <div class="card-header">
            <h3><c:out value="${exhibitionMatch.matchTitle}"/></h3>
            <h6><c:out value="${exhibitionMatch.competitionName}"/></h6>
        </div>
        <div class="card-body">
            <div class="row text-center mb-3">
                <div class="col-6"><h4><c:out value="${exhibitionMatch.group1Name}"/></h4></div>
                <div class="col-6"><h4><c:out value="${exhibitionMatch.group2Name}"/></h4></div>
            </div>
            <p><strong>Match Status:</strong> <c:out value="${exhibitionMatch.matchStatus}"/></p>
            <hr>
            <div class="d-flex justify-content-between align-items-center mb-3">
                <form action="<c:url value='/exhibition/logger/set'/>" method="POST">
                    <input type="hidden" name="matchId" value="<c:out value='${exhibitionMatch.matchId}'/>"/>
                    <input type="hidden" name="setIndex" value="${selectedSetIndex - 1}"/>
                    <button type="submit" class="btn btn-outline-primary" <c:if test="${selectedSetIndex == 0}">disabled</c:if>>Previous Set</button>
                </form>
                <div class="text-center">
                    <h5>Set ${selectedSet.setNumber} of ${totalSets}</h5>
                    <h4><c:out value="${selectedSet.setName}"/></h4>
                    <span class="badge bg-secondary"><c:out value="${selectedSet.setType}"/> · <c:out value="${selectedSet.matchStyle}"/></span>
                </div>
                <form action="<c:url value='/exhibition/logger/set'/>" method="POST">
                    <input type="hidden" name="matchId" value="<c:out value='${exhibitionMatch.matchId}'/>"/>
                    <input type="hidden" name="setIndex" value="${selectedSetIndex + 1}"/>
                    <button type="submit" class="btn btn-outline-primary" <c:if test="${selectedSetIndex + 1 >= totalSets}">disabled</c:if>>Next Set</button>
                </form>
            </div>
            <hr>
            <div class="row text-center">
                <div class="col-md-6 border-end">
                    <h3><c:out value="${exhibitionMatch.group1Name}"/></h3>
                    <div class="my-3">
                        <c:forEach items="${selectedSet.group1PlayerIds}" var="playerId">
                            <span class="badge bg-light text-dark border me-1">Player ID: <c:out value="${playerId}"/></span>
                        </c:forEach>
                    </div>
                    <h1>0</h1>
                </div>
                <div class="col-md-6">
                    <h3><c:out value="${exhibitionMatch.group2Name}"/></h3>
                    <div class="my-3">
                        <c:forEach items="${selectedSet.group2PlayerIds}" var="playerId">
                            <span class="badge bg-light text-dark border me-1">Player ID: <c:out value="${playerId}"/></span>
                        </c:forEach>
                    </div>
                    <h1>0</h1>
                </div>
            </div>
            <hr>
            <p><strong>Set Status:</strong> <c:out value="${selectedSet.setStatus}"/></p>
            <p class="text-muted">Scoring controls will be implemented in the next stage.</p>
        </div>
    </div>
</div>
</body>
</html>