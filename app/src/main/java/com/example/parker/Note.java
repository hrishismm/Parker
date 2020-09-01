package com.example.parker;

public class Note {

    private String title;
private String description;
private int priority;
    private String latitude;
    private String longitude;
    private  int remaining;
    private String name;
    private String Email;
    private String phoneno;
    private String message;



    public Note()
{


//Empty constructor needed

}



    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return Email;
    }

    public String getPhoneno() {
        return phoneno;
    }

    public String getMessage() {
        return message;
    }

    public Note(String title, String description, int priority, String latitude, String longitude, int remaining,String name,String Email,String phoneno,String message)
{

this.title=title;
this.description=description;
this.priority=priority;
this.latitude=latitude;
    this.longitude=longitude;
    this.remaining=remaining;
this.name=name;
this.Email=Email;
this.phoneno=phoneno;
this.message=message;
}

    public int getRemaining() {
        return remaining;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getPriority() {
        return priority;
    }
}
