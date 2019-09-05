# FRC Scout

Combining the excitement of sport with the rigors of science and technology, FIRST Robotics Competition is the ultimate Sport for the Mind. High-school student participants call it “the hardest fun you’ll ever have.”

Under strict rules, limited resources, and an intense six-week time limit, teams of students are challenged to raise funds, design a team "brand," hone teamwork skills, and build and program industrial-size robots to play a difficult field game against like-minded competitors. It’s as close to real-world engineering as a student can get. Volunteer professional mentors lend their time and talents to guide each team. Each season ends with an exciting FIRST Championship.

Information is critical. FRC Scout is an application that improves upon the process of gathering information from other teams, or "scouting". Users on the mobile side are able to meet with other teams on the fly and input it all on the mobile app. Data can then be submitted to the web server where it becomes available for everyone to view.

## Getting Started
If you have no already viewed it, see the [FRCScout Web Server](https://github.com/AlphaDevelopmentSolutions/FRCScoutWebServer) for the web server documentation.

#### Requirements
This application requires a minimum Android OS version 21 (5.0 Lollipop).

#### Configuration
Configuration is easy, simply create the Keys.java file at:

    app/src/main/java/com/alphadevelopmentsolutions/frcscout/Interafces/Keys.java

and fill it with the following information from the FRCScout Web Server:

    package com.alphadevelopmentsolutions.frcscout.Interfaces;
    
    public interface Keys
    {
        String API_KEY = "YOUR_API_KEY";
        String WEB_URL = "http(s)://subdomain.domain.com/";
        String API_URL = WEB_URL + "api/api.php";
    }
    
## Contributing
Contributions are welcomed and encouraged! 

To contribute, fork this repo and create a pull request to merge into the release branch your issue is tagged under.
