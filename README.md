# gatling-tests

## This is a set of instructions for testing apps I have built to compare web frameworks.
=
###Required Equipment if testing with Apache
* Router
* 2 network cables
* 2 laptops

=
###Install java jdk on the laptop running the tests

**On Linux**

```
sudo apt-get install default-jdk
```

**On OSX**

Go to the following link, accept the licence and download the mac version and install.
https://jdk7.java.net/download.html

=
###Changing The Limit Your OS Places On Virtual Users on the laptop running the tests

**ON OSX**

Open a terminal and input the following commands to set the new limit to 3000000 files.

```
$ sudo sysctl -w kern.maxfilesperproc=3000000
$ sudo sysctl -w kern.maxfiles=3000000
$ sudo sysctl -w net.inet.ip.portrange.first=1024
```

**On Linux**

When using the testing tool Tsung, in its configuration file i was able to set the number of users / sessions, but when i ran this it would limit to a set number. On further investigation your OS limits the number of open file handles during normal operation. This needs to be tweaked in order to open many new sockets and achieve heavy load.

To permanently set the soft and hard values for all users of the system to allow for up to 3000000 open files. Edit /etc/security/limits.conf and append the following two lines:

```
*       soft    nofile  3000000
*       hard    nofile  3000000
```

Save the file and start a new session so that the limits take effect. You can now verify by typing the command:

```
ulimit -a 
```

This checks that the limits are correctly set. For Debian and Ubuntu, you should enable PAM user limits. To do so, add:

```
session required pam_limits.so
```

into the following files:
* /etc/pam.d/common-session
* /etc/pam.d/common-session-noninteractive (if the file exists)
* /etc/pam.d/sshd (if you access the machine via SSH)

Also, if accessing the machine via SSH, be sure to have:

```
UseLogin yes
```

set in the file: 
* /etc/ssh/sshd_config

For more ports for testing, you may want to do the following:

```
sudo sysctl -w net.ipv4.ip_local_port_range="1025 65535"
```

**increase the maximum number of possible open file descriptors**

```
echo 3000000 | sudo tee /proc/sys/fs/nr_open
echo 3000000 | sudo tee /proc/sys/fs/file-max
```

=
###Install Git on the laptop being tested

**on Linux**

```
sudo apt-get install git
```

**On OSX**

Download the file here: http://git-scm.com/download/mac and follow the instructions.

=
###Install Gatling on the laptop running the tests

Download the zip of Gatling from their website then unzip it to the Documents folder. http://gatling.io/#/download

Then change directory to the Gatling folder and delete the user-files folder:

```
rm -rf user-files
```

Depending on the testing platform you you want to run on, follow the relevant section below, eg: Apache or without Apache.

The reason for the different tests with Apache is that when you are not using Apache you need the port numbers in the base urls, whereas when using Apache the base url will just be the ip address of the second laptop.

First get the relevent application running in development or production mode depending on the results you want to replicate.
* Ruby on Rails blog app: https://github.com/archerydwd/ror-blog
* Ruby on Rails sakila app: https://github.com/archerydwd/ror_sakila
* Flask blog app: https://github.com/archerydwd/flask_blog
* Flask sakila app: https://github.com/archerydwd/flask_sakila

Chicago boss ships with their own high performance web server, it does not require Apache, Nginx, haproxy. 
Therefore I decided to test Chicago Boss as it comes with its own web server. As incorporating apache would slow it down unlike rails and flask.

* Chicago Boss blog app: https://github.com/archerydwd/cb_blog
* Chicago Boss sakila app: https://github.com/archerydwd/cb_sakila

=
###Getting the Gatling Tests for Apache

Now we need to replace the folder that we just deleted from above:

```
git clone https://github.com/archerydwd/gatling-tests.git
cd gatling-tests
mv user-files ../
cd ..
rm -rf gatling-tests
```

What we just done:
* cloned my tests from github
* changed directory into the tests
* mv the folder with the actual tests back to replace the folder we deleted
* changed directory back to gatling
* deleted the cloned github folder

In the user-files directory there is a folder called simulations containing blog and sakila folders. Inside these folders are tests for Chicago Boss, Flask & Ruby on Rails versions of the blog and sakila apps that we are going to be testing.

=
###Getting the Gatling Tests for Without Apache

Now we need to replace the folder that we just deleted from above:

```
git clone https://github.com/archerydwd/gatling-tests-with-port-numbers.git
cd gatling-tests-with-port-numbers
mv user-files ../
cd ..
rm -rf gatling-tests-with-port-numbers
```

What we just done:
* cloned my tests from github
* changed directory into the tests
* mv the folder with the actual tests back to replace the folder we deleted
* changed directory back to gatling
* deleted the cloned github folder

In the user-files directory there is a folder called simulations containing blog and sakila folders. Inside these folders are tests for Chicago Boss, Flask & Ruby on Rails versions of the blog and sakila apps that we are going to be testing.

=
###Running the tests

If you are using the Apache set up, then you firstly need to wire up the laptops to the router and turn off the wifi on both laptops. On each laptop run the command ifconfig to determine that laptops ip address, for instance my laptop 1 = 192.168.1.3 and laptop 2 = 192.168.1.2.. You can now open a terminal on laptop 1 and type:

```
ping 192.168.1.2
```

If you start getting responses back then we are good to go.

In the user-files/simulations/ directory, open the one that you wish to use, say for this we will use: Flask_blog_1000_users.scala and change the base-url from 127.0.0.1:5000 to 192.168.1.2 Note we are not using a port number as this is an apache test. Save the file.

Enable the virtual host (site) on laptop 2:

```
sudo a2ensite flask_blog.conf
sudo etc/init.d/apache2 reload
```

In your terminal change directory to the Gatling folder and then run:

```
./$GATLING_HOME/bin/gatling.sh
```

This will produce a menu for you to pick from, which will be populated with the list of tests in our user-files/simulations folder.
Pick one from the menu by entering the number associated with the test and hit enter, then hit enter again twice to select the defaults. 
Once it’s finished it will produce a file in the results folder and provide a path to it. Then I created the file structure and moved the results from the tests into their relevant folders:

If not using Apache:

```
results/
|___blog/
|   |___development/
|   |   |   iteration1/
|   |   |   iteration2/
|   |   |   iteration3/
|   |___production/
|   |   |   iteration1/
|   |   |   iteration2/
|   |   |   iteration3/
|___sakila/
    |___development/
    |   |   iteration1/
    |   |   iteration2/
    |   |   iteration3/
    |___production/
        |   iteration1/
        |   iteration2/
        |   iteration3/
```

 If using Apache:
 
```
results/
|___blog/
|   |   iteration1/
|   |   iteration2/
|   |   iteration3/
|___sakila/
    |   iteration1/
    |   iteration2/
    |   iteration3/
```




