package org.example.bloodbank;

public class DonorData {
    String name;
    String address;
    String contact;
    String age;
    String bloodGroup;
    public DonorData() {

    }
    public DonorData(String name,String address,String contact,String age,String bloodGroup)
    {
        this.name=name;
        this.address=address;
        this.age=age;
        this.contact=contact;
        this.bloodGroup=bloodGroup;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }
}
