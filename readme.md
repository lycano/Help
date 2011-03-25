Help
===========

jascotty2 Changelog
-----------------

Version 0.2.3 :

* fixed a null pointer exception in plugin help


Version 0.2.2 :

* various null pointer fixes (could have nullpointerException on load without)
* improved help listing
* added console help support


Compilation
-----------

For compilation, you need :

* JDK 6 (Sun JDK or OpenJDK Highlight recommanded)
* Install [Maven 3](http://maven.apache.org/download.html)
* Check out and install [Bukkit](http://github.com/Bukkit/Bukkit) and [CraftBukkit](http://github.com/Bukkit/CraftBukkit)
* Check out this repo 
* Get the Permissions plugin jar and run `mvn install:install-file -Dfile=Permissions.jar -DgroupId=org.bukkit -DartifactId=permissions -Dversion=2.0 -Dpackaging=jar`
* `mvn clean install`
