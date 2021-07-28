# Weightroom

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
2. [Schema](#Schema)

## Overview
### Description
**Weightroom** will offer users substitute exercises with the equipment that the users have access to in order to hit the muscle group/category [arms,shoulders,etc.] that they would have missed hitting otherwise. Many people have ran into the problem of not having access to the equipment that an exercise calls for so they just dont hit that muscle group for the day.
### App Evaluation
[Evaluation of your app across the following attributes]
- **Category: Health/Fitness**
- **Mobile: People use the app to train based on their equipment, desired exercise types, and level of experience/fitness. The mobile app allows user to get all of the information they need for a quality workout from their phone.**
- **Story: Users can get personalized workout recommendations with specific directions to follow based on their equipment**
- **Market: Anyone who goes to the gym or wants to go. People who workout at home.**
- **Habit: Used while at the gym or when expecting to go**
- **Scope: This app would allow users to input their equipment and then retrieve a list of exercises and the accompanying direction on how to perform the exercise with the equipment they have access to. Further details such as muscles worked will also be available**

## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories**

* [x] Users can sign-in/register
* [x] Users can input a list of equiptment they have access too
* [x] Checkboxes for equiptment that the user has access to
* [x] Display of exercises using user-specified equipment
* [x] On login/registration they are prompted to input their equipment (not prompted everytime they login)
* [x] Users can go to their profile and edit their equipment list
* [x] Users are prompted ONLY once on login/registration to enter equipment

**Optional Nice-to-have Stories**

* [x] Customized icon displayed in action bar
* [ ] Recommendation Algorithm for exercise selection based off of current exercises
* [ ] Progress Bar (Clean animation) while data is being fetched
* [ ] binding library
* [ ] profile picture upload



### 2. Screen Archetypes

* Welcome screen Quick animation
   * A nice screen that displays logo and progress bar underneath
* Login screen
   * Allow users to sign-in or register after welcome

* Prompt screen
   * Allow users to enter their equipment after first login or registration
* Home screen (Recycler View, linear)
   * View exercises unique to the user based on their equipment
   * Hold down exercises on the view to add them to a list of current exercises
* Detail Screen
    *  Display muscles worked, equipment required, and description of the exercise movement. 


### 3. Navigation

**Tab Navigation** (Tab to Screen)

* Home page
* Current exercises
* Profile

**Flow Navigation** (Screen to Screen)

* Login/Register
   * Home

* Prompt screen
   * Allow user to input their equipment
* Stream Screen 
   * Details: Muscles developed/worked, equipment, description of how to perform the movement
* Current Exercise Screen 
    * list of exercises that user has selected to add to their current exercise list

## Wireframes
  [Barebones sketch since digital, will be polished with images in actual app]
<img src= https://i.imgur.com/plzjdLI.png
 width=600>

* [x] [BONUS] Digital Wireframes & Mockups
* [x] [BONUS] Interactive Prototype

Figma includes digital wirefram/mockup as well as interactive prototype: https://www.figma.com/file/CnVOUOEJJkz8Megp9eJafu/Wireframing-(Copy)?node-id=361070%3A448

## Schema 
### Models
#### Equipment

   | Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | objectId      | String   | unique id for the user post (default field) |
   | user          | Pointer to User| image author |
   | equpment      | Array    | list of equipment the user has access to |
   | createdAt     | DateTime | date when post is created (default field) |
   | updatedAt     | DateTime | date when post is last updated (default field) |
### Networking
* HomeFeed
    * (READ/GET) Read the latest equipment object that is stored for the user
    * ![](https://i.imgur.com/SnS7Ssk.png)
* PromptScreen
    * (Create/POST) Create a new equipment list for the user
    * ![](https://i.imgur.com/U3pEKP6.png)

* ProfileScreen 

    * (Read/GET) Query logged in user object
    * ![](https://i.imgur.com/bPnwFFa.png)
    
## License

    Copyright 2021 Christian Boswell

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

