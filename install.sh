#!/bin/bash

mvn clean install -Dmaven.test.skip
cp target/*.jar ~/.m2/repository/com/gisgraphy/gisgraphoid/1.0/
