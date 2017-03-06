##New error at 6/3 - 20:2:38
Message was: In getPrefix
Id: Here's string Id: --247301726271438848--
Unexpected character (<) at position 0.
	at org.json.simple.parser.Yylex.yylex(Yylex.java:610)
	at org.json.simple.parser.JSONParser.nextToken(JSONParser.java:269)
	at org.json.simple.parser.JSONParser.parse(JSONParser.java:118)
	at org.json.simple.parser.JSONParser.parse(JSONParser.java:92)
	at Main.DiscordBot.getPrefix(DiscordBot.java:686)
	at Main.DiscordBot.onMessageReceived(DiscordBot.java:122)
	at net.dv8tion.jda.core.hooks.ListenerAdapter.onEvent(ListenerAdapter.java:326)
	at net.dv8tion.jda.core.hooks.InterfacedEventManager.handle(InterfacedEventManager.java:84)
	at net.dv8tion.jda.core.handle.MessageCreateHandler.handleDefaultMessage(MessageCreateHandler.java:128)
	at net.dv8tion.jda.core.handle.MessageCreateHandler.handleInternally(MessageCreateHandler.java:50)
	at net.dv8tion.jda.core.handle.SocketHandler.handle(SocketHandler.java:38)
	at net.dv8tion.jda.core.requests.WebSocketClient.handleEvent(WebSocketClient.java:722)
	at net.dv8tion.jda.core.requests.WebSocketClient.onTextMessage(WebSocketClient.java:460)
	at com.neovisionaries.ws.client.ListenerManager.callOnTextMessage(ListenerManager.java:352)
	at com.neovisionaries.ws.client.ReadingThread.callOnTextMessage(ReadingThread.java:262)
	at com.neovisionaries.ws.client.ReadingThread.callOnTextMessage(ReadingThread.java:240)
	at com.neovisionaries.ws.client.ReadingThread.handleTextFrame(ReadingThread.java:965)
	at com.neovisionaries.ws.client.ReadingThread.handleFrame(ReadingThread.java:748)
	at com.neovisionaries.ws.client.ReadingThread.main(ReadingThread.java:110)
	at com.neovisionaries.ws.client.ReadingThread.run(ReadingThread.java:66)

---------------

##New error at 6/3 - 20:2:29
Message was: In getPrefix
Id: Here's string Id: --247301726271438848--
Unexpected character (<) at position 0.
	at org.json.simple.parser.Yylex.yylex(Yylex.java:610)
	at org.json.simple.parser.JSONParser.nextToken(JSONParser.java:269)
	at org.json.simple.parser.JSONParser.parse(JSONParser.java:118)
	at org.json.simple.parser.JSONParser.parse(JSONParser.java:92)
	at Main.DiscordBot.getPrefix(DiscordBot.java:686)
	at Main.DiscordBot.onMessageReceived(DiscordBot.java:122)
	at net.dv8tion.jda.core.hooks.ListenerAdapter.onEvent(ListenerAdapter.java:326)
	at net.dv8tion.jda.core.hooks.InterfacedEventManager.handle(InterfacedEventManager.java:84)
	at net.dv8tion.jda.core.handle.MessageCreateHandler.handleDefaultMessage(MessageCreateHandler.java:128)
	at net.dv8tion.jda.core.handle.MessageCreateHandler.handleInternally(MessageCreateHandler.java:50)
	at net.dv8tion.jda.core.handle.SocketHandler.handle(SocketHandler.java:38)
	at net.dv8tion.jda.core.requests.WebSocketClient.handleEvent(WebSocketClient.java:722)
	at net.dv8tion.jda.core.requests.WebSocketClient.onTextMessage(WebSocketClient.java:460)
	at com.neovisionaries.ws.client.ListenerManager.callOnTextMessage(ListenerManager.java:352)
	at com.neovisionaries.ws.client.ReadingThread.callOnTextMessage(ReadingThread.java:262)
	at com.neovisionaries.ws.client.ReadingThread.callOnTextMessage(ReadingThread.java:240)
	at com.neovisionaries.ws.client.ReadingThread.handleTextFrame(ReadingThread.java:965)
	at com.neovisionaries.ws.client.ReadingThread.handleFrame(ReadingThread.java:748)
	at com.neovisionaries.ws.client.ReadingThread.main(ReadingThread.java:110)
	at com.neovisionaries.ws.client.ReadingThread.run(ReadingThread.java:66)

