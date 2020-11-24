# The Password Manager

## Generate, Check, and Store Passwords!

### Features:
#### Generate Password:
- generates a password that meets all the requirements of a strong password
- option to save generated password
#### Check Password Strength:
- input password to see how strong of a password it is, and which requirements it does and does not meet
#### Store Saved Passwords
- see all saved passwords
- select a password to add info to it like usernames and additional notes
- add new password entry
- delete password entry


### Who is this for?
*Anyone!* Anyone who uses the internet has probably had to create a password protected account at some point, and this
application can help with that. It can simply be used as a place to keep track of all your passwords, or it can help 
create new passwords.


### Why build this?
I've been running into the issue recently where I'm staring at a login screen and have no idea what the password could
be. Sure, Google saves some of my passwords, but there are still a bunch that are only stored at the back of my brain.
With many things moving online, I also have to create new passwords for things, and I'm running out of ideas. An app like
this fixes both of these problems!


### User Stories:
#### Phase 1
##### The Main 4:
- As a user, I want to be able to generate a strong password
- As a user, I want to have the option to save a generated or typed in password under a name to my collection of passwords
- As a user, I want to be able to view the list of all the names of my stored passwords sorted by most recent or 
alphabetically
- As a user, I want to check the strength of a password I type in
##### Additional:
- As a user, I want to be able to select a name of a password and see the password plus related info
- As a user, I want to be able to add and update the info for my password like the username, url, or even the password
- As a user, I want to be able to delete a stored password

#### Phase 2
- As a user, I want the application to automatically save any changes I make to my saved passwords
- As a user, if I have any saved passwords, I want to see them immediately when the application starts
 
#### Phase 3
- As a user, I want the ability to quickly copy passwords to my clipboard
- As a user, I want the option to load saved passwords
- As a user, I want the option to save changes to a password I edit
- As a user, I want to be able to search through my saved passwords

#### Phase 4: Task 2
PasswordManager was made robust. getPasswordLog and deletePasswordLog now both throw a checked exception. 
Additionally, viewPasswordsSorted now uses an enumeration for the sort order instead of requiring a specific string input.