package store;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.persistence.*;

@Entity
@Table( name = "shop")
public class Shop {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="id")
	private int id;
	@Column(name ="director")
	private String director = "Vaneev Andrey";
	@Column(name ="StoreName")
	private String StoreName = "Supreme";
	@Column(name ="specialization")
	private String specialization = "grocery shop";
	@Column(name ="adress")
	private String adress = "London 2/3 Peter Street";

	public void SetAdress(String new_adress){
		adress = new_adress;
		}
	public void SetDirector(String new_director){
		director = new_director;
		}
	public void SetSpecialization(String new_specialization){
		specialization = new_specialization;
		}
	public void SetStoreName(String new_name){
		StoreName = new_name;
		}
	public String GetSpecialization(){return specialization;}
	public String GetAdress(){return adress;}
	public String GetStoreName(){return StoreName;}
	public String GetDirector(){return director;}

	public void GetInfo() {
		System.out.println(GetStoreName());
		System.out.println("Специализация: " + GetSpecialization());
		System.out.println("Адрес :" + GetAdress());
		System.out.println("Директор :" + GetDirector());
	}
	
}
