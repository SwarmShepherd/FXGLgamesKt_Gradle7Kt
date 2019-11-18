# FXGLgamesKt_Gradle7Kt

To get more familiar with Game Authoring using Kotlin, javaFX, FXGL, IntellIj, and a Build system I decided to look to convert several games from Java to Kotlin.  So far 6 games have been translated and tested (lightly)... with several more hopefully close to working.

I started out using Maven, but after reading about Gradle I wanted to give it a try and wanted to see if I could get everything working using the (newer) Kotlin DSL (replacing Groovy).

Indeed, it seems to have worked well, and exactly the same Gradle file has been used for all of the initial six games.

So this Repo is:
    Kotlin Src
    Kotlin DSL

My work has been done in the community 2019.1 version of IntelliJ

If I open any ("bare") game directy (with only src, resources, and the 

            build.gradle.kts

file IntelliJ asks "Import Project from Gradle" and shows defaults, I accept the defaults, and when I go to the xxxxxApp src file, scroll to bottom I can almost immediately run the App withoutany further setup.  

My impression from watching videos, searching, and working with Gradle 7 is that it is amazingly well integrated into IntellIj... certainly the Kotlin DSL seems to shine for simplicity and conciseness.


...I expect to be converting most if not all the 16 games over the next weeks, and I have found one or two I may add if there is interest in the Kotlin Versions.




