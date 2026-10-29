
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Exhibition Match Ready</title>
    <link rel="stylesheet" href="<c:url value='/webjars/bootstrap/5.3.3/css/bootstrap.min.css'/>"/>
</head>
<body>
<div class="container py-4">
    <div class="card">
        <div class="card-header"><h3>Exhibition Match Ready</h3></div>
        <div class="card-body">
            <p><strong>Competition:</strong> <c:out value="${exhibitionMatch.competitionName}"/></p>
            <p><strong>Match:</strong> <c:out value="${exhibitionMatch.matchTitle}"/></p>
            <p><strong>Group 1:</strong> <c:out value="${exhibitionMatch.group1Name}"/></p>
            <p><strong>Group 2:</strong> <c:out value="${exhibitionMatch.group2Name}"/></p>
            <p><strong>JSON:</strong> <c:out value="${exhibitionMatch.matchFileName}"/>.exhibition.json</p>
            <p><strong>Status:</strong> <c:out value="${exhibitionMatch.matchStatus}"/></p>
            <hr>
            <h5>Configured Sets</h5>
            <c:forEach items="${exhibitionMatch.sets}" var="set">
                <div class="border rounded p-3 mb-3">
                    <h6>Set ${set.setNumber}: <c:out value="${set.setName}"/></h6>
                    <p>Type: <c:out value="${set.setType}"/></p>
                    <p>Format: <c:out value="${set.matchStyle}"/></p>
                    <p>Group 1 Player IDs: <c:forEach items="${set.group1PlayerIds}" var="playerId" varStatus="s"><c:if test="${!s.first}">, </c:if><c:out value="${playerId}"/></c:forEach></p>
                    <p>Group 2 Player IDs: <c:forEach items="${set.group2PlayerIds}" var="playerId" varStatus="s"><c:if test="${!s.first}">, </c:if><c:out value="${playerId}"/></c:forEach></p>
                    <p>Status: <c:out value="${set.setStatus}"/></p>
                </div>
            </c:forEach>
            <form action="<c:url value='/exhibition/logger'/>" method="POST">
                <input type="hidden" name="matchId" value="<c:out value='${exhibitionMatch.matchId}'/>"/>
                <button type="submit" class="btn btn-primary">Open Exhibition Logger</button>
            </form>
            <button type="button" class="btn btn-secondary mt-2" onclick="history.back()">Back</button>
        </div>
    </div>
</div>
</body>
</html>
