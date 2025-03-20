#!/bin/bash

# Остановка и удаление контейнера PostgreSQL
echo "Stopping and removing existing PostgreSQL container..."
docker-compose down -v

# Очистка dangling (ненужных) volume
echo "Removing unused volumes..."
docker volume prune -f

# Перезапуск контейнера
echo "Starting PostgreSQL container..."
docker-compose up -d

# Ожидание запуска
sleep 5

# Проверка статуса контейнера
docker ps | grep postgres

sleep 5
