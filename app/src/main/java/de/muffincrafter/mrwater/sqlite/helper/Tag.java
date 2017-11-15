package de.muffincrafter.mrwater.sqlite.helper;

public class Tag
{
	int id;
	String tagName;
	
	public Tag()
	{
		
	}
	
	public Tag(String tagName)
	{
		this.tagName = tagName;
	}
	
	public Tag(int id, String tagName)
	{
		this.id = id;
		this.tagName = tagName;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public int getId()
	{
		return id;
	}

	public void setTagName(String tagName)
	{
		this.tagName = tagName;
	}

	public String getTagName()
	{
		return tagName;
	}
}
