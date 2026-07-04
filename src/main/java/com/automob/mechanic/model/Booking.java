package com.automob.mechanic.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings", indexes = {
    @Index(name = "idx_booking_status",    columnList = "status"),
    @Index(name = "idx_booking_email",     columnList = "email"),
    @Index(name = "idx_booking_booked_at", columnList = "booked_at")
})
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Column(nullable = false)
    private String email;

    @NotBlank(message = "Phone number is required")
    @Column(nullable = false)
    private String phone;

    @Column(name = "phone_code")
    private String phoneCode;

    @NotBlank(message = "Service is required")
    @Column(nullable = false)
    private String service;

    @NotBlank(message = "Car model is required")
    @Column(name = "car_model", nullable = false)
    private String carModel;

    @Column(name = "fuel_type")
    private String fuelType;

    @Column(name = "kilometers_ran")
    private String kilometersRan;

    @Column(name = "booked_at", nullable = false, updatable = false)
    private LocalDateTime bookedAt;

    @Column(nullable = false)
    private String status = "PENDING";

    @PrePersist
    protected void onCreate() {
        this.bookedAt = LocalDateTime.now();
        if (this.status == null) this.status = "PENDING";
    }

    public Long getId()                { return id; }
    public String getName()            { return name; }
    public String getEmail()           { return email; }
    public String getPhone()           { return phone; }
    public String getPhoneCode()       { return phoneCode; }
    public String getService()         { return service; }
    public String getCarModel()        { return carModel; }
    public String getFuelType()        { return fuelType; }
    public String getKilometersRan()   { return kilometersRan; }
    public LocalDateTime getBookedAt() { return bookedAt; }
    public String getStatus()          { return status; }

    public void setId(Long id)                  { this.id = id; }
    public void setName(String name)            { this.name = name; }
    public void setEmail(String email)          { this.email = email; }
    public void setPhone(String phone)          { this.phone = phone; }
    public void setPhoneCode(String phoneCode)  { this.phoneCode = phoneCode; }
    public void setService(String service)      { this.service = service; }
    public void setCarModel(String carModel)    { this.carModel = carModel; }
    public void setFuelType(String fuelType)    { this.fuelType = fuelType; }
    public void setKilometersRan(String km)     { this.kilometersRan = km; }
    public void setBookedAt(LocalDateTime dt)   { this.bookedAt = dt; }
    public void setStatus(String status)        { this.status = status; }
}
