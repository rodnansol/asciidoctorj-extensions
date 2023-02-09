#!/bin/bash
set -euo pipefail

function new_entry_in_gitignore() {
   echo "Creating new entry in .gitignore: .asciidoctor"
   echo "### AsciiDoctorJ Extension Library###" >> .gitignore
   echo .asciidoctor >> .gitignore
}

function download_and_create_gitignore_entry() {
  echo "Downloading latest shaded AsciidoctorJ Extensions and moving it to the current working directory's .asciidoctor/lib folder"
  wget https://repo1.maven.org/maven2/org/rodnansol/asciidoctorj-extensions/maven-metadata.xml -O /tmp/asciidoctorj-extensions-maven-metadata.xml -q
  LATEST_VERSION=($(grep -oP '(?<=latest>)[^<]+' "/tmp/asciidoctorj-extensions-maven-metadata.xml"))
  echo "Latest version: ${LATEST_VERSION}"
  mkdir -p .asciidoctor/lib
  JAR_FILE=.asciidoctor/lib/asciidoctorj-extensions-${LATEST_VERSION}-shaded.jar
  if [ -f $JAR_FILE ]; then
    echo "Latest version is already downloaded into the .asciidoctor/lib folder"
  else
    wget https://repo1.maven.org/maven2/org/rodnansol/asciidoctorj-extensions/${LATEST_VERSION}/asciidoctorj-extensions-${LATEST_VERSION}-shaded.jar -O .asciidoctor/lib/asciidoctorj-extensions-${LATEST_VERSION}-shaded.jar
  fi
  if ! grep -q ".asciidoctor" ".gitignore"; then
   new_entry_in_gitignore
  else
    echo ".asciidoctor already exists in the .gitignore"
  fi
}

if [ `command -v wget` ]; then
  download_and_create_gitignore_entry
else
  echo "wget command must be present on your PATH before running the script, please configure your PATH"
fi
