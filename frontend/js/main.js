$('#partido').change(function(){

	$('#mapid').remove();
	$('<div id="mapid" style="height: 100%;"/>').appendTo('#content');

	$.ajax({
		url: 'http://localhost:8080/api/eleicao/2014/presidente/primeiro-turno/estados',
		type: "GET",
		dataType: "json",
		success: function (infoEstados) {
			var map;
			var partidoSelecionado = $('#partido').val();
			var intensidade;
			var votosPorPartido;
			var container = L.DomUtil.get('mapid');
			
			if(container != null){
				container._leaflet_id = null;
			}
			
			map = L.map('mapid').setView([-14.210, -47.989], 4);
			
			for(var i = 0; i < infoEstados.length; i++){
				var listaCandidatosEstados = JSON.parse(infoEstados[i].candidatos);
				for(j = 0; j < listaCandidatosEstados.length;j++){
					if(partidoSelecionado === listaCandidatosEstados[j][0]){
						intensidade = listaCandidatosEstados[j][3];
						votosPorPartido = listaCandidatosEstados[j][2];
					}	
				}
				
				var layer = L.geoJSON(JSON.parse(infoEstados[i].malhas),{
					style: {
						"fillColor": obterCor(),
						"weight": 2,
						"opacity": 1,
						"color":"#FFFFFF",
						"fillOpacity": 0.7
					}
				}).addTo(map);
				
				layer.bindTooltip(infoEstados[i].nome + ' - ' + infoEstados[i].sigla + '<br/>Votos recebidos: ' + votosPorPartido.toString()).addTo(map);
				
				function obterCor(){
					return intensidade > 50  ? '#800026' :
						   intensidade > 40  ? '#BD0026' :
						   intensidade > 30  ? '#E31A1C' :
						   intensidade > 20  ? '#FC4E2A' :
						   intensidade > 10  ? '#FD8D3C' :
						   intensidade > 1   ? '#FEB24C' :
						   intensidade > 0.5 ? '#FED976' :
											   '#FFEDA0';
				}
			}
			
			var basemap = L.tileLayer('http://{s}.basemaps.cartocdn.com/light_nolabels/{z}/{x}/{y}.png', {
				maxZoom: 19
			}).addTo(map);
		},
		error: function () {
			$('<p>Ocorreu uma falha ao tentar conectar a API.</p>').appendTo('#mapid');
		}
	});
	$('#estado').val('Selecione');
});

$('#estado').change(function(){
	var estadoSelecionado = $('#estado').val();

	$('#mapid').remove();
	$('<div id="mapid" style="height: 100%;"/>').appendTo('#content');

	$.ajax({
		url: 'http://localhost:8080/api/eleicao/2014/presidente/primeiro-turno/estados/'+estadoSelecionado+'/municipios',
		type: "GET",
		dataType: "json",
		success: function(infoMunicipios){
			var map;
			var partidoSelecionado = $('#partido').val();
			var intensidade;
			var votosPorPartido;
			var container = L.DomUtil.get('mapid');
			
			if(container != null){
				container._leaflet_id = null;
			}
			
			function obterLatLongEstado(){
				var malhasEstadoSelecionado = JSON.parse(infoMunicipios[0].estado.malhas);
				var centroide = malhasEstadoSelecionado.features[0].properties.centroide;
				var temp = centroide[0];
				centroide[0] = centroide[1];
				centroide[1] = temp;
				
				return centroide;
			}
			
			map = L.map('mapid').setView(obterLatLongEstado(), 6);
			
			for(var i = 0; i < infoMunicipios.length; i++){
				var listaCandidatosEstados = JSON.parse(infoMunicipios[i].candidatos);
				for(j = 0; j < listaCandidatosEstados.length;j++){
					if(partidoSelecionado === listaCandidatosEstados[j][0]){
						intensidade = listaCandidatosEstados[j][3];
						votosPorPartido = listaCandidatosEstados[j][2];
					}	
				}
				
				var layer = L.geoJSON(JSON.parse(infoMunicipios[i].malhas),{
					style: {
						"fillColor": obterCor(),
						"weight": 2,
						"opacity": 1,
						"color":"#FFFFFF",
						"fillOpacity": 0.7
					}
				}).addTo(map);
				
				layer.bindTooltip(infoMunicipios[i].nome + '<br/>Votos recebidos: ' + votosPorPartido.toString()).addTo(map);
				
				function obterCor(){
					return intensidade > 50  ? '#800026' :
						   intensidade > 40  ? '#BD0026' :
						   intensidade > 30  ? '#E31A1C' :
						   intensidade > 20  ? '#FC4E2A' :
						   intensidade > 10  ? '#FD8D3C' :
						   intensidade > 1   ? '#FEB24C' :
						   intensidade > 0.5 ? '#FED976' :
											   '#FFEDA0';
				}
			}
			
			var basemap = L.tileLayer('http://{s}.basemaps.cartocdn.com/light_nolabels/{z}/{x}/{y}.png', {
				maxZoom: 19
			}).addTo(map);
		},
		error: function () {
			$('<p>Ocorreu uma falha ao tentar conectar a API.</p>').appendTo('#mapid');
		}
	});
});