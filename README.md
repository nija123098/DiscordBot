# DiscordBot
##### AKA Evelyn
A multipurpose Discord bot with an emphasis on music, moderation, and economy.

## Technology
DiscordBot uses the Java reflection API, this is rumored to be incredibly slow, it's not.  There are many things wrong with the JVM, reflection is no longer one of them.

DiscordBot untilizes many APIs in an effort to provide as many **meaningful** integrations as possible.  While this includes mostly data collection from other services, it does not necessarily mean it is limited to data collection and that data collection is all it will utilize going into the future.

SQL (MeriaDB) I hate Databases.

## Design
The project was made with several things in mind.

1. The server running the program is sufficient and is not short on by memory.

2. That the core should do most of the work allowing for all other code to be as simple as possible.

3. That other programers would work on features of the bot which fit into the core, either by commands or normally running services and be able to do so in a simple mannor due to the core's ability to do most of the work.

4. That the bot would not be limited by access issues in method invocation and would instead be supplied as requested.

5. That the core was removable from most features of the bot in order to allow for the possibility of features being jar loaded in while the bot continued to run. (This has been put on a very far back burner as login speed has been increased significantly and it is no longer a goal to make a general framework for Discord bots)



# Getting Started

## Music
I play music! simply by using the !play command you can search for music from soundcloud and youtube.

## Moderation
I have several advanced moderation features; welcoming messages, moderation logs, a custom permissions system to let you grant moderation powers to specific roles.

## Economy
I'm also facilitating an entire economic system based on :cookie:

## An introduction to some more concepts
the following can be seen by using !guide

### You can edit commands:
If you make a typo in a command, and I react to it with a :grey_question:
simply edit your command and it will work!

### I like reactions:
A lot of my commands/features use reactions, for example this guide uses reactions to switch pages. Some of my commands can only be activated by reactions: ex. star board, slots, translate.
Other commands can use reactions as well as the usual text, you'll find the appropriate use when you do !help <command>

### Configuration:
Just about any and all features of mine are configurable, including channels, roles, users, just try !config with any of the previous parameters and see what happens.

### Cookies:
Cookies are stored in two ways, your personal bank, and your guild bank.
Your guild bank cookies can only be used in that guild, for purchasing roles, etc.
Your personal bank of cookies don't decrease when you use your guild bank cookies, but are instead used for other features you might see Soon™

### DM commands:
I can recognise all commands in direct message with me, and you'll need to use !cfg there to set all your user configurations, like your spoken language.

### Translation:
I have a translation functionality! Once you've set your user language as I mentioned in the previous point, you can react to any message with :speech_balloon: or :speech_left: and I'll do my best to translate that message for you!

### Calls:
Did I mention you can open up a call to another server? Well, you can! !help call is all you need.

### Reputation:
Do you think someone said something nice? Something awesome? Did it inspire you? Well now you can show your happiness by giving someone a reputation point. Simply react to a message they sent with :thumbsup:

### Star Board:
Rather than give someone just a reputation point, why not try to feature their message on your server's star board? React with enough stars to someone's message and I'll feature it in the star board channel! The more stars, the fancier the message!