---------------

##New error at 6/3 - 18:54:25
Message was: #gif
Id: 288368740158930944
java.lang.StringIndexOutOfBoundsException: String index out of range: -1
	at java.lang.String.substring(String.java:1967)
	at Main.DiscordBot.gif(DiscordBot.java:513)
	at Main.DiscordBot.onMessageReceived(DiscordBot.java:139)
	at net.dv8tion.jda.core.hooks.ListenerAdapter.onEvent(ListenerAdapter.java:326)
	at net.dv8tion.jda.core.hooks.InterfacedEventManager.handle(InterfacedEventManager.java:84)
	at net.dv8tion.jda.core.handle.MessageCreateHandler.handleDefaultMessage(MessageCreateHandler.java:128)
	at net.dv8tion.jda.core.handle.MessageCreateHandler.handleInternally(MessageCreateHandler.java:50)
	at net.dv8tion.jda.core.handle.SocketHandler.handle(SocketHandler.java:38)
	at net.dv8tion.jda.core.requests.WebSocketClient.handleEvent(WebSocketClient.java:722)
	at net.dv8tion.jda.core.requests.WebSocketClient.onTextMessage(WebSocketClient.java:460)
	at com.neovisionaries.ws.client.ListenerManager.callOnTextMessage(ListenerManager.java:352)
	at com.neovisionaries.ws.client.ReadingThread.callOnTextMessage(ReadingThread.java:262)
	at com.neovisionaries.ws.client.ReadingThread.callOnTextMessage(ReadingThread.java:240)
	at com.neovisionaries.ws.client.ReadingThread.handleTextFrame(ReadingThread.java:965)
	at com.neovisionaries.ws.client.ReadingThread.handleFrame(ReadingThread.java:748)
	at com.neovisionaries.ws.client.ReadingThread.main(ReadingThread.java:110)
	at com.neovisionaries.ws.client.ReadingThread.run(ReadingThread.java:66)

---------------

##New error at 6/3 - 18:17:17
Message was: ;gif woops
Id: 288359389310615563
java.lang.IllegalStateException: Cannot delete another User's messages in a Group or PrivateChannel.
	at net.dv8tion.jda.core.entities.impl.MessageImpl.delete(MessageImpl.java:470)
	at Main.DiscordBot.gif(DiscordBot.java:538)
	at Main.DiscordBot.onMessageReceived(DiscordBot.java:159)
	at net.dv8tion.jda.core.hooks.ListenerAdapter.onEvent(ListenerAdapter.java:326)
	at net.dv8tion.jda.core.hooks.InterfacedEventManager.handle(InterfacedEventManager.java:84)
	at net.dv8tion.jda.core.handle.MessageCreateHandler.handleDefaultMessage(MessageCreateHandler.java:128)
	at net.dv8tion.jda.core.handle.MessageCreateHandler.handleInternally(MessageCreateHandler.java:50)
	at net.dv8tion.jda.core.handle.SocketHandler.handle(SocketHandler.java:38)
	at net.dv8tion.jda.core.requests.WebSocketClient.handleEvent(WebSocketClient.java:722)
	at net.dv8tion.jda.core.requests.WebSocketClient.onTextMessage(WebSocketClient.java:460)
	at com.neovisionaries.ws.client.ListenerManager.callOnTextMessage(ListenerManager.java:352)
	at com.neovisionaries.ws.client.ReadingThread.callOnTextMessage(ReadingThread.java:262)
	at com.neovisionaries.ws.client.ReadingThread.callOnTextMessage(ReadingThread.java:240)
	at com.neovisionaries.ws.client.ReadingThread.handleTextFrame(ReadingThread.java:965)
	at com.neovisionaries.ws.client.ReadingThread.handleFrame(ReadingThread.java:748)
	at com.neovisionaries.ws.client.ReadingThread.main(ReadingThread.java:110)
	at com.neovisionaries.ws.client.ReadingThread.run(ReadingThread.java:66)

---------------

