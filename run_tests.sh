#!/bin/bash
cd "$(dirname "$0")"
mkdir -p bin
javac -d bin $(find src -name "*.java")
javac -cp bin -d bin test/EventFlowTests.java
java -cp bin EventFlowTests
