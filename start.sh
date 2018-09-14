#!/bin/sh

echo "-------------------------------"
echo "---- Installing application ---"
echo "-------------------------------"

mvn clean install

mv target/camel-spring-boot-tuning-0.1.jar vagrant/ansible/camel-spring-boot-tuning.jar

echo "-------------------------------"
echo "---- Provisioning machines ----"
echo "-------------------------------"

ansible-galaxy install -r vagrant/ansible/requirements.yml --ignore-errors

cd vagrant

vagrant up app_default app_tuned --provider virtualbox &
vagrant up app_tuned --provider virtualbox &

