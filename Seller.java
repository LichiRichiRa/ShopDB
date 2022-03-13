package store;

import javax.persistence.*;

@Entity
@Table( name = "sellers")
public class Seller{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="id")
	private int id;
	@Column(name ="salary")
	private int salary;
	@Column(name ="name")	
	private String name;
	
	public void GetInfo(){
		System.out.println(GetID() +" "+ GetName() +" "+ GetSalary());
		}
	public int GetSalary(){return salary;}
	public void SetSalary(int new_salary){
		salary = new_salary;
		}
	public void SetID(int new_ID){
		id = new_ID;
		}

	public String GetName(){return name;}
	public void SetName(String new_name){
		name = new_name;
		}

	public int GetID(){return id;}

}
