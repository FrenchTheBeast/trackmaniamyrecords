#!/bin/bash

# === CONFIG ===
BRANCH="main"
REPO_URL="https://github.com/FrenchTheBeast/trackmaniamyrecords"
VERSION_FILE=".deploy_version"

# === INIT ===
if [ ! -f "$VERSION_FILE" ]; then
  echo "0" > "$VERSION_FILE"
fi

VERSION=$(cat "$VERSION_FILE")
NEXT_VERSION=$((VERSION + 1))

# === GIT ACTIONS ===
echo "📦 Commit v0.0.$NEXT_VERSION en cours..."

git add .
git commit -m "Deploy v0.$NEXT_VERSION"
git push origin $BRANCH

# === VERSION UPDATE ===
echo $NEXT_VERSION > "$VERSION_FILE"
echo "✅ Pushed v0.0.$NEXT_VERSION to $BRANCH"
