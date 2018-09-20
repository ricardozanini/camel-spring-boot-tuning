#!/bin/sh

echo "---------------------------------"
echo "---- Destroying your machines ---"
echo "---------------------------------"

cd vagrant

vagrant destroy app_default app_tuned