##New error at 6/3 - 18:16:58
Message was: ;gif oops i did it again
Id: 288359308662407168
java.lang.IllegalStateException: Cannot delete another User's messages in a Group or PrivateChannel.
	at net.dv8tion.jda.core.entities.impl.MessageImpl.delete(MessageImpl.java:470)
	at Main.DiscordBot.gif(DiscordBot.java:538)
	at Main.DiscordBot.onMessageReceived(DiscordBot.java:159)
	at net.dv8tion.jda.core.hooks.ListenerAdapter.onEvent(ListenerAdapter.java:326)
	at net.dv8tion.jda.core.hooks.InterfacedEventManager.handle(InterfacedEventManager.java:84)
	at net.dv8tion.jda.core.handle.MessageCreateHandler.handleDefaultMessage(MessageCreateHandler.java:128)
	at net.dv8tion.jda.core.handle.MessageCreateHandler.handleInternally(MessageCreateHandler.java:50)
	at net.dv8tion.jda.core.handle.SocketHandler.handle(SocketHandler.java:38)
	at net.dv8tion.jda.core.requests.WebSocketClient.handleEvent(WebSocketClient.java:722)
	at net.dv8tion.jda.core.requests.WebSocketClient.onTextMessage(WebSocketClient.java:460)
	at com.neovisionaries.ws.client.ListenerManager.callOnTextMessage(ListenerManager.java:352)
	at com.neovisionaries.ws.client.ReadingThread.callOnTextMessage(ReadingThread.java:262)
	at com.neovisionaries.ws.client.ReadingThread.callOnTextMessage(ReadingThread.java:240)
	at com.neovisionaries.ws.client.ReadingThread.handleTextFrame(ReadingThread.java:965)
	at com.neovisionaries.ws.client.ReadingThread.handleFrame(ReadingThread.java:748)
	at com.neovisionaries.ws.client.ReadingThread.main(ReadingThread.java:110)
	at com.neovisionaries.ws.client.ReadingThread.run(ReadingThread.java:66)

---------------

##New error at 6/3 - 12:37:14
Message was: ==https://www.reddit.com/r/imgoingtohellforthis/comments/5xidvg/picking_up_girls/?st=izwz6jzh&sh=51454908==
java.lang.StringIndexOutOfBoundsException: String index out of range: -21
	at java.lang.String.substring(String.java:1967)
	at Main.DiscordBot.reddit(DiscordBot.java:368)
	at Main.DiscordBot.onMessageReceived(DiscordBot.java:137)
	at net.dv8tion.jda.core.hooks.ListenerAdapter.onEvent(ListenerAdapter.java:326)
	at net.dv8tion.jda.core.hooks.InterfacedEventManager.handle(InterfacedEventManager.java:84)
	at net.dv8tion.jda.core.handle.MessageCreateHandler.handleDefaultMessage(MessageCreateHandler.java:128)
	at net.dv8tion.jda.core.handle.MessageCreateHandler.handleInternally(MessageCreateHandler.java:50)
	at net.dv8tion.jda.core.handle.SocketHandler.handle(SocketHandler.java:38)
	at net.dv8tion.jda.core.requests.WebSocketClient.handleEvent(WebSocketClient.java:722)
	at net.dv8tion.jda.core.requests.WebSocketClient.onTextMessage(WebSocketClient.java:460)
	at com.neovisionaries.ws.client.ListenerManager.callOnTextMessage(ListenerManager.java:352)
	at com.neovisionaries.ws.client.ReadingThread.callOnTextMessage(ReadingThread.java:262)
	at com.neovisionaries.ws.client.ReadingThread.callOnTextMessage(ReadingThread.java:240)
	at com.neovisionaries.ws.client.ReadingThread.handleTextFrame(ReadingThread.java:965)
	at com.neovisionaries.ws.client.ReadingThread.handleFrame(ReadingThread.java:748)
	at com.neovisionaries.ws.client.ReadingThread.main(ReadingThread.java:110)
	at com.neovisionaries.ws.client.ReadingThread.run(ReadingThread.java:66)

---------------

