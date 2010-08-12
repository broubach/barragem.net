function barragemCallback(barragem) {
	var html = "<style>";
	html += ".mashup_wrapper { position: relative; }";
	html += ".mashup_logo { position: absolute; bottom: -30px; left: 185px; }";
	html += ".mashup_rodape { position: absolute; bottom: -23px; left: 115px; font-family: arial,verdana,helvetica,sans-serif; color: #7F7F7F; font-size: 11px; font-weight: bold; }";
	html += ".mashup_titulo { font-family: arial,verdana,helvetica,sans-serif; color: #333333; font-size: 14px; font-weight: bold;}";
	html += ".mashup_ranking { font-family: arial,verdana,helvetica,sans-serif; color: #333333; font-size: 12px}";
	html += ".mashup_ranking { border-collapse: collapse; margin-top: 3px; }";
	html += ".mashup_ranking thead { font-weight: bold; font-size: 12px; color: #ffffff; background: #268ACA; }";
	html += ".mashup_ranking td { border-bottom: 1px solid #E3EAEE; padding: 4px; }";
	html += ".mashup_ranking th { padding-left: 5px; padding-right: 40px; padding-top: 5px; padding-bottom: 5px; }";
	html += ".mashup_ranking td, .mashup_ranking th { text-align: left; }";
	html += "</style>";
	html += "<div class='mashup_wrapper'>";
	html += "<label class='mashup_titulo'>" + barragem.local + ", " + barragem.categoria + " - " + barragem.ciclo + "</label>";
	html += "<table class='mashup_ranking'><thead class='titulo'><tr><th>Nome</th><th>Ranking</th><th>Pontua&ccedil;&atilde;o</th></tr></thead><tbody>";
	for (var i=0; i<barragem.ranking.length; i++) {
		html += "<tr>";
		html += "<td>" + barragem.ranking[i].nome + "</td>";
		html += "<td>" + barragem.ranking[i].ranking + "</td>";
		html += "<td>" + barragem.ranking[i].pontuacao + "</td>";
		html += "</tr>";
	}
	html += "</tbody></table>";
	html += "<label style='mashup_rodape'>powered by</label>";
	html += "<a href='http://www.barragem.net' class='mashup_logo'><img src='http://www.barragem.net/img/logo-barragem-mashup.png' width='144px' height='27px' border='0'/></a>";
	html += "</div>";
	html += "<br/>";
	document.getElementById('barragem_ranking_mashup').innerHTML = html;
}