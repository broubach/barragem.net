$j(function() {
  	// elementos restantes para arredondar em um segundo momento, descritos no ticket #36

	var settings = {
		      tl: { radius: 10 },
		      tr: { radius: 10 },
		      bl: { radius: 10 },
		      br: { radius: 10 },
		      antiAlias: true
	};
	curvyCorners(settings, "fieldset");
	curvyCorners(settings, ".passoInativo");
	curvyCorners(settings, ".passoAtivo");
	curvyCorners(settings, ".perfilContainer");
  	curvyCorners(settings, ".foto-exibir-perfil");
//	curvyCorners(settings, ".exibirPainelBarragemRankingTable");
//	curvyCorners(settings, ".exibirPainelBarragemRodadaTable");
	
	settings = {
		      tl: { radius: 3 },
		      tr: { radius: 3 },
		      bl: { radius: 3 },
		      br: { radius: 3 },
		      antiAlias: true
	};
  	curvyCorners(settings, ".language");

	settings = {
		      tl: { radius: 5 },
		      tr: { radius: 5 },
		      bl: { radius: 5 },
		      br: { radius: 5 },
		      antiAlias: true
	};
  	curvyCorners(settings, ".btn-novo");

	settings = {
		      tl: { radius: 20 },
		      tr: { radius: 20 },
		      bl: { radius: 20 },
		      br: { radius: 20 },
		      antiAlias: true
	};
  	curvyCorners(settings, ".maincontent");
  	curvyCorners(settings, ".sidebar");
});