package com.carrental.model;

public enum UserRole {
    GUEST,      // Can only browse cars
    CLIENT,     // Can rent cars
    PROVIDER,   // Can add and manage cars
    ADMIN       // Can manage users, cars, and rentals
}
