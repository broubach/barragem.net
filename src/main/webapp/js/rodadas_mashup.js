function rodadasCallback(barragem) {
	var html = "<h1 style='color:#333333; font-family: arial,verdana,helvetica,sans-serif; font-size:14px; font-weight:bold;'>" + barragem.local + ", " + barragem.categoria + " - " + barragem.ciclo + "</h1>";
	html += "<div><ul style='padding: 5px; margin-top: 25px; margin-left: 0px; float: left;'>";
	for (var i=0; i<barragem.rodadas; i++) {
		html += "<li style='list-style: none;'><a href='#' style='font-family: arial,verdana,helvetica,sans-serif; font-size: 13.2px; font-weight: 400; font-style: normal; color: #5AA427; text-decoration: underline; text-align: left;' onclick='loadRodada(" + barragem.id + ", " + (i + 1) + ")'>" + (i + 1) + "a. Rodada</a></li>";
	}
	html += "</ul></div>";

	html += "<div id='mashup_jogos'>";
	html += "<table style='font-family: arial,verdana,helvetica,sans-serif; font-size: 12px; border-collapse: collapse; color: #333333; margin-top: 3px;'>";
	html += "<thead><tr>";
	html += "<th style='text-align: right; border-bottom:1px solid #E3EAEE;' colspan='7'>";
	html += "<label style='vertical-align: middle; font-family: arial,verdana,helvetica,sans-serif; color: rgb(127, 127, 127); font-size: 11px; font-weight: bold;'>powered by</label> <a href='http://www.barragem.net'><img width='144px' height='27px' border='0' style='vertical-align: middle;' src='http://www.barragem.net/img/logo-barragem-mashup.png'></a>";
	html += "</th>";
	html += "</tr></thead>";
	html += "<tr>";
	html += "<td style='border-bottom:1px solid #E3EAEE; padding:4px; text-align:left; white-space: nowrap;'>Selecione uma rodada para visualizar os jogos.</td>";
	html += "</tr>";
	html += "</table>";
	html += "</div>";

	document.getElementById("barragem_rodadas_mashup").innerHTML = html;
}

function loadRodada(barragem, rodada) {
	loadjsfile('http://www.barragem.net/publicpages/mashup/rodada.json?barragemId='+barragem+'&callback=rodadaCallback&numRodada='+rodada, 'js');
}

//http://www.javascriptkit.com/javatutors/loadjavascriptcss.shtml
function loadjsfile(filename, filetype){
	if (filetype=="js"){
		var fileref=document.createElement('script');
		fileref.setAttribute("type","text/javascript");
		fileref.setAttribute("src", filename);
	}
	if (typeof fileref!="undefined") {
		document.getElementsByTagName("head")[0].appendChild(fileref);
	}
}

function rodadaCallback(jogosEBonus) {
	var html = "<table style='font-family: arial,verdana,helvetica,sans-serif; font-size: 12px; border-collapse: collapse; color: #333333; margin-top: 3px;'><thead>";
	html += "<tr><th style='text-align: right; border-bottom:1px solid #E3EAEE;' colspan='7'><label style='vertical-align: middle; font-family: arial,verdana,helvetica,sans-serif; color: rgb(127, 127, 127); font-size: 11px; font-weight: bold;'>powered by</label> <a href='http://www.barragem.net'><img width='144px' height='27px' border='0' style='vertical-align: middle;' src='http://www.barragem.net/img/logo-barragem-mashup.png'></a></th></tr>";
	html += "</thead><tbody>";
	for (var i=0; i<jogosEBonus.jogos.length; i++) {
		html += "<tr>";
		html += "<td style='border-bottom: 1px solid #E3EAEE; padding: 4px; text-align: left; white-space: nowrap;'>" + jogosEBonus.jogos[i].jogador1 + "</td>";
		html += "<td style='border-bottom: 1px solid #E3EAEE; padding: 4px; text-align: left; white-space: nowrap;'>" + jogosEBonus.jogos[i].pts1 + "</td>";
		html += "<td style='border-bottom: 1px solid #E3EAEE; padding: 4px; text-align: left; white-space: nowrap;'>X</td>";
		html += "<td style='border-bottom: 1px solid #E3EAEE; padding: 4px; text-align: left; white-space: nowrap;'>" + jogosEBonus.jogos[i].jogador2 + "</td>";
		html += "<td style='border-bottom: 1px solid #E3EAEE; padding: 4px; text-align: left; white-space: nowrap;'>" + jogosEBonus.jogos[i].pts2 + "</td>";
		html += "<td style='border-bottom: 1px solid #E3EAEE; padding: 4px; text-align: left; white-space: nowrap;'>" + jogosEBonus.jogos[i].placar + "</td>";
		html += "<td style='border-bottom: 1px solid #E3EAEE; padding: 4px; text-align: left; white-space: nowrap;'>" + jogosEBonus.jogos[i].dataHora + "</td>";
		html += "</tr>";
	}
	if (jogosEBonus.jogos.length == 0) {
		html += "<tr>";
		html += "<td style='border-bottom:1px solid #E3EAEE; padding:4px; text-align:left; white-space: nowrap;'>Rodada sem jogos cadastrados.</td>";
		html += "</tr>";
	}

	html += "</tbody></table>";
	if (jogosEBonus.bonus.length > 0) {
		html += "<br/><fieldset style='color:#333333; font-family:arial,verdana,helvetica,sans-serif;font-size:12px;float:left;'>";
		html += '<legend>B\u00f4nus</legend>';
		html += "<ul style='margin-top: 0px; margin-bottom: 0px; padding-left: 15px'>";
		for (var i=0; i<jogosEBonus.bonus.length; i++) {
			html += '<li>' + jogosEBonus.bonus[i].jogadorNome + ': ' + jogosEBonus.bonus[i].justificativa + ' (' + jogosEBonus.bonus[i].valor + ' pts' + ')</li>';
		}
		html += '</ul>';
		html += '</fieldset>';
	}

	document.getElementById("mashup_jogos").innerHTML = html;
}