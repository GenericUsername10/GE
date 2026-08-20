#!/bin/bash

set -e

echo "Running Python courses..."
.venv/bin/python src/courses.py

echo "Running Python ge..."
.venv/bin/python src/ge.py

echo "Compiling Java database..."
javac -cp "lib/*" -d . src/database.java

echo "Running Java database..."
java -cp ".:lib/*" src.database

echo "All scripts completed successfully."