package de.muffincrafter.mrwater.sqlite.helper;

public class Product
{
	int id;
	String name;
	int water;
	int barcode;
	
	public Product()
	{
		
	}
	
	public Product(String name, int water, int barcode)
	{
		this.name = name;
		this.water = water;
		this.barcode = barcode;
	}
	
	public Product(int id, String name, int water, int barcode)
	{
		this.id = id;
		this.name = name;
		this.water = water;
		this.barcode = barcode;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public int getId()
	{
		return id;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return name;
	}

	public void setWater(int water)
	{
		this.water = water;
	}

	public int getWater()
	{
		return water;
	}

	public void setBarcode(int barcode)
	{
		this.barcode = barcode;
	}

	public int getBarcode()
	{
		return barcode;
	}
}
