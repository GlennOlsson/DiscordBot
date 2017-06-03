# DiscordBot
A bot for the message app Discord, made in Java using JDA. Initial idea was a 
bot that transformed reddit links to posts, to the corresponding image/gif of 
the post, but it is expanding beyond that!

# Run it yourself
If you want to run the bot yourself, you can do that easily by downloading this
repository. If you have a compiler, like an IDE, just add the jar files to your
classpath, and then run **DiscordBot.java** in the **main** package. If you are 
running java through the terminal, just use the **compile.sh** and **run.sh** 
files, in the **Discord** directory. 
###For the program to work, you will need to set up some things first.
1. Create a file named **Secret.json** containing:

       {"oath":"YOUR_DISCORD_BOT_OATH_KEY"}
      
2. Set the path in the path string in **ReadWrite.java** in **backend** 
directory. Set the file path as the return value in the
 **getPath** method, under either
    
       if(System.getProperty("os.name").toLowerCase().contains("windows")){
    if you are using windows, or
       
       else if (System.getProperty("os.name").toLowerCase().contains("linux")) {
    if you are using linux. You could also add a similar if statement, if
    you are using another OS.

3. If you are using linux, you will have to create two files for logging,
**Print.md** and **Errorlog.md**. Add the path to **Print.md** in **Print.java**
in the **backend** directory, where the String path is defined. Add the path to
**Errorlog.md** in **ErrorLog.java** in the same directory, as the definition of
the String path.