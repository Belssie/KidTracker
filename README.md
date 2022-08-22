# KidTracker
## Description
This application is a diary for information about a kid's daily routine.
(Currently fixing some of the code - last section ->Improvement plan)

The type of data to be added and manipulated with CRUD operations:
- name, age, birth date and gender of a child;
- list of tracked children;
- pictures;
- feeding, sleeping/fell asleep/woke up, diaper change, pumping date/time;
- growth, memory, symptoms, medication taken and vaccine date/time;
- notes for all of the above;
- chronologies of the above;
- reminders done with notifications, presented in a chronological view;

Additional options:
- language change (BG/EN/FR/DE);
- email support;

![class-method-diagram](https://user-images.githubusercontent.com/43501902/160607887-fd2b7164-effb-4b73-9566-379af6224067.PNG)


## Technologies
The project is created on Android Studio with Java and the used database is noSQL Firebase RealTime Database with the following options:
- Firebase Storage - to store uploaded photos;
- Firebase Authentication - for account authentication and password retrieval.

## Prerequisites
Android Studio with Emulator/mobile phone with Android OS;

## How to run
Open the project through Android Studio and run the application.


## Improvement plan
I. Code
- more testing of the functionalities;
- improve the application of OOP principles and use more abstraction;
- clean the code;
- add comments;
- 
II. Database
- switch to SQLite DB;

III. Functionality
- better interface;
- diagrams for the chronologies;
- option to change theme (light/dark);
- alarm sound;
- icon for each activity;
- a section "Icon Legend";
- ability for the user to add Chip buttons as words in some of the fragments;
- Push notifications;
