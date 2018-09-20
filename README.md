# Camel Spring Boot Tuning

This is the base repository for the Camel Spring Boot Tuning Guide blog post.

The goal of this lab is to have an environment ready for load test a simple REST Camel application deployed in two machines. The fist machine `app_default` is the application installed "as is", without any kind of tuning or fancy configuration. The second one, `app_tuned`, we have modified all the components from the architecture to achieve a more performatic goal. The architecture is illustrated below:

```
+-----------------------+        +---------------------------+      +------------------------------+
|                       |        |                           |      |                              |
|     Apache HTTPD      |        |   Camel Spring Boot App   |      |      Internal Mock Web       |
|     Reverse Proxy     +------->+     (Embedded Tomcat)     +----->+ (Python Simple HTTP Server)  |
|                       |        |                           |      |                              |
+-----------------------+        +---------------------------+      +------------------------------+
```

In general, the VM `app_tuned` performs almost 300% better:

![Transactions Per Second](https://raw.githubusercontent.com/ricardozanini/camel-spring-boot-tuning/master/docs/assets/tps.png)

The VM `app_default` doesn't handle all the requests (150 simultaneous users) during much time, since on our experiments the JVM always crashes.

Please read the blog post about what had been done in this lab regarding tuning. Note that is not that much, but people tend to neglitect the most obsvious tuning points all the time. I hope this can be helpful somehow. I love to write and hack configurations, if you'd like to talk about it, reach me out.

## Requirements

To run this lab, you're going to need:

1. Vagrant
    1. VirtualBox for Vagrant provider
    2. Vagrant Plugin [Hostmanager](https://www.vagrantup.com/docs/cli/plugin.html#plugin-install)
: `vagrant plugin install vagrant-hostmanager`
2. [VirtualBox](https://www.virtualbox.org/wiki/Downloads)
3. [Ansible](https://docs.ansible.com/ansible/latest/installation_guide/intro_installation.html)
4. [JMeter](https://jmeter.apache.org/download_jmeter.cgi) for load tests
5. [PCP Vector](https://rhelblog.redhat.com/2015/12/18/getting-started-using-performance-co-pilot-and-vector-for-browser-based-metric-visualizations/) to collect the VM metrics (optional)

## How to use

Having the requirements installed, run the script `start.sh` that will build the application and provision the machines used in this lab. The Ansible playbooks are going to install all software needed to have this lab set.

Then you can run the [JMeter Load Test](https://github.com/ricardozanini/camel-spring-boot-tuning/blob/master/jmeter/load_test.jmx) to see the results in place. You can `ssh` in the machines (`vagrant ssh app_tuned`) and play with the configurations to see if the application performs even better. If you do, please share your results.

After runing your tests, destroy your VMs by runing `./destroy.sh` in the project home.

### Generating JMeter Reports

1. Configure the `user.properties` on JMeter home according to the [dashboard documentation](http://jmeter.apache.org/usermanual/generating-dashboard.html)
2. Save the CSV result file on the `Aggregate Report` tab (`$PROJECT_HOME/jmeter/results.csv`)
3. Generate the report after performing the load test by running `jmeter -g jmeter/results.csv -o jmeter/results-output`.

### Viewing PCP Metrics

Both machines are provisioned with `pcp` installed and enabled. To view your metris during your load test, install Vector on your host and open two tabs, pointing each one to `app.local` and `app2.local`:

* http://localhost:44323/vector/index.html#/?host=localhost:44323&hostspec=app.local
* http://localhost:44323/vector/index.html#/?host=localhost:44323&hostspec=app2.local

Enjoy!