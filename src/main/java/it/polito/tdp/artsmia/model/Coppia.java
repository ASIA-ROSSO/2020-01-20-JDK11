package it.polito.tdp.artsmia.model;

public class Coppia implements Comparable<Coppia>{
	private Artist a1;
	private Artist a2;
	private Double peso;
	public Coppia(Artist a1, Artist a2, Double peso) {
		super();
		this.a1 = a1;
		this.a2 = a2;
		this.peso = peso;
	}
	public Artist getA1() {
		return a1;
	}
	public void setA1(Artist a1) {
		this.a1 = a1;
	}
	public Artist getA2() {
		return a2;
	}
	public void setA2(Artist a2) {
		this.a2 = a2;
	}
	public Double getPeso() {
		return peso;
	}
	public void setPeso(Double peso) {
		this.peso = peso;
	}
	
	@Override
	public String toString() {
		return "a1=" + a1.getId() + ", a2=" + a2.getId() + ", peso=" + peso;
	}
	
	
	@Override
	public int compareTo(Coppia o) {
		return - this.peso.compareTo(o.getPeso());
	}
	
	

}
