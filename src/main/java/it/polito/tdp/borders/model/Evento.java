package it.polito.tdp.borders.model;

public class Evento implements Comparable<Evento>{

	private int t; //tempo t
	private Country country;
	private int n; //numero di persone arrivate al tempo t nel country 
	
	public Evento(int t, Country country, int n) {
		this.t = t;
		this.country = country;
		this.n = n;
	}

	public int getT() {
		return t;
	}

	public void setT(int t) {
		this.t = t;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public int getN() {
		return n;
	}

	public void setN(int n) {
		this.n = n;
	}

	@Override
	public int compareTo(Evento other) {
		return this.t-other.t;
	}

}
