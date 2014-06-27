<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<!-- importazione delle classi di gestione del database -->
<%@ page import="database.*"%>
<%@ page import="java.sql.*"%>

<jsp:useBean id="user" scope="page" class="database.UtenteEntry">
    <jsp:setProperty name="user" property="*" />
</jsp:useBean>
<html>
<head>
        <title>Modulo di registrazione </title>
        <meta name="author" content="Tarantino - Turturo" />
        <link rel="shortcut icon" href="img/favicon.ico"/>
        <link type="text/css" rel="stylesheet" href="stile1.css" title="primo"/>
        <link type="text/css" rel="alternate stylesheet" href="stile2.css" title="secondo"/>
        <script type="text/javascript" src="validazione.js"></script>
</head>
<body>
<% iPartitaDAO utente = PartitaDAOFactory.getInstance(PartitaDAOFactory.MYSQL); 
   try
   {
       if(utente.existUtente(user))
       {
           out.print("<h1 class=\"errore\">Attenzione! Esiste gi&agrave; un account associato a " + user.getEmail() + "</h1>" );%>
           <jsp:include page="FormRegistrazione.html" />
       <%}
       else
       {
            utente = PartitaDAOFactory.getInstance(PartitaDAOFactory.MYSQL);
            utente.addUtente(user);
            
            utente = PartitaDAOFactory.getInstance(PartitaDAOFactory.MYSQL);
            if(utente.existUtente(user))
                out.print("<h1>Registrazione avvenuta con successo!</h1><br />" + 
                		  "<table>" +
	                		  "<thead>" +
							      "<tr>" +
							        "<th colspan=\"2\"><h3>Riepilogo dati</h3></th>" +
							      "</tr>" +
							  "</thead>" +
				                "<tr>" +
				                  "<td class=\"form\" id=\"tdlNickname\" >Nickname: </td>" +
				                  "<td id=\"nickname\" >" + user.getNickname() + " </td>" +
				                "</tr>" +
				                "<tr>" +
				                  "<td class=\"form\" id=\"tdlPassword\" >Password: </td>" +
	                              "<td id=\"password\" >" + user.getPassword() + " </td>" +
	                          "</tr>" +
	                          "<tr>" +
	                              "<td class=\"form\" id=\"tdlEmail\" >Email: </td>" +
	                              "<td id=\"email\" >" + user.getEmail() + " </td>" +
	                          "</tr>" +
                          "</table>");
       }
   }
   catch(SQLException e)
   {
       out.print("<h1>Siamo spiacenti!" +
               "<br /> Si &egrave; verificato un errore durante la registrazione." +
               "<br /> La preghiamo di riprovare pi&ugrave; tardi!</h1>");
   }
%>
</body>
</html>