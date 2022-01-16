package com.example.deportesdalda

import com.google.firebase.database.Exclude

class Users {
    private var email: String? = null
    private var name: String? = null
    private var lastname: String? = null
    private var points: String? = null
    private var phone: String? = null
    private var date: String? = null

    @Exclude
    fun getEmail(): String? {
        return email
    }

    fun setEmail(email: String?) {
        this.email = email
    }

    fun setName(name: String?) {
        this.name = name
    }

    fun getName(): String? {
        return name
    }

    fun setLastname(lastname: String?) {
        this.lastname = lastname
    }

    fun getLastname(): String? {
        return lastname
    }

    fun setPoints(points: String?) {
        this.points = points
    }

    fun getPoints(): String? {
        return points
    }

    fun setPhone(phone: String?) {
        this.phone = phone
    }

    fun getPhone(): String? {
        return phone
    }

    fun setDate(date: String?) {
        this.date = date
    }

    fun getDate(): String? {
        return date
    }
}