var stringCorretto = "Hai inserito caratteri corretti";

function controlloNick(id) {
	var nickname = document.getElementById("modulo").nickname.value;

	//Effettua il controllo sul campo NICKNAME
	if ((nickname == "") || (nickname == "undefined")) 
		inserisci_immagine(id, false, "Il campo Nickname e' obbligatorio" );
	else
		inserisci_immagine(id, true, stringCorretto );
}


function controlloPsw(id) {
	var password = document.getElementById("modulo").password.value;

	//Effettua il controllo sul campo PASSWORD
	if ((password == "") || (password == "undefined")) 
		inserisci_immagine(id, false, "Il campo Password e' obbligatorio" );
	else if ( password.length < 6) 
		inserisci_immagine(id, false, "Attenzione! La password deve avere tra 6 e 10 caratteri" );
	else
		inserisci_immagine(id, true, stringCorretto );
}


function controlloPsw2(id) {
	var password = document.getElementById("modulo").password.value;
	var confPsw = document.getElementById("modulo").confPsw.value;

	//Effettua il controllo sul campo PASSWORD
	if ((confPsw == "") || (confPsw == "undefined")) 
		inserisci_immagine(id, false, "Il campo Conferma password e' obbligatorio" );
	else if (password != confPsw) //Verifica l'uguaglianza tra i campi PASSWORD e CONFERMA PASSWORD
		inserisci_immagine(id, false, "La password confermata e' diversa da quella scelta" );
	else
		inserisci_immagine(id, true, stringCorretto );
}


//Espressione regolare dell'email
var email_reg_exp = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-]{2,})+\.)+([a-zA-Z0-9]{2,})+$/;

function controlloEmail(id){
	var email = document.getElementById("modulo").email.value;

	if (!email_reg_exp.test(email) || (email == "") || (email == "undefined")) 
		inserisci_immagine(id, false, "Inserire un indirizzo email valido");
	else
		inserisci_immagine(id, true, stringCorretto );
}


function controlloEmail2(id){
	var email = document.getElementById("modulo").email.value;
	var confEmail = document.getElementById("modulo").confEmail.value;

	if (!email_reg_exp.test(confEmail) || (confEmail == "") || (email == "undefined")) 
		inserisci_immagine(id, false, "Inserire un indirizzo email valido");
	else if (email != confEmail) 
		inserisci_immagine(id, false,"L'indirizzo email confermato e' diverso da quello scelto, controllare");
	else
		inserisci_immagine(id, true, stringCorretto );
}


function inserisci_immagine(id, esito, title){
	var css1 = document.styleSheets[0];
	var css2 = document.styleSheets[1];
	
	if(document.getElementById && document.createElement)
	{
		immagine = document.getElementById(id);

		if(esito)
		{
			/*
			 * Reimpostiamo l'attributo src dell'immagine,
			 * l'attributo alt e l'attributo title
			 */
			immagine.setAttribute("src","img/ok.gif");
			immagine.setAttribute("alt","Perfetto");
			immagine.setAttribute("title", title);
			document.getElementById("head").innerHTML = " &nbsp; ";
			css1.disabled=false;
			css2.disabled=true;
		}
		else
		{
			/*
			 * Reimpostiamo l'attributo src dell'immagine,
			 * l'attributo alt e l'attributo title
			 */
			immagine.setAttribute("src","img/errore.gif");
			immagine.setAttribute("alt","Errore");
			immagine.setAttribute("title", title);
			css1.disabled=true;
			css2.disabled=false;
			document.getElementById("head").firstChild.nodeValue = title;
		}
	}
	else if(!esito)
		alert(title);
}


function lingua(nome){
	var indice = document.getElementById(nome).selectedIndex;
	var linguaSel = document.getElementById(nome).options[indice].id

	var linguaSelezionata = linguaSel;

	switch(linguaSel){
	
	// Lingua Italiana
	case "it":
		document.getElementById("legOne").firstChild.nodeValue = "Modulo di registrazione";
		document.getElementById("lblLanguage").firstChild.nodeValue = "Lingua ";
		document.getElementById("tdlNickname").firstChild.nodeValue = "Nickname: ";
		document.getElementById("tdlPassword").firstChild.nodeValue = "Password: ";
		document.getElementById("td2Password").firstChild.nodeValue = "Conferma Password: ";
		document.getElementById("tdlEmail").firstChild.nodeValue = "Email: ";
		document.getElementById("td2Email").firstChild.nodeValue = "Conferma Email: ";
		document.getElementById("send").value = "Invio";
		document.getElementById("reset").value = "Cancella";

		document.getElementById("language").title = "Seleziona la lingua ";
		document.getElementById("nickname").title = "Inserisci il tuo nickname";
		document.getElementById("password").title = "Inserisci la tua password";
		document.getElementById("password2").title = "Riscrivi la tua password";
		document.getElementById("email").title = "Inserisci il tuo indirizzo email";	
		document.getElementById("email2").title = "Riscrivi il tuo indirizzo email";
		document.getElementById("send").title = "Invia i dati appena inseriti";
		document.getElementById("reset").title = "Resetta tutti i campi appena inseriti";

		break;

		// Lingua Inglese
	case "en":
		document.getElementById("legOne").firstChild.nodeValue = "Registration modul";
		document.getElementById("lblLanguage").firstChild.nodeValue = "Language ";
		document.getElementById("tdlNickname").firstChild.nodeValue = "Nickname: ";
		document.getElementById("tdlPassword").firstChild.nodeValue = "Password: ";
		document.getElementById("td2Password").firstChild.nodeValue = "Password Confirmation: ";
		document.getElementById("tdlEmail").firstChild.nodeValue = "Email: ";
		document.getElementById("td2Email").firstChild.nodeValue = "Email Confirmation: ";
		document.getElementById("send").value = "Send";
		document.getElementById("reset").value = "Reset";

		document.getElementById("language").title = "Select your language ";
		document.getElementById("nickname").title = "Enter your nickname";
		document.getElementById("password").title = "Enter your password";
		document.getElementById("password2").title = "Retype your password";
		document.getElementById("email").title = "Enter your email address";	
		document.getElementById("email2").title = "Retype your email address";
		document.getElementById("send").title = "Send the data just inserteda";
		document.getElementById("reset").title = "Reset all the fields just entered";

		break;
	}
}