javac -d bin -sourcepath src -cp JDA-3.0.BETA2_143.jar:JDA-3.0.BETA2_143-withDependencies.jar:jsoup-1.8.1.jar:json-simple-1.1.1.jar:jsch-0.1.54.jar:org.eclipse.jgit-4.6.0.201612231935-r.jar:slf4j-api-1.7.24.jar src/main/DiscordBot.java
echo "Compile success, now running"
java -cp bin:JDA-3.0.BETA2_143.jar:JDA-3.0.BETA2_143-withDependencies.jar:jsoup-1.8.1.jar:json-simple-1.1.1.jar:json-simple-1.1.1.jar:jsch-0.1.54.jar:org.eclipse.jgit-4.6.0.201612231935-r.jar:slf4j-api-1.7.24.jar main/DiscordBot