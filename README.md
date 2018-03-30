# DiscordBot
##### AKA Evelyn
A multipurpose Discord bot with an emphasis on music, moderation, economy, and more.


## Design
The project was made with several things in mind.

1. Optimization is a priority, however due to the open source nature of the project sometimes readability is preferred over performance.

2. That the core of the bot should do most of the work allowing for all other code to be as simple as possible.

3. That non-core programmers could easily work on features of the bot which fit into the core, either by commands or normally running services and be able to do so in a simple manner due to the core's ability to do the most complex work.

4. That no process modifying Discord objects shall be asynchronous without the best check for fail as to ensure that if a command is reported as completed it is completed successfully.

5. That the core was removable from most features of the bot in order to allow for the possibility of features being jar loaded while the bot continues to run in the near future.


## Technology
DiscordBot makes use of several technologies to function optimally and well.

The Java Reflection API is used for command invocation and event dispatching.  While rumored to be incredibly slow, it has been greatly optimized.

Neural networks are used in some games when playing against the bot.  The implementation is a generational network and written in such a way that XStream can properly Serialize it without messing up references, as done when an object oriented approach is taken.  This also exemplifies the simplicity of basic neural networks.

Many APIs are utilized in an effort to provide as many meaningful integrations as possible.  While this includes mostly data collection from other services, it does not necessarily mean it is limited to data collection and that data collection is all it will utilize going into the future.

MariaDB is utilized for data storage.  Unless a direct type is present in MariaDB, XML generated through XStream is used to store values to make values in the DB human readable and easily deserializable.


## Getting Started

#### Setup
Use `@Evelyn prefix myPrefix` to change her prefix.  She will always respond by mention.  The help command displays all help on commands available.

#### Music
DiscordBot plays music. The @Evelyn play command can be used to search for music from youtube and users are able to specify a Youtube video, Soundcloud track, or Twitch stream by URL.

#### Moderation
DiscordBot has several advanced moderation features such as message filtering and a custom permissions system to let you grant powers to specific roles per channel and in the whole server.

#### Cookie Economy
Cookies are stored in two ways; personal bank and guild bank.  Guild bank cookies can only be used in that guild, for purchasing or renting roles.  Personal banks don't decrease when you use your guild bank cookies and are instead used for other features to be released Soon™.

#### Editing commands:
If a typo is made in a command and is reacted with a grey question mark, simply edit the command and it will be reevaluated!

#### Reactions:
Many commands/features can use reactions. Some commands can only be activated by reactions, for example "star board" and "translate".  Other commands can use reactions as well as the usual text, appropriate use can be found by doing @Evelyn help <command>

#### Configuration:
Most features are configurable, including channels, roles, users, just try @Evelyn config with any indication of the previously mentioned objects.

#### DM commands:
All commands in DMs are recognized, and is the easiest place to set up user configurations like preferred language.

#### Translation:
DiscordBot has translation capability.  Once user_language has been set any message can be reacted to with a speech balloon and that message's translation will be DMed.

#### Reputation:
To give someone a reputation point react to a message they sent with a thumbs up.
