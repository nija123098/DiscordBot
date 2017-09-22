# DiscordBot
##### AKA Evelyn
A multipurpose Discord bot with an emphasis on music, moderation, economy, and much more.

## Technology
DiscordBot uses the Java reflection API, this is rumored to be incredibly slow, it's not. There are many things wrong with the JVM, reflection is no longer one of them.

DiscordBot utilizes many APIs in an effort to provide as many **meaningful** integrations as possible. While this includes mostly data collection from other services, it does not necessarily mean it is limited to data collection and that data collection is all it will utilize going into the future.

SQL (MariaDB) Utilized for large data storage, though everything is stored as strings as many values are XML, this is in the queue to be optimized.

Caching is done based on shards. Shards are Discord's way splitting connections to allow large bots to be split between multiple servers and so that large bots do not destroy Discord's servers with large connections. This is done in a way such that guilds and dependent objects are provided to a single connection during it's connection time, all other types of objects can not be guaranteed to only be used in relation to a single guild. As such no value which is not dependent on guild can not be cached normally as other instances of this program may change the value making any potential normal method of caching things give the wrong value.

## Design
The project was made with several things in mind.

1. The server running the program is sufficient and is not short on by memory.

2. That the core should do most of the work allowing for all other code to be as simple as possible.

3. That other programmers would work on features of the bot which fit into the core, either by commands or normally running services and be able to do so in a simple manner due to the core's ability to do most of the work.

4. That the bot would not be limited by access issues in method invocation and would instead be supplied as requested.

5. That the core was removable from most features of the bot in order to allow for the possibility of features being jar loaded in while the bot continued to run. (This has been put on a very far back burner as login speed has been increased significantly and it is no longer a goal to make a general framework for Discord bots)



## Getting Started

### Music
DiscordBot plays music. The @Evelyn play command can be used to search for music from youtube and users are able to specify a Youtube video, Soundcloud track, or Twitch stream by URL.

### Moderation
DiscordBot has several advanced moderation features such as message filtering and a custom permissions system to let you grant powers to specific roles per channel and in the whole server.

### Cookie Economy
Cookies are stored in two ways; personal bank and guild bank.  Guild bank cookies can only be used in that guild, for purchasing or renting roles. Personal banks don't decrease when you use your guild bank cookies and are instead used for other features to be released Soon™.

### Editing commands:
If a typo is made in a command and is reacted with a grey question mark, simply edit the command and it will be reevaluated!

### Reactions:
Many commands/features can use reactions. Some commands can only be activated by reactions, for example "star board" and "translate".  Other commands can use reactions as well as the usual text, appropriate use can be found by doing @Evelyn help <command>

### Configuration:
Most features are configurable, including channels, roles, users, just try @Evelyn config with any indication of the previously mentioned objects.

### DM commands:
All commands in DMs are recognized, and is the easiest place to set up user configurations like preferred language.

### Translation:
DiscordBot has translation capability. Once user_language has been set any message can be reacted to with a speech balloon and that message's translation will be DMed.

### Reputation:
To give someone a reputation point react to a message they sent with a thumbs up.
