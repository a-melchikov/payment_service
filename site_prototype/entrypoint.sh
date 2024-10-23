#!/bin/bash

echo "Waiting for postgres..."
while ! nc -z db 5432; do
  sleep 0.1
done
echo "PostgreSQL started"

poetry run python manage.py migrate

poetry run python manage.py shell -c "from django.contrib.auth import get_user_model; User = get_user_model(); User.objects.filter(username='admin').exists() or User.objects.create_superuser('admin', 'admin@example.com', 'admin')"

poetry run python manage.py populate_db

poetry run python manage.py runserver 0.0.0.0:8000