##New error at 6/3 - 12:35:41
Message was: ==https://www.reddit.com/r/imgoingtohellforthis/comments/5xidvg/picking_up_girls/?st=izwz6jzh&sh=51454908==
java.lang.StringIndexOutOfBoundsException: String index out of range: -21
	at java.lang.String.substring(String.java:1967)
	at Main.DiscordBot.reddit(DiscordBot.java:368)
	at Main.DiscordBot.onMessageReceived(DiscordBot.java:137)
	at net.dv8tion.jda.core.hooks.ListenerAdapter.onEvent(ListenerAdapter.java:326)
	at net.dv8tion.jda.core.hooks.InterfacedEventManager.handle(InterfacedEventManager.java:84)
	at net.dv8tion.jda.core.handle.MessageCreateHandler.handleDefaultMessage(MessageCreateHandler.java:128)
	at net.dv8tion.jda.core.handle.MessageCreateHandler.handleInternally(MessageCreateHandler.java:50)
	at net.dv8tion.jda.core.handle.SocketHandler.handle(SocketHandler.java:38)
	at net.dv8tion.jda.core.requests.WebSocketClient.handleEvent(WebSocketClient.java:722)
	at net.dv8tion.jda.core.requests.WebSocketClient.onTextMessage(WebSocketClient.java:460)
	at com.neovisionaries.ws.client.ListenerManager.callOnTextMessage(ListenerManager.java:352)
	at com.neovisionaries.ws.client.ReadingThread.callOnTextMessage(ReadingThread.java:262)
	at com.neovisionaries.ws.client.ReadingThread.callOnTextMessage(ReadingThread.java:240)
	at com.neovisionaries.ws.client.ReadingThread.handleTextFrame(ReadingThread.java:965)
	at com.neovisionaries.ws.client.ReadingThread.handleFrame(ReadingThread.java:748)
	at com.neovisionaries.ws.client.ReadingThread.main(ReadingThread.java:110)
	at com.neovisionaries.ws.client.ReadingThread.run(ReadingThread.java:66)

---------------

##New error at 6/3 - 12:34:26
Message was: ==https://www.reddit.com/r/imgoingtohellforthis/comments/5xidvg/picking_up_girls/?st=izwz6jzh&sh=51454908==
java.lang.StringIndexOutOfBoundsException: String index out of range: -21
	at java.lang.String.substring(String.java:1967)
	at Main.DiscordBot.reddit(DiscordBot.java:368)
	at Main.DiscordBot.onMessageReceived(DiscordBot.java:137)
	at net.dv8tion.jda.core.hooks.ListenerAdapter.onEvent(ListenerAdapter.java:326)
	at net.dv8tion.jda.core.hooks.InterfacedEventManager.handle(InterfacedEventManager.java:84)
	at net.dv8tion.jda.core.handle.MessageCreateHandler.handleDefaultMessage(MessageCreateHandler.java:128)
	at net.dv8tion.jda.core.handle.MessageCreateHandler.handleInternally(MessageCreateHandler.java:50)
	at net.dv8tion.jda.core.handle.SocketHandler.handle(SocketHandler.java:38)
	at net.dv8tion.jda.core.requests.WebSocketClient.handleEvent(WebSocketClient.java:722)
	at net.dv8tion.jda.core.requests.WebSocketClient.onTextMessage(WebSocketClient.java:460)
	at com.neovisionaries.ws.client.ListenerManager.callOnTextMessage(ListenerManager.java:352)
	at com.neovisionaries.ws.client.ReadingThread.callOnTextMessage(ReadingThread.java:262)
	at com.neovisionaries.ws.client.ReadingThread.callOnTextMessage(ReadingThread.java:240)
	at com.neovisionaries.ws.client.ReadingThread.handleTextFrame(ReadingThread.java:965)
	at com.neovisionaries.ws.client.ReadingThread.handleFrame(ReadingThread.java:748)
	at com.neovisionaries.ws.client.ReadingThread.main(ReadingThread.java:110)
	at com.neovisionaries.ws.client.ReadingThread.run(ReadingThread.java:66)

---------------

