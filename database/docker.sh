docker stop telegram-quest-bot
docker rm telegram-quest-bot
docker build -f Dockerfile -t telegram-quest-bot-img .
docker run --name telegram-quest-bot -p 127.0.0.1:5432:5432 -d telegram-quest-bot-img
