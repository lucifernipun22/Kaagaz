<h1 align="center">Kaagaz Camera App</h1>

<p align="center">
  <a href="https://android-arsenal.com/api?level=21"><img alt="API" src="https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat"/></a>
</p>

<p align="center">  
This is a Camera App android project based on modern Android application tech-stacks and MVVM architecture. In this app user can capture the image and also take multiple photos in one go. When user capture the image it will 
save into the Room database. We can also preview all the images and fetch the image from the database.
</p>


## Libraries & Tech Used
Go to the [build.gradle](https://github.com/lucifernipun22/Kaagaz/blob/main/app/build.gradle) to see all the libraries.
- Minimum SDK level 23
- Target SDK 29
- [Kotlin](https://kotlinlang.org/) based, [Coroutines](https://github.com/Kotlin/kotlinx.coroutines) for asynchronous handling.
- JetPack
  - LiveData - notify domain layer data to views.
  - Lifecycle - observing data when lifecycle state changes.
  - ViewModel - lifecycle aware UI related data holder.
- Architecture
  - MVVM Architecture (View - DataBinding - ViewModel - Model) 
- [Glide](https://github.com/bumptech/glide) - loading images.
-  Room - for Save the data into local database
## Architecture
 Kaagaz Camera App based on MVVM architecture.
- ![architecture](https://github.com/lucifernipun22/Saveo_Assignment/blob/main/architecture.png)
