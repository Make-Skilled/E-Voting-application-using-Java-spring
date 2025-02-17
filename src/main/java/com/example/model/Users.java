package com.example.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Users {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "aadhar", unique = true, nullable = false, length = 12)
    private String aadhar;

    @Column(name = "mobile_number", nullable = false, length = 10)
    private String mobileNumber;

    @Column(name = "dob", nullable = false)
    private String dob;

    @Column(name = "state", nullable = false)
    private String state;

    @Column(name = "district", nullable = false)
    private String district;

    @Column(name = "pincode", nullable = false, length = 6)
    private String pincode;

    @Column(name = "gender", nullable = false)
    private String gender;

    public Users() {
    }

    public Users(String fullName, String email, String aadhar, String mobileNumber, String dob, String state, String district, String pincode, String gender) {
        this.fullName = fullName;
        this.email = email;
        this.aadhar = aadhar;
        this.mobileNumber = mobileNumber;
        this.dob = dob;
        this.state = state;
        this.district = district;
        this.pincode = pincode;
        this.gender = gender;
    }

    // Getters and Setters
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAadhar() {
        return aadhar;
    }

    public void setAadhar(String aadhar) {
        this.aadhar = aadhar;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "Users{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", aadhar='" + aadhar + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", dob='" + dob + '\'' +
                ", state='" + state + '\'' +
                ", district='" + district + '\'' +
                ", pincode='" + pincode + '\'' +
                ", gender='" + gender + '\'' +
                '}';
    }
}
