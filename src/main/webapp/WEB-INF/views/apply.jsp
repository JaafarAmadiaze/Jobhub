<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Postuler à l'offre</title>
</head>
<body>
<h1>Postuler à : ${job.title}</h1>
<form method="post" action="/apply">
    <input type="hidden" name="jobId" value="${job.id}" />

    <label for="message">Message au recruteur :</label><br>
    <textarea name="message" rows="6" cols="50" required></textarea><br><br>

    <button type="submit">Envoyer la candidature</button>
</form>
</body>
</html>
