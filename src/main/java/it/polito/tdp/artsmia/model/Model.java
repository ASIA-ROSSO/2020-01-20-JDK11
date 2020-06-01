package it.polito.tdp.artsmia.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {
	
	private ArtsmiaDAO dao;
	private SimpleWeightedGraph<Artist, DefaultWeightedEdge> grafo;
	private Map<Integer, Artist> artistiMap;
	private List<Coppia> coppie;
	
	private List<Artist> best;
	private Integer bestPeso;
	
	public Model() {
		dao = new ArtsmiaDAO();
	}
	
	public List<String> getRuoli(){
		return dao.listRole();
	}

	public void creaGrafo(String ruolo) {
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		this.artistiMap = new HashMap<Integer, Artist>();
		dao.getArtistiCon(ruolo, artistiMap);
		
		coppie = this.dao.getCoppie(ruolo, artistiMap);
		for(Coppia a : coppie) {
			if(!this.grafo.containsVertex(a.getA1()))
				this.grafo.addVertex(a.getA1());
			if(!this.grafo.containsVertex(a.getA2()))
				this.grafo.addVertex(a.getA2());
			
			if(this.grafo.getEdge(a.getA1(), a.getA2()) == null)
				Graphs.addEdge(this.grafo, a.getA1(), a.getA2(), a.getPeso());
		}
		
		for(Artist a : artistiMap.values()) {
			if(!this.grafo.containsVertex(a))
				this.grafo.addVertex(a);
		}
		
System.out.println(String.format("Grafo creato con %d vertici e %d archi", this.grafo.vertexSet().size(), this.grafo.edgeSet().size()));

	}

	public SimpleWeightedGraph<Artist, DefaultWeightedEdge> getGrafo() {
		return grafo;
	}

	public List<Coppia> getCoppie() {
		Collections.sort(coppie);
		return coppie;
	}

	public void setCoppie(List<Coppia> coppie) {
		this.coppie = coppie;
	}

	public List<Artist> trovaPercorso(Integer artistaId) {
		List<Artist> parziale = new ArrayList<>();
		this.best = new ArrayList<>();
		bestPeso = -1;
		Artist a = artistiMap.get(artistaId);
		
		parziale.add(a); //aggiungiamo il nodo sorgente
		trovaRicorsivo(parziale, -1);
		
		return best;

	}

	private void trovaRicorsivo(List<Artist> parziale, int peso) {
		//la condizione di terminazione è implicita, la ricorsione si esaurisce quando non ci sono più vicini da considerare
		
		//se il peso è -1, sono nel primo caso
		Artist last = parziale.get(parziale.size() -1);
		//ottengo i vicini tramite 
		List<Artist> vicini = Graphs.neighborListOf(this.grafo, last);
		
		for(Artist vicino : vicini) {
			if(!parziale.contains(vicino) && peso == -1) {
			parziale.add(vicino);
			trovaRicorsivo(parziale, (int) this.grafo.getEdgeWeight(this.grafo.getEdge(last, vicino)));
			parziale.remove(vicino);
			} else {
				if(!parziale.contains(vicino) && this.grafo.getEdgeWeight(this.grafo.getEdge(last,  vicino)) == peso) {
					parziale.add(vicino);
					trovaRicorsivo(parziale, peso);
					parziale.remove(vicino);
				}
					
				}
			}
		
		if(parziale.size() > best.size()) {
			best = new ArrayList<Artist>(parziale);
			bestPeso = peso;
		}
		
		
	}
	
	
	
	public Integer getBestPeso() {
		return bestPeso;
	}

	public boolean contiene(Integer id) {
		Artist a = artistiMap.get(id);
		if(this.grafo.containsVertex(a))
			return true;
		
		return false;
		
	}
	
	
	
	
	

}
