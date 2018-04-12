# LondonMeetup

This is an app that I built as part of Coursework 1 for the U08971 Advanced Mobile Software Development module. You can find the coursework specification [here](U08971-CW1-SPEC.pdf).

There are two folders - LondonMeetup and LondonMeetup_v2. 

## LondonMeetup

This is the original source code of the app that I submitted. It's written purely in Java and does not use any third party libraries (apart from Google Play Libraries and Support Libraries - which aren't really "3rd party"). This is because the coursework specifications strictly prevent the use of any such libraries. Due to time restrictions, it also does not use any specific architecture, such as MVP or MVVM.

## LondonMeetup_V2

This is a complete rewrite of the app in pure Kotlin. This uses **Dagger** for dependency injection, **Retrofit** for networking and **Kotlin coroutines** for asynchronous work. It's based on **MVP** + **Clean Architecture**. 

This is a WORK IN PROGRESS. The code is divided into two packages - oldcode and newcode.

* The _oldcode_ package contains Kotlin code that hasn't been transitioned to MVP+Clean architecture.
* The _newcode_ package contains Kotlin code that either has been fully transitioned to MVP+Clean architecture, or is currently being transitioned.
