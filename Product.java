package store;

import javax.persistence.*;
@Entity
@Table( name = "products")
public class Product{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="id")
	private int id;
	@Column(name ="price")
	private int price;
	@Column(name ="quantity")
	private int quantity;
	@Column(name ="name")
	private String name;

	
	public void GetInfo(){
	
		System.out.println(GetID() +" "+  GetName() +" "+  GetPrice() +" "+ GetQuantity());
		}

	public int GetPrice(){return price;}
	public void SetPrice(int new_price){
		price = new_price;
		}
	public void SetID(int new_ID){
		id = new_ID;
		}

	public int GetQuantity(){return quantity;}
	public void SetQuantity(int new_quantity){
		quantity = new_quantity;
		}
	
	public String GetName(){return name;}
	public void SetName(String new_name){
		name = new_name;
		}

	public int GetID(){return id;}
}