##New error at 6/3 - 12:25:44
 java.lang.StringIndexOutOfBoundsException: String index out of range: -21
	at java.lang.String.substring(String.java:1967)
	at Main.DiscordBot.reddit(DiscordBot.java:368)
	at Main.DiscordBot.onMessageReceived(DiscordBot.java:137)
	at net.dv8tion.jda.core.hooks.ListenerAdapter.onEvent(ListenerAdapter.java:326)
	at net.dv8tion.jda.core.hooks.InterfacedEventManager.handle(InterfacedEventManager.java:84)
	at net.dv8tion.jda.core.handle.MessageCreateHandler.handleDefaultMessage(MessageCreateHandler.java:128)
	at net.dv8tion.jda.core.handle.MessageCreateHandler.handleInternally(MessageCreateHandler.java:50)
	at net.dv8tion.jda.core.handle.SocketHandler.handle(SocketHandler.java:38)
	at net.dv8tion.jda.core.requests.WebSocketClient.handleEvent(WebSocketClient.java:722)
	at net.dv8tion.jda.core.requests.WebSocketClient.onTextMessage(WebSocketClient.java:460)
	at com.neovisionaries.ws.client.ListenerManager.callOnTextMessage(ListenerManager.java:352)
	at com.neovisionaries.ws.client.ReadingThread.callOnTextMessage(ReadingThread.java:262)
	at com.neovisionaries.ws.client.ReadingThread.callOnTextMessage(ReadingThread.java:240)
	at com.neovisionaries.ws.client.ReadingThread.handleTextFrame(ReadingThread.java:965)
	at com.neovisionaries.ws.client.ReadingThread.handleFrame(ReadingThread.java:748)
	at com.neovisionaries.ws.client.ReadingThread.main(ReadingThread.java:110)
	at com.neovisionaries.ws.client.ReadingThread.run(ReadingThread.java:66)

---------------

##New error at 6/3 - 12:23:38
 java.lang.StringIndexOutOfBoundsException: String index out of range: -21
	at java.lang.String.substring(String.java:1967)
	at Main.DiscordBot.reddit(DiscordBot.java:368)
	at Main.DiscordBot.onMessageReceived(DiscordBot.java:137)
	at net.dv8tion.jda.core.hooks.ListenerAdapter.onEvent(ListenerAdapter.java:326)
	at net.dv8tion.jda.core.hooks.InterfacedEventManager.handle(InterfacedEventManager.java:84)
	at net.dv8tion.jda.core.handle.MessageCreateHandler.handleDefaultMessage(MessageCreateHandler.java:128)
	at net.dv8tion.jda.core.handle.MessageCreateHandler.handleInternally(MessageCreateHandler.java:50)
	at net.dv8tion.jda.core.handle.SocketHandler.handle(SocketHandler.java:38)
	at net.dv8tion.jda.core.requests.WebSocketClient.handleEvent(WebSocketClient.java:722)
	at net.dv8tion.jda.core.requests.WebSocketClient.onTextMessage(WebSocketClient.java:460)
	at com.neovisionaries.ws.client.ListenerManager.callOnTextMessage(ListenerManager.java:352)
	at com.neovisionaries.ws.client.ReadingThread.callOnTextMessage(ReadingThread.java:262)
	at com.neovisionaries.ws.client.ReadingThread.callOnTextMessage(ReadingThread.java:240)
	at com.neovisionaries.ws.client.ReadingThread.handleTextFrame(ReadingThread.java:965)
	at com.neovisionaries.ws.client.ReadingThread.handleFrame(ReadingThread.java:748)
	at com.neovisionaries.ws.client.ReadingThread.main(ReadingThread.java:110)
	at com.neovisionaries.ws.client.ReadingThread.run(ReadingThread.java:66)

---------------

