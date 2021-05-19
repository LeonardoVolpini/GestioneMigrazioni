package it.polito.tdp.borders.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.borders.db.BordersDAO;

public class Model {
	
	private Graph<Country, DefaultEdge> graph ;
	private Map<Integer,Country> countriesMap ;
	private Simulator sim;
	
	public Model() {
		this.countriesMap = new HashMap<>() ;
		this.sim= new Simulator();
	}
	
	public void creaGrafo(int anno) {
		
		this.graph = new SimpleGraph<>(DefaultEdge.class) ;

		BordersDAO dao = new BordersDAO() ;
		
		//vertici
		dao.getCountriesFromYear(anno,this.countriesMap) ;
		Graphs.addAllVertices(graph, this.countriesMap.values()) ;
		
		// archi
		List<Adiacenza> archi = dao.getCoppieAdiacenti(anno) ;
		for( Adiacenza c: archi) {
			graph.addEdge(this.countriesMap.get(c.getState1no()), 
					this.countriesMap.get(c.getState2no())) ;
			
		}
	}
	
	public List<CountryAndNumber> getCountryAndNumbers(){
		List<CountryAndNumber> result= new LinkedList<>();
		for (Country c : this.graph.vertexSet()) {
			result.add(new CountryAndNumber(c,this.graph.degreeOf(c)) );
		}
		Collections.sort(result);
		return result;
	}
	
	public void simula(Country partenza) {
		if (graph!=null) {
			sim.init(partenza, graph);
			sim.run();
		}
	}
	public int getT() {
		return this.sim.numPassiSimulati();
	}
	
	public List<CountryAndNumber> getStanziali() {
		Map<Country,Integer> stanziali=sim.getStanziali();
		List<CountryAndNumber> result= new LinkedList<>();
		for (Country c : stanziali.keySet()) {
			if (stanziali.get(c)>0) {
				CountryAndNumber cn = new CountryAndNumber(c,stanziali.get(c));
				result.add(cn);
			}
		}
		Collections.sort(result);
		return result;
	}

	public Set<Country> getCountries() {
		if (graph!=null) {
			return this.graph.vertexSet();
		}
		return null;
	}
	

}
