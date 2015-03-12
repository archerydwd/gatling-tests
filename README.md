# gatling-tests

## This is a set of instructions for setting up Gatling on Ubuntu to test a series of applications in order to get a banchmark for certain web frameworks.

=
###Install java jdk
=

```
sudo apt-get install default-jdk
```

=
###Changing The Limit Your OS Places On Virtual Users
=

When using the testing tool Tsung, in its configuration file i was able to set the number of users / sessions, but when i ran this it would limit to a set number. On further investigation your OS limits the number of open file handles during normal operation. This needs to be tweaked in order to open many new sockets and achieve heavy load.

To permanently set the soft and hard values for all users of the system to allow for up to 65536 open files. Edit /etc/security/limits.conf and append the following two lines:

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
/etc/pam.d/common-session
/etc/pam.d/common-session-noninteractive (if the file exists)
/etc/pam.d/sshd (if you access the machine via SSH)

Also, if accessing the machine via SSH, be sure to have:

```
UseLogin yes
```

set in the file: 
/etc/ssh/sshd_config

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
###Install Git
=

```
sudo apt-get install git
```

=
###Install Gatling
=

Download the zip of Gatling from their website then unzip it to the Documents folder. http://gatling.io/#/download

Then change directory to the Gatling folder and delete the user-files folder:

```
rm -rf user-files
```

Now we need to replace the folder that we just deleted:

```
git clone https://github.com/archerydwd/gatling-tests.git
cd gatling-tests
mv user-files ../
cd ..
rm -rf gatling-tests
```

What we just done:
- cloned my tests from github
- changed directory into the tests
- mv the folder with the actual tests back to replace the folder we deleted
- changed directory back to gatling
- deleted the cloned github folder

=
###Change base url
=

In the user-files folder there is a folder called simulations containing blog and sakila folders. Inside these folders are tests for Chicago Boss, Flask & Ruby on Rails versions of the blog and sakila apps that we are going to be testing.

