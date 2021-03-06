package de.muffincrafter.mrwater;

public class Product
{
	private String name;
	private int water;
	private String tags;
	private int barcode;
	private long id;

	public Product(String name, int quantity, String tags, int barcode, long id)
	{
		this.name = name;
		this.water = quantity;
		this.tags = tags;
		this.barcode = barcode;
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public int getWater()
	{
		return water;
	}

	public void setWater(int water)
	{
		this.water = water;
	}

	public String getTags()
	{
		return tags;
	}

	public void setTags(String tags)
	{
		this.tags = tags;
	}

	public int getBarcode()
	{
		return barcode;
	}

	public void setBarcode(int barcode)
	{
		this.barcode = barcode;
	}

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	@Override
	public String toString()
	{
		String output = name + " (" + water + " l/kg)";

		return output;
	}
}
