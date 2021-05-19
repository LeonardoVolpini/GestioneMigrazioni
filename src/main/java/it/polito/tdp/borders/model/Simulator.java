package it.polito.tdp.borders.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;

public class Simulator {

	//modello-> qual'e' lo stato del sistema ad ogni passo
	private Graph<Country,DefaultEdge> grafo;
	
	//tipi di evento -> coda prioritaria
	private PriorityQueue<Evento> queue;
	
	//parametri in input
	private int N_MIGRANTI=1000;
	private Country partenza;
	
	//valori in output
	private int T=-1;
	private Map<Country,Integer> stanziali;
	
	public void init(Country country, Graph<Country,DefaultEdge> g) {
		this.partenza=country;
		this.grafo=g;
		
		//imposto stato iniziale:
		this.T=1;
		this.stanziali= new HashMap<>();
		for(Country c: this.grafo.vertexSet()) {
			stanziali.put(c, 0);
		}
		
		this.queue= new PriorityQueue<>();
		this.queue.add(new Evento(T,partenza,this.N_MIGRANTI) );
	}
	
	public void run() {
		Evento e;
		while ((e=this.queue.poll()) != null) {
			int nPersone=e.getN();
			Country stato=e.getCountry();
			this.T=e.getT();
			
			//ottengo i vicini di stato
			List<Country> vicini = Graphs.neighborListOf(grafo, stato);
			
			int migrantiPerStato=(nPersone/2)/vicini.size();
			if(migrantiPerStato>0) {
				//le persone si spostano
				for (Country confinante:vicini) {
					queue.add(new Evento(e.getT()+1,confinante,migrantiPerStato));
				}
			}
			int stanziali= nPersone - migrantiPerStato*vicini.size();
			this.stanziali.put(stato, this.stanziali.get(stato)+stanziali);
		}
	}
	
	public Map<Country,Integer> getStanziali(){
		return this.stanziali;
	}
	
	public int numPassiSimulati() {
		return this.T;
	}
	
}
