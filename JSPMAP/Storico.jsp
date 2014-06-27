<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<!-- importazione delle classi di gestione del database e il calendario -->
<%@ page import="database.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.text.*"%>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
	<meta name="author" content="Tarantino - Turturo" />
	<link rel="shortcut icon" href="img/favicon.ico"/>
	<link type="text/css" rel="stylesheet" href="stile1.css" title="primo"/>
    <title>Storico</title>
</head>
<body>
    <% 
    String data = request.getParameter("data");
    String mail;
    
    if(data.equalsIgnoreCase("succ"))
    {
        data = (String)session.getAttribute("richiesta");
        mail = (String)session.getAttribute("email");
        
        SimpleDateFormat formatData = new SimpleDateFormat("yyyy-MM-dd");
        
        Date newDate = formatData.parse(data);
        Calendar calendario = Calendar.getInstance();
        calendario.setTime(newDate);
        calendario.add(Calendar.DAY_OF_MONTH, 1);
        newDate = calendario.getTime();
        
        data = formatData.format(newDate);
        
        session.setAttribute("richiesta", data);
    }
    else if(data.equalsIgnoreCase("prec"))
    {
        data = (String)session.getAttribute("richiesta");
        mail = (String)session.getAttribute("email");
        
        SimpleDateFormat formatData = new SimpleDateFormat("yyyy-MM-dd");
        
        Date newDate = formatData.parse(data);
        Calendar calendario = Calendar.getInstance();
        calendario.setTime(newDate);
        calendario.add(Calendar.DAY_OF_MONTH, -1);
        newDate = calendario.getTime();
        
        data = formatData.format(newDate);
        
        session.setAttribute("richiesta", data);
    }
    else
    {
    	session.setAttribute("richiesta", data);
    	mail = request.getParameter("email");
    	session.setAttribute("email", mail);
    }
        
    
    iPartitaDAO transazione = PartitaDAOFactory.getInstance(PartitaDAOFactory.MYSQL);
    ArrayList<PartitaEntry> tabella = transazione.getTransazioni(data, mail);
    %>
    
    <%!
    static String convAmEu(String data){
    	Date dateUnformatted = null;
    	String dateFormatted = null;
    	try
    	{
    		dateUnformatted = new SimpleDateFormat("yyyy-MM-dd").parse(data);
    		dateFormatted = new SimpleDateFormat("dd/MM/yyyy").format(dateUnformatted);
    	}
    	catch(ParseException e){}

    	return dateFormatted;
    }
    %>
<table id="tabStorico">
  <thead>
      <tr>
        <th class="storico"><a href="Storico.jsp?data=prec"><img class="frecce" src="img/sx.png" alt="Giorno Precedente" title="Giorno Precedente"/></a></th>
        <th class="storico" colspan="2" >Data Storico: <%= convAmEu((String)session.getAttribute("richiesta")) %> </th>
        <th class="storico"><a href="Storico.jsp?data=succ"><img class="frecce" src="img/dx.png" alt="Giorno Successivo" title="Giorno Successivo"/></a></th>
      </tr>
      <tr id="storicoRow">
        <th class="storicoCol"> Ruolo</th>
		<th class="storicoCol"> Punteggio</th>
		<th class="storicoCol"> Vincita</th>
		<th class="storicoCol"> Data</th>
	  </tr>
  </thead>
  <tbody>
  <% 
  Date dataConf = null;
  
  if(tabella.size() > 0)
	  dataConf = tabella.get(0).daiData();
  String color = "white";
  
  for(PartitaEntry tupla : tabella) { 
      if(tupla.daiData().equals(dataConf))
    	  out.print("<tr class=\"" + color + "\">");
      else 
      {
    	  if(color.equals("white"))
    		  color = "color";
    	  else
    		  color = "white";
    	  
    	  out.print("<tr class=\"" + color + "\">");
          dataConf = tupla.daiData();
      } %>
       
        <td><%= tupla.daiRuolo() %></td>
        <td><%= tupla.daiPunteggio() %></td>
        <td><%= tupla.daiVincita() %>  Euro  </td>
        <td><%= new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(tupla.daiData()) %></td>
  </tr>
  <% } // chiudo for %> 
  </tbody>
  <tfoot>
      <tr>
        <th class="storico" colspan="4"><%= mail %> </th>
      </tr>
      <tr></tr>
  </tfoot>
</table>
</body>
</html>