##New error at 6/3 - 0:20:46
 java.lang.StringIndexOutOfBoundsException: String index out of range: -21
	at java.lang.String.substring(String.java:1967)
	at Main.DiscordBot.reddit(DiscordBot.java:338)
	at Main.DiscordBot.onMessageReceived(DiscordBot.java:107)
	at net.dv8tion.jda.core.hooks.ListenerAdapter.onEvent(ListenerAdapter.java:326)
	at net.dv8tion.jda.core.hooks.InterfacedEventManager.handle(InterfacedEventManager.java:84)
	at net.dv8tion.jda.core.handle.MessageCreateHandler.handleDefaultMessage(MessageCreateHandler.java:128)
	at net.dv8tion.jda.core.handle.MessageCreateHandler.handleInternally(MessageCreateHandler.java:50)
	at net.dv8tion.jda.core.handle.SocketHandler.handle(SocketHandler.java:38)
	at net.dv8tion.jda.core.requests.WebSocketClient.handleEvent(WebSocketClient.java:722)
	at net.dv8tion.jda.core.requests.WebSocketClient.onTextMessage(WebSocketClient.java:460)
	at com.neovisionaries.ws.client.ListenerManager.callOnTextMessage(ListenerManager.java:352)
	at com.neovisionaries.ws.client.ReadingThread.callOnTextMessage(ReadingThread.java:262)
	at com.neovisionaries.ws.client.ReadingThread.callOnTextMessage(ReadingThread.java:240)
	at com.neovisionaries.ws.client.ReadingThread.handleTextFrame(ReadingThread.java:965)
	at com.neovisionaries.ws.client.ReadingThread.handleFrame(ReadingThread.java:748)
	at com.neovisionaries.ws.client.ReadingThread.main(ReadingThread.java:110)
	at com.neovisionaries.ws.client.ReadingThread.run(ReadingThread.java:66)

---------------

##New error at 6/3 - 0:18:9
 java.lang.StringIndexOutOfBoundsException: String index out of range: -21
	at java.lang.String.substring(String.java:1967)
	at Main.DiscordBot.reddit(DiscordBot.java:338)
	at Main.DiscordBot.onMessageReceived(DiscordBot.java:107)
	at net.dv8tion.jda.core.hooks.ListenerAdapter.onEvent(ListenerAdapter.java:326)
	at net.dv8tion.jda.core.hooks.InterfacedEventManager.handle(InterfacedEventManager.java:84)
	at net.dv8tion.jda.core.handle.MessageCreateHandler.handleDefaultMessage(MessageCreateHandler.java:128)
	at net.dv8tion.jda.core.handle.MessageCreateHandler.handleInternally(MessageCreateHandler.java:50)
	at net.dv8tion.jda.core.handle.SocketHandler.handle(SocketHandler.java:38)
	at net.dv8tion.jda.core.requests.WebSocketClient.handleEvent(WebSocketClient.java:722)
	at net.dv8tion.jda.core.requests.WebSocketClient.onTextMessage(WebSocketClient.java:460)
	at com.neovisionaries.ws.client.ListenerManager.callOnTextMessage(ListenerManager.java:352)
	at com.neovisionaries.ws.client.ReadingThread.callOnTextMessage(ReadingThread.java:262)
	at com.neovisionaries.ws.client.ReadingThread.callOnTextMessage(ReadingThread.java:240)
	at com.neovisionaries.ws.client.ReadingThread.handleTextFrame(ReadingThread.java:965)
	at com.neovisionaries.ws.client.ReadingThread.handleFrame(ReadingThread.java:748)
	at com.neovisionaries.ws.client.ReadingThread.main(ReadingThread.java:110)
	at com.neovisionaries.ws.client.ReadingThread.run(ReadingThread.java:66)

---------------

##New error at 5/3 - 23:22:2
 java.lang.NullPointerException
	at Main.LoggExceptions.main(LoggExceptions.java:18)

---------------

##New error at 5/3 - 23:21:39
 java.lang.NullPointerException
	at Main.LoggExceptions.main(LoggExceptions.java:22)

---------------

##New error at 5/3 - 21:20:39
 javax.security.auth.login.LoginException: Provided token was null or empty!
	at net.dv8tion.jda.core.entities.impl.JDAImpl.login(JDAImpl.java:107)
	at net.dv8tion.jda.core.JDABuilder.buildAsync(JDABuilder.java:417)
	at net.dv8tion.jda.core.JDABuilder.buildBlocking(JDABuilder.java:440)
	at Main.Test.main(Test.java:26)

---------------

##New error at 5/3 - 21:17:43
 java.lang.NullPointerException
	at Main.LoggExceptions.main(LoggExceptions.java:18)

---------------

##New error at 5/3 - 21:17:42
 java.lang.NullPointerException
	at Main.LoggExceptions.main(LoggExceptions.java:18)

---------------

##New error at 5/3 - 21:17:41
 java.lang.NullPointerException
	at Main.LoggExceptions.main(LoggExceptions.java:18)

---------------

##New error at 5/3 - 21:17:40
 java.lang.NullPointerException
	at Main.LoggExceptions.main(LoggExceptions.java:18)

---------------

