package com.example.testedit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseUser {

	@SerializedName("image")
	@Expose
	private String image;

	@SerializedName("mail")
	@Expose
	private String mail;

	@SerializedName("name")
	@Expose
	private String name;

	@SerializedName("imagename")
	@Expose
	private String imagename;

	@SerializedName("id")
	@Expose
	private int id;

	public String getImagename() {
		return imagename;
	}

	public void setImagename(String imagename) {
		this.imagename = imagename;
	}

	public void setImage(String image){
		this.image = image;
	}

	public String getImage(){
		return image;
	}

	public void setMail(String mail){
		this.mail = mail;
	}

	public String getMail(){
		return mail;
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
			"ResponseUser{" +
			"image = '" + image + '\'' + 
			",mail = '" + mail + '\'' + 
			",name = '" + name + '\'' + 
			",id = '" + id + '\'' + 
			"}";
		}
}