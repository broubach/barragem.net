function barragemCallback(barragem) {
	var html = "<div style='position: relative;'>";
	html += "<table style='font-family: arial,verdana,helvetica,sans-serif; color: #333333; font-size: 12px; border-collapse: collapse; margin-top: 3px;'><thead>";
	html += "<tr><td colspan='3'><label style='font-family: arial,verdana,helvetica,sans-serif; color: #333333; font-size: 14px; font-weight: bold;'>" + barragem.local + ", " + barragem.categoria + " - " + barragem.ciclo + "</label></td></tr>";
	html += "<tr style='font-weight: bold; font-size: 12px; color: #ffffff; background: #268ACA;'><th style='padding-left: 5px; padding-right: 20px; padding-top: 5px; padding-bottom: 5px; text-align: left;'>Nome</th><th style='padding-left: 5px; padding-right: 20px; padding-top: 5px; padding-bottom: 5px; text-align: left;'>Ranking</th><th style='padding-left: 5px; padding-right: 20px; padding-top: 5px; padding-bottom: 5px; text-align: left;'>Pontua&ccedil;&atilde;o</th></tr>";
	html += "</thead><tbody>";
	for (var i=0; i<barragem.ranking.length; i++) {
		html += "<tr>";
		html += "<td style='border-bottom: 1px solid #E3EAEE; padding: 4px; text-align: left;'>" + barragem.ranking[i].nome + "</td>";
		html += "<td style='border-bottom: 1px solid #E3EAEE; padding: 4px; text-align: left;'>" + barragem.ranking[i].ranking + "</td>";
		html += "<td style='border-bottom: 1px solid #E3EAEE; padding: 4px; text-align: left;'>" + barragem.ranking[i].pontuacao + "</td>";
		html += "</tr>";
	}
	html += "</tbody><tfoot><tr><td colspan='3' style='text-align: right;'>";
	html += "<label style='vertical-align: middle; font-family: arial,verdana,helvetica,sans-serif; color: #7F7F7F; font-size: 11px; font-weight: bold;'>powered by</label> ";
	html += "<a href='http://www.barragem.net'><img src='http://www.barragem.net/img/logo-barragem-mashup.png' width='144px' height='27px' border='0' style='vertical-align: middle;'/></a>";
	html += "</td></tr></tfoot></table>";
	html += "</div>";
	html += "<br/>";
	document.getElementById('barragem_ranking_mashup').innerHTML = html;
}