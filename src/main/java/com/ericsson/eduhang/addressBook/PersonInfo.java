package com.ericsson.eduhang.addressBook;

public class PersonInfo{

    private String name;
    private String address;
    private String phone;

    public PersonInfo setName(String name){
        this.name = name;
        return this;
    }

    public String getName(){
        return name;
    }

    public PersonInfo setAddress(String address){
        this.address = address;
        return this;
    }
  
    public String getAddress(){
        return address;
    }

    public PersonInfo setPhone(String phone){
        this.phone = phone;
        return this;
    }

    public String getPhone(){
        return phone;
    }

    public PersonInfo(){
        name = "";
        address = "";
        phone = "";
    }
    
    public boolean equals(Object obj) {
        if(obj==null)
          return false;
        if(this == obj){
          return true;
        }
        if (obj instanceof PersonInfo) {
          PersonInfo other = (PersonInfo) obj;
          if((other.name).equals(this.name) && (other.phone).equals(this.phone) && (other.address).equals(this.address))
        	  return true;
        }
        return false;
      }

}