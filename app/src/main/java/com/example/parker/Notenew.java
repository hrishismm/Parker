package com.example.parker;


public class Notenew {

    private String name;
    private String Email;
    private int phoneno;
    private String message;


    public Notenew()
    {


//Empty constructor needed

    }
    public Notenew(String Email, int phoneno, String name, String message)
    {

        this.Email=Email;
        this.phoneno=phoneno;
        this.name=name;
        this.message=message;

    }
    public String getName() {
        return name;
    }

    public String getEmail() {
        return Email;
    }

    public int getPhoneno() {
        return phoneno;
    }

    public String getMessage() {
        return message;
    }


}
