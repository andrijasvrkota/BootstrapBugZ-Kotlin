#!/bin/sh

# self delete this script after execution
rm -- "$0"

clear
echo "What is the name of your app: "
APP_NAME=$(gum input --placeholder "")

clear
echo "Choose your backend: "
BACKEND=$(gum choose "spring-boot")

clear
echo "Choose your frontend: "
FRONEND=$(gum choose "svelte-kit")

# delete all other folders except the choosen one
find ./backend -mindepth 1 ! -regex "^./backend/$BACKEND\(/.*\)?" -delete
find ./frontend -mindepth 1 ! -regex "^./frontend/$FRONEND\(/.*\)?" -delete

cp -r ./backend/$BACKEND/. ./backend
cp -r ./frontend/$FRONEND/. ./frontend

rm -rf ./backend/$BACKEND
rm -rf ./frontend/$FRONEND

clear
echo "Name of your app is: $APP_NAME"
echo "You picked $BACKEND for the backend, and $FRONEND for the frontend."

git add -A
git commit -m "Initial commit."
git push