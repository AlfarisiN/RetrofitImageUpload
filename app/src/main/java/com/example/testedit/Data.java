package com.example.testedit;

import com.google.gson.annotations.SerializedName;

public class Data{

	@SerializedName("image")
	private Image image;

	@SerializedName("mail")
	private String mail;

	@SerializedName("imagename")
	private String imagename;

	@SerializedName("name")
	private String name;

	@SerializedName("id")
	private int id;

	public void setImage(Image image){
		this.image = image;
	}

	public Image getImage(){
		return image;
	}

	public void setMail(String mail){
		this.mail = mail;
	}

	public String getMail(){
		return mail;
	}

	public void setImagename(String imagename){
		this.imagename = imagename;
	}

	public String getImagename(){
		return imagename;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	@Override
 	public String toString(){
		return 
			"Data{" + 
			"image = '" + image + '\'' + 
			",mail = '" + mail + '\'' + 
			",imagename = '" + imagename + '\'' + 
			",name = '" + name + '\'' + 
			",id = '" + id + '\'' + 
			"}";
		}
}